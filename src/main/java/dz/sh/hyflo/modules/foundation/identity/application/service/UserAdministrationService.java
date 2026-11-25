/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : UserAdministrationService
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Service
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.application.service
 *
 * @Description : Enforces company-owned account lifecycle and authorization in the mutation transaction.
 */

package dz.sh.hyflo.modules.foundation.identity.application.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import dz.sh.hyflo.modules.foundation.identity.application.service.SessionAuthenticationService.CurrentIdentity;
import dz.sh.hyflo.modules.foundation.identity.domain.model.*;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.*;
import dz.sh.hyflo.modules.foundation.organization.application.service.OrganizationUnitScopeService;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserAdministrationService {
    private final EffectiveAuthorizationService authorization;
    private final UserRepository users;
    private final LoginSessionRepository sessions;
    private final IdentityProviderRepository providers;
    private final UserRoleGrantRepository grants;
    private final RolePermissionRepository permissions;
    private final OrganizationUnitScopeService companies;
    private final UserStatusRepository statuses;
    private final UserTypeRepository types;
    private final LocalCredentialRepository credentials;
    private final UserLifecycleEventService events;

    public UserAdministrationService(EffectiveAuthorizationService authorization, UserRepository users,
            LoginSessionRepository sessions, IdentityProviderRepository providers, UserRoleGrantRepository grants,
            RolePermissionRepository permissions, OrganizationUnitScopeService companies, UserStatusRepository statuses,
            UserTypeRepository types, LocalCredentialRepository credentials, UserLifecycleEventService events) {
        this.authorization = authorization;
        this.users = users;
        this.sessions = sessions;
        this.providers = providers;
        this.grants = grants;
        this.permissions = permissions;
        this.companies = companies;
        this.statuses = statuses;
        this.types = types;
        this.credentials = credentials;
        this.events = events;
    }

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public Account read(CurrentIdentity principal, long companyId, long userId) {
        authorization.requireCompanyPermission(principal, companyId, "user.read");
        return account(users.findOwned(userId, companyId).orElseThrow(UserAdministrationService::denied));
    }

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public List<Account> list(CurrentIdentity principal, long companyId, long afterId) {
        authorization.requireCompanyPermission(principal, companyId, "user.read");
        return users.findOwnedPage(companyId, afterId, PageRequest.of(0, 100)).stream()
                .map(UserAdministrationService::account).toList();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Account create(CurrentIdentity principal, long companyId, String username, String email,
            String typeCode, String reason, String correlation) {
        User actor = lockAuthority(principal, companyId, "user.create");
        String normalized = User.normalizeUsername(username);
        if ("hyflo-bootstrap".equals(normalized) || "hyflo-bootstrap@hyflo.invalid".equalsIgnoreCase(email)
                || !(UserType.HUMAN_CODE.equals(typeCode) || UserType.SERVICE_CODE.equals(typeCode))) {
            throw new IllegalArgumentException("Invalid user input");
        }
        UserType type = types.findByCode(typeCode).filter(t -> t.hasCode(typeCode)).orElseThrow();
        IdentityProvider provider = providers.findByCodeForWrite(IdentityProvider.LOCAL_CODE)
                .filter(p -> IdentityProvider.LOCAL_CODE.equals(p.getCode()) && p.isEnabled()).orElseThrow();
        // Re-evaluate temporal conditions after all lookups, before inserting a globally unique account.
        actor = lockAuthority(principal, companyId, "user.create");
        Instant now = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        User user = users.save(new User(provider, normalized, email, null, companyId, status(UserStatus.PENDING_CODE), type));
        events.record(UserLifecycleEvent.CREATED, now, correlation, reason, null, user, actor);
        return account(user);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Account transition(CurrentIdentity principal, long companyId, long userId, String operation,
            String reason, String correlation) {
        String permission = switch (operation) {
            case "activate" -> "user.activate";
            case "suspend" -> "user.suspend";
            case "reactivate" -> "user.reactivate";
            case "disable" -> "user.disable";
            default -> throw new IllegalArgumentException("Unknown operation");
        };
        User actor = lockAuthority(principal, companyId, permission);
        if (actor.getId() == userId) throw denied();
        User target = users.findOwnedForUpdate(userId, companyId).orElseThrow(UserAdministrationService::denied);
        actor = lockAuthority(principal, companyId, permission);
        if (target.getOwningOrganizationUnitId() != companyId || "hyflo-bootstrap".equals(target.getUsername())
                || !IdentityProvider.LOCAL_CODE.equals(target.getIdentityProvider().getCode())) throw denied();

        String previous = target.getUserStatus().getCode();
        String event;
        Instant now = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        switch (operation) {
            case "activate", "reactivate" -> {
                String expected = operation.equals("activate") ? UserStatus.PENDING_CODE : UserStatus.SUSPENDED_CODE;
                if (!expected.equals(previous) || !target.getIdentityProvider().isEnabled()) throw rejected();
                LocalCredential credential = credentials.findByUser_Id(userId).orElseThrow(UserAdministrationService::rejected);
                target.activate(credential, status(UserStatus.ACTIVE_CODE));
                event = operation.equals("activate") ? UserLifecycleEvent.ACTIVATED : UserLifecycleEvent.REACTIVATED;
            }
            case "suspend" -> {
                if (!UserStatus.ACTIVE_CODE.equals(previous)) throw rejected();
                target.suspend(status(UserStatus.SUSPENDED_CODE));
                revokeSessions(userId, now);
                event = UserLifecycleEvent.SUSPENDED;
            }
            case "disable" -> {
                if (!(UserStatus.PENDING_CODE.equals(previous) || UserStatus.ACTIVE_CODE.equals(previous)
                        || UserStatus.SUSPENDED_CODE.equals(previous))) throw rejected();
                target.disable(status(UserStatus.DISABLED_CODE));
                revokeSessions(userId, now);
                event = UserLifecycleEvent.DISABLED;
            }
            default -> throw new IllegalArgumentException("Unknown operation");
        }
        // The target row remains locked. If expiry/revocation is encountered while waiting on sessions,
        // fail closed and roll back both lifecycle and session mutations.
        actor = lockAuthority(principal, companyId, permission);
        events.record(event, now, correlation, reason, previous, target, actor);
        return account(target);
    }

    private void revokeSessions(long userId, Instant now) {
        for (LoginSession session : sessions.findUnrevokedForUpdate(userId)) {
            session.revoke(now.isBefore(session.getStartedAt()) ? session.getStartedAt() : now);
        }
    }

    /** All locks are held by the caller's existing write transaction; never use the read gate for writes. */
    private User lockAuthority(CurrentIdentity principal, long companyId, String requiredPermission) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (principal == null || authentication == null || !authentication.isAuthenticated()
                || !principal.equals(authentication.getPrincipal())) throw unauthenticated();
        if (companyId <= 0) throw new IllegalArgumentException("Invalid company");
        User actor = users.findForUpdate(principal.userId()).orElseThrow(UserAdministrationService::unauthenticated);
        LoginSession session = sessions.findForUpdate(principal.sessionId()).orElseThrow(UserAdministrationService::unauthenticated);
        IdentityProvider provider = providers.findByCodeForWrite(IdentityProvider.LOCAL_CODE)
                .filter(p -> IdentityProvider.LOCAL_CODE.equals(p.getCode()) && p.isEnabled())
                .orElseThrow(UserAdministrationService::unauthenticated);
        Instant now = Instant.now();
        if (session.getUser().getId() != principal.userId() || !session.isActiveAt(now)
                || !principal.username().equals(actor.getUsername()) || !provider.getId().equals(actor.getIdentityProvider().getId())
                || !SessionAuthenticationService.eligible(actor)) throw unauthenticated();

        var candidates = grants.findCompanyCandidatesForWrite(actor.getId(), companyId);
        Instant candidateTime = Instant.now();
        Set<Long> roleIds = candidates.stream()
                .filter(g -> g.getUser().getId().equals(actor.getId()))
                .filter(g -> EffectiveAuthorizationService.matchesCompany(g, companyId, candidateTime))
                .map(g -> g.getRole().getId()).collect(Collectors.toSet());
        if (roleIds.isEmpty()) throw denied();
        var links = permissions.findForRolesForWrite(roleIds);
        if (!links.stream().anyMatch(link -> roleIds.contains(link.getRole().getId()) && link.getRole().isEnabled()
                && EffectiveAuthorizationService.validPermission(link.getPermission())
                && requiredPermission.equals(link.getPermission().getCode()))) throw denied();
        if (!companies.lockEligibleCompanyForIdentityWrite(companyId, LocalDate.now(ZoneOffset.UTC))) throw denied();
        Instant finalTime = Instant.now();
        if (!session.isActiveAt(finalTime)) throw unauthenticated();
        Set<Long> activeRoles = candidates.stream()
                .filter(g -> g.getUser().getId().equals(actor.getId()))
                .filter(g -> EffectiveAuthorizationService.matchesCompany(g, companyId, finalTime))
                .map(g -> g.getRole().getId()).collect(Collectors.toSet());
        if (!links.stream().anyMatch(link -> activeRoles.contains(link.getRole().getId()) && link.getRole().isEnabled()
                && EffectiveAuthorizationService.validPermission(link.getPermission())
                && requiredPermission.equals(link.getPermission().getCode()))
                || !companies.lockEligibleCompanyForIdentityWrite(companyId, LocalDate.ofInstant(finalTime, ZoneOffset.UTC))) throw denied();
        return actor;
    }

    private UserStatus status(String code) {
        return statuses.findByCode(code).filter(s -> s.hasCode(code)).orElseThrow();
    }

    private static Account account(User user) {
        return new Account(user.getId(), user.getUsername(), user.getEmail(), user.getOwningOrganizationUnitId(),
                user.getUserType().getCode(), user.getUserStatus().getCode());
    }
    private static AccessDeniedException denied() { return new AccessDeniedException("Access denied"); }
    private static InsufficientAuthenticationException unauthenticated() { return new InsufficientAuthenticationException("Authentication required"); }
    private static AccountOperationRejected rejected() { return new AccountOperationRejected(); }

    public static final class AccountOperationRejected extends RuntimeException {
        private AccountOperationRejected() { super("Account operation rejected"); }
    }

    public record Account(long id, String username, String email, long companyId, String userTypeCode, String userStatusCode) {}
}
