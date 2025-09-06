package com.example.strategy.retry;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Collection of network-related strategies for demonstrating retry and fallback patterns.
 * These strategies simulate real network operations with various failure scenarios.
 */
public class NetworkStrategies {

    /**
     * Exception types for different failure scenarios
     */
    public static class NetworkException extends Exception {
        public NetworkException(String message) { super(message); }
    }

    public static class TimeoutException extends NetworkException {
        public TimeoutException(String message) { super(message); }
    }

    public static class ConnectionException extends NetworkException {
        public ConnectionException(String message) { super(message); }
    }

    public static class ServerException extends NetworkException {
        public ServerException(String message) { super(message); }
    }

    /**
     * Primary HTTP client strategy - may fail randomly
     */
    public static final RetryableStrategy<NetworkRequest, NetworkResponse> PRIMARY_HTTP_CLIENT = request -> {
        Thread.sleep(ThreadLocalRandom.current().nextInt(100, 300));
        
        // Simulate various failure scenarios (40% chance of failure)
        double failureRate = 0.4;
        if (ThreadLocalRandom.current().nextDouble() < failureRate) {
            int failureType = ThreadLocalRandom.current().nextInt(4);
            switch (failureType) {
                case 0:
                    throw new TimeoutException("Request timeout after " + request.getTimeoutMs() + "ms");
                case 1:
                    throw new ConnectionException("Connection refused to " + request.getUrl());
                case 2:
                    throw new ServerException("Internal server error (500)");
                default:
                    throw new NetworkException("Unknown network error");
            }
        }
        
        // Success case
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Server", "primary-server");
        
        return new NetworkResponse(200, "{\"status\":\"success\",\"data\":\"response from primary\"}", 
                                 headers, ThreadLocalRandom.current().nextLong(50, 200), 
                                 request.getRequestId(), false, "primary-server");
    };

    /**
     * Secondary HTTP client strategy - more reliable but slower
     */
    public static final RetryableStrategy<NetworkRequest, NetworkResponse> SECONDARY_HTTP_CLIENT = request -> {
        Thread.sleep(ThreadLocalRandom.current().nextInt(300, 600)); // Slower but more reliable
        
        // Lower failure rate (15%)
        if (ThreadLocalRandom.current().nextDouble() < 0.15) {
            throw new TimeoutException("Secondary server timeout");
        }
        
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Server", "secondary-server");
        
        return new NetworkResponse(200, "{\"status\":\"success\",\"data\":\"response from secondary\"}", 
                                 headers, ThreadLocalRandom.current().nextLong(300, 600), 
                                 request.getRequestId(), false, "secondary-server");
    };

    /**
     * Cached response strategy - very fast but may have stale data
     */
    public static final RetryableStrategy<NetworkRequest, NetworkResponse> CACHED_RESPONSE = request -> {
        Thread.sleep(10); // Very fast cache lookup
        
        // Simulate cache miss (20% chance)
        if (ThreadLocalRandom.current().nextDouble() < 0.2) {
            throw new NetworkException("Cache miss for " + request.getUrl());
        }
        
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Server", "cache-server");
        headers.put("Cache-Control", "max-age=3600");
        
        return new NetworkResponse(200, "{\"status\":\"success\",\"data\":\"cached response\",\"cached\":true}", 
                                 headers, 10, request.getRequestId(), true, "cache-server");
    };

    /**
     * Database fallback strategy - queries local database when network fails
     */
    public static final RetryableStrategy<NetworkRequest, NetworkResponse> DATABASE_FALLBACK = request -> {
        Thread.sleep(ThreadLocalRandom.current().nextInt(50, 150));
        
        // Database queries are generally reliable (5% failure rate)
        if (ThreadLocalRandom.current().nextDouble() < 0.05) {
            throw new NetworkException("Database connection failed");
        }
        
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Server", "database-server");
        
        return new NetworkResponse(200, "{\"status\":\"success\",\"data\":\"database fallback response\"}", 
                                 headers, ThreadLocalRandom.current().nextLong(50, 150), 
                                 request.getRequestId(), false, "database-server");
    };

    /**
     * Mock service strategy - always succeeds but returns mock data
     */
    public static final RetryableStrategy<NetworkRequest, NetworkResponse> MOCK_SERVICE = request -> {
        Thread.sleep(5); // Instant response
        
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Server", "mock-server");
        
        return new NetworkResponse(200, "{\"status\":\"success\",\"data\":\"mock response\",\"mock\":true}", 
                                 headers, 5, request.getRequestId(), false, "mock-server");
    };

    /**
     * Unreliable strategy that fails most of the time
     */
    public static final RetryableStrategy<NetworkRequest, NetworkResponse> UNRELIABLE_SERVICE = request -> {
        Thread.sleep(ThreadLocalRandom.current().nextInt(200, 800));
        
        // High failure rate (80%)
        if (ThreadLocalRandom.current().nextDouble() < 0.8) {
            int failureType = ThreadLocalRandom.current().nextInt(3);
            switch (failureType) {
                case 0:
                    throw new TimeoutException("Unreliable service timeout");
                case 1:
                    throw new ServerException("Unreliable service error (503)");
                default:
                    throw new ConnectionException("Unreliable service connection failed");
            }
        }
        
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Server", "unreliable-server");
        
        return new NetworkResponse(200, "{\"status\":\"success\",\"data\":\"unreliable service response\"}", 
                                 headers, ThreadLocalRandom.current().nextLong(200, 800), 
                                 request.getRequestId(), false, "unreliable-server");
    };

    /**
     * Fallback strategies for different scenarios
     */
    public static final FallbackStrategy<NetworkRequest, NetworkResponse> CACHE_FALLBACK = (request, lastException) -> {
        System.out.println("Using cache fallback due to: " + lastException.getMessage());
        
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Server", "fallback-cache");
        headers.put("X-Fallback", "true");
        
        return new NetworkResponse(200, "{\"status\":\"fallback\",\"data\":\"cached fallback response\"}", 
                                 headers, 5, request.getRequestId(), true, "fallback-cache");
    };

    public static final FallbackStrategy<NetworkRequest, NetworkResponse> DEFAULT_RESPONSE_FALLBACK = (request, lastException) -> {
        System.out.println("Using default response fallback due to: " + lastException.getMessage());
        
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Server", "fallback-default");
        headers.put("X-Fallback", "true");
        
        return new NetworkResponse(200, "{\"status\":\"fallback\",\"data\":\"default response\",\"error\":\"" + lastException.getMessage() + "\"}", 
                                 headers, 1, request.getRequestId(), false, "fallback-default");
    };

    public static final FallbackStrategy<NetworkRequest, NetworkResponse> ERROR_RESPONSE_FALLBACK = (request, lastException) -> {
        System.out.println("Using error response fallback due to: " + lastException.getMessage());
        
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Server", "fallback-error");
        headers.put("X-Fallback", "true");
        
        return new NetworkResponse(503, "{\"status\":\"error\",\"message\":\"Service temporarily unavailable: " + lastException.getMessage() + "\"}", 
                                 headers, 1, request.getRequestId(), false, "fallback-error");
    };

    /**
     * Factory method to create a strategy with configurable failure rate
     */
    public static RetryableStrategy<NetworkRequest, NetworkResponse> createConfigurableStrategy(
            String serverName, double failureRate, int minDelayMs, int maxDelayMs) {
        return request -> {
            Thread.sleep(ThreadLocalRandom.current().nextInt(minDelayMs, maxDelayMs));
            
            if (ThreadLocalRandom.current().nextDouble() < failureRate) {
                throw new NetworkException("Configured failure for " + serverName);
            }
            
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("Server", serverName);
            
            return new NetworkResponse(200, "{\"status\":\"success\",\"data\":\"response from " + serverName + "\"}", 
                                     headers, ThreadLocalRandom.current().nextLong(minDelayMs, maxDelayMs), 
                                     request.getRequestId(), false, serverName);
        };
    }
}