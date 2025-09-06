package com.example.strategy.retry;

/**
 * Strategy interface that can potentially fail and may need retry logic.
 * This is the base interface for strategies that might throw exceptions or return failure indicators.
 *
 * @param <T> Input type for the strategy
 * @param <R> Return type for the strategy
 */
@FunctionalInterface
public interface RetryableStrategy<T, R> {
    /**
     * Executes the strategy with the given input.
     * May throw exceptions that trigger retry logic.
     *
     * @param input The input data for the strategy
     * @return The result of executing the strategy
     * @throws Exception If the strategy execution fails
     */
    R execute(T input) throws Exception;
}