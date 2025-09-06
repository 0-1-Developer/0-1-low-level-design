package com.example.strategy;

import com.example.strategy.classic.*;
import com.example.strategy.functional.*;
import com.example.strategy.generic.*;
import com.example.strategy.async.*;
import com.example.strategy.composite.*;
import com.example.strategy.config.*;
import com.example.strategy.retry.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Unit tests for all strategy pattern implementations.
 * These tests verify the core functionality without relying on demo outputs.
 */
public class StrategyPatternUnitTests {
    
    private static int totalTests = 0;
    private static int passedTests = 0;
    
    public static void main(String[] args) {
        System.out.println("=== Strategy Pattern Unit Tests ===\n");
        
        testClassicStrategies();
        testFunctionalStrategies();
        testGenericStrategies();
        testAsyncStrategies();
        testCompositeStrategies();
        testConfigStrategies();
        testRetryStrategies();
        
        System.out.println("\n" + "=".repeat(50));
        System.out.printf("Unit Tests Summary: %d/%d passed (%.1f%%)%n", 
                         passedTests, totalTests, (passedTests * 100.0) / totalTests);
        
        if (passedTests == totalTests) {
            System.out.println("All unit tests passed!");
        } else {
            System.out.println("Some unit tests failed.");
        }
    }
    
    private static void testClassicStrategies() {
        System.out.println("Testing Classic Strategy Pattern:");
        System.out.println("-".repeat(35));
        
        // Test strategy switching
        test("Classic Strategy Context", () -> {
            PaymentContext context = new PaymentContext();
            CreditCardStrategy creditCard = new CreditCardStrategy();
            creditCard.setTestData("1234567890123456", "123", "12/25");
            
            context.setStrategy(creditCard);
            boolean result = context.executePayment(100.0);
            
            return result; // Should return true for valid payment
        });
        
        // Test strategy validation
        test("Classic Strategy Validation", () -> {
            CreditCardStrategy creditCard = new CreditCardStrategy();
            creditCard.setTestData("invalid", "123", "12/25");
            
            return !creditCard.validatePaymentDetails(); // Should return false for invalid card
        });
        
        System.out.println();
    }
    
    private static void testFunctionalStrategies() {
        System.out.println("Testing Functional Strategy Pattern:");
        System.out.println("-".repeat(35));
        
        // Test lambda strategies
        test("Functional Lambda Strategy", () -> {
            ShoppingCart cart = new ShoppingCart();
            cart.addItem(new ShoppingCart.Item("Test", 100.0, 2));
            
            cart.setDiscountStrategy((price, quantity) -> price * quantity * 0.9);
            double total = cart.calculateTotal();
            
            return Math.abs(total - 180.0) < 0.01; // 200 * 0.9 = 180
        });
        
        // Test predefined strategies
        test("Functional Predefined Strategy", () -> {
            ShoppingCart cart = new ShoppingCart();
            cart.addItem(new ShoppingCart.Item("Test", 50.0, 15)); // Bulk quantity
            
            cart.setDiscountStrategy(DiscountStrategies.BULK_DISCOUNT);
            double total = cart.calculateTotal();
            
            return total < 750.0; // Should get bulk discount
        });
        
        System.out.println();
    }
    
    private static void testGenericStrategies() {
        System.out.println("Testing Generic Strategy Pattern:");
        System.out.println("-".repeat(35));
        
        // Test string validation
        test("Generic String Validation", () -> {
            ValidationContext<String> context = new ValidationContext<>(ValidationStrategies.EMAIL_VALIDATOR);
            ValidationResult validResult = context.validate("test@example.com");
            ValidationResult invalidResult = context.validate("invalid-email");
            
            return validResult.isValid() && !invalidResult.isValid();
        });
        
        // Test user validation
        test("Generic User Validation", () -> {
            ValidationContext<User> context = new ValidationContext<>(ValidationStrategies.BASIC_USER_VALIDATOR);
            User validUser = new User("john_doe", "john@example.com", 25);
            User invalidUser = new User("jo", "invalid-email", -5);
            
            return context.isValid(validUser) && !context.isValid(invalidUser);
        });
        
        System.out.println();
    }
    
    private static void testAsyncStrategies() {
        System.out.println("Testing Async Strategy Pattern:");
        System.out.println("-".repeat(35));
        
        // Test async execution
        test("Async Strategy Execution", () -> {
            AsyncDataProcessor<ImageProcessingRequest, ImageProcessingResult> processor = 
                new AsyncDataProcessor<>(AsyncImageProcessingStrategies.THUMBNAIL_GENERATOR);
            
            ImageProcessingRequest request = new ImageProcessingRequest(
                "test.jpg", "JPEG", 1920, 1080, 1000000);
            
            try {
                CompletableFuture<ImageProcessingResult> future = processor.processAsync(request);
                ImageProcessingResult result = future.get();
                return result.isSuccess();
            } catch (InterruptedException | ExecutionException e) {
                return false;
            }
        });
        
        // Test parallel processing
        test("Async Parallel Processing", () -> {
            AsyncDataProcessor<ImageProcessingRequest, ImageProcessingResult> processor = 
                new AsyncDataProcessor<>(AsyncImageProcessingStrategies.THUMBNAIL_GENERATOR);
            
            ImageProcessingRequest request1 = new ImageProcessingRequest("test1.jpg", "JPEG", 800, 600, 500000);
            ImageProcessingRequest request2 = new ImageProcessingRequest("test2.jpg", "PNG", 1024, 768, 800000);
            
            try {
                CompletableFuture<java.util.List<ImageProcessingResult>> future = processor.processAllAsync(request1, request2);
                java.util.List<ImageProcessingResult> results = future.get();
                return results.size() == 2 && results.get(0).isSuccess() && results.get(1).isSuccess();
            } catch (InterruptedException | ExecutionException e) {
                return false;
            }
        });
        
        System.out.println();
    }
    
    private static void testCompositeStrategies() {
        System.out.println("Testing Composite Strategy Pattern:");
        System.out.println("-".repeat(35));
        
        // Test sequential processing
        test("Composite Sequential Processing", () -> {
            TextDocument document = new TextDocument("hello world", "Test", "text");
            ExecutionContext context = new ExecutionContext();
            
            TextDocument result = CompositeStrategyProcessor.executeSequential(
                document, context,
                DocumentProcessingStrategies.UPPERCASE_CONVERTER,
                DocumentProcessingStrategies.WHITESPACE_NORMALIZER
            );
            
            return "HELLO WORLD".equals(result.getContent());
        });
        
        // Test parallel processing
        test("Composite Parallel Processing", () -> {
            TextDocument document = new TextDocument("test content for analysis", "Test", "text");
            ExecutionContext context = new ExecutionContext();
            
            List<String> results = CompositeStrategyProcessor.executeParallel(
                document, context,
                DocumentProcessingStrategies.WORD_COUNTER,
                (doc, ctx) -> "Custom analysis"
            );
            
            return results.size() == 2 && results.get(0).contains("words");
        });
        
        System.out.println();
    }
    
    private static void testConfigStrategies() {
        System.out.println("Testing Config-Driven Strategy Pattern:");
        System.out.println("-".repeat(35));
        
        // Test configuration management
        test("Config Strategy Management", () -> {
            ConfigurableStrategyManager<ReportRequest, ReportResult> manager = 
                new ConfigurableStrategyManager<>();
            
            StrategyConfig csvConfig = new StrategyConfig()
                .set("delimiter", ",")
                .set("include_headers", true);
            
            manager.registerStrategy("csv", ReportGenerationStrategies.CSV_GENERATOR, csvConfig);
            
            ReportRequest request = new ReportRequest("TEST-001", "Test Report", 
                Arrays.asList("name", "email"), new Date(), new Date(), "test@example.com");
            
            ReportResult result = manager.execute("csv", request);
            return result.isSuccess() && "CSV".equals(result.getFormat());
        });
        
        // Test configuration updates
        test("Config Strategy Updates", () -> {
            ConfigurableStrategyManager<ReportRequest, ReportResult> manager = 
                new ConfigurableStrategyManager<>();
            
            manager.registerStrategy("json", ReportGenerationStrategies.JSON_GENERATOR);
            manager.updateConfigProperty("json", "pretty_print", false);
            
            StrategyConfig config = manager.getConfiguration("json");
            return !config.getBoolean("pretty_print", true);
        });
        
        System.out.println();
    }
    
    private static void testRetryStrategies() {
        System.out.println("Testing Retry/Fallback Strategy Pattern:");
        System.out.println("-".repeat(35));
        
        // Test retry policy
        test("Retry Policy Configuration", () -> {
            RetryPolicy policy = new RetryPolicy.Builder()
                .maxAttempts(3)
                .baseDelay(java.time.Duration.ofMillis(100))
                .backoffMultiplier(2.0)
                .build();
            
            java.time.Duration delay1 = policy.calculateDelay(1);
            java.time.Duration delay2 = policy.calculateDelay(2);
            
            return delay1.toMillis() == 100 && delay2.toMillis() == 200;
        });
        
        // Test successful retry execution
        test("Retry Successful Execution", () -> {
            RetryableStrategyExecutor<String, String> executor = new RetryableStrategyExecutor<>();
            
            // Strategy that always succeeds
            RetryableStrategy<String, String> alwaysSucceeds = input -> "Success: " + input;
            
            executor.registerStrategy("success", alwaysSucceeds);
            RetryResult<String> result = executor.execute("success", "test");
            
            return result.isSuccess() && result.getTotalAttempts() == 1;
        });
        
        // Test fallback execution
        test("Retry Fallback Execution", () -> {
            RetryableStrategyExecutor<String, String> executor = new RetryableStrategyExecutor<>();
            
            // Strategy that always fails
            RetryableStrategy<String, String> alwaysFails = input -> {
                throw new RuntimeException("Always fails");
            };
            
            // Simple fallback
            FallbackStrategy<String, String> fallback = (input, exception) -> "Fallback: " + input;
            
            executor.registerStrategy("fail", alwaysFails, RetryPolicy.DEFAULT, fallback);
            RetryResult<String> result = executor.execute("fail", "test");
            
            return result.isSuccess() && result.isFallbackUsed() && 
                   result.getResult().orElse("").startsWith("Fallback:");
        });
        
        System.out.println();
    }
    
    private static void test(String testName, TestCase testCase) {
        totalTests++;
        try {
            boolean result = testCase.run();
            if (result) {
                System.out.println("PASS: " + testName);
                passedTests++;
            } else {
                System.out.println("FAIL: " + testName + " (assertion failed)");
            }
        } catch (Exception e) {
            System.out.println("FAIL: " + testName + " (exception: " + e.getMessage() + ")");
        }
    }
    
    @FunctionalInterface
    private interface TestCase {
        boolean run() throws Exception;
    }
}