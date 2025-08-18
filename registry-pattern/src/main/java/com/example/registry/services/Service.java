package com.example.registry.services;

/**
 * Common interface for all services in the registry pattern examples.
 * This ensures that all registered services have a consistent contract.
 */
public interface Service {
    /**
     * Returns the name of the service.
     * @return the service name
     */
    String getName();
    
    /**
     * Executes the service's main functionality.
     */
    void execute();
}