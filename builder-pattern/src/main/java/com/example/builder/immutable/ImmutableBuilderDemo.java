package com.example.builder.immutable;

import java.time.LocalDateTime;
import java.util.Arrays;

public class ImmutableBuilderDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Immutable Object Builder Demo ===\n");
        
        System.out.println("1. Building an immutable User object:");
        
        Address userAddress = new Address("123 Main St", "New York", "NY", "10001", "USA");
        
        User user = new User.UserBuilder()
            .id(1001)
            .username("johndoe")
            .email("john@example.com")
            .firstName("John")
            .lastName("Doe")
            .dateOfBirth(LocalDateTime.of(1990, 5, 15, 0, 0))
            .phoneNumber("+1-555-123-4567")
            .address(userAddress)
            .addInterest("Technology")
            .addInterest("Photography")
            .addInterest("Travel")
            .active(true)
            .lastLoginAt(LocalDateTime.now().minusHours(2))
            .build();
        
        System.out.println("Created user:");
        System.out.println(user);
        System.out.println();
        
        System.out.println("2. Demonstrating immutability:");
        
        System.out.println("Getting user's interests list:");
        var interests = user.getInterests();
        System.out.println("Original interests: " + interests);
        
        try {
            System.out.println("Attempting to modify the interests list...");
            interests.add("Hacking");
        } catch (UnsupportedOperationException e) {
            System.out.println("✓ Cannot modify - list is immutable: " + e.getClass().getSimpleName());
        }
        
        System.out.println("Interests after attempted modification: " + user.getInterests());
        System.out.println();
        
        System.out.println("3. Demonstrating defensive copying:");
        Address originalAddress = user.getAddress();
        System.out.println("Original address: " + originalAddress);
        
        Address retrievedAddress = user.getAddress();
        System.out.println("Are they the same object? " + (originalAddress == retrievedAddress));
        System.out.println("Are they equal? " + originalAddress.toString().equals(retrievedAddress.toString()));
        System.out.println();
        
        System.out.println("4. Creating another user with different configuration:");
        User adminUser = new User.UserBuilder()
            .id(2001)
            .username("admin")
            .email("admin@company.com")
            .firstName("Jane")
            .lastName("Admin")
            .interests(Arrays.asList("Security", "System Administration", "DevOps"))
            .active(true)
            .build();
        
        System.out.println("Admin user:");
        System.out.println(adminUser);
        System.out.println();
        
        System.out.println("5. Demonstrating validation:");
        try {
            User invalidUser = new User.UserBuilder()
                .id(3001)
                .firstName("Invalid")
                .lastName("User")
                .build();
        } catch (IllegalStateException e) {
            System.out.println("✓ Validation caught missing required field: " + e.getMessage());
        }
        
        try {
            User invalidUser = new User.UserBuilder()
                .id(-1)
                .username("test")
                .email("test@example.com")
                .build();
        } catch (IllegalStateException e) {
            System.out.println("✓ Validation caught invalid ID: " + e.getMessage());
        }
        
        System.out.println();
        System.out.println("Key Benefits of Immutable Builder:");
        System.out.println("- Thread-safe objects after construction");
        System.out.println("- No accidental modification after creation");
        System.out.println("- Defensive copying protects internal state");
        System.out.println("- Safe to share references across multiple threads");
        System.out.println("- Validation ensures object consistency");
        System.out.println("- Builder can be reused for creating similar objects");
        
        System.out.println("\n=== Demo Complete ===");
    }
}