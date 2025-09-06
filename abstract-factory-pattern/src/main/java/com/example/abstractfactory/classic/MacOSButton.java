package com.example.abstractfactory.classic;

import com.example.abstractfactory.common.Button;

/**
 * Concrete product: macOS-style button with specific styling and behavior.
 */
public class MacOSButton implements Button {
    @Override
    public void render() {
        System.out.println("Rendering macOS button with rounded corners and aqua style");
    }
    
    @Override
    public void onClick() {
        System.out.println("macOS button clicked - smooth animation with haptic feedback");
    }
    
    @Override
    public String getStyle() {
        return "macOS";
    }
}