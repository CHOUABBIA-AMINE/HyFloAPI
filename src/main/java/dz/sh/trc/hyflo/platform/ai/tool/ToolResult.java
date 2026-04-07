package dz.sh.trc.hyflo.platform.ai.tool;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.Objects;

/**
 * Compact, schema-safe tool result returned by every {@link ToolFunction}.
 *
 * <h3>Design rules:</h3>
 * <ul>
 *   <li>{@code content} is the primary payload — must be JSON-serialisable,
 *       short, and directly usable by the LLM in its next reasoning step</li>
 *   <li>{@code success} drives {@link ToolExecutor} logging; on false,
 *       {@code errorCode} and {@code errorMessage} must be set</li>
 *   <li>NULL fields are excluded from serialisation ({@code NON_NULL})
 *       to keep the token footprint minimal</li>
 * </ul>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ToolResult {

    private final String  toolName;
    private final boolean success;
    private final Object  content;
    private final String  errorCode;
    private final String  errorMessage;
    private final Instant executedAt;

    private ToolResult(Builder b) {
        this.toolName     = Objects.requireNonNull(b.toolName,   "toolName must not be null");
        this.success      = b.success;
        this.content      = b.content;
        this.errorCode    = b.errorCode;
        this.errorMessage = b.errorMessage;
        this.executedAt   = b.executedAt != null ? b.executedAt : Instant.now();
    }

    public String  getToolName()     { return toolName; }
    public boolean isSuccess()       { return success; }
    public Object  getContent()      { return content; }
    public String  getErrorCode()    { return errorCode; }
    public String  getErrorMessage() { return errorMessage; }
    public Instant getExecutedAt()   { return executedAt; }

    /** Creates a successful result with the given content payload. */
    public static ToolResult ok(String toolName, Object content) {
        return new Builder(toolName).success(true).content(content).build();
    }

    /** Creates a failed result with error code and message. */
    public static ToolResult failure(String toolName, String errorCode, String message) {
        return new Builder(toolName).success(false)
                .errorCode(errorCode).errorMessage(message).build();
    }

    public static Builder builder(String toolName) { return new Builder(toolName); }

    public static final class Builder {
        private final String toolName;
        private boolean success;
        private Object  content;
        private String  errorCode;
        private String  errorMessage;
        private Instant executedAt;

        private Builder(String toolName) { this.toolName = toolName; }

        public Builder success(boolean v)      { this.success = v;      return this; }
        public Builder content(Object v)       { this.content = v;      return this; }
        public Builder errorCode(String v)     { this.errorCode = v;    return this; }
        public Builder errorMessage(String v)  { this.errorMessage = v; return this; }
        public Builder executedAt(Instant v)   { this.executedAt = v;   return this; }
        public ToolResult build()              { return new ToolResult(this); }
    }
}