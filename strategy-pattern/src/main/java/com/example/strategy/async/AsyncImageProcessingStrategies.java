package com.example.strategy.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Collection of asynchronous image processing strategies.
 * Simulates real-world image processing operations with different processing times and complexities.
 */
public class AsyncImageProcessingStrategies {

    /**
     * Fast thumbnail generation strategy
     */
    public static final AsyncStrategy<ImageProcessingRequest, ImageProcessingResult> THUMBNAIL_GENERATOR = request -> {
        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();
            
            try {
                // Simulate thumbnail generation (fast operation)
                Thread.sleep(ThreadLocalRandom.current().nextInt(100, 300));
                
                long processingTime = System.currentTimeMillis() - startTime;
                String outputPath = request.getImagePath().replace(".", "_thumbnail.");
                long outputSize = request.getFileSize() / 10; // Thumbnails are much smaller
                
                return ImageProcessingResult.success(outputPath, "Thumbnail Generation", processingTime, outputSize);
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                long processingTime = System.currentTimeMillis() - startTime;
                return ImageProcessingResult.failure("Thumbnail Generation", processingTime, "Processing interrupted");
            }
        });
    };

    /**
     * Image resizing strategy with different processing times based on size
     */
    public static final AsyncStrategy<ImageProcessingRequest, ImageProcessingResult> IMAGE_RESIZER = request -> {
        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();
            
            try {
                // Processing time based on image size
                int pixels = request.getWidth() * request.getHeight();
                int processingTime = Math.max(200, pixels / 10000); // Minimum 200ms, scales with pixel count
                Thread.sleep(processingTime);
                
                long actualProcessingTime = System.currentTimeMillis() - startTime;
                String outputPath = request.getImagePath().replace(".", "_resized.");
                long outputSize = (long) (request.getFileSize() * 0.7); // Slightly smaller after resize
                
                return ImageProcessingResult.success(outputPath, "Image Resize", actualProcessingTime, outputSize);
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                long processingTime = System.currentTimeMillis() - startTime;
                return ImageProcessingResult.failure("Image Resize", processingTime, "Processing interrupted");
            }
        });
    };

    /**
     * Heavy AI-based image enhancement strategy
     */
    public static final AsyncStrategy<ImageProcessingRequest, ImageProcessingResult> AI_ENHANCER = request -> {
        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();
            
            try {
                // Simulate heavy AI processing (2-5 seconds)
                Thread.sleep(ThreadLocalRandom.current().nextInt(2000, 5000));
                
                long processingTime = System.currentTimeMillis() - startTime;
                String outputPath = request.getImagePath().replace(".", "_enhanced.");
                long outputSize = (long) (request.getFileSize() * 1.2); // Slightly larger after enhancement
                
                return ImageProcessingResult.success(outputPath, "AI Enhancement", processingTime, outputSize);
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                long processingTime = System.currentTimeMillis() - startTime;
                return ImageProcessingResult.failure("AI Enhancement", processingTime, "Processing interrupted");
            }
        });
    };

    /**
     * Format conversion strategy
     */
    public static final AsyncStrategy<ImageProcessingRequest, ImageProcessingResult> FORMAT_CONVERTER = request -> {
        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();
            
            try {
                // Processing time based on format complexity
                int processingTime = request.getFormat().equalsIgnoreCase("PNG") ? 800 : 400;
                Thread.sleep(processingTime + ThreadLocalRandom.current().nextInt(-100, 100));
                
                long actualProcessingTime = System.currentTimeMillis() - startTime;
                String newFormat = request.getFormat().equalsIgnoreCase("JPEG") ? "PNG" : "JPEG";
                String outputPath = request.getImagePath().replaceAll("\\.[^.]+$", "." + newFormat.toLowerCase());
                
                // PNG files are typically larger, JPEG smaller
                long outputSize = newFormat.equals("PNG") ? 
                    (long) (request.getFileSize() * 1.5) : 
                    (long) (request.getFileSize() * 0.6);
                
                return ImageProcessingResult.success(outputPath, "Format Conversion to " + newFormat, actualProcessingTime, outputSize);
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                long processingTime = System.currentTimeMillis() - startTime;
                return ImageProcessingResult.failure("Format Conversion", processingTime, "Processing interrupted");
            }
        });
    };

    /**
     * Watermark addition strategy
     */
    public static final AsyncStrategy<ImageProcessingRequest, ImageProcessingResult> WATERMARK_ADDER = request -> {
        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();
            
            try {
                // Simple watermark operation
                Thread.sleep(ThreadLocalRandom.current().nextInt(300, 600));
                
                long processingTime = System.currentTimeMillis() - startTime;
                String outputPath = request.getImagePath().replace(".", "_watermarked.");
                long outputSize = request.getFileSize(); // Size remains roughly the same
                
                return ImageProcessingResult.success(outputPath, "Watermark Addition", processingTime, outputSize);
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                long processingTime = System.currentTimeMillis() - startTime;
                return ImageProcessingResult.failure("Watermark Addition", processingTime, "Processing interrupted");
            }
        });
    };

    /**
     * Unreliable processing strategy that sometimes fails
     */
    public static final AsyncStrategy<ImageProcessingRequest, ImageProcessingResult> UNRELIABLE_PROCESSOR = request -> {
        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();
            
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 2000));
                
                // 30% chance of failure
                if (ThreadLocalRandom.current().nextDouble() < 0.3) {
                    long processingTime = System.currentTimeMillis() - startTime;
                    return ImageProcessingResult.failure("Unreliable Processing", processingTime, "Random processing failure");
                }
                
                long processingTime = System.currentTimeMillis() - startTime;
                String outputPath = request.getImagePath().replace(".", "_processed.");
                long outputSize = request.getFileSize();
                
                return ImageProcessingResult.success(outputPath, "Unreliable Processing", processingTime, outputSize);
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                long processingTime = System.currentTimeMillis() - startTime;
                return ImageProcessingResult.failure("Unreliable Processing", processingTime, "Processing interrupted");
            }
        });
    };

    /**
     * Fast fallback strategy for when other strategies fail or timeout
     */
    public static final AsyncStrategy<ImageProcessingRequest, ImageProcessingResult> FAST_FALLBACK = request -> {
        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();
            
            try {
                // Very fast fallback processing
                Thread.sleep(50);
                
                long processingTime = System.currentTimeMillis() - startTime;
                String outputPath = request.getImagePath().replace(".", "_fallback.");
                long outputSize = request.getFileSize();
                
                return ImageProcessingResult.success(outputPath, "Fast Fallback Processing", processingTime, outputSize);
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                long processingTime = System.currentTimeMillis() - startTime;
                return ImageProcessingResult.failure("Fast Fallback Processing", processingTime, "Processing interrupted");
            }
        });
    };

    /**
     * Factory method to create a custom processing strategy with configurable delay
     */
    public static AsyncStrategy<ImageProcessingRequest, ImageProcessingResult> createCustomProcessor(
            String operationName, int minDelayMs, int maxDelayMs) {
        return request -> CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();
            
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(minDelayMs, maxDelayMs));
                
                long processingTime = System.currentTimeMillis() - startTime;
                String outputPath = request.getImagePath().replace(".", "_" + operationName.toLowerCase().replaceAll("\\s+", "_") + ".");
                long outputSize = request.getFileSize();
                
                return ImageProcessingResult.success(outputPath, operationName, processingTime, outputSize);
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                long processingTime = System.currentTimeMillis() - startTime;
                return ImageProcessingResult.failure(operationName, processingTime, "Processing interrupted");
            }
        });
    }
}