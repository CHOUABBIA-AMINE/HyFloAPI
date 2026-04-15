package dz.sh.trc.hyflo.platform.ai.observability;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Micrometer-based metrics recorder for AI agent turns.
 *
 * <h3>Meters registered:</h3>
 * <ul>
 *   <li>{@code hyflo.ai.turn.executed} (Counter) — tags: {@code prompt}, {@code model},
 *       {@code status} ({@code success} | {@code error})</li>
 *   <li>{@code hyflo.ai.turn.latency} (Timer) — tags: {@code prompt}, {@code model}</li>
 *   <li>{@code hyflo.ai.tokens.prompt} (Counter) — tags: {@code prompt}, {@code model}</li>
 *   <li>{@code hyflo.ai.tokens.completion} (Counter) — tags: {@code prompt}, {@code model}</li>
 *   <li>{@code hyflo.ai.tokens.total} (Counter) — tags: {@code prompt}, {@code model}</li>
 *   <li>{@code hyflo.ai.error} (Counter) — tags: {@code prompt}, {@code model},
 *       {@code errorCode}</li>
 * </ul>
 *
 * <h3>MDC keys set during a turn:</h3>
 * <table border="1">
 *   <tr><th>Key</th><th>Value</th></tr>
 *   <tr><td>{@code correlationId}</td><td>{@link AiTraceContext#correlationId()}</td></tr>
 *   <tr><td>{@code sessionId}</td><td>{@link AiTraceContext#sessionId()} or {@code "-"}</td></tr>
 *   <tr><td>{@code userId}</td><td>{@link AiTraceContext#userId()}</td></tr>
 *   <tr><td>{@code promptId}</td><td>{@link AiTraceContext#promptQualified()}</td></tr>
 *   <tr><td>{@code aiModel}</td><td>{@link AiTraceContext#modelName()}</td></tr>
 *   <tr><td>{@code turnNumber}</td><td>{@link AiTraceContext#turnNumber()}</td></tr>
 * </table>
 * <p>MDC is cleared by {@link #endTurn(AiTraceContext, TurnResult)}. Callers
 * must always call {@code endTurn} — prefer try/finally.</p>
 *
 * <h3>Integration with platform audit:</h3>
 * <p>This class only records metrics and MDC. Structured audit log entries
 * are the responsibility of {@link AiAuditLogger}. Callers invoke both:
 * <pre>{@code
 * metricsRecorder.startTurn(ctx);
 * try {
 *     // ... agent execution ...
 *     metricsRecorder.endTurn(ctx, TurnResult.success(promptTokens, completionTokens));
 *     auditLogger.logSuccess(ctx, promptTokens, completionTokens, latencyMs);
 * } catch (Exception e) {
 *     metricsRecorder.endTurn(ctx, TurnResult.error(errorCode));
 *     auditLogger.logFailure(ctx, errorCode, e.getMessage());
 *     throw e;
 * }
 * }</pre>
 */
@Component
public class AiMetricsRecorder {

    private static final Logger log = LoggerFactory.getLogger(AiMetricsRecorder.class);

    // ── MDC key constants ─────────────────────────────────────────────────────
    public static final String MDC_CORRELATION_ID = "correlationId";
    public static final String MDC_SESSION_ID     = "sessionId";
    public static final String MDC_USER_ID        = "userId";
    public static final String MDC_PROMPT_ID      = "promptId";
    public static final String MDC_AI_MODEL       = "aiModel";
    public static final String MDC_TURN_NUMBER    = "turnNumber";

    // ── Meter name constants ──────────────────────────────────────────────────
    private static final String METER_TURN_EXECUTED    = "hyflo.ai.turn.executed";
    private static final String METER_TURN_LATENCY     = "hyflo.ai.turn.latency";
    private static final String METER_TOKENS_PROMPT    = "hyflo.ai.tokens.prompt";
    private static final String METER_TOKENS_COMPLETION= "hyflo.ai.tokens.completion";
    private static final String METER_TOKENS_TOTAL     = "hyflo.ai.tokens.total";
    private static final String METER_ERROR            = "hyflo.ai.error";

    private final MeterRegistry registry;

    public AiMetricsRecorder(MeterRegistry registry) {
        this.registry = Objects.requireNonNull(registry, "MeterRegistry");
    }

    // -------------------------------------------------------------------------
    // Turn lifecycle
    // -------------------------------------------------------------------------

    /**
     * Called at the start of a turn. Populates MDC keys for all downstream
     * log lines in the same thread.
     *
     * @param ctx the trace context for this turn
     */
    public void startTurn(AiTraceContext ctx) {
        Objects.requireNonNull(ctx, "AiTraceContext");

        MDC.put(MDC_CORRELATION_ID, ctx.correlationId());
        MDC.put(MDC_SESSION_ID,     ctx.hasSession() ? ctx.sessionId() : "-");
        MDC.put(MDC_USER_ID,        ctx.userId());
        MDC.put(MDC_PROMPT_ID,      ctx.promptQualified());
        MDC.put(MDC_AI_MODEL,       ctx.modelName());
        MDC.put(MDC_TURN_NUMBER,    String.valueOf(ctx.turnNumber()));

        log.debug("[AiMetricsRecorder] turn started prompt='{}' model='{}' turn={}",
                ctx.promptQualified(), ctx.modelName(), ctx.turnNumber());
    }

    /**
     * Called at the end of a turn (success or failure). Records all meters
     * and clears MDC keys set by {@link #startTurn(AiTraceContext)}.
     *
     * <p>Must always be called, even on exception — use try/finally.</p>
     *
     * @param ctx    the trace context
     * @param result the outcome of the turn
     */
    public void endTurn(AiTraceContext ctx, TurnResult result) {
        Objects.requireNonNull(ctx,    "AiTraceContext");
        Objects.requireNonNull(result, "TurnResult");

        try {
            long latencyMs = Duration.between(ctx.startedAt(), Instant.now()).toMillis();

            String prompt = ctx.promptQualified();
            String model  = ctx.modelName();

            // Turn executed counter
            Counter.builder(METER_TURN_EXECUTED)
                    .tag("prompt", prompt)
                    .tag("model",  model)
                    .tag("status", result.status().name().toLowerCase())
                    .register(registry)
                    .increment();

            // Latency timer
            Timer.builder(METER_TURN_LATENCY)
                    .tag("prompt", prompt)
                    .tag("model",  model)
                    .register(registry)
                    .record(latencyMs, TimeUnit.MILLISECONDS);

            // Token counters (only on success with token data)
            if (result.status() == TurnResult.Status.SUCCESS) {
                if (result.promptTokens() > 0) {
                    Counter.builder(METER_TOKENS_PROMPT)
                            .tag("prompt", prompt).tag("model", model)
                            .register(registry).increment(result.promptTokens());
                }
                if (result.completionTokens() > 0) {
                    Counter.builder(METER_TOKENS_COMPLETION)
                            .tag("prompt", prompt).tag("model", model)
                            .register(registry).increment(result.completionTokens());
                }
                int total = result.promptTokens() + result.completionTokens();
                if (total > 0) {
                    Counter.builder(METER_TOKENS_TOTAL)
                            .tag("prompt", prompt).tag("model", model)
                            .register(registry).increment(total);
                }

                log.info("[AiMetricsRecorder] turn SUCCESS latencyMs={} promptTokens={} "
                        + "completionTokens={} prompt='{}' model='{}'",
                        latencyMs, result.promptTokens(), result.completionTokens(),
                        prompt, model);

            } else {
                // Error counter
                String errorCode = result.errorCode() != null ? result.errorCode() : "UNKNOWN";
                Counter.builder(METER_ERROR)
                        .tag("prompt",    prompt)
                        .tag("model",     model)
                        .tag("errorCode", errorCode)
                        .register(registry)
                        .increment();

                log.warn("[AiMetricsRecorder] turn ERROR latencyMs={} errorCode='{}' "
                        + "prompt='{}' model='{}'",
                        latencyMs, errorCode, prompt, model);
            }

        } finally {
            clearMdc();
        }
    }

    // -------------------------------------------------------------------------
    // MDC helpers
    // -------------------------------------------------------------------------

    /** Removes all MDC keys set by {@link #startTurn(AiTraceContext)}. */
    public void clearMdc() {
        MDC.remove(MDC_CORRELATION_ID);
        MDC.remove(MDC_SESSION_ID);
        MDC.remove(MDC_USER_ID);
        MDC.remove(MDC_PROMPT_ID);
        MDC.remove(MDC_AI_MODEL);
        MDC.remove(MDC_TURN_NUMBER);
    }

    // -------------------------------------------------------------------------
    // TurnResult
    // -------------------------------------------------------------------------

    /**
     * Outcome of a single AI agent turn, passed to
     * {@link AiMetricsRecorder#endTurn(AiTraceContext, TurnResult)}.
     */
    public static final class TurnResult {

        public enum Status { SUCCESS, ERROR }

        private final Status status;
        private final int    promptTokens;
        private final int    completionTokens;
        private final String errorCode;

        private TurnResult(Status status, int promptTokens, int completionTokens, String errorCode) {
            this.status           = status;
            this.promptTokens     = promptTokens;
            this.completionTokens = completionTokens;
            this.errorCode        = errorCode;
        }

        public static TurnResult success(int promptTokens, int completionTokens) {
            return new TurnResult(Status.SUCCESS, promptTokens, completionTokens, null);
        }

        public static TurnResult error(String errorCode) {
            return new TurnResult(Status.ERROR, 0, 0, errorCode);
        }

        public Status status()           { return status; }
        public int    promptTokens()     { return promptTokens; }
        public int    completionTokens() { return completionTokens; }
        public String errorCode()        { return errorCode; }
    }
}