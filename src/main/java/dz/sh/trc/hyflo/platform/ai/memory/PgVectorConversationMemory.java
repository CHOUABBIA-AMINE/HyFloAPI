package dz.sh.trc.hyflo.platform.ai.memory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * PostgreSQL-backed implementation of {@link ConversationMemory}.
 *
 * <p>This implementation is disabled by default. Enable it with:</p>
 * <pre>{@code
 * hyflo.ai.memory.pg-vector.enabled=true
 * }</pre>
 *
 * <h3>Table schema required:</h3>
 * <pre>{@code
 * CREATE TABLE IF NOT EXISTS ai_conversation_message (
 *     id             BIGSERIAL PRIMARY KEY,
 *     session_id     VARCHAR(128)  NOT NULL,
 *     role           VARCHAR(16)   NOT NULL,
 *     content        TEXT          NOT NULL,
 *     tool_call_id   VARCHAR(128),
 *     correlation_id VARCHAR(128),
 *     sent_at        TIMESTAMPTZ   NOT NULL DEFAULT now(),
 *     summary        TEXT                               -- written by summarize()
 * );
 *
 * CREATE INDEX IF NOT EXISTS idx_ai_conversation_session
 *     ON ai_conversation_message (session_id, sent_at);
 * }</pre>
 *
 * <h3>PgVector readiness:</h3>
 * <p>The {@code embedding} column (type {@code vector(1536)}) and the HNSW index
 * are intentionally NOT added in this first pass. They will be added in a future
 * step when semantic retrieval is implemented. The schema as written is forwards-
 * compatible — adding an embedding column is a non-breaking {@code ALTER TABLE}.</p>
 *
 * <h3>Summarization:</h3>
 * <p>{@link #summarize(String)} reads the most recent
 * {@code SUMMARIZE_WINDOW} messages, concatenates them, and stores the result
 * in the {@code summary} column of the latest message row. The summary is returned
 * for injection into a fresh context window when the history is too long.
 * Note: this implementation does NOT call an LLM — it concatenates content.
 * A proper summarization agent pass will be added in Step 4.4.</p>
 *
 * <h3>Failure handling:</h3>
 * <p>If the database is unavailable, all methods throw {@link MemoryAccessException},
 * which the {@link dz.sh.trc.hyflo.platform.ai.agent.AgentExecutor} catches and
 * handles as a transient failure for the resilience layer.</p>
 */
@Component
@ConditionalOnProperty(name = "hyflo.ai.memory.pg-vector.enabled", havingValue = "true")
public class PgVectorConversationMemory implements ConversationMemory {

    private static final Logger log =
            LoggerFactory.getLogger(PgVectorConversationMemory.class);

    private static final int SUMMARIZE_WINDOW = 20;

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public PgVectorConversationMemory(JdbcTemplate jdbcTemplate,
                                       ObjectMapper objectMapper) {
        this.jdbcTemplate = Objects.requireNonNull(jdbcTemplate, "JdbcTemplate");
        this.objectMapper = Objects.requireNonNull(objectMapper, "ObjectMapper");
    }

    // -------------------------------------------------------------------------
    // ConversationMemory implementation
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public void append(String sessionId, ConversationMessage message) {
        Objects.requireNonNull(sessionId, "sessionId");
        Objects.requireNonNull(message,   "message");

        try {
            jdbcTemplate.update("""
                    INSERT INTO ai_conversation_message
                        (session_id, role, content, tool_call_id, correlation_id, sent_at)
                    VALUES (?, ?, ?, ?, ?, ?)
                    """,
                    sessionId,
                    message.role().name(),
                    message.content(),
                    message.toolCallId(),
                    message.correlationId(),
                    message.timestamp() != null
                            ? java.sql.Timestamp.from(message.timestamp())
                            : java.sql.Timestamp.from(Instant.now()));

            log.debug("[PgVectorConversationMemory] appended role={} sessionId={}",
                    message.role(), sessionId);

        } catch (Exception e) {
            log.error("[PgVectorConversationMemory] append failed sessionId={}: {}",
                    sessionId, e.getMessage());
            throw new MemoryAccessException(
                    "Failed to append message to session '" + sessionId + "'", e, sessionId);
        }
    }

    @Override
    public List<ConversationMessage> getHistory(String sessionId) {
        Objects.requireNonNull(sessionId, "sessionId");

        try {
            List<ConversationMessage> messages = jdbcTemplate.query("""
                    SELECT role, content, tool_call_id, correlation_id, sent_at
                    FROM ai_conversation_message
                    WHERE session_id = ?
                    ORDER BY sent_at ASC, id ASC
                    """,
                    (rs, rowNum) -> mapRow(rs),
                    sessionId);

            return Collections.unmodifiableList(messages);

        } catch (Exception e) {
            log.error("[PgVectorConversationMemory] getHistory failed sessionId={}: {}",
                    sessionId, e.getMessage());
            throw new MemoryAccessException(
                    "Failed to read history for session '" + sessionId + "'", e, sessionId);
        }
    }

    @Override
    public List<ConversationMessage> getRecentHistory(String sessionId, int n) {
        Objects.requireNonNull(sessionId, "sessionId");
        if (n <= 0) throw new IllegalArgumentException("n must be > 0");

        try {
            // Fetch last n rows ordered ascending for LLM context replay
            List<ConversationMessage> messages = jdbcTemplate.query("""
                    SELECT role, content, tool_call_id, correlation_id, sent_at
                    FROM (
                        SELECT role, content, tool_call_id, correlation_id, sent_at, id
                        FROM ai_conversation_message
                        WHERE session_id = ?
                        ORDER BY sent_at DESC, id DESC
                        LIMIT ?
                    ) sub
                    ORDER BY sent_at ASC, id ASC
                    """,
                    (rs, rowNum) -> mapRow(rs),
                    sessionId, n);

            return Collections.unmodifiableList(messages);

        } catch (Exception e) {
            log.error("[PgVectorConversationMemory] getRecentHistory failed sessionId={}: {}",
                    sessionId, e.getMessage());
            throw new MemoryAccessException(
                    "Failed to read recent history for session '" + sessionId + "'", e, sessionId);
        }
    }

    @Override
    @Transactional
    public void clearHistory(String sessionId) {
        Objects.requireNonNull(sessionId, "sessionId");

        try {
            int deleted = jdbcTemplate.update(
                    "DELETE FROM ai_conversation_message WHERE session_id = ?",
                    sessionId);
            log.debug("[PgVectorConversationMemory] cleared {} messages sessionId={}",
                    deleted, sessionId);
        } catch (Exception e) {
            throw new MemoryAccessException(
                    "Failed to clear history for session '" + sessionId + "'", e, sessionId);
        }
    }

    @Override
    @Transactional
    public void evict(String sessionId) {
        clearHistory(sessionId); // same operation at this schema level
        log.debug("[PgVectorConversationMemory] evicted sessionId={}", sessionId);
    }

    @Override
    public Optional<String> summarize(String sessionId) {
        Objects.requireNonNull(sessionId, "sessionId");

        try {
            List<ConversationMessage> recent = getRecentHistory(sessionId, SUMMARIZE_WINDOW);
            if (recent.isEmpty()) return Optional.empty();

            // Concatenation-only summary — LLM summarization will replace this in Step 4.4.
            StringBuilder sb = new StringBuilder("Conversation summary (last ")
                    .append(recent.size()).append(" messages):\n");
            for (ConversationMessage m : recent) {
                if (m.role() != ConversationMessage.Role.SYSTEM) {
                    sb.append("[").append(m.role()).append("] ")
                      .append(m.content(), 0, Math.min(m.content().length(), 200))
                      .append('\n');
                }
            }
            String summary = sb.toString();

            // Persist in the latest message row
            jdbcTemplate.update("""
                    UPDATE ai_conversation_message
                    SET summary = ?
                    WHERE id = (
                        SELECT id FROM ai_conversation_message
                        WHERE session_id = ?
                        ORDER BY sent_at DESC, id DESC
                        LIMIT 1
                    )
                    """,
                    summary, sessionId);

            return Optional.of(summary);

        } catch (Exception e) {
            log.warn("[PgVectorConversationMemory] summarize failed sessionId={}: {}",
                    sessionId, e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public int messageCount(String sessionId) {
        Objects.requireNonNull(sessionId, "sessionId");

        try {
            Integer count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM ai_conversation_message WHERE session_id = ?",
                    Integer.class,
                    sessionId);
            return count != null ? count : 0;
        } catch (Exception e) {
            throw new MemoryAccessException(
                    "Failed to count messages for session '" + sessionId + "'", e, sessionId);
        }
    }

    // -------------------------------------------------------------------------
    // Row mapping
    // -------------------------------------------------------------------------

    private ConversationMessage mapRow(ResultSet rs) throws SQLException {
        return ConversationMessage.builder()
                .role(ConversationMessage.Role.valueOf(rs.getString("role")))
                .content(rs.getString("content"))
                .toolCallId(rs.getString("tool_call_id"))
                .correlationId(rs.getString("correlation_id"))
                .timestamp(rs.getTimestamp("sent_at").toInstant())
                .build();
    }
}