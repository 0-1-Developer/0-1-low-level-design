package com.example.registry.factory;

import com.example.registry.services.*;

/**
 * Demonstration of the Factory Registry Pattern
 * 
 * This example shows how to use the FactoryServiceRegistry to:
 * 1. Register factory functions instead of instances
 * 2. Support both prototype and singleton scopes
 * 3. Enable lazy initialization
 * 4. Demonstrate flexible object lifecycle management
 */
public class FactoryRegistryDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Factory Registry Pattern Demo ===\n");
        
        // Get the singleton registry instance
        FactoryServiceRegistry registry = FactoryServiceRegistry.getInstance();
        
        // === REGISTRATION PHASE ===
        System.out.println("1. Registering service factories (Lazy Initialization):");
        
        // Register prototype factories (new instance each time)
        registry.registerPrototypeFactory("EMAIL_PROTOTYPE", EmailService::new);
        registry.registerPrototypeFactory("SMS_PROTOTYPE", SMSService::new);
        
        // Register singleton factories (same instance each time)
        registry.registerSingletonFactory("EMAIL_SINGLETON", EmailService::new);
        registry.registerSingletonFactory("PUSH_SINGLETON", PushNotificationService::new);
        
        System.out.println("Total registered factories: " + registry.getFactoryCount());
        System.out.println("Singleton cache size: " + registry.getSingletonCacheSize());
        System.out.println("Note: No service instances have been created yet!\n");
        
        // === PROTOTYPE SCOPE DEMONSTRATION ===
        System.out.println("2. Demonstrating Prototype Scope (new instance each time):");
        
        Service email1 = registry.getService("EMAIL_PROTOTYPE");
        Service email2 = registry.getService("EMAIL_PROTOTYPE");
        
        if (email1 != null && email2 != null) {
            email1.execute();
            email2.execute();
            
            System.out.println("Are email1 and email2 the same instance? " + (email1 == email2));
            System.out.println("Expected: false (different instances)\n");
        }
        
        // === SINGLETON SCOPE DEMONSTRATION ===
        System.out.println("3. Demonstrating Singleton Scope (same instance each time):");
        
        Service emailSingleton1 = registry.getService("EMAIL_SINGLETON");
        System.out.println("Singleton cache size after first call: " + registry.getSingletonCacheSize());
        
        Service emailSingleton2 = registry.getService("EMAIL_SINGLETON");
        System.out.println("Singleton cache size after second call: " + registry.getSingletonCacheSize());
        
        if (emailSingleton1 != null && emailSingleton2 != null) {
            emailSingleton1.execute();
            
            System.out.println("Are emailSingleton1 and emailSingleton2 the same instance? " + 
                             (emailSingleton1 == emailSingleton2));
            System.out.println("Expected: true (same instance)\n");
        }
        
        // === LAZY INITIALIZATION DEMONSTRATION ===
        System.out.println("4. Demonstrating Lazy Initialization:");
        
        // Register a factory but don't call it yet
        registry.registerSingletonFactory("LAZY_SERVICE", () -> {
            System.out.println("  -> Factory called! Creating PushNotificationService...");
            return new PushNotificationService();
        });
        
        System.out.println("Factory registered, but service not created yet.");
        System.out.println("Current singleton cache size: " + registry.getSingletonCacheSize());
        
        // Now request the service - this triggers creation
        System.out.println("Requesting service for the first time:");
        Service lazyService = registry.getService("LAZY_SERVICE");
        if (lazyService != null) {
            lazyService.execute();
        }
        
        System.out.println("Singleton cache size after creation: " + registry.getSingletonCacheSize());
        System.out.println();
        
        // === COMPLEX FACTORY EXAMPLE ===
        System.out.println("5. Complex Factory with Configuration:");
        
        // Factory that creates configured services
        registry.registerPrototypeFactory("CONFIGURED_EMAIL", () -> {
            EmailService service = new EmailService();
            // Could add configuration here
            System.out.println("  -> Created configured EmailService");
            return service;
        });
        
        Service configuredService = registry.getService("CONFIGURED_EMAIL");
        if (configuredService != null) {
            configuredService.execute();
        }
        System.out.println();
        
        // === ERROR HANDLING ===
        System.out.println("6. Error Handling:");
        
        // Register a factory that throws an exception
        registry.registerPrototypeFactory("ERROR_SERVICE", () -> {
            throw new RuntimeException("Simulated factory error");
        });
        
        Service errorService = registry.getService("ERROR_SERVICE");
        System.out.println("Error service result: " + errorService);
        
        // Try to get non-existent service
        Service nonExistent = registry.getService("NON_EXISTENT");
        System.out.println("Non-existent service result: " + nonExistent);
        System.out.println();
        
        // === ADVANCED OPERATIONS ===
        System.out.println("7. Advanced Operations:");
        
        System.out.println("Has EMAIL_PROTOTYPE factory? " + registry.hasFactory("EMAIL_PROTOTYPE"));
        System.out.println("Has UNKNOWN factory? " + registry.hasFactory("UNKNOWN"));
        
        // Clear singleton cache
        System.out.println("Singleton cache before clear: " + registry.getSingletonCacheSize());
        registry.clearSingletonCache();
        System.out.println("Singleton cache after clear: " + registry.getSingletonCacheSize());
        
        // Unregister a factory
        boolean removed = registry.unregisterFactory("ERROR_SERVICE");
        System.out.println("Successfully removed ERROR_SERVICE factory: " + removed);
        
        System.out.println("Factories after removal: " + registry.getFactoryCount());
        
        // === CLEANUP ===
        System.out.println("\n8. Cleanup:");
        registry.clear();
        System.out.println("Factories after cleanup: " + registry.getFactoryCount());
        System.out.println("Singleton cache after cleanup: " + registry.getSingletonCacheSize());
        
        System.out.println("\n=== Demo Complete ===");
    }
}