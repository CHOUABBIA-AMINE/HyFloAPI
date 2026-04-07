package dz.sh.trc.hyflo.platform.ai.resilience;

import dz.sh.trc.hyflo.platform.ai.agent.AgentExecutionException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Programmatic circuit breaker wrapper for LLM provider calls.
 *
 * <p>Owns the {@link CircuitBreaker} instance for the {@code llmCircuitBreaker}
 * instance name. When the failure rate exceeds the configured threshold,
 * the circuit opens and subsequent calls are rejected immediately without
 * reaching the provider — protecting quota and reducing latency.</p>
 *
 * <h3>Circuit breaker configuration:</h3>
 * <ul>
 *   <li>Sliding window: COUNT_BASED, 10 calls</li>
 *   <li>Failure rate threshold: 50%</li>
 *   <li>Wait in open state: 30s before transitioning to half-open</li>
 *   <li>Half-open probe calls: 2</li>
 *   <li>Minimum calls before rate is calculated: 5</li>
 *   <li>Slow call duration threshold: 15s (LLM calls > 15s count as failures)</li>
 *   <li>Slow call rate threshold: 80%</li>
 * </ul>
 *
 * <h3>Why programmatic?</h3>
 * <p>The slow-call threshold and the failure predicate (only count
 * {@link AgentExecutionException} as circuit-breaker-relevant failures,
 * not business validation errors) cannot be expressed via properties alone.</p>
 */
@Component
public class AiCircuitBreaker {

    private static final Logger log = LoggerFactory.getLogger(AiCircuitBreaker.class);

    /** Resilience4j instance name — must match application.properties key. */
    public static final String INSTANCE_NAME = "llmCircuitBreaker";

    private final CircuitBreaker circuitBreaker;

    public AiCircuitBreaker(AiResilienceProperties props,
                             CircuitBreakerRegistry circuitBreakerRegistry) {
        Objects.requireNonNull(props, "AiResilienceProperties must not be null");
        Objects.requireNonNull(circuitBreakerRegistry, "CircuitBreakerRegistry must not be null");

        AiResilienceProperties.CircuitBreakerConfig cbProps = props.getCircuitBreaker();

        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .slidingWindowType(
                        CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(cbProps.getSlidingWindowSize())
                .minimumNumberOfCalls(cbProps.getMinimumNumberOfCalls())
                .failureRateThreshold(cbProps.getFailureRateThreshold())
                .slowCallDurationThreshold(
                        Duration.ofMillis(cbProps.getSlowCallDurationThresholdMs()))
                .slowCallRateThreshold(cbProps.getSlowCallRateThreshold())
                .waitDurationInOpenState(
                        Duration.ofMillis(cbProps.getWaitDurationInOpenStateMs()))
                .permittedNumberOfCallsInHalfOpenState(
                        cbProps.getPermittedNumberOfCallsInHalfOpenState())
                // Only count AgentExecutionException as circuit-breaker failures
                // (not IllegalArgumentException / validation issues)
                .recordException(t -> t instanceof AgentExecutionException
                        || t instanceof java.io.IOException
                        || t instanceof java.util.concurrent.TimeoutException)
                .build();

        this.circuitBreaker = circuitBreakerRegistry.circuitBreaker(INSTANCE_NAME, config);

        // Register state-change listeners for observability
        this.circuitBreaker.getEventPublisher()
                .onStateTransition(e -> log.warn(
                        "[AiCircuitBreaker] state transition: {} → {}",
                        e.getStateTransition().getFromState(),
                        e.getStateTransition().getToState()))
                .onCallNotPermitted(e -> log.warn(
                        "[AiCircuitBreaker] call rejected — circuit is OPEN"))
                .onError(e -> log.debug(
                        "[AiCircuitBreaker] recorded failure — duration={}ms cause={}",
                        e.getElapsedDuration().toMillis(),
                        e.getThrowable().getMessage()))
                .onSlowCallRateExceeded(e -> log.warn(
                        "[AiCircuitBreaker] slow call rate exceeded threshold: {}%",
                        e.getSlowCallRate()))
                .onFailureRateExceeded(e -> log.warn(
                        "[AiCircuitBreaker] failure rate exceeded threshold: {}%",
                        e.getFailureRate()));

        log.info("[AiCircuitBreaker] Initialised — failureThreshold={}% slidingWindow={} openWait={}ms",
                cbProps.getFailureRateThreshold(),
                cbProps.getSlidingWindowSize(),
                cbProps.getWaitDurationInOpenStateMs());
    }

    /**
     * Executes the supplier through the circuit breaker.
     *
     * <p>When the circuit is open, throws {@link AgentExecutionException}
     * with {@code isTransient=true} so the caller can invoke the fallback
     * model resolver instead of surfacing an error to the user.</p>
     *
     * @param supplier the LLM call to protect
     * @param correlationId the request correlation ID for error context
     * @param <T>      the return type
     * @return the supplier result
     * @throws AgentExecutionException if the circuit is open or the call fails
     */
    public <T> T execute(Supplier<T> supplier, String correlationId) {
        Objects.requireNonNull(supplier, "supplier must not be null");

        Supplier<T> decorated = CircuitBreaker.decorateSupplier(circuitBreaker, supplier);
        try {
            return decorated.get();
        } catch (CallNotPermittedException e) {
            log.warn("[AiCircuitBreaker] call not permitted — correlationId={}", correlationId);
            throw new AgentExecutionException(
                    "LLM provider circuit breaker is OPEN — call rejected",
                    e,
                    correlationId,
                    true  // transient=true → triggers fallback model resolution
            );
        } catch (AgentExecutionException e) {
            throw e;
        } catch (Exception e) {
            throw new AgentExecutionException(
                    "LLM call failed in circuit breaker: " + e.getMessage(),
                    e,
                    correlationId,
                    true
            );
        }
    }

    /** Returns the current circuit state for health check integration. */
    public CircuitBreaker.State getState() {
        return circuitBreaker.getState();
    }

    /** Exposes the underlying {@link CircuitBreaker} instance for metric registration. */
    public CircuitBreaker getCircuitBreaker() {
        return circuitBreaker;
    }
}