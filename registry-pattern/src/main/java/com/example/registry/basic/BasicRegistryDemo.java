package com.example.registry.basic;

import com.example.registry.services.*;

/**
 * Demonstration of the Basic Registry Pattern
 * 
 * This example shows how to use the BasicServiceRegistry to:
 * 1. Register pre-created service instances
 * 2. Retrieve and use services by string keys
 * 3. Handle missing services gracefully
 */
public class BasicRegistryDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Basic Registry Pattern Demo ===\n");
        
        // Get the singleton registry instance
        BasicServiceRegistry registry = BasicServiceRegistry.getInstance();
        
        // === REGISTRATION PHASE ===
        System.out.println("1. Registering services (Eager Initialization):");
        
        // Create service instances first (eager initialization)
        Service emailService = new EmailService();
        Service smsService = new SMSService();
        Service pushService = new PushNotificationService();
        
        // Register the pre-created instances
        registry.registerService("EMAIL", emailService);
        registry.registerService("SMS", smsService);
        registry.registerService("PUSH", pushService);
        
        System.out.println("Total registered services: " + registry.getServiceCount());
        System.out.println();
        
        // === USAGE PHASE ===
        System.out.println("2. Using registered services:");
        
        // Retrieve and use the email service
        Service retrievedEmailService = registry.getService("EMAIL");
        if (retrievedEmailService != null) {
            retrievedEmailService.execute();
            
            // Verify it's the same instance (singleton behavior)
            System.out.println("Same instance? " + (emailService == retrievedEmailService));
        }
        
        // Retrieve and use the SMS service
        Service retrievedSMSService = registry.getService("SMS");
        if (retrievedSMSService != null) {
            retrievedSMSService.execute();
        }
        
        System.out.println();
        
        // === DEMONSTRATING ISSUES ===
        System.out.println("3. Demonstrating potential issues:");
        
        // Issue 1: Typo in key (runtime error)
        Service typoService = registry.getService("EMIAL"); // Typo: EMIAL instead of EMAIL
        if (typoService == null) {
            System.out.println("Service not found due to typo in key!");
        }
        
        // Issue 2: Unsafe casting (potential ClassCastException)
        Service genericService = registry.getService("EMAIL");
        if (genericService != null) {
            try {
                // This cast is unsafe - we have to trust that "EMAIL" maps to EmailService
                EmailService specificEmailService = (EmailService) genericService;
                specificEmailService.sendEmail("user@example.com", "Welcome!");
            } catch (ClassCastException e) {
                System.err.println("ClassCastException: " + e.getMessage());
            }
        }
        
        System.out.println();
        
        // === ADVANCED OPERATIONS ===
        System.out.println("4. Advanced registry operations:");
        
        // Check if service exists
        System.out.println("Has EMAIL service? " + registry.hasService("EMAIL"));
        System.out.println("Has FAX service? " + registry.hasService("FAX"));
        
        // Unregister a service
        Service removedService = registry.unregisterService("PUSH");
        if (removedService != null) {
            System.out.println("Successfully removed: " + removedService.getName());
        }
        
        System.out.println("Services after removal: " + registry.getServiceCount());
        
        // === CLEANUP ===
        System.out.println("\n5. Cleanup:");
        registry.clear();
        System.out.println("Services after cleanup: " + registry.getServiceCount());
        
        System.out.println("\n=== Demo Complete ===");
    }
}