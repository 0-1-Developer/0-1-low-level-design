package com.example.strategy.async;

/**
 * Represents the result of an image processing operation.
 * Contains the processed image information and metadata.
 */
public class ImageProcessingResult {
    private final String outputPath;
    private final String operation;
    private final long processingTimeMs;
    private final boolean success;
    private final String errorMessage;
    private final long outputFileSize;

    public ImageProcessingResult(String outputPath, String operation, long processingTimeMs, 
                               boolean success, String errorMessage, long outputFileSize) {
        this.outputPath = outputPath;
        this.operation = operation;
        this.processingTimeMs = processingTimeMs;
        this.success = success;
        this.errorMessage = errorMessage;
        this.outputFileSize = outputFileSize;
    }

    public static ImageProcessingResult success(String outputPath, String operation, 
                                              long processingTimeMs, long outputFileSize) {
        return new ImageProcessingResult(outputPath, operation, processingTimeMs, true, null, outputFileSize);
    }

    public static ImageProcessingResult failure(String operation, long processingTimeMs, String errorMessage) {
        return new ImageProcessingResult(null, operation, processingTimeMs, false, errorMessage, 0);
    }

    public String getOutputPath() {
        return outputPath;
    }

    public String getOperation() {
        return operation;
    }

    public long getProcessingTimeMs() {
        return processingTimeMs;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public long getOutputFileSize() {
        return outputFileSize;
    }

    @Override
    public String toString() {
        if (success) {
            return String.format("Success: %s completed in %dms, output: %s (%d bytes)", 
                               operation, processingTimeMs, outputPath, outputFileSize);
        } else {
            return String.format("Failed: %s failed after %dms - %s", 
                               operation, processingTimeMs, errorMessage);
        }
    }
}