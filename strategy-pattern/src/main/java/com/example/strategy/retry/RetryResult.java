package com.example.strategy.retry;

import java.util.List;
import java.util.Optional;

/**
 * Result object that contains information about retry attempts and final outcome.
 * Provides detailed information about what happened during retry execution.
 *
 * @param <T> The type of the successful result
 */
public class RetryResult<T> {
    private final T result;
    private final boolean success;
    private final int totalAttempts;
    private final long totalExecutionTimeMs;
    private final List<Exception> attemptExceptions;
    private final Exception finalException;
    private final boolean fallbackUsed;
    private final String strategyName;

    private RetryResult(Builder<T> builder) {
        this.result = builder.result;
        this.success = builder.success;
        this.totalAttempts = builder.totalAttempts;
        this.totalExecutionTimeMs = builder.totalExecutionTimeMs;
        this.attemptExceptions = builder.attemptExceptions;
        this.finalException = builder.finalException;
        this.fallbackUsed = builder.fallbackUsed;
        this.strategyName = builder.strategyName;
    }

    public Optional<T> getResult() {
        return Optional.ofNullable(result);
    }

    public boolean isSuccess() {
        return success;
    }

    public int getTotalAttempts() {
        return totalAttempts;
    }

    public long getTotalExecutionTimeMs() {
        return totalExecutionTimeMs;
    }

    public List<Exception> getAttemptExceptions() {
        return attemptExceptions;
    }

    public Optional<Exception> getFinalException() {
        return Optional.ofNullable(finalException);
    }

    public boolean isFallbackUsed() {
        return fallbackUsed;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public boolean hadRetries() {
        return totalAttempts > 1;
    }

    public int getFailedAttempts() {
        return success ? totalAttempts - 1 : totalAttempts;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RetryResult{");
        sb.append("success=").append(success);
        sb.append(", attempts=").append(totalAttempts);
        sb.append(", executionTime=").append(totalExecutionTimeMs).append("ms");
        
        if (fallbackUsed) {
            sb.append(", fallbackUsed=true");
        }
        
        if (strategyName != null) {
            sb.append(", strategy='").append(strategyName).append("'");
        }
        
        if (!success && finalException != null) {
            sb.append(", finalError='").append(finalException.getMessage()).append("'");
        }
        
        sb.append("}");
        return sb.toString();
    }

    public static class Builder<T> {
        private T result;
        private boolean success;
        private int totalAttempts;
        private long totalExecutionTimeMs;
        private List<Exception> attemptExceptions;
        private Exception finalException;
        private boolean fallbackUsed;
        private String strategyName;

        public Builder<T> result(T result) {
            this.result = result;
            this.success = true;
            return this;
        }

        public Builder<T> failure(Exception finalException) {
            this.success = false;
            this.finalException = finalException;
            return this;
        }

        public Builder<T> totalAttempts(int totalAttempts) {
            this.totalAttempts = totalAttempts;
            return this;
        }

        public Builder<T> totalExecutionTime(long totalExecutionTimeMs) {
            this.totalExecutionTimeMs = totalExecutionTimeMs;
            return this;
        }

        public Builder<T> attemptExceptions(List<Exception> attemptExceptions) {
            this.attemptExceptions = attemptExceptions;
            return this;
        }

        public Builder<T> fallbackUsed(boolean fallbackUsed) {
            this.fallbackUsed = fallbackUsed;
            return this;
        }

        public Builder<T> strategyName(String strategyName) {
            this.strategyName = strategyName;
            return this;
        }

        public RetryResult<T> build() {
            return new RetryResult<>(this);
        }
    }
}