package com.example.strategy.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class AsyncStrategyDemo {
    public static void main(String[] args) {
        System.out.println("=== Async Strategy Pattern Demo ===\n");
        
        // Create sample image processing requests
        ImageProcessingRequest image1 = new ImageProcessingRequest("/images/photo1.jpg", "JPEG", 1920, 1080, 2500000);
        ImageProcessingRequest image2 = new ImageProcessingRequest("/images/photo2.png", "PNG", 800, 600, 1200000);
        ImageProcessingRequest image3 = new ImageProcessingRequest("/images/photo3.jpg", "JPEG", 4000, 3000, 8000000);
        
        // 1. Basic async processing
        System.out.println("1. Basic Async Processing:");
        System.out.println("--------------------------");
        
        AsyncDataProcessor<ImageProcessingRequest, ImageProcessingResult> processor = 
            new AsyncDataProcessor<>(AsyncImageProcessingStrategies.THUMBNAIL_GENERATOR);
        
        CompletableFuture<ImageProcessingResult> thumbnailFuture = processor.processAsync(image1);
        System.out.println("Started thumbnail generation for: " + image1.getImagePath());
        
        thumbnailFuture.thenAccept(result -> 
            System.out.println("Thumbnail result: " + result)
        ).join();
        
        // 2. Strategy switching at runtime
        System.out.println("\n2. Runtime Strategy Switching:");
        System.out.println("------------------------------");
        
        processor.setStrategy(AsyncImageProcessingStrategies.IMAGE_RESIZER);
        CompletableFuture<ImageProcessingResult> resizeFuture = processor.processAsync(image2);
        System.out.println("Started image resize for: " + image2.getImagePath());
        
        resizeFuture.thenAccept(result -> 
            System.out.println("Resize result: " + result)
        ).join();
        
        // 3. Parallel processing of multiple images
        System.out.println("\n3. Parallel Processing:");
        System.out.println("-----------------------");
        
        processor.setStrategy(AsyncImageProcessingStrategies.FORMAT_CONVERTER);
        
        long startTime = System.currentTimeMillis();
        CompletableFuture<java.util.List<ImageProcessingResult>> parallelFuture = 
            processor.processAllAsync(image1, image2, image3);
        
        System.out.println("Started parallel format conversion for 3 images...");
        
        parallelFuture.thenAccept(results -> {
            long endTime = System.currentTimeMillis();
            System.out.println("All conversions completed in " + (endTime - startTime) + "ms:");
            for (ImageProcessingResult result : results) {
                System.out.println("  " + result);
            }
        }).join();
        
        // 4. Race condition - first successful result
        System.out.println("\n4. First Successful Result (Race):");
        System.out.println("----------------------------------");
        
        // Create different processors with different strategies
        AsyncDataProcessor<ImageProcessingRequest, ImageProcessingResult> fastProcessor = 
            new AsyncDataProcessor<>(AsyncImageProcessingStrategies.WATERMARK_ADDER);
        AsyncDataProcessor<ImageProcessingRequest, ImageProcessingResult> slowProcessor = 
            new AsyncDataProcessor<>(AsyncImageProcessingStrategies.AI_ENHANCER);
        AsyncDataProcessor<ImageProcessingRequest, ImageProcessingResult> mediumProcessor = 
            new AsyncDataProcessor<>(AsyncImageProcessingStrategies.IMAGE_RESIZER);
        
        CompletableFuture<ImageProcessingResult> raceFuture = CompletableFuture.anyOf(
            fastProcessor.processAsync(image1),
            slowProcessor.processAsync(image1),
            mediumProcessor.processAsync(image1)
        ).thenApply(result -> (ImageProcessingResult) result);
        
        System.out.println("Started race between watermark, AI enhancement, and resize...");
        raceFuture.thenAccept(result -> 
            System.out.println("First completed: " + result)
        ).join();
        
        // 5. Timeout with fallback
        System.out.println("\n5. Timeout with Fallback:");
        System.out.println("-------------------------");
        
        processor.setStrategy(AsyncImageProcessingStrategies.AI_ENHANCER);
        CompletableFuture<ImageProcessingResult> timeoutFuture = 
            processor.processWithTimeout(image1, 1000, AsyncImageProcessingStrategies.FAST_FALLBACK);
        
        System.out.println("Started AI enhancement with 1-second timeout...");
        timeoutFuture.thenAccept(result -> 
            System.out.println("Result (with potential fallback): " + result)
        ).join();
        
        // 6. Strategy chaining (pipeline processing)
        System.out.println("\n6. Strategy Chaining (Pipeline):");
        System.out.println("--------------------------------");
        
        System.out.println("Starting pipeline processing...");
        
        // Simulate a processing pipeline
        CompletableFuture<ImageProcessingResult> pipelineFuture = 
            AsyncImageProcessingStrategies.IMAGE_RESIZER.executeAsync(image2)
                .thenCompose(resizeResult -> {
                    System.out.println("Pipeline step 1 complete: " + resizeResult);
                    ImageProcessingRequest watermarkInput = new ImageProcessingRequest(
                        resizeResult.getOutputPath(), image2.getFormat(), 
                        image2.getWidth(), image2.getHeight(), resizeResult.getOutputFileSize());
                    return AsyncImageProcessingStrategies.WATERMARK_ADDER.executeAsync(watermarkInput);
                })
                .thenCompose(watermarkResult -> {
                    System.out.println("Pipeline step 2 complete: " + watermarkResult);
                    ImageProcessingRequest convertInput = new ImageProcessingRequest(
                        watermarkResult.getOutputPath(), image2.getFormat(), 
                        image2.getWidth(), image2.getHeight(), watermarkResult.getOutputFileSize());
                    return AsyncImageProcessingStrategies.FORMAT_CONVERTER.executeAsync(convertInput);
                });
        
        pipelineFuture.thenAccept(finalResult -> 
            System.out.println("Pipeline complete: " + finalResult)
        ).join();
        
        // 7. Error handling and retries
        System.out.println("\n7. Error Handling and Retries:");
        System.out.println("------------------------------");
        
        CompletableFuture<ImageProcessingResult> retryFuture = 
            attemptWithRetry(image1, AsyncImageProcessingStrategies.UNRELIABLE_PROCESSOR, 3);
        
        System.out.println("Started unreliable processing with retries...");
        retryFuture.thenAccept(result -> 
            System.out.println("Final result after retries: " + result)
        ).exceptionally(throwable -> {
            System.out.println("All retry attempts failed: " + throwable.getMessage());
            return null;
        }).join();
        
        // 8. Custom strategy demonstration
        System.out.println("\n8. Custom Strategy Factory:");
        System.out.println("---------------------------");
        
        AsyncStrategy<ImageProcessingRequest, ImageProcessingResult> customStrategy = 
            AsyncImageProcessingStrategies.createCustomProcessor("Color Correction", 500, 1500);
        
        processor.setStrategy(customStrategy);
        CompletableFuture<ImageProcessingResult> customFuture = processor.processAsync(image3);
        
        System.out.println("Started custom color correction...");
        customFuture.thenAccept(result -> 
            System.out.println("Custom processing result: " + result)
        ).join();
        
        System.out.println("\n=== Demo Complete ===");
    }
    
    private static CompletableFuture<ImageProcessingResult> attemptWithRetry(
            ImageProcessingRequest request, 
            AsyncStrategy<ImageProcessingRequest, ImageProcessingResult> strategy,
            int maxAttempts) {
        
        return strategy.executeAsync(request)
            .thenCompose(result -> {
                if (result.isSuccess()) {
                    return CompletableFuture.completedFuture(result);
                } else if (maxAttempts > 1) {
                    System.out.println("Attempt failed, retrying... (" + (maxAttempts - 1) + " attempts left)");
                    return attemptWithRetry(request, strategy, maxAttempts - 1);
                } else {
                    return CompletableFuture.completedFuture(result);
                }
            });
    }
}