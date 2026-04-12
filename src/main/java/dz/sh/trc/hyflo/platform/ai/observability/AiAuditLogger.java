package dz.sh.trc.hyflo.platform.ai.observability;

import dz.sh.trc.hyflo.domain.audit.service.AuditedService;
import dz.sh.trc.hyflo.domain.audit.service.AuditedService.AuditEventBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Writes structured AI audit log entries through the platform's existing
 * {@link AuditedService}, matching the conventions established by
 * {@link dz.sh.trc.hyflo.platform.audit.aspect.AuditAspect}.
 *
 * <h3>Audit event fields used:</h3>
 * <ul>
 *   <li>{@code entityName}      = {@code "AI_TURN"}</li>
 *   <li>{@code action}          = {@code "EXECUTE"}</li>
 *   <li>{@code module}          = {@code "AI"}</li>
 *   <li>{@code businessProcess} = prompt qualified name (e.g. {@code "flow-anomaly:v1"})</li>
 *   <li>{@code username}        = {@link AiTraceContext#userId()}</li>
 *   <li>{@code sessionId}       = {@link AiTraceContext#sessionId()} or {@code null}</li>
 *   <li>{@code status}          = {@code "SUCCESS"} or {@code "FAILED"} — matching
 *       the existing {@code AuditAspect} convention exactly</li>
 *   <li>{@code duration}        = latency in milliseconds</li>
 *   <li>{@code errorMessage}    = error detail (failure path only)</li>
 *   <li>{@code description}     = human-readable summary</li>
 * </ul>
 *
 * <h3>AI-specific detail (in {@code parameters}):</h3>
 * <p>The {@code parameters} field carries a structured map so audit queries
 * can filter by prompt version, model, or token counts without parsing
 * free-text descriptions:</p>
 * <pre>
 * correlationId | promptTemplateId | promptVersion | modelUsed
 *               | promptTokens     | completionTokens | totalTokens
 * </pre>
 *
 * <h3>Usage pattern — always in a try/finally with AiMetricsRecorder:</h3>
 * <pre>{@code
 * metricsRecorder.startTurn(ctx);
 * try {
 *     AgentResponse resp = agentExecutor.execute(...);
 *     long latencyMs = Duration.between(ctx.startedAt(), Instant.now()).toMillis();
 *     metricsRecorder.endTurn(ctx, TurnResult.success(resp.promptTokens(), resp.completionTokens()));
 *     auditLogger.logSuccess(ctx, resp.promptTokens(), resp.completionTokens(), latencyMs);
 *     insightRepository.save(AiInsight.from(ctx, resp, latencyMs));
 * } catch (Exception e) {
 *     metricsRecorder.endTurn(ctx, TurnResult.error(errorCode(e)));
 *     auditLogger.logFailure(ctx, errorCode(e), e.getMessage(), latencyMs);
 *     throw e;
 * }
 * }</pre>
 */
@Component
public class AiAuditLogger {

    private static final Logger log = LoggerFactory.getLogger(AiAuditLogger.class);

    private static final String ENTITY_NAME = "AI_TURN";
    private static final String ACTION      = "EXECUTE";
    private static final String MODULE      = "AI";

    private final AuditedService auditedService;

    public AiAuditLogger(AuditedService auditedService) {
        this.auditedService = Objects.requireNonNull(auditedService, "AuditedService");
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Logs a successful AI turn to the platform audit trail.
     *
     * @param ctx              the trace context for this turn
     * @param promptTokens     tokens consumed by the prompt
     * @param completionTokens tokens in the model's response
     * @param latencyMs        wall-clock latency from request to response
     */
    public void logSuccess(AiTraceContext ctx,
                           int promptTokens,
                           int completionTokens,
                           long latencyMs) {
        Objects.requireNonNull(ctx, "AiTraceContext");

        try {
            AuditEventBuilder event = buildBase(ctx, latencyMs)
                    .status("SUCCESS")
                    .description(buildDescription(ctx, "SUCCESS", null))
                    .parameters(buildParameters(
                            ctx, promptTokens, completionTokens, null));

            auditedService.logAuditEvent(event);

            log.debug("[AiAuditLogger] logged SUCCESS correlationId='{}' prompt='{}' "
                    + "latencyMs={} tokens={}+{}",
                    ctx.correlationId(), ctx.promptQualified(),
                    latencyMs, promptTokens, completionTokens);

        } catch (Exception e) {
            // Audit logging must never break the caller's success path
            log.error("[AiAuditLogger] failed to write SUCCESS audit entry "
                    + "correlationId='{}': {}", ctx.correlationId(), e.getMessage(), e);
        }
    }

    /**
     * Logs a failed AI turn to the platform audit trail.
     *
     * @param ctx        the trace context for this turn
     * @param errorCode  the error classification code (e.g. {@code "ISOLATION_VIOLATION"})
     * @param errorDetail the exception message or diagnostic detail
     * @param latencyMs  wall-clock latency until failure
     */
    public void logFailure(AiTraceContext ctx,
                           String errorCode,
                           String errorDetail,
                           long latencyMs) {
        Objects.requireNonNull(ctx, "AiTraceContext");

        try {
            AuditEventBuilder event = buildBase(ctx, latencyMs)
                    .status("FAILED")
                    .errorMessage(truncate(errorDetail, 500))
                    .description(buildDescription(ctx, "FAILED", errorCode))
                    .parameters(buildParameters(ctx, 0, 0, errorCode));

            auditedService.logAuditEvent(event);

            log.debug("[AiAuditLogger] logged FAILED correlationId='{}' prompt='{}' "
                    + "errorCode='{}' latencyMs={}",
                    ctx.correlationId(), ctx.promptQualified(), errorCode, latencyMs);

        } catch (Exception e) {
            log.error("[AiAuditLogger] failed to write FAILURE audit entry "
                    + "correlationId='{}': {}", ctx.correlationId(), e.getMessage(), e);
        }
    }

    // -------------------------------------------------------------------------
    // Builder helpers
    // -------------------------------------------------------------------------

    private AuditEventBuilder buildBase(AiTraceContext ctx, long latencyMs) {
        return AuditEventBuilder.create()
                .entityName(ENTITY_NAME)
                .action(ACTION)
                .module(MODULE)
                .businessProcess(ctx.promptQualified())
                .username(ctx.userId())
                .sessionId(ctx.hasSession() ? ctx.sessionId() : null)
                .duration(latencyMs);
    }

    private java.util.List<Object> buildParameters(AiTraceContext ctx,
                                                    int promptTokens,
                                                    int completionTokens,
                                                    String errorCode) {
        // Structured as a list of "key=value" strings — matches the existing
        // AuditAspect convention of Arrays.asList(joinPoint.getArgs()).
        java.util.List<String> params = new java.util.ArrayList<>();
        params.add("correlationId="    + ctx.correlationId());
        params.add("promptTemplateId=" + ctx.promptTemplateId());
        params.add("promptVersion="    + ctx.promptVersion());
        params.add("modelUsed="        + ctx.modelName());
        params.add("promptTokens="     + promptTokens);
        params.add("completionTokens=" + completionTokens);
        params.add("totalTokens="      + (promptTokens + completionTokens));
        if (errorCode != null) params.add("errorCode=" + errorCode);
        return java.util.Collections.unmodifiableList(params);
    }

    private String buildDescription(AiTraceContext ctx, String status, String errorCode) {
        StringBuilder sb = new StringBuilder();
        sb.append(status.equals("SUCCESS") ? "AI turn executed" : "AI turn failed");
        sb.append(" — prompt: ").append(ctx.promptQualified());
        sb.append(", model: ").append(ctx.modelName());
        sb.append(", turn: ").append(ctx.turnNumber());
        if (ctx.hasSession()) sb.append(", session: ").append(ctx.sessionId());
        if (errorCode != null) sb.append(", error: ").append(errorCode);
        return sb.toString();
    }

    private static String truncate(String s, int max) {
        if (s == null) return null;
        return s.length() <= max ? s : s.substring(0, max) + "…";
    }
}