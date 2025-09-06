package com.example.strategy.retry;

/**
 * Represents a network request for demonstrating retry strategies.
 * Contains request details that might be used in retry scenarios.
 */
public class NetworkRequest {
    private final String url;
    private final String method;
    private final String payload;
    private final int timeoutMs;
    private final String requestId;

    public NetworkRequest(String url, String method, String payload, int timeoutMs, String requestId) {
        this.url = url;
        this.method = method;
        this.payload = payload;
        this.timeoutMs = timeoutMs;
        this.requestId = requestId;
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public String getPayload() {
        return payload;
    }

    public int getTimeoutMs() {
        return timeoutMs;
    }

    public String getRequestId() {
        return requestId;
    }

    @Override
    public String toString() {
        return String.format("NetworkRequest{id='%s', method='%s', url='%s', timeout=%dms}", 
                           requestId, method, url, timeoutMs);
    }
}