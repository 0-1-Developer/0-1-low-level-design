package com.example.registry.factory;

import com.example.registry.services.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Factory Registry Pattern Implementation
 * 
 * This registry stores factory functions (Suppliers) that know how to create services
 * rather than storing the service instances themselves. This enables lazy initialization
 * and flexible object lifecycle management.
 * 
 * Key Characteristics:
 * - Stores factory functions (Supplier<Service>) instead of instances
 * - Supports lazy initialization (objects created on first request)
 * - Can return new instances each time (prototype scope) or cached instances (singleton scope)
 * - Thread-safe using ConcurrentHashMap
 */
public class FactoryServiceRegistry {
    
    // Singleton instance
    private static final FactoryServiceRegistry INSTANCE = new FactoryServiceRegistry();
    
    // Storage for service factories
    private final Map<String, Supplier<Service>> factoryMap;
    
    // Optional cache for singleton-scoped services
    private final Map<String, Service> singletonCache;
    
    // Private constructor
    private FactoryServiceRegistry() {
        factoryMap = new ConcurrentHashMap<>();
        singletonCache = new ConcurrentHashMap<>();
    }
    
    /**
     * Gets the singleton instance of the factory registry.
     * @return the registry instance
     */
    public static FactoryServiceRegistry getInstance() {
        return INSTANCE;
    }
    
    /**
     * Registers a service factory that creates new instances each time (prototype scope).
     * 
     * @param key the unique identifier for the service
     * @param factory the factory function that creates service instances
     * @throws IllegalArgumentException if key or factory is null
     */
    public void registerPrototypeFactory(String key, Supplier<Service> factory) {
        validateInputs(key, factory);
        System.out.println("Registering prototype factory for key: " + key);
        factoryMap.put(key, factory);
        // Remove from singleton cache if it exists
        singletonCache.remove(key);
    }
    
    /**
     * Registers a service factory that creates and caches a single instance (singleton scope).
     * The factory is called only once, and the same instance is returned for subsequent requests.
     * 
     * @param key the unique identifier for the service
     * @param factory the factory function that creates the service instance
     * @throws IllegalArgumentException if key or factory is null
     */
    public void registerSingletonFactory(String key, Supplier<Service> factory) {
        validateInputs(key, factory);
        System.out.println("Registering singleton factory for key: " + key);
        
        // Wrap the factory to provide singleton behavior
        Supplier<Service> singletonFactory = () -> {
            return singletonCache.computeIfAbsent(key, k -> {
                System.out.println("Creating singleton instance for key: " + key);
                return factory.get();
            });
        };
        
        factoryMap.put(key, singletonFactory);
    }
    
    /**
     * Retrieves a service by creating it using the registered factory.
     * 
     * @param key the service key
     * @return a service instance created by the factory, or null if no factory is registered
     */
    public Service getService(String key) {
        if (key == null || key.trim().isEmpty()) {
            return null;
        }
        
        Supplier<Service> factory = factoryMap.get(key);
        if (factory == null) {
            System.err.println("Warning: No factory found for key: " + key);
            return null;
        }
        
        try {
            return factory.get();
        } catch (Exception e) {
            System.err.println("Error creating service for key " + key + ": " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Checks if a factory is registered for the given key.
     * 
     * @param key the service key
     * @return true if factory exists, false otherwise
     */
    public boolean hasFactory(String key) {
        return key != null && factoryMap.containsKey(key);
    }
    
    /**
     * Unregisters a service factory.
     * 
     * @param key the service key to remove
     * @return true if factory was removed, false if not found
     */
    public boolean unregisterFactory(String key) {
        if (key == null) {
            return false;
        }
        
        boolean removed = factoryMap.remove(key) != null;
        singletonCache.remove(key); // Also remove from singleton cache
        
        if (removed) {
            System.out.println("Unregistered factory for key: " + key);
        }
        return removed;
    }
    
    /**
     * Gets the number of registered factories.
     * 
     * @return the count of registered factories
     */
    public int getFactoryCount() {
        return factoryMap.size();
    }
    
    /**
     * Clears all registered factories and singleton cache.
     */
    public void clear() {
        System.out.println("Clearing all registered factories and singleton cache");
        factoryMap.clear();
        singletonCache.clear();
    }
    
    /**
     * Clears only the singleton cache, keeping the factories registered.
     * Useful for forcing recreation of singleton instances.
     */
    public void clearSingletonCache() {
        System.out.println("Clearing singleton cache");
        singletonCache.clear();
    }
    
    /**
     * Gets the number of cached singleton instances.
     * 
     * @return the count of cached singletons
     */
    public int getSingletonCacheSize() {
        return singletonCache.size();
    }
    
    private void validateInputs(String key, Supplier<Service> factory) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Service key cannot be null or empty");
        }
        if (factory == null) {
            throw new IllegalArgumentException("Factory cannot be null");
        }
    }
}