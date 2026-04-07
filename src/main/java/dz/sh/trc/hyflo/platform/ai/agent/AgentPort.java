package dz.sh.trc.hyflo.platform.ai.agent;

/**
 * Internal platform port for all AI provider adapters.
 *
 * <p>This is the single abstraction used inside {@code platform/ai}
 * between configuration/routing logic and the concrete provider adapters
 * (OpenAI, Ollama, NoOp). It is <strong>not</strong> the same as the
 * intelligence-layer ports (e.g. {@code FlowAnomalyAgentPort}).</p>
 *
 * <p>Intelligence-layer ports translate their domain DTOs into
 * {@link AgentRequest} and delegate here. This isolates all Spring AI
 * SDK usage to the concrete adapter implementations.</p>
 *
 * <h3>Implementations expected in Commit 2.2:</h3>
 * <ul>
 *   <li>{@code OpenAiAgentAdapter} — live adapter using Spring AI OpenAI</li>
 *   <li>{@code OllamaAgentAdapter} — live adapter using Spring AI Ollama</li>
 *   <li>{@code NoOpAgentAdapter}   — fallback when {@code hyflo.ai.enabled=false}</li>
 * </ul>
 */
public interface AgentPort {

    /**
     * Sends a prompt to the underlying LLM and returns the raw response.
     *
     * @param request the platform-level request containing prompt and context
     * @return the platform-level response with generated content and metadata
     */
    AgentResponse call(AgentRequest request);

    /**
     * Returns the provider identifier handled by this adapter.
     * Values: {@code "OPENAI"}, {@code "OLLAMA"}, {@code "NONE"}.
     *
     * @return provider string matching {@link AiProperties#getProvider()}
     */
    String provider();

    /**
     * Checks whether this adapter's underlying provider is reachable.
     *
     * @return {@code true} if a call to {@link #call} can succeed right now
     */
    boolean isAvailable();
}