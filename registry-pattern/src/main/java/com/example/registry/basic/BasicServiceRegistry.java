package com.example.registry.basic;

import com.example.registry.services.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Basic Registry Pattern Implementation
 * 
 * This registry stores pre-created service instances and provides access to them
 * via string keys. It's implemented as a thread-safe singleton.
 * 
 * Key Characteristics:
 * - Stores actual object instances (eager initialization)
 * - Uses string keys for lookup
 * - Returns the same shared instance every time
 * - Thread-safe using ConcurrentHashMap
 */
public class BasicServiceRegistry {
    
    // Singleton instance
    private static final BasicServiceRegistry INSTANCE = new BasicServiceRegistry();
    
    // Thread-safe storage for services
    private final Map<String, Service> serviceMap;
    
    // Private constructor to prevent direct instantiation
    private BasicServiceRegistry() {
        serviceMap = new ConcurrentHashMap<>();
    }
    
    /**
     * Gets the singleton instance of the registry.
     * @return the registry instance
     */
    public static BasicServiceRegistry getInstance() {
        return INSTANCE;
    }
    
    /**
     * Registers a service with the given key.
     * The service instance must be created before registration.
     * 
     * @param key the unique identifier for the service
     * @param service the service instance to register
     * @throws IllegalArgumentException if key or service is null
     */
    public void registerService(String key, Service service) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Service key cannot be null or empty");
        }
        if (service == null) {
            throw new IllegalArgumentException("Service cannot be null");
        }
        
        System.out.println("Registering service with key: " + key + " -> " + service.getName());
        serviceMap.put(key, service);
    }
    
    /**
     * Retrieves a service by its key.
     * 
     * @param key the service key
     * @return the service instance, or null if not found
     */
    public Service getService(String key) {
        if (key == null || key.trim().isEmpty()) {
            return null;
        }
        
        Service service = serviceMap.get(key);
        if (service == null) {
            System.err.println("Warning: No service found for key: " + key);
        }
        return service;
    }
    
    /**
     * Checks if a service is registered with the given key.
     * 
     * @param key the service key
     * @return true if service exists, false otherwise
     */
    public boolean hasService(String key) {
        return key != null && serviceMap.containsKey(key);
    }
    
    /**
     * Unregisters a service from the registry.
     * 
     * @param key the service key to remove
     * @return the removed service, or null if not found
     */
    public Service unregisterService(String key) {
        if (key == null) {
            return null;
        }
        
        Service removed = serviceMap.remove(key);
        if (removed != null) {
            System.out.println("Unregistered service with key: " + key);
        }
        return removed;
    }
    
    /**
     * Gets the number of registered services.
     * 
     * @return the count of registered services
     */
    public int getServiceCount() {
        return serviceMap.size();
    }
    
    /**
     * Clears all registered services.
     * Useful for testing or application shutdown.
     */
    public void clear() {
        System.out.println("Clearing all registered services");
        serviceMap.clear();
    }
}