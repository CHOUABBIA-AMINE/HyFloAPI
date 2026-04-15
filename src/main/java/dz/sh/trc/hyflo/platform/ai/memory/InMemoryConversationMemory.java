package dz.sh.trc.hyflo.platform.ai.memory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * JVM-local, in-memory implementation of {@link ConversationMemory}.
 *
 * <p>This implementation is active by default. It is backed by a
 * {@link ConcurrentHashMap} of session ID → message list, using
 * {@link CopyOnWriteArrayList} for each session so that reads (which
 * are much more frequent than writes in the agent loop) are lock-free.</p>
 *
 * <h3>Activation:</h3>
 * <p>This bean is registered as a {@link ConditionalOnMissingBean} —
 * it activates whenever no other {@link ConversationMemory} bean is
 * present. When {@link PgVectorConversationMemory} is enabled via
 * {@code hyflo.ai.memory.pg-vector.enabled=true}, that bean takes
 * precedence and this in-memory impl is not registered.</p>
 *
 * <h3>Limitations:</h3>
 * <ul>
 *   <li>Session data is lost on JVM restart.</li>
 *   <li>Not suitable for multi-node deployments without a shared store.</li>
 *   <li>{@link #summarize(String)} always returns empty — no LLM call is made.</li>
 * </ul>
 *
 * <h3>Thread safety:</h3>
 * <p>{@link ConcurrentHashMap#computeIfAbsent} guarantees that each session
 * list is created exactly once. {@link CopyOnWriteArrayList} makes reads
 * safe for concurrent agent loop iterations.</p>
 */
@Component
@ConditionalOnMissingBean(ConversationMemory.class)
public class InMemoryConversationMemory implements ConversationMemory {

    private static final Logger log = LoggerFactory.getLogger(InMemoryConversationMemory.class);

    private final ConcurrentHashMap<String, CopyOnWriteArrayList<ConversationMessage>>
            store = new ConcurrentHashMap<>();

    // -------------------------------------------------------------------------
    // ConversationMemory implementation
    // -------------------------------------------------------------------------

    @Override
    public void append(String sessionId, ConversationMessage message) {
        Objects.requireNonNull(sessionId, "sessionId");
        Objects.requireNonNull(message,   "message");

        store.computeIfAbsent(sessionId, k -> new CopyOnWriteArrayList<>()).add(message);

        log.debug("[InMemoryConversationMemory] appended role={} sessionId={}",
                message.role(), sessionId);
    }

    @Override
    public List<ConversationMessage> getHistory(String sessionId) {
        Objects.requireNonNull(sessionId, "sessionId");

        List<ConversationMessage> history = store.get(sessionId);
        if (history == null) {
            return List.of();
        }
        return Collections.unmodifiableList(new ArrayList<>(history));
    }

    @Override
    public List<ConversationMessage> getRecentHistory(String sessionId, int n) {
        Objects.requireNonNull(sessionId, "sessionId");
        if (n <= 0) throw new IllegalArgumentException("n must be > 0");

        List<ConversationMessage> history = store.get(sessionId);
        if (history == null || history.isEmpty()) {
            return List.of();
        }

        List<ConversationMessage> snapshot = new ArrayList<>(history);
        int from = Math.max(0, snapshot.size() - n);
        return Collections.unmodifiableList(snapshot.subList(from, snapshot.size()));
    }

    @Override
    public void clearHistory(String sessionId) {
        Objects.requireNonNull(sessionId, "sessionId");

        CopyOnWriteArrayList<ConversationMessage> list = store.get(sessionId);
        if (list != null) {
            list.clear();
            log.debug("[InMemoryConversationMemory] cleared history sessionId={}", sessionId);
        }
    }

    @Override
    public void evict(String sessionId) {
        Objects.requireNonNull(sessionId, "sessionId");

        store.remove(sessionId);
        log.debug("[InMemoryConversationMemory] evicted sessionId={}", sessionId);
    }

    @Override
    public Optional<String> summarize(String sessionId) {
        // Summarization requires PgVectorConversationMemory + a summarization agent pass.
        // In-memory implementation does not perform LLM calls.
        return Optional.empty();
    }

    @Override
    public int messageCount(String sessionId) {
        Objects.requireNonNull(sessionId, "sessionId");

        List<ConversationMessage> list = store.get(sessionId);
        return list == null ? 0 : list.size();
    }
}