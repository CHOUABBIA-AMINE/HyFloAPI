/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : OwnProfileResponse
 * @CreatedOn   : 11-26-2026
 * @UpdatedOn   : 11-26-2026
 *
 * @Type        : Record
 * @Layer       : Response
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.api.response
 *
 * @Description : Safe own account profile projection.
 */

package dz.sh.hyflo.modules.foundation.identity.api.response;

public record OwnProfileResponse(long userId, String username, String email, long companyId,
        String userTypeCode, String userStatusCode) {
    @Override public String toString() { return "OwnProfileResponse[redacted]"; }
}
