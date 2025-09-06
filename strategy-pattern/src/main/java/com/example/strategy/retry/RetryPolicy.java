package com.example.strategy.retry;

import java.time.Duration;
import java.util.function.Predicate;

/**
 * Configuration class for retry behavior.
 * Defines how many times to retry, delays between retries, and which exceptions should trigger retries.
 */
public class RetryPolicy {
    private final int maxAttempts;
    private final Duration baseDelay;
    private final Duration maxDelay;
    private final double backoffMultiplier;
    private final Predicate<Exception> retryCondition;
    private final boolean jitterEnabled;

    private RetryPolicy(Builder builder) {
        this.maxAttempts = builder.maxAttempts;
        this.baseDelay = builder.baseDelay;
        this.maxDelay = builder.maxDelay;
        this.backoffMultiplier = builder.backoffMultiplier;
        this.retryCondition = builder.retryCondition;
        this.jitterEnabled = builder.jitterEnabled;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public Duration getBaseDelay() {
        return baseDelay;
    }

    public Duration getMaxDelay() {
        return maxDelay;
    }

    public double getBackoffMultiplier() {
        return backoffMultiplier;
    }

    public Predicate<Exception> getRetryCondition() {
        return retryCondition;
    }

    public boolean isJitterEnabled() {
        return jitterEnabled;
    }

    /**
     * Calculates the delay for a specific attempt number
     */
    public Duration calculateDelay(int attemptNumber) {
        if (attemptNumber <= 1) {
            return baseDelay;
        }
        
        long delayMillis = baseDelay.toMillis();
        
        // Apply exponential backoff
        for (int i = 1; i < attemptNumber; i++) {
            delayMillis = (long) (delayMillis * backoffMultiplier);
        }
        
        // Apply jitter if enabled (add random variation up to 10%)
        if (jitterEnabled) {
            double jitter = Math.random() * 0.1 + 0.95; // 0.95 to 1.05
            delayMillis = (long) (delayMillis * jitter);
        }
        
        // Ensure we don't exceed max delay
        Duration calculatedDelay = Duration.ofMillis(delayMillis);
        return calculatedDelay.compareTo(maxDelay) > 0 ? maxDelay : calculatedDelay;
    }

    /**
     * Checks if an exception should trigger a retry
     */
    public boolean shouldRetry(Exception exception) {
        return retryCondition.test(exception);
    }

    public static class Builder {
        private int maxAttempts = 3;
        private Duration baseDelay = Duration.ofMillis(1000);
        private Duration maxDelay = Duration.ofSeconds(30);
        private double backoffMultiplier = 2.0;
        private Predicate<Exception> retryCondition = e -> true;
        private boolean jitterEnabled = true;

        public Builder maxAttempts(int maxAttempts) {
            this.maxAttempts = Math.max(1, maxAttempts);
            return this;
        }

        public Builder baseDelay(Duration baseDelay) {
            this.baseDelay = baseDelay;
            return this;
        }

        public Builder maxDelay(Duration maxDelay) {
            this.maxDelay = maxDelay;
            return this;
        }

        public Builder backoffMultiplier(double backoffMultiplier) {
            this.backoffMultiplier = Math.max(1.0, backoffMultiplier);
            return this;
        }

        public Builder retryCondition(Predicate<Exception> retryCondition) {
            this.retryCondition = retryCondition;
            return this;
        }

        public Builder enableJitter(boolean enabled) {
            this.jitterEnabled = enabled;
            return this;
        }

        public Builder retryOn(Class<? extends Exception> exceptionClass) {
            this.retryCondition = exceptionClass::isInstance;
            return this;
        }

        @SafeVarargs
        public final Builder retryOn(Class<? extends Exception>... exceptionClasses) {
            this.retryCondition = exception -> {
                for (Class<? extends Exception> clazz : exceptionClasses) {
                    if (clazz.isInstance(exception)) {
                        return true;
                    }
                }
                return false;
            };
            return this;
        }

        public Builder dontRetryOn(Class<? extends Exception> exceptionClass) {
            this.retryCondition = exception -> !exceptionClass.isInstance(exception);
            return this;
        }

        public RetryPolicy build() {
            return new RetryPolicy(this);
        }
    }

    @Override
    public String toString() {
        return String.format("RetryPolicy{maxAttempts=%d, baseDelay=%s, maxDelay=%s, backoffMultiplier=%.1f, jitter=%s}", 
                           maxAttempts, baseDelay, maxDelay, backoffMultiplier, jitterEnabled);
    }

    // Predefined policies for common scenarios
    public static final RetryPolicy NO_RETRY = new Builder().maxAttempts(1).build();
    
    public static final RetryPolicy DEFAULT = new Builder().build();
    
    public static final RetryPolicy AGGRESSIVE = new Builder()
        .maxAttempts(5)
        .baseDelay(Duration.ofMillis(500))
        .backoffMultiplier(1.5)
        .build();
    
    public static final RetryPolicy CONSERVATIVE = new Builder()
        .maxAttempts(2)
        .baseDelay(Duration.ofSeconds(2))
        .backoffMultiplier(3.0)
        .build();
}