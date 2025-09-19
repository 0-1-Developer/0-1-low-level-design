package com.example.state.reactive;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class Event {
    private final String type;
    private final String source;
    private final Instant timestamp;
    private final Map<String, Object> payload;
    
    public Event(String type, String source) {
        this(type, source, new HashMap<>());
    }
    
    public Event(String type, String source, Map<String, Object> payload) {
        this.type = type;
        this.source = source;
        this.timestamp = Instant.now();
        this.payload = new HashMap<>(payload);
    }
    
    public String getType() {
        return type;
    }
    
    public String getSource() {
        return source;
    }
    
    public Instant getTimestamp() {
        return timestamp;
    }
    
    public Object get(String key) {
        return payload.get(key);
    }
    
    public <T> T get(String key, Class<T> type) {
        Object value = payload.get(key);
        if (value != null && type.isInstance(value)) {
            return type.cast(value);
        }
        return null;
    }
    
    @Override
    public String toString() {
        return String.format("Event[type=%s, source=%s, time=%s, payload=%s]",
            type, source, timestamp, payload);
    }
    
    public static class Builder {
        private String type;
        private String source;
        private Map<String, Object> payload = new HashMap<>();
        
        public Builder type(String type) {
            this.type = type;
            return this;
        }
        
        public Builder source(String source) {
            this.source = source;
            return this;
        }
        
        public Builder payload(String key, Object value) {
            this.payload.put(key, value);
            return this;
        }
        
        public Event build() {
            return new Event(type, source, payload);
        }
    }
}