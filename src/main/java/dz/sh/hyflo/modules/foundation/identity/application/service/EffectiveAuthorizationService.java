/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : EffectiveAuthorizationService
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Service
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.application.service
 *
 * @Description : Resolves current direct company permissions using an authenticated principal and owner eligibility.
 */

package dz.sh.hyflo.modules.foundation.identity.application.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.HashSet;
import dz.sh.hyflo.modules.foundation.identity.domain.model.UserType;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.GroupMembershipRepository;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.GroupRoleGrantRepository;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.PlatformRoleGrantRepository;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import dz.sh.hyflo.modules.foundation.identity.application.service.SessionAuthenticationService.CurrentIdentity;
import dz.sh.hyflo.modules.foundation.identity.domain.model.AuthorizationScope;
import dz.sh.hyflo.modules.foundation.identity.domain.model.Permission;
import dz.sh.hyflo.modules.foundation.identity.domain.model.UserRoleGrant;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.RolePermissionRepository;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.UserRoleGrantRepository;
import dz.sh.hyflo.modules.foundation.organization.application.service.OrganizationUnitScopeService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EffectiveAuthorizationService {

    private static final Pattern CODE_PART = Pattern.compile("[A-Za-z0-9_-]+");

    private final SessionAuthenticationService sessions;
    private final UserRoleGrantRepository grants;
    private final RolePermissionRepository rolePermissions;
    private final OrganizationUnitScopeService companies;
    private final GroupMembershipRepository memberships;
    private final GroupRoleGrantRepository groupGrants;
    private final PlatformRoleGrantRepository platformGrants;

    public EffectiveAuthorizationService(SessionAuthenticationService sessions, UserRoleGrantRepository grants,
            RolePermissionRepository rolePermissions, OrganizationUnitScopeService companies,
            GroupMembershipRepository memberships, GroupRoleGrantRepository groupGrants, PlatformRoleGrantRepository platformGrants) {
        this.sessions = sessions;
        this.grants = grants;
        this.rolePermissions = rolePermissions;
        this.companies = companies;
        this.memberships = memberships;
        this.groupGrants = groupGrants;
        this.platformGrants = platformGrants;
    }

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public CompanyPermissions ownPermissions(CurrentIdentity principal, long companyId) {
        Instant now = Instant.now();
        Set<String> codes = resolveCandidates(principal, companyId, now);
        if (codes.isEmpty() || !eligibleCompany(companyId, now)) {
            throw denied();
        }
        return new CompanyPermissions(companyId, List.copyOf(codes));
    }

    /** Company-level read capabilities only. The caller must select the operation's exact permission. */
    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public void requireCompanyPermission(CurrentIdentity principal, long companyId, String requiredPermission) {
        Instant now = Instant.now();
        Set<String> codes = resolveCandidates(principal, companyId, now);
        if (requiredPermission == null || !codes.contains(requiredPermission) || !eligibleCompany(companyId, now)) {
            throw denied();
        }
    }

    private Set<String> resolveCandidates(CurrentIdentity principal, long companyId, Instant now) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (principal == null || authentication == null || !authentication.isAuthenticated()
                || !principal.equals(authentication.getPrincipal())
                || sessions.resolve(principal.userId(), principal.sessionId(), now).filter(principal::equals).isEmpty()) {
            throw new InsufficientAuthenticationException("Authentication required");
        }
        if (companyId <= 0) {
            throw new IllegalArgumentException("Invalid company selector");
        }

        Set<Long> roleIds = grants.findCompanyCandidates(principal.userId(), companyId).stream()
                .filter(grant -> principal.userId() == grant.getUser().getId())
                .filter(grant -> matchesCompany(grant, companyId, now))
                .map(grant -> grant.getRole().getId())
                .collect(Collectors.toSet());
        Set<Long> inherited = inheritedRoleIds(principal.userId(), companyId, now);
        Set<Long> allRoles = new HashSet<>(roleIds);
        allRoles.addAll(inherited);
        if (allRoles.isEmpty()) return Set.of();
        return rolePermissions.findForRoles(allRoles).stream()
                .filter(link -> permitsLink(roleIds, inherited, link))
                .map(link -> link.getPermission())
                .filter(EffectiveAuthorizationService::validCompanyPermission)
                .map(Permission::getCode)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    // Same eligibility rules for both the read snapshot and the provider-locked write decision.
    Set<Long> inheritedRoleIds(long userId, long companyId, Instant now) {
        Set<Long> groupIds = memberships.findForUser(userId).stream()
                .filter(m -> m.getUser().getId() == userId && m.getUser().getOwningOrganizationUnitId() == companyId
                        && SessionAuthenticationService.eligible(m.getUser()) && m.getUser().getUserType().hasCode(UserType.HUMAN_CODE)
                        && m.getGroup().isEnabled() && m.isActiveAt(now))
                .map(m -> m.getGroup().getId()).collect(Collectors.toSet());
        if (groupIds.isEmpty() || platformGrants.existsByUserId(userId)) return Set.of();
        return groupGrants.findCompanyCandidates(groupIds, companyId).stream()
                .filter(g -> groupIds.contains(g.getGroup().getId()) && g.getGroup().isEnabled()
                        && GroupRoleGrantService.global(g, companyId) && g.getRole().isEnabled()
                        && g.getValidTo() != null && g.isActiveAt(now))
                .map(g -> g.getRole().getId()).collect(Collectors.toSet());
    }

    static boolean permitsLink(Set<Long> direct, Set<Long> inherited,
            dz.sh.hyflo.modules.foundation.identity.domain.model.RolePermission link) {
        long roleId = link.getRole().getId();
        return link.getRole().isEnabled() && !link.getRole().isPlatformRole()
                && validCompanyPermission(link.getPermission())
                && (direct.contains(roleId) || (inherited.contains(roleId) && CapabilityManifest.supports(link.getPermission())));
    }

    static boolean matchesCompany(UserRoleGrant grant, long companyId, Instant now) {
        AuthorizationScope scope = grant.getAuthorizationScope();
        return scope != null && AuthorizationScope.GLOBAL.equals(scope.getScopeType())
                && scope.getOrganizationUnitId() == companyId && scope.getScopeReferenceId() == null
                && grant.isActiveAt(now) && grant.getRole().isEnabled() && !grant.getRole().isPlatformRole();
    }

    static boolean validCompanyPermission(Permission permission) {
        return validPermission(permission) && !permission.isPlatformPermission();
    }

    static boolean validPermission(Permission permission) {
        String resource = permission.getResourceType().getCode();
        String action = permission.getAction().getCode();
        return resource != null && action != null && CODE_PART.matcher(resource).matches()
                && CODE_PART.matcher(action).matches() && (resource + "." + action).equals(permission.getCode());
    }

    private boolean eligibleCompany(long companyId, Instant now) {
        return companies.isEligibleCompanyUnit(companyId, LocalDate.ofInstant(now, ZoneOffset.UTC));
    }

    private static AccessDeniedException denied() {
        return new AccessDeniedException("Access denied");
    }

    public record CompanyPermissions(long companyId, List<String> permissionCodes) {
        public CompanyPermissions {
            permissionCodes = List.copyOf(permissionCodes);
        }
    }
}
