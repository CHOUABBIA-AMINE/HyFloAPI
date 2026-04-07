package dz.sh.trc.hyflo.platform.ai.model;

import dz.sh.trc.hyflo.platform.ai.agent.AgentContext;
import dz.sh.trc.hyflo.platform.ai.agent.AgentExecutionException;
import dz.sh.trc.hyflo.platform.ai.agent.AgentRequest;
import dz.sh.trc.hyflo.platform.ai.agent.AgentResponse;
import dz.sh.trc.hyflo.platform.ai.config.AiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;

import java.time.Instant;
import java.util.Objects;

/**
 * Spring AI {@link ChatClient}-backed LLM adapter.
 *
 * <p>This is the default {@link LLMClientPort} implementation. It uses
 * the Spring AI 1.0.0 GA {@code ChatClient} fluent API, which is provider-
 * agnostic at the SDK level — the underlying model (OpenAI, Azure OpenAI,
 * etc.) is determined by which {@code ChatClient} bean is injected via
 * {@link dz.sh.trc.hyflo.platform.ai.config.SpringAiClientConfig}.</p>
 *
 * <h3>Responsibilities:</h3>
 * <ol>
 *   <li>Translate {@link AgentRequest} into a Spring AI {@code ChatClient} call</li>
 *   <li>Inject system instruction from {@link AgentContext#getSystemInstructionOverride()}
 *       when present, otherwise use a safe HyFlo default system prompt</li>
 *   <li>Apply {@code maxTokens} and {@code temperature} overrides when present</li>
 *   <li>Extract token usage from {@link ChatResponseMetadata}</li>
 *   <li>Wrap all Spring AI exceptions in {@link AgentExecutionException}</li>
 * </ol>
 *
 * <p><strong>Not a Spring bean.</strong> Instantiated exclusively by
 * {@link SpringAiClientConfig} via {@code @Bean} — one instance per
 * configured provider. This keeps Spring AI auto-configuration conflicts
 * under explicit control.</p>
 */
public class SpringAiLLMAdapter implements LLMClientPort {

    private static final Logger log = LoggerFactory.getLogger(SpringAiLLMAdapter.class);

    /**
     * Default system instruction injected when no override is provided.
     * Establishes HyFlo domain context for the LLM without leaking business rules.
     */
    private static final String DEFAULT_SYSTEM_PROMPT =
            "You are HyFlo AI, an expert assistant for hydrocarbon pipeline operations. "
          + "You provide accurate, concise, and safety-aware analysis. "
          + "Always base responses on provided data. "
          + "Never invent sensor readings or pipeline states.";

    private final ChatClient chatClient;
    private final AiProperties aiProperties;
    private final String providerName;

    /**
     * @param chatClient   the Spring AI ChatClient configured for a specific provider
     * @param aiProperties bound AI platform properties
     * @param providerName provider identifier (e.g. {@code "OPENAI"})
     */
    public SpringAiLLMAdapter(ChatClient chatClient,
                               AiProperties aiProperties,
                               String providerName) {
        this.chatClient   = Objects.requireNonNull(chatClient,   "ChatClient must not be null");
        this.aiProperties = Objects.requireNonNull(aiProperties, "AiProperties must not be null");
        this.providerName = Objects.requireNonNull(providerName, "providerName must not be null");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AgentResponse call(AgentRequest request) {
        Objects.requireNonNull(request, "AgentRequest must not be null");

        AgentContext ctx = request.getContext();
        String correlationId = ctx.getCorrelationId();

        log.debug("[SpringAiLLMAdapter:{}] call — correlationId={}", providerName, correlationId);

        try {
            String systemPrompt = resolveSystemPrompt(ctx);

            // Build the ChatClient call spec
            ChatClient.ChatClientRequestSpec spec = chatClient.prompt()
                    .system(systemPrompt)
                    .user(request.getPrompt());

            // Apply per-request overrides when present
            if (request.getMaxTokens() != null || request.getTemperature() != null) {
                spec = applyRequestOptions(spec, request);
            }

            Instant callStart = Instant.now();
            ChatResponse chatResponse = spec.call().chatResponse();
            long latencyMs = Instant.now().toEpochMilli() - callStart.toEpochMilli();

            if (chatResponse == null
                    || chatResponse.getResult() == null
                    || chatResponse.getResult().getOutput() == null) {
                throw new AgentExecutionException(
                        "Spring AI returned null ChatResponse or empty output",
                        correlationId);
            }

            String content = chatResponse.getResult().getOutput().getText();
            TokenUsage usage = extractUsage(chatResponse);

            log.debug("[SpringAiLLMAdapter:{}] call completed — tokens={} latencyMs={} correlationId={}",
                    providerName, usage.total(), latencyMs, correlationId);

            return AgentResponse.builder(content, providerName)
                    .promptTokens(usage.prompt())
                    .completionTokens(usage.completion())
                    .fallback(false)
                    .respondedAt(Instant.now())
                    .correlationId(correlationId)
                    .build();

        } catch (AgentExecutionException e) {
            throw e;
        } catch (Exception e) {
            throw new AgentExecutionException(
                    "SpringAiLLMAdapter[" + providerName + "] call failed: " + e.getMessage(),
                    e, correlationId);
        }
    }

    @Override
    public String provider() {
        return providerName;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Performs a minimal single-token probe call to verify the provider
     * endpoint is reachable. Returns {@code false} on any exception.</p>
     */
    @Override
    public boolean isAvailable() {
        try {
            chatClient.prompt()
                    .user("ping")
                    .call()
                    .content();
            return true;
        } catch (Exception e) {
            log.warn("[SpringAiLLMAdapter:{}] isAvailable check failed: {}",
                    providerName, e.getMessage());
            return false;
        }
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private String resolveSystemPrompt(AgentContext ctx) {
        String override = ctx.getSystemInstructionOverride();
        return (override != null && !override.isBlank()) ? override : DEFAULT_SYSTEM_PROMPT;
    }

    /**
     * Applies per-request {@code maxTokens} and {@code temperature} overrides.
     *
     * <p>Spring AI 1.0.0 GA exposes chat options via
     * {@code ChatOptions.builder()} passed into
     * {@code ChatClient.prompt().options(...)}.</p>
     */
    private ChatClient.ChatClientRequestSpec applyRequestOptions(
            ChatClient.ChatClientRequestSpec spec, AgentRequest request) {

        org.springframework.ai.chat.prompt.ChatOptions.Builder optBuilder =
                org.springframework.ai.chat.prompt.ChatOptions.builder();

        if (request.getMaxTokens() != null) {
            optBuilder.maxTokens(request.getMaxTokens());
        }
        if (request.getTemperature() != null) {
            optBuilder.temperature(request.getTemperature());
        }
        return spec.options(optBuilder.build());
    }

    /**
     * Extracts token usage from {@link ChatResponseMetadata}.
     * Returns zeros when the provider does not report usage.
     */
    private TokenUsage extractUsage(ChatResponse chatResponse) {
        try {
            ChatResponseMetadata metadata = chatResponse.getMetadata();
            if (metadata != null) {
                Usage usage = metadata.getUsage();
                if (usage != null) {
                    return new TokenUsage(
                            (int) usage.getPromptTokens(),
                            (int) usage.getCompletionTokens());
                }
            }
        } catch (Exception e) {
            log.debug("[SpringAiLLMAdapter] Could not extract token usage: {}", e.getMessage());
        }
        return new TokenUsage(0, 0);
    }

    /** Compact token usage carrier. Package-private — not part of public API. */
    record TokenUsage(int prompt, int completion) {
        int total() { return prompt + completion; }
    }
}