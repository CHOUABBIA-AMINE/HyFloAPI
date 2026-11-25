/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : IdentityAuthenticationTest
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Test
 * @Module      : Foundation / Identity
 * @Package     : dz.sh.hyflo
 *
 * @Description : Verifies bounded cryptographic and login-throttling contracts.
 */

package dz.sh.hyflo;

import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Set;

import javax.crypto.spec.SecretKeySpec;

import dz.sh.hyflo.modules.foundation.identity.api.request.LoginRequest;
import dz.sh.hyflo.modules.foundation.identity.api.request.RefreshRequest;
import jakarta.validation.Validation;
import dz.sh.hyflo.modules.foundation.identity.api.response.TokenResponse;
import dz.sh.hyflo.modules.foundation.identity.application.service.AuthenticationService.LoginResult;
import dz.sh.hyflo.platform.security.IdentityAuthenticationProperties;
import dz.sh.hyflo.platform.security.JwtTokenService;
import dz.sh.hyflo.platform.security.LoginAttemptLimiter;
import dz.sh.hyflo.platform.exception.AuthenticationProblems;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IdentityAuthenticationTest {

    // Deterministic synthetic key, not used outside tests.
    private static final byte[] KEY = new byte[48];
    private static final String ENCODED = Base64.getEncoder().encodeToString(KEY);
    private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");

    @Test
    void rejectsMissingInvalidOrUnboundedAuthenticationConfiguration() {
        for (String key : new String[]{"", "not-base64!", Base64.getEncoder().encodeToString(new byte[31])}) {
            assertThatThrownBy(() -> properties(key, 600, 28800, 60, 120, 20, 10, 100))
                    .isInstanceOf(IllegalArgumentException.class);
        }
        assertThatThrownBy(() -> properties(ENCODED, 901, 28800, 60, 120, 20, 10, 100))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> properties(ENCODED, 600, 599, 60, 120, 20, 10, 100))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> properties(ENCODED, 600, 86401, 60, 120, 20, 10, 100))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> properties(ENCODED, 600, 28800, 0, 120, 20, 10, 100))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void validatesAccessTokenSignatureAndClaimsWithStrictExpiry() {
        var service = new JwtTokenService(properties(ENCODED, 600, 28800, 60, 120, 20, 10, 100));
        var issued = service.issue(12, 34, NOW, NOW.plusSeconds(30));
        assertThat(issued.expiresAt()).isEqualTo(NOW.plusSeconds(30));
        assertThat(service.verify(issued.value(), NOW)).isEqualTo(new JwtTokenService.TokenIdentity(12, 34));
        assertThat(service.verify(issued.value(), NOW.plusSeconds(29))).isEqualTo(new JwtTokenService.TokenIdentity(12, 34));
        assertThatThrownBy(() -> service.verify(issued.value(), NOW.plusSeconds(30))).isInstanceOf(IllegalArgumentException.class);
        var signed = Jwts.parser().verifyWith(new SecretKeySpec(KEY, "HmacSHA256"))
                .clock(() -> Date.from(NOW)).build().parseSignedClaims(issued.value());
        var claims = signed.getPayload();
        assertThat(signed.getHeader().getAlgorithm()).isEqualTo("HS256");
        assertThat(claims.getIssuer()).isEqualTo("hyflo-api");
        assertThat(claims.getAudience()).isEqualTo(Set.of("hyflo-api"));
        assertThat(claims.get("token_type")).isEqualTo("access");
        assertThat(claims.get("sid")).isEqualTo("34");
        assertThat(claims.getId()).isNotBlank();
        assertThat(claims.getIssuedAt().toInstant()).isEqualTo(NOW);
        assertThat(claims).doesNotContainKeys("roles", "permissions", "companyId");
        int signature = issued.value().lastIndexOf('.') + 1;
        String tampered = issued.value().substring(0, signature)
                + (issued.value().charAt(signature) == 'A' ? "B" : "A") + issued.value().substring(signature + 1);
        for (String invalid : new String[]{"", tampered, signed("wrong", "hyflo-api", "access", "12", "34", NOW, NOW.plusSeconds(30)),
                signed("hyflo-api", "wrong", "access", "12", "34", NOW, NOW.plusSeconds(30)),
                signed("hyflo-api", "hyflo-api", "refresh", "12", "34", NOW, NOW.plusSeconds(30)),
                signed("hyflo-api", "hyflo-api", "access", "0", "34", NOW, NOW.plusSeconds(30)),
                signed("hyflo-api", "hyflo-api", "access", "12", "-1", NOW, NOW.plusSeconds(30)),
                signed("hyflo-api", "hyflo-api", "access", "12", "34", NOW.plusSeconds(1), NOW.plusSeconds(30)),
                signed("hyflo-api", "hyflo-api", "access", "12", "34", NOW, NOW.plusSeconds(601))}) {
            assertThatThrownBy(() -> service.verify(invalid, NOW)).isInstanceOf(IllegalArgumentException.class);
        }
        String wrongAlgorithm = Jwts.builder().issuer("hyflo-api").audience().add("hyflo-api").and()
                .subject("12").claim("sid", "34").claim("token_type", "access").id("synthetic")
                .issuedAt(Date.from(NOW)).expiration(Date.from(NOW.plusSeconds(30)))
                .signWith(new SecretKeySpec(KEY, "HmacSHA384"), Jwts.SIG.HS384).compact();
        assertThatThrownBy(() -> service.verify(wrongAlgorithm, NOW)).isInstanceOf(IllegalArgumentException.class);
        String missingClaims = Jwts.builder().subject("12").signWith(new SecretKeySpec(KEY, "HmacSHA256"), Jwts.SIG.HS256).compact();
        assertThatThrownBy(() -> service.verify(missingClaims, NOW)).isInstanceOf(IllegalArgumentException.class);
        String refresh = service.newRefreshSecret();
        assertThat(Base64.getUrlDecoder().decode(refresh)).hasSize(32);
        assertThat(service.newRefreshSecret()).isNotEqualTo(refresh);
        assertThat(JwtTokenService.digestRefreshSecret(refresh)).matches("[0-9a-f]{64}").isNotEqualTo(refresh);
    }

    @Test
    void enforcesIndependentBoundedAttemptBucketsAndExpiresWindows() {
        var limiter = new LoginAttemptLimiter(properties(ENCODED, 600, 28800, 60, 6, 3, 2, 100));
        assertThat(limiter.retryAfter("ip1", "alice", NOW)).isZero();
        assertThat(limiter.retryAfter("ip2", "alice", NOW)).isZero();
        assertThat(limiter.retryAfter("ip3", "alice", NOW)).isEqualTo(60);
        assertThat(limiter.retryAfter("ip1", "bob", NOW)).isZero();
        assertThat(limiter.retryAfter("ip1", "carol", NOW)).isZero();
        assertThat(limiter.retryAfter("ip1", "dan", NOW)).isEqualTo(60);
        assertThat(limiter.retryAfter("ip2", "dan", NOW)).isZero();
        assertThat(limiter.retryAfter("ip3", "eve", NOW)).isZero();
        assertThat(limiter.retryAfter("ip4", "frank", NOW)).isEqualTo(60);
        assertThat(limiter.retryAfter("ip1", "alice", NOW.plusSeconds(59))).isEqualTo(1);
        assertThat(limiter.retryAfter("ip1", "alice", NOW.plusSeconds(60))).isZero();

        var bounded = new LoginAttemptLimiter(properties(ENCODED, 600, 28800, 60, 6, 3, 2, 2));
        assertThat(bounded.retryAfter("peer", "alice", NOW)).isZero();
        assertThat(bounded.retryAfter("peer", "bob", NOW)).isEqualTo(60);
        assertThat(bounded.retryAfter("other", "alice", NOW)).isEqualTo(60);
        assertThat(bounded.retryAfter("other", "bob", NOW.plusSeconds(60))).isZero();
    }

    @Test
    void refreshShapeAndSharedAttemptBudgetAreBounded() {
        var service = new JwtTokenService(properties(ENCODED, 600, 28800, 60, 120, 20, 10, 100));
        String secret = service.newRefreshSecret();
        try (var factory = Validation.buildDefaultValidatorFactory()) {
            var validator = factory.getValidator();
            assertThat(validator.validate(new RefreshRequest(secret))).isEmpty();
            for (String invalid : new String[]{"", "A".repeat(42), "A".repeat(42) + "B",
                    secret + "=", " " + secret}) {
                assertThat(validator.validate(new RefreshRequest(invalid))).isNotEmpty();
            }
            assertThat(validator.validate(new RefreshRequest(null))).isNotEmpty();
        }
        assertThat(new RefreshRequest(secret).toString()).doesNotContain(secret);
        var provider = dz.sh.hyflo.modules.foundation.identity.domain.model.IdentityProvider.local();
        var user = org.mockito.Mockito.mock(dz.sh.hyflo.modules.foundation.identity.domain.model.User.class);
        for (String event : new String[]{"REFRESH_SUCCEEDED", "REFRESH_REPLAY", "LOGOUT"}) {
            assertThat(new dz.sh.hyflo.modules.foundation.identity.domain.model.AuthenticationEvent(
                    event, NOW, "synthetic-correlation", provider, user).getEventCode()).isEqualTo(event);
            assertThatThrownBy(() -> new dz.sh.hyflo.modules.foundation.identity.domain.model.AuthenticationEvent(
                    event, NOW, "synthetic-correlation", provider, null)).isInstanceOf(IllegalArgumentException.class);
        }
        assertThatThrownBy(() -> new dz.sh.hyflo.modules.foundation.identity.domain.model.AuthenticationEvent(
                "UNKNOWN", NOW, "synthetic-correlation", provider, user)).isInstanceOf(IllegalArgumentException.class);
        var limiter = new LoginAttemptLimiter(properties(ENCODED, 600, 28800, 60, 4, 2, 1, 10));
        assertThat(limiter.retryAfter("peer1", "alice", NOW)).isZero();
        assertThat(limiter.retryAfterRefresh("peer1", NOW)).isZero();
        assertThat(limiter.retryAfterRefresh("peer1", NOW)).isEqualTo(60);
        assertThat(limiter.retryAfterRefresh("peer2", NOW)).isZero();
        assertThat(limiter.retryAfter("peer2", "alice", NOW)).isEqualTo(60);
        assertThat(limiter.retryAfterRefresh("peer2", NOW)).isZero();
        assertThat(limiter.retryAfterRefresh("peer3", NOW)).isEqualTo(60);
        assertThat(limiter.retryAfterRefresh("peer3", NOW.plusSeconds(60))).isZero();
    }

    @Test
    void redactsSecretsInDiagnosticRepresentationsAndUsesStableErrors() {
        assertThat(new LoginRequest("user", "synthetic-secret").toString()).doesNotContain("synthetic-secret");
        assertThat(new LoginResult("access-secret", NOW, "refresh-secret", NOW).toString())
                .doesNotContain("access-secret", "refresh-secret");
        assertThat(new TokenResponse("access-secret", "Bearer", NOW, "refresh-secret", NOW).toString())
                .doesNotContain("access-secret", "refresh-secret");
        var problem = AuthenticationProblems.problem(HttpStatus.UNAUTHORIZED, "synthetic-correlation");
        assertThat(problem.getStatus()).isEqualTo(401);
        assertThat(problem.getProperties()).containsEntry("code", "AUTHENTICATION_REQUIRED")
                .containsEntry("correlationId", "synthetic-correlation");
    }

    private static IdentityAuthenticationProperties properties(String key, int access, int session, int window,
            int global, int ip, int username, int capacity) {
        return new IdentityAuthenticationProperties(key, access, session, window, global, ip, username, capacity);
    }

    private static String signed(String issuer, String audience, String type, String user, String session,
            Instant issued, Instant expiry) {
        return Jwts.builder().issuer(issuer).audience().add(audience).and().subject(user).claim("sid", session)
                .claim("token_type", type).id("synthetic").issuedAt(Date.from(issued)).expiration(Date.from(expiry))
                .signWith(new SecretKeySpec(KEY, "HmacSHA256"), Jwts.SIG.HS256).compact();
    }
}
