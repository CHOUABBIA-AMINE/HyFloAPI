/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : JwtTokenService
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Security
 * @Module      : Platform / Security
 * @Package     : dz.sh.hyflo.platform.security
 *
 * @Description : Issues and verifies signed access tokens and opaque refresh secrets.
 */

package dz.sh.hyflo.platform.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.HexFormat;
import java.util.Set;
import java.util.UUID;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class JwtTokenService {

    private static final String ISSUER = "hyflo-api";
    private final IdentityAuthenticationProperties settings;
    private final SecureRandom random = new SecureRandom();

    public JwtTokenService(IdentityAuthenticationProperties settings) {
        this.settings = settings;
    }

    public AccessToken issue(long userId, long sessionId, Instant now, Instant sessionExpiry) {
        Instant issued = now.truncatedTo(ChronoUnit.SECONDS);
        Instant expiry = issued.plus(settings.accessLifetime());
        if (sessionExpiry.isBefore(expiry)) {
            expiry = sessionExpiry.truncatedTo(ChronoUnit.SECONDS);
        }
        if (userId <= 0 || sessionId <= 0 || !expiry.isAfter(issued)) {
            throw new IllegalArgumentException("Invalid access token interval or identity");
        }
        String token = Jwts.builder().issuer(ISSUER).audience().add(ISSUER).and()
                .subject(Long.toString(userId)).claim("sid", Long.toString(sessionId))
                .claim("token_type", "access").id(UUID.randomUUID().toString())
                .issuedAt(Date.from(issued)).expiration(Date.from(expiry))
                .signWith(settings.signingKey(), Jwts.SIG.HS256).compact();
        return new AccessToken(token, expiry);
    }

    public TokenIdentity verify(String token, Instant now) {
        if (token == null || token.isBlank() || token.length() > 4096) {
            throw new IllegalArgumentException("Invalid access token");
        }
        try {
            var signed = Jwts.parser().verifyWith(settings.signingKey())
                    .requireIssuer(ISSUER).requireAudience(ISSUER).require("token_type", "access")
                    .clock(() -> Date.from(now)).build().parseSignedClaims(token);
            var claims = signed.getPayload();
            if (!"HS256".equals(signed.getHeader().getAlgorithm())
                    || !Set.of(ISSUER).equals(claims.getAudience())
                    || claims.getId() == null || claims.getId().isBlank()
                    || claims.getIssuedAt() == null || claims.getExpiration() == null) {
                throw new IllegalArgumentException("Invalid access token");
            }
            Instant issued = claims.getIssuedAt().toInstant();
            Instant expiry = claims.getExpiration().toInstant();
            if (issued.isAfter(now) || !expiry.isAfter(now) || !expiry.isAfter(issued)
                    || expiry.isAfter(issued.plus(settings.accessLifetime()))) {
                throw new IllegalArgumentException("Invalid access token");
            }
            long userId = Long.parseLong(claims.getSubject());
            long sessionId = Long.parseLong(claims.get("sid", String.class));
            if (userId <= 0 || sessionId <= 0) {
                throw new IllegalArgumentException("Invalid access token");
            }
            return new TokenIdentity(userId, sessionId);
        } catch (JwtException | IllegalArgumentException | NullPointerException invalid) {
            throw new IllegalArgumentException("Invalid access token");
        }
    }

    public String newRefreshSecret() {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public static String digestRefreshSecret(String secret) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
                    .digest(secret.getBytes(StandardCharsets.US_ASCII)));
        } catch (NoSuchAlgorithmException impossible) {
            throw new IllegalStateException("SHA-256 unavailable");
        }
    }

    public record AccessToken(String value, Instant expiresAt) {
        @Override public String toString() { return "AccessToken[redacted]"; }
    }

    public record TokenIdentity(long userId, long sessionId) {}
}
