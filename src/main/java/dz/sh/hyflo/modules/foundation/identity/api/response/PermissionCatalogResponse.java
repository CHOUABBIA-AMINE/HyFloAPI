/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : PermissionCatalogResponse
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Record
 * @Layer       : Response
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.api.response
 *
 * @Description : Represents a stored permission definition and its structural validity.
 */

package dz.sh.hyflo.modules.foundation.identity.api.response;

public record PermissionCatalogResponse(long id, String code, String displayName, String actionCode,
        String resourceTypeCode, boolean validDefinition) {}
