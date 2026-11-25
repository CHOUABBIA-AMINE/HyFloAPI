/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : TokenResponse
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Record
 * @Layer       : Response
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.api.response
 *
 * @Description : Returns the newly issued access and opaque refresh token.
 */

package dz.sh.hyflo.modules.foundation.identity.api.response;

import java.time.Instant;

public record TokenResponse(String accessToken, String tokenType, Instant accessExpiresAt,
        String refreshToken, Instant sessionExpiresAt) {
    @Override public String toString() { return "TokenResponse[redacted]"; }
}
