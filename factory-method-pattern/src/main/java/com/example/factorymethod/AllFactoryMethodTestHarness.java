package com.example.factorymethod;

import com.example.factorymethod.classic.*;
import com.example.factorymethod.interfacebased.*;
import com.example.factorymethod.staticmethod.DocumentFactory;
import com.example.factorymethod.parameterized.*;
import com.example.factorymethod.registrybacked.*;
import com.example.factorymethod.reflection.*;
import com.example.factorymethod.functional.*;
import com.example.factorymethod.abstractdefault.*;
import com.example.factorymethod.testdouble.*;
import com.example.factorymethod.shared.Document;

public class AllFactoryMethodTestHarness {
    
    private static int testsPassed = 0;
    private static int testsTotal = 0;
    
    public static void main(String[] args) {
        System.out.println("=".repeat(80));
        System.out.println("🧪 FACTORY METHOD PATTERN - COMPREHENSIVE TEST HARNESS");
        System.out.println("=".repeat(80));
        System.out.println();
        
        System.out.println("🎯 Running tests for all 9 Factory Method pattern implementations...");
        System.out.println();
        
        // Test all variants
        testClassicFactoryMethod();
        testInterfaceBasedFactory();
        testStaticFactoryMethods();
        testParameterizedFactory();
        testRegistryBackedFactory();
        testReflectionBasedFactory();
        testFunctionalFactory();
        testAbstractDefaultFactory();
        testTestDoubleFactory();
        
        // Print results
        System.out.println("=".repeat(80));
        System.out.println("📊 TEST RESULTS SUMMARY");
        System.out.println("=".repeat(80));
        System.out.println("✅ Tests Passed: " + testsPassed);
        System.out.println("❌ Tests Failed: " + (testsTotal - testsPassed));
        System.out.println("📊 Success Rate: " + (testsPassed * 100 / testsTotal) + "%");
        System.out.println();
        
        if (testsPassed == testsTotal) {
            System.out.println("🎉 ALL TESTS PASSED! Factory Method patterns working correctly.");
        } else {
            System.out.println("⚠️  Some tests failed. Please review the implementations.");
        }
    }
    
    private static void testClassicFactoryMethod() {
        System.out.println("1️⃣  TESTING CLASSIC FACTORY METHOD");
        System.out.println("-".repeat(50));
        
        try {
            TextEditor textEditor = new TextEditor();
            textEditor.processDocument("Test", "Content");
            assertTest("Classic TextEditor creation", true);
            
            PdfReader pdfReader = new PdfReader();
            pdfReader.processDocument("Test", "Content");
            assertTest("Classic PdfReader creation", true);
            
        } catch (Exception e) {
            assertTest("Classic factory method execution", false);
        }
        System.out.println();
    }
    
    private static void testInterfaceBasedFactory() {
        System.out.println("2️⃣  TESTING INTERFACE-BASED FACTORY");
        System.out.println("-".repeat(50));
        
        try {
            com.example.factorymethod.interfacebased.DocumentFactory factory = new TextDocumentFactory();
            Document doc = factory.createDocument("Test");
            assertTest("Interface-based document creation", doc != null);
            assertTest("Interface-based document type", "TEXT".equals(doc.getDocumentType()));
            
            com.example.factorymethod.interfacebased.DocumentProcessor processor = new com.example.factorymethod.interfacebased.DocumentProcessor(factory);
            processor.processDocument("Test", "Content");
            assertTest("Interface-based processor execution", true);
            
        } catch (Exception e) {
            assertTest("Interface-based factory execution", false);
        }
        System.out.println();
    }
    
    private static void testStaticFactoryMethods() {
        System.out.println("3️⃣  TESTING STATIC FACTORY METHODS");
        System.out.println("-".repeat(50));
        
        try {
            Document textDoc = DocumentFactory.createTextDocument("Test");
            assertTest("Static text document creation", textDoc != null);
            assertTest("Static text document type", "TEXT".equals(textDoc.getDocumentType()));
            
            Document cachedDoc1 = DocumentFactory.createCachedTextDocument("Cached");
            Document cachedDoc2 = DocumentFactory.createCachedTextDocument("Cached");
            assertTest("Static cached document same instance", cachedDoc1 == cachedDoc2);
            
        } catch (Exception e) {
            assertTest("Static factory methods execution", false);
        }
        System.out.println();
    }
    
    private static void testParameterizedFactory() {
        System.out.println("4️⃣  TESTING PARAMETERIZED FACTORY");
        System.out.println("-".repeat(50));
        
        try {
            Document doc = ParameterizedDocumentFactory.createDocument(DocumentType.TEXT, "Test");
            assertTest("Parameterized document creation", doc != null);
            assertTest("Parameterized document type", "TEXT".equals(doc.getDocumentType()));
            
            Document stringDoc = ParameterizedDocumentFactory.createDocument("pdf", "Test");
            assertTest("Parameterized string-based creation", "PDF".equals(stringDoc.getDocumentType()));
            
        } catch (Exception e) {
            assertTest("Parameterized factory execution", false);
        }
        System.out.println();
    }
    
    private static void testRegistryBackedFactory() {
        System.out.println("5️⃣  TESTING REGISTRY-BACKED FACTORY");
        System.out.println("-".repeat(50));
        
        try {
            PluginManager.initializeCorePlugins();
            
            Document doc = DocumentFactoryRegistry.createDocument("text", "Test");
            assertTest("Registry-backed document creation", doc != null);
            assertTest("Registry-backed document type", "TEXT".equals(doc.getDocumentType()));
            
            boolean isSupported = DocumentFactoryRegistry.isTypeSupported("pdf");
            assertTest("Registry type support check", isSupported);
            
            DocumentFactoryRegistry.clearRegistry();
            
        } catch (Exception e) {
            assertTest("Registry-backed factory execution", false);
        }
        System.out.println();
    }
    
    private static void testReflectionBasedFactory() {
        System.out.println("6️⃣  TESTING REFLECTION-BASED FACTORY");
        System.out.println("-".repeat(50));
        
        try {
            Document doc = ReflectionDocumentFactory.createDocument("text", "Test");
            assertTest("Reflection-based document creation", doc != null);
            assertTest("Reflection-based document type", "TEXT".equals(doc.getDocumentType()));
            
            Document singletonDoc1 = ReflectionDocumentFactory.createSingletonDocument("pdf", "Singleton");
            Document singletonDoc2 = ReflectionDocumentFactory.createSingletonDocument("pdf", "Singleton");
            assertTest("Reflection-based singleton", singletonDoc1 == singletonDoc2);
            
            ReflectionDocumentFactory.clearCache();
            
        } catch (Exception e) {
            assertTest("Reflection-based factory execution", false);
        }
        System.out.println();
    }
    
    private static void testFunctionalFactory() {
        System.out.println("7️⃣  TESTING FUNCTIONAL/LAMBDA FACTORY");
        System.out.println("-".repeat(50));
        
        try {
            Document doc = FunctionalDocumentFactory.createDocument("text", "Test");
            assertTest("Functional document creation", doc != null);
            assertTest("Functional document type", "TEXT".equals(doc.getDocumentType()));
            
            Document processedDoc = FunctionalDocumentFactory.createDocumentWithProcessor(
                "text", "Test", d -> { d.setContent("Processed"); return d; });
            assertTest("Functional document processing", "Processed".equals(processedDoc.getContent()));
            
        } catch (Exception e) {
            assertTest("Functional factory execution", false);
        }
        System.out.println();
    }
    
    private static void testAbstractDefaultFactory() {
        System.out.println("8️⃣  TESTING ABSTRACT DEFAULT FACTORY");
        System.out.println("-".repeat(50));
        
        try {
            DefaultTextCreator defaultCreator = new DefaultTextCreator();
            defaultCreator.processDocument("Test", "Content");
            assertTest("Abstract default creator execution", true);
            
            SpecializedPdfCreator pdfCreator = new SpecializedPdfCreator();
            pdfCreator.processDocument("Test", "Content");
            assertTest("Abstract specialized creator execution", true);
            
        } catch (Exception e) {
            assertTest("Abstract default factory execution", false);
        }
        System.out.println();
    }
    
    private static void testTestDoubleFactory() {
        System.out.println("9️⃣  TESTING TEST-DOUBLE FACTORY");
        System.out.println("-".repeat(50));
        
        try {
            MockDocumentFactory mockFactory = new MockDocumentFactory();
            com.example.factorymethod.testdouble.DocumentProcessor processor = 
                new com.example.factorymethod.testdouble.DocumentProcessor(mockFactory);
            
            processor.processDocument("Test", "Content");
            assertTest("Test-double processor execution", true);
            assertTest("Test-double call tracking", mockFactory.getCreateCallCount() == 1);
            assertTest("Test-double title tracking", "Test".equals(mockFactory.getLastRequestedTitle()));
            
            mockFactory.reset();
            assertTest("Test-double reset functionality", mockFactory.getCreateCallCount() == 0);
            
        } catch (Exception e) {
            assertTest("Test-double factory execution", false);
        }
        System.out.println();
    }
    
    private static void assertTest(String testName, boolean condition) {
        testsTotal++;
        if (condition) {
            testsPassed++;
            System.out.println("   ✅ " + testName + ": PASS");
        } else {
            System.out.println("   ❌ " + testName + ": FAIL");
        }
    }
}