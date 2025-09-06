package com.example.strategy.config;

/**
 * Strategy interface that accepts configuration parameters.
 * Allows strategies to be configured externally without modifying their implementation.
 *
 * @param <T> Input type for the strategy
 * @param <R> Return type for the strategy
 * @param <C> Configuration type for the strategy
 */
@FunctionalInterface
public interface ConfigurableStrategy<T, R, C> {
    /**
     * Executes the strategy with the given input and configuration.
     *
     * @param input The input data for the strategy
     * @param config The configuration parameters for the strategy
     * @return The result of executing the strategy
     */
    R execute(T input, C config);
}