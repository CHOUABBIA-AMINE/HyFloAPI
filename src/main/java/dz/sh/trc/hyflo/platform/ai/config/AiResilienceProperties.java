package dz.sh.trc.hyflo.platform.ai.resilience;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Typed resilience configuration for the AI platform.
 * Binds to {@code hyflo.ai.resilience.*} in {@code application.properties}.
 *
 * <p>Kept in the {@code resilience} package (not {@code config}) because
 * all consumers are in this package and the config is tightly coupled to
 * the resilience implementation.</p>
 */
@Component
@ConfigurationProperties(prefix = "hyflo.ai.resilience")
public class AiResilienceProperties {

    private Retry retry = new Retry();
    private CircuitBreakerConfig circuitBreaker = new CircuitBreakerConfig();

    public Retry getRetry()                                        { return retry; }
    public void setRetry(Retry retry)                              { this.retry = retry; }
    public CircuitBreakerConfig getCircuitBreaker()                { return circuitBreaker; }
    public void setCircuitBreaker(CircuitBreakerConfig cb)         { this.circuitBreaker = cb; }

    /**
     * Retry configuration with exponential backoff.
     */
    public static class Retry {
        /** Maximum call attempts including the first (1 = no retry). */
        private int    maxAttempts       = 3;
        /** Initial wait before first retry in milliseconds. */
        private long   initialIntervalMs = 2000;
        /** Exponential multiplier applied to each subsequent wait. */
        private double multiplier        = 2.0;
        /** Maximum wait between retries in milliseconds. */
        private long   maxIntervalMs     = 30000;

        public int    getMaxAttempts()                  { return maxAttempts; }
        public void   setMaxAttempts(int v)             { this.maxAttempts = v; }
        public long   getInitialIntervalMs()            { return initialIntervalMs; }
        public void   setInitialIntervalMs(long v)      { this.initialIntervalMs = v; }
        public double getMultiplier()                   { return multiplier; }
        public void   setMultiplier(double v)           { this.multiplier = v; }
        public long   getMaxIntervalMs()                { return maxIntervalMs; }
        public void   setMaxIntervalMs(long v)          { this.maxIntervalMs = v; }
    }

    /**
     * Circuit breaker configuration.
     */
    public static class CircuitBreakerConfig {
        private float failureRateThreshold                   = 50f;
        private int   slidingWindowSize                      = 10;
        private int   minimumNumberOfCalls                   = 5;
        private long  waitDurationInOpenStateMs              = 30_000L;
        private int   permittedNumberOfCallsInHalfOpenState  = 2;
        /** LLM calls exceeding this duration (ms) count as slow calls. */
        private long  slowCallDurationThresholdMs            = 15_000L;
        /** Percentage of slow calls that triggers a circuit open. */
        private float slowCallRateThreshold                  = 80f;

        public float getFailureRateThreshold()                     { return failureRateThreshold; }
        public void  setFailureRateThreshold(float v)              { this.failureRateThreshold = v; }
        public int   getSlidingWindowSize()                        { return slidingWindowSize; }
        public void  setSlidingWindowSize(int v)                   { this.slidingWindowSize = v; }
        public int   getMinimumNumberOfCalls()                     { return minimumNumberOfCalls; }
        public void  setMinimumNumberOfCalls(int v)                { this.minimumNumberOfCalls = v; }
        public long  getWaitDurationInOpenStateMs()                { return waitDurationInOpenStateMs; }
        public void  setWaitDurationInOpenStateMs(long v)          { this.waitDurationInOpenStateMs = v; }
        public int   getPermittedNumberOfCallsInHalfOpenState()    { return permittedNumberOfCallsInHalfOpenState; }
        public void  setPermittedNumberOfCallsInHalfOpenState(int v){ this.permittedNumberOfCallsInHalfOpenState = v; }
        public long  getSlowCallDurationThresholdMs()              { return slowCallDurationThresholdMs; }
        public void  setSlowCallDurationThresholdMs(long v)        { this.slowCallDurationThresholdMs = v; }
        public float getSlowCallRateThreshold()                    { return slowCallRateThreshold; }
        public void  setSlowCallRateThreshold(float v)             { this.slowCallRateThreshold = v; }
    }
}