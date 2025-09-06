package com.example.abstractfactory.classic;

import com.example.abstractfactory.common.*;

/**
 * Demonstrates the classic Abstract Factory pattern.
 * Shows how different factories create coherent families of products.
 */
public class ClassicAbstractFactoryDemo {
    
    private static class Application {
        private Button button;
        private Checkbox checkbox;
        private ScrollBar scrollBar;
        
        public Application(GUIFactory factory) {
            button = factory.createButton();
            checkbox = factory.createCheckbox();
            scrollBar = factory.createScrollBar();
        }
        
        public void renderUI() {
            button.render();
            checkbox.render();
            scrollBar.render();
        }
        
        public void testInteraction() {
            button.onClick();
            checkbox.check();
            scrollBar.scrollTo(50);
        }
        
        public boolean verifyFamilyConsistency() {
            String buttonStyle = button.getStyle();
            String checkboxStyle = checkbox.getStyle();
            String scrollBarStyle = scrollBar.getStyle();
            
            boolean consistent = buttonStyle.equals(checkboxStyle) && 
                                checkboxStyle.equals(scrollBarStyle);
            
            System.out.println("\nFamily consistency check:");
            System.out.println("  Button style: " + buttonStyle);
            System.out.println("  Checkbox style: " + checkboxStyle);
            System.out.println("  ScrollBar style: " + scrollBarStyle);
            System.out.println("  All components from same family: " + consistent);
            
            return consistent;
        }
    }
    
    public static void main(String[] args) {
        System.out.println("===== Classic Abstract Factory Demo =====\n");
        
        System.out.println("--- Creating Windows Application ---");
        GUIFactory windowsFactory = new WindowsFactory();
        System.out.println("Factory: " + windowsFactory.getClass().getSimpleName());
        
        Application windowsApp = new Application(windowsFactory);
        windowsApp.renderUI();
        System.out.println("\nTesting interactions:");
        windowsApp.testInteraction();
        windowsApp.verifyFamilyConsistency();
        
        System.out.println("\n--- Creating macOS Application ---");
        GUIFactory macFactory = new MacOSFactory();
        System.out.println("Factory: " + macFactory.getClass().getSimpleName());
        
        Application macApp = new Application(macFactory);
        macApp.renderUI();
        System.out.println("\nTesting interactions:");
        macApp.testInteraction();
        macApp.verifyFamilyConsistency();
        
        System.out.println("\n--- Cross-Platform Configuration Demo ---");
        String os = System.getProperty("os.name", "unknown").toLowerCase();
        GUIFactory autoFactory;
        
        if (os.contains("win")) {
            autoFactory = new WindowsFactory();
            System.out.println("Detected Windows - using WindowsFactory");
        } else if (os.contains("mac")) {
            autoFactory = new MacOSFactory();
            System.out.println("Detected macOS - using MacOSFactory");
        } else {
            autoFactory = new WindowsFactory();
            System.out.println("Unknown OS - defaulting to WindowsFactory");
        }
        
        Application autoApp = new Application(autoFactory);
        autoApp.renderUI();
        autoApp.verifyFamilyConsistency();
    }
}