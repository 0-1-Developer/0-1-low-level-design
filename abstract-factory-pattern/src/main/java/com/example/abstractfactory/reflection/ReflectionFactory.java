package com.example.abstractfactory.reflection;

import com.example.abstractfactory.common.*;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Reflection-based Abstract Factory that instantiates products using class names.
 * Useful for frameworks and configuration-driven scenarios.
 */
public class ReflectionFactory {
    private final Map<String, String> componentMappings = new HashMap<>();
    private final String theme;
    
    public ReflectionFactory(String theme) {
        this.theme = theme;
        loadMappings();
    }
    
    private void loadMappings() {
        String packagePrefix = "com.example.abstractfactory.reflection.";
        
        switch (theme.toLowerCase()) {
            case "corporate":
                componentMappings.put("button", packagePrefix + "CorporateButton");
                componentMappings.put("checkbox", packagePrefix + "CorporateCheckbox");
                componentMappings.put("scrollbar", packagePrefix + "CorporateScrollBar");
                break;
            case "gaming":
                componentMappings.put("button", packagePrefix + "GamingButton");
                componentMappings.put("checkbox", packagePrefix + "GamingCheckbox");
                componentMappings.put("scrollbar", packagePrefix + "GamingScrollBar");
                break;
            default:
                throw new IllegalArgumentException("Unknown theme: " + theme);
        }
    }
    
    @SuppressWarnings("unchecked")
    private <T> T createComponent(String componentType, Class<T> expectedType) {
        String className = componentMappings.get(componentType.toLowerCase());
        
        if (className == null) {
            throw new IllegalArgumentException("No mapping found for component: " + componentType);
        }
        
        try {
            Class<?> clazz = Class.forName(className);
            
            if (!expectedType.isAssignableFrom(clazz)) {
                throw new IllegalArgumentException(
                    "Class " + className + " does not implement " + expectedType.getName()
                );
            }
            
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            Object instance = constructor.newInstance();
            
            System.out.println("Created " + componentType + " via reflection: " + clazz.getSimpleName());
            
            return (T) instance;
            
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class not found: " + className, e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate: " + className, e);
        }
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
    
    public String getTheme() {
        return theme;
    }
    
    /**
     * Load mappings from external configuration (simulated).
     */
    public void loadFromConfig(Map<String, String> config) {
        componentMappings.clear();
        componentMappings.putAll(config);
        System.out.println("Loaded " + config.size() + " component mappings from config");
    }
}

// Corporate theme components
class CorporateButton implements Button {
    @Override
    public void render() {
        System.out.println("Rendering corporate button - professional blue with serif font");
    }
    
    @Override
    public void onClick() {
        System.out.println("Corporate button clicked - formal transition");
    }
    
    @Override
    public String getStyle() {
        return "Corporate";
    }
}

class CorporateCheckbox implements Checkbox {
    private boolean checked = false;
    
    @Override
    public void render() {
        System.out.println("Rendering corporate checkbox - conservative style");
    }
    
    @Override
    public void check() {
        checked = true;
        System.out.println("Corporate checkbox checked - checkmark icon");
    }
    
    @Override
    public void uncheck() {
        checked = false;
        System.out.println("Corporate checkbox unchecked");
    }
    
    @Override
    public boolean isChecked() {
        return checked;
    }
    
    @Override
    public String getStyle() {
        return "Corporate";
    }
}

class CorporateScrollBar implements ScrollBar {
    private int position = 0;
    
    @Override
    public void render() {
        System.out.println("Rendering corporate scroll bar - traditional style");
    }
    
    @Override
    public void scrollTo(int position) {
        this.position = Math.max(0, Math.min(position, 100));
        System.out.println("Corporate scroll bar at position: " + this.position);
    }
    
    @Override
    public int getPosition() {
        return position;
    }
    
    @Override
    public String getStyle() {
        return "Corporate";
    }
}

// Gaming theme components
class GamingButton implements Button {
    @Override
    public void render() {
        System.out.println("Rendering gaming button - RGB lighting with aggressive angles");
    }
    
    @Override
    public void onClick() {
        System.out.println("Gaming button clicked - explosion effect with sound");
    }
    
    @Override
    public String getStyle() {
        return "Gaming";
    }
}

class GamingCheckbox implements Checkbox {
    private boolean checked = false;
    
    @Override
    public void render() {
        System.out.println("Rendering gaming checkbox - hexagonal shape with glow");
    }
    
    @Override
    public void check() {
        checked = true;
        System.out.println("Gaming checkbox checked - power-up sound");
    }
    
    @Override
    public void uncheck() {
        checked = false;
        System.out.println("Gaming checkbox unchecked - power-down sound");
    }
    
    @Override
    public boolean isChecked() {
        return checked;
    }
    
    @Override
    public String getStyle() {
        return "Gaming";
    }
}

class GamingScrollBar implements ScrollBar {
    private int position = 0;
    
    @Override
    public void render() {
        System.out.println("Rendering gaming scroll bar - LED strip style");
    }
    
    @Override
    public void scrollTo(int position) {
        this.position = Math.max(0, Math.min(position, 100));
        System.out.println("Gaming scroll bar at: " + this.position + " (RGB pulse)");
    }
    
    @Override
    public int getPosition() {
        return position;
    }
    
    @Override
    public String getStyle() {
        return "Gaming";
    }
}