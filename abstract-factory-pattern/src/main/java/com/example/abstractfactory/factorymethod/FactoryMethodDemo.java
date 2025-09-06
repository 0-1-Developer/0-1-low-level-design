package com.example.abstractfactory.factorymethod;

import com.example.abstractfactory.common.*;

/**
 * Demonstrates Factory Method-backed Abstract Factory.
 * Shows how abstract factory methods can be implemented by concrete factories.
 */
public class FactoryMethodDemo {
    
    public static void testFactory(AbstractUIFactory factory) {
        System.out.println("\nTesting factory: " + factory.getClass().getSimpleName());
        System.out.println("Theme: " + factory.getThemeName());
        
        Button button = factory.createButton();
        Checkbox checkbox = factory.createCheckbox();
        ScrollBar scrollBar = factory.createScrollBar();
        
        button.render();
        checkbox.render();
        scrollBar.render();
        
        System.out.println("\nInteraction test:");
        button.onClick();
        checkbox.check();
        scrollBar.scrollTo(75);
        
        System.out.println("\nFamily consistency:");
        boolean consistent = button.getStyle().equals(checkbox.getStyle()) &&
                           checkbox.getStyle().equals(scrollBar.getStyle());
        System.out.println("All components from same family (" + button.getStyle() + "): " + consistent);
        
        System.out.println("\nUsing template method:");
        factory.createAndConfigureUI();
    }
    
    public static void main(String[] args) {
        System.out.println("===== Factory Method-backed Abstract Factory Demo =====");
        
        AbstractUIFactory darkFactory = new DarkThemeFactory();
        testFactory(darkFactory);
        
        AbstractUIFactory lightFactory = new LightThemeFactory();
        testFactory(lightFactory);
        
        System.out.println("\n--- Dynamic Theme Selection ---");
        int hour = java.time.LocalTime.now().getHour();
        AbstractUIFactory autoFactory;
        
        if (hour >= 18 || hour < 6) {
            autoFactory = new DarkThemeFactory();
            System.out.println("Evening/Night detected - using Dark Theme");
        } else {
            autoFactory = new LightThemeFactory();
            System.out.println("Daytime detected - using Light Theme");
        }
        
        autoFactory.createAndConfigureUI();
    }
}