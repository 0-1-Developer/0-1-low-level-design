package com.example.abstractfactory.parameterized;

/**
 * Enum defining the available theme styles.
 */
public enum ThemeStyle {
    MATERIAL("Material Design"),
    FLUENT("Fluent Design"),
    NEUMORPHIC("Neumorphic");
    
    private final String displayName;
    
    ThemeStyle(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}