/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : IdentityCatalogService
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Service
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.application.service
 *
 * @Description : Reads bounded shared Identity definitions only after company authorization.
 */

package dz.sh.hyflo.modules.foundation.identity.application.service;

import java.util.List;

import dz.sh.hyflo.modules.foundation.identity.application.service.SessionAuthenticationService.CurrentIdentity;
import dz.sh.hyflo.modules.foundation.identity.domain.model.Permission;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.PermissionRepository;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.RolePermissionRepository;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.RoleRepository;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.UserStatusRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IdentityCatalogService {

    private static final int CATALOG_PAGE_SIZE = 100;

    private final EffectiveAuthorizationService authorization;
    private final UserStatusRepository statuses;
    private final RoleRepository roles;
    private final PermissionRepository permissions;
    private final RolePermissionRepository rolePermissions;

    public IdentityCatalogService(EffectiveAuthorizationService authorization, UserStatusRepository statuses,
            RoleRepository roles, PermissionRepository permissions, RolePermissionRepository rolePermissions) {
        this.authorization = authorization;
        this.statuses = statuses;
        this.roles = roles;
        this.permissions = permissions;
        this.rolePermissions = rolePermissions;
    }

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public List<StatusEntry> userStatuses(CurrentIdentity principal, long companyId) {
        authorization.requireCompanyPermission(principal, companyId, "identity-catalog.read");
        return statuses.findAllByOrderByCodeAsc().stream()
                .map(status -> new StatusEntry(status.getId(), status.getCode(), status.getDisplayName()))
                .toList();
    }

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public List<RoleEntry> roles(CurrentIdentity principal, long companyId, long afterId) {
        authorization.requireCompanyPermission(principal, companyId, "role.read");
        return roles.findByIdGreaterThanOrderByIdAsc(afterId, PageRequest.of(0, CATALOG_PAGE_SIZE)).stream()
                .map(role -> new RoleEntry(role.getId(), role.getCode(), role.getDisplayName(), role.isEnabled()))
                .toList();
    }

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public List<PermissionEntry> permissions(CurrentIdentity principal, long companyId, long afterId) {
        authorization.requireCompanyPermission(principal, companyId, "permission.read");
        return permissions.findCatalogPage(afterId, PageRequest.of(0, CATALOG_PAGE_SIZE)).stream()
                .map(IdentityCatalogService::permissionEntry).toList();
    }

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public List<PermissionEntry> rolePermissions(CurrentIdentity principal, long companyId, long roleId, long afterId) {
        authorization.requireCompanyPermission(principal, companyId, "role.read");
        authorization.requireCompanyPermission(principal, companyId, "permission.read");
        return rolePermissions.findCatalogPageForRole(roleId, afterId, PageRequest.of(0, CATALOG_PAGE_SIZE)).stream()
                .map(IdentityCatalogService::permissionEntry).toList();
    }

    private static PermissionEntry permissionEntry(Permission permission) {
        return new PermissionEntry(permission.getId(), permission.getCode(), permission.getDisplayName(),
                permission.getAction().getCode(), permission.getResourceType().getCode(),
                EffectiveAuthorizationService.validPermission(permission));
    }

    public record StatusEntry(long id, String code, String displayName) {}

    public record RoleEntry(long id, String code, String displayName, boolean enabled) {}

    public record PermissionEntry(long id, String code, String displayName, String actionCode,
            String resourceTypeCode, boolean validDefinition) {}
}
