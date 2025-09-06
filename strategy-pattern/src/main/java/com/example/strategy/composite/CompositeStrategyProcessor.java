package com.example.strategy.composite;

import java.util.*;
import java.util.function.Predicate;

/**
 * Processor that can execute multiple strategies in various compositions.
 * Supports sequential execution, parallel execution, conditional execution, and more.
 *
 * @param <T> Input type
 * @param <R> Result type
 */
public class CompositeStrategyProcessor<T, R> {
    
    /**
     * Executes strategies in sequence, passing the result of each to the next.
     * The output of one strategy becomes the input of the next.
     */
    @SafeVarargs
    public static <T> T executeSequential(T input, ExecutionContext context, 
                                         CompositeStrategy<T, T>... strategies) {
        T currentInput = input;
        context.setMetadata("execution_mode", "sequential");
        context.put("step_count", 0);
        
        for (int i = 0; i < strategies.length; i++) {
            context.put("current_step", i + 1);
            context.put("step_count", i + 1);
            currentInput = strategies[i].execute(currentInput, context);
        }
        
        return currentInput;
    }
    
    /**
     * Executes strategies in parallel and returns all results.
     * Each strategy receives the same input.
     */
    @SafeVarargs
    public static <T, R> List<R> executeParallel(T input, ExecutionContext context,
                                               CompositeStrategy<T, R>... strategies) {
        List<R> results = new ArrayList<>();
        context.setMetadata("execution_mode", "parallel");
        context.put("strategy_count", strategies.length);
        
        for (int i = 0; i < strategies.length; i++) {
            ExecutionContext strategyContext = createChildContext(context, "strategy_" + i);
            R result = strategies[i].execute(input, strategyContext);
            results.add(result);
        }
        
        return results;
    }
    
    /**
     * Executes strategies conditionally based on predicates.
     * Only strategies whose conditions are met will be executed.
     */
    public static <T, R> List<R> executeConditional(T input, ExecutionContext context,
                                                   List<ConditionalStrategy<T, R>> conditionalStrategies) {
        List<R> results = new ArrayList<>();
        context.setMetadata("execution_mode", "conditional");
        context.put("total_conditions", conditionalStrategies.size());
        
        int executedCount = 0;
        for (int i = 0; i < conditionalStrategies.size(); i++) {
            ConditionalStrategy<T, R> conditionalStrategy = conditionalStrategies.get(i);
            
            if (conditionalStrategy.condition.test(input)) {
                ExecutionContext strategyContext = createChildContext(context, "conditional_" + i);
                R result = conditionalStrategy.strategy.execute(input, strategyContext);
                results.add(result);
                executedCount++;
            }
        }
        
        context.put("executed_count", executedCount);
        return results;
    }
    
    /**
     * Executes strategies until one succeeds (returns non-null result) or all fail.
     * Useful for fallback scenarios.
     */
    @SafeVarargs
    public static <T, R> Optional<R> executeUntilSuccess(T input, ExecutionContext context,
                                                       CompositeStrategy<T, R>... strategies) {
        context.setMetadata("execution_mode", "until_success");
        context.put("total_strategies", strategies.length);
        
        for (int i = 0; i < strategies.length; i++) {
            try {
                ExecutionContext strategyContext = createChildContext(context, "attempt_" + i);
                R result = strategies[i].execute(input, strategyContext);
                
                if (result != null) {
                    context.put("successful_strategy_index", i);
                    context.put("attempts_made", i + 1);
                    return Optional.of(result);
                }
            } catch (Exception e) {
                context.put("error_at_step_" + i, e.getMessage());
                // Continue to next strategy
            }
        }
        
        context.put("attempts_made", strategies.length);
        context.put("all_failed", true);
        return Optional.empty();
    }
    
    /**
     * Executes strategies based on priority order and stops when a threshold is met.
     */
    public static <T, R> List<R> executeWithPriority(T input, ExecutionContext context,
                                                    List<PriorityStrategy<T, R>> priorityStrategies,
                                                    int maxExecutions) {
        // Sort by priority (higher number = higher priority)
        priorityStrategies.sort((a, b) -> Integer.compare(b.priority, a.priority));
        
        List<R> results = new ArrayList<>();
        context.setMetadata("execution_mode", "priority");
        context.put("max_executions", maxExecutions);
        
        int executed = 0;
        for (PriorityStrategy<T, R> priorityStrategy : priorityStrategies) {
            if (executed >= maxExecutions) {
                break;
            }
            
            ExecutionContext strategyContext = createChildContext(context, "priority_" + priorityStrategy.priority);
            R result = priorityStrategy.strategy.execute(input, strategyContext);
            results.add(result);
            executed++;
        }
        
        context.put("strategies_executed", executed);
        return results;
    }
    
    /**
     * Creates a composite strategy from multiple strategies that vote on the result.
     * The result with the most votes is returned.
     */
    @SafeVarargs
    public static <T, R> R executeWithVoting(T input, ExecutionContext context,
                                           CompositeStrategy<T, R>... strategies) {
        Map<R, Integer> votes = new HashMap<>();
        context.setMetadata("execution_mode", "voting");
        context.put("voter_count", strategies.length);
        
        for (int i = 0; i < strategies.length; i++) {
            ExecutionContext voterContext = createChildContext(context, "voter_" + i);
            R result = strategies[i].execute(input, voterContext);
            votes.put(result, votes.getOrDefault(result, 0) + 1);
        }
        
        // Find result with most votes
        R winningResult = null;
        int maxVotes = 0;
        
        for (Map.Entry<R, Integer> entry : votes.entrySet()) {
            if (entry.getValue() > maxVotes) {
                maxVotes = entry.getValue();
                winningResult = entry.getKey();
            }
        }
        
        context.put("winning_votes", maxVotes);
        context.put("total_unique_results", votes.size());
        
        return winningResult;
    }
    
    /**
     * Creates a child execution context with a specific prefix
     */
    private static ExecutionContext createChildContext(ExecutionContext parent, String prefix) {
        ExecutionContext child = new ExecutionContext(parent.snapshot());
        child.setMetadata("parent_context", "true");
        child.setMetadata("context_prefix", prefix);
        return child;
    }
    
    /**
     * Helper class for conditional strategy execution
     */
    public static class ConditionalStrategy<T, R> {
        public final Predicate<T> condition;
        public final CompositeStrategy<T, R> strategy;
        
        public ConditionalStrategy(Predicate<T> condition, CompositeStrategy<T, R> strategy) {
            this.condition = condition;
            this.strategy = strategy;
        }
    }
    
    /**
     * Helper class for priority-based strategy execution
     */
    public static class PriorityStrategy<T, R> {
        public final int priority;
        public final CompositeStrategy<T, R> strategy;
        
        public PriorityStrategy(int priority, CompositeStrategy<T, R> strategy) {
            this.priority = priority;
            this.strategy = strategy;
        }
    }
}