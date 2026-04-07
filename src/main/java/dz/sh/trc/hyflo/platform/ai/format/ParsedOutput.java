package dz.sh.trc.hyflo.platform.ai.format;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Immutable container for a validated, strongly-typed LLM output.
 *
 * <p>This is the only type that crosses the boundary between the
 * {@code platform.ai.format} layer and any business code that consumes
 * AI responses. Raw {@code Map<String,Object>} never escapes this package.</p>
 *
 * <h3>States:</h3>
 * <ul>
 *   <li><strong>SUCCESS</strong> — {@link #value()} is present; the LLM
 *       produced valid, schema-conformant JSON that was deserialized into
 *       {@code T} successfully.</li>
 *   <li><strong>FAILED</strong> — {@link #value()} is empty; all correction
 *       retries were exhausted or a non-retryable error occurred.
 *       {@link #validationErrors()} describes what was wrong.
 *       {@link #lastRawOutput()} contains the last raw LLM string for
 *       audit and debugging.</li>
 * </ul>
 *
 * <h3>Usage contract:</h3>
 * <pre>{@code
 * ParsedOutput<FlowScoreDTO> result =
 *     structuredOutputEnforcer.enforce(rawLlmOutput, FlowScoreDTO.class, schema, correlationId);
 *
 * if (!result.isSuccess()) {
 *     throw new OutputParseException(result.correlationId(),
 *         result.validationErrors(), result.totalAttempts());
 * }
 * FlowScoreDTO score = result.value().orElseThrow();
 * }</pre>
 *
 * @param <T> the target business type
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ParsedOutput<T> {

    /**
     * Outcome of the parse + validate + correct cycle.
     */
    public enum Status { SUCCESS, FAILED }

    private final Status        status;
    private final T             value;
    private final List<String>  validationErrors;
    private final String        lastRawOutput;
    private final String        correlationId;
    private final int           totalAttempts;
    private final Instant       resolvedAt;

    private ParsedOutput(Builder<T> b) {
        this.status           = Objects.requireNonNull(b.status,        "status");
        this.correlationId    = Objects.requireNonNull(b.correlationId, "correlationId");
        this.value            = b.value;
        this.validationErrors = b.validationErrors != null
                ? Collections.unmodifiableList(b.validationErrors) : List.of();
        this.lastRawOutput    = b.lastRawOutput;
        this.totalAttempts    = b.totalAttempts;
        this.resolvedAt       = b.resolvedAt != null ? b.resolvedAt : Instant.now();
    }

    // -------------------------------------------------------------------------
    // Factory methods
    // -------------------------------------------------------------------------

    public static <T> ParsedOutput<T> success(T value, String correlationId,
                                               int attempts, String rawOutput) {
        return new Builder<T>(Status.SUCCESS, correlationId)
                .value(value)
                .lastRawOutput(rawOutput)
                .totalAttempts(attempts)
                .build();
    }

    public static <T> ParsedOutput<T> failure(List<String> errors, String correlationId,
                                               int attempts, String lastRaw) {
        return new Builder<T>(Status.FAILED, correlationId)
                .validationErrors(errors)
                .lastRawOutput(lastRaw)
                .totalAttempts(attempts)
                .build();
    }

    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------

    public boolean       isSuccess()         { return status == Status.SUCCESS; }
    public Status        status()            { return status; }
    public Optional<T>   value()             { return Optional.ofNullable(value); }
    public List<String>  validationErrors()  { return validationErrors; }
    public String        lastRawOutput()     { return lastRawOutput; }
    public String        correlationId()     { return correlationId; }
    public int           totalAttempts()     { return totalAttempts; }
    public Instant       resolvedAt()        { return resolvedAt; }

    @Override
    public String toString() {
        return "ParsedOutput{status=" + status
                + ", correlationId='" + correlationId + '\''
                + ", totalAttempts=" + totalAttempts
                + ", errors=" + validationErrors + '}';
    }

    // -------------------------------------------------------------------------
    // Builder
    // -------------------------------------------------------------------------

    public static final class Builder<T> {
        private final Status status;
        private final String correlationId;
        private T            value;
        private List<String> validationErrors;
        private String       lastRawOutput;
        private int          totalAttempts;
        private Instant      resolvedAt;

        public Builder(Status status, String correlationId) {
            this.status        = status;
            this.correlationId = correlationId;
        }

        public Builder<T> value(T v)                  { this.value = v;             return this; }
        public Builder<T> validationErrors(List<String> v) { this.validationErrors = v; return this; }
        public Builder<T> lastRawOutput(String v)      { this.lastRawOutput = v;    return this; }
        public Builder<T> totalAttempts(int v)         { this.totalAttempts = v;    return this; }
        public Builder<T> resolvedAt(Instant v)        { this.resolvedAt = v;       return this; }
        public ParsedOutput<T> build()                 { return new ParsedOutput<>(this); }
    }
}