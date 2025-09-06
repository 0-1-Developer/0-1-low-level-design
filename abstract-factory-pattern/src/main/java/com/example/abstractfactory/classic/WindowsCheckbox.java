package com.example.abstractfactory.classic;

import com.example.abstractfactory.common.Checkbox;

/**
 * Concrete product: Windows-style checkbox with specific styling and behavior.
 */
public class WindowsCheckbox implements Checkbox {
    private boolean checked = false;
    
    @Override
    public void render() {
        System.out.println("Rendering Windows checkbox with square mark and 3D border");
    }
    
    @Override
    public void check() {
        checked = true;
        System.out.println("Windows checkbox checked - displaying checkmark");
    }
    
    @Override
    public void uncheck() {
        checked = false;
        System.out.println("Windows checkbox unchecked - removing checkmark");
    }
    
    @Override
    public boolean isChecked() {
        return checked;
    }
    
    @Override
    public String getStyle() {
        return "Windows";
    }
}