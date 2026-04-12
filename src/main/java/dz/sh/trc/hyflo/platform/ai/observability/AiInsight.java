package dz.sh.trc.hyflo.platform.ai.observability;

import dz.sh.trc.hyflo.platform.kernel.GenericModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * JPA entity that persists the full telemetry record of a single AI agent turn.
 *
 * <p>Extends {@link GenericModel} to inherit the standard {@code F_00} primary key
 * and {@code IDENTITY} generation strategy, consistent with all other HyFlo entities.</p>
 *
 * <h3>Purpose:</h3>
 * <p>{@code AiInsight} records are the governance trail for every AI decision
 * the platform emits. Operators and compliance reviewers can query the table to
 * answer: which prompt version produced this output? How many tokens did it consume?
 * How long did it take? Did it fail, and why?</p>
 *
 * <h3>Column naming convention ({@code F_NN}):</h3>
 * <p>Follows the {@code GenericModel} / project-wide convention. Every column
 * is prefixed {@code F_} with a two-digit sequence number matching declaration order.</p>
 *
 * <h3>Append-only:</h3>
 * <p>Insight records are never updated or deleted after creation. They are
 * written once by {@link AiInsightRepository#save(Object)} and then queried
 * read-only. Do not add update or delete methods to the repository.</p>
 *
 * <h3>Table:</h3>
 * <pre>{@code T_AI_INSIGHT}</pre>
 */
@Entity
@Table(
    name = "T_AI_INSIGHT",
    indexes = {
        @Index(name = "idx_ai_insight_session",   columnList = "F_02"),
        @Index(name = "idx_ai_insight_user",      columnList = "F_03"),
        @Index(name = "idx_ai_insight_prompt",    columnList = "F_04"),
        @Index(name = "idx_ai_insight_started_at",columnList = "F_09"),
        @Index(name = "idx_ai_insight_status",    columnList = "F_11")
    }
)
@Getter
@Setter
@NoArgsConstructor
public class AiInsight extends GenericModel {

    /**
     * Outcome of the AI turn.
     */
    public enum TurnStatus { SUCCESS, ERROR }

    // ── Trace identity ────────────────────────────────────────────────────────

    /** F_01 — correlation ID from the request ({@code X-Correlation-ID} header). */
    @Column(name = "F_01", nullable = false, length = 128)
    private String correlationId;

    /** F_02 — session ID; null for stateless (flow scoring) turns. */
    @Column(name = "F_02", length = 128)
    private String sessionId;

    /** F_03 — authenticated user ID who triggered this turn. */
    @Column(name = "F_03", nullable = false, length = 256)
    private String userId;

    // ── Prompt ────────────────────────────────────────────────────────────────

    /** F_04 — prompt template ID (e.g. {@code "flow-anomaly"}). */
    @Column(name = "F_04", nullable = false, length = 128)
    private String promptTemplateId;

    /** F_05 — prompt version number (e.g. {@code 1}). */
    @Column(name = "F_05", nullable = false)
    private Integer promptVersion;

    // ── Model ─────────────────────────────────────────────────────────────────

    /** F_06 — model name as reported by the LLM client (e.g. {@code "gpt-4o"}). */
    @Column(name = "F_06", nullable = false, length = 128)
    private String modelUsed;

    // ── Token counts ──────────────────────────────────────────────────────────

    /** F_07 — tokens consumed by the rendered prompt. */
    @Column(name = "F_07")
    private Integer promptTokens;

    /** F_08 — tokens in the model's completion response. */
    @Column(name = "F_08")
    private Integer completionTokens;

    // ── Timing ────────────────────────────────────────────────────────────────

    /** F_09 — wall-clock instant when the turn was initiated. */
    @Column(name = "F_09", nullable = false)
    private Instant startedAt;

    /** F_10 — latency in milliseconds from request to response (or failure). */
    @Column(name = "F_10")
    private Long latencyMs;

    // ── Outcome ───────────────────────────────────────────────────────────────

    /** F_11 — turn outcome: {@link TurnStatus#SUCCESS} or {@link TurnStatus#ERROR}. */
    @Enumerated(EnumType.STRING)
    @Column(name = "F_11", nullable = false, length = 16)
    private TurnStatus status;

    /** F_12 — error classification code; null on success. */
    @Column(name = "F_12", length = 128)
    private String errorCode;

    /** F_13 — error detail message (truncated to 1000 chars); null on success. */
    @Column(name = "F_13", length = 1000)
    private String errorDetail;

    // ── Turn position ─────────────────────────────────────────────────────────

    /** F_14 — 1-based turn number within the session (always 1 for stateless turns). */
    @Column(name = "F_14")
    private Integer turnNumber;

    // -------------------------------------------------------------------------
    // Factory method
    // -------------------------------------------------------------------------

    /**
     * Constructs an {@code AiInsight} for a <strong>successful</strong> turn.
     *
     * @param ctx              the trace context
     * @param promptTokens     tokens consumed by the rendered prompt
     * @param completionTokens tokens in the model completion
     * @param latencyMs        wall-clock latency in milliseconds
     * @return a ready-to-persist insight record
     */
    public static AiInsight success(AiTraceContext ctx,
                                    int promptTokens,
                                    int completionTokens,
                                    long latencyMs) {
        AiInsight i = fromContext(ctx, latencyMs);
        i.promptTokens     = promptTokens;
        i.completionTokens = completionTokens;
        i.status           = TurnStatus.SUCCESS;
        return i;
    }

    /**
     * Constructs an {@code AiInsight} for a <strong>failed</strong> turn.
     *
     * @param ctx         the trace context
     * @param latencyMs   wall-clock latency until failure
     * @param errorCode   error classification code
     * @param errorDetail exception message or diagnostic detail
     * @return a ready-to-persist insight record
     */
    public static AiInsight error(AiTraceContext ctx,
                                   long latencyMs,
                                   String errorCode,
                                   String errorDetail) {
        AiInsight i = fromContext(ctx, latencyMs);
        i.status      = TurnStatus.ERROR;
        i.errorCode   = errorCode;
        i.errorDetail = errorDetail != null && errorDetail.length() > 1000
                ? errorDetail.substring(0, 1000) + "…"
                : errorDetail;
        return i;
    }

    private static AiInsight fromContext(AiTraceContext ctx, long latencyMs) {
        AiInsight i        = new AiInsight();
        i.correlationId    = ctx.correlationId();
        i.sessionId        = ctx.hasSession() ? ctx.sessionId() : null;
        i.userId           = ctx.userId();
        i.promptTemplateId = ctx.promptTemplateId();
        i.promptVersion    = ctx.promptVersion();
        i.modelUsed        = ctx.modelName();
        i.startedAt        = ctx.startedAt();
        i.latencyMs        = latencyMs;
        i.turnNumber       = ctx.turnNumber();
        return i;
    }

    // ── Convenience ──────────────────────────────────────────────────────────

    /** Returns the total token count (prompt + completion), or 0 if not recorded. */
    public int totalTokens() {
        int p = promptTokens     != null ? promptTokens     : 0;
        int c = completionTokens != null ? completionTokens : 0;
        return p + c;
    }

    /** Returns the prompt qualified name, e.g. {@code "flow-anomaly:v1"}. */
    public String promptQualified() {
        return promptTemplateId + ":v" + promptVersion;
    }

    @Override
    public String toString() {
        return "AiInsight{id=" + getId()
                + ", correlationId='" + correlationId + '\''
                + ", prompt='" + promptQualified() + '\''
                + ", model='" + modelUsed + '\''
                + ", status=" + status
                + ", latencyMs=" + latencyMs
                + '}';
    }
}