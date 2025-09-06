package com.example.strategy.generic;

/**
 * Represents the result of a validation operation.
 * Immutable value object that contains validation status and optional error message.
 */
public class ValidationResult {
    private final boolean valid;
    private final String errorMessage;

    public ValidationResult(boolean valid, String errorMessage) {
        this.valid = valid;
        this.errorMessage = errorMessage;
    }

    public ValidationResult(boolean valid) {
        this(valid, null);
    }

    public boolean isValid() {
        return valid;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean hasErrorMessage() {
        return errorMessage != null && !errorMessage.trim().isEmpty();
    }

    @Override
    public String toString() {
        if (valid) {
            return "Valid";
        } else {
            return hasErrorMessage() ? "Invalid: " + errorMessage : "Invalid";
        }
    }
}