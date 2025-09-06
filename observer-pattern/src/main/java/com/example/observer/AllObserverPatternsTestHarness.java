package com.example.observer;

import com.example.observer.classic.*;
import com.example.observer.push.*;
import com.example.observer.pull.*;
import com.example.observer.property.*;
import com.example.observer.eventbus.*;

public class AllObserverPatternsTestHarness {
    private static int testsRun = 0;
    private static int testsPassed = 0;
    
    public static void main(String[] args) {
        System.out.println("=== Observer Pattern - All Variants Test Harness ===\n");
        
        testClassicObserver();
        testPushObserver();
        testPullObserver();
        testPropertyObserver();
        testEventBusObserver();
        
        System.out.println("=== TEST SUMMARY ===");
        System.out.println("Tests Run: " + testsRun);
        System.out.println("Tests Passed: " + testsPassed);
        System.out.println("Tests Failed: " + (testsRun - testsPassed));
        System.out.println("Success Rate: " + (testsPassed * 100 / testsRun) + "%");
        
        if (testsPassed == testsRun) {
            System.out.println("ALL TESTS PASSED - Observer Pattern Implementation Complete!");
        } else {
            System.out.println("Some tests failed - Review implementation");
        }
    }
    
    private static void testClassicObserver() {
        System.out.println("=== Testing Classic GoF Observer ===");
        try {
            WeatherStation station = new WeatherStation();
            CurrentConditionsDisplay display = new CurrentConditionsDisplay("Test");
            
            station.attach(display);
            assertEquals(1, station.getObserverCount(), "Observer attachment");
            
            station.setMeasurements(75f, 60f, 30.1f);
            assertEquals(75f, display.getTemperature(), "Temperature update");
            assertEquals(60f, display.getHumidity(), "Humidity update");
            
            station.detach(display);
            assertEquals(0, station.getObserverCount(), "Observer detachment");
            
            System.out.println("Classic Observer: PASSED\n");
        } catch (Exception e) {
            System.out.println("Classic Observer: FAILED - " + e.getMessage() + "\n");
        }
    }
    
    private static void testPushObserver() {
        System.out.println("=== Testing Push Observer ===");
        try {
            PushWeatherStation station = new PushWeatherStation("TEST");
            TestPushObserver observer = new TestPushObserver();
            
            station.subscribe(observer);
            assertEquals(1, station.getSubscriberCount(), "Push observer subscription");
            
            station.reportWeather(80f, 70f, 29.8f, "TestCity");
            assertTrue(observer.wasNotified(), "Push notification received");
            assertEquals("TestCity", observer.getLastLocation(), "Push data integrity");
            
            System.out.println("Push Observer: PASSED\n");
        } catch (Exception e) {
            System.out.println("Push Observer: FAILED - " + e.getMessage() + "\n");
        }
    }
    
    private static void testPullObserver() {
        System.out.println("=== Testing Pull Observer ===");
        try {
            PullWeatherStation station = new PullWeatherStation("TEST");
            TestPullObserver observer = new TestPullObserver();
            
            station.attach(observer);
            assertEquals(1, station.getObserverCount(), "Pull observer attachment");
            
            station.setMeasurements(85f, 65f, 30.2f, "TestCity");
            assertTrue(observer.wasNotified(), "Pull notification received");
            assertEquals(85f, observer.getLastTemperature(), "Pull data access");
            
            System.out.println("Pull Observer: PASSED\n");
        } catch (Exception e) {
            System.out.println("Pull Observer: FAILED - " + e.getMessage() + "\n");
        }
    }
    
    private static void testPropertyObserver() {
        System.out.println("=== Testing Property Observer ===");
        try {
            PropertyWeatherStation station = new PropertyWeatherStation("TEST");
            TestPropertyObserver observer = new TestPropertyObserver();
            
            station.addPropertyChangeListener(PropertyWeatherStation.PROPERTY_TEMPERATURE, observer);
            assertEquals(1, station.getPropertyListeners(PropertyWeatherStation.PROPERTY_TEMPERATURE), "Property listener registration");
            
            station.setTemperature(90f);
            assertTrue(observer.wasNotified(), "Property change notification");
            assertEquals("temperature", observer.getLastPropertyName(), "Property name correct");
            
            System.out.println("Property Observer: PASSED\n");
        } catch (Exception e) {
            System.out.println("Property Observer: FAILED - " + e.getMessage() + "\n");
        }
    }
    
    private static void testEventBusObserver() {
        System.out.println("=== Testing Event Bus Observer ===");
        try {
            EventBus bus = new EventBus("TEST");
            TestEventBusSubscriber subscriber = new TestEventBusSubscriber();
            
            bus.subscribe("test.topic", subscriber::handleEvent);
            assertEquals(1, bus.getSubscriberCount("test.topic"), "Event bus subscription");
            
            bus.publish(new Event("test.topic", "test data"));
            assertTrue(subscriber.wasNotified(), "Event bus notification");
            assertEquals("test data", (String) subscriber.getLastData(), "Event data integrity");
            
            System.out.println("Event Bus Observer: PASSED\n");
        } catch (Exception e) {
            System.out.println("Event Bus Observer: FAILED - " + e.getMessage() + "\n");
        }
    }
    
    // Test helper classes
    private static class TestPushObserver implements PushObserver<WeatherData> {
        private boolean notified = false;
        private String lastLocation;
        
        @Override
        public void update(WeatherData data) {
            notified = true;
            lastLocation = data.getLocation();
        }
        
        public boolean wasNotified() { return notified; }
        public String getLastLocation() { return lastLocation; }
    }
    
    private static class TestPullObserver implements PullObserver {
        private boolean notified = false;
        private float lastTemperature;
        
        @Override
        public void update(PullSubject subject) {
            notified = true;
            if (subject instanceof PullWeatherStation) {
                lastTemperature = ((PullWeatherStation) subject).getTemperature();
            }
        }
        
        public boolean wasNotified() { return notified; }
        public float getLastTemperature() { return lastTemperature; }
    }
    
    private static class TestPropertyObserver implements PropertyChangeListener {
        private boolean notified = false;
        private String lastPropertyName;
        
        @Override
        public void propertyChanged(PropertyChangeEvent event) {
            notified = true;
            lastPropertyName = event.getPropertyName();
        }
        
        public boolean wasNotified() { return notified; }
        public String getLastPropertyName() { return lastPropertyName; }
    }
    
    private static class TestEventBusSubscriber {
        private boolean notified = false;
        private Object lastData;
        
        public void handleEvent(Event event) {
            notified = true;
            lastData = event.getData();
        }
        
        public boolean wasNotified() { return notified; }
        public Object getLastData() { return lastData; }
    }
    
    // Test utility methods
    private static void assertEquals(int expected, int actual, String testName) {
        testsRun++;
        if (expected == actual) {
            testsPassed++;
            System.out.println("  ✓ " + testName + ": " + actual);
        } else {
            System.out.println("  ✗ " + testName + ": Expected " + expected + ", got " + actual);
        }
    }
    
    private static void assertEquals(float expected, float actual, String testName) {
        testsRun++;
        if (Math.abs(expected - actual) < 0.001f) {
            testsPassed++;
            System.out.println("  ✓ " + testName + ": " + actual);
        } else {
            System.out.println("  ✗ " + testName + ": Expected " + expected + ", got " + actual);
        }
    }
    
    private static void assertEquals(String expected, String actual, String testName) {
        testsRun++;
        if ((expected == null && actual == null) || (expected != null && expected.equals(actual))) {
            testsPassed++;
            System.out.println("  ✓ " + testName + ": " + actual);
        } else {
            System.out.println("  ✗ " + testName + ": Expected '" + expected + "', got '" + actual + "'");
        }
    }
    
    private static void assertTrue(boolean condition, String testName) {
        testsRun++;
        if (condition) {
            testsPassed++;
            System.out.println("  ✓ " + testName + ": true");
        } else {
            System.out.println("  ✗ " + testName + ": Expected true, got false");
        }
    }
}