package com.example.strategy.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Base configuration class for configurable strategies.
 * Provides a flexible key-value configuration system with type-safe getters.
 */
public class StrategyConfig {
    private final Map<String, Object> properties = new HashMap<>();
    
    public StrategyConfig() {}
    
    public StrategyConfig(Properties properties) {
        properties.forEach((key, value) -> this.properties.put(key.toString(), value));
    }
    
    public StrategyConfig(Map<String, Object> properties) {
        this.properties.putAll(properties);
    }
    
    /**
     * Sets a configuration property
     */
    public StrategyConfig set(String key, Object value) {
        properties.put(key, value);
        return this;
    }
    
    /**
     * Gets a configuration property with type casting
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) properties.get(key);
    }
    
    /**
     * Gets a configuration property with default value
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, T defaultValue) {
        return (T) properties.getOrDefault(key, defaultValue);
    }
    
    /**
     * Gets a string property
     */
    public String getString(String key) {
        return get(key);
    }
    
    /**
     * Gets a string property with default value
     */
    public String getString(String key, String defaultValue) {
        return get(key, defaultValue);
    }
    
    /**
     * Gets an integer property
     */
    public Integer getInt(String key) {
        Object value = properties.get(key);
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof String) {
            return Integer.parseInt((String) value);
        }
        return null;
    }
    
    /**
     * Gets an integer property with default value
     */
    public int getInt(String key, int defaultValue) {
        Integer value = getInt(key);
        return value != null ? value : defaultValue;
    }
    
    /**
     * Gets a boolean property
     */
    public Boolean getBoolean(String key) {
        Object value = properties.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value instanceof String) {
            return Boolean.parseBoolean((String) value);
        }
        return null;
    }
    
    /**
     * Gets a boolean property with default value
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        Boolean value = getBoolean(key);
        return value != null ? value : defaultValue;
    }
    
    /**
     * Gets a double property
     */
    public Double getDouble(String key) {
        Object value = properties.get(key);
        if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof String) {
            return Double.parseDouble((String) value);
        } else if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return null;
    }
    
    /**
     * Gets a double property with default value
     */
    public double getDouble(String key, double defaultValue) {
        Double value = getDouble(key);
        return value != null ? value : defaultValue;
    }
    
    /**
     * Checks if a key exists in the configuration
     */
    public boolean hasKey(String key) {
        return properties.containsKey(key);
    }
    
    /**
     * Removes a configuration property
     */
    public Object remove(String key) {
        return properties.remove(key);
    }
    
    /**
     * Gets all configuration keys
     */
    public java.util.Set<String> getKeys() {
        return properties.keySet();
    }
    
    /**
     * Gets the number of configuration properties
     */
    public int size() {
        return properties.size();
    }
    
    /**
     * Checks if the configuration is empty
     */
    public boolean isEmpty() {
        return properties.isEmpty();
    }
    
    /**
     * Clears all configuration properties
     */
    public void clear() {
        properties.clear();
    }
    
    /**
     * Creates a copy of this configuration
     */
    public StrategyConfig copy() {
        return new StrategyConfig(this.properties);
    }
    
    /**
     * Merges another configuration into this one
     */
    public StrategyConfig merge(StrategyConfig other) {
        this.properties.putAll(other.properties);
        return this;
    }
    
    @Override
    public String toString() {
        return "StrategyConfig" + properties;
    }
}