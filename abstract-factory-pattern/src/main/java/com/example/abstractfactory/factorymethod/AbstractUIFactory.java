package com.example.abstractfactory.factorymethod;

import com.example.abstractfactory.common.*;

/**
 * Abstract factory with abstract factory methods.
 * Each method is a factory method that subclasses will implement.
 */
public abstract class AbstractUIFactory {
    
    public abstract Button createButton();
    
    public abstract Checkbox createCheckbox();
    
    public abstract ScrollBar createScrollBar();
    
    /**
     * Template method that can provide additional behavior around creation.
     */
    public void createAndConfigureUI() {
        Button button = createButton();
        Checkbox checkbox = createCheckbox();
        ScrollBar scrollBar = createScrollBar();
        
        System.out.println("Created UI family: " + getThemeName());
        button.render();
        checkbox.render();
        scrollBar.render();
    }
    
    protected abstract String getThemeName();
}