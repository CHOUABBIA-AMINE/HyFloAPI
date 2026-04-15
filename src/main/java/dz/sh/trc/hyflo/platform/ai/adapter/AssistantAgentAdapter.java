package dz.sh.trc.hyflo.platform.ai.adapter;

import dz.sh.trc.hyflo.intelligence.assistant.dto.AssistantRequestDTO;
import dz.sh.trc.hyflo.intelligence.assistant.dto.AssistantResponseDTO;
import dz.sh.trc.hyflo.intelligence.assistant.port.AssistantAgentPort;
import dz.sh.trc.hyflo.platform.ai.agent.AgentContext;
import dz.sh.trc.hyflo.platform.ai.agent.AgentExecutor;
import dz.sh.trc.hyflo.platform.ai.agent.AgentRequest;
import dz.sh.trc.hyflo.platform.ai.agent.AgentResponse;
import dz.sh.trc.hyflo.platform.ai.agent.strategy.AgentStrategy.AgentExecutionMode;
import dz.sh.trc.hyflo.platform.ai.memory.SessionIsolationEnforcer;
import dz.sh.trc.hyflo.platform.ai.memory.SessionRegistry;
import dz.sh.trc.hyflo.platform.ai.prompt.PromptBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * Platform adapter implementing {@link AssistantPort}.
 *
 * <p>Unlike the analysis adapters, the assistant is conversational:
 * it carries a {@code sessionId}, passes it through
 * {@link SessionIsolationEnforcer} before execution, and uses
 * {@link AgentExecutionMode#CONVERSATIONAL} so the strategy can
 * inject memory history into the LLM context.</p>
 *
 * <h3>Session lifecycle:</h3>
 * <p>The adapter does NOT create sessions — that is the use-case's
 * responsibility via {@link SessionRegistry}. The adapter only
 * enforces isolation and extends the session TTL on success.</p>
 */
@Component
@ConditionalOnProperty(name = "hyflo.ai.enabled", havingValue = "true")
public class AssistantAgentAdapter implements AssistantAgentPort {

    private static final Logger log = LoggerFactory.getLogger(AssistantAgentAdapter.class);

    private final AgentExecutor            agentExecutor;
    private final SessionIsolationEnforcer isolationEnforcer;
    private final SessionRegistry          sessionRegistry;

    public AssistantAgentAdapter(AgentExecutor agentExecutor,
                                  SessionIsolationEnforcer isolationEnforcer,
                                  SessionRegistry sessionRegistry) {
        this.agentExecutor     = Objects.requireNonNull(agentExecutor,     "AgentExecutor");
        this.isolationEnforcer = Objects.requireNonNull(isolationEnforcer, "SessionIsolationEnforcer");
        this.sessionRegistry   = Objects.requireNonNull(sessionRegistry,   "SessionRegistry");
    }

    @Override
    public AssistantResponseDTO chat(AssistantRequestDTO message) {
        Objects.requireNonNull(message, "AssistantMessageDTO");

        // Enforce session isolation BEFORE touching any memory or executor.
        isolationEnforcer.enforce(
                message.sessionId(),
                message.userId(),
                message.correlationId());

        String prompt = PromptBuilder.create()
                .system("""
                        You are the HyFlo operational assistant — an expert in hydrocarbon
                        pipeline operations, safety, and monitoring. Answer clearly and
                        concisely. If asked about data you cannot see, say so.
                        """)
                .section("userId",       message.userId())
                .section("userQuestion", message.content())
                .instruction("Answer the user's question in the context of HyFlo operations.")
                .build();

        AgentContext context = AgentContext.builder()
                .sessionId(message.sessionId())
                .correlationId(message.correlationId())
                .domainContext(Map.of("domain", "ASSISTANT", "userId", message.userId()))
                .build();

        AgentRequest agentRequest = AgentRequest.builder(prompt, context)
                .temperature(0.7)
                .build();

        AgentResponse response = agentExecutor.execute(agentRequest, AgentExecutionMode.CONVERSATIONAL);

        // Extend TTL after every successful turn.
        sessionRegistry.extendSession(message.sessionId());

        log.debug("[AssistantAgentAdapter] chat completed sessionId={} correlationId={}",
                message.sessionId(), message.correlationId());

        return new AssistantResponseDTO(
                message.sessionId(),
                response.getContent(),
                response.getProvider(),
                response.getPromptTokens(),
                response.getCompletionTokens(),
                response.isFallback(),
                response.getRespondedAt(),
                response.getCorrelationId()
        );
    }
}