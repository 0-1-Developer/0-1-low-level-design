package com.example.abstractfactory.registry;

import com.example.abstractfactory.common.*;

/**
 * Abstract Factory that delegates creation to a registry.
 * Allows dynamic factory selection and runtime registration.
 */
public class RegistryBackedFactory {
    private final UIComponentRegistry registry;
    private final String themeName;
    private UIComponentRegistry.FactoryBundle bundle;
    
    public RegistryBackedFactory(String themeName) {
        this(themeName, UIComponentRegistry.getInstance());
    }
    
    public RegistryBackedFactory(String themeName, UIComponentRegistry registry) {
        this.themeName = themeName;
        this.registry = registry;
        this.bundle = registry.getFactory(themeName);
    }
    
    public Button createButton() {
        return bundle.createButton();
    }
    
    public Checkbox createCheckbox() {
        return bundle.createCheckbox();
    }
    
    public ScrollBar createScrollBar() {
        return bundle.createScrollBar();
    }
    
    public void switchTheme(String newTheme) {
        this.bundle = registry.getFactory(newTheme);
        System.out.println("Switched theme from " + themeName + " to " + newTheme);
    }
    
    public String getThemeName() {
        return themeName;
    }
}