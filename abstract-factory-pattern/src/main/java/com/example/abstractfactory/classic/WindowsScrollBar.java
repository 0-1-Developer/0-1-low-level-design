package com.example.abstractfactory.classic;

import com.example.abstractfactory.common.ScrollBar;

/**
 * Concrete product: Windows-style scroll bar with specific styling and behavior.
 */
public class WindowsScrollBar implements ScrollBar {
    private int position = 0;
    private static final int MAX_POSITION = 100;
    
    @Override
    public void render() {
        System.out.println("Rendering Windows scroll bar with arrows and track");
    }
    
    @Override
    public void scrollTo(int position) {
        this.position = Math.max(0, Math.min(position, MAX_POSITION));
        System.out.println("Windows scroll bar moved to position: " + this.position);
    }
    
    @Override
    public int getPosition() {
        return position;
    }
    
    @Override
    public String getStyle() {
        return "Windows";
    }
}