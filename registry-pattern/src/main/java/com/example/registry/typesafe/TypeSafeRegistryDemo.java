package com.example.registry.typesafe;

import com.example.registry.services.*;

/**
 * Demonstration of the Type-Safe Registry Pattern
 * 
 * This example shows how to use the TypeSafeRegistry to:
 * 1. Register instances using Class objects as keys
 * 2. Retrieve instances with compile-time type safety
 * 3. Eliminate manual casting and ClassCastException risks
 * 4. Demonstrate the benefits over string-based registries
 */
public class TypeSafeRegistryDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Type-Safe Registry Pattern Demo ===\n");
        
        // Get the singleton registry instance
        TypeSafeRegistry registry = TypeSafeRegistry.getInstance();
        
        // === REGISTRATION PHASE ===
        System.out.println("1. Registering services with type safety:");
        
        // Register instances using their Class types as keys
        registry.register(EmailService.class, new EmailService());
        registry.register(SMSService.class, new SMSService());
        registry.register(PushNotificationService.class, new PushNotificationService());
        
        // You can also register by interface type
        Service genericEmailService = new EmailService();
        registry.register(Service.class, genericEmailService);
        
        System.out.println("Total registered instances: " + registry.getRegisteredCount());
        System.out.println("Registered types: " + registry.getRegisteredTypes());
        System.out.println();
        
        // === TYPE-SAFE RETRIEVAL ===
        System.out.println("2. Type-safe retrieval (no casting required):");
        
        // Retrieve with automatic type inference - no casting needed!
        EmailService emailService = registry.get(EmailService.class);
        if (emailService != null) {
            emailService.execute();
            // Can directly call EmailService-specific methods
            emailService.sendEmail("user@example.com", "Welcome!");
        }
        
        SMSService smsService = registry.get(SMSService.class);
        if (smsService != null) {
            smsService.execute();
            // Can directly call SMSService-specific methods
            smsService.sendSMS("+1234567890", "Hello World!");
        }
        
        PushNotificationService pushService = registry.get(PushNotificationService.class);
        if (pushService != null) {
            pushService.execute();
            // Can directly call PushNotificationService-specific methods
            pushService.sendPushNotification("device123", "Alert", "New message received");
        }
        
        System.out.println();
        
        // === COMPILE-TIME SAFETY DEMONSTRATION ===
        System.out.println("3. Compile-time safety benefits:");
        
        // This works perfectly - correct type assignment
        EmailService correctAssignment = registry.get(EmailService.class);
        System.out.println("Correct assignment successful: " + (correctAssignment != null));
        
        // The following lines would cause COMPILE ERRORS (uncomment to see):
        // SMSService wrongAssignment = registry.get(EmailService.class); // Won't compile!
        // EmailService anotherWrong = registry.get(SMSService.class);    // Won't compile!
        
        System.out.println("Compile-time safety prevents type mismatches!");
        System.out.println();
        
        // === INTERFACE-BASED REGISTRATION ===
        System.out.println("4. Interface-based registration:");
        
        Service genericService = registry.get(Service.class);
        if (genericService != null) {
            System.out.println("Retrieved service by interface: " + genericService.getName());
            genericService.execute();
        }
        System.out.println();
        
        // === COMPARISON WITH STRING-BASED APPROACH ===
        System.out.println("5. Comparison with string-based approach:");
        
        System.out.println("Type-Safe Registry Benefits:");
        System.out.println("✅ No manual casting required");
        System.out.println("✅ Compile-time type checking");
        System.out.println("✅ No ClassCastException risk");
        System.out.println("✅ Refactoring-safe (class renames are handled automatically)");
        System.out.println("✅ IDE auto-completion and type hints");
        
        System.out.println("\nString-Based Registry Issues:");
        System.out.println("❌ Manual casting required: (EmailService) registry.get(\"EMAIL\")");
        System.out.println("❌ Runtime errors from typos: registry.get(\"EMIAL\")");
        System.out.println("❌ ClassCastException risk");
        System.out.println("❌ No compile-time validation");
        System.out.println();
        
        // === ADVANCED USAGE ===
        System.out.println("6. Advanced usage patterns:");
        
        // Generic method that works with any type
        demonstrateGenericRetrieval(registry, EmailService.class);
        demonstrateGenericRetrieval(registry, SMSService.class);
        
        // Check type existence
        System.out.println("Has EmailService? " + registry.hasType(EmailService.class));
        System.out.println("Has String? " + registry.hasType(String.class));
        
        System.out.println();
        
        // === INHERITANCE AND POLYMORPHISM ===
        System.out.println("7. Inheritance and polymorphism:");
        
        // Register a subclass instance
        class AdvancedEmailService extends EmailService {
            @Override
            public void execute() {
                System.out.println("Executing Advanced Email Service with extra features...");
            }
        }
        
        registry.register(AdvancedEmailService.class, new AdvancedEmailService());
        
        AdvancedEmailService advancedService = registry.get(AdvancedEmailService.class);
        if (advancedService != null) {
            advancedService.execute();
        }
        
        System.out.println();
        
        // === ERROR HANDLING ===
        System.out.println("8. Error handling:");
        
        // Try to get non-registered type
        String nonExistent = registry.get(String.class);
        System.out.println("Non-existent type result: " + nonExistent);
        
        // Null safety
        EmailService nullResult = registry.get(null);
        System.out.println("Null type parameter result: " + nullResult);
        
        System.out.println();
        
        // === CLEANUP OPERATIONS ===
        System.out.println("9. Cleanup operations:");
        
        // Unregister a specific type
        EmailService removed = registry.unregister(EmailService.class);
        System.out.println("Removed EmailService: " + (removed != null));
        System.out.println("Registered count after removal: " + registry.getRegisteredCount());
        
        // Clear all
        registry.clear();
        System.out.println("Registered count after clear: " + registry.getRegisteredCount());
        
        System.out.println("\n=== Demo Complete ===");
    }
    
    /**
     * Generic method demonstrating type-safe retrieval with any type.
     * 
     * @param <T> the type parameter
     * @param registry the registry instance
     * @param type the class type to retrieve
     */
    private static <T> void demonstrateGenericRetrieval(TypeSafeRegistry registry, Class<T> type) {
        T instance = registry.get(type);
        if (instance != null) {
            System.out.println("Generic retrieval of " + type.getSimpleName() + ": " + 
                             instance.getClass().getSimpleName());
        }
    }
}