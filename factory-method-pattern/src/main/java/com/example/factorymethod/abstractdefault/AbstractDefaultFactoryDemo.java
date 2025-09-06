package com.example.factorymethod.abstractdefault;

import java.util.Arrays;
import java.util.List;

public class AbstractDefaultFactoryDemo {
    
    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("🏭 FACTORY METHOD PATTERN - ABSTRACT DEFAULT DEMO");
        System.out.println("=".repeat(70));
        System.out.println();
        
        System.out.println("📋 This demo shows abstract creator with default factory method:");
        System.out.println("   • Abstract base class provides default implementation");
        System.out.println("   • Subclasses can use default or override as needed");
        System.out.println("   • Reduces boilerplate when most subclasses use same factory");
        System.out.println("   • Provides sensible defaults while allowing customization");
        System.out.println();
        
        List<AbstractDocumentCreator> creators = Arrays.asList(
            new DefaultTextCreator(),
            new SpecializedPdfCreator()
        );
        
        for (AbstractDocumentCreator creator : creators) {
            System.out.println("🎯 Testing " + creator.getCreatorType());
            creator.processDocument("Sample Document", "Content for " + creator.getCreatorType());
        }
        
        System.out.println("🎯 KEY BENEFITS:");
        System.out.println("   ✅ Reduces code duplication with sensible defaults");
        System.out.println("   ✅ Allows selective override only when needed");
        System.out.println("   ✅ Maintains flexibility of factory method pattern");
        System.out.println("   ✅ Simplifies common use cases");
        System.out.println();
        
        System.out.println("⚠️  CONSIDERATIONS:");
        System.out.println("   • Default might not be appropriate for all subclasses");
        System.out.println("   • Can hide factory method behavior from subclass implementers");
        System.out.println("   • Less explicit about creation responsibilities");
    }
}