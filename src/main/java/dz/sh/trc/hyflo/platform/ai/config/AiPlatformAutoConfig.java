package dz.sh.trc.hyflo.platform.ai.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Auto-configuration entry point for the HyFlo AI platform module.
 *
 * <p>Activates only when {@code hyflo.ai.enabled=true}. When the property
 * is absent or {@code false}, the entire AI configuration tree is skipped
 * and no Spring AI beans are initialised.</p>
 *
 * <p>This class is intentionally thin — it only enables the properties
 * binding and imports {@link AgentConfig} for bean wiring.
 * No business rules, no prompt logic, no adapter implementation here.</p>
 *
 * <h3>When AI is disabled (default):</h3>
 * <p>The {@code NoOpAgentAdapter} declared in {@link AgentConfig} remains
 * the only active {@link dz.sh.trc.hyflo.platform.ai.agent.AgentPort}
 * bean, so all intelligence-layer ports compile and start without error.</p>
 */
@Configuration
@EnableConfigurationProperties(AiProperties.class)
@Import(AgentConfig.class)
public class AiPlatformAutoConfig {
    /*
     * Intentionally empty.
     * Bean registration is delegated to AgentConfig.
     * Property validation is handled by AiProperties binding.
     */
}