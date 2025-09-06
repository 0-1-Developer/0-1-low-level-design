package com.example.abstractfactory.config;

import com.example.abstractfactory.common.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Demonstrates Config-driven Abstract Factory.
 * Shows how external configuration can drive factory behavior.
 */
public class ConfigDrivenFactoryDemo {
    
    private static void testFactory(ConfigDrivenFactory factory) {
        System.out.println("\n=== Testing " + factory.getThemeName() + " Theme ===");
        
        Button button = factory.createButton();
        Checkbox checkbox = factory.createCheckbox();
        ScrollBar scrollBar = factory.createScrollBar();
        
        button.render();
        checkbox.render();
        scrollBar.render();
        
        System.out.println("\nInteractions:");
        button.onClick();
        checkbox.check();
        scrollBar.scrollTo(45);
        
        System.out.println("\nFamily consistency:");
        boolean consistent = button.getStyle().equals(checkbox.getStyle()) &&
                           checkbox.getStyle().equals(scrollBar.getStyle());
        System.out.println("All components from " + button.getStyle() + " family: " + consistent);
    }
    
    private static void demonstrateEnvironmentConfig() {
        System.out.println("\n=== Environment-based Configuration ===");
        System.out.println("Attempting to load theme from UI_THEME environment variable...");
        
        ThemeConfig config = ThemeConfig.ConfigLoader.loadFromEnvironment();
        ConfigDrivenFactory factory = new ConfigDrivenFactory(config);
        testFactory(factory);
    }
    
    private static void demonstrateFileConfig() {
        System.out.println("\n=== File-based Configuration ===");
        
        String[] configFiles = {
            "config/enterprise-theme.json",
            "config/startup-theme.yaml",
            "config/default-theme.properties"
        };
        
        for (String file : configFiles) {
            ThemeConfig config = ThemeConfig.ConfigLoader.loadFromFile(file);
            ConfigDrivenFactory factory = new ConfigDrivenFactory(config);
            
            System.out.println("\nLoaded from: " + file);
            Button button = factory.createButton();
            button.render();
        }
    }
    
    private static void demonstratePropertiesConfig() {
        System.out.println("\n=== Properties-based Configuration ===");
        
        Map<String, String> properties = new HashMap<>();
        properties.put("theme.name", "Custom Properties Theme");
        properties.put("factory.type", "dynamic");
        properties.put("component.button.class", "com.example.abstractfactory.config.StartupButton");
        properties.put("component.button.color", "neon");
        properties.put("component.button.animation", "pulse");
        properties.put("component.checkbox.class", "com.example.abstractfactory.config.EnterpriseCheckbox");
        properties.put("component.scrollbar.class", "com.example.abstractfactory.config.DefaultScrollBar");
        
        ThemeConfig config = ThemeConfig.ConfigLoader.loadFromProperties(properties);
        ConfigDrivenFactory factory = new ConfigDrivenFactory(config);
        
        System.out.println("Created mixed-theme factory from properties");
        Button button = factory.createButton();
        Checkbox checkbox = factory.createCheckbox();
        ScrollBar scrollBar = factory.createScrollBar();
        
        button.render();
        checkbox.render();
        scrollBar.render();
        
        System.out.println("\nNote: Mixed themes may not be visually consistent");
        System.out.println("Button: " + button.getStyle());
        System.out.println("Checkbox: " + checkbox.getStyle());
        System.out.println("ScrollBar: " + scrollBar.getStyle());
    }
    
    private static void demonstrateConfigReload() {
        System.out.println("\n=== Configuration Hot-Reload Demo ===");
        
        ThemeConfig initialConfig = ThemeConfig.ConfigLoader.loadFromFile("config/default-theme.properties");
        ConfigDrivenFactory factory = new ConfigDrivenFactory(initialConfig);
        
        System.out.println("Initial configuration:");
        Button button1 = factory.createButton();
        button1.render();
        
        System.out.println("\nSimulating configuration change...");
        ThemeConfig newConfig = ThemeConfig.ConfigLoader.loadFromFile("config/enterprise-theme.json");
        factory.reloadConfig(newConfig);
        
        System.out.println("After reload:");
        Button button2 = factory.createButton();
        button2.render();
        
        System.out.println("\nButtons created before and after config change:");
        System.out.println("Before: " + button1.getStyle());
        System.out.println("After: " + button2.getStyle());
    }
    
    public static void main(String[] args) {
        System.out.println("===== Config-driven Abstract Factory Demo =====");
        
        // Test predefined configurations
        ThemeConfig defaultConfig = ThemeConfig.ConfigLoader.loadFromFile("default");
        testFactory(new ConfigDrivenFactory(defaultConfig));
        
        ThemeConfig enterpriseConfig = ThemeConfig.ConfigLoader.loadFromFile("enterprise");
        testFactory(new ConfigDrivenFactory(enterpriseConfig));
        
        ThemeConfig startupConfig = ThemeConfig.ConfigLoader.loadFromFile("startup");
        testFactory(new ConfigDrivenFactory(startupConfig));
        
        // Demonstrate different configuration sources
        demonstrateEnvironmentConfig();
        demonstrateFileConfig();
        demonstratePropertiesConfig();
        demonstrateConfigReload();
        
        System.out.println("\n=== Config-driven Factory Benefits ===");
        System.out.println("- Theme switching without code changes");
        System.out.println("- Multi-tenant support with different configs");
        System.out.println("- A/B testing with configuration flags");
        System.out.println("- Easy deployment of UI updates");
        
        System.out.println("\n=== Considerations ===");
        System.out.println("- Configuration validation required");
        System.out.println("- Error handling for missing/invalid configs");
        System.out.println("- Performance impact of dynamic loading");
        System.out.println("- Security concerns with external configs");
    }
}