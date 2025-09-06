package com.example.factorymethod.testdouble;

public class TestDoubleFactoryDemo {
    
    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("🏭 FACTORY METHOD PATTERN - TEST DOUBLE ORIENTED DEMO");
        System.out.println("=".repeat(70));
        System.out.println();
        
        System.out.println("📋 This demo shows factory methods for testing with:");
        System.out.println("   • Production factory for real implementation");
        System.out.println("   • Mock factory for controlled testing");
        System.out.println("   • Easy swapping between real and fake implementations");
        System.out.println("   • Verification of factory interactions");
        System.out.println();
        
        // Production usage
        System.out.println("🏢 PRODUCTION USAGE:");
        DocumentProcessor productionProcessor = new DocumentProcessor(new ProductionDocumentFactory());
        productionProcessor.processDocument("Production Report", "Real business data");
        
        // Testing with mock factory
        System.out.println("🧪 TESTING WITH MOCK FACTORY:");
        MockDocumentFactory mockFactory = new MockDocumentFactory();
        DocumentProcessor testProcessor = new DocumentProcessor(mockFactory);
        
        // Process some documents
        testProcessor.processDocument("Test Document 1", "Test content 1");
        testProcessor.processDocument("Test Document 2", "Test content 2");
        
        // Verify interactions
        System.out.println("🔍 VERIFYING MOCK INTERACTIONS:");
        System.out.println("   Factory call count: " + mockFactory.getCreateCallCount());
        System.out.println("   Last requested title: " + mockFactory.getLastRequestedTitle());
        System.out.println();
        
        // Batch processing test
        System.out.println("📦 BATCH PROCESSING TEST:");
        String[] testTitles = {"Batch Doc 1", "Batch Doc 2", "Batch Doc 3"};
        testProcessor.processBatch(testTitles, "Batch test content");
        
        System.out.println("🔍 FINAL VERIFICATION:");
        System.out.println("   Total factory calls: " + mockFactory.getCreateCallCount());
        System.out.println("   Last title processed: " + mockFactory.getLastRequestedTitle());
        
        // Reset mock
        mockFactory.reset();
        System.out.println("   Calls after reset: " + mockFactory.getCreateCallCount());
        System.out.println();
        
        // Simple test harness
        runTestHarness();
        
        System.out.println("🎯 KEY BENEFITS:");
        System.out.println("   ✅ Easy testing with mock implementations");
        System.out.println("   ✅ Isolation of units under test");
        System.out.println("   ✅ Verification of factory usage patterns");
        System.out.println("   ✅ Fast tests without external dependencies");
        System.out.println("   ✅ Controlled test environments");
        System.out.println();
        
        System.out.println("⚠️  CONSIDERATIONS:");
        System.out.println("   • Mocks need maintenance when interfaces change");
        System.out.println("   • Over-mocking can lead to brittle tests");
        System.out.println("   • Mock behavior must accurately reflect real implementation");
        System.out.println("   • Integration tests still needed for end-to-end validation");
    }
    
    private static void runTestHarness() {
        System.out.println("🧪 RUNNING TEST HARNESS:");
        
        MockDocumentFactory factory = new MockDocumentFactory();
        DocumentProcessor processor = new DocumentProcessor(factory);
        
        // Test 1: Single document processing
        System.out.print("   Test 1 - Single document: ");
        processor.processDocument("Test", "Content");
        boolean test1Pass = factory.getCreateCallCount() == 1;
        System.out.println(test1Pass ? "PASS ✅" : "FAIL ❌");
        
        // Test 2: Batch processing
        System.out.print("   Test 2 - Batch processing: ");
        factory.reset();
        processor.processBatch(new String[]{"A", "B"}, "Content");
        boolean test2Pass = factory.getCreateCallCount() == 2;
        System.out.println(test2Pass ? "PASS ✅" : "FAIL ❌");
        
        // Test 3: Title tracking
        System.out.print("   Test 3 - Title tracking: ");
        factory.reset();
        processor.processDocument("TrackedTitle", "Content");
        boolean test3Pass = "TrackedTitle".equals(factory.getLastRequestedTitle());
        System.out.println(test3Pass ? "PASS ✅" : "FAIL ❌");
        
        System.out.println("   Test harness completed");
        System.out.println();
    }
}