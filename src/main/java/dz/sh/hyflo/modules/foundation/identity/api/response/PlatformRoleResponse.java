/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : PlatformRoleResponse
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Record
 * @Layer       : Response
 * @Module      : Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.api.response
 *
 * @Description : PlatformRoleResponse for scoped platform administration.
 */

package dz.sh.hyflo.modules.foundation.identity.api.response;

public record PlatformRoleResponse(long id, String code, String displayName, boolean enabled, boolean platformRole) {}
