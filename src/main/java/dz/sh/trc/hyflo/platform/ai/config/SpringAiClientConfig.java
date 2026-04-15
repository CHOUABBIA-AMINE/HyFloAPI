package dz.sh.trc.hyflo.platform.ai.config;

import dz.sh.trc.hyflo.platform.ai.agent.AgentPort;
import dz.sh.trc.hyflo.platform.ai.agent.AgentRequest;
import dz.sh.trc.hyflo.platform.ai.agent.AgentResponse;
import dz.sh.trc.hyflo.platform.ai.model.LLMClientPort;
import dz.sh.trc.hyflo.platform.ai.model.OllamaLLMAdapter;
import dz.sh.trc.hyflo.platform.ai.model.SpringAiLLMAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.Instant;
import java.util.Objects;

/**
 * Spring bean configuration for AI provider adapter wiring.
 *
 * <p>This class is the only place in the entire codebase where Spring AI
 * {@code ChatModel} and {@code ChatClient} beans are referenced. All other
 * classes depend on {@link LLMClientPort} or
 * {@link dz.sh.trc.hyflo.platform.ai.agent.AgentPort} — never on Spring AI
 * classes directly.</p>
 *
 * <h3>Bean registration strategy:</h3>
 * <ul>
 *   <li>When {@code hyflo.ai.provider=openai}: registers
 *       {@link SpringAiLLMAdapter} as {@link LLMClientPort} and wires it as
 *       the active {@link AgentPort}</li>
 *   <li>When {@code hyflo.ai.provider=ollama}: registers
 *       {@link OllamaLLMAdapter} as {@link LLMClientPort} and wires it as
 *       the active {@link AgentPort}</li>
 *   <li>When {@code hyflo.ai.enabled=false} (default): neither provider bean
 *       is registered; {@code AgentConfig.noOpAgentAdapter()} remains the
 *       only {@link AgentPort} in context</li>
 * </ul>
 *
 * <p>Both provider beans are annotated {@code @Primary} conditionally so
 * that {@code AgentConfig.noOpAgentAdapter()} is cleanly overridden when
 * AI is enabled.</p>
 */
@Configuration
public class SpringAiClientConfig {

    private static final Logger log = LoggerFactory.getLogger(SpringAiClientConfig.class);

    // -------------------------------------------------------------------------
    // OpenAI provider beans
    // -------------------------------------------------------------------------

    /**
     * Registers a {@link SpringAiLLMAdapter} backed by the Spring AI OpenAI
     * {@code ChatModel} auto-configured by {@code spring-ai-starter-model-openai}.
     *
     * <p>Active only when {@code hyflo.ai.enabled=true} AND
     * {@code hyflo.ai.provider=openai}.</p>
     */
    @Bean
    @ConditionalOnProperty(name = "hyflo.ai.enabled", havingValue = "true")
    @ConditionalOnProperty(name = "hyflo.ai.provider", havingValue = "openai")
    LLMClientPort openAiLLMClientPort(
            ChatModel chatModel,
            AiProperties aiProperties) {
        log.info("[SpringAiClientConfig] Registering SpringAiLLMAdapter (provider=OPENAI)");
        ChatClient chatClient = ChatClient.builder(chatModel).build();
        return new SpringAiLLMAdapter(chatClient, aiProperties, "OPENAI");
    }

    /**
     * Wires the OpenAI {@link LLMClientPort} as the active {@link AgentPort}
     * when OpenAI is the configured provider.
     *
     * <p>This bean overrides the no-op fallback from {@link AgentConfig}
     * because it is marked {@code @Primary}.</p>
     */
    @Bean
    @Primary
    @ConditionalOnProperty(name = "hyflo.ai.enabled", havingValue = "true")
    @ConditionalOnProperty(name = "hyflo.ai.provider", havingValue = "openai")
    AgentPort openAiAgentPort(LLMClientPort openAiLLMClientPort) {
        log.info("[SpringAiClientConfig] Wiring OpenAI LLMClientPort as primary AgentPort");
        return buildAgentPortAdapter(openAiLLMClientPort);
    }

    // -------------------------------------------------------------------------
    // Ollama provider beans
    // -------------------------------------------------------------------------

    /**
     * Registers an {@link OllamaLLMAdapter} backed by the Spring AI Ollama
     * {@code ChatModel} auto-configured by {@code spring-ai-starter-model-ollama}.
     *
     * <p>Active only when {@code hyflo.ai.enabled=true} AND
     * {@code hyflo.ai.provider=ollama}.</p>
     */
    @Bean
    @ConditionalOnProperty(name = "hyflo.ai.enabled", havingValue = "true")
    @ConditionalOnProperty(name = "hyflo.ai.provider", havingValue = "ollama")
    LLMClientPort ollamaLLMClientPort(
            ChatModel chatModel,
            AiProperties aiProperties) {
        log.info("[SpringAiClientConfig] Registering OllamaLLMAdapter (provider=OLLAMA)");
        ChatClient chatClient = ChatClient.builder(chatModel).build();
        return new OllamaLLMAdapter(chatClient, aiProperties);
    }

    /**
     * Wires the Ollama {@link LLMClientPort} as the active {@link AgentPort}
     * when Ollama is the configured provider.
     */
    @Bean
    @Primary
    @ConditionalOnProperty(name = "hyflo.ai.enabled", havingValue = "true")
    @ConditionalOnProperty(name = "hyflo.ai.provider", havingValue = "ollama")
    AgentPort ollamaAgentPort(LLMClientPort ollamaLLMClientPort) {
        log.info("[SpringAiClientConfig] Wiring Ollama LLMClientPort as primary AgentPort");
        return buildAgentPortAdapter(ollamaLLMClientPort);
    }

    // -------------------------------------------------------------------------
    // Shared adapter builder
    // -------------------------------------------------------------------------

    /**
     * Bridges {@link LLMClientPort} into the {@link AgentPort} contract.
     *
     * <p>This anonymous adapter is intentionally thin: it only delegates
     * to the {@link LLMClientPort} and forwards {@code provider()} and
     * {@code isAvailable()} calls. No strategy logic lives here.</p>
     */
    private static AgentPort buildAgentPortAdapter(LLMClientPort llmClientPort) {
        Objects.requireNonNull(llmClientPort, "LLMClientPort must not be null");
        return new AgentPort() {

            @Override
            public AgentResponse call(AgentRequest request) {
                return llmClientPort.call(request);
            }

            @Override
            public String provider() {
                return llmClientPort.provider();
            }

            @Override
            public boolean isAvailable() {
                return llmClientPort.isAvailable();
            }
        };
    }
}