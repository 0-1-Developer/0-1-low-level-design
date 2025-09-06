package com.example.strategy;

import java.lang.reflect.Method;

/**
 * Test harness that runs all strategy pattern variant demos.
 * This class provides a unified way to test all implementations and verify they work correctly.
 */
public class AllStrategiesTestHarness {
    
    private static int totalTests = 0;
    private static int passedTests = 0;
    private static int failedTests = 0;
    
    public static void main(String[] args) {
        System.out.println("=== Strategy Pattern Implementation Test Harness ===");
        System.out.println("Testing all strategy pattern variants...\n");
        
        // Test all strategy variants
        testClassicStrategy();
        testFunctionalStrategy();
        testRegistryStrategy();
        testEnumStrategy();
        testGenericStrategy();
        testAsyncStrategy();
        testCompositeStrategy();
        testConfigStrategy();
        testRetryStrategy();
        
        // Print final results
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST HARNESS SUMMARY");
        System.out.println("=".repeat(70));
        System.out.printf("Total Tests: %d%n", totalTests);
        System.out.printf("Passed: %d%n", passedTests);
        System.out.printf("Failed: %d%n", failedTests);
        System.out.printf("Success Rate: %.1f%%%n", (passedTests * 100.0) / totalTests);
        
        if (failedTests == 0) {
            System.out.println("\nAll tests passed! All strategy implementations are working correctly.");
        } else {
            System.out.println("\nSome tests failed. Please check the implementations.");
        }
    }
    
    private static void testClassicStrategy() {
        System.out.println("1. Testing Classic Strategy Pattern:");
        System.out.println("-".repeat(40));
        runTest("Classic Strategy", () -> {
            com.example.strategy.classic.ClassicStrategyDemo.main(new String[]{});
        });
        System.out.println();
    }
    
    private static void testFunctionalStrategy() {
        System.out.println("2. Testing Functional Strategy Pattern:");
        System.out.println("-".repeat(40));
        runTest("Functional Strategy", () -> {
            com.example.strategy.functional.FunctionalStrategyDemo.main(new String[]{});
        });
        System.out.println();
    }
    
    private static void testRegistryStrategy() {
        System.out.println("3. Testing Registry Strategy Pattern:");
        System.out.println("-".repeat(40));
        runTest("Registry Strategy", () -> {
            com.example.strategy.registry.RegistryStrategyDemo.main(new String[]{});
        });
        System.out.println();
    }
    
    private static void testEnumStrategy() {
        System.out.println("4. Testing Enum Strategy Pattern:");
        System.out.println("-".repeat(40));
        runTest("Enum Strategy", () -> {
            com.example.strategy.enums.EnumStrategyDemo.main(new String[]{});
        });
        System.out.println();
    }
    
    private static void testGenericStrategy() {
        System.out.println("5. Testing Generic Type-Safe Strategy Pattern:");
        System.out.println("-".repeat(40));
        runTest("Generic Strategy", () -> {
            com.example.strategy.generic.GenericStrategyDemo.main(new String[]{});
        });
        System.out.println();
    }
    
    private static void testAsyncStrategy() {
        System.out.println("6. Testing Async Strategy Pattern:");
        System.out.println("-".repeat(40));
        runTest("Async Strategy", () -> {
            com.example.strategy.async.AsyncStrategyDemo.main(new String[]{});
        });
        System.out.println();
    }
    
    private static void testCompositeStrategy() {
        System.out.println("7. Testing Composite Strategy Pattern:");
        System.out.println("-".repeat(40));
        runTest("Composite Strategy", () -> {
            com.example.strategy.composite.CompositeStrategyDemo.main(new String[]{});
        });
        System.out.println();
    }
    
    private static void testConfigStrategy() {
        System.out.println("8. Testing Config-Driven Strategy Pattern:");
        System.out.println("-".repeat(40));
        runTest("Config Strategy", () -> {
            com.example.strategy.config.ConfigStrategyDemo.main(new String[]{});
        });
        System.out.println();
    }
    
    private static void testRetryStrategy() {
        System.out.println("9. Testing Retry/Fallback Strategy Pattern:");
        System.out.println("-".repeat(40));
        runTest("Retry Strategy", () -> {
            com.example.strategy.retry.RetryStrategyDemo.main(new String[]{});
        });
        System.out.println();
    }
    
    private static void runTest(String testName, TestRunner runner) {
        totalTests++;
        long startTime = System.currentTimeMillis();
        
        try {
            // Capture original System.out to restore later if needed
            runner.run();
            long duration = System.currentTimeMillis() - startTime;
            
            System.out.printf("PASS: %s (%.3fs)%n", testName, duration / 1000.0);
            passedTests++;
            
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            System.out.printf("FAIL: %s (%.3fs)%n", testName, duration / 1000.0);
            System.out.println("  Error: " + e.getMessage());
            if (Boolean.parseBoolean(System.getProperty("strategy.test.verbose", "false"))) {
                e.printStackTrace();
            }
            failedTests++;
        }
    }
    
    @FunctionalInterface
    private interface TestRunner {
        void run() throws Exception;
    }
    
    /**
     * Additional validation methods to verify strategy implementations
     */
    public static class StrategyValidator {
        
        /**
         * Validates that all required demo classes exist and have main methods
         */
        public static boolean validateDemoClasses() {
            String[] demoClasses = {
                "com.example.strategy.classic.ClassicStrategyDemo",
                "com.example.strategy.functional.FunctionalStrategyDemo", 
                "com.example.strategy.registry.RegistryStrategyDemo",
                "com.example.strategy.enums.EnumStrategyDemo",
                "com.example.strategy.generic.GenericStrategyDemo",
                "com.example.strategy.async.AsyncStrategyDemo",
                "com.example.strategy.composite.CompositeStrategyDemo",
                "com.example.strategy.config.ConfigStrategyDemo",
                "com.example.strategy.retry.RetryStrategyDemo"
            };
            
            for (String className : demoClasses) {
                try {
                    Class<?> clazz = Class.forName(className);
                    Method mainMethod = clazz.getDeclaredMethod("main", String[].class);
                    if (mainMethod == null) {
                        System.err.println("Missing main method in " + className);
                        return false;
                    }
                } catch (ClassNotFoundException e) {
                    System.err.println("Demo class not found: " + className);
                    return false;
                } catch (NoSuchMethodException e) {
                    System.err.println("Main method not found in " + className);
                    return false;
                }
            }
            return true;
        }
        
        /**
         * Validates that all strategy interfaces follow expected patterns
         */
        public static boolean validateStrategyInterfaces() {
            try {
                // Check classic strategy interface
                Class.forName("com.example.strategy.classic.PaymentStrategy");
                
                // Check functional strategy interface
                Class.forName("com.example.strategy.functional.DiscountStrategy");
                
                // Check registry strategy interface  
                Class.forName("com.example.strategy.registry.CompressionStrategy");
                
                // Check enum strategy
                Class.forName("com.example.strategy.enums.SortStrategy");
                
                // Check generic strategy interface
                Class.forName("com.example.strategy.generic.Strategy");
                
                // Check async strategy interface
                Class.forName("com.example.strategy.async.AsyncStrategy");
                
                // Check composite strategy interface
                Class.forName("com.example.strategy.composite.CompositeStrategy");
                
                // Check configurable strategy interface
                Class.forName("com.example.strategy.config.ConfigurableStrategy");
                
                // Check retryable strategy interface
                Class.forName("com.example.strategy.retry.RetryableStrategy");
                
                return true;
            } catch (ClassNotFoundException e) {
                System.err.println("Strategy interface not found: " + e.getMessage());
                return false;
            }
        }
    }
    
    /**
     * Run basic validation before executing demos
     */
    static {
        System.out.println("Validating strategy implementations...");
        
        if (!StrategyValidator.validateDemoClasses()) {
            System.err.println("Demo class validation failed!");
            System.exit(1);
        }
        
        if (!StrategyValidator.validateStrategyInterfaces()) {
            System.err.println("Strategy interface validation failed!");
            System.exit(1);
        }
        
        System.out.println("All validations passed. Starting test execution...\n");
    }
}