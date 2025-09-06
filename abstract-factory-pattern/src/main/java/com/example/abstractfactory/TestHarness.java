package com.example.abstractfactory;

import com.example.abstractfactory.common.*;
import com.example.abstractfactory.classic.*;
import com.example.abstractfactory.factorymethod.*;
import com.example.abstractfactory.parameterized.*;
import com.example.abstractfactory.registry.*;
import com.example.abstractfactory.reflection.*;
import com.example.abstractfactory.functional.*;
import com.example.abstractfactory.prototype.*;
import com.example.abstractfactory.config.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Comprehensive test harness for all Abstract Factory implementations.
 * Validates family consistency and component interoperability.
 */
public class TestHarness {
    
    private static class TestResult {
        String testName;
        boolean passed;
        String message;
        
        TestResult(String testName, boolean passed, String message) {
            this.testName = testName;
            this.passed = passed;
            this.message = message;
        }
    }
    
    private static List<TestResult> results = new ArrayList<>();
    
    /**
     * Test that all components from a factory belong to the same family.
     */
    private static void testFamilyConsistency(String factoryName, Button button, Checkbox checkbox, ScrollBar scrollBar) {
        String testName = factoryName + " - Family Consistency";
        
        String buttonStyle = button.getStyle();
        String checkboxStyle = checkbox.getStyle();
        String scrollBarStyle = scrollBar.getStyle();
        
        boolean consistent = buttonStyle.equals(checkboxStyle) && 
                           checkboxStyle.equals(scrollBarStyle);
        
        String message = String.format("Button=%s, Checkbox=%s, ScrollBar=%s", 
                                      buttonStyle, checkboxStyle, scrollBarStyle);
        
        results.add(new TestResult(testName, consistent, message));
    }
    
    /**
     * Test that components can be interacted with without errors.
     */
    private static void testComponentFunctionality(String factoryName, Button button, Checkbox checkbox, ScrollBar scrollBar) {
        String testName = factoryName + " - Component Functionality";
        
        try {
            button.render();
            button.onClick();
            
            checkbox.render();
            checkbox.check();
            boolean wasChecked = checkbox.isChecked();
            checkbox.uncheck();
            boolean wasUnchecked = !checkbox.isChecked();
            
            scrollBar.render();
            scrollBar.scrollTo(50);
            int position = scrollBar.getPosition();
            
            boolean passed = wasChecked && wasUnchecked && position == 50;
            String message = passed ? "All components functional" : "Component malfunction detected";
            
            results.add(new TestResult(testName, passed, message));
        } catch (Exception e) {
            results.add(new TestResult(testName, false, "Exception: " + e.getMessage()));
        }
    }
    
    /**
     * Test that different factories produce different families.
     */
    private static void testFactoryIndependence() {
        String testName = "Factory Independence";
        
        WindowsFactory windowsFactory = new WindowsFactory();
        MacOSFactory macFactory = new MacOSFactory();
        
        Button winButton = windowsFactory.createButton();
        Button macButton = macFactory.createButton();
        
        boolean independent = !winButton.getStyle().equals(macButton.getStyle());
        String message = String.format("Windows=%s, macOS=%s", 
                                      winButton.getStyle(), macButton.getStyle());
        
        results.add(new TestResult(testName, independent, message));
    }
    
    /**
     * Test classic Abstract Factory implementation.
     */
    private static void testClassicFactory() {
        System.out.println("\n--- Testing Classic Abstract Factory ---");
        
        WindowsFactory windowsFactory = new WindowsFactory();
        Button button = windowsFactory.createButton();
        Checkbox checkbox = windowsFactory.createCheckbox();
        ScrollBar scrollBar = windowsFactory.createScrollBar();
        
        testFamilyConsistency("Classic Windows", button, checkbox, scrollBar);
        testComponentFunctionality("Classic Windows", button, checkbox, scrollBar);
        
        MacOSFactory macFactory = new MacOSFactory();
        button = macFactory.createButton();
        checkbox = macFactory.createCheckbox();
        scrollBar = macFactory.createScrollBar();
        
        testFamilyConsistency("Classic macOS", button, checkbox, scrollBar);
        testComponentFunctionality("Classic macOS", button, checkbox, scrollBar);
    }
    
    /**
     * Test Factory Method-backed implementation.
     */
    private static void testFactoryMethod() {
        System.out.println("\n--- Testing Factory Method-backed ---");
        
        AbstractUIFactory darkFactory = new DarkThemeFactory();
        Button button = darkFactory.createButton();
        Checkbox checkbox = darkFactory.createCheckbox();
        ScrollBar scrollBar = darkFactory.createScrollBar();
        
        testFamilyConsistency("Factory Method Dark", button, checkbox, scrollBar);
        testComponentFunctionality("Factory Method Dark", button, checkbox, scrollBar);
    }
    
    /**
     * Test Parameterized implementation.
     */
    private static void testParameterized() {
        System.out.println("\n--- Testing Parameterized Factory ---");
        
        ParameterizedUIFactory materialFactory = new ParameterizedUIFactory(ThemeStyle.MATERIAL);
        Button button = materialFactory.createButton();
        Checkbox checkbox = materialFactory.createCheckbox();
        ScrollBar scrollBar = materialFactory.createScrollBar();
        
        testFamilyConsistency("Parameterized Material", button, checkbox, scrollBar);
        testComponentFunctionality("Parameterized Material", button, checkbox, scrollBar);
    }
    
    /**
     * Test Registry-backed implementation.
     */
    private static void testRegistry() {
        System.out.println("\n--- Testing Registry-backed Factory ---");
        
        RegistryBackedFactory minimalFactory = new RegistryBackedFactory("minimal");
        Button button = minimalFactory.createButton();
        Checkbox checkbox = minimalFactory.createCheckbox();
        ScrollBar scrollBar = minimalFactory.createScrollBar();
        
        testFamilyConsistency("Registry Minimal", button, checkbox, scrollBar);
        testComponentFunctionality("Registry Minimal", button, checkbox, scrollBar);
    }
    
    /**
     * Test Reflection-based implementation.
     */
    private static void testReflection() {
        System.out.println("\n--- Testing Reflection-based Factory ---");
        
        try {
            ReflectionFactory corporateFactory = new ReflectionFactory("corporate");
            Button button = corporateFactory.createButton();
            Checkbox checkbox = corporateFactory.createCheckbox();
            ScrollBar scrollBar = corporateFactory.createScrollBar();
            
            testFamilyConsistency("Reflection Corporate", button, checkbox, scrollBar);
            testComponentFunctionality("Reflection Corporate", button, checkbox, scrollBar);
        } catch (Exception e) {
            results.add(new TestResult("Reflection Factory", false, "Exception: " + e.getMessage()));
        }
    }
    
    /**
     * Test Functional/Lambda implementation.
     */
    private static void testFunctional() {
        System.out.println("\n--- Testing Functional Factory ---");
        
        FunctionalFactory flatFactory = FunctionalFactory.Themes.createFlatDesign();
        Button button = flatFactory.createButton();
        Checkbox checkbox = flatFactory.createCheckbox();
        ScrollBar scrollBar = flatFactory.createScrollBar();
        
        testFamilyConsistency("Functional Flat", button, checkbox, scrollBar);
        testComponentFunctionality("Functional Flat", button, checkbox, scrollBar);
    }
    
    /**
     * Test Prototype-backed implementation.
     */
    private static void testPrototype() {
        System.out.println("\n--- Testing Prototype-backed Factory ---");
        
        PrototypeFactory vintageFactory = new PrototypeFactory(
            "Vintage",
            new PrototypeFactory.VintageButton(),
            new PrototypeFactory.VintageCheckbox(),
            new PrototypeFactory.VintageScrollBar()
        );
        
        Button button = vintageFactory.createButton();
        Checkbox checkbox = vintageFactory.createCheckbox();
        ScrollBar scrollBar = vintageFactory.createScrollBar();
        
        testFamilyConsistency("Prototype Vintage", button, checkbox, scrollBar);
        testComponentFunctionality("Prototype Vintage", button, checkbox, scrollBar);
        
        // Test clone independence
        Button button2 = vintageFactory.createButton();
        boolean independent = button != button2;
        results.add(new TestResult("Prototype Clone Independence", independent, 
                                  "Clones are separate instances: " + independent));
    }
    
    /**
     * Test Config-driven implementation.
     */
    private static void testConfigDriven() {
        System.out.println("\n--- Testing Config-driven Factory ---");
        
        ThemeConfig config = ThemeConfig.ConfigLoader.loadFromFile("enterprise");
        ConfigDrivenFactory enterpriseFactory = new ConfigDrivenFactory(config);
        
        Button button = enterpriseFactory.createButton();
        Checkbox checkbox = enterpriseFactory.createCheckbox();
        ScrollBar scrollBar = enterpriseFactory.createScrollBar();
        
        testFamilyConsistency("Config Enterprise", button, checkbox, scrollBar);
        testComponentFunctionality("Config Enterprise", button, checkbox, scrollBar);
    }
    
    /**
     * Print test results summary.
     */
    private static void printResults() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEST RESULTS SUMMARY");
        System.out.println("=".repeat(60));
        
        int passed = 0;
        int failed = 0;
        
        for (TestResult result : results) {
            String status = result.passed ? "PASS" : "FAIL";
            String marker = result.passed ? "✓" : "✗";
            
            System.out.printf("%s [%s] %s\n", marker, status, result.testName);
            if (!result.passed || result.message.contains("Exception")) {
                System.out.println("      Details: " + result.message);
            }
            
            if (result.passed) passed++;
            else failed++;
        }
        
        System.out.println("\n" + "-".repeat(60));
        System.out.printf("Total Tests: %d | Passed: %d | Failed: %d\n", 
                         results.size(), passed, failed);
        
        double passRate = (passed * 100.0) / results.size();
        System.out.printf("Pass Rate: %.1f%%\n", passRate);
        
        if (failed == 0) {
            System.out.println("\n✓ ALL TESTS PASSED!");
        } else {
            System.out.println("\n✗ SOME TESTS FAILED - Review details above");
        }
    }
    
    public static void main(String[] args) {
        System.out.println("===== Abstract Factory Pattern Test Harness =====");
        System.out.println("Testing all implementations for consistency and functionality...");
        
        // Run all tests
        testClassicFactory();
        testFactoryMethod();
        testParameterized();
        testRegistry();
        testReflection();
        testFunctional();
        testPrototype();
        testConfigDriven();
        
        // Test cross-factory independence
        testFactoryIndependence();
        
        // Print results
        printResults();
    }
}