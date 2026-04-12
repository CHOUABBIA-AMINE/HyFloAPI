package dz.sh.trc.hyflo.platform.ai.adapter;

import dz.sh.trc.hyflo.intelligence.crisis.dto.CrisisAssessmentRequestDTO;
import dz.sh.trc.hyflo.intelligence.crisis.dto.CrisisAssessmentResultDTO;
import dz.sh.trc.hyflo.intelligence.crisis.dto.CrisisRecommendationDTO;
import dz.sh.trc.hyflo.intelligence.crisis.port.CrisisAssessmentPort;
import dz.sh.trc.hyflo.platform.ai.agent.AgentContext;
import dz.sh.trc.hyflo.platform.ai.agent.AgentExecutor;
import dz.sh.trc.hyflo.platform.ai.agent.AgentRequest;
import dz.sh.trc.hyflo.platform.ai.agent.AgentResponse;
import dz.sh.trc.hyflo.platform.ai.agent.strategy.AgentStrategy.AgentExecutionMode;
import dz.sh.trc.hyflo.platform.ai.prompt.PromptBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Platform adapter implementing {@link CrisisAssessmentPort}.
 *
 * <p>Translates crisis domain DTOs into {@link AgentRequest} and maps
 * the {@link AgentResponse} back to crisis domain DTOs. No business logic.</p>
 *
 * <h3>Temperature:</h3>
 * <p>Crisis calls use temperature 0.1 — the most deterministic setting —
 * because severity scores must be reproducible under re-analysis.</p>
 */
@Component
@ConditionalOnProperty(name = "hyflo.ai.enabled", havingValue = "true")
public class CrisisAssessmentAgentAdapter implements CrisisAssessmentPort {

    private static final Logger log = LoggerFactory.getLogger(CrisisAssessmentAgentAdapter.class);

    private final AgentExecutor agentExecutor;

    public CrisisAssessmentAgentAdapter(AgentExecutor agentExecutor) {
        this.agentExecutor = Objects.requireNonNull(agentExecutor, "AgentExecutor");
    }

    @Override
    public CrisisAssessmentResultDTO assess(CrisisAssessmentRequestDTO request) {
        Objects.requireNonNull(request, "CrisisAssessmentRequestDTO");

        String prompt = PromptBuilder.create()
                .system("""
                        You are HyFlo's crisis assessment AI. Evaluate the crisis event
                        and respond with a JSON object:
                        severityScore (0-100 int), severityLevel (LOW|MEDIUM|HIGH|CRITICAL),
                        summary (string), impactAnalysis (string), immediateRisk (bool).
                        Raw JSON only.
                        """)
                .section("eventType",   request.eventType())
                .section("location",    request.location())
                .section("description", request.description())
                .section("reportedAt",  request.reportedAt() != null
                                            ? request.reportedAt().toString() : "unknown")
                .instruction("Assess this crisis event.")
                .build();

        AgentContext context = AgentContext.builder()
                .correlationId(request.correlationId())
                .domainContext(Map.of("domain", "CRISIS"))
                .build();

        AgentRequest agentRequest = AgentRequest.builder(prompt, context)
                .temperature(0.1)
                .build();

        AgentResponse response = agentExecutor.execute(agentRequest, AgentExecutionMode.DIRECT);

        SimpleJsonExtractor j = new SimpleJsonExtractor(response.getContent());
        return new CrisisAssessmentResultDTO(
                request.eventType(),
                request.location(),
                j.integer("severityScore",  50),
                j.string("severityLevel",   "MEDIUM"),
                j.string("summary",         response.getContent()),
                j.string("impactAnalysis",  ""),
                j.bool("immediateRisk",     false),
                response.getProvider(),
                response.getPromptTokens(),
                response.getCompletionTokens(),
                response.getRespondedAt(),
                response.getCorrelationId()
        );
    }

    @Override
    public CrisisRecommendationDTO recommend(CrisisAssessmentRequestDTO request) {
        Objects.requireNonNull(request, "CrisisAssessmentRequestDTO");

        String prompt = PromptBuilder.create()
                .system("""
                        You are HyFlo's crisis response planner. Given the crisis context,
                        return a JSON object with:
                        priorityLevel (IMMEDIATE|HIGH|STANDARD),
                        recommendations (array of strings),
                        estimatedResolutionHours (int),
                        summary (string).
                        Raw JSON only.
                        """)
                .section("eventType",   request.eventType())
                .section("location",    request.location())
                .section("description", request.description())
                .instruction("Generate operational recommendations.")
                .build();

        AgentContext context = AgentContext.builder()
                .correlationId(request.correlationId())
                .domainContext(Map.of("domain", "CRISIS"))
                .build();

        AgentRequest agentRequest = AgentRequest.builder(prompt, context)
                .temperature(0.2)
                .build();

        AgentResponse response = agentExecutor.execute(agentRequest, AgentExecutionMode.DIRECT);

        SimpleJsonExtractor j = new SimpleJsonExtractor(response.getContent());
        return new CrisisRecommendationDTO(
                request.eventType(),
                request.location(),
                j.string("priorityLevel",             "STANDARD"),
                j.stringList("recommendations",       List.of(response.getContent())),
                j.integer("estimatedResolutionHours", 4),
                j.string("summary",                   response.getContent()),
                response.getProvider(),
                response.getRespondedAt(),
                response.getCorrelationId()
        );
    }
}