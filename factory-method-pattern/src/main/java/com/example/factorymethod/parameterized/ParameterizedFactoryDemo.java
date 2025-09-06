package com.example.factorymethod.parameterized;

import com.example.factorymethod.shared.Document;

public class ParameterizedFactoryDemo {
    
    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("🏭 FACTORY METHOD PATTERN - PARAMETERIZED DEMO");
        System.out.println("=".repeat(70));
        System.out.println();
        
        System.out.println("📋 This demo shows parameterized factory methods where:");
        System.out.println("   • Single factory method takes parameters to determine product type");
        System.out.println("   • Supports multiple parameter types (enum, string, format options)");
        System.out.println("   • Centralized creation logic with parameter validation");
        System.out.println("   • Distinguishes from Simple Factory by using method parameters");
        System.out.println();
        
        // Create documents using enum parameters
        System.out.println("🎯 ENUM-BASED PARAMETERIZED CREATION:");
        for (DocumentType type : ParameterizedDocumentFactory.getSupportedTypes()) {
            Document doc = ParameterizedDocumentFactory.createDocument(type, "Sample " + type.getDisplayName());
            processDocument(doc, "Standard content for " + type.getDisplayName().toLowerCase());
        }
        
        // Create documents using string parameters
        System.out.println("📝 STRING-BASED PARAMETERIZED CREATION:");
        String[] typeStrings = {"text", "pdf", "word", "html", "xml", "invalid"};
        for (String typeString : typeStrings) {
            Document doc = ParameterizedDocumentFactory.createDocument(typeString, "String-based " + typeString);
            processDocument(doc, "Content for " + typeString + " document");
        }
        
        // Create documents with format parameters
        System.out.println("🎨 FORMAT-ENHANCED PARAMETERIZED CREATION:");
        String[] formats = {"compressed", "encrypted", "template"};
        DocumentType[] types = {DocumentType.TEXT, DocumentType.PDF, DocumentType.HTML};
        
        for (int i = 0; i < formats.length; i++) {
            Document doc = ParameterizedDocumentFactory.createDocumentWithFormat(
                types[i], 
                "Formatted Document " + (i + 1), 
                formats[i]
            );
            processDocument(doc, null); // Content set by format
        }
        
        System.out.println("🎯 KEY BENEFITS:");
        System.out.println("   ✅ Single point of creation with parameter-driven selection");
        System.out.println("   ✅ Easy to add new types without changing client code");
        System.out.println("   ✅ Parameter validation and error handling centralized");
        System.out.println("   ✅ Supports multiple parameter types and formats");
        System.out.println("   ✅ Type-safe with enums, flexible with strings");
        System.out.println();
        
        System.out.println("⚠️  CONSIDERATIONS:");
        System.out.println("   • Can become complex with many parameter combinations");
        System.out.println("   • Violates Open/Closed Principle - new types require factory changes");
        System.out.println("   • Parameter validation logic can grow complex");
        System.out.println("   • Not true Factory Method pattern - closer to Simple Factory");
    }
    
    private static void processDocument(Document document, String content) {
        document.open();
        if (content != null && !content.isEmpty()) {
            document.setContent(content);
        }
        document.save();
        document.close();
        
        System.out.println("✅ Processed " + document.getDocumentType() + " document: " + document.getTitle());
        if (!document.getContent().isEmpty()) {
            String preview = document.getContent().substring(0, Math.min(60, document.getContent().length()));
            if (document.getContent().length() > 60) preview += "...";
            System.out.println("   Content: " + preview);
        }
        System.out.println();
    }
}