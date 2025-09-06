package com.example.abstractfactory.prototype;

import com.example.abstractfactory.common.*;

/**
 * Demonstrates Prototype-backed Abstract Factory.
 * Shows how cloning can be more efficient than creating new instances.
 */
public class PrototypeFactoryDemo {
    
    private static void testPrototypeFactory(PrototypeFactory factory) {
        System.out.println("\n=== " + factory.getThemeName() + " (Prototype) ===");
        
        long startTime = System.currentTimeMillis();
        
        Button button1 = factory.createButton();
        Button button2 = factory.createButton();
        Checkbox checkbox = factory.createCheckbox();
        ScrollBar scrollBar = factory.createScrollBar();
        
        long endTime = System.currentTimeMillis();
        System.out.println("Creation time: " + (endTime - startTime) + "ms");
        
        System.out.println("\nRendering components:");
        button1.render();
        checkbox.render();
        scrollBar.render();
        
        System.out.println("\nTesting cloned independence:");
        button1.onClick();
        button2.onClick();
        System.out.println("Button1 and Button2 are different instances: " + (button1 != button2));
        
        System.out.println("\nTesting state independence:");
        checkbox.check();
        Checkbox checkbox2 = factory.createCheckbox();
        System.out.println("Original checkbox checked: " + checkbox.isChecked());
        System.out.println("Cloned checkbox checked: " + checkbox2.isChecked());
        
        System.out.println("\nFamily consistency:");
        boolean consistent = button1.getStyle().equals(checkbox.getStyle()) &&
                           checkbox.getStyle().equals(scrollBar.getStyle());
        System.out.println("All components from " + button1.getStyle() + " family: " + consistent);
    }
    
    private static void compareCreationPerformance() {
        System.out.println("\n=== Performance Comparison ===");
        
        // Create prototypes (expensive initial creation)
        System.out.println("Creating initial prototypes...");
        long prototypeStart = System.currentTimeMillis();
        
        PrototypeFactory.VintageButton vintageButtonProto = new PrototypeFactory.VintageButton();
        PrototypeFactory.VintageCheckbox vintageCheckboxProto = new PrototypeFactory.VintageCheckbox();
        PrototypeFactory.VintageScrollBar vintageScrollBarProto = new PrototypeFactory.VintageScrollBar();
        
        long prototypeEnd = System.currentTimeMillis();
        System.out.println("Prototype creation time: " + (prototypeEnd - prototypeStart) + "ms");
        
        PrototypeFactory vintageFactory = new PrototypeFactory(
            "Vintage",
            vintageButtonProto,
            vintageCheckboxProto,
            vintageScrollBarProto
        );
        
        // Create many clones
        System.out.println("\nCreating 100 component sets via cloning:");
        long cloneStart = System.currentTimeMillis();
        
        for (int i = 0; i < 100; i++) {
            Button button = vintageFactory.createButton();
            Checkbox checkbox = vintageFactory.createCheckbox();
            ScrollBar scrollBar = vintageFactory.createScrollBar();
        }
        
        long cloneEnd = System.currentTimeMillis();
        System.out.println("Clone creation time for 100 sets: " + (cloneEnd - cloneStart) + "ms");
        
        // Compare with direct instantiation
        System.out.println("\nCreating 100 component sets via new instantiation:");
        long directStart = System.currentTimeMillis();
        
        for (int i = 0; i < 100; i++) {
            Button button = new PrototypeFactory.VintageButton();
            Checkbox checkbox = new PrototypeFactory.VintageCheckbox();
            ScrollBar scrollBar = new PrototypeFactory.VintageScrollBar();
        }
        
        long directEnd = System.currentTimeMillis();
        System.out.println("Direct creation time for 100 sets: " + (directEnd - directStart) + "ms");
        
        long cloneTime = cloneEnd - cloneStart;
        long directTime = directEnd - directStart;
        
        if (cloneTime < directTime) {
            System.out.println("\nCloning was " + (directTime - cloneTime) + "ms faster!");
        } else {
            System.out.println("\nDirect instantiation was " + (cloneTime - directTime) + "ms faster!");
        }
    }
    
    public static void main(String[] args) {
        System.out.println("===== Prototype-backed Abstract Factory Demo =====");
        
        // Create Vintage theme factory
        PrototypeFactory vintageFactory = new PrototypeFactory(
            "Vintage Theme",
            new PrototypeFactory.VintageButton(),
            new PrototypeFactory.VintageCheckbox(),
            new PrototypeFactory.VintageScrollBar()
        );
        
        testPrototypeFactory(vintageFactory);
        
        // Create Futuristic theme factory
        PrototypeFactory futuristicFactory = new PrototypeFactory(
            "Futuristic Theme",
            new PrototypeFactory.FuturisticButton(),
            new PrototypeFactory.FuturisticCheckbox(),
            new PrototypeFactory.FuturisticScrollBar()
        );
        
        testPrototypeFactory(futuristicFactory);
        
        // Performance comparison
        compareCreationPerformance();
        
        System.out.println("\n=== Prototype Factory Benefits ===");
        System.out.println("- Efficient when object creation is expensive");
        System.out.println("- Preserves complex initialization state");
        System.out.println("- Can clone pre-configured objects");
        System.out.println("- Reduces memory allocation overhead");
        
        System.out.println("\n=== Considerations ===");
        System.out.println("- Must implement proper deep cloning");
        System.out.println("- State must be properly reset on clone");
        System.out.println("- Initial prototype creation still expensive");
        System.out.println("- Cloning complexity for nested objects");
    }
}