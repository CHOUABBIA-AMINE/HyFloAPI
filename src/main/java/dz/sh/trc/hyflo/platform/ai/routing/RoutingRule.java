package dz.sh.trc.hyflo.platform.ai.routing;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * A single declarative routing rule mapping a condition to a target model ID.
 *
 * <p>Rules are evaluated in priority order by {@link RuleBasedModelRouter}.
 * The first matching rule wins — no further rules are evaluated.</p>
 *
 * <h3>Design rationale:</h3>
 * <p>Using a {@link Predicate} for the condition keeps rules composable and
 * testable in isolation. Adding a new routing criterion (e.g. time-of-day,
 * request size) only requires creating a new {@code RoutingRule} instance —
 * no changes to the router or existing rules.</p>
 *
 * <pre>
 * Example usage:
 *
 * RoutingRule crisisRule = new RoutingRule(
 *     "crisis-strong-model",
 *     ctx -> ctx.getAgentType() == AgentType.CRISIS,
 *     "gpt-4o",
 *     1            // highest priority
 * );
 * </pre>
 */
public final class RoutingRule {

    /** Human-readable rule identifier for logging and debugging. */
    private final String name;

    /**
     * Condition that determines whether this rule applies to a given
     * {@link RoutingContext}.
     */
    private final Predicate<RoutingContext> condition;

    /**
     * Target model ID to use when this rule matches.
     * Must match a model identifier understood by the active adapter
     * (e.g. {@code "gpt-4o"}, {@code "gpt-4o-mini"}, {@code "mistral"},
     * {@code "llama3"}).
     */
    private final String targetModelId;

    /**
     * Rule evaluation priority. Lower value = higher priority.
     * Rules with priority 1 are evaluated before rules with priority 10.
     */
    private final int priority;

    public RoutingRule(String name,
                       Predicate<RoutingContext> condition,
                       String targetModelId,
                       int priority) {
        this.name          = Objects.requireNonNull(name, "rule name must not be null");
        this.condition     = Objects.requireNonNull(condition, "rule condition must not be null");
        this.targetModelId = Objects.requireNonNull(targetModelId, "targetModelId must not be null");
        this.priority      = priority;
    }

    /**
     * Tests whether this rule applies to the given routing context.
     *
     * @param context the routing context to test
     * @return {@code true} if this rule matches and its {@code targetModelId} should be used
     */
    public boolean matches(RoutingContext context) {
        Objects.requireNonNull(context, "RoutingContext must not be null");
        return condition.test(context);
    }

    