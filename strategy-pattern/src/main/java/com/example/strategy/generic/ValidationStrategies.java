package com.example.strategy.generic;

import java.util.regex.Pattern;

/**
 * Collection of predefined validation strategies for different data types.
 * Demonstrates type-safe strategy implementations.
 */
public class ValidationStrategies {
    
    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );
    
    // Username validation pattern (alphanumeric and underscore, 3-20 characters)
    private static final Pattern USERNAME_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_]{3,20}$"
    );

    /**
     * String validation strategies
     */
    public static final Strategy<String, ValidationResult> EMAIL_VALIDATOR = email -> {
        if (email == null || email.trim().isEmpty()) {
            return new ValidationResult(false, "Email cannot be empty");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return new ValidationResult(false, "Invalid email format");
        }
        return new ValidationResult(true);
    };

    public static final Strategy<String, ValidationResult> USERNAME_VALIDATOR = username -> {
        if (username == null || username.trim().isEmpty()) {
            return new ValidationResult(false, "Username cannot be empty");
        }
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            return new ValidationResult(false, "Username must be 3-20 characters long and contain only letters, numbers, and underscores");
        }
        return new ValidationResult(true);
    };

    public static final Strategy<String, ValidationResult> PASSWORD_VALIDATOR = password -> {
        if (password == null || password.isEmpty()) {
            return new ValidationResult(false, "Password cannot be empty");
        }
        if (password.length() < 8) {
            return new ValidationResult(false, "Password must be at least 8 characters long");
        }
        if (!password.matches(".*[A-Z].*")) {
            return new ValidationResult(false, "Password must contain at least one uppercase letter");
        }
        if (!password.matches(".*[a-z].*")) {
            return new ValidationResult(false, "Password must contain at least one lowercase letter");
        }
        if (!password.matches(".*\\d.*")) {
            return new ValidationResult(false, "Password must contain at least one digit");
        }
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            return new ValidationResult(false, "Password must contain at least one special character");
        }
        return new ValidationResult(true);
    };

    /**
     * Integer validation strategies
     */
    public static final Strategy<Integer, ValidationResult> AGE_VALIDATOR = age -> {
        if (age == null) {
            return new ValidationResult(false, "Age cannot be null");
        }
        if (age < 0) {
            return new ValidationResult(false, "Age cannot be negative");
        }
        if (age > 150) {
            return new ValidationResult(false, "Age cannot be greater than 150");
        }
        return new ValidationResult(true);
    };

    public static final Strategy<Integer, ValidationResult> ADULT_AGE_VALIDATOR = age -> {
        ValidationResult basicValidation = AGE_VALIDATOR.execute(age);
        if (!basicValidation.isValid()) {
            return basicValidation;
        }
        if (age < 18) {
            return new ValidationResult(false, "Must be 18 or older");
        }
        return new ValidationResult(true);
    };

    /**
     * User validation strategies (composite validations)
     */
    public static final Strategy<User, ValidationResult> BASIC_USER_VALIDATOR = user -> {
        ValidationResult usernameResult = USERNAME_VALIDATOR.execute(user.getUsername());
        if (!usernameResult.isValid()) {
            return new ValidationResult(false, "Username validation failed: " + usernameResult.getErrorMessage());
        }
        
        ValidationResult emailResult = EMAIL_VALIDATOR.execute(user.getEmail());
        if (!emailResult.isValid()) {
            return new ValidationResult(false, "Email validation failed: " + emailResult.getErrorMessage());
        }
        
        ValidationResult ageResult = AGE_VALIDATOR.execute(user.getAge());
        if (!ageResult.isValid()) {
            return new ValidationResult(false, "Age validation failed: " + ageResult.getErrorMessage());
        }
        
        return new ValidationResult(true);
    };

    public static final Strategy<User, ValidationResult> ADULT_USER_VALIDATOR = user -> {
        ValidationResult basicResult = BASIC_USER_VALIDATOR.execute(user);
        if (!basicResult.isValid()) {
            return basicResult;
        }
        
        ValidationResult adultResult = ADULT_AGE_VALIDATOR.execute(user.getAge());
        if (!adultResult.isValid()) {
            return new ValidationResult(false, "Adult age validation failed: " + adultResult.getErrorMessage());
        }
        
        return new ValidationResult(true);
    };

    /**
     * Factory method to create custom length validator
     */
    public static Strategy<String, ValidationResult> createLengthValidator(int minLength, int maxLength) {
        return input -> {
            if (input == null) {
                return new ValidationResult(false, "Input cannot be null");
            }
            if (input.length() < minLength) {
                return new ValidationResult(false, String.format("Input must be at least %d characters long", minLength));
            }
            if (input.length() > maxLength) {
                return new ValidationResult(false, String.format("Input cannot be longer than %d characters", maxLength));
            }
            return new ValidationResult(true);
        };
    }

    /**
     * Factory method to create custom range validator for numbers
     */
    public static Strategy<Integer, ValidationResult> createRangeValidator(int min, int max) {
        return input -> {
            if (input == null) {
                return new ValidationResult(false, "Input cannot be null");
            }
            if (input < min) {
                return new ValidationResult(false, String.format("Value must be at least %d", min));
            }
            if (input > max) {
                return new ValidationResult(false, String.format("Value cannot be greater than %d", max));
            }
            return new ValidationResult(true);
        };
    }

    /**
     * Combines multiple strategies into a single strategy that validates all conditions
     */
    @SafeVarargs
    public static <T> Strategy<T, ValidationResult> combineValidators(Strategy<T, ValidationResult>... validators) {
        return input -> {
            for (Strategy<T, ValidationResult> validator : validators) {
                ValidationResult result = validator.execute(input);
                if (!result.isValid()) {
                    return result;
                }
            }
            return new ValidationResult(true);
        };
    }
}