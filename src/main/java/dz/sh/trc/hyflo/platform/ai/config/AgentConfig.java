package dz.sh.trc.hyflo.platform.ai.config;

import dz.sh.trc.hyflo.platform.ai.agent.AgentPort;
import dz.sh.trc.hyflo.platform.ai.agent.AgentRequest;
import dz.sh.trc.hyflo.platform.ai.agent.AgentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;

/**
 * Spring bean configuration for the AI platform agent wiring.
 *
 * <p>Responsibilities of this class:</p>
 * <ol>
 *   <li>Register the {@code NoOpAgentAdapter} as the default
 *       {@link AgentPort} bean when {@code hyflo.ai.enabled=false}
 *       (or when no provider adapter bean is present).</li>
 *   <li>In Commit 2.2, this class will be extended to register the
 *       {@code OpenAiAgentAdapter} or {@code OllamaAgentAdapter}
 *       conditional on {@code hyflo.ai.provider}.</li>
 * </ol>
 *
 * <p>The {@code NoOpAgentAdapter} is always registered as a fallback via
 * {@code @ConditionalOnMissingBean}. This ensures the project compiles
 * and starts immediately even before any live adapter is implemented.</p>
 */
@Configuration
public class AgentConfig {

    private static final Logger log = LoggerFactory.getLogger(AgentConfig.class);

    /**
     * No-op {@link AgentPort} fallback — active when:
     * <ul>
     *   <li>{@code hyflo.ai.enabled=false} (default), OR</li>
     *   <li>no other {@link AgentPort} bean has been registered
     *       (guards against missing provider adapters)</li>
     * </ul>
     *
     * <p>Returns a static {@link AgentResponse} with {@code fallback=true}
     * and provider {@code "NONE"} for every call. Safe, zero-latency,
     * no external dependency.</p>
     */
    @Bean
    @ConditionalOnMissingBean(AgentPort.class)
    AgentPort noOpAgentAdapter() {
        log.info("[AgentConfig] Registering NoOpAgentAdapter " +
                 "(hyflo.ai.enabled=false or no provider adapter present)");
        return new AgentPort() {

            @Override
            public AgentResponse call(AgentRequest request) {
                log.debug("[NoOpAgentAdapter] call — correlationId={} — AI is disabled, returning fallback",
                        request.getContext().getCorrelationId());
                return AgentResponse.builder(
                                "AI capabilities are currently disabled. " +
                                "Set hyflo.ai.enabled=true and configure a provider to activate.",
                                "NONE")
                        .modelId("no-op")
                        .fallback(true)
                        .respondedAt(Instant.now())
                        .correlationId(request.getContext().getCorrelationId())
                        .build();
            }

            @Override
            public String provider() {
                return "NONE";
            }

            @Override
            public boolean isAvailable() {
                return false;
            }
        };
    }
}