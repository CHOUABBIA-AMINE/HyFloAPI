/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : AuthenticationService
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Service
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.application.service
 *
 * @Description : Coordinates local login, security evidence and token issuance in one transaction.
 */

package dz.sh.hyflo.modules.foundation.identity.application.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import dz.sh.hyflo.modules.foundation.identity.domain.model.AuthenticationEvent;
import dz.sh.hyflo.modules.foundation.identity.domain.model.IdentityProvider;
import dz.sh.hyflo.modules.foundation.identity.domain.model.LocalCredential;
import dz.sh.hyflo.modules.foundation.identity.domain.model.LoginSession;
import dz.sh.hyflo.modules.foundation.identity.domain.model.RefreshToken;
import dz.sh.hyflo.modules.foundation.identity.domain.model.User;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.AuthenticationEventRepository;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.IdentityProviderRepository;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.LocalCredentialRepository;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.LoginSessionRepository;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.RefreshTokenRepository;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.UserRepository;
import dz.sh.hyflo.platform.security.IdentityAuthenticationProperties;
import dz.sh.hyflo.platform.security.JwtTokenService;
import dz.sh.hyflo.platform.security.PasswordEncodingConfiguration.IdentityPasswordEncoder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class AuthenticationService {

    private static final Pattern BCRYPT = Pattern.compile("\\{bcrypt}\\$2[aby]\\$(0[4-9]|1[0-4])\\$[./A-Za-z0-9]{53}");
    private final IdentityProviderRepository providers;
    private final UserRepository users;
    private final LocalCredentialRepository credentials;
    private final LoginSessionRepository sessions;
    private final RefreshTokenRepository refreshTokens;
    private final AuthenticationEventRepository events;
    private final IdentityPasswordEncoder encoder;
    private final JwtTokenService tokens;
    private final IdentityAuthenticationProperties settings;
    private final String dummyHash;

    public AuthenticationService(IdentityProviderRepository providers, UserRepository users,
            LocalCredentialRepository credentials, LoginSessionRepository sessions,
            RefreshTokenRepository refreshTokens, AuthenticationEventRepository events,
            IdentityPasswordEncoder encoder, JwtTokenService tokens, IdentityAuthenticationProperties settings) {
        this.providers = providers;
        this.users = users;
        this.credentials = credentials;
        this.sessions = sessions;
        this.refreshTokens = refreshTokens;
        this.events = events;
        this.encoder = encoder;
        this.tokens = tokens;
        this.settings = settings;
        this.dummyHash = encoder.encode(UUID.randomUUID().toString());
    }

    @Transactional
    public Optional<LoginResult> login(String username, String password, String correlationId) {
        Instant now = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        IdentityProvider provider = providers.findByCode(IdentityProvider.LOCAL_CODE)
                .filter(p -> IdentityProvider.LOCAL_CODE.equals(p.getCode()))
                .orElseThrow(() -> new IllegalStateException("Identity is not initialized"));
        User user = users.findForAuthentication(provider.getId(), User.normalizeUsername(username)).orElse(null);
        LocalCredential credential = user == null ? null : credentials.findByUser_Id(user.getId()).orElse(null);
        boolean eligible = user != null && SessionAuthenticationService.eligible(user) && credential != null
                && BCRYPT.matcher(credential.getPasswordHash()).matches();
        boolean matches;
        try {
            matches = encoder.matches(password, eligible ? credential.getPasswordHash() : dummyHash);
        } catch (IllegalArgumentException invalidHash) {
            encoder.matches(password, dummyHash);
            matches = false;
        }
        if (!eligible || !matches) {
            events.save(new AuthenticationEvent(AuthenticationEvent.LOGIN_FAILED, now, correlationId, provider, user));
            return Optional.empty();
        }

        Instant sessionExpiry = now.plus(settings.sessionLifetime());
        LoginSession session = sessions.save(new LoginSession(user, now, sessionExpiry));
        String refreshSecret = tokens.newRefreshSecret();
        refreshTokens.save(new RefreshToken(session, JwtTokenService.digestRefreshSecret(refreshSecret), now));
        events.save(new AuthenticationEvent(AuthenticationEvent.LOGIN_SUCCEEDED, now, correlationId, provider, user));
        var access = tokens.issue(user.getId(), session.getId(), now, sessionExpiry);
        return Optional.of(new LoginResult(access.value(), access.expiresAt(), refreshSecret, sessionExpiry));
    }

    public record LoginResult(String accessToken, Instant accessExpiresAt, String refreshToken, Instant sessionExpiresAt) {
        @Override public String toString() { return "LoginResult[redacted]"; }
    }
}
