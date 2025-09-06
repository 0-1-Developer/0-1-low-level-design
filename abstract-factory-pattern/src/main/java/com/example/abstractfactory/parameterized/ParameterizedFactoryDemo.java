package com.example.abstractfactory.parameterized;

import com.example.abstractfactory.common.*;

/**
 * Demonstrates the Parameterized Abstract Factory pattern.
 * Shows how parameters can be used to select product variants while maintaining family consistency.
 */
public class ParameterizedFactoryDemo {
    
    private static void demonstrateTheme(ThemeStyle theme) {
        System.out.println("\n=== " + theme.getDisplayName() + " Theme ===");
        
        ParameterizedUIFactory factory = new ParameterizedUIFactory(theme);
        
        Button button = factory.createButton();
        Checkbox checkbox = factory.createCheckbox();
        ScrollBar scrollBar = factory.createScrollBar();
        
        button.render();
        checkbox.render();
        scrollBar.render();
        
        System.out.println("\nInteractions:");
        button.onClick();
        checkbox.check();
        scrollBar.scrollTo(30);
        
        System.out.println("\nFamily consistency check:");
        System.out.println("Button style: " + button.getStyle());
        System.out.println("Checkbox style: " + checkbox.getStyle());
        System.out.println("ScrollBar style: " + scrollBar.getStyle());
        
        boolean consistent = button.getStyle().equals(checkbox.getStyle()) &&
                           checkbox.getStyle().equals(scrollBar.getStyle());
        System.out.println("All from same family: " + consistent);
    }
    
    private static void demonstrateGenericCreation() {
        System.out.println("\n=== Generic Component Creation ===");
        
        ParameterizedUIFactory factory = new ParameterizedUIFactory(ThemeStyle.MATERIAL);
        
        Button button = factory.createComponent(ComponentType.BUTTON);
        Checkbox checkbox = factory.createComponent(ComponentType.CHECKBOX);
        ScrollBar scrollBar = factory.createComponent(ComponentType.SCROLLBAR);
        
        System.out.println("Created components using generic method:");
        button.render();
        checkbox.render();
        scrollBar.render();
    }
    
    public static void main(String[] args) {
        System.out.println("===== Parameterized Abstract Factory Demo =====");
        
        for (ThemeStyle theme : ThemeStyle.values()) {
            demonstrateTheme(theme);
        }
        
        demonstrateGenericCreation();
        
        System.out.println("\n=== Dynamic Theme Selection Based on User Preference ===");
        
        String userPreference = "modern";
        ThemeStyle selectedTheme;
        
        switch (userPreference.toLowerCase()) {
            case "modern":
            case "google":
                selectedTheme = ThemeStyle.MATERIAL;
                break;
            case "microsoft":
            case "windows":
                selectedTheme = ThemeStyle.FLUENT;
                break;
            case "soft":
            case "3d":
                selectedTheme = ThemeStyle.NEUMORPHIC;
                break;
            default:
                selectedTheme = ThemeStyle.MATERIAL;
        }
        
        System.out.println("User preference: " + userPreference);
        System.out.println("Selected theme: " + selectedTheme.getDisplayName());
        
        ParameterizedUIFactory userFactory = new ParameterizedUIFactory(selectedTheme);
        Button userButton = userFactory.createButton();
        userButton.render();
        userButton.onClick();
    }
}