package com.example.strategy.retry;

import java.util.Map;

/**
 * Represents a network response from retry strategies.
 * Contains response details and metadata about the request execution.
 */
public class NetworkResponse {
    private final int statusCode;
    private final String body;
    private final Map<String, String> headers;
    private final long responseTimeMs;
    private final String requestId;
    private final boolean fromCache;
    private final String serverName;

    public NetworkResponse(int statusCode, String body, Map<String, String> headers, 
                          long responseTimeMs, String requestId, boolean fromCache, String serverName) {
        this.statusCode = statusCode;
        this.body = body;
        this.headers = headers;
        this.responseTimeMs = responseTimeMs;
        this.requestId = requestId;
        this.fromCache = fromCache;
        this.serverName = serverName;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public long getResponseTimeMs() {
        return responseTimeMs;
    }

    public String getRequestId() {
        return requestId;
    }

    public boolean isFromCache() {
        return fromCache;
    }

    public String getServerName() {
        return serverName;
    }

    public boolean isSuccessful() {
        return statusCode >= 200 && statusCode < 300;
    }

    public boolean isClientError() {
        return statusCode >= 400 && statusCode < 500;
    }

    public boolean isServerError() {
        return statusCode >= 500;
    }

    @Override
    public String toString() {
        return String.format("NetworkResponse{id='%s', status=%d, responseTime=%dms, server='%s', fromCache=%s}", 
                           requestId, statusCode, responseTimeMs, serverName, fromCache);
    }
}