package dz.sh.trc.hyflo.platform.ai.resilience;

import dz.sh.trc.hyflo.platform.ai.agent.AgentExecutionException;
import dz.sh.trc.hyflo.platform.ai.agent.AgentRequest;
import dz.sh.trc.hyflo.platform.ai.agent.AgentResponse;
import dz.sh.trc.hyflo.platform.ai.model.LLMClientPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Decorator that wraps a {@link LLMClientPort} with the full resilience pipeline.
 *
 * <p>The pipeline order is:</p>
 * <pre>
 *   caller
 *     → AiResilienceDecorator.execute(request, primaryAdapter)
 *         → AiCircuitBreaker.execute(...)         [1. reject if open]
 *             → AiRetryPolicy.execute(...)         [2. retry with backoff]
 *                 → primaryAdapter.call(request)   [3. actual LLM call]
 *
 *   on transient failure after exhaustion:
 *     → FallbackModelResolver.resolve(request, failedProvider, cause)
 *         → secondary adapter OR degraded static response
 * </pre>
 *
 * <h3>Failure decision tree:</h3>
 * <ul>
 *   <li>Circuit OPEN → skip retry → go to fallback immediately</li>
 *   <li>Transient failure → retry up to maxAttempts → if still failing → fallback</li>
 *   <li>Non-transient failure (auth, invalid request) → propagate immediately,
 *       no retry, no fallback (retrying auth errors wastes quota)</li>
 * </ul>
 *
 * <h3>Usage by adapters:</h3>
 * <p>{@link dz.sh.trc.hyflo.platform.ai.model.SpringAiLLMAdapter} and
 * {@link dz.sh.trc.hyflo.platform.ai.model.OllamaLLMAdapter} call
 * {@link #execute(AgentRequest, LLMClientPort)} instead of calling the
 * provider SDK directly. This keeps resilience logic entirely out of
 * the adapter implementations.</p>
 *
 * <p><strong>This class is stateless per call.</strong> The circuit breaker
 * and retry state live in the Resilience4j registries, not here.</p>
 */
@Component
public class AiResilienceDecorator {

    private static final Logger log = LoggerFactory.getLogger(AiResilienceDecorator.class);

    private final AiRetryPolicy retryPolicy;
    private final AiCircuitBreaker circuitBreaker;
    private final FallbackModelResolver fallbackResolver;

    public AiResilienceDecorator(AiRetryPolicy retryPolicy,
                                  AiCircuitBreaker circuitBreaker,
                                  FallbackModelResolver fallbackResolver) {
        this.retryPolicy      = Objects.requireNonNull(retryPolicy,      "AiRetryPolicy must not be null");
        this.circuitBreaker   = Objects.requireNonNull(circuitBreaker,   "AiCircuitBreaker must not be null");
        this.fallbackResolver = Objects.requireNonNull(fallbackResolver, "FallbackModelResolver must not be null");
    }

    /**
     * Executes the primary LLM adapter call through the full resilience pipeline.
     *
     * @param request        the assembled platform-level request
     * @param primaryAdapter the adapter to call first (OpenAI or Ollama)
     * @return the response — from primary, fallback, or degraded — never null
     * @throws AgentExecutionException only for non-transient failures
     *         (e.g. authentication errors) that should not be retried or
     *         swallowed by the fallback
     */
    public AgentResponse execute(AgentRequest request, LLMClientPort primaryAdapter) {
        Objects.requireNonNull(request,        "AgentRequest must not be null");
        Objects.requireNonNull(primaryAdapter, "primaryAdapter must not be null");

        String correlationId = request.getContext().getCorrelationId();
        String provider      = primaryAdapter.provider();

        log.debug("[AiResilienceDecorator] execute — provider={} correlationId={}",
                provider, correlationId);

        try {
            // Pipeline: circuit breaker → retry → LLM call
            return circuitBreaker.execute(
                    () -> retryPolicy.execute(
                            () -> primaryAdapter.call(request)
                    ),
                    correlationId
            );

        } catch (AgentExecutionException e) {
            if (e.isTransient()) {
                // Transient: circuit open or retry exhausted → resolve fallback
                log.warn("[AiResilienceDecorator] transient failure on provider={} — "
                        + "activating fallback. correlationId={} cause={}",
                        provider, correlationId, e.getMessage());
                return fallbackResolver.resolve(request, provider, e);
            }

            // Non-transient: auth error, bad request — propagate immediately
            log.error("[AiResilienceDecorator] non-transient failure on provider={} — "
                    + "propagating. correlationId={} cause={}",
                    provider, correlationId, e.getMessage());
            throw e;

        } catch (Exception e) {
            // Unexpected: wrap and route to fallback (defensive)
            log.error("[AiResilienceDecorator] unexpected failure on provider={} — "
                    + "activating fallback. correlationId={}",
                    provider, correlationId, e);

            AgentExecutionException wrapped = new AgentExecutionException(
                    "Unexpected error in resilience pipeline: " + e.getMessage(),
                    e, correlationId, true);
            return fallbackResolver.resolve(request, provider, wrapped);
        }
    }
}