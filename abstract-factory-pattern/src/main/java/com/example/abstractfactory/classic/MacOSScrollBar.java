package com.example.abstractfactory.classic;

import com.example.abstractfactory.common.ScrollBar;

/**
 * Concrete product: macOS-style scroll bar with specific styling and behavior.
 */
public class MacOSScrollBar implements ScrollBar {
    private int position = 0;
    private static final int MAX_POSITION = 100;
    
    @Override
    public void render() {
        System.out.println("Rendering macOS scroll bar with thin overlay style");
    }
    
    @Override
    public void scrollTo(int position) {
        this.position = Math.max(0, Math.min(position, MAX_POSITION));
        System.out.println("macOS scroll bar smoothly glided to position: " + this.position);
    }
    
    @Override
    public int getPosition() {
        return position;
    }
    
    @Override
    public String getStyle() {
        return "macOS";
    }
}