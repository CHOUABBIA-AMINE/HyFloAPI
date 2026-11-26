/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : PlatformRoleGrantService
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Service
 * @Module      : Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.application.service
 *
 * @Description : PlatformRoleGrantService for scoped platform administration.
 */

package dz.sh.hyflo.modules.foundation.identity.application.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import dz.sh.hyflo.modules.foundation.identity.domain.model.LocalCredential;
import dz.sh.hyflo.modules.foundation.identity.domain.model.UserType;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.*;
import dz.sh.hyflo.modules.foundation.identity.application.service.SessionAuthenticationService.CurrentIdentity;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Isolation;
import dz.sh.hyflo.modules.foundation.identity.domain.model.PlatformRoleGrant;
import dz.sh.hyflo.modules.foundation.identity.domain.model.Role;
import dz.sh.hyflo.modules.foundation.identity.domain.model.User;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.PlatformRoleGrantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlatformRoleGrantService {
    private final PlatformRoleGrantRepository grants;

    private final PlatformAuthorizationService authorization;
    private final UserRepository users;
    private final RoleRepository roles;
    private final RolePermissionRepository links;
    private final LocalCredentialRepository credentials;

    public PlatformRoleGrantService(PlatformRoleGrantRepository grants, PlatformAuthorizationService authorization,
            UserRepository users, RoleRepository roles, RolePermissionRepository links, LocalCredentialRepository credentials) {
        this.grants = grants;
        this.authorization = authorization;
        this.users = users;
        this.roles = roles;
        this.links = links;
        this.credentials = credentials;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void provision(User user, Role role, User actor, Instant now, String reason, String correlationId) {
        if (!"hyflo-bootstrap".equals(actor.getUsername()) || grants.existsByUserId(user.getId()))
            throw new IllegalStateException("Initial platform grant unavailable");
        grants.save(new PlatformRoleGrant(user, role, now, null, reason, correlationId, actor));
    }
    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public List<Grant> list(CurrentIdentity principal, long userId, long afterId) {
        authorization.requireRead(principal, PlatformAuthorizationService.GRANT_READ);
        if (userId <= 0 || afterId < 0) throw new IllegalArgumentException("Invalid selector");
        if (!grants.existsEligibleUser(userId)) throw denied();
        return grants.findHistory(userId, afterId, PageRequest.of(0, 100)).stream()
                .map(PlatformRoleGrantService::projection).toList();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Grant assign(CurrentIdentity principal, long userId, long roleId, String reason, String correlation) {
        User actor = authorization.requireCatalogWrite(principal, PlatformAuthorizationService.GRANT_ASSIGN);
        validate(userId, roleId, reason);
        if (actor.getId() == userId) throw denied();
        User target = lockTarget(userId);
        if (!eligible(target)) throw new GrantRejected();
        Role role = roles.findForUpdate(roleId).orElseThrow(PlatformRoleGrantService::denied);
        if (!role.isEnabled() || !role.isPlatformRole()) throw new GrantRejected();
        var bundle = links.findForRolesForWrite(Set.of(roleId));
        if (bundle.isEmpty() || bundle.stream().anyMatch(link -> !link.getPermission().isPlatformPermission()
                || !EffectiveAuthorizationService.validPermission(link.getPermission())
                || !CapabilityManifest.supports(link.getPermission()))) throw new GrantRejected();
        actor = authorization.requireCatalogWrite(principal, PlatformAuthorizationService.GRANT_ASSIGN);
        Instant now = now();
        var durable = authorization.durablePermissions(actor.getId(), now, null);
        if (!durable.contains(PlatformAuthorizationService.GRANT_ASSIGN)
                || bundle.stream().anyMatch(link -> !durable.contains(link.getPermission().getCode()))) throw denied();
        if (grants.countByUserId(userId) >= 1000 || grants.countNonEndedForUserRole(userId, roleId, now) != 0)
            throw new GrantRejected();
        requireContinuity(now, null);
        actor = authorization.requireCatalogWrite(principal, PlatformAuthorizationService.GRANT_ASSIGN);
        return projection(grants.save(new PlatformRoleGrant(target, role, now, null, reason, correlation, actor)));
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void revoke(CurrentIdentity principal, long userId, long grantId, String reason, String correlation) {
        authorization.requireCatalogWrite(principal, PlatformAuthorizationService.GRANT_REVOKE);
        validate(userId, grantId, reason);
        lockTarget(userId);
        PlatformRoleGrant grant = grants.findSelectedForUpdate(grantId, userId).orElseThrow(PlatformRoleGrantService::denied);
        User actor = authorization.requireCatalogWrite(principal, PlatformAuthorizationService.GRANT_REVOKE);
        Instant now = now();
        if (grant.getRevokedAt() != null || (grant.getValidTo() != null && !grant.getValidTo().isAfter(now)))
            throw new GrantRejected();
        requireContinuity(now, grantId);
        actor = authorization.requireCatalogWrite(principal, PlatformAuthorizationService.GRANT_REVOKE);
        grant.revoke(now, actor, reason, correlation);
    }

    private void requireContinuity(Instant now, Long excludedGrantId) {
        if (!hasDurableAdministrator(now, excludedGrantId, null, null)) throw new GrantRejected();
    }

    // Called only under the existing exclusive LOCAL provider gate.
    boolean hasDurableAdministrator(Instant now, Long excludedGrantId, Long roleId, Long permissionId) {
        return grants.findDurableCandidateUsers(now).stream().anyMatch(id ->
                hasSupportedCredential(id)
                && authorization.durablePermissions(id, now, excludedGrantId, roleId, permissionId)
                        .containsAll(PlatformAuthorizationService.implementedPermissions()));
    }

    boolean hasSupportedCredential(long userId) {
        return credentials.findByUser_Id(userId)
                .filter(c -> LocalCredential.supportsPasswordHash(c.getPasswordHash())).isPresent();
    }

    private User lockTarget(long id) {
        return users.findForUpdate(id).filter(u -> !"hyflo-bootstrap".equals(u.getUsername()))
                .orElseThrow(PlatformRoleGrantService::denied);
    }

    private boolean eligible(User user) {
        return SessionAuthenticationService.eligible(user) && user.getUserType().hasCode(UserType.HUMAN_CODE)
                && credentials.findByUser_Id(user.getId()).filter(c ->
                        LocalCredential.supportsPasswordHash(c.getPasswordHash())).isPresent();
    }

    private static Instant now() { return Instant.now().truncatedTo(ChronoUnit.MICROS); }
    private static void validate(long userId, long selectedId, String reason) {
        if (userId <= 0 || selectedId <= 0 || reason == null || reason.isBlank() || reason.length() > 255)
            throw new IllegalArgumentException("Invalid grant input");
    }
    private static AccessDeniedException denied() { return new AccessDeniedException("Access denied"); }

    public static final class GrantRejected extends RuntimeException {
        public GrantRejected() { super("Account operation rejected"); }
    }

    private static Grant projection(PlatformRoleGrant grant) {
        return new Grant(grant.getId(), grant.getUser().getId(), grant.getRole().getId(), grant.getRole().getCode(),
                grant.getValidFrom(), grant.getValidTo(), grant.getRevokedAt(), grant.getReason(), grant.getCorrelationId(),
                grant.getGrantedBy().getId(), grant.getRevocationReason(), grant.getRevocationCorrelationId(),
                grant.getRevokedBy() == null ? null : grant.getRevokedBy().getId());
    }
    public record Grant(long id, long userId, long roleId, String roleCode, Instant validFrom, Instant validTo,
            Instant revokedAt, String reason, String correlationId, long grantedBy, String revocationReason,
            String revocationCorrelationId, Long revokedBy) {}
}
