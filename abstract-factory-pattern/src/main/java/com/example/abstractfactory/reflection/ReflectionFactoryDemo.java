package com.example.abstractfactory.reflection;

import com.example.abstractfactory.common.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Demonstrates Reflection-based Abstract Factory.
 * Shows dynamic class loading and configuration-driven instantiation.
 */
public class ReflectionFactoryDemo {
    
    private static void testTheme(String theme) {
        System.out.println("\n=== " + theme + " Theme (via Reflection) ===");
        
        try {
            ReflectionFactory factory = new ReflectionFactory(theme);
            
            Button button = factory.createButton();
            Checkbox checkbox = factory.createCheckbox();
            ScrollBar scrollBar = factory.createScrollBar();
            
            button.render();
            checkbox.render();
            scrollBar.render();
            
            System.out.println("\nInteractions:");
            button.onClick();
            checkbox.check();
            scrollBar.scrollTo(60);
            
            System.out.println("\nFamily consistency check:");
            boolean consistent = button.getStyle().equals(checkbox.getStyle()) &&
                               checkbox.getStyle().equals(scrollBar.getStyle());
            System.out.println("All components from " + button.getStyle() + " family: " + consistent);
            
        } catch (Exception e) {
            System.out.println("Error creating factory: " + e.getMessage());
        }
    }
    
    private static void demonstrateConfigDriven() {
        System.out.println("\n=== Configuration-Driven Factory ===");
        
        ReflectionFactory factory = new ReflectionFactory("corporate");
        
        // Simulate loading custom configuration
        Map<String, String> customConfig = new HashMap<>();
        String packagePrefix = "com.example.abstractfactory.reflection.";
        customConfig.put("button", packagePrefix + "GamingButton");
        customConfig.put("checkbox", packagePrefix + "CorporateCheckbox");
        customConfig.put("scrollbar", packagePrefix + "GamingScrollBar");
        
        System.out.println("Loading mixed configuration (Gaming button, Corporate checkbox, Gaming scrollbar):");
        factory.loadFromConfig(customConfig);
        
        Button button = factory.createButton();
        Checkbox checkbox = factory.createCheckbox();
        ScrollBar scrollBar = factory.createScrollBar();
        
        button.render();
        checkbox.render();
        scrollBar.render();
        
        System.out.println("\nNote: Mixed families may not be visually consistent!");
        System.out.println("Button style: " + button.getStyle());
        System.out.println("Checkbox style: " + checkbox.getStyle());
        System.out.println("ScrollBar style: " + scrollBar.getStyle());
    }
    
    private static void demonstrateErrorHandling() {
        System.out.println("\n=== Error Handling Demo ===");
        
        try {
            System.out.println("Attempting to create factory with invalid theme:");
            ReflectionFactory factory = new ReflectionFactory("nonexistent");
            factory.createButton();
        } catch (Exception e) {
            System.out.println("Caught exception: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
        
        try {
            System.out.println("\nAttempting to load invalid class mapping:");
            ReflectionFactory factory = new ReflectionFactory("corporate");
            Map<String, String> badConfig = new HashMap<>();
            badConfig.put("button", "com.example.NonExistentButton");
            factory.loadFromConfig(badConfig);
            factory.createButton();
        } catch (Exception e) {
            System.out.println("Caught exception: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        System.out.println("===== Reflection-based Abstract Factory Demo =====");
        
        testTheme("corporate");
        testTheme("gaming");
        
        demonstrateConfigDriven();
        demonstrateErrorHandling();
        
        System.out.println("\n=== Reflection Benefits & Risks ===");
        System.out.println("Benefits:");
        System.out.println("- Highly flexible, can load classes at runtime");
        System.out.println("- Configuration-driven, no compile-time dependencies");
        System.out.println("- Useful for plugin systems and frameworks");
        
        System.out.println("\nRisks:");
        System.out.println("- Type safety lost at compile time");
        System.out.println("- Performance overhead from reflection");
        System.out.println("- Harder to debug and trace");
        System.out.println("- Security concerns with dynamic class loading");
    }
}