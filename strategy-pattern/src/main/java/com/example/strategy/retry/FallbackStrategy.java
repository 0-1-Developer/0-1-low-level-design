package com.example.strategy.retry;

/**
 * Strategy interface for fallback operations when primary strategies fail.
 * Fallback strategies should be more reliable and simpler than primary strategies.
 *
 * @param <T> Input type for the strategy
 * @param <R> Return type for the strategy
 */
@FunctionalInterface
public interface FallbackStrategy<T, R> {
    /**
     * Executes the fallback strategy.
     * This should be a reliable operation that rarely fails.
     *
     * @param input The original input data
     * @param lastException The exception that caused the primary strategy to fail
     * @return The fallback result
     */
    R executeFallback(T input, Exception lastException);
}