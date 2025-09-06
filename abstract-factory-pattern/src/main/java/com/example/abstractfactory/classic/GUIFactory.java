package com.example.abstractfactory.classic;

import com.example.abstractfactory.common.*;

/**
 * Abstract Factory interface defining methods to create a family of related UI components.
 * Each concrete factory ensures that all components it creates work together harmoniously.
 */
public interface GUIFactory {
    Button createButton();
    Checkbox createCheckbox();
    ScrollBar createScrollBar();
}