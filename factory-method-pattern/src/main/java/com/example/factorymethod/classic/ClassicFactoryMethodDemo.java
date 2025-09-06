package com.example.factorymethod.classic;

import java.util.Arrays;
import java.util.List;

public class ClassicFactoryMethodDemo {
    
    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("🏭 FACTORY METHOD PATTERN - CLASSIC INHERITANCE DEMO");
        System.out.println("=".repeat(70));
        System.out.println();
        
        System.out.println("📋 This demo shows the classic Factory Method pattern where:");
        System.out.println("   • Abstract creator (Application) defines the factory method");
        System.out.println("   • Concrete creators override the factory method to create products");
        System.out.println("   • Client code works with creators, not products directly");
        System.out.println();
        
        List<Application> applications = Arrays.asList(
            new TextEditor(),
            new PdfReader(),
            new WordProcessor()
        );
        
        for (Application app : applications) {
            System.out.println("📱 Running " + app.getApplicationName() + ":");
            app.processDocument("Meeting Notes", "Today's agenda: Design patterns discussion");
        }
        
        System.out.println("🎯 KEY BENEFITS:");
        System.out.println("   ✅ Separation of object creation from usage");
        System.out.println("   ✅ Easy to add new document types without changing existing code");
        System.out.println("   ✅ Each application controls its own document creation logic");
        System.out.println("   ✅ Follows Open/Closed Principle - open for extension, closed for modification");
        System.out.println();
        
        System.out.println("⚠️  CONSIDERATIONS:");
        System.out.println("   • Requires inheritance hierarchy");
        System.out.println("   • Each product type needs its own creator");
        System.out.println("   • Can lead to class explosion with many product variants");
    }
}