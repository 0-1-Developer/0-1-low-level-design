package com.example.abstractfactory.parameterized;

import com.example.abstractfactory.common.*;

/**
 * Parameterized Abstract Factory that uses parameters to determine which
 * concrete products to create while maintaining family consistency.
 */
public class ParameterizedUIFactory {
    private final ThemeStyle theme;
    
    public ParameterizedUIFactory(ThemeStyle theme) {
        this.theme = theme;
    }
    
    public Button createButton() {
        return createComponent(ComponentType.BUTTON);
    }
    
    public Checkbox createCheckbox() {
        return createComponent(ComponentType.CHECKBOX);
    }
    
    public ScrollBar createScrollBar() {
        return createComponent(ComponentType.SCROLLBAR);
    }
    
    @SuppressWarnings("unchecked")
    public <T> T createComponent(ComponentType type) {
        switch (theme) {
            case MATERIAL:
                return (T) createMaterialComponent(type);
            case FLUENT:
                return (T) createFluentComponent(type);
            case NEUMORPHIC:
                return (T) createNeumorphicComponent(type);
            default:
                throw new IllegalArgumentException("Unknown theme: " + theme);
        }
    }
    
    private Object createMaterialComponent(ComponentType type) {
        switch (type) {
            case BUTTON:
                return new MaterialButton();
            case CHECKBOX:
                return new MaterialCheckbox();
            case SCROLLBAR:
                return new MaterialScrollBar();
            default:
                throw new IllegalArgumentException("Unknown component type: " + type);
        }
    }
    
    private Object createFluentComponent(ComponentType type) {
        switch (type) {
            case BUTTON:
                return new FluentButton();
            case CHECKBOX:
                return new FluentCheckbox();
            case SCROLLBAR:
                return new FluentScrollBar();
            default:
                throw new IllegalArgumentException("Unknown component type: " + type);
        }
    }
    
    private Object createNeumorphicComponent(ComponentType type) {
        switch (type) {
            case BUTTON:
                return new NeumorphicButton();
            case CHECKBOX:
                return new NeumorphicCheckbox();
            case SCROLLBAR:
                return new NeumorphicScrollBar();
            default:
                throw new IllegalArgumentException("Unknown component type: " + type);
        }
    }
    
    public String getThemeName() {
        return theme.getDisplayName();
    }
    
    // Material Design components
    private static class MaterialButton implements Button {
        @Override
        public void render() {
            System.out.println("Rendering Material button with elevation and ripple effect");
        }
        
        @Override
        public void onClick() {
            System.out.println("Material button clicked - ripple animation");
        }
        
        @Override
        public String getStyle() {
            return "Material";
        }
    }
    
    private static class MaterialCheckbox implements Checkbox {
        private boolean checked = false;
        
        @Override
        public void render() {
            System.out.println("Rendering Material checkbox with animated checkmark");
        }
        
        @Override
        public void check() {
            checked = true;
            System.out.println("Material checkbox checked - slide animation");
        }
        
        @Override
        public void uncheck() {
            checked = false;
            System.out.println("Material checkbox unchecked - fade out");
        }
        
        @Override
        public boolean isChecked() {
            return checked;
        }
        
        @Override
        public String getStyle() {
            return "Material";
        }
    }
    
    private static class MaterialScrollBar implements ScrollBar {
        private int position = 0;
        
        @Override
        public void render() {
            System.out.println("Rendering Material scroll bar with touch-friendly size");
        }
        
        @Override
        public void scrollTo(int position) {
            this.position = Math.max(0, Math.min(position, 100));
            System.out.println("Material scroll bar at: " + this.position);
        }
        
        @Override
        public int getPosition() {
            return position;
        }
        
        @Override
        public String getStyle() {
            return "Material";
        }
    }
    
    // Fluent Design components
    private static class FluentButton implements Button {
        @Override
        public void render() {
            System.out.println("Rendering Fluent button with acrylic material and reveal highlight");
        }
        
        @Override
        public void onClick() {
            System.out.println("Fluent button clicked - reveal border effect");
        }
        
        @Override
        public String getStyle() {
            return "Fluent";
        }
    }
    
    private static class FluentCheckbox implements Checkbox {
        private boolean checked = false;
        
        @Override
        public void render() {
            System.out.println("Rendering Fluent checkbox with subtle depth");
        }
        
        @Override
        public void check() {
            checked = true;
            System.out.println("Fluent checkbox checked - smooth transition");
        }
        
        @Override
        public void uncheck() {
            checked = false;
            System.out.println("Fluent checkbox unchecked");
        }
        
        @Override
        public boolean isChecked() {
            return checked;
        }
        
        @Override
        public String getStyle() {
            return "Fluent";
        }
    }
    
    private static class FluentScrollBar implements ScrollBar {
        private int position = 0;
        
        @Override
        public void render() {
            System.out.println("Rendering Fluent scroll bar with parallax effect");
        }
        
        @Override
        public void scrollTo(int position) {
            this.position = Math.max(0, Math.min(position, 100));
            System.out.println("Fluent scroll bar at: " + this.position);
        }
        
        @Override
        public int getPosition() {
            return position;
        }
        
        @Override
        public String getStyle() {
            return "Fluent";
        }
    }
    
    // Neumorphic components
    private static class NeumorphicButton implements Button {
        @Override
        public void render() {
            System.out.println("Rendering Neumorphic button with soft shadow and embossed effect");
        }
        
        @Override
        public void onClick() {
            System.out.println("Neumorphic button clicked - pressed inset effect");
        }
        
        @Override
        public String getStyle() {
            return "Neumorphic";
        }
    }
    
    private static class NeumorphicCheckbox implements Checkbox {
        private boolean checked = false;
        
        @Override
        public void render() {
            System.out.println("Rendering Neumorphic checkbox with soft edges");
        }
        
        @Override
        public void check() {
            checked = true;
            System.out.println("Neumorphic checkbox checked - inset glow");
        }
        
        @Override
        public void uncheck() {
            checked = false;
            System.out.println("Neumorphic checkbox unchecked - raised effect");
        }
        
        @Override
        public boolean isChecked() {
            return checked;
        }
        
        @Override
        public String getStyle() {
            return "Neumorphic";
        }
    }
    
    private static class NeumorphicScrollBar implements ScrollBar {
        private int position = 0;
        
        @Override
        public void render() {
            System.out.println("Rendering Neumorphic scroll bar with soft shadows");
        }
        
        @Override
        public void scrollTo(int position) {
            this.position = Math.max(0, Math.min(position, 100));
            System.out.println("Neumorphic scroll bar at: " + this.position);
        }
        
        @Override
        public int getPosition() {
            return position;
        }
        
        @Override
        public String getStyle() {
            return "Neumorphic";
        }
    }
}