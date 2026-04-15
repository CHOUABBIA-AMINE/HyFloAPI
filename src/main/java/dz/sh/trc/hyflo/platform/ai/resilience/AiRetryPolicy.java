package dz.sh.trc.hyflo.platform.ai.resilience;

import dz.sh.trc.hyflo.platform.ai.agent.AgentExecutionException;
import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

/**
 * Programmatic retry policy for LLM calls with exponential backoff.
 *
 * <p>This class owns the {@link Retry} instance for the {@code llmRetry}
 * instance name. It is the single source of truth for what constitutes a
 * retryable failure and how long to wait between attempts.</p>
 *
 * <h3>Retry policy:</h3>
 * <ul>
 *   <li>Max attempts: 3 (configurable via {@link AiResilienceProperties})</li>
 *   <li>Backoff: exponential — base 2s, multiplier 2.0, max 30s
 *       (attempts: 2s → 4s → 8s)</li>
 *   <li>Retryable exceptions: {@link IOException}, {@link TimeoutException},
 *       and {@link AgentExecutionException} flagged as transient</li>
 *   <li>Non-retryable: authentication errors, invalid request errors —
 *       retrying these wastes quota and time</li>
 * </ul>
 *
 * <h3>Why programmatic instead of annotation-driven?</h3>
 * <ul>
 *   <li>Exponential backoff requires {@link IntervalFunction} which cannot
 *       be expressed in {@code application.properties}</li>
 *   <li>The retry exception predicate needs access to
 *       {@link AgentExecutionException#isTransient()} — not available
 *       via property-based class lists</li>
 *   <li>Programmatic config is explicit, testable, and keeps the policy
 *       co-located with its rationale</li>
 * </ul>
 */
@Component
public class AiRetryPolicy {

    private static final Logger log = LoggerFactory.getLogger(AiRetryPolicy.class);

    /** Resilience4j instance name — must match application.properties key. */
    public static final String INSTANCE_NAME = "llmRetry";

    private final Retry retry;
    private final AiResilienceProperties props;

    public AiRetryPolicy(AiResilienceProperties props, RetryRegistry retryRegistry) {
        this.props = Objects.requireNonNull(props, "AiResilienceProperties must not be null");
        Objects.requireNonNull(retryRegistry, "RetryRegistry must not be null");

        RetryConfig config = RetryConfig.custom()
                // Exponential backoff: baseMs * multiplier^(attempt-1), capped at maxMs
                .intervalFunction(
                        IntervalFunction.ofExponentialBackoff(
                                Duration.ofMillis(props.getRetry().getInitialIntervalMs()),
                                props.getRetry().getMultiplier(),
                                Duration.ofMillis(props.getRetry().getMaxIntervalMs())
                        )
                )
                .maxAttempts(props.getRetry().getMaxAttempts())
                // Retry on network-level and timeout transient failures
                .retryOnException(AiRetryPolicy::isTransientFailure)
                // Never retry on non-transient AgentExecutionException
                .ignoreExceptions(IllegalArgumentException.class)
                .build();

        this.retry = retryRegistry.retry(INSTANCE_NAME, config);

        // Register event listeners for observability
        this.retry.getEventPublisher()
                .onRetry(e -> log.warn("[AiRetryPolicy] attempt #{} — cause: {} correlationId={}",
                        e.getNumberOfRetryAttempts(),
                        e.getLastThrowable() != null ? e.getLastThrowable().getMessage() : "unknown",
                        extractCorrelationId(e.getLastThrowable())))
                .onError(e -> log.error("[AiRetryPolicy] exhausted after {} attempts — cause: {}",
                        e.getNumberOfRetryAttempts(),
                        e.getLastThrowable() != null ? e.getLastThrowable().getMessage() : "unknown"))
                .onSuccess(e -> {
                    if (e.getNumberOfRetryAttempts() > 0) {
                        log.info("[AiRetryPolicy] succeeded after {} retry attempt(s)",
                                e.getNumberOfRetryAttempts());
                    }
                });

        log.info("[AiRetryPolicy] Initialised — maxAttempts={} initialIntervalMs={} multiplier={} maxIntervalMs={}",
                props.getRetry().getMaxAttempts(),
                props.getRetry().getInitialIntervalMs(),
                props.getRetry().getMultiplier(),
                props.getRetry().getMaxIntervalMs());
    }

    /**
     * Executes the supplier with retry wrapping applied.
     *
     * @param supplier the LLM call to execute (must not be null)
     * @param <T>      the return type
     * @return the supplier result
     * @throws AgentExecutionException wrapping the final cause after retry exhaustion
     */
    public <T> T execute(Supplier<T> supplier) {
        Objects.requireNonNull(supplier, "supplier must not be null");
        Supplier<T> decorated = Retry.decorateSupplier(retry, supplier);
        try {
            return decorated.get();
        } catch (Exception e) {
            throw wrapAsAgentExecutionException(e);
        }
    }

    /** Exposes the underlying {@link Retry} instance for metric registration. */
    public Retry getRetry() {
        return retry;
    }

    // -------------------------------------------------------------------------
    // Transient failure classification
    // -------------------------------------------------------------------------

    /**
     * Classifies a throwable as transient (retryable) or permanent (non-retryable).
     *
     * <p>Transient: network I/O, timeout, rate-limit (HTTP 429),
     * server errors (HTTP 5xx), and {@link AgentExecutionException}
     * instances explicitly marked transient.</p>
     *
     * <p>Permanent: authentication errors, invalid request (HTTP 400/401/403),
     * and any {@link AgentExecutionException} not marked transient.</p>
     */
    static boolean isTransientFailure(Throwable t) {
        if (t instanceof IOException)      return true;
        if (t instanceof TimeoutException) return true;
        if (t instanceof AgentExecutionException aee) {
            return aee.isTransient();
        }
        // Unwrap cause for cases where Resilience4j wraps the original
        if (t.getCause() != null && t.getCause() != t) {
            return isTransientFailure(t.getCause());
        }
        return false;
    }

    private static AgentExecutionException wrapAsAgentExecutionException(Exception e) {
        if (e instanceof AgentExecutionException aee) return aee;
        return new AgentExecutionException(
                "LLM call failed after retry exhaustion: " + e.getMessage(),
                e,
                null,
                false  // non-transient after exhaustion — no more retries upstream
        );
    }

    private static String extractCorrelationId(Throwable t) {
        if (t instanceof AgentExecutionException aee) return aee.getCorrelationId();
        return "unknown";
    }
}