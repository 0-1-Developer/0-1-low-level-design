package com.example.strategy.config;

import java.io.*;
import java.util.*;

/**
 * Manager class for handling configurable strategies with external configuration loading.
 * Supports loading configuration from files, properties, and runtime modification.
 *
 * @param <T> Input type
 * @param <R> Result type
 */
public class ConfigurableStrategyManager<T, R> {
    private final Map<String, ConfigurableStrategy<T, R, StrategyConfig>> strategies = new HashMap<>();
    private final Map<String, StrategyConfig> configurations = new HashMap<>();
    private StrategyConfig defaultConfig = new StrategyConfig();
    private String activeStrategyName;
    
    /**
     * Registers a strategy with a name
     */
    public void registerStrategy(String name, ConfigurableStrategy<T, R, StrategyConfig> strategy) {
        strategies.put(name, strategy);
        if (activeStrategyName == null) {
            activeStrategyName = name;
        }
    }
    
    /**
     * Registers a strategy with name and default configuration
     */
    public void registerStrategy(String name, ConfigurableStrategy<T, R, StrategyConfig> strategy, 
                               StrategyConfig defaultConfig) {
        registerStrategy(name, strategy);
        configurations.put(name, defaultConfig);
    }
    
    /**
     * Sets configuration for a specific strategy
     */
    public void setConfiguration(String strategyName, StrategyConfig config) {
        configurations.put(strategyName, config);
    }
    
    /**
     * Gets configuration for a specific strategy
     */
    public StrategyConfig getConfiguration(String strategyName) {
        return configurations.getOrDefault(strategyName, defaultConfig);
    }
    
    /**
     * Sets the default configuration for all strategies
     */
    public void setDefaultConfiguration(StrategyConfig config) {
        this.defaultConfig = config;
    }
    
    /**
     * Sets the active strategy
     */
    public void setActiveStrategy(String strategyName) {
        if (!strategies.containsKey(strategyName)) {
            throw new IllegalArgumentException("Strategy not found: " + strategyName);
        }
        this.activeStrategyName = strategyName;
    }
    
    /**
     * Gets the active strategy name
     */
    public String getActiveStrategy() {
        return activeStrategyName;
    }
    
    /**
     * Executes the active strategy with its configuration
     */
    public R execute(T input) {
        if (activeStrategyName == null) {
            throw new IllegalStateException("No active strategy set");
        }
        return execute(activeStrategyName, input);
    }
    
    /**
     * Executes a specific strategy with its configuration
     */
    public R execute(String strategyName, T input) {
        ConfigurableStrategy<T, R, StrategyConfig> strategy = strategies.get(strategyName);
        if (strategy == null) {
            throw new IllegalArgumentException("Strategy not found: " + strategyName);
        }
        
        StrategyConfig config = getConfiguration(strategyName);
        return strategy.execute(input, config);
    }
    
    /**
     * Executes a specific strategy with custom configuration
     */
    public R execute(String strategyName, T input, StrategyConfig config) {
        ConfigurableStrategy<T, R, StrategyConfig> strategy = strategies.get(strategyName);
        if (strategy == null) {
            throw new IllegalArgumentException("Strategy not found: " + strategyName);
        }
        
        return strategy.execute(input, config);
    }
    
    /**
     * Loads configuration from a properties file
     */
    public void loadConfigurationFromFile(String strategyName, String configFilePath) throws IOException {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(configFilePath)) {
            properties.load(fis);
            StrategyConfig config = new StrategyConfig(properties);
            setConfiguration(strategyName, config);
        }
    }
    
    /**
     * Loads configuration from properties string (for demonstration purposes)
     */
    public void loadConfigurationFromString(String strategyName, String propertiesString) throws IOException {
        Properties properties = new Properties();
        try (StringReader reader = new StringReader(propertiesString)) {
            properties.load(reader);
            StrategyConfig config = new StrategyConfig(properties);
            setConfiguration(strategyName, config);
        }
    }
    
    /**
     * Saves configuration to a properties file
     */
    public void saveConfigurationToFile(String strategyName, String configFilePath) throws IOException {
        StrategyConfig config = getConfiguration(strategyName);
        Properties properties = new Properties();
        
        for (String key : config.getKeys()) {
            Object value = config.get(key);
            if (value != null) {
                properties.setProperty(key, value.toString());
            }
        }
        
        try (FileOutputStream fos = new FileOutputStream(configFilePath)) {
            properties.store(fos, "Configuration for strategy: " + strategyName);
        }
    }
    
    /**
     * Lists all registered strategies
     */
    public Set<String> getRegisteredStrategies() {
        return new HashSet<>(strategies.keySet());
    }
    
    /**
     * Checks if a strategy is registered
     */
    public boolean isStrategyRegistered(String strategyName) {
        return strategies.containsKey(strategyName);
    }
    
    /**
     * Updates configuration property for active strategy
     */
    public void updateConfigProperty(String key, Object value) {
        updateConfigProperty(activeStrategyName, key, value);
    }
    
    /**
     * Updates configuration property for specific strategy
     */
    public void updateConfigProperty(String strategyName, String key, Object value) {
        StrategyConfig config = configurations.get(strategyName);
        if (config == null) {
            config = new StrategyConfig();
            configurations.put(strategyName, config);
        }
        config.set(key, value);
    }
    
    /**
     * Gets configuration property for active strategy
     */
    public <V> V getConfigProperty(String key) {
        return getConfigProperty(activeStrategyName, key);
    }
    
    /**
     * Gets configuration property for specific strategy
     */
    public <V> V getConfigProperty(String strategyName, String key) {
        StrategyConfig config = getConfiguration(strategyName);
        return config.get(key);
    }
    
    /**
     * Removes a strategy and its configuration
     */
    public void removeStrategy(String strategyName) {
        strategies.remove(strategyName);
        configurations.remove(strategyName);
        
        if (strategyName.equals(activeStrategyName)) {
            activeStrategyName = strategies.keySet().stream().findFirst().orElse(null);
        }
    }
    
    /**
     * Clears all strategies and configurations
     */
    public void clear() {
        strategies.clear();
        configurations.clear();
        activeStrategyName = null;
    }
    
    /**
     * Gets summary information about all strategies
     */
    public String getSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("ConfigurableStrategyManager Summary:\n");
        summary.append("  Registered Strategies: ").append(strategies.size()).append("\n");
        summary.append("  Active Strategy: ").append(activeStrategyName).append("\n");
        summary.append("  Configured Strategies: ").append(configurations.size()).append("\n");
        
        for (String strategyName : strategies.keySet()) {
            summary.append("  - ").append(strategyName);
            if (strategyName.equals(activeStrategyName)) {
                summary.append(" (active)");
            }
            StrategyConfig config = configurations.get(strategyName);
            if (config != null) {
                summary.append(" [").append(config.size()).append(" config properties]");
            }
            summary.append("\n");
        }
        
        return summary.toString();
    }
}