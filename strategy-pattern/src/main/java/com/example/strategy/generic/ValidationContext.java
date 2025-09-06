package com.example.strategy.generic;

import java.util.Objects;

/**
 * A generic context that can work with different types of validation strategies.
 * Demonstrates type-safe strategy pattern implementation.
 *
 * @param <T> The type of data to validate
 */
public class ValidationContext<T> {
    private Strategy<T, ValidationResult> validationStrategy;

    public ValidationContext(Strategy<T, ValidationResult> validationStrategy) {
        this.validationStrategy = Objects.requireNonNull(validationStrategy, "Validation strategy cannot be null");
    }

    public void setValidationStrategy(Strategy<T, ValidationResult> validationStrategy) {
        this.validationStrategy = Objects.requireNonNull(validationStrategy, "Validation strategy cannot be null");
    }

    public ValidationResult validate(T data) {
        if (data == null) {
            return new ValidationResult(false, "Input data cannot be null");
        }
        return validationStrategy.execute(data);
    }

    public boolean isValid(T data) {
        return validate(data).isValid();
    }
}