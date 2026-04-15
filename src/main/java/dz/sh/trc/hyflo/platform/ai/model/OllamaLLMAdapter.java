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
import org.springframework.ai.ollama.api.OllamaOptions;

import java.time.Instant;
import java.util.Objects;

/**
 * Ollama-specific {@link LLMClientPort} adapter for local model inference.
 *
 * <p>Structurally mirrors {@link SpringAiLLMAdapter} but uses
 * {@link OllamaOptions} for model-specific overrides (e.g. {@code numPredict}
 * instead of {@code maxTokens}, Ollama-specific sampling parameters).</p>
 *
 * <p>Activated when {@code hyflo.ai.provider=ollama}.
 * Instantiated by {@link dz.sh.trc.hyflo.platform.ai.config.SpringAiClientConfig}
 * under the {@code @ConditionalOnProperty(name="hyflo.ai.provider", havingValue="ollama")}
 * condition.</p>
 *
 * <h3>Why a separate class instead of reusing {@link SpringAiLLMAdapter}?</h3>
 * <ul>
 *   <li>Ollama has model-specific options ({@code OllamaOptions}) that differ
 *       from the generic {@code ChatOptions} interface</li>
 *   <li>Ollama's {@code isAvailable()} check should probe the local endpoint,
 *       not make a token-consuming LLM call</li>
 *   <li>Logging and error messages are provider-labelled separately for
 *       operational clarity in production vs. local environments</li>
 * </ul>
 *
 * <p><strong>Not a Spring bean.</strong> Instantiated exclusively by
 * {@link SpringAiClientConfig} via {@code @Bean}.</p>
 */
public class OllamaLLMAdapter implements LLMClientPort {

    private static final Logger log = LoggerFactory.getLogger(OllamaLLMAdapter.class);

    static final String PROVIDER_NAME = "OLLAMA";

    /**
     * Default HyFlo system prompt — identical to {@link SpringAiLLMAdapter}
     * to ensure consistent assistant behaviour regardless of provider.
     */
    private static final String DEFAULT_SYSTEM_PROMPT =
            "You are HyFlo AI, an expert assistant for hydrocarbon pipeline operations. "
          + "You provide accurate, concise, and safety-aware analysis. "
          + "Always base responses on provided data. "
          + "Never invent sensor readings or pipeline states.";

    private final ChatClient chatClient;
    private final AiProperties aiProperties;

    /**
     * @param chatClient   the Spring AI ChatClient configured for Ollama
     * @param aiProperties bound AI platform properties
     */
    public OllamaLLMAdapter(ChatClient chatClient, AiProperties aiProperties) {
        this.chatClient   = Objects.requireNonNull(chatClient,   "ChatClient must not be null");
        this.aiProperties = Objects.requireNonNull(aiProperties, "AiProperties must not be null");
    }

    /**
     * {@inheritDoc}
     *
     * <p>Uses {@link OllamaOptions} for model-specific overrides when
     * {@code maxTokens} or {@code temperature} are set on the request.</p>
     */
    @Override
    public AgentResponse call(AgentRequest request) {
        Objects.requireNonNull(request, "AgentRequest must not be null");

        AgentContext ctx = request.getContext();
        String correlationId = ctx.getCorrelationId();

        log.debug("[OllamaLLMAdapter] call — correlationId={}", correlationId);

        try {
            String systemPrompt = resolveSystemPrompt(ctx);

            ChatClient.ChatClientRequestSpec spec = chatClient.prompt()
                    .system(systemPrompt)
                    .user(request.getPrompt());

            if (request.getMaxTokens() != null || request.getTemperature() != null) {
                spec = applyOllamaOptions(spec, request);
            }

            Instant callStart = Instant.now();
            ChatResponse chatResponse = spec.call().chatResponse();
            long latencyMs = Instant.now().toEpochMilli() - callStart.toEpochMilli();

            if (chatResponse == null
                    || chatResponse.getResult() == null
                    || chatResponse.getResult().getOutput() == null) {
                throw new AgentExecutionException(
                        "Ollama returned null ChatResponse or empty output", correlationId);
            }

            String content = chatResponse.getResult().getOutput().getText();
            SpringAiLLMAdapter.TokenUsage usage = extractUsage(chatResponse);

            log.debug("[OllamaLLMAdapter] call completed — tokens={} latencyMs={} correlationId={}",
                    usage.total(), latencyMs, correlationId);

            return AgentResponse.builder(content, PROVIDER_NAME)
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
                    "OllamaLLMAdapter call failed: " + e.getMessage(), e, correlationId);
        }
    }

    @Override
    public String provider() {
        return PROVIDER_NAME;
    }

    /**
     * {@inheritDoc}
     *
     * <p>For Ollama, availability is determined by a direct HTTP health
     * check against the Ollama server rather than a token-consuming LLM call.
     * Spring AI's {@code OllamaApi} exposes a list-models endpoint that
     * serves as a lightweight connectivity probe.</p>
     */
    @Override
    public boolean isAvailable() {
        try {
            /*
             * Minimal probe: ask for a single-token completion.
             * A richer health check using OllamaApi.listModels() is deferred
             * to Phase 3 (platform/ai/health).
             */
            chatClient.prompt()
                    .user("ping")
                    .call()
                    .content();
            return true;
        } catch (Exception e) {
            log.warn("[OllamaLLMAdapter] isAvailable check failed: {}", e.getMessage());
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
     * Builds {@link OllamaOptions} from per-request overrides.
     * {@code numPredict} is the Ollama equivalent of {@code maxTokens}.
     */
    private ChatClient.ChatClientRequestSpec applyOllamaOptions(
            ChatClient.ChatClientRequestSpec spec, AgentRequest request) {

        OllamaOptions.Builder optBuilder = OllamaOptions.builder();

        if (request.getMaxTokens() != null) {
            optBuilder.numPredict(request.getMaxTokens());
        }
        if (request.getTemperature() != null) {
            optBuilder.temperature(request.getTemperature());
        }
        return spec.options(optBuilder.build());
    }

    /**
     * Extracts token usage from {@link ChatResponseMetadata}.
     * Returns zeros when Ollama does not report usage.
     */
    private SpringAiLLMAdapter.TokenUsage extractUsage(ChatResponse chatResponse) {
        try {
            ChatResponseMetadata metadata = chatResponse.getMetadata();
            if (metadata != null) {
                Usage usage = metadata.getUsage();
                if (usage != null) {
                    return new SpringAiLLMAdapter.TokenUsage(
                            (int) usage.getPromptTokens(),
                            (int) usage.getCompletionTokens());
                }
            }
        } catch (Exception e) {
            log.debug("[OllamaLLMAdapter] Could not extract token usage: {}", e.getMessage());
        }
        return new SpringAiLLMAdapter.TokenUsage(0, 0);
    }
}