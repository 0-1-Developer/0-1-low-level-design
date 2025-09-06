package com.example.strategy.generic;

/**
 * Generic Strategy interface that provides type safety for input and output types.
 * This allows for compile-time type checking and eliminates the need for casting.
 *
 * @param <T> Input type for the strategy
 * @param <R> Return type for the strategy
 */
@FunctionalInterface
public interface Strategy<T, R> {
    /**
     * Executes the strategy with the given input.
     *
     * @param input The input data for the strategy
     * @return The result of executing the strategy
     */
    R execute(T input);
}