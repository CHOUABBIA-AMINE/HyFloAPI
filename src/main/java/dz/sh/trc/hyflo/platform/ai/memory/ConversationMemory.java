package dz.sh.trc.hyflo.platform.ai.memory;

import java.util.List;
import java.util.Optional;

/**
 * Port interface for conversation memory — the contract all callers use.
 *
 * <h3>Implementations:</h3>
 * <ul>
 *   <li>{@link InMemoryConversationMemory} — active by default; JVM-local,
 *       lost on restart. Suitable for development and stateless deployments.</li>
 *   <li>{@link PgVectorConversationMemory} — enabled via
 *       {@code hyflo.ai.memory.pg-vector.enabled=true}; persists to PostgreSQL
 *       with pgvector extension for future semantic search.</li>
 * </ul>
 *
 * <h3>Usage contract:</h3>
 * <p>All callers must go through {@link SessionIsolationEnforcer} before
 * calling any method on this interface. The memory store itself does NOT
 * enforce session ownership — that is the enforcer's responsibility.
 * Callers that bypass the enforcer will fail code review.</p>
 *
 * <h3>Message ordering:</h3>
 * <p>{@link #getHistory(String)} always returns messages in insertion order
 * (oldest first), ready for direct serialization into the LLM context window.</p>
 */
public interface ConversationMemory {

    /**
     * Appends a message to the history of the given session.
     *
     * @param sessionId the session ID (must have been created by {@link SessionRegistry})
     * @param message   the message to append
     */
    void append(String sessionId, ConversationMessage message);

    /**
     * Returns the full message history for the given session, in insertion order.
     * Returns an empty list if the session has no history yet.
     *
     * @param sessionId the session ID
     * @return an unmodifiable list of messages, oldest first
     */
    List<ConversationMessage> getHistory(String sessionId);

    /**
     * Returns the last {@code n} messages of the session history, oldest first.
     * Useful for sliding-window context management when the full history is
     * too long for the model's context window.
     *
     * @param sessionId the session ID
     * @param n         how many recent messages to return (must be > 0)
     * @return an unmodifiable list of the last {@code n} messages, oldest first
     */
    List<ConversationMessage> getRecentHistory(String sessionId, int n);

    /**
     * Clears the full message history for the given session.
     * Used when the user explicitly starts a new conversation thread
     * within an existing session.
     *
     * @param sessionId the session ID
     */
    void clearHistory(String sessionId);

    /**
     * Removes all data associated with the given session.
     * Called by {@link SessionRegistry} when a session expires or is explicitly closed.
     *
     * @param sessionId the session ID
     */
    void evict(String sessionId);

    /**
     * Optional: returns a natural-language summary of the session history,
     * suitable for injection into a new context window.
     *
     * <p>The in-memory implementation returns {@link Optional#empty()} —
     * summarization requires the PgVector implementation with a summarization
     * agent pass.</p>
     *
     * @param sessionId the session ID
     * @return a summary string, or empty if summarization is not available
     */
    Optional<String> summarize(String sessionId);

    /**
     * Returns the number of messages stored for the given session.
     *
     * @param sessionId the session ID
     * @return the message count (0 if the session has no history or does not exist)
     */
    int messageCount(String sessionId);
}