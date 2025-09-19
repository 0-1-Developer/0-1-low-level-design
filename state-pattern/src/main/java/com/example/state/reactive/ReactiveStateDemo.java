package com.example.state.reactive;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ReactiveStateDemo {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Reactive Event-Driven State Pattern Demo ===\n");
        
        EventBus eventBus = new EventBus();
        
        eventBus.subscribe("*", event -> {
            System.out.println("[EVENT LOG] " + event);
        });
        
        ReactiveContext iotDevice = new ReactiveContext(eventBus);
        
        iotDevice.addState(new IoTDeviceStates.DisconnectedState(eventBus));
        iotDevice.addState(new IoTDeviceStates.ConnectingState(eventBus));
        iotDevice.addState(new IoTDeviceStates.ConnectedState(eventBus));
        iotDevice.addState(new IoTDeviceStates.TransmittingState(eventBus));
        iotDevice.addState(new IoTDeviceStates.SleepState(eventBus));
        iotDevice.addState(new IoTDeviceStates.UpdatingState(eventBus));
        
        System.out.println("--- Scenario 1: Normal Connection Flow ---");
        iotDevice.initialize("DISCONNECTED");
        
        Thread.sleep(100);
        eventBus.publish(new Event("WIFI_AVAILABLE", "network", 
            Map.of("ssid", "HomeNetwork")));
        
        Thread.sleep(100);
        eventBus.publish(new Event("CONNECTION_SUCCESS", "network"));
        
        Thread.sleep(100);
        eventBus.publish(new Event("SEND_DATA", "app", 
            Map.of("data", "{temperature: 22.5}")));
        
        Thread.sleep(100);
        eventBus.publish(new Event("TRANSMISSION_COMPLETE", "network", 
            Map.of("bytes", 128)));
        
        System.out.println("\n--- Scenario 2: Connection with Retries ---");
        eventBus.publish(new Event("CONNECTION_LOST", "network"));
        
        Thread.sleep(100);
        eventBus.publish(new Event("MANUAL_CONNECT", "user"));
        
        Thread.sleep(100);
        eventBus.publish(new Event("CONNECTION_FAILED", "network"));
        
        Thread.sleep(100);
        eventBus.publish(new Event("CONNECTION_FAILED", "network"));
        
        Thread.sleep(100);
        eventBus.publish(new Event("CONNECTION_SUCCESS", "network"));
        
        System.out.println("\n--- Scenario 3: Power Management ---");
        eventBus.publish(new Event("LOW_POWER", "battery", 
            Map.of("level", 15)));
        
        Thread.sleep(100);
        eventBus.publish(new Event("SENSOR_TRIGGER", "motion", 
            Map.of("sensor", "PIR-001")));
        
        System.out.println("\n--- Scenario 4: Firmware Update ---");
        eventBus.publish(new Event("FIRMWARE_UPDATE", "server", 
            Map.of("version", "2.0.0")));
        
        Thread.sleep(100);
        eventBus.publish(new Event("UPDATE_PROGRESS", "updater", 
            Map.of("progress", 25)));
        
        Thread.sleep(100);
        eventBus.publish(new Event("UPDATE_PROGRESS", "updater", 
            Map.of("progress", 75)));
        
        Thread.sleep(100);
        eventBus.publish(new Event("UPDATE_COMPLETE", "updater"));
        
        System.out.println("\n--- Scenario 5: Async Event Simulation ---");
        eventBus.publish(new Event("WIFI_AVAILABLE", "network", 
            Map.of("ssid", "CoffeeShopWiFi")));
        
        new Thread(() -> {
            try {
                Thread.sleep(200);
                eventBus.publish(new Event("CONNECTION_TIMEOUT", "network"));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
        
        new Thread(() -> {
            try {
                Thread.sleep(300);
                eventBus.publish(new Event("CONNECTION_SUCCESS", "network"));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
        
        Thread.sleep(500);
        
        System.out.println("\n--- Benefits of Reactive State Pattern ---");
        System.out.println("+ Decoupled event producers and consumers");
        System.out.println("+ Asynchronous event processing");
        System.out.println("+ Natural for event-driven systems");
        System.out.println("+ Easy integration with external event sources");
        System.out.println("+ Supports complex event flows");
        
        System.out.println("\n--- Use Cases ---");
        System.out.println("- IoT devices with sensor events");
        System.out.println("- Reactive UI frameworks");
        System.out.println("- Message-driven microservices");
        System.out.println("- Real-time streaming applications");
        System.out.println("- Distributed systems with event sourcing");
        
        eventBus.stop();
    }
}