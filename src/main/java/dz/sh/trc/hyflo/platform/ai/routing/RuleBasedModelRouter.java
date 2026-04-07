package dz.sh.trc.hyflo.platform.ai.routing;

import dz.sh.trc.hyflo.platform.ai.agent.strategy.AgentStrategy.AgentExecutionMode;
import dz.sh.trc.hyflo.platform.ai.config.AiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Rule-based {@link ModelRouter} implementation.
 *
 * <p>Evaluates an ordered list of {@link RoutingRule}s sorted by priority
 * (lowest value = highest priority). Returns the {@code targetModelId} of
 * the first matching rule. Falls back to {@link #defaultModelId()} when
 * no rule matches.</p>
 *
 * <h3>Default routing strategy (codified as rules):</h3>
 * <table border="1">
 *   <tr><th>Priority</th><th>Condition</th><th>Model (OpenAI)</th><th>Model (Ollama)</th></tr>
 *   <tr><td>1</td><td>hint = "local-only"</td><td>mistral</td><td>mistral</td></tr>
 *   <tr><td>2</td><td>CRISIS or criticality = CRITICAL</td><td>gpt-4o</td><td>llama3</td></tr>
 *   <tr><td>3</td><td>ASSISTANT + CONVERSATIONAL</td><td>gpt-4o-mini</td><td>mistral</td></tr>
 *   <tr><td>4</td><td>FLOW + STATELESS</td><td>gpt-4o-mini</td><td>mistral</td></tr>
 *   <tr><td>5</td><td>NETWORK + any</td><td>gpt-4o-mini</td><td>mistral</td></tr>
 *   <tr><td>10</td><td>hint = "prefer-fast"</td><td>gpt-4o-mini</td><td>mistral</td></tr>
 *   <tr><td>99</td><td>catch-all fallback</td><td>default model</td><td>default model</td></tr>
 * </table>
 *
 * <p>Rules are built at startup from {@link AiProperties} so model IDs
 * are never hardcoded in Java — they are driven by
 * {@code hyflo.ai.routing.*} properties.</p>
 */
@Component
public class RuleBasedModelRouter implements ModelRouter {

    private static final Logger log = LoggerFactory.getLogger(RuleBasedModelRouter.class);

    private final List<RoutingRule> rules;
    private final String defaultModel;

    public RuleBasedModelRouter(AiProperties aiProperties) {
        Objects.requireNonNull(aiProperties, "AiProperties must not be null");

        AiProperties.Routing routing = aiProperties.getRouting();
        this.defaultModel = routing.getDefaultModelId();
        this.rules = buildRules(routing);

        log.info("[RuleBasedModelRouter] Initialised with {} rule(s), defaultModel={}",
                rules.size(), defaultModel);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Iterates rules in priority order (ascending). Returns the target model
     * of the first matching rule. Logs the matched rule at DEBUG level.
     * Returns {@link #defaultModelId()} when no rule matches.</p>
     */
    @Override
    public String route(RoutingContext context) {
        Objects.requireNonNull(context, "RoutingContext must not be null");

        for (RoutingRule rule : rules) {
            if (rule.matches(context)) {
                log.debug("[RuleBasedModelRouter] matched rule='{}' → model='{}' context={}",
                        rule.getName(), rule.getTargetModelId(), context);
                return rule.getTargetModelId();
            }
        }

        log.debug("[RuleBasedModelRouter] no rule matched → defaultModel='{}' context={}",
                defaultModel, context);
        return defaultModel;
    }

    @Override
    public String defaultModelId() {
        return defaultModel;
    }

    // -------------------------------------------------------------------------
    // Rule construction from AiProperties.Routing
    // -------------------------------------------------------------------------

    private List<RoutingRule> buildRules(AiProperties.Routing r) {
        List<RoutingRule> built = new ArrayList<>();

        // Priority 1 — explicit local-only hint (highest priority)
        built.add(new RoutingRule(
                "hint-local-only",
                ctx -> "local-only".equalsIgnoreCase(ctx.getRoutingHint()),
                r.getLocalModelId(),
                1
        ));

        // Priority 2 — CRISIS agent OR CRITICAL severity → strong model
        built.add(new RoutingRule(
                "crisis-or-critical",
                ctx -> ctx.getAgentType() == RoutingContext.AgentType.CRISIS
                    || ctx.getCriticalityLevel() == RoutingContext.CriticalityLevel.CRITICAL,
                r.getStrongModelId(),
                2
        ));

        // Priority 3 — ASSISTANT conversational → fast model
        built.add(new RoutingRule(
                "assistant-conversational",
                ctx -> ctx.getAgentType() == RoutingContext.AgentType.ASSISTANT
                    && ctx.getExecutionMode() == AgentExecutionMode.CONVERSATIONAL,
                r.getFastModelId(),
                3
        ));

        // Priority 4 — FLOW stateless → fast / cheap model
        built.add(new RoutingRule(
                "flow-stateless",
                ctx -> ctx.getAgentType() == RoutingContext.AgentType.FLOW
                    && ctx.getExecutionMode() == AgentExecutionMode.STATELESS,
                r.getFastModelId(),
                4
        ));

        // Priority 5 — NETWORK any mode → fast / moderate model
        built.add(new RoutingRule(
                "network-any",
                ctx -> ctx.getAgentType() == RoutingContext.AgentType.NETWORK,
                r.getFastModelId(),
                5
        ));

        // Priority 10 — explicit prefer-fast hint
        built.add(new RoutingRule(
                "hint-prefer-fast",
                ctx -> "prefer-fast".equalsIgnoreCase(ctx.getRoutingHint()),
                r.getFastModelId(),
                10
        ));

        // Priority 20 — explicit require-strong hint
        built.add(new RoutingRule(
                "hint-require-strong",
                ctx -> "require-strong".equalsIgnoreCase(ctx.getRoutingHint()),
                r.getStrongModelId(),
                20
        ));

        // Sort by priority ascending (lower = higher priority, first match wins)
        built.sort(Comparator.comparingInt(RoutingRule::getPriority));
        return built;
    }
}