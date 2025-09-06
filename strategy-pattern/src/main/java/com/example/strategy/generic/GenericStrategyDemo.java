package com.example.strategy.generic;

public class GenericStrategyDemo {
    public static void main(String[] args) {
        System.out.println("=== Generic Type-Safe Strategy Pattern Demo ===\n");
        
        // String validation examples
        System.out.println("1. String Validation Strategies:");
        System.out.println("--------------------------------");
        
        ValidationContext<String> emailContext = new ValidationContext<>(ValidationStrategies.EMAIL_VALIDATOR);
        testStringValidation(emailContext, "Email", "user@example.com", "invalid-email", "");
        
        ValidationContext<String> usernameContext = new ValidationContext<>(ValidationStrategies.USERNAME_VALIDATOR);
        testStringValidation(usernameContext, "Username", "john_doe123", "jo", "invalid-username-with-special-chars!");
        
        ValidationContext<String> passwordContext = new ValidationContext<>(ValidationStrategies.PASSWORD_VALIDATOR);
        testStringValidation(passwordContext, "Password", "StrongPass123!", "weak", "NoSpecialChars123");
        
        // Integer validation examples
        System.out.println("\n2. Integer Validation Strategies:");
        System.out.println("---------------------------------");
        
        ValidationContext<Integer> ageContext = new ValidationContext<>(ValidationStrategies.AGE_VALIDATOR);
        testIntegerValidation(ageContext, "Age", 25, -5, 200);
        
        ValidationContext<Integer> adultAgeContext = new ValidationContext<>(ValidationStrategies.ADULT_AGE_VALIDATOR);
        testIntegerValidation(adultAgeContext, "Adult Age", 25, 16, 200);
        
        // User validation examples
        System.out.println("\n3. Complex Object Validation:");
        System.out.println("-----------------------------");
        
        ValidationContext<User> userContext = new ValidationContext<>(ValidationStrategies.BASIC_USER_VALIDATOR);
        
        User validUser = new User("alice_smith", "alice@example.com", 28);
        User invalidUsernameUser = new User("al", "alice@example.com", 28);
        User invalidEmailUser = new User("alice_smith", "invalid-email", 28);
        User invalidAgeUser = new User("alice_smith", "alice@example.com", -5);
        
        testUserValidation(userContext, "Basic User", validUser, invalidUsernameUser, invalidEmailUser, invalidAgeUser);
        
        // Adult user validation
        userContext.setValidationStrategy(ValidationStrategies.ADULT_USER_VALIDATOR);
        User minorUser = new User("teen_user", "teen@example.com", 16);
        testUserValidation(userContext, "Adult User", validUser, minorUser);
        
        // Custom validators using factory methods
        System.out.println("\n4. Custom Validator Factories:");
        System.out.println("------------------------------");
        
        ValidationContext<String> customLengthContext = new ValidationContext<>(
            ValidationStrategies.createLengthValidator(5, 15)
        );
        testStringValidation(customLengthContext, "Custom Length (5-15)", "ValidLength", "Too", "ThisIsTooLongForTheValidator");
        
        ValidationContext<Integer> customRangeContext = new ValidationContext<>(
            ValidationStrategies.createRangeValidator(18, 65)
        );
        testIntegerValidation(customRangeContext, "Working Age (18-65)", 35, 15, 70);
        
        // Combined validators
        System.out.println("\n5. Combined Validation Strategies:");
        System.out.println("---------------------------------");
        
        Strategy<String, ValidationResult> combinedPasswordValidator = ValidationStrategies.combineValidators(
            ValidationStrategies.createLengthValidator(12, 50),
            ValidationStrategies.PASSWORD_VALIDATOR
        );
        
        ValidationContext<String> combinedContext = new ValidationContext<>(combinedPasswordValidator);
        testStringValidation(combinedContext, "Enhanced Password (12+ chars)", 
            "VeryStrongPassword123!", "StrongPass1!", "WeakPassword");
        
        // Type safety demonstration
        System.out.println("\n6. Type Safety Demonstration:");
        System.out.println("-----------------------------");
        System.out.println("✓ Compile-time type safety prevents runtime ClassCastException");
        System.out.println("✓ No need for explicit casting or instanceof checks");
        System.out.println("✓ IDE provides better autocomplete and refactoring support");
        System.out.println("✓ Generic constraints ensure strategy and context compatibility");
        
        System.out.println("\n=== Demo Complete ===");
    }
    
    private static void testStringValidation(ValidationContext<String> context, String type, 
                                           String valid, String invalid1, String invalid2) {
        System.out.printf("%s Validation:%n", type);
        System.out.printf("  '%s' -> %s%n", valid, context.validate(valid));
        System.out.printf("  '%s' -> %s%n", invalid1, context.validate(invalid1));
        if (invalid2 != null && !invalid2.isEmpty()) {
            System.out.printf("  '%s' -> %s%n", invalid2, context.validate(invalid2));
        }
        System.out.println();
    }
    
    private static void testIntegerValidation(ValidationContext<Integer> context, String type,
                                            int valid, int invalid1, int invalid2) {
        System.out.printf("%s Validation:%n", type);
        System.out.printf("  %d -> %s%n", valid, context.validate(valid));
        System.out.printf("  %d -> %s%n", invalid1, context.validate(invalid1));
        System.out.printf("  %d -> %s%n", invalid2, context.validate(invalid2));
        System.out.println();
    }
    
    private static void testUserValidation(ValidationContext<User> context, String type, User... users) {
        System.out.printf("%s Validation:%n", type);
        for (User user : users) {
            ValidationResult result = context.validate(user);
            System.out.printf("  %s -> %s%n", user, result);
        }
        System.out.println();
    }
}