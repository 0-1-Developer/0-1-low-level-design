package com.example.factorymethod.staticmethod;

import com.example.factorymethod.shared.Document;

public class StaticFactoryMethodDemo {
    
    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("🏭 FACTORY METHOD PATTERN - STATIC METHODS DEMO");
        System.out.println("=".repeat(70));
        System.out.println();
        
        System.out.println("📋 This demo shows static factory methods with features:");
        System.out.println("   • Named constructors with descriptive method names");
        System.out.println("   • Optional caching/memoization for performance");
        System.out.println("   • Template-based document creation");
        System.out.println("   • No need for object instantiation");
        System.out.println();
        
        // Basic static factory methods
        System.out.println("📄 BASIC STATIC FACTORY METHODS:");
        Document text = DocumentFactory.createTextDocument("Basic Text");
        processDocument(text, "Simple text content");
        
        Document pdf = DocumentFactory.createPdfDocument("Basic PDF");
        processDocument(pdf, "PDF document content");
        
        Document word = DocumentFactory.createWordDocument("Basic Word");
        processDocument(word, "Word document content");
        
        // Cached factory methods
        System.out.println("💾 CACHED FACTORY METHODS:");
        Document cachedText1 = DocumentFactory.createCachedTextDocument("Cached Document");
        processDocument(cachedText1, "First access - creates and caches");
        
        Document cachedText2 = DocumentFactory.createCachedTextDocument("Cached Document");
        processDocument(cachedText2, "Second access - retrieves from cache");
        
        System.out.println("📊 Cache size: " + DocumentFactory.getCacheSize());
        
        // Template-based creation
        System.out.println("📋 TEMPLATE-BASED CREATION:");
        Document report = DocumentFactory.createDocumentFromTemplate("Q4 Report", "report");
        processDocument(report, null); // Content already set by template
        
        Document letter = DocumentFactory.createDocumentFromTemplate("Business Letter", "letter");
        processDocument(letter, null);
        
        Document manual = DocumentFactory.createDocumentFromTemplate("User Manual", "manual");
        processDocument(manual, null);
        
        // Cleanup
        DocumentFactory.clearCache();
        
        System.out.println("🎯 KEY BENEFITS:");
        System.out.println("   ✅ Descriptive method names (createTextDocument vs new TextDocument)");
        System.out.println("   ✅ Can return cached instances for performance optimization");
        System.out.println("   ✅ Can return different subtypes based on parameters");
        System.out.println("   ✅ No inheritance hierarchy required");
        System.out.println("   ✅ Thread-safe with proper implementation");
        System.out.println();
        
        System.out.println("⚠️  CONSIDERATIONS:");
        System.out.println("   • No polymorphism - can't override static methods");
        System.out.println("   • All creation logic centralized in one class");
        System.out.println("   • Caching adds complexity and memory usage");
        System.out.println("   • Global state through static variables");
    }
    
    private static void processDocument(Document document, String content) {
        document.open();
        if (content != null) {
            document.setContent(content);
        }
        document.save();
        document.close();
        System.out.println("✅ Processed " + document.getDocumentType() + " document: " + document.getTitle());
        if (!document.getContent().isEmpty()) {
            System.out.println("   Content preview: " + 
                document.getContent().substring(0, Math.min(50, document.getContent().length())) + 
                (document.getContent().length() > 50 ? "..." : ""));
        }
        System.out.println();
    }
}