package com.example.abstractfactory.config;

import com.example.abstractfactory.common.*;
import java.lang.reflect.Constructor;
import java.util.Map;

/**
 * Config-driven Abstract Factory that creates products based on external configuration.
 * Allows runtime theme switching without code changes.
 */
public class ConfigDrivenFactory {
    private ThemeConfig config;
    
    public ConfigDrivenFactory(ThemeConfig config) {
        this.config = config;
        System.out.println("Initialized factory with theme: " + config.getThemeName());
    }
    
    public Button createButton() {
        return createComponent("button", Button.class);
    }
    
    public Checkbox createCheckbox() {
        return createComponent("checkbox", Checkbox.class);
    }
    
    public ScrollBar createScrollBar() {
        return createComponent("scrollbar", ScrollBar.class);
    }
    
    @SuppressWarnings("unchecked")
    private <T> T createComponent(String type, Class<T> expectedType) {
        ThemeConfig.ComponentConfig componentConfig = config.getComponentConfig(type);
        
        if (componentConfig == null) {
            throw new IllegalArgumentException("No configuration found for component: " + type);
        }
        
        try {
            Class<?> clazz = Class.forName(componentConfig.getClassName());
            
            if (!expectedType.isAssignableFrom(clazz)) {
                throw new IllegalArgumentException(
                    "Class " + componentConfig.getClassName() + " does not implement " + expectedType.getName()
                );
            }
            
            // Try constructor with properties map first
            try {
                Constructor<?> constructor = clazz.getConstructor(Map.class);
                return (T) constructor.newInstance(componentConfig.getProperties());
            } catch (NoSuchMethodException e) {
                // Fall back to default constructor
                Constructor<?> constructor = clazz.getConstructor();
                T instance = (T) constructor.newInstance();
                
                // Apply properties if possible
                if (instance instanceof Configurable) {
                    ((Configurable) instance).configure(componentConfig.getProperties());
                }
                
                return instance;
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to create component: " + type, e);
        }
    }
    
    public void reloadConfig(ThemeConfig newConfig) {
        this.config = newConfig;
        System.out.println("Reloaded configuration. New theme: " + newConfig.getThemeName());
    }
    
    public String getThemeName() {
        return config.getThemeName();
    }
}

/**
 * Interface for components that can be configured after creation.
 */
interface Configurable {
    void configure(Map<String, String> properties);
}

// Default theme components
class DefaultButton implements Button, Configurable {
    private String color = "gray";
    private String size = "medium";
    
    public DefaultButton() {}
    
    public DefaultButton(Map<String, String> properties) {
        configure(properties);
    }
    
    @Override
    public void configure(Map<String, String> properties) {
        this.color = properties.getOrDefault("color", "gray");
        this.size = properties.getOrDefault("size", "medium");
    }
    
    @Override
    public void render() {
        System.out.println("Rendering default button - " + color + " color, " + size + " size");
    }
    
    @Override
    public void onClick() {
        System.out.println("Default button clicked");
    }
    
    @Override
    public String getStyle() {
        return "Default";
    }
}

class DefaultCheckbox implements Checkbox, Configurable {
    private boolean checked = false;
    private String style = "square";
    
    public DefaultCheckbox() {}
    
    public DefaultCheckbox(Map<String, String> properties) {
        configure(properties);
    }
    
    @Override
    public void configure(Map<String, String> properties) {
        this.style = properties.getOrDefault("style", "square");
    }
    
    @Override
    public void render() {
        System.out.println("Rendering default checkbox - " + style + " style");
    }
    
    @Override
    public void check() {
        checked = true;
        System.out.println("Default checkbox checked");
    }
    
    @Override
    public void uncheck() {
        checked = false;
        System.out.println("Default checkbox unchecked");
    }
    
    @Override
    public boolean isChecked() {
        return checked;
    }
    
    @Override
    public String getStyle() {
        return "Default";
    }
}

class DefaultScrollBar implements ScrollBar, Configurable {
    private int position = 0;
    private String width = "standard";
    
    public DefaultScrollBar() {}
    
    public DefaultScrollBar(Map<String, String> properties) {
        configure(properties);
    }
    
    @Override
    public void configure(Map<String, String> properties) {
        this.width = properties.getOrDefault("width", "standard");
    }
    
    @Override
    public void render() {
        System.out.println("Rendering default scroll bar - " + width + " width");
    }
    
    @Override
    public void scrollTo(int position) {
        this.position = Math.max(0, Math.min(position, 100));
        System.out.println("Default scroll bar at: " + this.position);
    }
    
    @Override
    public int getPosition() {
        return position;
    }
    
    @Override
    public String getStyle() {
        return "Default";
    }
}

// Enterprise theme components
class EnterpriseButton implements Button, Configurable {
    private String color = "blue";
    private String font = "Arial";
    private String size = "large";
    
    public EnterpriseButton() {}
    
    public EnterpriseButton(Map<String, String> properties) {
        configure(properties);
    }
    
    @Override
    public void configure(Map<String, String> properties) {
        this.color = properties.getOrDefault("color", "blue");
        this.font = properties.getOrDefault("font", "Arial");
        this.size = properties.getOrDefault("size", "large");
    }
    
    @Override
    public void render() {
        System.out.println("Rendering enterprise button - " + color + ", " + font + " font, " + size);
    }
    
    @Override
    public void onClick() {
        System.out.println("Enterprise button clicked - logging action");
    }
    
    @Override
    public String getStyle() {
        return "Enterprise";
    }
}

class EnterpriseCheckbox implements Checkbox, Configurable {
    private boolean checked = false;
    private String validation = "strict";
    
    public EnterpriseCheckbox() {}
    
    public EnterpriseCheckbox(Map<String, String> properties) {
        configure(properties);
    }
    
    @Override
    public void configure(Map<String, String> properties) {
        this.validation = properties.getOrDefault("validation", "strict");
    }
    
    @Override
    public void render() {
        System.out.println("Rendering enterprise checkbox - " + validation + " validation");
    }
    
    @Override
    public void check() {
        checked = true;
        System.out.println("Enterprise checkbox checked - audit logged");
    }
    
    @Override
    public void uncheck() {
        checked = false;
        System.out.println("Enterprise checkbox unchecked - audit logged");
    }
    
    @Override
    public boolean isChecked() {
        return checked;
    }
    
    @Override
    public String getStyle() {
        return "Enterprise";
    }
}

class EnterpriseScrollBar implements ScrollBar, Configurable {
    private int position = 0;
    private boolean smooth = true;
    
    public EnterpriseScrollBar() {}
    
    public EnterpriseScrollBar(Map<String, String> properties) {
        configure(properties);
    }
    
    @Override
    public void configure(Map<String, String> properties) {
        this.smooth = Boolean.parseBoolean(properties.getOrDefault("smooth", "true"));
    }
    
    @Override
    public void render() {
        System.out.println("Rendering enterprise scroll bar - smooth: " + smooth);
    }
    
    @Override
    public void scrollTo(int position) {
        this.position = Math.max(0, Math.min(position, 100));
        String scrollType = smooth ? "smoothly scrolled" : "jumped";
        System.out.println("Enterprise scroll bar " + scrollType + " to: " + this.position);
    }
    
    @Override
    public int getPosition() {
        return position;
    }
    
    @Override
    public String getStyle() {
        return "Enterprise";
    }
}

// Startup theme components
class StartupButton implements Button, Configurable {
    private String color = "vibrant";
    private String animation = "bounce";
    
    public StartupButton() {}
    
    public StartupButton(Map<String, String> properties) {
        configure(properties);
    }
    
    @Override
    public void configure(Map<String, String> properties) {
        this.color = properties.getOrDefault("color", "vibrant");
        this.animation = properties.getOrDefault("animation", "bounce");
    }
    
    @Override
    public void render() {
        System.out.println("Rendering startup button - " + color + " with " + animation + " animation");
    }
    
    @Override
    public void onClick() {
        System.out.println("Startup button clicked - " + animation + " effect!");
    }
    
    @Override
    public String getStyle() {
        return "Startup";
    }
}

class StartupCheckbox implements Checkbox, Configurable {
    private boolean checked = false;
    private String style = "toggle";
    
    public StartupCheckbox() {}
    
    public StartupCheckbox(Map<String, String> properties) {
        configure(properties);
    }
    
    @Override
    public void configure(Map<String, String> properties) {
        this.style = properties.getOrDefault("style", "toggle");
    }
    
    @Override
    public void render() {
        System.out.println("Rendering startup checkbox - " + style + " switch");
    }
    
    @Override
    public void check() {
        checked = true;
        System.out.println("Startup checkbox toggled ON - satisfying animation");
    }
    
    @Override
    public void uncheck() {
        checked = false;
        System.out.println("Startup checkbox toggled OFF");
    }
    
    @Override
    public boolean isChecked() {
        return checked;
    }
    
    @Override
    public String getStyle() {
        return "Startup";
    }
}

class StartupScrollBar implements ScrollBar, Configurable {
    private int position = 0;
    private boolean autohide = true;
    
    public StartupScrollBar() {}
    
    public StartupScrollBar(Map<String, String> properties) {
        configure(properties);
    }
    
    @Override
    public void configure(Map<String, String> properties) {
        this.autohide = Boolean.parseBoolean(properties.getOrDefault("autohide", "true"));
    }
    
    @Override
    public void render() {
        String visibility = autohide ? "auto-hiding" : "always visible";
        System.out.println("Rendering startup scroll bar - " + visibility);
    }
    
    @Override
    public void scrollTo(int position) {
        this.position = Math.max(0, Math.min(position, 100));
        System.out.println("Startup scroll bar glided to: " + this.position);
        if (autohide) {
            System.out.println("  (scroll bar will auto-hide after 2 seconds)");
        }
    }
    
    @Override
    public int getPosition() {
        return position;
    }
    
    @Override
    public String getStyle() {
        return "Startup";
    }
}