package com.example.abstractfactory.factorymethod;

import com.example.abstractfactory.common.*;

/**
 * Concrete factory using factory methods to create light theme components.
 */
public class LightThemeFactory extends AbstractUIFactory {
    
    @Override
    public Button createButton() {
        return new LightButton();
    }
    
    @Override
    public Checkbox createCheckbox() {
        return new LightCheckbox();
    }
    
    @Override
    public ScrollBar createScrollBar() {
        return new LightScrollBar();
    }
    
    @Override
    protected String getThemeName() {
        return "Light Theme";
    }
    
    private static class LightButton implements Button {
        @Override
        public void render() {
            System.out.println("Rendering light button with white background and dark text");
        }
        
        @Override
        public void onClick() {
            System.out.println("Light button clicked - ripple effect");
        }
        
        @Override
        public String getStyle() {
            return "Light";
        }
    }
    
    private static class LightCheckbox implements Checkbox {
        private boolean checked = false;
        
        @Override
        public void render() {
            System.out.println("Rendering light checkbox with thin border");
        }
        
        @Override
        public void check() {
            checked = true;
            System.out.println("Light checkbox checked - primary color accent");
        }
        
        @Override
        public void uncheck() {
            checked = false;
            System.out.println("Light checkbox unchecked");
        }
        
        @Override
        public boolean isChecked() {
            return checked;
        }
        
        @Override
        public String getStyle() {
            return "Light";
        }
    }
    
    private static class LightScrollBar implements ScrollBar {
        private int position = 0;
        
        @Override
        public void render() {
            System.out.println("Rendering light scroll bar with visible track");
        }
        
        @Override
        public void scrollTo(int position) {
            this.position = Math.max(0, Math.min(position, 100));
            System.out.println("Light scroll bar position: " + this.position);
        }
        
        @Override
        public int getPosition() {
            return position;
        }
        
        @Override
        public String getStyle() {
            return "Light";
        }
    }
}