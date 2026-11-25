/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : IdentityAuthenticationProperties
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Configuration
 * @Module      : Platform / Security
 * @Package     : dz.sh.hyflo.platform.security
 *
 * @Description : Validates external signing and bounded development authentication settings.
 */

package dz.sh.hyflo.platform.security;

import java.time.Duration;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class IdentityAuthenticationProperties {

    private final SecretKey signingKey;
    private final Duration accessLifetime;
    private final Duration sessionLifetime;
    private final int windowSeconds;
    private final int globalLimit;
    private final int ipLimit;
    private final int usernameLimit;
    private final int bucketCapacity;

    public IdentityAuthenticationProperties(
            @Value("${hyflo.identity.jwt-key:}") String key,
            @Value("${hyflo.identity.access-seconds:600}") int accessSeconds,
            @Value("${hyflo.identity.session-seconds:28800}") int sessionSeconds,
            @Value("${hyflo.identity.login-window-seconds:60}") int windowSeconds,
            @Value("${hyflo.identity.login-global-limit:120}") int globalLimit,
            @Value("${hyflo.identity.login-ip-limit:20}") int ipLimit,
            @Value("${hyflo.identity.login-username-limit:10}") int usernameLimit,
            @Value("${hyflo.identity.login-bucket-capacity:10000}") int bucketCapacity) {
        byte[] raw;
        try {
            raw = Base64.getDecoder().decode(key);
        } catch (IllegalArgumentException invalid) {
            throw new IllegalArgumentException("Invalid Identity signing key configuration");
        }
        if (raw.length < 32 || raw.length > 1024) {
            throw new IllegalArgumentException("Identity signing key must contain at least 32 random bytes");
        }
        if (accessSeconds < 1 || accessSeconds > 900 || sessionSeconds < accessSeconds || sessionSeconds > 86400
                || windowSeconds < 1 || windowSeconds > 3600 || globalLimit < 1 || globalLimit > 10000
                || ipLimit < 1 || ipLimit > globalLimit || usernameLimit < 1 || usernameLimit > globalLimit
                || bucketCapacity < 2 || bucketCapacity > 100000) {
            throw new IllegalArgumentException("Invalid Identity authentication limits");
        }
        this.signingKey = new SecretKeySpec(raw, "HmacSHA256");
        this.accessLifetime = Duration.ofSeconds(accessSeconds);
        this.sessionLifetime = Duration.ofSeconds(sessionSeconds);
        this.windowSeconds = windowSeconds;
        this.globalLimit = globalLimit;
        this.ipLimit = ipLimit;
        this.usernameLimit = usernameLimit;
        this.bucketCapacity = bucketCapacity;
    }

    SecretKey signingKey() { return signingKey; }
    public Duration accessLifetime() { return accessLifetime; }
    public Duration sessionLifetime() { return sessionLifetime; }
    public int windowSeconds() { return windowSeconds; }
    public int globalLimit() { return globalLimit; }
    public int ipLimit() { return ipLimit; }
    public int usernameLimit() { return usernameLimit; }
    public int bucketCapacity() { return bucketCapacity; }
}
