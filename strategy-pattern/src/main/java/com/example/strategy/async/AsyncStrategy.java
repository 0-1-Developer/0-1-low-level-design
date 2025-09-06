package com.example.strategy.async;

import java.util.concurrent.CompletableFuture;

/**
 * Asynchronous strategy interface that returns a CompletableFuture for non-blocking operations.
 * This is useful for I/O operations, network calls, or any long-running computations.
 *
 * @param <T> Input type for the strategy
 * @param <R> Return type for the strategy
 */
@FunctionalInterface
public interface AsyncStrategy<T, R> {
    /**
     * Executes the strategy asynchronously with the given input.
     *
     * @param input The input data for the strategy
     * @return A CompletableFuture containing the result of the strategy execution
     */
    CompletableFuture<R> executeAsync(T input);
}