/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : UserStatusResponse
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Record
 * @Layer       : Response
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.api.response
 *
 * @Description : Returns an authorized account status catalog entry.
 */

package dz.sh.hyflo.modules.foundation.identity.api.response;

public record UserStatusResponse(long id, String code, String displayName) {}
