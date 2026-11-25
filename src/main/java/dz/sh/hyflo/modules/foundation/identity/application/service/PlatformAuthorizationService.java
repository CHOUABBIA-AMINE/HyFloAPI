/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : PlatformAuthorizationService
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
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
    private static final Set<String> IMPLEMENTED = Set.of(ROLE_READ, ROLE_CREATE, CAPABILITY_RECONCILE);
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
        return requireWrite(principal, CAPABILITY_RECONCILE, true);
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
