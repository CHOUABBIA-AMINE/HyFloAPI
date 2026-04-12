package dz.sh.trc.hyflo.platform.ai.cost;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Declares token budget limits and warning thresholds per user and per role.
 *
 * <p>Loaded from {@code hyflo.ai.cost.*} in {@code application.properties}.
 * Per-user limits always override role limits — if both apply to the same user,
 * the per-user value wins.</p>
 *
 * <h3>Budget dimensions:</h3>
 * <ul>
 *   <li><strong>Daily limit</strong> — rolling 24-hour token allowance.
 *       Reset at midnight UTC by {@link TokenUsageTracker}.</li>
 *   <li><strong>Monthly limit</strong> — calendar-month token allowance.</li>
 *   <li><strong>Per-request reservation cap</strong> — maximum tokens a single
 *       {@link TokenBudgetManager#reserve} call may reserve. Prevents one
 *       runaway request from consuming the full daily budget.</li>
 *   <li><strong>Warn threshold</strong> — fraction of the daily limit at which
 *       {@link BudgetExceededHandler} starts emitting warnings (e.g. 0.80 = 80%).</li>
 * </ul>
 *
 * <h3>Resolution order for a userId + role pair:</h3>
 * <ol>
 *   <li>Per-user override map ({@code hyflo.ai.cost.user-overrides.*})</li>
 *   <li>Per-role limit map ({@code hyflo.ai.cost.role-limits.*})</li>
 *   <li>Global defaults ({@code hyflo.ai.cost.default-daily-limit-tokens}, etc.)</li>
 * </ol>
 *
 * <h3>Defaults (no configuration needed to run):</h3>
 * <ul>
 *   <li>Daily: 200 000 tokens</li>
 *   <li>Monthly: 4 000 000 tokens</li>
 *   <li>Per-request cap: 16 000 tokens (covers a GPT-4o turn with full history)</li>
 *   <li>Warn at 80% of daily limit</li>
 * </ul>
 */
@Component
@ConfigurationProperties(prefix = "hyflo.ai.cost")
public class CostPolicy {

    private static final Logger log = LoggerFactory.getLogger(CostPolicy.class);

    // -------------------------------------------------------------------------
    // Global defaults
    // -------------------------------------------------------------------------

    private int    defaultDailyLimitTokens   = 200_000;
    private int    defaultMonthlyLimitTokens = 4_000_000;
    private int    defaultPerRequestCapTokens = 16_000;
    private double defaultWarnThreshold       = 0.80;

    /**
     * Whether to hard-block the request when the budget is exceeded.
     * Default {@code false}: degraded mode is allowed (shorter context, cheaper model).
     */
    private boolean blockOnExceeded = false;

    // -------------------------------------------------------------------------
    // Per-role maps (key = role name without ROLE_ prefix, e.g. "OPERATOR")
    // -------------------------------------------------------------------------

    private final Map<String, Integer> roleDailyLimits   = new HashMap<>();
    private final Map<String, Integer> roleMonthlyLimits = new HashMap<>();

    // -------------------------------------------------------------------------
    // Per-user maps (key = userId)
    // -------------------------------------------------------------------------

    private final Map<String, Integer> userDailyOverrides   = new HashMap<>();
    private final Map<String, Integer> userMonthlyOverrides = new HashMap<>();

    // -------------------------------------------------------------------------
    // Resolution API
    // -------------------------------------------------------------------------

    /**
     * Resolves the effective daily token limit for a user+role combination.
     *
     * @param userId   the authenticated user ID
     * @param roleName the user's primary role (e.g. "OPERATOR", "ADMIN")
     * @return the applicable daily token limit
     */
    public int resolveDailyLimit(String userId, String roleName) {
        Objects.requireNonNull(userId, "userId");

        if (userDailyOverrides.containsKey(userId)) {
            int limit = userDailyOverrides.get(userId);
            log.debug("[CostPolicy] daily limit from user-override userId='{}': {}", userId, limit);
            return limit;
        }
        if (roleName != null && roleDailyLimits.containsKey(roleName)) {
            int limit = roleDailyLimits.get(roleName);
            log.debug("[CostPolicy] daily limit from role='{}': {}", roleName, limit);
            return limit;
        }
        log.debug("[CostPolicy] daily limit from global default: {}", defaultDailyLimitTokens);
        return defaultDailyLimitTokens;
    }

    /**
     * Resolves the effective monthly token limit for a user+role combination.
     *
     * @param userId   the authenticated user ID
     * @param roleName the user's primary role
     * @return the applicable monthly token limit
     */
    public int resolveMonthlyLimit(String userId, String roleName) {
        Objects.requireNonNull(userId, "userId");

        if (userMonthlyOverrides.containsKey(userId)) {
            return userMonthlyOverrides.get(userId);
        }
        if (roleName != null && roleMonthlyLimits.containsKey(roleName)) {
            return roleMonthlyLimits.get(roleName);
        }
        return defaultMonthlyLimitTokens;
    }

    /**
     * Returns the maximum number of tokens a single reserve call may claim.
     * Prevents one request from consuming the entire daily budget.
     *
     * @return per-request token reservation cap
     */
    public int perRequestCap() {
        return defaultPerRequestCapTokens;
    }

    /**
     * Returns the warn threshold fraction (0.0–1.0).
     * When {@code dailyUsed / dailyLimit >= warnThreshold}, a warning is emitted.
     *
     * @return warn threshold as a fraction of the daily limit
     */
    public double warnThreshold() {
        return defaultWarnThreshold;
    }

    /**
     * Returns true if requests should be hard-blocked when the budget is exceeded.
     * When false, {@link BudgetExceededHandler} returns {@link BudgetDecision#ALLOW_DEGRADED}.
     *
     * @return whether to block on exceeded budget
     */
    public boolean isBlockOnExceeded() {
        return blockOnExceeded;
    }

    // -------------------------------------------------------------------------
    // @ConfigurationProperties setters
    // -------------------------------------------------------------------------

    public int    getDefaultDailyLimitTokens()    { return defaultDailyLimitTokens; }
    public void   setDefaultDailyLimitTokens(int v)    { this.defaultDailyLimitTokens    = v; }
    public int    getDefaultMonthlyLimitTokens()  { return defaultMonthlyLimitTokens; }
    public void   setDefaultMonthlyLimitTokens(int v)  { this.defaultMonthlyLimitTokens  = v; }
    public int    getDefaultPerRequestCapTokens() { return defaultPerRequestCapTokens; }
    public void   setDefaultPerRequestCapTokens(int v) { this.defaultPerRequestCapTokens = v; }
    public double getDefaultWarnThreshold()       { return defaultWarnThreshold; }
    public void   setDefaultWarnThreshold(double v)    { this.defaultWarnThreshold       = v; }
    public boolean isBlockOnExceeded_()            { return blockOnExceeded; }
    public void   setBlockOnExceeded(boolean v)        { this.blockOnExceeded             = v; }

    public Map<String, Integer> getRoleDailyLimits()    { return roleDailyLimits; }
    public Map<String, Integer> getRoleMonthlyLimits()  { return roleMonthlyLimits; }
    public Map<String, Integer> getUserDailyOverrides() { return userDailyOverrides; }
    public Map<String, Integer> getUserMonthlyOverrides(){ return userMonthlyOverrides; }
}