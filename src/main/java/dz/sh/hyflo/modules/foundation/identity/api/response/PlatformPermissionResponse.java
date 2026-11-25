/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : PlatformPermissionResponse
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Record
 * @Layer       : Response
 * @Module      : Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.api.response
 *
 * @Description : PlatformPermissionResponse for runtime role permission management.
 */

package dz.sh.hyflo.modules.foundation.identity.api.response;

public record PlatformPermissionResponse(long id, String code, String displayName, String actionCode,
        String resourceTypeCode, boolean platformPermission, boolean supported) {}
