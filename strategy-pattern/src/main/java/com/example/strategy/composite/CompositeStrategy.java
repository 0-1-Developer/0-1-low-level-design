package com.example.strategy.composite;

/**
 * Base interface for composite strategy pattern.
 * Allows strategies to be composed together to form complex processing pipelines.
 *
 * @param <T> Input type for the strategy
 * @param <R> Return type for the strategy
 */
@FunctionalInterface
public interface CompositeStrategy<T, R> {
    /**
     * Executes the strategy with the given input and context.
     *
     * @param input The input data for the strategy
     * @param context The execution context containing shared data and configuration
     * @return The result of executing the strategy
     */
    R execute(T input, ExecutionContext context);
}