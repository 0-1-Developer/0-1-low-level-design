package com.example.abstractfactory.classic;

import com.example.abstractfactory.common.*;

/**
 * Concrete factory that creates a family of macOS-themed UI components.
 * Ensures all created components are compatible and maintain macOS design consistency.
 */
public class MacOSFactory implements GUIFactory {
    @Override
    public Button createButton() {
        return new MacOSButton();
    }
    
    @Override
    public Checkbox createCheckbox() {
        return new MacOSCheckbox();
    }
    
    @Override
    public ScrollBar createScrollBar() {
        return new MacOSScrollBar();
    }
}