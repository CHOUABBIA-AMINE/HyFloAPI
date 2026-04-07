package dz.sh.trc.hyflo.platform.ai.agent.strategy;

import dz.sh.trc.hyflo.platform.ai.agent.strategy.AgentStrategy.AgentExecutionMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Registry that indexes all {@link AgentStrategy} implementations by their
 * {@link AgentExecutionMode} and provides O(1) lookup for the executor.
 *
 * <p>Spring collects every {@link AgentStrategy} bean into the constructor
 * list automatically. No manual registration is needed — adding a new
 * strategy class annotated with {@code @Component} is sufficient.</p>
 *
 * <p>The registry validates at startup that no two strategies share the
 * same mode, failing fast before any AI call is attempted.</p>
 */
@Component
public class AgentStrategyRegistry {

    private static final Logger log = LoggerFactory.getLogger(AgentStrategyRegistry.class);

    private final Map<AgentExecutionMode, AgentStrategy> strategyMap;

    /**
     * @param strategies all {@link AgentStrategy} beans collected by Spring
     */
    public AgentStrategyRegistry(List<AgentStrategy> strategies) {
        Objects.requireNonNull(strategies, "strategies list must not be null");

        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        AgentStrategy::mode,
                        Function.identity(),
                        (a, b) -> {
                            throw new IllegalStateException(
                                    "Duplicate AgentStrategy for mode: " + a.mode()
                                    + " — conflicting beans: "
                                    + a.getClass().getSimpleName()
                                    + " and " + b.getClass().getSimpleName());
                        }
                ));

        log.info("[AgentStrategyRegistry] Registered {} strategy/strategies: {}",
                strategyMap.size(),
                strategyMap.keySet());
    }

    /**
     * Returns the strategy registered for the given execution mode.
     *
     * @param mode the desired execution mode
     * @return the matching strategy — never null
     * @throws IllegalArgumentException if no strategy is registered for the mode
     */
    public AgentStrategy resolve(AgentExecutionMode mode) {
        Objects.requireNonNull(mode, "mode must not be null");
        AgentStrategy strategy = strategyMap.get(mode);
        if (strategy == null) {
            throw new IllegalArgumentException(
                    "No AgentStrategy registered for mode: " + mode
                    + ". Available modes: " + strategyMap.keySet());
        }
        return strategy;
    }

    /**
     * Returns {@code true} if a strategy for the given mode is registered.
     *
     * @param mode the execution mode to check
     * @return {@code true} if a strategy exists
     */
    public boolean supports(AgentExecutionMode mode) {
        return strategyMap.containsKey(mode);
    }
}