/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : LoginAttemptLimiter
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Security
 * @Module      : Platform / Security
 * @Package     : dz.sh.hyflo.platform.security
 *
 * @Description : Bounds login attempts by global, peer address and normalized username buckets.
 */

package dz.sh.hyflo.platform.security;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class LoginAttemptLimiter {

    private final IdentityAuthenticationProperties settings;
    private final Map<String, Integer> buckets = new HashMap<>();
    private long window = Long.MIN_VALUE;
    private int global;

    public LoginAttemptLimiter(IdentityAuthenticationProperties settings) {
        this.settings = settings;
    }

    /** Return zero when admitted, otherwise seconds until the next window. */
    public synchronized long retryAfter(String peer, String normalizedUsername, Instant now) {
        return admit(peer, "user:" + normalizedUsername, now);
    }

    /** Shares global and direct-peer limits without retaining refresh credentials or digests. */
    public synchronized long retryAfterRefresh(String peer, Instant now) {
        return admit(peer, null, now);
    }

    private long admit(String peer, String username, Instant now) {
        long second = now.getEpochSecond();
        long current = Math.floorDiv(second, settings.windowSeconds());
        if (current > window) {
            buckets.clear();
            global = 0;
            window = current;
        }
        long retry = Math.max(1, (window + 1) * settings.windowSeconds() - second);
        String ip = "ip:" + peer;
        int required = (buckets.containsKey(ip) ? 0 : 1)
                + (username == null || buckets.containsKey(username) ? 0 : 1);
        if (global >= settings.globalLimit() || buckets.getOrDefault(ip, 0) >= settings.ipLimit()
                || (username != null && buckets.getOrDefault(username, 0) >= settings.usernameLimit())
                || buckets.size() + required > settings.bucketCapacity()) {
            return retry;
        }
        global++;
        buckets.merge(ip, 1, Integer::sum);
        if (username != null) buckets.merge(username, 1, Integer::sum);
        return 0;
    }
}
