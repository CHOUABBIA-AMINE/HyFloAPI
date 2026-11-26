/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : PlatformRoleAdministrationService
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-26-2026
 *
 * @Type        : Class
 * @Layer       : Service
 * @Module      : Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.application.service
 *
 * @Description : PlatformRoleAdministrationService for scoped platform administration.
 */

package dz.sh.hyflo.modules.foundation.identity.application.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import dz.sh.hyflo.modules.foundation.identity.application.service.SessionAuthenticationService.CurrentIdentity;
import dz.sh.hyflo.modules.foundation.identity.domain.model.Role;
import dz.sh.hyflo.modules.foundation.identity.domain.model.RoleDefinitionEvent;
import dz.sh.hyflo.modules.foundation.identity.domain.model.Permission;
import dz.sh.hyflo.modules.foundation.identity.domain.model.RolePermission;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.*;
import org.springframework.security.access.AccessDeniedException;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.RoleRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlatformRoleAdministrationService {
    private final PlatformAuthorizationService authorization;
    private final RoleRepository roles;
    private final RoleDefinitionEventService events;
    private final PermissionRepository permissions;
    private final RolePermissionRepository links;
    private final UserRoleGrantRepository userGrants;
    private final GroupRoleGrantRepository groupGrants;
    private final PlatformRoleGrantRepository platformGrants;
    private final PlatformRoleGrantService platformGrantService;
    private static final Set<String> FOREIGN_DELEGABLE = Set.of("identity-catalog.read", "role.read", "permission.read");

    public PlatformRoleAdministrationService(PlatformAuthorizationService authorization, RoleRepository roles,
            RoleDefinitionEventService events, PermissionRepository permissions, RolePermissionRepository links,
            UserRoleGrantRepository userGrants, GroupRoleGrantRepository groupGrants, PlatformRoleGrantRepository platformGrants,
            PlatformRoleGrantService platformGrantService) {
        this.authorization = authorization;
        this.roles = roles;
        this.events = events;
        this.permissions = permissions;
        this.links = links;
        this.userGrants = userGrants;
        this.groupGrants = groupGrants;
        this.platformGrants = platformGrants;
        this.platformGrantService = platformGrantService;
    }

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public List<RoleEntry> list(CurrentIdentity principal, long afterId) {
        if (afterId < 0) throw new IllegalArgumentException("Invalid cursor");
        authorization.requireRead(principal, PlatformAuthorizationService.ROLE_READ);
        return roles.findByIdGreaterThanOrderByIdAsc(afterId, PageRequest.of(0, 100)).stream()
                .map(PlatformRoleAdministrationService::entry).toList();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public RoleEntry create(CurrentIdentity principal, String code, String name, Boolean platformRole, String reason) {
        var actor = authorization.requireWrite(principal, PlatformAuthorizationService.ROLE_CREATE);
        if (platformRole == null || reason == null || reason.isBlank() || reason.length() > 255)
            throw new IllegalArgumentException("Invalid role input");
        var candidate = new Role(code, name, platformRole);
        var role = roles.save(candidate);
        // Recheck temporal authority after the potentially blocking insert; all authority locks are retained.
        actor = authorization.requireWrite(principal, PlatformAuthorizationService.ROLE_CREATE);
        events.record(role, actor, Instant.now(), UUID.randomUUID().toString(), reason);
        return entry(role);
    }

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public List<PermissionEntry> permissions(CurrentIdentity principal, long afterId) {
        authorization.requireRead(principal, PlatformAuthorizationService.PERMISSION_READ);
        if (afterId < 0) throw new IllegalArgumentException("Invalid cursor");
        return permissions.findCatalogPage(afterId, PageRequest.of(0, 100)).stream()
                .map(PlatformRoleAdministrationService::permissionEntry).toList();
    }

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public List<PermissionEntry> rolePermissions(CurrentIdentity principal, long roleId, long afterId) {
        authorization.requireRead(principal, PlatformAuthorizationService.ROLE_READ);
        authorization.requireRead(principal, PlatformAuthorizationService.PERMISSION_READ);
        if (roleId <= 0 || afterId < 0) throw new IllegalArgumentException("Invalid selector");
        roles.findById(roleId).orElseThrow(PlatformRoleAdministrationService::denied);
        return links.findCatalogPageForRole(roleId, afterId, PageRequest.of(0, 100)).stream()
                .map(PlatformRoleAdministrationService::permissionEntry).toList();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void changePermission(CurrentIdentity principal, long roleId, long permissionId, String reason, boolean associate) {
        String operation = associate ? PlatformAuthorizationService.PERMISSION_ASSOCIATE : PlatformAuthorizationService.PERMISSION_REMOVE;
        var actor = authorization.requireCatalogWrite(principal, operation);
        if (roleId <= 0 || permissionId <= 0 || reason == null || reason.isBlank() || reason.length() > 255)
            throw new IllegalArgumentException("Invalid role permission change");
        Role role = roles.findForUpdate(roleId).orElseThrow(PlatformRoleAdministrationService::denied);
        Permission permission = permissions.findForRoleChange(permissionId).orElseThrow(PlatformRoleAdministrationService::denied);
        var existing = links.findByRoleIdAndPermissionId(roleId, permissionId);
        if (!role.isEnabled() || existing.isPresent() == associate) throw new RoleChangeRejected();
        if (associate && (role.isPlatformRole() != permission.isPlatformPermission() || !CapabilityManifest.supports(permission)))
            throw new RoleChangeRejected();
        Instant now = Instant.now();
        if (!role.isPlatformRole() && associate) {
            if (userGrants.countNonEndedForUser(roleId, now, actor.getId()) > 0
                    || groupGrants.countNonEndedForRole(roleId, now) > 0) throw new RoleChangeRejected();
            if (userGrants.countNonEndedForeign(roleId, now) > 0
                    && (!foreignDelegable(permission) || links.findForRoles(Set.of(roleId)).stream()
                    .anyMatch(link -> !foreignDelegable(link.getPermission())))) throw new RoleChangeRejected();
        }
        // No other admitted grant/catalog writer can enter while the exclusive provider lock is held.
        actor = authorization.requireCatalogWrite(principal, operation);
        boolean assigned = role.isPlatformRole() && validateAssignedChange(actor.getId(), roleId, permission, operation, associate);
        // Recheck the live session after any blocking authority reads; the provider gate retains the decision.
        actor = authorization.requireCatalogWrite(principal, operation);
        if (role.isPlatformRole())
            assigned = validateAssignedChange(actor.getId(), roleId, permission, operation, associate);
        actor = authorization.requireCatalogWrite(principal, operation);
        if (associate) links.save(new RolePermission(role, permission));
        else links.delete(existing.orElseThrow());
        if (assigned) authorization.requireCatalogWrite(principal, operation);
        events.recordPermissionChange(role, permission, actor, Instant.now(), UUID.randomUUID().toString(), reason, associate);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public RoleEntry rename(CurrentIdentity principal, long roleId, String displayName, String reason) {
        String operation = PlatformAuthorizationService.ROLE_RENAME;
        authorization.requireCatalogWrite(principal, operation);
        validateLifecycleInput(roleId, reason);
        if (displayName == null || displayName.isBlank() || displayName.length() > 120)
            throw new IllegalArgumentException("Invalid role label");
        Role role = roles.findForUpdate(roleId).orElseThrow(PlatformRoleAdministrationService::denied);
        var actor = authorization.requireCatalogWrite(principal, operation);
        if (!role.isEnabled() || role.getDisplayName().equals(displayName)) throw new RoleChangeRejected();
        Instant now = Instant.now();
        String correlation = UUID.randomUUID().toString();
        var before = RoleDefinitionEvent.renamedFrom(role, actor, now, correlation, reason);
        role.rename(displayName);
        var after = RoleDefinitionEvent.renamed(role, actor, now, correlation, reason);
        authorization.requireCatalogWrite(principal, operation);
        events.recordLifecycle(before);
        events.recordLifecycle(after);
        return entry(role);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public RoleEntry retire(CurrentIdentity principal, long roleId, String reason) {
        String operation = PlatformAuthorizationService.ROLE_RETIRE;
        authorization.requireCatalogWrite(principal, operation);
        validateLifecycleInput(roleId, reason);
        Role role = roles.findForUpdate(roleId).orElseThrow(PlatformRoleAdministrationService::denied);
        authorization.requireCatalogWrite(principal, operation);
        if (!role.isEnabled()) throw new RoleChangeRejected();
        Instant now = Instant.now();
        if (!role.isPlatformRole() && userGrants.countNonEndedBootstrapGrants(roleId, now) > 0)
            throw new RoleChangeRejected();
        var actor = authorization.requireCatalogWrite(principal, operation);
        if (role.isPlatformRole()) {
            validateAssignedRetirement(actor.getId(), roleId, operation);
            actor = authorization.requireCatalogWrite(principal, operation);
            validateAssignedRetirement(actor.getId(), roleId, operation);
            actor = authorization.requireCatalogWrite(principal, operation);
        }
        role.retire();
        authorization.requireCatalogWrite(principal, operation);
        events.recordLifecycle(RoleDefinitionEvent.retired(role, actor, Instant.now(), UUID.randomUUID().toString(), reason));
        return entry(role);
    }

    private boolean validateAssignedChange(long actorId, long roleId, Permission permission, String operation,
            boolean associate) {
        Instant now = Instant.now().truncatedTo(ChronoUnit.MICROS);
        if (platformGrants.countNonEndedForRole(roleId, now) == 0) return false;
        requireDurableOperation(actorId, now, operation);
        if (associate) {
            if (!authorization.durablePermissions(actorId, now, null).contains(permission.getCode())) throw denied();
            if (platformGrants.countNonEndedTemporalForRole(roleId, now) > 0
                    || links.findForRoles(Set.of(roleId)).stream().anyMatch(link ->
                        !link.getPermission().isPlatformPermission()
                        || !EffectiveAuthorizationService.validPermission(link.getPermission())
                        || !CapabilityManifest.supports(link.getPermission())))
                throw new RoleChangeRejected();
            if (!platformGrantService.hasDurableAdministrator(now, null, null, null)) throw new RoleChangeRejected();
        } else {
            requireProspectiveAuthority(actorId, now, operation, roleId, permission.getId());
        }
        return true;
    }

    private void validateAssignedRetirement(long actorId, long roleId, String operation) {
        Instant now = Instant.now().truncatedTo(ChronoUnit.MICROS);
        if (platformGrants.countNonEndedForRole(roleId, now) == 0) return;
        requireDurableOperation(actorId, now, operation);
        requireProspectiveAuthority(actorId, now, operation, roleId, null);
    }

    private void requireDurableOperation(long actorId, Instant now, String operation) {
        if (!platformGrantService.hasSupportedCredential(actorId)
                || !authorization.durablePermissions(actorId, now, null).contains(operation)) throw denied();
    }

    private void requireProspectiveAuthority(long actorId, Instant now, String operation, long roleId, Long permissionId) {
        if (!authorization.durablePermissions(actorId, now, null, roleId, permissionId).contains(operation)
                || !platformGrantService.hasDurableAdministrator(now, null, roleId, permissionId))
            throw new RoleChangeRejected();
    }

    private static void validateLifecycleInput(long roleId, String reason) {
        if (roleId <= 0 || reason == null || reason.isBlank() || reason.length() > 255)
            throw new IllegalArgumentException("Invalid role lifecycle input");
    }

    private static boolean foreignDelegable(Permission permission) {
        return FOREIGN_DELEGABLE.contains(permission.getCode()) && CapabilityManifest.supports(permission);
    }

    private static PermissionEntry permissionEntry(Permission permission) {
        return new PermissionEntry(permission.getId(), permission.getCode(), permission.getDisplayName(),
                permission.getAction().getCode(), permission.getResourceType().getCode(),
                permission.isPlatformPermission(), CapabilityManifest.supports(permission));
    }

    private static AccessDeniedException denied() { return new AccessDeniedException("Access denied"); }

    public static final class RoleChangeRejected extends RuntimeException {
        public RoleChangeRejected() { super("Role operation rejected"); }
    }

    public record PermissionEntry(long id, String code, String displayName, String actionCode,
            String resourceTypeCode, boolean platformPermission, boolean supported) {}

    private static RoleEntry entry(Role role) {
        return new RoleEntry(role.getId(), role.getCode(), role.getDisplayName(), role.isEnabled(), role.isPlatformRole());
    }

    public record RoleEntry(long id, String code, String displayName, boolean enabled, boolean platformRole) {}
}
