package com.example.factorymethod.reflection;

import com.example.factorymethod.shared.Document;
import com.example.factorymethod.reflection.ReflectionDocumentFactory.ReflectionFactoryException;
import java.util.Arrays;
import java.util.List;

public class ReflectionFactoryDemo {
    
    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("🏭 FACTORY METHOD PATTERN - REFLECTION-BASED DEMO");
        System.out.println("=".repeat(70));
        System.out.println();
        
        System.out.println("📋 This demo shows reflection-based factory methods with:");
        System.out.println("   • Dynamic class loading and instantiation");
        System.out.println("   • Runtime type registration using class names");
        System.out.println("   • Singleton pattern integration via reflection");
        System.out.println("   • Powerful but requires careful error handling");
        System.out.println();
        
        // Test basic reflection-based creation
        System.out.println("🔍 BASIC REFLECTION-BASED CREATION:");
        List<String> types = Arrays.asList("text", "pdf", "word", "html", "xml");
        for (String type : types) {
            try {
                Document doc = ReflectionDocumentFactory.createDocument(type, "Reflection " + type.toUpperCase());
                processDocument(doc, "Content created via reflection for " + type);
            } catch (ReflectionFactoryException e) {
                System.err.println("❌ Failed to create " + type + " document: " + e.getMessage());
                System.out.println();
            }
        }
        
        // Test direct class name creation
        System.out.println("🎯 DIRECT CLASS NAME CREATION:");
        String[] classNames = {
            "com.example.factorymethod.shared.TextDocument",
            "com.example.factorymethod.shared.PdfDocument",
            "com.example.factorymethod.shared.InvalidDocument"
        };
        
        for (String className : classNames) {
            try {
                Document doc = ReflectionDocumentFactory.createDocumentByClassName(className, "Direct Class");
                processDocument(doc, "Created directly from class: " + className);
            } catch (ReflectionFactoryException e) {
                System.err.println("❌ Failed to create from class " + className + ": " + e.getMessage());
                System.out.println();
            }
        }
        
        // Test singleton pattern via reflection
        System.out.println("🔄 SINGLETON PATTERN VIA REFLECTION:");
        try {
            Document singleton1 = ReflectionDocumentFactory.createSingletonDocument("text", "Singleton Doc");
            processDocument(singleton1, "First singleton instance");
            
            Document singleton2 = ReflectionDocumentFactory.createSingletonDocument("text", "Singleton Doc");
            processDocument(singleton2, "Second singleton access (should be cached)");
            
            System.out.println("🔍 Same instance? " + (singleton1 == singleton2));
            System.out.println();
            
        } catch (ReflectionFactoryException e) {
            System.err.println("❌ Singleton creation failed: " + e.getMessage());
        }
        
        // Test runtime type registration
        System.out.println("📝 RUNTIME TYPE REGISTRATION:");
        try {
            // Register a custom type at runtime
            ReflectionDocumentFactory.registerType("custom_text", 
                "com.example.factorymethod.shared.TextDocument");
            
            Document customDoc = ReflectionDocumentFactory.createDocument("custom_text", "Custom Type");
            processDocument(customDoc, "Created using runtime-registered type");
            
        } catch (Exception e) {
            System.err.println("❌ Runtime registration failed: " + e.getMessage());
        }
        
        // Test error handling
        System.out.println("⚠️  ERROR HANDLING DEMONSTRATION:");
        try {
            ReflectionDocumentFactory.createDocument("nonexistent", "Error Test");
        } catch (ReflectionFactoryException e) {
            System.out.println("✅ Properly caught error: " + e.getMessage());
        }
        
        try {
            ReflectionDocumentFactory.createDocumentByClassName("com.invalid.Class", "Error Test");
        } catch (ReflectionFactoryException e) {
            System.out.println("✅ Properly caught class not found: " + e.getMessage());
        }
        System.out.println();
        
        // Cleanup
        ReflectionDocumentFactory.clearCache();
        
        System.out.println("🎯 KEY BENEFITS:");
        System.out.println("   ✅ Maximum flexibility - create any class at runtime");
        System.out.println("   ✅ Dynamic type registration without code changes");
        System.out.println("   ✅ Can integrate with configuration files or databases");
        System.out.println("   ✅ Supports plugin architectures with unknown types");
        System.out.println("   ✅ Can implement advanced patterns like singletons");
        System.out.println();
        
        System.out.println("⚠️  CONSIDERATIONS:");
        System.out.println("   • Performance overhead due to reflection");
        System.out.println("   • Runtime errors instead of compile-time safety");
        System.out.println("   • Requires careful exception handling");
        System.out.println("   • Can break with code obfuscation or security managers");
        System.out.println("   • Harder to debug and maintain");
    }
    
    private static void processDocument(Document document, String content) {
        document.open();
        if (content != null) {
            document.setContent(content);
        }
        document.save();
        document.close();
        System.out.println("✅ Processed " + document.getDocumentType() + " document: " + document.getTitle());
        System.out.println();
    }
}