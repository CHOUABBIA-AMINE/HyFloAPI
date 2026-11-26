/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : GroupRoleGrantService
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-26-2026
 *
 * @Type        : Class
 * @Layer       : Service
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.application.service
 *
 * @Description : Declares the module-owned application service for GroupRoleGrant.
 */

package dz.sh.hyflo.modules.foundation.identity.application.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import dz.sh.hyflo.modules.foundation.identity.application.service.SessionAuthenticationService.CurrentIdentity;
import dz.sh.hyflo.modules.foundation.identity.domain.model.*;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.*;
import dz.sh.hyflo.modules.foundation.organization.application.service.OrganizationUnitScopeService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GroupRoleGrantService {
    private final GroupRoleGrantRepository grants;
    private final GroupRepository groups;
    private final GroupMembershipRepository memberships;
    private final RoleRepository roles;
    private final RolePermissionRepository links;
    private final UserRoleGrantRepository direct;
    private final PlatformRoleGrantRepository platformGrants;
    private final PlatformAuthorizationService authorization;
    private final OrganizationUnitScopeService companies;

    public GroupRoleGrantService(GroupRoleGrantRepository grants, GroupRepository groups,
            GroupMembershipRepository memberships, RoleRepository roles, RolePermissionRepository links,
            UserRoleGrantRepository direct, PlatformRoleGrantRepository platformGrants,
            PlatformAuthorizationService authorization, OrganizationUnitScopeService companies) {
        this.grants = grants;
        this.groups = groups;
        this.memberships = memberships;
        this.roles = roles;
        this.links = links;
        this.direct = direct;
        this.platformGrants = platformGrants;
        this.authorization = authorization;
        this.companies = companies;
    }

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public List<Entry> list(CurrentIdentity principal, long groupId, long companyId, long afterId) {
        authorization.requireRead(principal, PlatformAuthorizationService.GROUP_GRANT_READ);
        if (!companies.isEligibleCompanyUnit(companyId, LocalDate.now(ZoneOffset.UTC))) throw GroupService.denied();
        groups.findById(groupId).orElseThrow(GroupService::denied);
        return grants.findGlobalPage(groupId, companyId, afterId, PageRequest.of(0, 100)).stream()
                .filter(g -> global(g, companyId)).map(GroupRoleGrantService::entry).toList();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Entry assign(CurrentIdentity principal, long groupId, long companyId, long roleId, String validTo, String reason) {
        String permission = PlatformAuthorizationService.GROUP_GRANT_ASSIGN;
        User actor = authorization.requireCatalogWrite(principal, permission);
        Instant end = UserAdministrationService.finiteExpiry(validTo);
        if (reason == null || reason.isBlank() || reason.length() > 255) throw new IllegalArgumentException("Invalid reason");
        lockCompany(companyId);
        Group group = groups.findForUpdate(groupId).orElseThrow(GroupService::denied);
        Role role = roles.findForUpdate(roleId).orElseThrow(GroupService::denied);
        if (!group.isEnabled()) throw new GroupService.GroupRejected();
        validateBundle(role);
        Instant now = Instant.now().truncatedTo(ChronoUnit.MICROS);
        if (!end.isAfter(now)) throw new IllegalArgumentException("Invalid expiry");
        for (var grant : grants.findNonEnded(groupId, now)) {
            if (!global(grant, companyId) || (grant.getRole().getId() == roleId && grant.overlapsInterval(now, end)))
                throw new GroupService.GroupRejected();
        }
        for (var membership : memberships.findNonEndedForGroup(groupId, now)) {
            User member = membership.getUser();
            if (member.getOwningOrganizationUnitId() != companyId || member.getId().equals(actor.getId())
                    || "hyflo-bootstrap".equals(member.getUsername()) || !member.getUserType().hasCode(UserType.HUMAN_CODE)
                    || !IdentityProvider.LOCAL_CODE.equals(member.getIdentityProvider().getCode())
                    || platformGrants.existsByUserId(member.getId())) throw new GroupService.GroupRejected();
        }
        var source = direct.findCompanyCandidatesForWrite(actor.getId(), companyId);
        actor = authorization.requireCatalogWrite(principal, permission);
        lockCompany(companyId);
        now = Instant.now().truncatedTo(ChronoUnit.MICROS);
        if (!end.isAfter(now)) throw new IllegalArgumentException("Invalid expiry");
        requireSource(source, actor.getId(), companyId, roleId, now, end);
        return entry(grants.save(new GroupRoleGrant(group, role, AuthorizationScope.global(companyId), reason, now, end, actor)));
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void revoke(CurrentIdentity principal, long groupId, long companyId, long grantId) {
        String permission = PlatformAuthorizationService.GROUP_GRANT_REVOKE;
        authorization.requireCatalogWrite(principal, permission);
        lockCompany(companyId);
        groups.findForUpdate(groupId).orElseThrow(GroupService::denied);
        var grant = grants.findSelectedForUpdate(grantId, groupId, companyId).filter(g -> global(g, companyId))
                .orElseThrow(GroupService::denied);
        var actor = authorization.requireCatalogWrite(principal, permission);
        lockCompany(companyId);
        Instant now = Instant.now().truncatedTo(ChronoUnit.MICROS);
        if (grant.getRevokedAt() != null || (grant.getValidTo() != null && !grant.getValidTo().isAfter(now)))
            throw new GroupService.GroupRejected();
        grant.revoke(now, actor);
    }

    /** Caller retains the exclusive provider, target user and group locks. */
    @Transactional(propagation = Propagation.MANDATORY)
    void validateMembership(User actor, User target, long groupId) {
        long companyId = target.getOwningOrganizationUnitId();
        for (var grant : grants.findNonEnded(groupId, Instant.now())) {
            if (!global(grant, companyId) || grant.getValidTo() == null || !grant.isActiveAt(Instant.now()))
                throw new GroupService.GroupRejected();
            lockCompany(companyId);
            Role role = roles.findForUpdate(grant.getRole().getId()).orElseThrow(GroupService::denied);
            validateBundle(role);
            var source = direct.findCompanyCandidatesForWrite(actor.getId(), companyId);
            Instant now = Instant.now();
            if (!grant.isActiveAt(now)) throw new GroupService.GroupRejected();
            requireSource(source, actor.getId(), companyId, role.getId(), now, grant.getValidTo());
        }
    }

    private void lockCompany(long companyId) {
        if (!companies.lockEligibleCompanyForIdentityWrite(companyId, LocalDate.now(ZoneOffset.UTC))) throw GroupService.denied();
    }

    private void validateBundle(Role role) {
        if (!role.isEnabled() || role.isPlatformRole()) throw new GroupService.GroupRejected();
        var bundle = links.findForRolesForWrite(Set.of(role.getId()));
        if (bundle.isEmpty() || bundle.stream().anyMatch(l -> !EffectiveAuthorizationService.validCompanyPermission(l.getPermission())
                || !CapabilityManifest.supports(l.getPermission()))) throw new GroupService.GroupRejected();
    }

    private static void requireSource(List<UserRoleGrant> source, long actor, long company, long role, Instant now, Instant end) {
        if (source.stream().noneMatch(g -> g.getUser().getId() == actor && g.getRole().getId() == role
                && EffectiveAuthorizationService.matchesCompany(g, company, now) && g.coversInterval(now, end)))
            throw GroupService.denied();
    }

    static boolean global(GroupRoleGrant grant, long companyId) {
        var scope = grant.getAuthorizationScope();
        return scope != null && scope.getOrganizationUnitId() == companyId && AuthorizationScope.GLOBAL.equals(scope.getScopeType())
                && scope.getScopeReferenceId() == null && !grant.getRole().isPlatformRole();
    }

    private static Entry entry(GroupRoleGrant g) {
        return new Entry(g.getId(), g.getGroup().getId(), g.getRole().getId(), g.getRole().getCode(),
                g.getAuthorizationScope().getOrganizationUnitId(), g.getAuthorizationScope().getScopeType(), g.getValidFrom(),
                g.getValidTo(), g.getRevokedAt(), g.getReason(), g.getGrantedBy().getId(),
                g.getRevokedBy() == null ? null : g.getRevokedBy().getId());
    }

    public record Entry(long id, long groupId, long roleId, String roleCode, long companyId, String scopeType,
            Instant validFrom, Instant validTo, Instant revokedAt, String reason, long grantedBy, Long revokedBy) {}
}
