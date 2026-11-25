/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : AuthenticationMapper
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Mapper
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.api.mapper
 *
 * @Description : Maps safe application results to explicit Identity response DTOs.
 */

package dz.sh.hyflo.modules.foundation.identity.api.mapper;

import dz.sh.hyflo.modules.foundation.identity.api.response.CurrentIdentityResponse;
import dz.sh.hyflo.modules.foundation.identity.api.response.TokenResponse;
import dz.sh.hyflo.modules.foundation.identity.application.service.AuthenticationService.LoginResult;
import dz.sh.hyflo.modules.foundation.identity.application.service.SessionAuthenticationService.CurrentIdentity;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationMapper {

    public TokenResponse toTokenResponse(LoginResult result) {
        return new TokenResponse(result.accessToken(), "Bearer", result.accessExpiresAt(),
                result.refreshToken(), result.sessionExpiresAt());
    }

    public CurrentIdentityResponse toCurrentIdentityResponse(CurrentIdentity identity) {
        return new CurrentIdentityResponse(identity.userId(), identity.username(), identity.sessionId());
    }
}
