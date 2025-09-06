package com.example.abstractfactory.registry;

import com.example.abstractfactory.common.*;

/**
 * Demonstrates Registry-backed Abstract Factory.
 * Shows dynamic registration and runtime factory selection.
 */
public class RegistryFactoryDemo {
    
    private static void testFactory(String themeName) {
        System.out.println("\n=== Testing theme: " + themeName + " ===");
        
        RegistryBackedFactory factory = new RegistryBackedFactory(themeName);
        
        Button button = factory.createButton();
        Checkbox checkbox = factory.createCheckbox();
        ScrollBar scrollBar = factory.createScrollBar();
        
        button.render();
        checkbox.render();
        scrollBar.render();
        
        System.out.println("\nInteractions:");
        button.onClick();
        checkbox.check();
        scrollBar.scrollTo(42);
        
        System.out.println("\nFamily consistency:");
        boolean consistent = button.getStyle().equals(checkbox.getStyle()) &&
                           checkbox.getStyle().equals(scrollBar.getStyle());
        System.out.println("All components from " + button.getStyle() + " family: " + consistent);
    }
    
    private static void demonstratePluginRegistration() {
        System.out.println("\n=== Plugin Registration Demo ===");
        
        UIComponentRegistry registry = UIComponentRegistry.getInstance();
        
        // Register a custom "Glassmorphic" theme at runtime
        registry.registerFactory("glassmorphic", new UIComponentRegistry.FactoryBundle(
            () -> new Button() {
                @Override
                public void render() {
                    System.out.println("Rendering glassmorphic button - translucent with blur");
                }
                
                @Override
                public void onClick() {
                    System.out.println("Glassmorphic button clicked - glass shatter effect");
                }
                
                @Override
                public String getStyle() {
                    return "Glassmorphic";
                }
            },
            () -> new Checkbox() {
                private boolean checked = false;
                
                @Override
                public void render() {
                    System.out.println("Rendering glassmorphic checkbox - frosted glass");
                }
                
                @Override
                public void check() {
                    checked = true;
                    System.out.println("Glassmorphic checkbox checked - refraction effect");
                }
                
                @Override
                public void uncheck() {
                    checked = false;
                    System.out.println("Glassmorphic checkbox unchecked");
                }
                
                @Override
                public boolean isChecked() {
                    return checked;
                }
                
                @Override
                public String getStyle() {
                    return "Glassmorphic";
                }
            },
            () -> new ScrollBar() {
                private int position = 0;
                
                @Override
                public void render() {
                    System.out.println("Rendering glassmorphic scroll bar - transparent track");
                }
                
                @Override
                public void scrollTo(int position) {
                    this.position = Math.max(0, Math.min(position, 100));
                    System.out.println("Glassmorphic scroll bar at: " + this.position);
                }
                
                @Override
                public int getPosition() {
                    return position;
                }
                
                @Override
                public String getStyle() {
                    return "Glassmorphic";
                }
            }
        ));
        
        testFactory("glassmorphic");
    }
    
    public static void main(String[] args) {
        System.out.println("===== Registry-backed Abstract Factory Demo =====");
        
        // Test pre-registered factories
        testFactory("minimal");
        testFactory("retro");
        
        // Test non-existent theme (falls back to default)
        System.out.println("\n=== Testing Unknown Theme ===");
        System.out.println("Attempting to use 'cyberpunk' theme (not registered):");
        testFactory("cyberpunk");
        
        // Demonstrate runtime registration
        demonstratePluginRegistration();
        
        // Demonstrate theme switching
        System.out.println("\n=== Theme Switching Demo ===");
        RegistryBackedFactory switchableFactory = new RegistryBackedFactory("minimal");
        
        System.out.println("Initial theme: minimal");
        Button button = switchableFactory.createButton();
        button.render();
        
        switchableFactory.switchTheme("retro");
        Button retroButton = switchableFactory.createButton();
        retroButton.render();
    }
}