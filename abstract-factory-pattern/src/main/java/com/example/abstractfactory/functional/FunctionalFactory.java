package com.example.abstractfactory.functional;

import com.example.abstractfactory.common.*;
import java.util.function.Supplier;

/**
 * Functional Abstract Factory using lambda expressions and function composition.
 * Provides a lightweight, flexible approach to creating product families.
 */
public class FunctionalFactory {
    private final Supplier<Button> buttonSupplier;
    private final Supplier<Checkbox> checkboxSupplier;
    private final Supplier<ScrollBar> scrollBarSupplier;
    private final String themeName;
    
    public FunctionalFactory(
        String themeName,
        Supplier<Button> buttonSupplier,
        Supplier<Checkbox> checkboxSupplier,
        Supplier<ScrollBar> scrollBarSupplier
    ) {
        this.themeName = themeName;
        this.buttonSupplier = buttonSupplier;
        this.checkboxSupplier = checkboxSupplier;
        this.scrollBarSupplier = scrollBarSupplier;
    }
    
    public Button createButton() {
        return buttonSupplier.get();
    }
    
    public Checkbox createCheckbox() {
        return checkboxSupplier.get();
    }
    
    public ScrollBar createScrollBar() {
        return scrollBarSupplier.get();
    }
    
    public String getThemeName() {
        return themeName;
    }
    
    /**
     * Factory builder for fluent construction of functional factories.
     */
    public static class Builder {
        private String themeName = "Default";
        private Supplier<Button> buttonSupplier;
        private Supplier<Checkbox> checkboxSupplier;
        private Supplier<ScrollBar> scrollBarSupplier;
        
        public Builder withTheme(String themeName) {
            this.themeName = themeName;
            return this;
        }
        
        public Builder withButton(Supplier<Button> supplier) {
            this.buttonSupplier = supplier;
            return this;
        }
        
        public Builder withCheckbox(Supplier<Checkbox> supplier) {
            this.checkboxSupplier = supplier;
            return this;
        }
        
        public Builder withScrollBar(Supplier<ScrollBar> supplier) {
            this.scrollBarSupplier = supplier;
            return this;
        }
        
        public FunctionalFactory build() {
            if (buttonSupplier == null || checkboxSupplier == null || scrollBarSupplier == null) {
                throw new IllegalStateException("All component suppliers must be provided");
            }
            return new FunctionalFactory(themeName, buttonSupplier, checkboxSupplier, scrollBarSupplier);
        }
    }
    
    /**
     * Predefined factory creators for common themes.
     */
    public static class Themes {
        
        public static FunctionalFactory createFlatDesign() {
            return new Builder()
                .withTheme("Flat Design")
                .withButton(() -> new SimpleButton("Flat", 
                    "Rendering flat button - no shadows, solid colors",
                    "Flat button clicked - color change"))
                .withCheckbox(() -> new SimpleCheckbox("Flat",
                    "Rendering flat checkbox - simple square",
                    "Flat checkbox"))
                .withScrollBar(() -> new SimpleScrollBar("Flat",
                    "Rendering flat scroll bar - single color"))
                .build();
        }
        
        public static FunctionalFactory createSkeuomorphic() {
            return new Builder()
                .withTheme("Skeuomorphic")
                .withButton(() -> new SimpleButton("Skeuomorphic",
                    "Rendering skeuomorphic button - realistic textures and shadows",
                    "Skeuomorphic button clicked - physical press effect"))
                .withCheckbox(() -> new SimpleCheckbox("Skeuomorphic",
                    "Rendering skeuomorphic checkbox - 3D appearance",
                    "Skeuomorphic checkbox"))
                .withScrollBar(() -> new SimpleScrollBar("Skeuomorphic",
                    "Rendering skeuomorphic scroll bar - metallic texture"))
                .build();
        }
        
        public static FunctionalFactory createAccessible() {
            return new Builder()
                .withTheme("Accessible")
                .withButton(() -> new SimpleButton("Accessible",
                    "Rendering accessible button - high contrast, large text",
                    "Accessible button clicked - audio feedback"))
                .withCheckbox(() -> new SimpleCheckbox("Accessible",
                    "Rendering accessible checkbox - large target area",
                    "Accessible checkbox"))
                .withScrollBar(() -> new SimpleScrollBar("Accessible",
                    "Rendering accessible scroll bar - keyboard navigation support"))
                .build();
        }
    }
    
    // Simple reusable component implementations
    private static class SimpleButton implements Button {
        private final String style;
        private final String renderMessage;
        private final String clickMessage;
        
        SimpleButton(String style, String renderMessage, String clickMessage) {
            this.style = style;
            this.renderMessage = renderMessage;
            this.clickMessage = clickMessage;
        }
        
        @Override
        public void render() {
            System.out.println(renderMessage);
        }
        
        @Override
        public void onClick() {
            System.out.println(clickMessage);
        }
        
        @Override
        public String getStyle() {
            return style;
        }
    }
    
    private static class SimpleCheckbox implements Checkbox {
        private final String style;
        private final String renderMessage;
        private final String prefix;
        private boolean checked = false;
        
        SimpleCheckbox(String style, String renderMessage, String prefix) {
            this.style = style;
            this.renderMessage = renderMessage;
            this.prefix = prefix;
        }
        
        @Override
        public void render() {
            System.out.println(renderMessage);
        }
        
        @Override
        public void check() {
            checked = true;
            System.out.println(prefix + " checked");
        }
        
        @Override
        public void uncheck() {
            checked = false;
            System.out.println(prefix + " unchecked");
        }
        
        @Override
        public boolean isChecked() {
            return checked;
        }
        
        @Override
        public String getStyle() {
            return style;
        }
    }
    
    private static class SimpleScrollBar implements ScrollBar {
        private final String style;
        private final String renderMessage;
        private int position = 0;
        
        SimpleScrollBar(String style, String renderMessage) {
            this.style = style;
            this.renderMessage = renderMessage;
        }
        
        @Override
        public void render() {
            System.out.println(renderMessage);
        }
        
        @Override
        public void scrollTo(int position) {
            this.position = Math.max(0, Math.min(position, 100));
            System.out.println(style + " scroll bar at: " + this.position);
        }
        
        @Override
        public int getPosition() {
            return position;
        }
        
        @Override
        public String getStyle() {
            return style;
        }
    }
}