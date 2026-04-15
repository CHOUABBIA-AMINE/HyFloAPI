package dz.sh.trc.hyflo.platform.ai.observability;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for {@link AiInsight} persistence.
 *
 * <h3>Append-only contract:</h3>
 * <p>This repository exposes only {@code save()} and read operations.
 * There are no {@code delete}, {@code deleteAll}, or bulk-update methods.
 * Insight records are immutable governance documents after creation.</p>
 *
 * <h3>Query design:</h3>
 * <p>All named queries use JPQL to remain independent of the underlying
 * database dialect (PostgreSQL in production, H2 in tests). Pageable
 * variants are provided for all list-returning queries to prevent
 * unbounded result sets in production.</p>
 */
@Repository
public interface AiInsightRepository extends JpaRepository<AiInsight, Long> {

    // ── Lookup by correlation ID ──────────────────────────────────────────────

    /**
     * Returns the insight for a specific request correlation ID.
     * Correlation IDs are unique per turn; this always returns at most one result.
     *
     * @param correlationId the request correlation ID
     * @return the insight, or empty if not found
     */
    Optional<AiInsight> findByCorrelationId(String correlationId);

    // ── Session-scoped queries ────────────────────────────────────────────────

    /**
     * Returns all insights for the given session, newest first.
     *
     * @param sessionId the conversation session ID
     * @param pageable  pagination params
     * @return a page of insights for the session
     */
    Page<AiInsight> findBySessionIdOrderByStartedAtDesc(String sessionId, Pageable pageable);

    /**
     * Returns the turn count for a session.
     * Used by the session TTL extension logic to detect idle sessions.
     *
     * @param sessionId the session ID
     * @return number of turns recorded for this session
     */
    long countBySessionId(String sessionId);

    // ── User-scoped queries ───────────────────────────────────────────────────

    /**
     * Returns all insights for the given user, newest first.
     *
     * @param userId   the authenticated user ID
     * @param pageable pagination params
     * @return a page of insights for the user
     */
    Page<AiInsight> findByUserIdOrderByStartedAtDesc(String userId, Pageable pageable);

    // ── Prompt version queries ────────────────────────────────────────────────

    /**
     * Returns all insights for a specific prompt template ID (all versions).
     * Used to compare performance across prompt versions.
     *
     * @param promptTemplateId the template ID (e.g. {@code "flow-anomaly"})
     * @param pageable         pagination params
     * @return a page of insights
     */
    Page<AiInsight> findByPromptTemplateIdOrderByStartedAtDesc(
            String promptTemplateId, Pageable pageable);

    /**
     * Returns all insights for a specific prompt version.
     *
     * @param promptTemplateId the template ID
     * @param promptVersion    the version number
     * @param pageable         pagination params
     * @return a page of insights
     */
    Page<AiInsight> findByPromptTemplateIdAndPromptVersionOrderByStartedAtDesc(
            String promptTemplateId, int promptVersion, Pageable pageable);

    // ── Time-window queries ───────────────────────────────────────────────────

    /**
     * Returns all insights started within the given time window, newest first.
     *
     * @param from     start of window (inclusive)
     * @param to       end of window (inclusive)
     * @param pageable pagination params
     * @return a page of insights
     */
    Page<AiInsight> findByStartedAtBetweenOrderByStartedAtDesc(
            Instant from, Instant to, Pageable pageable);

    // ── Error / failure queries ───────────────────────────────────────────────

    /**
     * Returns all failed turns, newest first.
     * Used for operational health dashboards and alerting.
     *
     * @param pageable pagination params
     * @return a page of failed insights
     */
    Page<AiInsight> findByStatusOrderByStartedAtDesc(
            AiInsight.TurnStatus status, Pageable pageable);

    /**
     * Returns failed turns for a specific error code.
     *
     * @param errorCode the error classification code
     * @param pageable  pagination params
     * @return a page of insights with the given error code
     */
    Page<AiInsight> findByErrorCodeOrderByStartedAtDesc(
            String errorCode, Pageable pageable);

    // ── Aggregate / reporting queries ─────────────────────────────────────────

    /**
     * Returns per-prompt aggregate statistics for the given time window.
     *
     * <p>Returns rows of: {@code [promptTemplateId, promptVersion, count, avgLatencyMs,
     * totalPromptTokens, totalCompletionTokens, errorCount]}.</p>
     *
     * @param from start of window
     * @param to   end of window
     * @return aggregate rows
     */
    @Query("""
            SELECT i.promptTemplateId,
                   i.promptVersion,
                   COUNT(i),
                   AVG(i.latencyMs),
                   SUM(COALESCE(i.promptTokens, 0)),
                   SUM(COALESCE(i.completionTokens, 0)),
                   SUM(CASE WHEN i.status = 'ERROR' THEN 1 ELSE 0 END)
            FROM AiInsight i
            WHERE i.startedAt BETWEEN :from AND :to
            GROUP BY i.promptTemplateId, i.promptVersion
            ORDER BY i.promptTemplateId, i.promptVersion
            """)
    List<Object[]> aggregateByPromptAndVersion(
            @Param("from") Instant from,
            @Param("to")   Instant to);

    /**
     * Returns per-model aggregate statistics for the given time window.
     *
     * <p>Returns rows of: {@code [modelUsed, count, avgLatencyMs,
     * totalTokens, errorCount]}.</p>
     *
     * @param from start of window
     * @param to   end of window
     * @return aggregate rows
     */
    @Query("""
            SELECT i.modelUsed,
                   COUNT(i),
                   AVG(i.latencyMs),
                   SUM(COALESCE(i.promptTokens, 0) + COALESCE(i.completionTokens, 0)),
                   SUM(CASE WHEN i.status = 'ERROR' THEN 1 ELSE 0 END)
            FROM AiInsight i
            WHERE i.startedAt BETWEEN :from AND :to
            GROUP BY i.modelUsed
            ORDER BY COUNT(i) DESC
            """)
    List<Object[]> aggregateByModel(
            @Param("from") Instant from,
            @Param("to")   Instant to);

    /**
     * Returns the p95 latency bucket for a given prompt and time window.
     * Delegates to a native query for {@code percentile_cont} (PostgreSQL).
     *
     * @param promptTemplateId the template ID
     * @param from             start of window
     * @param to               end of window
     * @return the p95 latency in milliseconds, or null if no data
     */
    @Query(value = """
            SELECT percentile_cont(0.95) WITHIN GROUP (ORDER BY F_10)
            FROM T_AI_INSIGHT
            WHERE F_04 = :promptTemplateId
              AND F_09 BETWEEN :from AND :to
            """,
            nativeQuery = true)
    Double findP95LatencyMs(
            @Param("promptTemplateId") String promptTemplateId,
            @Param("from")            Instant from,
            @Param("to")              Instant to);
}