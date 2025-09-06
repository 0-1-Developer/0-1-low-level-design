package com.example.factorymethod.functional;

import com.example.factorymethod.shared.Document;
import java.util.Arrays;

public class FunctionalFactoryDemo {
    
    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("🏭 FACTORY METHOD PATTERN - FUNCTIONAL/LAMBDA DEMO");
        System.out.println("=".repeat(70));
        System.out.println();
        
        System.out.println("📋 This demo shows functional factory methods with:");
        System.out.println("   • Lambda expressions and method references as factories");
        System.out.println("   • Function composition and transformation");
        System.out.println("   • Higher-order functions for factory customization");
        System.out.println("   • Functional programming paradigms in Java 8+");
        System.out.println();
        
        // Basic functional factory creation
        System.out.println("⚡ LAMBDA-BASED FACTORY CREATION:");
        String[] types = {"text", "pdf", "word", "html", "xml"};
        for (String type : types) {
            Document doc = FunctionalDocumentFactory.createDocument(type, "Functional " + type.toUpperCase());
            processDocument(doc, "Content created using lambda factory");
        }
        
        // Zero-argument suppliers
        System.out.println("🎯 ZERO-ARGUMENT SUPPLIER FACTORIES:");
        String[] supplierTypes = {"default_text", "default_pdf", "template_report"};
        for (String type : supplierTypes) {
            Document doc = FunctionalDocumentFactory.createDocument(type);
            processDocument(doc, null); // Content might be pre-set
        }
        
        // Functional transformations
        System.out.println("🔄 FUNCTIONAL TRANSFORMATIONS:");
        Document transformedDoc = FunctionalDocumentFactory.createDocumentWithProcessor(
            "text", 
            "Transformed Document", 
            doc -> {
                doc.setContent("TRANSFORMED: " + doc.getTitle().toUpperCase());
                return doc;
            }
        );
        processDocument(transformedDoc, null);
        
        // Conditional transformations
        System.out.println("🔀 CONDITIONAL TRANSFORMATIONS:");
        Document conditionalDoc1 = FunctionalDocumentFactory.createConditionalDocument(
            "pdf", 
            "Conditional PDF", 
            true, 
            doc -> {
                doc.setContent("CONDITION TRUE: Enhanced content for " + doc.getTitle());
                return doc;
            }
        );
        processDocument(conditionalDoc1, null);
        
        Document conditionalDoc2 = FunctionalDocumentFactory.createConditionalDocument(
            "word", 
            "Conditional Word", 
            false, 
            doc -> {
                doc.setContent("CONDITION FALSE: This should not appear");
                return doc;
            }
        );
        processDocument(conditionalDoc2, "Default content since condition was false");
        
        // Runtime registration of lambda factories
        System.out.println("📝 RUNTIME LAMBDA REGISTRATION:");
        FunctionalDocumentFactory.registerFactory("custom", title -> {
            Document doc = new CustomFunctionalDocument(title);
            doc.setContent("Custom lambda-created content");
            return doc;
        });
        
        FunctionalDocumentFactory.registerZeroArgFactory("auto_custom", () -> {
            Document doc = new CustomFunctionalDocument("Auto Generated");
            doc.setContent("Zero-arg supplier content");
            return doc;
        });
        
        Document customDoc = FunctionalDocumentFactory.createDocument("custom", "Runtime Lambda");
        processDocument(customDoc, null);
        
        Document autoDoc = FunctionalDocumentFactory.createDocument("auto_custom");
        processDocument(autoDoc, null);
        
        // Factory composition
        System.out.println("🔗 FACTORY COMPOSITION:");
        Document composedDoc = FunctionalDocumentFactory.composeFactories("text", "pdf", "Composed");
        processDocument(composedDoc, null);
        
        System.out.println("🎯 KEY BENEFITS:");
        System.out.println("   ✅ Concise syntax with lambda expressions");
        System.out.println("   ✅ Functional composition and transformation");
        System.out.println("   ✅ Higher-order functions for factory customization");
        System.out.println("   ✅ Immutable and side-effect-free factory logic");
        System.out.println("   ✅ Easy to test and reason about");
        System.out.println();
        
        System.out.println("⚠️  CONSIDERATIONS:");
        System.out.println("   • Requires Java 8+ lambda support");
        System.out.println("   • Can be less readable for complex factory logic");
        System.out.println("   • Debugging lambda expressions can be challenging");
        System.out.println("   • Performance overhead for frequent lambda creation");
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
            System.out.println("   Content: " + document.getContent());
        }
        System.out.println();
    }
    
    static class CustomFunctionalDocument extends Document {
        public CustomFunctionalDocument(String title) {
            super(title);
        }
        
        @Override
        public void open() {
            System.out.println("🔧 Opening custom functional document: " + title);
        }
        
        @Override
        public void save() {
            System.out.println("💾 Saving custom functional document: " + title + ".func");
        }
        
        @Override
        public void close() {
            System.out.println("🔐 Closing custom functional document: " + title);
        }
        
        @Override
        public String getDocumentType() {
            return "CUSTOM_FUNCTIONAL";
        }
    }
}