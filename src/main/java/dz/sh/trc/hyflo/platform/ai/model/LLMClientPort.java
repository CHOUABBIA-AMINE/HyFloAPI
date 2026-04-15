package dz.sh.trc.hyflo.platform.ai.model;

import dz.sh.trc.hyflo.platform.ai.agent.AgentRequest;
import dz.sh.trc.hyflo.platform.ai.agent.AgentResponse;

/**
 * Internal model-layer port for raw LLM client calls.
 *
 * <p>This port sits one level below {@link dz.sh.trc.hyflo.platform.ai.agent.AgentPort}
 * in the {@code platform/ai} call chain. Concrete adapters implement this
 * interface and are never exposed outside {@code platform/ai/model}.</p>
 *
 * <h3>Layer position:</h3>
 * <pre>
 *   AgentPort  (agent layer — strategy-selected, session/tool-aware)
 *       ↓
 *   LLMClientPort  (model layer — raw prompt ↔ response, no strategy logic)
 *       ↓
 *   Spring AI ChatClient / ChatModel  (SDK — never referenced above this layer)
 * </pre>
 *
 * <h3>Why a separate port at this level?</h3>
 * <ul>
 *   <li>Strategies ({@code StatelessAnalysisStrategy}, {@code ConversationalStrategy},
 *       etc.) depend on {@link dz.sh.trc.hyflo.platform.ai.agent.AgentPort} —
 *       not on this port.</li>
 *   <li>This port is the exact boundary where Spring AI SDK classes appear.
 *       Changing the Spring AI version or switching providers only requires
 *       changing the adapter implementing this interface.</li>
 *   <li>Enables isolated unit testing: strategies are tested with a mock
 *       {@code AgentPort}; adapters are tested with a mock {@code LLMClientPort}.</li>
 * </ul>
 *
 * <p>Implementations in this commit:</p>
 * <ul>
 *   <li>{@link SpringAiLLMAdapter} — uses Spring AI {@code ChatClient} (default)</li>
 *   <li>{@link OllamaLLMAdapter} — Ollama-specific adapter (local fallback)</li>
 * </ul>
 */
public interface LLMClientPort {

    /**
     * Sends the assembled prompt to the underlying LLM and returns the raw response.
     *
     * <p>Implementations must:</p>
     * <ul>
     *   <li>Never throw unchecked exceptions directly — wrap in
     *       {@link dz.sh.trc.hyflo.platform.ai.agent.AgentExecutionException}</li>
     *   <li>Always populate {@code correlationId} and {@code provider} in the response</li>
     *   <li>Populate token usage fields from the LLM response metadata when available</li>
     * </ul>
     *
     * @param request the platform-level request (prompt + context + overrides)
     * @return the platform-level response (content + metadata) — never null
     */
    AgentResponse call(AgentRequest request);

    /**
     * Returns the provider identifier for this adapter.
     * Values: {@code "OPENAI"} | {@code "OLLAMA"}.
     *
     * @return provider string matching {@code hyflo.ai.provider} property values
     */
    String provider();

    /**
     * Performs a lightweight connectivity check against the provider endpoint.
     *
     * <p>Called by {@link dz.sh.trc.hyflo.platform.ai.agent.AgentPort#isAvailable()}
     * to surface provider health to the executor layer.</p>
     *
     * @return {@code true} if the provider endpoint is reachable
     */
    boolean isAvailable();
}