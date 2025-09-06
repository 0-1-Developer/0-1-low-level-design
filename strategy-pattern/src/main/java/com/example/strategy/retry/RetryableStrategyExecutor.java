package com.example.strategy.retry;

import java.util.*;

/**
 * Executor that handles retry logic and fallback strategies for retryable strategies.
 * Provides comprehensive retry mechanisms with detailed result tracking.
 *
 * @param <T> Input type
 * @param <R> Result type
 */
public class RetryableStrategyExecutor<T, R> {
    private final Map<String, RetryableStrategy<T, R>> strategies = new HashMap<>();
    private final Map<String, RetryPolicy> retryPolicies = new HashMap<>();
    private final Map<String, FallbackStrategy<T, R>> fallbackStrategies = new HashMap<>();
    private String activeStrategy;
    private RetryPolicy defaultRetryPolicy = RetryPolicy.DEFAULT;

    /**
     * Registers a strategy with a name
     */
    public void registerStrategy(String name, RetryableStrategy<T, R> strategy) {
        strategies.put(name, strategy);
        if (activeStrategy == null) {
            activeStrategy = name;
        }
    }

    /**
     * Registers a strategy with custom retry policy
     */
    public void registerStrategy(String name, RetryableStrategy<T, R> strategy, RetryPolicy retryPolicy) {
        registerStrategy(name, strategy);
        retryPolicies.put(name, retryPolicy);
    }

    /**
     * Registers a strategy with retry policy and fallback
     */
    public void registerStrategy(String name, RetryableStrategy<T, R> strategy, 
                               RetryPolicy retryPolicy, FallbackStrategy<T, R> fallback) {
        registerStrategy(name, strategy, retryPolicy);
        fallbackStrategies.put(name, fallback);
    }

    /**
     * Sets retry policy for a specific strategy
     */
    public void setRetryPolicy(String strategyName, RetryPolicy retryPolicy) {
        retryPolicies.put(strategyName, retryPolicy);
    }

    /**
     * Sets fallback strategy for a specific strategy
     */
    public void setFallbackStrategy(String strategyName, FallbackStrategy<T, R> fallback) {
        fallbackStrategies.put(strategyName, fallback);
    }

    /**
     * Sets the default retry policy for strategies without specific policies
     */
    public void setDefaultRetryPolicy(RetryPolicy retryPolicy) {
        this.defaultRetryPolicy = retryPolicy;
    }

    /**
     * Sets the active strategy
     */
    public void setActiveStrategy(String strategyName) {
        if (!strategies.containsKey(strategyName)) {
            throw new IllegalArgumentException("Strategy not found: " + strategyName);
        }
        this.activeStrategy = strategyName;
    }

    /**
     * Executes the active strategy with retry logic
     */
    public RetryResult<R> execute(T input) {
        if (activeStrategy == null) {
            throw new IllegalStateException("No active strategy set");
        }
        return execute(activeStrategy, input);
    }

    /**
     * Executes a specific strategy with retry logic
     */
    public RetryResult<R> execute(String strategyName, T input) {
        RetryableStrategy<T, R> strategy = strategies.get(strategyName);
        if (strategy == null) {
            throw new IllegalArgumentException("Strategy not found: " + strategyName);
        }

        RetryPolicy policy = retryPolicies.getOrDefault(strategyName, defaultRetryPolicy);
        FallbackStrategy<T, R> fallback = fallbackStrategies.get(strategyName);

        return executeWithRetry(strategy, input, policy, fallback, strategyName);
    }

    /**
     * Core retry execution logic
     */
    private RetryResult<R> executeWithRetry(RetryableStrategy<T, R> strategy, T input, 
                                          RetryPolicy policy, FallbackStrategy<T, R> fallback,
                                          String strategyName) {
        long startTime = System.currentTimeMillis();
        List<Exception> attemptExceptions = new ArrayList<>();
        Exception lastException = null;

        for (int attempt = 1; attempt <= policy.getMaxAttempts(); attempt++) {
            try {
                // Add delay before retry attempts (not on first attempt)
                if (attempt > 1) {
                    sleep(policy.calculateDelay(attempt));
                }

                R result = strategy.execute(input);
                long totalTime = System.currentTimeMillis() - startTime;

                return new RetryResult.Builder<R>()
                    .result(result)
                    .totalAttempts(attempt)
                    .totalExecutionTime(totalTime)
                    .attemptExceptions(new ArrayList<>(attemptExceptions))
                    .strategyName(strategyName)
                    .build();

            } catch (Exception e) {
                lastException = e;
                attemptExceptions.add(e);

                // Check if we should retry this exception
                if (!policy.shouldRetry(e) || attempt == policy.getMaxAttempts()) {
                    break;
                }

                System.out.printf("Attempt %d failed for strategy '%s': %s. Retrying...%n", 
                                attempt, strategyName, e.getMessage());
            }
        }

        // All retries failed, try fallback if available
        if (fallback != null) {
            try {
                R fallbackResult = fallback.executeFallback(input, lastException);
                long totalTime = System.currentTimeMillis() - startTime;

                return new RetryResult.Builder<R>()
                    .result(fallbackResult)
                    .totalAttempts(policy.getMaxAttempts())
                    .totalExecutionTime(totalTime)
                    .attemptExceptions(attemptExceptions)
                    .fallbackUsed(true)
                    .strategyName(strategyName)
                    .build();

            } catch (Exception fallbackException) {
                System.out.printf("Fallback also failed for strategy '%s': %s%n", 
                                strategyName, fallbackException.getMessage());
                lastException = fallbackException;
            }
        }

        // Complete failure
        long totalTime = System.currentTimeMillis() - startTime;
        return new RetryResult.Builder<R>()
            .failure(lastException)
            .totalAttempts(policy.getMaxAttempts())
            .totalExecutionTime(totalTime)
            .attemptExceptions(attemptExceptions)
            .fallbackUsed(fallback != null)
            .strategyName(strategyName)
            .build();
    }

    /**
     * Executes multiple strategies in parallel and returns the first successful result
     */
    public RetryResult<R> executeRace(T input, String... strategyNames) {
        if (strategyNames.length == 0) {
            throw new IllegalArgumentException("At least one strategy name must be provided");
        }

        List<Thread> threads = new ArrayList<>();
        List<RetryResult<R>> results = Collections.synchronizedList(new ArrayList<>());
        Object lock = new Object();

        for (String strategyName : strategyNames) {
            Thread thread = new Thread(() -> {
                RetryResult<R> result = execute(strategyName, input);
                synchronized (lock) {
                    results.add(result);
                    lock.notify();
                }
            });
            threads.add(thread);
            thread.start();
        }

        // Wait for first successful result or all to complete
        synchronized (lock) {
            while (results.isEmpty() || results.stream().noneMatch(RetryResult::isSuccess)) {
                if (results.size() == strategyNames.length) {
                    break; // All strategies have completed
                }
                try {
                    lock.wait(1000); // Wait with timeout
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        // Stop all remaining threads
        threads.forEach(Thread::interrupt);

        // Return first successful result or first result if none succeeded
        return results.stream()
            .filter(RetryResult::isSuccess)
            .findFirst()
            .orElse(results.isEmpty() ? 
                new RetryResult.Builder<R>().failure(new RuntimeException("No results")).build() : 
                results.get(0));
    }

    /**
     * Executes strategies in fallback chain until one succeeds
     */
    public RetryResult<R> executeFallbackChain(T input, String... strategyNames) {
        Exception lastException = null;
        
        for (String strategyName : strategyNames) {
            RetryResult<R> result = execute(strategyName, input);
            if (result.isSuccess()) {
                return result;
            }
            lastException = result.getFinalException().orElse(lastException);
            
            System.out.printf("Strategy '%s' failed, trying next in chain...%n", strategyName);
        }
        
        // All strategies in chain failed
        return new RetryResult.Builder<R>()
            .failure(lastException != null ? lastException : new RuntimeException("All strategies in fallback chain failed"))
            .totalAttempts(strategyNames.length)
            .strategyName("fallback_chain")
            .build();
    }

    /**
     * Gets statistics about registered strategies
     */
    public String getStatistics() {
        StringBuilder stats = new StringBuilder();
        stats.append("RetryableStrategyExecutor Statistics:\n");
        stats.append("  Registered Strategies: ").append(strategies.size()).append("\n");
        stats.append("  Active Strategy: ").append(activeStrategy).append("\n");
        stats.append("  Strategies with Retry Policies: ").append(retryPolicies.size()).append("\n");
        stats.append("  Strategies with Fallbacks: ").append(fallbackStrategies.size()).append("\n");
        stats.append("  Default Retry Policy: ").append(defaultRetryPolicy).append("\n");

        for (String name : strategies.keySet()) {
            stats.append("  - ").append(name);
            if (name.equals(activeStrategy)) {
                stats.append(" (active)");
            }
            if (retryPolicies.containsKey(name)) {
                stats.append(" [custom retry]");
            }
            if (fallbackStrategies.containsKey(name)) {
                stats.append(" [has fallback]");
            }
            stats.append("\n");
        }

        return stats.toString();
    }

    /**
     * Helper method to sleep with proper exception handling
     */
    private void sleep(java.time.Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted during retry delay", e);
        }
    }

    /**
     * Removes a strategy and its associated policies
     */
    public void removeStrategy(String strategyName) {
        strategies.remove(strategyName);
        retryPolicies.remove(strategyName);
        fallbackStrategies.remove(strategyName);
        
        if (strategyName.equals(activeStrategy)) {
            activeStrategy = strategies.keySet().stream().findFirst().orElse(null);
        }
    }

    /**
     * Clears all strategies and policies
     */
    public void clear() {
        strategies.clear();
        retryPolicies.clear();
        fallbackStrategies.clear();
        activeStrategy = null;
    }

    /**
     * Gets the names of all registered strategies
     */
    public Set<String> getRegisteredStrategies() {
        return new HashSet<>(strategies.keySet());
    }
}