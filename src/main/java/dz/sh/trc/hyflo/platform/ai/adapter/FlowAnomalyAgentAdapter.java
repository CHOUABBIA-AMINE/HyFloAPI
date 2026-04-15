package dz.sh.trc.hyflo.platform.ai.adapter;

import dz.sh.trc.hyflo.intelligence.flow.dto.FlowAnalysisRequestDTO;
import dz.sh.trc.hyflo.intelligence.flow.dto.FlowAnalysisResultDTO;
import dz.sh.trc.hyflo.intelligence.flow.dto.FlowAnomalyReportDTO;
import dz.sh.trc.hyflo.intelligence.flow.dto.FlowForecastDTO;
import dz.sh.trc.hyflo.intelligence.flow.port.FlowAnomalyAgentPort;
import dz.sh.trc.hyflo.platform.ai.agent.AgentContext;
import dz.sh.trc.hyflo.platform.ai.agent.AgentExecutionException;
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
import java.util.stream.Collectors;

/**
 * Platform adapter that implements {@link FlowAnomalyAgentPort} by
 * delegating to {@link AgentExecutor}.
 *
 * <p>This is the seam between the flow intelligence domain and the
 * AI platform runtime. It is the only class in the codebase that
 * knows both the flow domain DTOs and the platform AgentRequest/Response
 * shape. No business logic lives here — only translation.</p>
 *
 * <h3>Activation:</h3>
 * <p>Active when {@code hyflo.ai.enabled=true}. When false,
 * {@link NoOpFlowAnomalyAgentAdapter} is injected instead.</p>
 *
 * <h3>Prompt strategy (Step 5.1):</h3>
 * <p>Prompts are assembled inline by {@link PromptBuilder}.
 * When Step 4.3 (PromptRenderer) is merged, the build methods
 * will be replaced by renderer calls — no other change to this adapter.</p>
 */
@Component
@ConditionalOnProperty(name = "hyflo.ai.enabled", havingValue = "true")
public class FlowAnomalyAgentAdapter implements FlowAnomalyAgentPort {

    private static final Logger log = LoggerFactory.getLogger(FlowAnomalyAgentAdapter.class);

    private final AgentExecutor agentExecutor;

    public FlowAnomalyAgentAdapter(AgentExecutor agentExecutor) {
        this.agentExecutor = Objects.requireNonNull(agentExecutor, "AgentExecutor");
    }

    // -------------------------------------------------------------------------
    // FlowAnomalyAgentPort
    // -------------------------------------------------------------------------

    @Override
    public FlowAnalysisResultDTO analyse(FlowAnalysisRequestDTO request) {
        Objects.requireNonNull(request, "FlowAnalysisRequestDTO");

        String prompt = PromptBuilder.create()
                .system("""
                        You are HyFlo's pipeline flow analyst. Analyse the provided sensor
                        readings and return a JSON object with exactly these fields:
                        narrative (string), healthScore (0-100 int), anomalyDetected (bool),
                        severityLevel (NONE|LOW|MEDIUM|HIGH|CRITICAL).
                        Respond with raw JSON only — no markdown fences.
                        """)
                .sections(baseFlowContext(request))
                .section("readings", formatReadings(request))
                .instruction("Produce the JSON analysis now.")
                .build();

        AgentResponse response = execute(prompt, request.correlationId(), AgentExecutionMode.DIRECT);

        return mapToAnalysisResult(request, response);
    }

    @Override
    public FlowAnomalyReportDTO detectAnomalies(FlowAnalysisRequestDTO request) {
        Objects.requireNonNull(request, "FlowAnalysisRequestDTO");

        String prompt = PromptBuilder.create()
                .system("""
                        You are HyFlo's anomaly detection engine. Identify anomalies in
                        the provided readings. Return a JSON object with:
                        anomalyCount (int), summary (string), anomalies (array of objects
                        each with sensorId, detectedAt (ISO-8601), anomalyType
                        (PRESSURE_DROP|FLOW_SURGE|TEMPERATURE_SPIKE|FLATLINE|OSCILLATION|UNKNOWN),
                        severity (LOW|MEDIUM|HIGH|CRITICAL), description, recommendedAction).
                        Raw JSON only.
                        """)
                .sections(baseFlowContext(request))
                .section("readings", formatReadings(request))
                .instruction("List all anomalies detected.")
                .build();

        AgentResponse response = execute(prompt, request.correlationId(), AgentExecutionMode.DIRECT);

        return mapToAnomalyReport(request, response);
    }

    @Override
    public FlowForecastDTO forecast(FlowAnalysisRequestDTO request) {
        Objects.requireNonNull(request, "FlowAnalysisRequestDTO");

        String prompt = PromptBuilder.create()
                .system("""
                        You are HyFlo's flow forecasting agent. Based on the historical
                        readings, forecast the next 6 hours. Return a JSON object with:
                        forecastHorizonHours (int, 6), summary (string), confidence (0.0-1.0 double),
                        dataPoints (array of { forecastAt (ISO-8601), predictedFlowRateM3h (double),
                        lowerBound (double), upperBound (double) }).
                        Raw JSON only.
                        """)
                .sections(baseFlowContext(request))
                .section("historicalReadings", formatReadings(request))
                .instruction("Generate the 6-hour forecast.")
                .build();

        AgentResponse response = execute(prompt, request.correlationId(), AgentExecutionMode.DIRECT);

        return mapToForecast(request, response);
    }

    // -------------------------------------------------------------------------
    // Shared execution
    // -------------------------------------------------------------------------

    private AgentResponse execute(String prompt, String correlationId, AgentExecutionMode mode) {
        AgentContext context = AgentContext.builder()
                .correlationId(correlationId)
                .domainContext(Map.of("domain", "FLOW"))
                .build();

        AgentRequest agentRequest = AgentRequest.builder(prompt, context)
                .temperature(0.2)
                .build();

        try {
            return agentExecutor.execute(agentRequest, mode);
        } catch (AgentExecutionException e) {
            log.error("[FlowAnomalyAgentAdapter] execution failed correlationId={}: {}",
                    correlationId, e.getMessage());
            throw e;
        }
    }

    // -------------------------------------------------------------------------
    // Context helpers
    // -------------------------------------------------------------------------

    private Map<String, String> baseFlowContext(FlowAnalysisRequestDTO r) {
        return Map.of(
                "pipelineId",  r.pipelineId(),
                "segmentRef",  r.segmentRef(),
                "windowStart", r.windowStart().toString(),
                "windowEnd",   r.windowEnd().toString(),
                "operatorNote", r.operatorNote() != null ? r.operatorNote() : "(none)"
        );
    }

    private String formatReadings(FlowAnalysisRequestDTO r) {
        if (r.readings() == null || r.readings().isEmpty()) return "(no readings)";
        return r.readings().stream()
                .map(rd -> String.format("[%s] sensor=%s flow=%.2f m³/h pressure=%.2f bar temp=%.1f°C",
                        rd.measuredAt(), rd.sensorId(),
                        rd.flowRateM3h(), rd.pressureBar(), rd.temperatureCelsius()))
                .collect(Collectors.joining("\n"));
    }

    // -------------------------------------------------------------------------
    // Response → domain DTO mapping
    // -------------------------------------------------------------------------

    private FlowAnalysisResultDTO mapToAnalysisResult(FlowAnalysisRequestDTO req,
                                                       AgentResponse res) {
        // Structured parse with graceful fallback on malformed JSON.
        // Step 4.3 StructuredOutputConverter will replace this.
        SimpleJsonExtractor j = new SimpleJsonExtractor(res.getContent());
        return new FlowAnalysisResultDTO(
                req.pipelineId(),
                req.segmentRef(),
                j.string("narrative",      res.getContent()),
                j.integer("healthScore",   75),
                j.bool("anomalyDetected",  false),
                j.string("severityLevel",  "NONE"),
                res.getProvider(),
                res.getPromptTokens(),
                res.getCompletionTokens(),
                res.getRespondedAt(),
                res.getCorrelationId()
        );
    }

    private FlowAnomalyReportDTO mapToAnomalyReport(FlowAnalysisRequestDTO req,
                                                      AgentResponse res) {
        SimpleJsonExtractor j = new SimpleJsonExtractor(res.getContent());
        return new FlowAnomalyReportDTO(
                req.pipelineId(),
                req.segmentRef(),
                j.integer("anomalyCount", 0),
                List.of(),   // full anomaly array parsing deferred to Step 4.3 structured output
                j.string("summary", res.getContent()),
                res.getProvider(),
                res.getRespondedAt(),
                res.getCorrelationId()
        );
    }

    private FlowForecastDTO mapToForecast(FlowAnalysisRequestDTO req,
                                           AgentResponse res) {
        SimpleJsonExtractor j = new SimpleJsonExtractor(res.getContent());
        return new FlowForecastDTO(
                req.pipelineId(),
                req.segmentRef(),
                6,
                j.string("summary",    res.getContent()),
                j.decimal("confidence", 0.7),
                List.of(),   // data points array deferred to Step 4.3
                res.getProvider(),
                res.getRespondedAt(),
                res.getCorrelationId()
        );
    }
}