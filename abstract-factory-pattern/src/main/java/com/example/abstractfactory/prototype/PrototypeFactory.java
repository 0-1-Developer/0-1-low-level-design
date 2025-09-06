package com.example.abstractfactory.prototype;

import com.example.abstractfactory.common.*;

/**
 * Prototype-backed Abstract Factory that creates products by cloning prototypes.
 * Useful when object creation is expensive or complex.
 */
public class PrototypeFactory {
    private final CloneableButton buttonPrototype;
    private final CloneableCheckbox checkboxPrototype;
    private final CloneableScrollBar scrollBarPrototype;
    private final String themeName;
    
    public PrototypeFactory(
        String themeName,
        CloneableButton buttonPrototype,
        CloneableCheckbox checkboxPrototype,
        CloneableScrollBar scrollBarPrototype
    ) {
        this.themeName = themeName;
        this.buttonPrototype = buttonPrototype;
        this.checkboxPrototype = checkboxPrototype;
        this.scrollBarPrototype = scrollBarPrototype;
    }
    
    public Button createButton() {
        CloneableButton cloned = buttonPrototype.createClone();
        System.out.println("Created button by cloning prototype");
        return cloned;
    }
    
    public Checkbox createCheckbox() {
        CloneableCheckbox cloned = checkboxPrototype.createClone();
        System.out.println("Created checkbox by cloning prototype");
        return cloned;
    }
    
    public ScrollBar createScrollBar() {
        CloneableScrollBar cloned = scrollBarPrototype.createClone();
        System.out.println("Created scroll bar by cloning prototype");
        return cloned;
    }
    
    public String getThemeName() {
        return themeName;
    }
    
    /**
     * Base class for cloneable buttons with expensive initialization.
     */
    public static abstract class CloneableButton implements Button, Cloneable<CloneableButton> {
        protected String style;
        protected String complexData;
        protected long creationTime;
        
        protected CloneableButton(String style) {
            this.style = style;
            this.creationTime = System.currentTimeMillis();
            // Simulate expensive initialization
            this.complexData = performExpensiveInitialization();
        }
        
        protected CloneableButton(CloneableButton source) {
            this.style = source.style;
            this.complexData = source.complexData;
            this.creationTime = System.currentTimeMillis();
        }
        
        private String performExpensiveInitialization() {
            // Simulate expensive operation
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "Complex_Data_" + style + "_" + System.nanoTime();
        }
        
        @Override
        public String getStyle() {
            return style;
        }
        
        public long getCreationTime() {
            return creationTime;
        }
    }
    
    /**
     * Base class for cloneable checkboxes.
     */
    public static abstract class CloneableCheckbox implements Checkbox, Cloneable<CloneableCheckbox> {
        protected String style;
        protected boolean checked = false;
        protected String configuration;
        
        protected CloneableCheckbox(String style, String configuration) {
            this.style = style;
            this.configuration = configuration;
        }
        
        protected CloneableCheckbox(CloneableCheckbox source) {
            this.style = source.style;
            this.configuration = source.configuration;
            this.checked = false; // Reset state on clone
        }
        
        @Override
        public void check() {
            checked = true;
        }
        
        @Override
        public void uncheck() {
            checked = false;
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
    
    /**
     * Base class for cloneable scroll bars.
     */
    public static abstract class CloneableScrollBar implements ScrollBar, Cloneable<CloneableScrollBar> {
        protected String style;
        protected int position = 0;
        protected int maxValue;
        
        protected CloneableScrollBar(String style, int maxValue) {
            this.style = style;
            this.maxValue = maxValue;
        }
        
        protected CloneableScrollBar(CloneableScrollBar source) {
            this.style = source.style;
            this.maxValue = source.maxValue;
            this.position = 0; // Reset position on clone
        }
        
        @Override
        public void scrollTo(int position) {
            this.position = Math.max(0, Math.min(position, maxValue));
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
    
    // Concrete prototype implementations
    public static class VintageButton extends CloneableButton {
        public VintageButton() {
            super("Vintage");
        }
        
        private VintageButton(VintageButton source) {
            super(source);
        }
        
        @Override
        public void render() {
            System.out.println("Rendering vintage button - wood texture, brass accents");
        }
        
        @Override
        public void onClick() {
            System.out.println("Vintage button clicked - mechanical click sound");
        }
        
        @Override
        public CloneableButton createClone() {
            return new VintageButton(this);
        }
    }
    
    public static class VintageCheckbox extends CloneableCheckbox {
        public VintageCheckbox() {
            super("Vintage", "brass_and_wood");
        }
        
        private VintageCheckbox(VintageCheckbox source) {
            super(source);
        }
        
        @Override
        public void render() {
            System.out.println("Rendering vintage checkbox - toggle switch style");
        }
        
        @Override
        public void check() {
            super.check();
            System.out.println("Vintage checkbox toggled on - mechanical switch sound");
        }
        
        @Override
        public void uncheck() {
            super.uncheck();
            System.out.println("Vintage checkbox toggled off");
        }
        
        @Override
        public CloneableCheckbox createClone() {
            return new VintageCheckbox(this);
        }
    }
    
    public static class VintageScrollBar extends CloneableScrollBar {
        public VintageScrollBar() {
            super("Vintage", 100);
        }
        
        private VintageScrollBar(VintageScrollBar source) {
            super(source);
        }
        
        @Override
        public void render() {
            System.out.println("Rendering vintage scroll bar - brass rail with wooden handle");
        }
        
        @Override
        public void scrollTo(int position) {
            super.scrollTo(position);
            System.out.println("Vintage scroll bar slid to: " + position);
        }
        
        @Override
        public CloneableScrollBar createClone() {
            return new VintageScrollBar(this);
        }
    }
    
    public static class FuturisticButton extends CloneableButton {
        public FuturisticButton() {
            super("Futuristic");
        }
        
        private FuturisticButton(FuturisticButton source) {
            super(source);
        }
        
        @Override
        public void render() {
            System.out.println("Rendering futuristic button - holographic projection");
        }
        
        @Override
        public void onClick() {
            System.out.println("Futuristic button activated - plasma discharge effect");
        }
        
        @Override
        public CloneableButton createClone() {
            return new FuturisticButton(this);
        }
    }
    
    public static class FuturisticCheckbox extends CloneableCheckbox {
        public FuturisticCheckbox() {
            super("Futuristic", "holographic_display");
        }
        
        private FuturisticCheckbox(FuturisticCheckbox source) {
            super(source);
        }
        
        @Override
        public void render() {
            System.out.println("Rendering futuristic checkbox - energy field");
        }
        
        @Override
        public void check() {
            super.check();
            System.out.println("Futuristic checkbox energized - force field activated");
        }
        
        @Override
        public void uncheck() {
            super.uncheck();
            System.out.println("Futuristic checkbox de-energized");
        }
        
        @Override
        public CloneableCheckbox createClone() {
            return new FuturisticCheckbox(this);
        }
    }
    
    public static class FuturisticScrollBar extends CloneableScrollBar {
        public FuturisticScrollBar() {
            super("Futuristic", 100);
        }
        
        private FuturisticScrollBar(FuturisticScrollBar source) {
            super(source);
        }
        
        @Override
        public void render() {
            System.out.println("Rendering futuristic scroll bar - energy beam");
        }
        
        @Override
        public void scrollTo(int position) {
            super.scrollTo(position);
            System.out.println("Futuristic scroll bar teleported to: " + position);
        }
        
        @Override
        public CloneableScrollBar createClone() {
            return new FuturisticScrollBar(this);
        }
    }
}