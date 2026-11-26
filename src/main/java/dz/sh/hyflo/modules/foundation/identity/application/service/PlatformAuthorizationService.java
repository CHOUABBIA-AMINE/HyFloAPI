/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : PlatformAuthorizationService
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-26-2026
 *
 * @Type        : Class
 * @Layer       : Service
 * @Module      : Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.application.service
 *
 * @Description : PlatformAuthorizationService for scoped platform administration.
 */

package dz.sh.hyflo.modules.foundation.identity.application.service;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import dz.sh.hyflo.modules.foundation.identity.application.service.SessionAuthenticationService.CurrentIdentity;
import dz.sh.hyflo.modules.foundation.identity.domain.model.*;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlatformAuthorizationService {
    public static final String ROLE_READ = "platform-role.read";
    public static final String ROLE_CREATE = "platform-role.create";
    public static final String CAPABILITY_RECONCILE = "platform-capability.reconcile";
    public static final String PERMISSION_READ = "platform-permission.read";
    public static final String PERMISSION_ASSOCIATE = "platform-role-permission.associate";
    public static final String PERMISSION_REMOVE = "platform-role-permission.remove";
    public static final String ROLE_RENAME = "platform-role.rename";
    public static final String ROLE_RETIRE = "platform-role.retire";
    public static final String GRANT_READ = "platform-user-role-grant.read";
    public static final String GRANT_ASSIGN = "platform-user-role-grant.assign";
    public static final String GRANT_REVOKE = "platform-user-role-grant.revoke";
    public static final String GROUP_READ = "platform-group.read";
    public static final String GROUP_CREATE = "platform-group.create";
    public static final String GROUP_RENAME = "platform-group.rename";
    public static final String GROUP_RETIRE = "platform-group.retire";
    public static final String MEMBERSHIP_READ = "platform-group-membership.read";
    public static final String MEMBERSHIP_ASSIGN = "platform-group-membership.assign";
    public static final String MEMBERSHIP_REVOKE = "platform-group-membership.revoke";
    public static final String GROUP_GRANT_READ = "platform-group-role-grant.read";
    public static final String GROUP_GRANT_ASSIGN = "platform-group-role-grant.assign";
    public static final String GROUP_GRANT_REVOKE = "platform-group-role-grant.revoke";
    private static final Set<String> IMPLEMENTED = Set.of(ROLE_READ, ROLE_CREATE, CAPABILITY_RECONCILE,
            PERMISSION_READ, PERMISSION_ASSOCIATE, PERMISSION_REMOVE, ROLE_RENAME, ROLE_RETIRE, GRANT_READ, GRANT_ASSIGN, GRANT_REVOKE,
            GROUP_READ, GROUP_CREATE, GROUP_RENAME, GROUP_RETIRE, MEMBERSHIP_READ, MEMBERSHIP_ASSIGN, MEMBERSHIP_REVOKE, GROUP_GRANT_READ, GROUP_GRANT_ASSIGN, GROUP_GRANT_REVOKE);
    private static final Set<String> CATALOG_WRITES = Set.of(CAPABILITY_RECONCILE, PERMISSION_ASSOCIATE, PERMISSION_REMOVE, ROLE_RENAME, ROLE_RETIRE, GRANT_ASSIGN, GRANT_REVOKE,
            GROUP_CREATE, GROUP_RENAME, GROUP_RETIRE, MEMBERSHIP_ASSIGN, MEMBERSHIP_REVOKE, GROUP_GRANT_ASSIGN, GROUP_GRANT_REVOKE);
    private static final int MAX_CANDIDATES = 1000;

    private final SessionAuthenticationService authentication;
    private final UserRepository users;
    private final LoginSessionRepository sessions;
    private final IdentityProviderRepository providers;
    private final PlatformRoleGrantRepository grants;
    private final RolePermissionRepository links;

    public PlatformAuthorizationService(SessionAuthenticationService authentication, UserRepository users,
            LoginSessionRepository sessions, IdentityProviderRepository providers,
            PlatformRoleGrantRepository grants, RolePermissionRepository links) {
        this.authentication = authentication;
        this.users = users;
        this.sessions = sessions;
        this.providers = providers;
        this.grants = grants;
        this.links = links;
    }

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public void requireRead(CurrentIdentity principal, String permission) {
        requirePrincipal(principal);
        Instant now = Instant.now();
        if (authentication.resolve(principal.userId(), principal.sessionId(), now).filter(principal::equals).isEmpty())
            throw unauthenticated();
        requireImplemented(permission);
        var candidates = grants.findCandidates(principal.userId(), PageRequest.of(0, MAX_CANDIDATES + 1));
        Set<Long> roleIds = activeRoles(candidates, principal, now);
        if (roleIds.isEmpty() || !hasPermission(links.findForRoles(roleIds), roleIds, permission)) throw denied();
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public User requireWrite(CurrentIdentity principal, String permission) {
        return requireWrite(principal, permission, false);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public User requireReconciliationWrite(CurrentIdentity principal) {
        return requireCatalogWrite(principal, CAPABILITY_RECONCILE);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public User requireCatalogWrite(CurrentIdentity principal, String permission) {
        if (permission == null || !CATALOG_WRITES.contains(permission)) throw denied();
        return requireWrite(principal, permission, true);
    }

    private User requireWrite(CurrentIdentity principal, String permission, boolean exclusiveProvider) {
        requirePrincipal(principal);
        requireImplemented(permission);
        User actor = users.findForUpdate(principal.userId()).orElseThrow(PlatformAuthorizationService::unauthenticated);
        LoginSession session = sessions.findForUpdate(principal.sessionId()).orElseThrow(PlatformAuthorizationService::unauthenticated);
        IdentityProvider provider = (exclusiveProvider
                ? providers.findByCodeExclusive(IdentityProvider.LOCAL_CODE)
                : providers.findByCodeForWrite(IdentityProvider.LOCAL_CODE))
                .filter(p -> IdentityProvider.LOCAL_CODE.equals(p.getCode()) && p.isEnabled())
                .orElseThrow(PlatformAuthorizationService::unauthenticated);
        checkLive(principal, actor, session, provider, Instant.now());
        if (!actor.getUserType().hasCode(UserType.HUMAN_CODE)) throw denied();
        var candidates = grants.findCandidatesForWrite(actor.getId(), PageRequest.of(0, MAX_CANDIDATES + 1));
        Set<Long> roleIds = activeRoles(candidates, principal, Instant.now());
        if (roleIds.isEmpty()) throw denied();
        var lockedLinks = links.findForRolesForWrite(roleIds);
        Instant now = Instant.now();
        checkLive(principal, actor, session, provider, now);
        if (!hasPermission(lockedLinks, activeRoles(candidates, principal, now), permission)) throw denied();
        return actor;
    }

    // Callers retain the exclusive LOCAL provider lock for this prospective authority calculation.
    Set<String> durablePermissions(long userId, Instant now, Long excludedGrantId) {
        var candidates = grants.findCandidates(userId, PageRequest.of(0, MAX_CANDIDATES + 1));
        if (candidates.size() > MAX_CANDIDATES) return Set.of();
        Set<Long> roleIds = candidates.stream()
                .filter(g -> !java.util.Objects.equals(g.getId(), excludedGrantId))
                .filter(g -> g.getUser().getId() == userId && g.getUser().getUserType().hasCode(UserType.HUMAN_CODE)
                        && SessionAuthenticationService.eligible(g.getUser()))
                .filter(g -> g.getValidTo() == null && g.getRevokedAt() == null && g.isActiveAt(now)
                        && g.getRole().isPlatformRole() && g.getRole().isEnabled())
                .map(g -> g.getRole().getId()).collect(Collectors.toSet());
        if (roleIds.isEmpty()) return Set.of();
        return links.findForRoles(roleIds).stream()
                .filter(link -> roleIds.contains(link.getRole().getId()) && link.getRole().isPlatformRole()
                        && link.getRole().isEnabled() && EffectiveAuthorizationService.validPermission(link.getPermission())
                        && link.getPermission().isPlatformPermission() && IMPLEMENTED.contains(link.getPermission().getCode()))
                .map(link -> link.getPermission().getCode()).collect(Collectors.toSet());
    }

    static Set<String> implementedPermissions() { return IMPLEMENTED; }

    private static void requirePrincipal(CurrentIdentity principal) {
        var security = SecurityContextHolder.getContext().getAuthentication();
        if (principal == null || security == null || !security.isAuthenticated()
                || !principal.equals(security.getPrincipal())) throw unauthenticated();
    }

    private static void requireImplemented(String permission) {
        if (permission == null || !IMPLEMENTED.contains(permission)) throw denied();
    }

    private static void checkLive(CurrentIdentity principal, User actor, LoginSession session,
            IdentityProvider provider, Instant now) {
        if (actor.getId() != principal.userId() || session.getUser().getId() != principal.userId()
                || !session.isActiveAt(now) || !principal.username().equals(actor.getUsername())
                || !provider.getId().equals(actor.getIdentityProvider().getId())
                || !SessionAuthenticationService.eligible(actor)) throw unauthenticated();
    }

    private static Set<Long> activeRoles(List<PlatformRoleGrant> candidates, CurrentIdentity principal, Instant now) {
        if (candidates.size() > MAX_CANDIDATES) throw denied();
        return candidates.stream()
                .filter(g -> g.getUser().getId() == principal.userId()
                        && principal.username().equals(g.getUser().getUsername())
                        && g.getUser().getUserType().hasCode(UserType.HUMAN_CODE)
                        && SessionAuthenticationService.eligible(g.getUser()))
                .filter(g -> g.isActiveAt(now) && g.getRole().isPlatformRole() && g.getRole().isEnabled())
                .map(g -> g.getRole().getId()).collect(Collectors.toSet());
    }

    private static boolean hasPermission(List<RolePermission> candidates, Set<Long> roles, String permission) {
        return candidates.stream().anyMatch(link -> roles.contains(link.getRole().getId())
                && link.getRole().isPlatformRole() && link.getRole().isEnabled()
                && EffectiveAuthorizationService.validPermission(link.getPermission())
                && link.getPermission().isPlatformPermission() && permission.equals(link.getPermission().getCode()));
    }

    private static AccessDeniedException denied() { return new AccessDeniedException("Access denied"); }
    private static InsufficientAuthenticationException unauthenticated() {
        return new InsufficientAuthenticationException("Authentication required");
    }
}
