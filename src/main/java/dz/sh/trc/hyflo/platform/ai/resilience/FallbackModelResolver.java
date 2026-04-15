package dz.sh.trc.hyflo.platform.ai.resilience;

import dz.sh.trc.hyflo.platform.ai.agent.AgentExecutionException;
import dz.sh.trc.hyflo.platform.ai.agent.AgentRequest;
import dz.sh.trc.hyflo.platform.ai.agent.AgentResponse;
import dz.sh.trc.hyflo.platform.ai.model.LLMClientPort;
import dz.sh.trc.hyflo.platform.ai.routing.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Resolves and executes a fallback LLM call when the primary provider fails.
 *
 * <p>The fallback chain is ordered by preference:</p>
 * <ol>
 *   <li><strong>Hint-driven override</strong> — if the request has a
 *       {@code "local-only"} routing hint and an Ollama adapter is
 *       registered, the Ollama adapter is tried first</li>
 *   <li><strong>Secondary provider</strong> — if OpenAI was primary,
 *       Ollama is tried as secondary (and vice versa)</li>
 *   <li><strong>Degraded static response</strong> — if all adapters fail,
 *       returns a structured degraded {@link AgentResponse} so the caller
 *       always receives a valid response object (never throws to user)</li>
 * </ol>
 *
 * <h3>When is this invoked?</h3>
 * <p>{@link AiResilienceDecorator} calls this resolver when:</p>
 * <ul>
 *   <li>The circuit breaker is OPEN ({@code CallNotPermittedException})</li>
 *   <li>Retry is exhausted and the exception is still transient</li>
 * </ul>
 *
 * <p>Non-transient failures ({@code AUTH_INVALID}, {@code BAD_REQUEST}) are
 * not sent to this resolver — they are propagated immediately.</p>
 */
@Component
public class FallbackModelResolver {

    private static final Logger log = LoggerFactory.getLogger(FallbackModelResolver.class);

    /**
     * Static degraded response content — returned only when ALL adapters fail.
     * Domain-aware: informs the operator that AI analysis is temporarily unavailable.
     */
    private static final String DEGRADED_CONTENT =
            "AI analysis is temporarily unavailable. "
          + "Please review the raw sensor data manually and consult standard operating procedures. "
          + "This is an automated safety fallback — no AI inference was performed.";

    private static final String DEGRADED_PROVIDER = "DEGRADED_FALLBACK";

    /**
     * All registered {@link LLMClientPort} adapters injected by Spring.
     * Order follows Spring bean registration order (primary first).
     */
    private final List<LLMClientPort> adapters;

    /**
     * @param adapters all {@link LLMClientPort} beans in context
     *                 (Spring collects them automatically)
     */
    public FallbackModelResolver(List<LLMClientPort> adapters) {
        Objects.requireNonNull(adapters, "adapters list must not be null");
        this.adapters = new ArrayList<>(adapters);
        log.info("[FallbackModelResolver] Registered {} adapter(s): {}",
                adapters.size(),
                adapters.stream().map(LLMClientPort::provider).toList());
    }

    /**
     * Resolves and executes the best available fallback for a failed primary call.
     *
     * @param request        the original request (prompt + context unchanged)
     * @param failedProvider the provider name that just failed
     * @param cause          the original failure reason (for logging)
     * @return a valid {@link AgentResponse} — either from a fallback adapter
     *         or from the degraded static response
     */
    public AgentResponse resolve(AgentRequest request,
                                  String failedProvider,
                                  AgentExecutionException cause) {
        Objects.requireNonNull(request,        "AgentRequest must not be null");
        Objects.requireNonNull(failedProvider, "failedProvider must not be null");

        String correlationId = request.getContext().getCorrelationId();
        String routingHint   = request.getContext().getRoutingHint();

        log.warn("[FallbackModelResolver] primary provider={} failed — resolving fallback. "
                + "correlationId={} cause={}",
                failedProvider,
                correlationId,
                cause != null ? cause.getMessage() : "unknown");

        // Step 1: collect candidate fallback adapters (skip the one that just failed)
        List<LLMClientPort> candidates = buildCandidateChain(failedProvider, routingHint);

        // Step 2: try each candidate in order
        for (LLMClientPort candidate : candidates) {
            if (!candidate.isAvailable()) {
                log.debug("[FallbackModelResolver] skipping unavailable provider={}",
                        candidate.provider());
                continue;
            }
            try {
                log.info("[FallbackModelResolver] attempting fallback via provider={} correlationId={}",
                        candidate.provider(), correlationId);

                AgentResponse response = candidate.call(request);

                return AgentResponse.builder(response.getContent(), candidate.provider())
                        .promptTokens(response.getPromptTokens())
                        .completionTokens(response.getCompletionTokens())
                        .fallback(true)           // ← marks response as fallback
                        .respondedAt(Instant.now())
                        .correlationId(correlationId)
                        .build();

            } catch (Exception e) {
                log.warn("[FallbackModelResolver] fallback provider={} also failed: {} — trying next",
                        candidate.provider(), e.getMessage());
            }
        }

        // Step 3: all adapters exhausted — return degraded static response
        log.error("[FallbackModelResolver] ALL providers exhausted — returning degraded response. "
                + "correlationId={}", correlationId);

        return AgentResponse.builder(DEGRADED_CONTENT, DEGRADED_PROVIDER)
                .fallback(true)
                .degraded(true)
                .respondedAt(Instant.now())
                .correlationId(correlationId)
                .build();
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    /**
     * Builds the ordered candidate fallback chain.
     *
     * <p>Logic:</p>
     * <ul>
     *   <li>If hint is {@code "local-only"}: put Ollama adapters first</li>
     *   <li>Skip the failed provider</li>
     *   <li>Preserve registration order for remaining adapters</li>
     * </ul>
     */
    private List<LLMClientPort> buildCandidateChain(String failedProvider, String routingHint) {
        List<LLMClientPort> candidates = new ArrayList<>();
        boolean preferLocal = "local-only".equalsIgnoreCase(routingHint);

        // First pass: preferred adapters
        for (LLMClientPort adapter : adapters) {
            if (adapter.provider().equalsIgnoreCase(failedProvider)) continue;
            boolean isLocal = "OLLAMA".equalsIgnoreCase(adapter.provider());
            if (preferLocal && isLocal) candidates.add(0, adapter);
            else if (!preferLocal || !isLocal) candidates.add(adapter);
        }

        return candidates;
    }
}