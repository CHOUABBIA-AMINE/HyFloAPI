/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : AuthorizationMapper
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Mapper
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.api.mapper
 *
 * @Description : Maps explicit authorized application read results without exposing JPA entities.
 */

package dz.sh.hyflo.modules.foundation.identity.api.mapper;

import dz.sh.hyflo.modules.foundation.identity.api.response.CompanyPermissionsResponse;
import dz.sh.hyflo.modules.foundation.identity.api.response.PermissionCatalogResponse;
import dz.sh.hyflo.modules.foundation.identity.api.response.RoleCatalogResponse;
import dz.sh.hyflo.modules.foundation.identity.api.response.UserStatusResponse;
import dz.sh.hyflo.modules.foundation.identity.application.service.EffectiveAuthorizationService.CompanyPermissions;
import dz.sh.hyflo.modules.foundation.identity.application.service.IdentityCatalogService.PermissionEntry;
import dz.sh.hyflo.modules.foundation.identity.application.service.IdentityCatalogService.RoleEntry;
import dz.sh.hyflo.modules.foundation.identity.application.service.IdentityCatalogService.StatusEntry;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationMapper {

    public CompanyPermissionsResponse toPermissions(CompanyPermissions result) {
        return new CompanyPermissionsResponse(result.companyId(), result.permissionCodes());
    }

    public UserStatusResponse toStatus(StatusEntry entry) {
        return new UserStatusResponse(entry.id(), entry.code(), entry.displayName());
    }

    public RoleCatalogResponse toRole(RoleEntry entry) {
        return new RoleCatalogResponse(entry.id(), entry.code(), entry.displayName(), entry.enabled());
    }

    public PermissionCatalogResponse toPermission(PermissionEntry entry) {
        return new PermissionCatalogResponse(entry.id(), entry.code(), entry.displayName(),
                entry.actionCode(), entry.resourceTypeCode(), entry.validDefinition());
    }
}
