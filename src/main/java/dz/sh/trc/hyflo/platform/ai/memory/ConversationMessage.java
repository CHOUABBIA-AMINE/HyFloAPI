package dz.sh.trc.hyflo.platform.ai.memory;

import java.time.Instant;
import java.util.Objects;

/**
 * A single typed message in a multi-turn conversation.
 *
 * <p>Messages are immutable value objects. Every turn appends two messages:
 * one {@link Role#USER} and one {@link Role#ASSISTANT}. Tool results
 * are appended as {@link Role#TOOL}, associating them with the preceding
 * assistant turn via {@link #toolCallId}.</p>
 *
 * <h3>Role semantics:</h3>
 * <ul>
 *   <li>{@link Role#SYSTEM}    — the agent's system prompt (turn 0, written once)</li>
 *   <li>{@link Role#USER}      — the human or upstream service message</li>
 *   <li>{@link Role#ASSISTANT} — the model response</li>
 *   <li>{@link Role#TOOL}      — a tool call result to be fed back to the model</li>
 * </ul>
 *
 * <h3>Ordering:</h3>
 * <p>Messages are ordered by {@link #timestamp}. The memory implementation
 * is responsible for preserving insertion order when replaying the history
 * to the LLM.</p>
 */
public final class ConversationMessage {

    /**
     * Message role, matching the roles expected by OpenAI-compatible APIs
     * (and by Spring AI's {@code ChatMessage} role hierarchy).
     */
    public enum Role { SYSTEM, USER, ASSISTANT, TOOL }

    private final Role    role;
    private final String  content;
    private final Instant timestamp;
    private final String  toolCallId;   // non-null only for Role.TOOL
    private final String  correlationId;

    private ConversationMessage(Builder b) {
        this.role          = Objects.requireNonNull(b.role,    "role");
        this.content       = Objects.requireNonNull(b.content, "content");
        this.timestamp     = b.timestamp != null ? b.timestamp : Instant.now();
        this.toolCallId    = b.toolCallId;
        this.correlationId = b.correlationId;
    }

    public Role    role()          { return role; }
    public String  content()       { return content; }
    public Instant timestamp()     { return timestamp; }
    public String  toolCallId()    { return toolCallId; }
    public String  correlationId() { return correlationId; }

    public boolean isToolResult()  { return role == Role.TOOL; }

    @Override
    public String toString() {
        return "ConversationMessage{role=" + role
                + ", timestamp=" + timestamp
                + ", contentLength=" + content.length() + "}";
    }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private Role    role;
        private String  content;
        private Instant timestamp;
        private String  toolCallId;
        private String  correlationId;

        public Builder role(Role v)          { this.role = v;          return this; }
        public Builder content(String v)     { this.content = v;       return this; }
        public Builder timestamp(Instant v)  { this.timestamp = v;     return this; }
        public Builder toolCallId(String v)  { this.toolCallId = v;    return this; }
        public Builder correlationId(String v){ this.correlationId = v; return this; }
        public ConversationMessage build()   { return new ConversationMessage(this); }
    }
}