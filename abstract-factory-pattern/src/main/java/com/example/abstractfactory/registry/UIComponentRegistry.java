package com.example.abstractfactory.registry;

import com.example.abstractfactory.common.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Registry that manages factory registrations for different UI themes.
 * Supports runtime registration and lookup of factories.
 */
public class UIComponentRegistry {
    private static final UIComponentRegistry INSTANCE = new UIComponentRegistry();
    
    private final Map<String, FactoryBundle> factories = new HashMap<>();
    
    private UIComponentRegistry() {
        registerDefaults();
    }
    
    public static UIComponentRegistry getInstance() {
        return INSTANCE;
    }
    
    /**
     * Bundle containing suppliers for all component types in a family.
     */
    public static class FactoryBundle {
        private final Supplier<Button> buttonSupplier;
        private final Supplier<Checkbox> checkboxSupplier;
        private final Supplier<ScrollBar> scrollBarSupplier;
        
        public FactoryBundle(
            Supplier<Button> buttonSupplier,
            Supplier<Checkbox> checkboxSupplier,
            Supplier<ScrollBar> scrollBarSupplier
        ) {
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
    }
    
    public void registerFactory(String themeName, FactoryBundle bundle) {
        if (themeName == null || bundle == null) {
            throw new IllegalArgumentException("Theme name and bundle cannot be null");
        }
        factories.put(themeName.toLowerCase(), bundle);
        System.out.println("Registered factory bundle for theme: " + themeName);
    }
    
    public FactoryBundle getFactory(String themeName) {
        FactoryBundle bundle = factories.get(themeName.toLowerCase());
        if (bundle == null) {
            System.out.println("Theme '" + themeName + "' not found. Available themes: " + factories.keySet());
            return getDefaultFactory();
        }
        return bundle;
    }
    
    public boolean hasFactory(String themeName) {
        return factories.containsKey(themeName.toLowerCase());
    }
    
    private FactoryBundle getDefaultFactory() {
        return factories.get("minimal");
    }
    
    private void registerDefaults() {
        // Minimal theme
        registerFactory("minimal", new FactoryBundle(
            MinimalButton::new,
            MinimalCheckbox::new,
            MinimalScrollBar::new
        ));
        
        // Retro theme
        registerFactory("retro", new FactoryBundle(
            RetroButton::new,
            RetroCheckbox::new,
            RetroScrollBar::new
        ));
    }
    
    // Minimal theme components
    private static class MinimalButton implements Button {
        @Override
        public void render() {
            System.out.println("Rendering minimal button - text only, no borders");
        }
        
        @Override
        public void onClick() {
            System.out.println("Minimal button clicked - subtle feedback");
        }
        
        @Override
        public String getStyle() {
            return "Minimal";
        }
    }
    
    private static class MinimalCheckbox implements Checkbox {
        private boolean checked = false;
        
        @Override
        public void render() {
            System.out.println("Rendering minimal checkbox - simple square");
        }
        
        @Override
        public void check() {
            checked = true;
            System.out.println("Minimal checkbox checked - X mark");
        }
        
        @Override
        public void uncheck() {
            checked = false;
            System.out.println("Minimal checkbox unchecked");
        }
        
        @Override
        public boolean isChecked() {
            return checked;
        }
        
        @Override
        public String getStyle() {
            return "Minimal";
        }
    }
    
    private static class MinimalScrollBar implements ScrollBar {
        private int position = 0;
        
        @Override
        public void render() {
            System.out.println("Rendering minimal scroll bar - thin line");
        }
        
        @Override
        public void scrollTo(int position) {
            this.position = Math.max(0, Math.min(position, 100));
            System.out.println("Minimal scroll bar at: " + this.position);
        }
        
        @Override
        public int getPosition() {
            return position;
        }
        
        @Override
        public String getStyle() {
            return "Minimal";
        }
    }
    
    // Retro theme components
    private static class RetroButton implements Button {
        @Override
        public void render() {
            System.out.println("Rendering retro button - 8-bit style with pixelated edges");
        }
        
        @Override
        public void onClick() {
            System.out.println("Retro button clicked - 8-bit sound effect");
        }
        
        @Override
        public String getStyle() {
            return "Retro";
        }
    }
    
    private static class RetroCheckbox implements Checkbox {
        private boolean checked = false;
        
        @Override
        public void render() {
            System.out.println("Rendering retro checkbox - green phosphor glow");
        }
        
        @Override
        public void check() {
            checked = true;
            System.out.println("Retro checkbox checked - blinking cursor");
        }
        
        @Override
        public void uncheck() {
            checked = false;
            System.out.println("Retro checkbox unchecked");
        }
        
        @Override
        public boolean isChecked() {
            return checked;
        }
        
        @Override
        public String getStyle() {
            return "Retro";
        }
    }
    
    private static class RetroScrollBar implements ScrollBar {
        private int position = 0;
        
        @Override
        public void render() {
            System.out.println("Rendering retro scroll bar - ASCII art style");
        }
        
        @Override
        public void scrollTo(int position) {
            this.position = Math.max(0, Math.min(position, 100));
            System.out.println("Retro scroll bar at: " + this.position + " [" + "=".repeat(position/10) + ">]");
        }
        
        @Override
        public int getPosition() {
            return position;
        }
        
        @Override
        public String getStyle() {
            return "Retro";
        }
    }
}