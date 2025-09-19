package com.example.state.reactive;

import java.util.concurrent.atomic.AtomicInteger;

public class IoTDeviceStates {
    
    public static class DisconnectedState extends ReactiveState {
        public DisconnectedState(EventBus eventBus) {
            super("DISCONNECTED", eventBus);
        }
        
        @Override
        protected void onEnter() {
            System.out.println("Device disconnected from network");
        }
        
        @Override
        protected void onExit() {
            System.out.println("Attempting connection...");
        }
        
        @Override
        protected void defineEventHandlers() {
            on("WIFI_AVAILABLE", event -> {
                System.out.println("WiFi network detected: " + event.get("ssid"));
                transitionTo("CONNECTING");
            });
            
            on("MANUAL_CONNECT", event -> {
                System.out.println("Manual connection requested");
                transitionTo("CONNECTING");
            });
        }
    }
    
    public static class ConnectingState extends ReactiveState {
        private AtomicInteger retries = new AtomicInteger(0);
        
        public ConnectingState(EventBus eventBus) {
            super("CONNECTING", eventBus);
        }
        
        @Override
        protected void onEnter() {
            System.out.println("Establishing connection...");
            retries.set(0);
            publishEvent("CONNECTION_ATTEMPT", "attempt", 1);
        }
        
        @Override
        protected void onExit() {
            System.out.println("Connection phase ended");
        }
        
        @Override
        protected void defineEventHandlers() {
            on("CONNECTION_SUCCESS", event -> {
                System.out.println("Connection established!");
                transitionTo("CONNECTED");
            });
            
            on("CONNECTION_FAILED", event -> {
                int attempt = retries.incrementAndGet();
                if (attempt < 3) {
                    System.out.println("Connection failed, retry " + attempt + "/3");
                    publishEvent("CONNECTION_ATTEMPT", "attempt", attempt + 1);
                } else {
                    System.out.println("Max retries reached");
                    transitionTo("DISCONNECTED");
                }
            });
            
            on("TIMEOUT", event -> {
                System.out.println("Connection timeout");
                publishEvent("CONNECTION_FAILED", "reason", "timeout");
            });
        }
    }
    
    public static class ConnectedState extends ReactiveState {
        public ConnectedState(EventBus eventBus) {
            super("CONNECTED", eventBus);
        }
        
        @Override
        protected void onEnter() {
            System.out.println("Device online and ready");
            publishEvent("DEVICE_ONLINE", "status", "ready");
        }
        
        @Override
        protected void onExit() {
            System.out.println("Going offline");
        }
        
        @Override
        protected void defineEventHandlers() {
            on("SEND_DATA", event -> {
                Object data = event.get("data");
                System.out.println("Sending data: " + data);
                transitionTo("TRANSMITTING");
            });
            
            on("CONNECTION_LOST", event -> {
                System.out.println("Connection lost!");
                transitionTo("DISCONNECTED");
            });
            
            on("LOW_POWER", event -> {
                System.out.println("Low power mode activated");
                transitionTo("SLEEP");
            });
            
            on("FIRMWARE_UPDATE", event -> {
                System.out.println("Firmware update available");
                transitionTo("UPDATING");
            });
        }
    }
    
    public static class TransmittingState extends ReactiveState {
        public TransmittingState(EventBus eventBus) {
            super("TRANSMITTING", eventBus);
        }
        
        @Override
        protected void onEnter() {
            System.out.println("Data transmission in progress");
        }
        
        @Override
        protected void onExit() {
            System.out.println("Transmission complete");
        }
        
        @Override
        protected void defineEventHandlers() {
            on("TRANSMISSION_COMPLETE", event -> {
                System.out.println("Data sent successfully");
                publishEvent("DATA_SENT", "bytes", event.get("bytes"));
                transitionTo("CONNECTED");
            });
            
            on("TRANSMISSION_ERROR", event -> {
                System.out.println("Transmission error: " + event.get("error"));
                transitionTo("CONNECTED");
            });
        }
    }
    
    public static class SleepState extends ReactiveState {
        public SleepState(EventBus eventBus) {
            super("SLEEP", eventBus);
        }
        
        @Override
        protected void onEnter() {
            System.out.println("Entering low-power sleep mode");
            publishEvent("POWER_MODE", "mode", "sleep");
        }
        
        @Override
        protected void onExit() {
            System.out.println("Waking up from sleep");
        }
        
        @Override
        protected void defineEventHandlers() {
            on("WAKE_UP", event -> {
                System.out.println("Wake signal received");
                transitionTo("CONNECTED");
            });
            
            on("SCHEDULED_WAKE", event -> {
                System.out.println("Scheduled wake time reached");
                transitionTo("CONNECTED");
            });
            
            on("SENSOR_TRIGGER", event -> {
                System.out.println("Sensor triggered wake: " + event.get("sensor"));
                transitionTo("CONNECTED");
            });
        }
    }
    
    public static class UpdatingState extends ReactiveState {
        public UpdatingState(EventBus eventBus) {
            super("UPDATING", eventBus);
        }
        
        @Override
        protected void onEnter() {
            System.out.println("Starting firmware update");
            publishEvent("UPDATE_STARTED", "version", "2.0.0");
        }
        
        @Override
        protected void onExit() {
            System.out.println("Update process finished");
        }
        
        @Override
        protected void defineEventHandlers() {
            on("UPDATE_PROGRESS", event -> {
                Integer progress = event.get("progress", Integer.class);
                System.out.println("Update progress: " + progress + "%");
            });
            
            on("UPDATE_COMPLETE", event -> {
                System.out.println("Firmware updated successfully");
                publishEvent("REBOOT_REQUIRED", "reason", "firmware_update");
                transitionTo("DISCONNECTED");
            });
            
            on("UPDATE_FAILED", event -> {
                System.out.println("Update failed: " + event.get("error"));
                transitionTo("CONNECTED");
            });
        }
    }
}