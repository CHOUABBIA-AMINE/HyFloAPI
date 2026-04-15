package dz.sh.trc.hyflo.platform.ai.observability;

import java.time.Instant;
import java.util.Objects;

/**
 * Immutable trace envelope for a single AI agent turn.
 *
 * <p>One {@code AiTraceContext} is created at the start of each agent execution
 * and passed through every observability component ({@link AiMetricsRecorder},
 * {@link AiAuditLogger}, {@link AiInsight} persistence). All fields are set
 * at construction time; the context is never mutated after creation.</p>
 *
 * <h3>Lifecycle:</h3>
 * <ol>
 *   <li>Created by the AgentExecutor at the start of a turn, after
 *       {@link dz.sh.trc.hyflo.platform.ai.memory.SessionIsolationEnforcer#enforce}
 *       has passed.</li>
 *   <li>Passed into {@link AiMetricsRecorder#startTurn(AiTraceContext)} which
 *       populates MDC keys for the duration of the turn.</li>
 *   <li>Passed into {@link AiAuditLogger} and
 *       {@link AiInsightRepository} at turn completion.</li>
 * </ol>
 *
 * <h3>Correlation ID:</h3>
 * <p>The {@link #correlationId} is set by the HTTP filter layer from the
 * incoming {@code X-Correlation-ID} header (or generated as a UUID if absent).
 * It is the primary key for cross-system log correlation.</p>
 */
public final class AiTraceContext {

    // ── Identity ─────────────────────────────────────────────────────────────
    private final String  correlationId;
    private final String  sessionId;        // null for stateless turns
    private final String  userId;

    // ── Prompt ───────────────────────────────────────────────────────────────
    private final String  promptTemplateId;
    private final int     promptVersion;

    // ── Model ────────────────────────────────────────────────────────────────
    private final String  modelName;

    // ── Turn position ────────────────────────────────────────────────────────
    private final int     turnNumber;       // 1-based within the session

    // ── Timing (set at construction; end time filled by AiMetricsRecorder) ──
    private final Instant startedAt;

    private AiTraceContext(Builder b) {
        this.correlationId   = Objects.requireNonNull(b.correlationId,   "correlationId");
        this.userId          = Objects.requireNonNull(b.userId,          "userId");
        this.promptTemplateId= Objects.requireNonNull(b.promptTemplateId,"promptTemplateId");
        this.modelName       = Objects.requireNonNull(b.modelName,       "modelName");
        this.sessionId       = b.sessionId;      // nullable — stateless turns
        this.promptVersion   = b.promptVersion;
        this.turnNumber      = b.turnNumber > 0 ? b.turnNumber : 1;
        this.startedAt       = b.startedAt != null ? b.startedAt : Instant.now();
    }

    // ── Accessors ─────────────────────────────────────────────────────────────

    public String  correlationId()    { return correlationId; }
    public String  sessionId()        { return sessionId; }
    public String  userId()           { return userId; }
    public String  promptTemplateId() { return promptTemplateId; }
    public int     promptVersion()    { return promptVersion; }
    public String  promptQualified()  { return promptTemplateId + ":v" + promptVersion; }
    public String  modelName()        { return modelName; }
    public int     turnNumber()       { return turnNumber; }
    public Instant startedAt()        { return startedAt; }

    /** Returns true when this turn is stateful (belongs to a session). */
    public boolean hasSession() { return sessionId != null && !sessionId.isBlank(); }

    @Override
    public String toString() {
        return "AiTraceContext{"
                + "correlationId='" + correlationId + '\''
                + ", userId='" + userId + '\''
                + ", prompt='" + promptQualified() + '\''
                + ", model='" + modelName + '\''
                + ", turn=" + turnNumber
                + (hasSession() ? ", sessionId='" + sessionId + '\'' : "")
                + '}';
    }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private String  correlationId;
        private String  sessionId;
        private String  userId;
        private String  promptTemplateId;
        private int     promptVersion   = 1;
        private String  modelName;
        private int     turnNumber      = 1;
        private Instant startedAt;

        public Builder correlationId(String v)    { this.correlationId   = v; return this; }
        public Builder sessionId(String v)        { this.sessionId       = v; return this; }
        public Builder userId(String v)           { this.userId          = v; return this; }
        public Builder promptTemplateId(String v) { this.promptTemplateId= v; return this; }
        public Builder promptVersion(int v)       { this.promptVersion   = v; return this; }
        public Builder modelName(String v)        { this.modelName       = v; return this; }
        public Builder turnNumber(int v)          { this.turnNumber      = v; return this; }
        public Builder startedAt(Instant v)       { this.startedAt       = v; return this; }
        public AiTraceContext build()             { return new AiTraceContext(this); }
    }
}