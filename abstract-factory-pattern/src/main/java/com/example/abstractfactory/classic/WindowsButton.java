package com.example.abstractfactory.classic;

import com.example.abstractfactory.common.Button;

/**
 * Concrete product: Windows-style button with specific styling and behavior.
 */
public class WindowsButton implements Button {
    @Override
    public void render() {
        System.out.println("Rendering Windows button with square corners and gradient background");
    }
    
    @Override
    public void onClick() {
        System.out.println("Windows button clicked - playing system sound");
    }
    
    @Override
    public String getStyle() {
        return "Windows";
    }
}