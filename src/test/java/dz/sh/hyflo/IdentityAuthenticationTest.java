/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : IdentityAuthenticationTest
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-26-2026
 *
 * @Type        : Class
 * @Layer       : Test
 * @Module      : Foundation / Identity
 * @Package     : dz.sh.hyflo
 *
 * @Description : Verifies bounded cryptographic and login-throttling contracts.
 */

package dz.sh.hyflo;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.Base64;
import java.util.Date;
import java.util.Set;

import javax.crypto.spec.SecretKeySpec;

import dz.sh.hyflo.modules.foundation.identity.api.request.ChangePasswordRequest;
import dz.sh.hyflo.modules.foundation.identity.api.request.LoginRequest;
import dz.sh.hyflo.modules.foundation.identity.api.request.RefreshRequest;
import jakarta.validation.Validation;
import dz.sh.hyflo.modules.foundation.identity.api.response.TokenResponse;
import dz.sh.hyflo.modules.foundation.identity.application.service.AuthenticationService;
import dz.sh.hyflo.modules.foundation.identity.application.service.AuthenticationService.LoginResult;
import dz.sh.hyflo.modules.foundation.identity.application.service.SessionAuthenticationService.CurrentIdentity;
import dz.sh.hyflo.modules.foundation.identity.domain.model.*;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.*;
import dz.sh.hyflo.platform.security.PasswordEncodingConfiguration.IdentityPasswordEncoder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import dz.sh.hyflo.platform.security.IdentityAuthenticationProperties;
import dz.sh.hyflo.platform.security.JwtTokenService;
import dz.sh.hyflo.platform.security.LoginAttemptLimiter;
import dz.sh.hyflo.platform.exception.AuthenticationProblems;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

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
        assertThat(AuthenticationService.validOwnEmailInput("Mixed@example.invalid", "password")).isTrue();
        assertThat(AuthenticationService.validOwnEmailInput("HYFLO-BOOTSTRAP@hyflo.invalid", "password")).isFalse();
        assertThat(AuthenticationService.validOwnEmailInput(" a@example.invalid", "password")).isFalse();
        assertThat(AuthenticationService.validOwnEmailInput("a@example.invalid", "é".repeat(37))).isFalse();
        assertThat(new dz.sh.hyflo.modules.foundation.identity.api.request.ChangeOwnEmailRequest(
                "private@example.invalid", "secret").toString()).doesNotContain("private@example.invalid", "secret");
        var provider = dz.sh.hyflo.modules.foundation.identity.domain.model.IdentityProvider.local();
        var user = org.mockito.Mockito.mock(dz.sh.hyflo.modules.foundation.identity.domain.model.User.class);
        for (String event : new String[]{"REFRESH_SUCCEEDED", "REFRESH_REPLAY", "LOGOUT",
                "PASSWORD_CHANGED", "PASSWORD_CHANGE_FAILED", "PROFILE_EMAIL_CHANGED", "PROFILE_EMAIL_FAILED"}) {
            assertThat(new dz.sh.hyflo.modules.foundation.identity.domain.model.AuthenticationEvent(
                    event, NOW, "synthetic-correlation", provider, user).getEventCode()).isEqualTo(event);
            assertThatThrownBy(() -> new dz.sh.hyflo.modules.foundation.identity.domain.model.AuthenticationEvent(
                    event, NOW, "synthetic-correlation", provider, null)).isInstanceOf(IllegalArgumentException.class);
        }
        assertThatThrownBy(() -> new dz.sh.hyflo.modules.foundation.identity.domain.model.AuthenticationEvent(
                "UNKNOWN", NOW, "synthetic-correlation", provider, user)).isInstanceOf(IllegalArgumentException.class);
        // Explicit persisted identities: Mockito's default boxed numeric return is zero.
        when(user.getId()).thenReturn(101L);
        User foreignUser = mock(User.class);
        when(foreignUser.getId()).thenReturn(202L);
        User samePersistedUser = mock(User.class);
        when(samePersistedUser.getId()).thenReturn(101L);
        var target = new LoginSession(user, NOW.minusSeconds(1), NOW.plusSeconds(30));
        assertThatThrownBy(() -> new AuthenticationEvent(AuthenticationEvent.SESSION_REVOKED,
                NOW, "synthetic-correlation", provider, user)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new AuthenticationEvent(AuthenticationEvent.SESSION_REVOKED,
                NOW, "synthetic-correlation", provider, user, target)).isInstanceOf(IllegalArgumentException.class);
        target.revoke(NOW);
        assertThat(new AuthenticationEvent(AuthenticationEvent.SESSION_REVOKED,
                NOW, "synthetic-correlation", provider, user, target).getLoginSession()).isSameAs(target);
        assertThat(new AuthenticationEvent(AuthenticationEvent.SESSION_REVOKED,
                NOW, "synthetic-correlation", provider, samePersistedUser, target).getLoginSession()).isSameAs(target);
        assertThatThrownBy(() -> new AuthenticationEvent(AuthenticationEvent.SESSION_REVOKED,
                NOW, "synthetic-correlation", provider, foreignUser, target)).isInstanceOf(IllegalArgumentException.class);
        User transientUser = mock(User.class);
        when(transientUser.getId()).thenReturn(null);
        assertThatThrownBy(() -> new AuthenticationEvent(AuthenticationEvent.SESSION_REVOKED,
                NOW, "synthetic-correlation", provider, transientUser, target)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new AuthenticationEvent(AuthenticationEvent.SESSION_REVOKED,
                NOW.plusSeconds(1), "synthetic-correlation", provider, user, target)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new AuthenticationEvent(AuthenticationEvent.LOGOUT,
                NOW, "synthetic-correlation", provider, user, target)).isInstanceOf(IllegalArgumentException.class);
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
    void rejectsRevocationWithinSameSecondAndRetainsSubsecondRefreshIssuance() {
        Instant issuedAt = NOW.plusNanos(100_000_000);
        Instant revokedAt = NOW.plusNanos(500_000_000);
        Instant decisionAt = NOW.plusNanos(750_000_000);
        var settings = properties(ENCODED, 600, 28800, 60, 120, 20, 10, 100);
        var tokens = new JwtTokenService(settings);
        var users = mock(UserRepository.class);
        var sessions = mock(LoginSessionRepository.class);
        var refreshTokens = mock(RefreshTokenRepository.class);
        var events = mock(AuthenticationEventRepository.class);
        var encoder = mock(IdentityPasswordEncoder.class);
        var provider = IdentityProvider.local();
        var user = mock(User.class);
        when(user.getId()).thenReturn(12L);
        when(user.getUsername()).thenReturn("synthetic-user");
        when(user.getIdentityProvider()).thenReturn(provider);
        when(user.getUserStatus()).thenReturn(new UserStatus(UserStatus.ACTIVE_CODE, "Active"));
        when(user.getUserType()).thenReturn(new UserType(UserType.HUMAN_CODE, null, null, "Human"));
        when(users.findForUpdate(12L)).thenReturn(Optional.of(user));
        var service = new AuthenticationService(mock(IdentityProviderRepository.class), users,
                mock(LocalCredentialRepository.class), sessions, refreshTokens, events, encoder,
                tokens, settings, Clock.fixed(decisionAt, ZoneOffset.UTC));

        // A refresh token issued at whole seconds must not rotate after a subsecond revocation.
        String secret = tokens.newRefreshSecret();
        String digest = JwtTokenService.digestRefreshSecret(secret);
        var revoked = spy(new LoginSession(user, NOW.minusSeconds(1), NOW.plusSeconds(60)));
        when(revoked.getId()).thenReturn(34L);
        var previous = new RefreshToken(revoked, digest, NOW);
        revoked.revoke(revokedAt);
        assertThat(revoked.isActiveAt(NOW)).isTrue();
        assertThat(revoked.isActiveAt(decisionAt)).isFalse();
        when(sessions.findUserIdBySessionId(34L)).thenReturn(Optional.of(12L));
        when(sessions.findForUpdate(34L)).thenReturn(Optional.of(revoked));
        when(refreshTokens.findSessionIdByDigest(digest)).thenReturn(Optional.of(34L));
        when(refreshTokens.findForUpdate(digest)).thenReturn(Optional.of(previous));
        assertThat(service.refresh(secret, "revoked")).isEmpty();
        assertThat(previous.getConsumedAt()).isNull();
        verify(refreshTokens, never()).save(any());
        verifyNoInteractions(events);

        var principal = new CurrentIdentity(12L, "synthetic-user", 34L);
        var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(UsernamePasswordAuthenticationToken.authenticated(principal, null, List.of()));
        SecurityContextHolder.setContext(context);
        try {
            assertThat(service.logout(principal, "revoked")).isFalse();
        } finally {
            SecurityContextHolder.clearContext();
        }
        assertThat(revoked.getRevokedAt()).isEqualTo(revokedAt);
        verifyNoInteractions(events);

        // A live token issued within that same second must be accepted without timestamp regression.
        var live = spy(new LoginSession(user, NOW.minusSeconds(1), NOW.plusSeconds(60)));
        when(live.getId()).thenReturn(34L);
        var liveToken = new RefreshToken(live, digest, issuedAt);
        when(sessions.findForUpdate(34L)).thenReturn(Optional.of(live));
        when(refreshTokens.findForUpdate(digest)).thenReturn(Optional.of(liveToken));
        assertThat(service.refresh(secret, "live")).isPresent();
        assertThat(liveToken.getConsumedAt()).isEqualTo(decisionAt);
        verify(refreshTokens).save(argThat(successor -> successor.getIssuedAt().equals(decisionAt)));
        verify(events).save(argThat(event -> event.getEventCode().equals(AuthenticationEvent.REFRESH_SUCCEEDED)
                && event.getOccurredAt().equals(decisionAt)));
    }

    @Test
    void redactsSecretsInDiagnosticRepresentationsAndUsesStableErrors() {
        assertThat(new LoginRequest("user", "synthetic-secret").toString()).doesNotContain("synthetic-secret");
        assertThat(new ChangePasswordRequest("old-secret", "new-secret").toString())
                .doesNotContain("old-secret", "new-secret");
        assertThat(AuthenticationService.validPasswordChangeInput("old-secret", "a".repeat(15))).isTrue();
        assertThat(AuthenticationService.validPasswordChangeInput("old-secret", "a".repeat(14))).isFalse();
        assertThat(AuthenticationService.validPasswordChangeInput("old-secret", "é".repeat(37))).isFalse();
        assertThat(AuthenticationService.validPasswordChangeInput("é".repeat(37), "a".repeat(15))).isFalse();
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
