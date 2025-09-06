package com.example.strategy.registry;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class CompressionStrategyRegistry {
    private static CompressionStrategyRegistry instance;
    private final Map<String, CompressionStrategy> strategies = new ConcurrentHashMap<>();
    private final Map<String, Supplier<CompressionStrategy>> strategyFactories = new ConcurrentHashMap<>();
    private CompressionStrategy defaultStrategy;
    
    private CompressionStrategyRegistry() {
        registerDefaultStrategies();
    }
    
    public static synchronized CompressionStrategyRegistry getInstance() {
        if (instance == null) {
            instance = new CompressionStrategyRegistry();
        }
        return instance;
    }
    
    private void registerDefaultStrategies() {
        register("ZIP", new ZipCompressionStrategy());
        register("RLE", new RleCompressionStrategy());
        register("LZW", new LzwCompressionStrategy());
        
        registerFactory("ZIP-LAZY", ZipCompressionStrategy::new);
        registerFactory("RLE-LAZY", RleCompressionStrategy::new);
        registerFactory("LZW-LAZY", LzwCompressionStrategy::new);
        
        setDefaultStrategy("ZIP");
    }
    
    public void register(String key, CompressionStrategy strategy) {
        if (key == null || strategy == null) {
            throw new IllegalArgumentException("Key and strategy cannot be null");
        }
        strategies.put(key.toUpperCase(), strategy);
        System.out.println("Registered strategy: " + key);
    }
    
    public void registerFactory(String key, Supplier<CompressionStrategy> factory) {
        if (key == null || factory == null) {
            throw new IllegalArgumentException("Key and factory cannot be null");
        }
        strategyFactories.put(key.toUpperCase(), factory);
        System.out.println("Registered factory for: " + key);
    }
    
    public CompressionStrategy getStrategy(String key) {
        if (key == null) {
            return defaultStrategy;
        }
        
        String upperKey = key.toUpperCase();
        
        CompressionStrategy strategy = strategies.get(upperKey);
        if (strategy != null) {
            return strategy;
        }
        
        Supplier<CompressionStrategy> factory = strategyFactories.get(upperKey);
        if (factory != null) {
            strategy = factory.get();
            strategies.put(upperKey, strategy);
            return strategy;
        }
        
        System.err.println("Strategy not found for key: " + key + ". Using default.");
        return defaultStrategy;
    }
    
    public void unregister(String key) {
        if (key != null) {
            strategies.remove(key.toUpperCase());
            strategyFactories.remove(key.toUpperCase());
            System.out.println("Unregistered strategy: " + key);
        }
    }
    
    public void setDefaultStrategy(String key) {
        CompressionStrategy strategy = getStrategy(key);
        if (strategy != null) {
            this.defaultStrategy = strategy;
            System.out.println("Default strategy set to: " + key);
        }
    }
    
    public Set<String> getAvailableStrategies() {
        Set<String> allKeys = new HashSet<>();
        allKeys.addAll(strategies.keySet());
        allKeys.addAll(strategyFactories.keySet());
        return Collections.unmodifiableSet(allKeys);
    }
    
    public CompressionStrategy selectBestStrategy(String data) {
        CompressionStrategy bestStrategy = null;
        double bestRatio = Double.MAX_VALUE;
        String bestName = "";
        
        for (Map.Entry<String, CompressionStrategy> entry : strategies.entrySet()) {
            CompressionStrategy strategy = entry.getValue();
            byte[] compressed = strategy.compress(data);
            double ratio = strategy.getCompressionRatio();
            
            if (ratio < bestRatio) {
                bestRatio = ratio;
                bestStrategy = strategy;
                bestName = entry.getKey();
            }
        }
        
        System.out.printf("Best strategy for data: %s (ratio: %.2f)%n", bestName, bestRatio);
        return bestStrategy != null ? bestStrategy : defaultStrategy;
    }
    
    public void clearRegistry() {
        strategies.clear();
        strategyFactories.clear();
        defaultStrategy = null;
        System.out.println("Registry cleared");
    }
}