package com.example.strategy.composite;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Execution context that carries state and configuration across strategy executions.
 * Thread-safe implementation for sharing data between composite strategies.
 */
public class ExecutionContext {
    private final Map<String, Object> data = new ConcurrentHashMap<>();
    private final Map<String, String> metadata = new ConcurrentHashMap<>();
    private final long startTime;
    
    public ExecutionContext() {
        this.startTime = System.currentTimeMillis();
    }
    
    public ExecutionContext(Map<String, Object> initialData) {
        this();
        if (initialData != null) {
            this.data.putAll(initialData);
        }
    }
    
    /**
     * Stores data in the context
     */
    public void put(String key, Object value) {
        data.put(key, value);
    }
    
    /**
     * Retrieves data from the context
     */
    @SuppressWarnings("unchecked")
    public <V> V get(String key) {
        return (V) data.get(key);
    }
    
    /**
     * Retrieves data with a default value if not present
     */
    @SuppressWarnings("unchecked")
    public <V> V get(String key, V defaultValue) {
        return (V) data.getOrDefault(key, defaultValue);
    }
    
    /**
     * Checks if a key exists in the context
     */
    public boolean contains(String key) {
        return data.containsKey(key);
    }
    
    /**
     * Removes data from the context
     */
    public Object remove(String key) {
        return data.remove(key);
    }
    
    /**
     * Clears all data from the context
     */
    public void clear() {
        data.clear();
        metadata.clear();
    }
    
    /**
     * Sets metadata (non-generic string key-value pairs)
     */
    public void setMetadata(String key, String value) {
        metadata.put(key, value);
    }
    
    /**
     * Gets metadata
     */
    public String getMetadata(String key) {
        return metadata.get(key);
    }
    
    /**
     * Gets metadata with default value
     */
    public String getMetadata(String key, String defaultValue) {
        return metadata.getOrDefault(key, defaultValue);
    }
    
    /**
     * Gets all metadata as a copy
     */
    public Map<String, String> getAllMetadata() {
        return new HashMap<>(metadata);
    }
    
    /**
     * Gets the execution start time
     */
    public long getStartTime() {
        return startTime;
    }
    
    /**
     * Gets the elapsed time since context creation
     */
    public long getElapsedTime() {
        return System.currentTimeMillis() - startTime;
    }
    
    /**
     * Gets the size of stored data
     */
    public int size() {
        return data.size();
    }
    
    /**
     * Checks if context is empty
     */
    public boolean isEmpty() {
        return data.isEmpty() && metadata.isEmpty();
    }
    
    /**
     * Creates a snapshot of current context data
     */
    public Map<String, Object> snapshot() {
        return new HashMap<>(data);
    }
    
    @Override
    public String toString() {
        return String.format("ExecutionContext{dataSize=%d, metadataSize=%d, elapsedTime=%dms}", 
                           data.size(), metadata.size(), getElapsedTime());
    }
}