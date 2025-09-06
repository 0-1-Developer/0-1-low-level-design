package com.example.abstractfactory.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for theme settings.
 * In a real application, this would be loaded from JSON/YAML/properties files.
 */
public class ThemeConfig {
    private String themeName;
    private String factoryType;
    private Map<String, ComponentConfig> components = new HashMap<>();
    
    public static class ComponentConfig {
        private String className;
        private Map<String, String> properties = new HashMap<>();
        
        public ComponentConfig(String className) {
            this.className = className;
        }
        
        public ComponentConfig withProperty(String key, String value) {
            properties.put(key, value);
            return this;
        }
        
        public String getClassName() {
            return className;
        }
        
        public Map<String, String> getProperties() {
            return properties;
        }
    }
    
    public ThemeConfig(String themeName, String factoryType) {
        this.themeName = themeName;
        this.factoryType = factoryType;
    }
    
    public void addComponent(String type, ComponentConfig config) {
        components.put(type, config);
    }
    
    public String getThemeName() {
        return themeName;
    }
    
    public String getFactoryType() {
        return factoryType;
    }
    
    public ComponentConfig getComponentConfig(String type) {
        return components.get(type);
    }
    
    public Map<String, ComponentConfig> getComponents() {
        return components;
    }
    
    /**
     * Simulates loading configuration from different sources.
     */
    public static class ConfigLoader {
        
        public static ThemeConfig loadFromEnvironment() {
            String theme = System.getenv("UI_THEME");
            if (theme == null) {
                theme = "default";
            }
            
            switch (theme.toLowerCase()) {
                case "enterprise":
                    return getEnterpriseConfig();
                case "startup":
                    return getStartupConfig();
                default:
                    return getDefaultConfig();
            }
        }
        
        public static ThemeConfig loadFromFile(String filename) {
            // Simulate file-based configuration
            System.out.println("Loading configuration from: " + filename);
            
            if (filename.contains("enterprise")) {
                return getEnterpriseConfig();
            } else if (filename.contains("startup")) {
                return getStartupConfig();
            }
            
            return getDefaultConfig();
        }
        
        public static ThemeConfig loadFromProperties(Map<String, String> properties) {
            String themeName = properties.getOrDefault("theme.name", "Custom");
            String factoryType = properties.getOrDefault("factory.type", "standard");
            
            ThemeConfig config = new ThemeConfig(themeName, factoryType);
            
            // Parse component configurations
            String buttonClass = properties.get("component.button.class");
            if (buttonClass != null) {
                ComponentConfig buttonConfig = new ComponentConfig(buttonClass);
                buttonConfig.withProperty("color", properties.getOrDefault("component.button.color", "default"));
                config.addComponent("button", buttonConfig);
            }
            
            return config;
        }
        
        private static ThemeConfig getDefaultConfig() {
            ThemeConfig config = new ThemeConfig("Default", "standard");
            
            String pkg = "com.example.abstractfactory.config.";
            config.addComponent("button", 
                new ComponentConfig(pkg + "DefaultButton")
                    .withProperty("color", "gray")
                    .withProperty("size", "medium"));
            
            config.addComponent("checkbox",
                new ComponentConfig(pkg + "DefaultCheckbox")
                    .withProperty("style", "square"));
            
            config.addComponent("scrollbar",
                new ComponentConfig(pkg + "DefaultScrollBar")
                    .withProperty("width", "standard"));
            
            return config;
        }
        
        private static ThemeConfig getEnterpriseConfig() {
            ThemeConfig config = new ThemeConfig("Enterprise", "professional");
            
            String pkg = "com.example.abstractfactory.config.";
            config.addComponent("button",
                new ComponentConfig(pkg + "EnterpriseButton")
                    .withProperty("color", "blue")
                    .withProperty("font", "Arial")
                    .withProperty("size", "large"));
            
            config.addComponent("checkbox",
                new ComponentConfig(pkg + "EnterpriseCheckbox")
                    .withProperty("validation", "strict"));
            
            config.addComponent("scrollbar",
                new ComponentConfig(pkg + "EnterpriseScrollBar")
                    .withProperty("smooth", "true"));
            
            return config;
        }
        
        private static ThemeConfig getStartupConfig() {
            ThemeConfig config = new ThemeConfig("Startup", "modern");
            
            String pkg = "com.example.abstractfactory.config.";
            config.addComponent("button",
                new ComponentConfig(pkg + "StartupButton")
                    .withProperty("color", "vibrant")
                    .withProperty("animation", "bounce"));
            
            config.addComponent("checkbox",
                new ComponentConfig(pkg + "StartupCheckbox")
                    .withProperty("style", "toggle"));
            
            config.addComponent("scrollbar",
                new ComponentConfig(pkg + "StartupScrollBar")
                    .withProperty("autohide", "true"));
            
            return config;
        }
    }
}