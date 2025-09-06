package com.example.factorymethod.interfacebased;

import java.util.Arrays;
import java.util.List;

public class InterfaceBasedFactoryDemo {
    
    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("🏭 FACTORY METHOD PATTERN - INTERFACE-BASED DEMO");
        System.out.println("=".repeat(70));
        System.out.println();
        
        System.out.println("📋 This demo shows interface-based Factory Method where:");
        System.out.println("   • Factory interface defines the creation contract");
        System.out.println("   • Multiple implementations provide different factory behaviors");
        System.out.println("   • Client can switch between factories at runtime");
        System.out.println("   • More flexible than inheritance-based approach");
        System.out.println();
        
        List<DocumentFactory> factories = Arrays.asList(
            new TextDocumentFactory(),
            new PdfDocumentFactory(),
            new WordDocumentFactory()
        );
        
        for (DocumentFactory factory : factories) {
            DocumentProcessor processor = new DocumentProcessor(factory);
            processor.processDocument("Project Report", "Q4 financial analysis");
        }
        
        System.out.println("🔄 RUNTIME FACTORY SWITCHING DEMO:");
        DocumentProcessor flexibleProcessor = new DocumentProcessor(new TextDocumentFactory());
        flexibleProcessor.processDocument("Initial Document", "Starting with text format");
        
        flexibleProcessor.switchFactory(new PdfDocumentFactory());
        flexibleProcessor.processDocument("Final Report", "Converting to PDF format");
        
        System.out.println("🎯 KEY BENEFITS:");
        System.out.println("   ✅ Composition over inheritance");
        System.out.println("   ✅ Runtime factory switching capability");
        System.out.println("   ✅ Multiple factory implementations without inheritance chains");
        System.out.println("   ✅ Easier to test with mock factories");
        System.out.println();
        
        System.out.println("⚠️  CONSIDERATIONS:");
        System.out.println("   • Client needs to choose appropriate factory");
        System.out.println("   • More setup required compared to inheritance approach");
        System.out.println("   • Factory selection logic might be scattered");
    }
}