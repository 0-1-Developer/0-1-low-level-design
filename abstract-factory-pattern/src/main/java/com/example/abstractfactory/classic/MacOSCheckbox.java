package com.example.abstractfactory.classic;

import com.example.abstractfactory.common.Checkbox;

/**
 * Concrete product: macOS-style checkbox with specific styling and behavior.
 */
public class MacOSCheckbox implements Checkbox {
    private boolean checked = false;
    
    @Override
    public void render() {
        System.out.println("Rendering macOS checkbox with rounded corners and blue checkmark");
    }
    
    @Override
    public void check() {
        checked = true;
        System.out.println("macOS checkbox checked - animated checkmark with bounce effect");
    }
    
    @Override
    public void uncheck() {
        checked = false;
        System.out.println("macOS checkbox unchecked - smooth fade out animation");
    }
    
    @Override
    public boolean isChecked() {
        return checked;
    }
    
    @Override
    public String getStyle() {
        return "macOS";
    }
}