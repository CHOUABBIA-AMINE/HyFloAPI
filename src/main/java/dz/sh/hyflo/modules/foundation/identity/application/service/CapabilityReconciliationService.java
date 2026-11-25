/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : CapabilityReconciliationService
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Service
 * @Module      : Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.application.service
 *
 * @Description : CapabilityReconciliationService for controlled capability reconciliation.
 */

package dz.sh.hyflo.modules.foundation.identity.application.service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;
import java.util.UUID;

import dz.sh.hyflo.modules.foundation.identity.application.service.SessionAuthenticationService.CurrentIdentity;
import dz.sh.hyflo.modules.foundation.identity.domain.model.Action;
import dz.sh.hyflo.modules.foundation.identity.domain.model.ResourceType;
import dz.sh.hyflo.modules.foundation.identity.domain.model.Permission;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.ActionRepository;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.ResourceTypeRepository;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.PermissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CapabilityReconciliationService {
    private final PlatformAuthorizationService authorization;
    private final ActionRepository actions;
    private final ResourceTypeRepository resources;
    private final PermissionRepository permissions;
    private final CapabilityReconciliationEventService events;

    public CapabilityReconciliationService(PlatformAuthorizationService authorization, ActionRepository actions,
            ResourceTypeRepository resources, PermissionRepository permissions, CapabilityReconciliationEventService events) {
        this.authorization = authorization;
        this.actions = actions;
        this.resources = resources;
        this.permissions = permissions;
        this.events = events;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Result reconcile(CurrentIdentity principal, String reason) {
        var actor = authorization.requireReconciliationWrite(principal);
        if (reason == null || reason.isBlank() || reason.length() > 255)
            throw new IllegalArgumentException("Invalid reconciliation reason");
        var insertedActions = new TreeSet<String>();
        var insertedResources = new TreeSet<String>();
        var insertedPermissions = new TreeSet<String>();

        // The exclusive LOCAL provider lock serializes catalog writers before any lookup.
        for (var entry : CapabilityManifest.entries()) {
            var existingActions = actions.findByCodeIgnoreCase(entry.actionCode());
            Action action;
            if (existingActions.isEmpty()) {
                action = actions.save(new Action(entry.actionCode(), entry.actionLabel()));
                insertedActions.add(entry.actionCode());
            } else {
                if (existingActions.size() != 1) throw new Drift();
                action = existingActions.getFirst();
                if (!entry.actionCode().equals(action.getCode()) || !entry.actionLabel().equals(action.getDisplayName()))
                    throw new Drift();
            }

            var existingResources = resources.findByCodeIgnoreCase(entry.resourceCode());
            ResourceType resource;
            if (existingResources.isEmpty()) {
                resource = resources.save(new ResourceType(entry.resourceCode(), entry.resourceLabel()));
                insertedResources.add(entry.resourceCode());
            } else {
                if (existingResources.size() != 1) throw new Drift();
                resource = existingResources.getFirst();
                if (!entry.resourceCode().equals(resource.getCode()) || !entry.resourceLabel().equals(resource.getDisplayName()))
                    throw new Drift();
            }

            var byCode = permissions.findByCodeIgnoreCase(entry.permissionCode());
            var byPair = permissions.findByActionIdAndResourceTypeId(action.getId(), resource.getId());
            if (byCode.isEmpty() && byPair.isEmpty()) {
                permissions.save(new Permission(entry.permissionCode(), entry.permissionLabel(), action, resource));
                insertedPermissions.add(entry.permissionCode());
            } else {
                if (byCode.size() != 1 || byPair.size() != 1) throw new Drift();
                var permission = byCode.getFirst();
                if (!Objects.equals(permission.getId(), byPair.getFirst().getId())
                        || !entry.permissionCode().equals(permission.getCode())
                        || !entry.permissionLabel().equals(permission.getDisplayName())
                        || !Objects.equals(action.getId(), permission.getAction().getId())
                        || !Objects.equals(resource.getId(), permission.getResourceType().getId())
                        || !EffectiveAuthorizationService.validPermission(permission))
                    throw new Drift();
            }
        }

        actor = authorization.requireReconciliationWrite(principal);
        var result = new Result(CapabilityManifest.ID, CapabilityManifest.digest(), List.copyOf(insertedActions),
                List.copyOf(insertedResources), List.copyOf(insertedPermissions));
        events.record(actor, Instant.now(), UUID.randomUUID().toString(), reason, result.manifestId(),
                result.manifestDigest(), result.insertedActionCodes(), result.insertedResourceTypeCodes(),
                result.insertedPermissionCodes());
        return result;
    }

    public record Result(String manifestId, String manifestDigest, List<String> insertedActionCodes,
            List<String> insertedResourceTypeCodes, List<String> insertedPermissionCodes) {}

    public static final class Drift extends RuntimeException {
        public Drift() { super("Capability definition conflict"); }
    }
}
