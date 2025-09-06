package com.example.abstractfactory.classic;

import com.example.abstractfactory.common.*;

/**
 * Concrete factory that creates a family of Windows-themed UI components.
 * Ensures all created components are compatible and maintain Windows design consistency.
 */
public class WindowsFactory implements GUIFactory {
    @Override
    public Button createButton() {
        return new WindowsButton();
    }
    
    @Override
    public Checkbox createCheckbox() {
        return new WindowsCheckbox();
    }
    
    @Override
    public ScrollBar createScrollBar() {
        return new WindowsScrollBar();
    }
}