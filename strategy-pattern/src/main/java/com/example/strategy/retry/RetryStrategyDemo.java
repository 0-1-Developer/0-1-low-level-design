package com.example.strategy.retry;

import java.time.Duration;

public class RetryStrategyDemo {
    public static void main(String[] args) {
        System.out.println("=== Retry/Fallback Strategy Pattern Demo ===\n");
        
        // Create sample network requests
        NetworkRequest apiRequest = new NetworkRequest(
            "https://api.example.com/data", 
            "GET", 
            null, 
            5000, 
            "REQ-001"
        );
        
        NetworkRequest criticalRequest = new NetworkRequest(
            "https://critical.example.com/transaction", 
            "POST", 
            "{\"amount\":1000,\"currency\":\"USD\"}", 
            3000, 
            "REQ-002"
        );
        
        System.out.println("Sample Requests:");
        System.out.println("  API Request: " + apiRequest);
        System.out.println("  Critical Request: " + criticalRequest);
        System.out.println();
        
        // Initialize the retry executor
        RetryableStrategyExecutor<NetworkRequest, NetworkResponse> executor = 
            new RetryableStrategyExecutor<>();
        
        // 1. Basic retry configuration
        System.out.println("1. Basic Retry Configuration:");
        System.out.println("-----------------------------");
        
        // Register strategies with different retry policies
        RetryPolicy basicRetry = new RetryPolicy.Builder()
            .maxAttempts(3)
            .baseDelay(Duration.ofMillis(500))
            .backoffMultiplier(2.0)
            .build();
        
        executor.registerStrategy("primary", NetworkStrategies.PRIMARY_HTTP_CLIENT, basicRetry);
        
        RetryPolicy aggressiveRetry = new RetryPolicy.Builder()
            .maxAttempts(5)
            .baseDelay(Duration.ofMillis(200))
            .backoffMultiplier(1.5)
            .enableJitter(true)
            .build();
        
        executor.registerStrategy("secondary", NetworkStrategies.SECONDARY_HTTP_CLIENT, aggressiveRetry);
        
        System.out.println("Registered strategies with retry policies");
        System.out.println(executor.getStatistics());
        
        // 2. Execute with basic retry
        System.out.println("2. Basic Retry Execution:");
        System.out.println("-------------------------");
        
        RetryResult<NetworkResponse> primaryResult = executor.execute("primary", apiRequest);
        System.out.println("Primary Strategy Result: " + primaryResult);
        if (primaryResult.isSuccess()) {
            System.out.println("  Response: " + primaryResult.getResult().get());
        } else {
            System.out.println("  Failed after " + primaryResult.getTotalAttempts() + " attempts");
            primaryResult.getFinalException().ifPresent(e -> 
                System.out.println("  Final error: " + e.getMessage()));
        }
        System.out.println();
        
        // 3. Retry with fallback strategies
        System.out.println("3. Retry with Fallback Strategies:");
        System.out.println("----------------------------------");
        
        // Add fallback to primary strategy
        executor.setFallbackStrategy("primary", NetworkStrategies.CACHE_FALLBACK);
        
        // Register unreliable strategy with fallback
        RetryPolicy conservativeRetry = new RetryPolicy.Builder()
            .maxAttempts(2)
            .baseDelay(Duration.ofMillis(1000))
            .retryOn(NetworkStrategies.TimeoutException.class, NetworkStrategies.ConnectionException.class)
            .build();
        
        executor.registerStrategy("unreliable", NetworkStrategies.UNRELIABLE_SERVICE, 
                                conservativeRetry, NetworkStrategies.DEFAULT_RESPONSE_FALLBACK);
        
        RetryResult<NetworkResponse> unreliableResult = executor.execute("unreliable", apiRequest);
        System.out.println("Unreliable Strategy Result: " + unreliableResult);
        if (unreliableResult.isSuccess()) {
            System.out.println("  Response: " + unreliableResult.getResult().get());
            if (unreliableResult.isFallbackUsed()) {
                System.out.println("  ✓ Fallback was used successfully");
            }
        }
        System.out.println();
        
        // 4. Conditional retry policies
        System.out.println("4. Conditional Retry Policies:");
        System.out.println("------------------------------");
        
        // Create retry policy that only retries on specific exceptions
        RetryPolicy selectiveRetry = new RetryPolicy.Builder()
            .maxAttempts(4)
            .baseDelay(Duration.ofMillis(300))
            .retryOn(NetworkStrategies.TimeoutException.class, NetworkStrategies.ConnectionException.class)
            .dontRetryOn(NetworkStrategies.ServerException.class) // Don't retry server errors
            .build();
        
        executor.registerStrategy("selective", NetworkStrategies.PRIMARY_HTTP_CLIENT, selectiveRetry);
        
        System.out.println("Testing selective retry policy (retries timeouts/connections, not server errors):");
        for (int i = 0; i < 3; i++) {
            RetryResult<NetworkResponse> selectiveResult = executor.execute("selective", 
                new NetworkRequest("https://test.com/endpoint" + i, "GET", null, 2000, "SEL-" + i));
            System.out.println("  Attempt " + (i + 1) + ": " + selectiveResult);
        }
        System.out.println();
        
        // 5. Race execution (parallel strategies)
        System.out.println("5. Race Execution (First Successful Wins):");
        System.out.println("------------------------------------------");
        
        // Register fast cache strategy
        executor.registerStrategy("cache", NetworkStrategies.CACHED_RESPONSE, RetryPolicy.NO_RETRY);
        
        long raceStart = System.currentTimeMillis();
        RetryResult<NetworkResponse> raceResult = executor.executeRace(apiRequest, "cache", "primary", "secondary");
        long raceTime = System.currentTimeMillis() - raceStart;
        
        System.out.println("Race Result (completed in " + raceTime + "ms): " + raceResult);
        if (raceResult.isSuccess()) {
            NetworkResponse response = raceResult.getResult().get();
            System.out.println("  Winner: " + response.getServerName() + " (from cache: " + response.isFromCache() + ")");
        }
        System.out.println();
        
        // 6. Fallback chain execution
        System.out.println("6. Fallback Chain Execution:");
        System.out.println("----------------------------");
        
        // Register database fallback and mock service
        executor.registerStrategy("database", NetworkStrategies.DATABASE_FALLBACK, RetryPolicy.CONSERVATIVE);
        executor.registerStrategy("mock", NetworkStrategies.MOCK_SERVICE, RetryPolicy.NO_RETRY);
        
        System.out.println("Executing fallback chain: unreliable -> database -> mock");
        RetryResult<NetworkResponse> chainResult = executor.executeFallbackChain(
            criticalRequest, "unreliable", "database", "mock");
        
        System.out.println("Fallback Chain Result: " + chainResult);
        if (chainResult.isSuccess()) {
            System.out.println("  Final Response: " + chainResult.getResult().get());
        }
        System.out.println();
        
        // 7. Custom configurable strategies
        System.out.println("7. Custom Configurable Strategies:");
        System.out.println("----------------------------------");
        
        // Create strategies with different failure rates
        RetryableStrategy<NetworkRequest, NetworkResponse> lowFailureStrategy = 
            NetworkStrategies.createConfigurableStrategy("low-failure-server", 0.1, 100, 300);
        
        RetryableStrategy<NetworkRequest, NetworkResponse> mediumFailureStrategy = 
            NetworkStrategies.createConfigurableStrategy("medium-failure-server", 0.5, 200, 500);
        
        executor.registerStrategy("low-failure", lowFailureStrategy, RetryPolicy.DEFAULT);
        executor.registerStrategy("medium-failure", mediumFailureStrategy, RetryPolicy.AGGRESSIVE);
        
        System.out.println("Testing custom strategies with different failure rates:");
        
        RetryResult<NetworkResponse> lowFailureResult = executor.execute("low-failure", apiRequest);
        System.out.println("  Low Failure (10% rate): " + lowFailureResult);
        
        RetryResult<NetworkResponse> mediumFailureResult = executor.execute("medium-failure", apiRequest);
        System.out.println("  Medium Failure (50% rate): " + mediumFailureResult);
        System.out.println();
        
        // 8. Performance comparison
        System.out.println("8. Performance and Reliability Comparison:");
        System.out.println("-----------------------------------------");
        
        String[] strategies = {"cache", "primary", "secondary", "database", "mock"};
        int totalTests = 5;
        
        System.out.printf("%-15s %-10s %-15s %-15s %-10s%n", 
                         "Strategy", "Success", "Avg Time (ms)", "Avg Attempts", "Fallback Used");
        System.out.println("--------------------------------------------------------------------------------");
        
        for (String strategy : strategies) {
            int successCount = 0;
            long totalTime = 0;
            int totalAttempts = 0;
            int fallbackUsed = 0;
            
            for (int i = 0; i < totalTests; i++) {
                NetworkRequest testRequest = new NetworkRequest(
                    "https://test.com/perf/" + i, "GET", null, 3000, "PERF-" + strategy + "-" + i);
                
                RetryResult<NetworkResponse> result = executor.execute(strategy, testRequest);
                
                if (result.isSuccess()) successCount++;
                totalTime += result.getTotalExecutionTimeMs();
                totalAttempts += result.getTotalAttempts();
                if (result.isFallbackUsed()) fallbackUsed++;
            }
            
            double successRate = (successCount * 100.0) / totalTests;
            double avgTime = totalTime / (double) totalTests;
            double avgAttempts = totalAttempts / (double) totalTests;
            
            System.out.printf("%-15s %-9.1f%% %-15.1f %-15.1f %-10d%n", 
                             strategy, successRate, avgTime, avgAttempts, fallbackUsed);
        }
        System.out.println();
        
        // 9. Exception handling and retry limits
        System.out.println("9. Exception Handling and Retry Limits:");
        System.out.println("---------------------------------------");
        
        // Test with no retry policy
        executor.registerStrategy("no-retry", NetworkStrategies.UNRELIABLE_SERVICE, RetryPolicy.NO_RETRY);
        
        RetryResult<NetworkResponse> noRetryResult = executor.execute("no-retry", apiRequest);
        System.out.println("No Retry Result: " + noRetryResult);
        
        // Test with maximum retry policy
        RetryPolicy maxRetry = new RetryPolicy.Builder()
            .maxAttempts(10)
            .baseDelay(Duration.ofMillis(100))
            .maxDelay(Duration.ofSeconds(2))
            .backoffMultiplier(1.2)
            .build();
        
        executor.registerStrategy("max-retry", NetworkStrategies.UNRELIABLE_SERVICE, maxRetry, 
                                NetworkStrategies.ERROR_RESPONSE_FALLBACK);
        
        RetryResult<NetworkResponse> maxRetryResult = executor.execute("max-retry", apiRequest);
        System.out.println("Max Retry Result: " + maxRetryResult);
        System.out.println("  Total execution time: " + maxRetryResult.getTotalExecutionTimeMs() + "ms");
        System.out.println("  Exception count: " + maxRetryResult.getAttemptExceptions().size());
        System.out.println();
        
        // Final statistics
        System.out.println("Final Executor Statistics:");
        System.out.println("=========================");
        System.out.println(executor.getStatistics());
        
        System.out.println("\n=== Demo Complete ===");
    }
}