package dz.sh.trc.hyflo.platform.ai.agent;

import dz.sh.trc.hyflo.platform.ai.agent.strategy.AgentStrategy;
import dz.sh.trc.hyflo.platform.ai.agent.strategy.AgentStrategy.AgentExecutionMode;
import dz.sh.trc.hyflo.platform.ai.agent.strategy.AgentStrategyRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Slim orchestrator that routes AI calls to the correct {@link AgentStrategy}.
 *
 * <p>This class has exactly one responsibility: accept an {@link AgentRequest}
 * with an execution mode, delegate to the {@link AgentStrategyRegistry} to
 * resolve the correct strategy, and return the {@link AgentResponse}.</p>
 *
 * <p>No execution logic lives here. Adding a new AI execution pattern requires
 * only adding a new {@link AgentStrategy} implementation — the executor does
 * not change.</p>
 *
 * <h3>Call chain:</h3>
 * <pre>
 *   intelligence/*/port/*AgentPort  (domain adapter impl)
 *           ↓
 *   AgentExecutor.execute(request, mode)      (this class)
 *           ↓
 *   AgentStrategyRegistry.resolve(mode)
 *           ↓
 *   AgentStrategy.execute(request)            (concrete strategy)
 *           ↓
 *   AgentPort.call(request)                   (provider adapter)
 * </pre>
 */
@Component
public class AgentExecutor {

    private static final Logger log = LoggerFactory.getLogger(AgentExecutor.class);

    private final AgentStrategyRegistry strategyRegistry;

    public AgentExecutor(AgentStrategyRegistry strategyRegistry) {
        this.strategyRegistry =
                Objects.requireNonNull(strategyRegistry, "AgentStrategyRegistry must not be null");
    }

    /**
     * Routes the request to the strategy registered for the given mode.
     *
     * @param request the fully assembled platform-level request
     * @param mode    the execution mode that controls which strategy is used
     * @return the platform-level response — never null
     * @throws AgentExecutionException if the strategy throws or the mode is unsupported
     */
    public AgentResponse execute(AgentRequest request, AgentExecutionMode mode) {
        Objects.requireNonNull(request, "AgentRequest must not be null");
        Objects.requireNonNull(mode,    "AgentExecutionMode must not be null");

        log.debug("[AgentExecutor] execute — mode={} correlationId={}",
                mode, request.getContext().getCorrelationId());

        try {
            AgentStrategy strategy = strategyRegistry.resolve(mode);
            AgentResponse response = strategy.execute(request);
            log.debug("[AgentExecutor] completed — mode={} provider={} fallback={} correlationId={}",
                    mode, response.getProvider(), response.isFallback(),
                    response.getCorrelationId());
            return response;

        } catch (IllegalArgumentException e) {
            throw new AgentExecutionException(
                    "No strategy registered for mode " + mode, e,
                    request.getContext().getCorrelationId());
        } catch (AgentExecutionException e) {
            throw e;
        } catch (Exception e) {
            throw new AgentExecutionException(
                    "Unexpected error during agent execution for mode " + mode, e,
                    request.getContext().getCorrelationId());
        }
    }
}