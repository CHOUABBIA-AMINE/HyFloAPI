package dz.sh.trc.hyflo.platform.ai.adapter;

import dz.sh.trc.hyflo.intelligence.network.dto.NetworkInsightRequestDTO;
import dz.sh.trc.hyflo.intelligence.network.dto.NetworkInsightResultDTO;
import dz.sh.trc.hyflo.intelligence.network.dto.NetworkOptimisationDTO;
import dz.sh.trc.hyflo.intelligence.network.port.NetworkInsightPort;
import dz.sh.trc.hyflo.platform.ai.agent.AgentContext;
import dz.sh.trc.hyflo.platform.ai.agent.AgentExecutor;
import dz.sh.trc.hyflo.platform.ai.agent.AgentRequest;
import dz.sh.trc.hyflo.platform.ai.agent.AgentResponse;
import dz.sh.trc.hyflo.platform.ai.agent.strategy.AgentStrategy.AgentExecutionMode;
import dz.sh.trc.hyflo.platform.ai.prompt.PromptBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Platform adapter implementing {@link NetworkInsightPort}.
 * Translates network domain DTOs → {@link AgentRequest} → domain result DTOs.
 */
@Component
@ConditionalOnProperty(name = "hyflo.ai.enabled", havingValue = "true")
public class NetworkInsightAgentAdapter implements NetworkInsightPort {

    private final AgentExecutor agentExecutor;

    public NetworkInsightAgentAdapter(AgentExecutor agentExecutor) {
        this.agentExecutor = Objects.requireNonNull(agentExecutor, "AgentExecutor");
    }

    @Override
    public NetworkInsightResultDTO analyse(NetworkInsightRequestDTO request) {
        Objects.requireNonNull(request, "NetworkInsightRequestDTO");

        String prompt = PromptBuilder.create()
                .system("""
                        You are HyFlo's pipeline network analyst. Analyse the provided
                        network segment state and return a JSON object:
                        narrative (string), operationalStatus (NOMINAL|DEGRADED|CRITICAL),
                        keyIndicators (array of strings), alertCount (int).
                        Raw JSON only.
                        """)
                .section("segmentId",      request.segmentId())
                .section("topologySummary", request.topologySummary())
                .section("activeAlerts",   request.activeAlerts() != null
                                               ? request.activeAlerts().toString() : "none")
                .instruction("Analyse the network segment state.")
                .build();

        AgentContext context = AgentContext.builder()
                .correlationId(request.correlationId())
                .domainContext(Map.of("domain", "NETWORK", "segmentId", request.segmentId()))
                .build();

        AgentResponse response = agentExecutor.execute(
                AgentRequest.builder(prompt, context).temperature(0.2).build(),
                AgentExecutionMode.DIRECT);

        SimpleJsonExtractor j = new SimpleJsonExtractor(response.getContent());
        return new NetworkInsightResultDTO(
                request.segmentId(),
                j.string("narrative",         response.getContent()),
                j.string("operationalStatus", "NOMINAL"),
                j.stringList("keyIndicators", List.of()),
                j.integer("alertCount",        0),
                response.getProvider(),
                response.getPromptTokens(),
                response.getCompletionTokens(),
                response.getRespondedAt(),
                response.getCorrelationId()
        );
    }

    @Override
    public NetworkOptimisationDTO optimise(NetworkInsightRequestDTO request) {
        Objects.requireNonNull(request, "NetworkInsightRequestDTO");

        String prompt = PromptBuilder.create()
                .system("""
                        You are HyFlo's network optimisation advisor. Based on the
                        segment state, return a JSON object:
                        summary (string), optimisations (array of objects each with
                        action (string), priority (HIGH|MEDIUM|LOW), rationale (string)),
                        estimatedEfficiencyGainPct (double).
                        Raw JSON only.
                        """)
                .section("segmentId",      request.segmentId())
                .section("topologySummary", request.topologySummary())
                .instruction("Suggest optimisations for this network segment.")
                .build();

        AgentContext context = AgentContext.builder()
                .correlationId(request.correlationId())
                .domainContext(Map.of("domain", "NETWORK"))
                .build();

        AgentResponse response = agentExecutor.execute(
                AgentRequest.builder(prompt, context).temperature(0.3).build(),
                AgentExecutionMode.DIRECT);

        SimpleJsonExtractor j = new SimpleJsonExtractor(response.getContent());
        return new NetworkOptimisationDTO(
                request.segmentId(),
                j.string("summary",                       response.getContent()),
                List.of(),    // optimisation array deferred to Step 4.3
                j.decimal("estimatedEfficiencyGainPct",   0.0),
                response.getProvider(),
                response.getRespondedAt(),
                response.getCorrelationId()
        );
    }
}