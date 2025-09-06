package com.example.strategy.async;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

/**
 * Context class that processes data using asynchronous strategies.
 * Provides methods for both sequential and parallel processing.
 *
 * @param <T> Input data type
 * @param <R> Result data type
 */
public class AsyncDataProcessor<T, R> {
    private AsyncStrategy<T, R> strategy;
    private final Executor executor;

    public AsyncDataProcessor(AsyncStrategy<T, R> strategy) {
        this(strategy, ForkJoinPool.commonPool());
    }

    public AsyncDataProcessor(AsyncStrategy<T, R> strategy, Executor executor) {
        this.strategy = Objects.requireNonNull(strategy, "Strategy cannot be null");
        this.executor = Objects.requireNonNull(executor, "Executor cannot be null");
    }

    public void setStrategy(AsyncStrategy<T, R> strategy) {
        this.strategy = Objects.requireNonNull(strategy, "Strategy cannot be null");
    }

    public AsyncStrategy<T, R> getStrategy() {
        return strategy;
    }

    /**
     * Processes a single item asynchronously.
     *
     * @param input The input data to process
     * @return CompletableFuture containing the processed result
     */
    public CompletableFuture<R> processAsync(T input) {
        return strategy.executeAsync(input);
    }

    /**
     * Processes multiple items in parallel and returns when all are complete.
     *
     * @param inputs Array of input data to process
     * @return CompletableFuture containing list of results in the same order
     */
    @SafeVarargs
    public final CompletableFuture<java.util.List<R>> processAllAsync(T... inputs) {
        java.util.List<CompletableFuture<R>> futures = new java.util.ArrayList<>();
        
        for (T input : inputs) {
            futures.add(processAsync(input));
        }
        
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> {
                    java.util.List<R> results = new java.util.ArrayList<>();
                    for (CompletableFuture<R> future : futures) {
                        results.add(future.join());
                    }
                    return results;
                });
    }

    /**
     * Processes multiple items and returns the first successful result.
     *
     * @param inputs Array of input data to process
     * @return CompletableFuture containing the first successful result
     */
    @SafeVarargs
    public final CompletableFuture<R> processAnyAsync(T... inputs) {
        @SuppressWarnings("unchecked")
        CompletableFuture<R>[] futures = new CompletableFuture[inputs.length];
        
        for (int i = 0; i < inputs.length; i++) {
            futures[i] = processAsync(inputs[i]);
        }
        
        return CompletableFuture.anyOf(futures)
                .thenApply(result -> (R) result);
    }

    /**
     * Processes data with a timeout and fallback strategy.
     *
     * @param input The input data to process
     * @param timeoutMillis Timeout in milliseconds
     * @param fallbackStrategy Fallback strategy to use if timeout occurs
     * @return CompletableFuture containing the result or fallback result
     */
    public CompletableFuture<R> processWithTimeout(T input, long timeoutMillis, 
                                                  AsyncStrategy<T, R> fallbackStrategy) {
        CompletableFuture<R> primaryFuture = processAsync(input);
        CompletableFuture<R> timeoutFuture = new CompletableFuture<>();
        
        // Schedule timeout
        CompletableFuture.delayedExecutor(timeoutMillis, java.util.concurrent.TimeUnit.MILLISECONDS, executor)
                .execute(() -> {
                    if (!primaryFuture.isDone()) {
                        System.out.println("Primary strategy timed out, using fallback...");
                        fallbackStrategy.executeAsync(input)
                                .whenComplete((result, ex) -> {
                                    if (ex != null) {
                                        timeoutFuture.completeExceptionally(ex);
                                    } else {
                                        timeoutFuture.complete(result);
                                    }
                                });
                    }
                });
        
        return CompletableFuture.anyOf(primaryFuture, timeoutFuture)
                .thenApply(result -> (R) result);
    }

    /**
     * Chains multiple strategies together sequentially.
     *
     * @param input The input data
     * @param strategies Array of strategies to chain
     * @return CompletableFuture containing the final result
     */
    @SafeVarargs
    public final CompletableFuture<T> chainStrategies(T input, AsyncStrategy<T, T>... strategies) {
        CompletableFuture<T> result = CompletableFuture.completedFuture(input);
        
        for (AsyncStrategy<T, T> strategy : strategies) {
            result = result.thenCompose(strategy::executeAsync);
        }
        
        return result;
    }
}