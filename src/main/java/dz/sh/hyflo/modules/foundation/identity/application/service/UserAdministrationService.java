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

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import dz.sh.hyflo.modules.foundation.identity.application.service.SessionAuthenticationService.CurrentIdentity;
import dz.sh.hyflo.modules.foundation.identity.domain.model.*;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.*;
import dz.sh.hyflo.modules.foundation.organization.application.service.OrganizationUnitScopeService;
import dz.sh.hyflo.modules.foundation.organization.application.service.EmployeeAssignmentService;
import dz.sh.hyflo.platform.security.PasswordEncodingConfiguration.IdentityPasswordEncoder;
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
    private final PlatformRoleGrantRepository platformGrants;
    private final RolePermissionRepository permissions;
    private final OrganizationUnitScopeService companies;
    private final UserStatusRepository statuses;
    private final UserTypeRepository types;
    private final LocalCredentialRepository credentials;
    private final UserLifecycleEventService events;
    private final IdentityPasswordEncoder encoder;
    private final EmployeeAssignmentService employeeAssignments;
    private final UserEmployeeAssociationEventService employeeEvents;

    public UserAdministrationService(EffectiveAuthorizationService authorization, UserRepository users,
            LoginSessionRepository sessions, IdentityProviderRepository providers, UserRoleGrantRepository grants,
            RolePermissionRepository permissions, OrganizationUnitScopeService companies, UserStatusRepository statuses,
            UserTypeRepository types, LocalCredentialRepository credentials, UserLifecycleEventService events,
            IdentityPasswordEncoder encoder, EmployeeAssignmentService employeeAssignments,
            UserEmployeeAssociationEventService employeeEvents, PlatformRoleGrantRepository platformGrants) {
        this.authorization = authorization;
        this.users = users;
        this.sessions = sessions;
        this.providers = providers;
        this.grants = grants;
        this.platformGrants = platformGrants;
        this.permissions = permissions;
        this.companies = companies;
        this.statuses = statuses;
        this.types = types;
        this.credentials = credentials;
        this.events = events;
        this.encoder = encoder;
        this.employeeAssignments = employeeAssignments;
        this.employeeEvents = employeeEvents;
    }

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public Account read(CurrentIdentity principal, long companyId, long userId) {
        authorization.requireCompanyPermission(principal, companyId, "user.read");
        return account(users.findOwned(userId, companyId).orElseThrow(UserAdministrationService::denied));
    }

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public List<Account> list(CurrentIdentity principal, long companyId, long afterId,
            String usernamePrefix, String email, String userStatusCode, String userTypeCode) {
        if (afterId < 0) throw new IllegalArgumentException("Invalid account cursor");
        String prefix = normalizeSearchPrefix(usernamePrefix);
        String address = normalizeSearchEmail(email);
        if (userStatusCode != null && !Set.of(UserStatus.PENDING_CODE, UserStatus.ACTIVE_CODE,
                UserStatus.SUSPENDED_CODE, UserStatus.DISABLED_CODE).contains(userStatusCode)) {
            throw new IllegalArgumentException("Invalid account status filter");
        }
        if (userTypeCode != null && !Set.of(UserType.HUMAN_CODE, UserType.SERVICE_CODE).contains(userTypeCode)) {
            throw new IllegalArgumentException("Invalid account type filter");
        }
        authorization.requireCompanyPermission(principal, companyId, "user.read");
        return users.findOwnedSearchPage(companyId, afterId, prefix, address, userStatusCode, userTypeCode,
                PageRequest.of(0, 100)).stream().map(UserAdministrationService::account).toList();
    }

    private static String normalizeSearchPrefix(String value) {
        if (value == null) return null;
        String candidate = value.strip();
        if (!candidate.matches("[a-zA-Z0-9._-]{1,20}")) {
            throw new IllegalArgumentException("Invalid username prefix");
        }
        return candidate.toLowerCase(Locale.ROOT);
    }

    private static String normalizeSearchEmail(String value) {
        if (value == null) return null;
        String candidate = value.strip();
        if (candidate.isEmpty() || candidate.length() > 100 || candidate.codePoints()
                .anyMatch(c -> Character.isWhitespace(c) || Character.isSpaceChar(c) || Character.isISOControl(c))) {
            throw new IllegalArgumentException("Invalid email filter");
        }
        return candidate;
    }

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public EmployeeAssociation employeeAssociation(CurrentIdentity principal, long companyId, long userId) {
        authorization.requireCompanyPermission(principal, companyId, "user-employee.read");
        if (principal.userId() == userId) throw denied();
        User target = users.findOwned(userId, companyId).orElseThrow(UserAdministrationService::denied);
        requireEmployeeTarget(target, companyId);
        return new EmployeeAssociation(target.getId(), target.getEmployeeId());
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void changeEmployeeAssociation(CurrentIdentity principal, long companyId, long userId,
            Long employeeId, String reason, String correlation) {
        if (employeeId != null && employeeId <= 0 || reason == null || reason.isBlank() || reason.length() > 255) {
            throw new IllegalArgumentException("Invalid employee association request");
        }
        String permission = "user-employee.associate";
        User actor = lockAuthority(principal, companyId, permission);
        if (actor.getId() == userId) throw denied();
        User target = users.findOwnedForUpdate(userId, companyId).orElseThrow(UserAdministrationService::denied);
        rejectPlatformManaged(target);
        requireEmployeeTarget(target, companyId);
        String status = target.getUserStatus().getCode();
        boolean linking = employeeId != null;
        if (!(UserStatus.PENDING_CODE.equals(status) || UserStatus.ACTIVE_CODE.equals(status)
                || UserStatus.SUSPENDED_CODE.equals(status) || (!linking && UserStatus.DISABLED_CODE.equals(status)))) {
            throw denied();
        }
        Long previous = target.getEmployeeId();
        LocalDate decisionDate = LocalDate.now(ZoneOffset.UTC);
        if (linking) {
            if (!employeeAssignments.lockEligibleEmployeeForIdentity(employeeId, companyId, decisionDate)) throw denied();
            if (previous != null || users.countLinkedEmployee(employeeId) != 0) throw rejected();
        } else if (previous == null) {
            throw rejected();
        }
        actor = lockAuthority(principal, companyId, permission);
        Instant now = Instant.now().truncatedTo(ChronoUnit.MICROS);
        if (linking && !decisionDate.equals(LocalDate.ofInstant(now, ZoneOffset.UTC))) throw denied();
        if (linking) target.linkEmployee(employeeId);
        else target.unlinkEmployee();
        employeeEvents.record(linking ? UserEmployeeAssociationEvent.LINKED : UserEmployeeAssociationEvent.UNLINKED,
                now, correlation, reason, previous, target, actor);
    }

    private static void requireEmployeeTarget(User target, long companyId) {
        if (target.getOwningOrganizationUnitId() != companyId || "hyflo-bootstrap".equals(target.getUsername())
                || !IdentityProvider.LOCAL_CODE.equals(target.getIdentityProvider().getCode())
                || !target.getIdentityProvider().isEnabled() || !target.getUserType().hasCode(UserType.HUMAN_CODE)) {
            throw denied();
        }
    }

    public record EmployeeAssociation(long userId, Long employeeId) {}

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
        rejectPlatformManaged(target);
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

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void initializeCredential(CurrentIdentity principal, long companyId, long userId,
            String password, String reason, String correlation) {
        mutateCredential(principal, companyId, userId, password, reason, correlation, true);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void resetCredential(CurrentIdentity principal, long companyId, long userId,
            String password, String reason, String correlation) {
        mutateCredential(principal, companyId, userId, password, reason, correlation, false);
    }

    private void mutateCredential(CurrentIdentity principal, long companyId, long userId,
            String password, String reason, String correlation, boolean initialize) {
        String permission = initialize ? "credential.initialize" : "credential.reset";
        User actor = lockAuthority(principal, companyId, permission);
        if (actor.getId() == userId) throw denied();
        User target = users.findOwnedForUpdate(userId, companyId).orElseThrow(UserAdministrationService::denied);
        rejectPlatformManaged(target);
        actor = lockAuthority(principal, companyId, permission);
        if (target.getOwningOrganizationUnitId() != companyId || "hyflo-bootstrap".equals(target.getUsername())
                || !IdentityProvider.LOCAL_CODE.equals(target.getIdentityProvider().getCode())
                || !target.getIdentityProvider().isEnabled()) throw denied();
        if (password == null || password.isBlank() || password.codePointCount(0, password.length()) < 15
                || password.getBytes(StandardCharsets.UTF_8).length > 72
                || reason == null || reason.isBlank() || reason.length() > 255 || reason.contains(password)) {
            throw new IllegalArgumentException("Invalid credential input");
        }
        String previous = target.getUserStatus().getCode();
        boolean pending = UserStatus.PENDING_CODE.equals(previous);
        if (initialize ? !pending : !(pending || UserStatus.ACTIVE_CODE.equals(previous)
                || UserStatus.SUSPENDED_CODE.equals(previous))) throw rejected();

        LocalCredential existing = credentials.findByUser_Id(userId).orElse(null);
        if (initialize == (existing != null)) throw rejected();
        if (existing != null && LocalCredential.supportsPasswordHash(existing.getPasswordHash())
                && encoder.matches(password, existing.getPasswordHash())) throw rejected();

        String encoded = encoder.encode(password);
        if (!LocalCredential.supportsPasswordHash(encoded)) throw new IllegalStateException("Unsupported encoded credential");
        // Encoding is deliberately inside the user/authority locks; recheck after the expensive work.
        actor = lockAuthority(principal, companyId, permission);
        Instant now = Instant.now().truncatedTo(ChronoUnit.MICROS);
        if (initialize) {
            credentials.save(new LocalCredential(target, encoded, now));
        } else {
            existing.replacePasswordHash(encoded, now);
            revokeSessions(userId, now);
        }
        actor = lockAuthority(principal, companyId, permission);
        events.record(initialize ? UserLifecycleEvent.CREDENTIAL_INITIALIZED : UserLifecycleEvent.CREDENTIAL_RESET,
                now, correlation, reason, previous, target, actor);
    }

    /** Administratively revoke every live session of an owned account, without changing its lifecycle. */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void revokeAccountSessions(CurrentIdentity principal, long companyId, long userId,
            String reason, String correlation) {
        String permission = "login-session.revoke";
        User actor = lockAuthority(principal, companyId, permission);
        if (actor.getId() == userId) throw denied();
        User target = users.findOwnedForUpdate(userId, companyId).orElseThrow(UserAdministrationService::denied);
        rejectPlatformManaged(target);
        actor = lockAuthority(principal, companyId, permission);
        if (target.getOwningOrganizationUnitId() != companyId || "hyflo-bootstrap".equals(target.getUsername())
                || !IdentityProvider.LOCAL_CODE.equals(target.getIdentityProvider().getCode())
                || !target.getIdentityProvider().isEnabled()) throw denied();
        if (reason == null || reason.isBlank() || reason.length() > 255) {
            throw new IllegalArgumentException("Invalid revocation reason");
        }
        String previous = target.getUserStatus().getCode();
        if (!(UserStatus.PENDING_CODE.equals(previous) || UserStatus.ACTIVE_CODE.equals(previous)
                || UserStatus.SUSPENDED_CODE.equals(previous) || UserStatus.DISABLED_CODE.equals(previous))) throw rejected();

        // The owner row serializes login, refresh, logout and other session writers.
        var candidates = sessions.findUnrevokedForUpdate(userId);
        actor = lockAuthority(principal, companyId, permission);
        Instant now = Instant.now().truncatedTo(ChronoUnit.MICROS);
        var live = candidates.stream()
                .filter(session -> session.getRevokedAt() == null && session.isActiveAt(now))
                .toList();
        if (live.isEmpty()) throw rejected();
        live.forEach(session -> session.revoke(now));
        actor = lockAuthority(principal, companyId, permission);
        events.record(UserLifecycleEvent.SESSIONS_REVOKED, now, correlation, reason, previous, target, actor);
    }

    // Only explicitly classified catalog reads may be delegated to an account controlled by another company.
    private static final Set<String> FOREIGN_DELEGABLE = Set.of("identity-catalog.read", "role.read", "permission.read");

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public List<Grant> listRoleGrants(CurrentIdentity principal, long companyId, long userId, long afterId) {
        authorization.requireCompanyPermission(principal, companyId, "user-role-grant.read");
        return grants.findGlobalPage(userId, companyId, afterId, PageRequest.of(0, 100)).stream()
                .filter(grant -> globalCompany(grant, companyId) && grant.getUser().getId() == userId
                        && !"hyflo-bootstrap".equals(grant.getUser().getUsername()))
                .map(UserAdministrationService::grantProjection).toList();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Grant assignRoleGrant(CurrentIdentity principal, long companyId, long userId, long roleId,
            String validTo, String reason) {
        String permission = "user-role-grant.assign";
        User actor = lockAuthority(principal, companyId, permission);
        User target = lockGrantTarget(actor, userId);
        if (!IdentityProvider.LOCAL_CODE.equals(target.getIdentityProvider().getCode())
                || !target.getIdentityProvider().isEnabled()) throw denied();
        String targetStatus = target.getUserStatus().getCode();
        if (!(UserStatus.PENDING_CODE.equals(targetStatus) || UserStatus.ACTIVE_CODE.equals(targetStatus)
                || UserStatus.SUSPENDED_CODE.equals(targetStatus))) throw rejected();
        Instant end = finiteExpiry(validTo);
        if (reason == null || reason.isBlank() || reason.length() > 255 || roleId <= 0) {
            throw new IllegalArgumentException("Invalid grant input");
        }

        // The actor's existing exact-role authority supplies the role; no unscoped catalog lookup.
        var sourceGrants = grants.findCompanyCandidatesForWrite(actor.getId(), companyId);
        var source = sourceGrants.stream()
                .filter(g -> g.getUser().getId() == principal.userId())
                .filter(g -> EffectiveAuthorizationService.matchesCompany(g, companyId, Instant.now()))
                .filter(g -> g.getRole().getId() == roleId)
                .findFirst().orElseThrow(UserAdministrationService::denied);
        Role role = source.getRole();
        var links = permissions.findForRolesForWrite(Set.of(roleId));
        if (links.isEmpty() || links.stream().anyMatch(link -> link.getRole().getId() != roleId
                || !EffectiveAuthorizationService.validCompanyPermission(link.getPermission()))) throw denied();
        if (target.getOwningOrganizationUnitId() != companyId && links.stream()
                .anyMatch(link -> !FOREIGN_DELEGABLE.contains(link.getPermission().getCode()))) throw denied();

        // Every writer serializes on the recipient User before checking for interval overlap.
        var targetGrants = grants.findCompanyCandidatesForWrite(userId, companyId);
        actor = lockAuthority(principal, companyId, permission);
        Instant now = Instant.now().truncatedTo(ChronoUnit.MICROS);
        if (!end.isAfter(now)) throw new IllegalArgumentException("Grant expiry must be in the future");
        long actorId = actor.getId();
        if (!role.isEnabled() || sourceGrants.stream().noneMatch(g -> g.getUser().getId() == actorId
                && g.getRole().getId() == roleId && EffectiveAuthorizationService.matchesCompany(g, companyId, now)
                && g.coversInterval(now, end))) throw denied();
        if (targetGrants.stream().anyMatch(g -> g.getUser().getId() == userId && g.getRole().getId() == roleId
                && globalCompany(g, companyId) && g.overlapsInterval(now, end))) throw rejected();

        return grantProjection(grants.save(new UserRoleGrant(target, role, AuthorizationScope.global(companyId),
                reason, now, end, actor)));
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void revokeRoleGrant(CurrentIdentity principal, long companyId, long userId, long grantId) {
        String permission = "user-role-grant.revoke";
        User actor = lockAuthority(principal, companyId, permission);
        lockGrantTarget(actor, userId);
        UserRoleGrant grant = grants.findSelectedForUpdate(grantId, userId, companyId)
                .orElseThrow(UserAdministrationService::denied);
        if (grant.getUser().getId() != userId || !globalCompany(grant, companyId)
                || "hyflo-bootstrap".equals(grant.getGrantedBy().getUsername())) throw denied();
        actor = lockAuthority(principal, companyId, permission);
        Instant now = Instant.now().truncatedTo(ChronoUnit.MICROS);
        if (grant.getRevokedAt() != null || (grant.getValidTo() != null && !now.isBefore(grant.getValidTo()))) {
            throw rejected();
        }
        grant.revoke(now, actor);
    }

    private User lockGrantTarget(User actor, long userId) {
        if (actor.getId() == userId) throw denied();
        User target = users.findForUpdate(userId).filter(user -> !"hyflo-bootstrap".equals(user.getUsername()))
                .orElseThrow(UserAdministrationService::denied);
        rejectPlatformManaged(target);
        return target;
    }

    /** Caller already holds the target User lock, including for historical platform authority. */
    private void rejectPlatformManaged(User target) {
        if (platformGrants.existsByUserId(target.getId())) throw denied();
    }

    private static Instant finiteExpiry(String text) {
        if (text == null || text.length() > 40) throw new IllegalArgumentException("Invalid grant expiry");
        try {
            Instant end = Instant.parse(text);
            if (end.isAfter(Instant.parse("9999-12-31T23:59:59.999999Z"))
                    || !end.equals(end.truncatedTo(ChronoUnit.MICROS))) {
                throw new IllegalArgumentException("Invalid grant expiry");
            }
            return end;
        } catch (java.time.DateTimeException invalid) {
            throw new IllegalArgumentException("Invalid grant expiry");
        }
    }

    private static boolean globalCompany(UserRoleGrant grant, long companyId) {
        AuthorizationScope scope = grant.getAuthorizationScope();
        return scope != null && AuthorizationScope.GLOBAL.equals(scope.getScopeType())
                && scope.getOrganizationUnitId() == companyId && scope.getScopeReferenceId() == null
                && !grant.getRole().isPlatformRole();
    }

    private static Grant grantProjection(UserRoleGrant grant) {
        return new Grant(grant.getId(), grant.getUser().getId(), grant.getRole().getId(), grant.getRole().getCode(),
                grant.getAuthorizationScope().getOrganizationUnitId(), grant.getAuthorizationScope().getScopeType(),
                grant.getValidFrom(), grant.getValidTo(), grant.getRevokedAt(), grant.getReason(),
                grant.getGrantedBy().getId(), grant.getRevokedBy() == null ? null : grant.getRevokedBy().getId());
    }

    public record Grant(long id, long userId, long roleId, String roleCode, long companyId, String scopeType,
            Instant validFrom, Instant validTo, Instant revokedAt, String reason, long grantedBy, Long revokedBy) {}

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
        if (!links.stream().anyMatch(link -> roleIds.contains(link.getRole().getId()) && link.getRole().isEnabled() && !link.getRole().isPlatformRole()
                && EffectiveAuthorizationService.validCompanyPermission(link.getPermission())
                && requiredPermission.equals(link.getPermission().getCode()))) throw denied();
        if (!companies.lockEligibleCompanyForIdentityWrite(companyId, LocalDate.now(ZoneOffset.UTC))) throw denied();
        Instant finalTime = Instant.now();
        if (!session.isActiveAt(finalTime)) throw unauthenticated();
        Set<Long> activeRoles = candidates.stream()
                .filter(g -> g.getUser().getId().equals(actor.getId()))
                .filter(g -> EffectiveAuthorizationService.matchesCompany(g, companyId, finalTime))
                .map(g -> g.getRole().getId()).collect(Collectors.toSet());
        if (!links.stream().anyMatch(link -> activeRoles.contains(link.getRole().getId()) && link.getRole().isEnabled() && !link.getRole().isPlatformRole()
                && EffectiveAuthorizationService.validCompanyPermission(link.getPermission())
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
