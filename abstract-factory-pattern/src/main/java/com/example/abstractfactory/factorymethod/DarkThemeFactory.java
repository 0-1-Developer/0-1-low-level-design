package com.example.abstractfactory.factorymethod;

import com.example.abstractfactory.common.*;

/**
 * Concrete factory using factory methods to create dark theme components.
 */
public class DarkThemeFactory extends AbstractUIFactory {
    
    @Override
    public Button createButton() {
        return new DarkButton();
    }
    
    @Override
    public Checkbox createCheckbox() {
        return new DarkCheckbox();
    }
    
    @Override
    public ScrollBar createScrollBar() {
        return new DarkScrollBar();
    }
    
    @Override
    protected String getThemeName() {
        return "Dark Theme";
    }
    
    private static class DarkButton implements Button {
        @Override
        public void render() {
            System.out.println("Rendering dark button with black background and white text");
        }
        
        @Override
        public void onClick() {
            System.out.println("Dark button clicked - subtle glow effect");
        }
        
        @Override
        public String getStyle() {
            return "Dark";
        }
    }
    
    private static class DarkCheckbox implements Checkbox {
        private boolean checked = false;
        
        @Override
        public void render() {
            System.out.println("Rendering dark checkbox with subtle border");
        }
        
        @Override
        public void check() {
            checked = true;
            System.out.println("Dark checkbox checked - neon accent color");
        }
        
        @Override
        public void uncheck() {
            checked = false;
            System.out.println("Dark checkbox unchecked");
        }
        
        @Override
        public boolean isChecked() {
            return checked;
        }
        
        @Override
        public String getStyle() {
            return "Dark";
        }
    }
    
    private static class DarkScrollBar implements ScrollBar {
        private int position = 0;
        
        @Override
        public void render() {
            System.out.println("Rendering dark scroll bar with minimal design");
        }
        
        @Override
        public void scrollTo(int position) {
            this.position = Math.max(0, Math.min(position, 100));
            System.out.println("Dark scroll bar position: " + this.position);
        }
        
        @Override
        public int getPosition() {
            return position;
        }
        
        @Override
        public String getStyle() {
            return "Dark";
        }
    }
}