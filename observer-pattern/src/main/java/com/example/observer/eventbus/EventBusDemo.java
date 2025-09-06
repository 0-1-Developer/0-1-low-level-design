package com.example.observer.eventbus;

public class EventBusDemo {
    public static void main(String[] args) {
        System.out.println("=== Event Bus/Pub-Sub Observer Pattern Demo ===");
        System.out.println("Demonstrating decoupled communication via topics\n");

        EventBus weatherBus = new EventBus("WeatherBus");

        System.out.println("1. Setting up topic subscribers:");
        
        weatherBus.subscribe("temperature", event -> {
            Float temp = (Float) event.getData();
            System.out.println("[TempAlert] Temperature event: " + temp + "F");
            if (temp > 85) System.out.println("[TempAlert] HIGH TEMPERATURE WARNING!");
        });

        weatherBus.subscribe("humidity", event -> {
            Float humidity = (Float) event.getData();
            System.out.println("[HumidityMonitor] Humidity event: " + humidity + "%");
        });

        weatherBus.subscribe("weather.severe", event -> {
            String message = (String) event.getData();
            System.out.println("[EmergencyAlert] SEVERE WEATHER: " + message);
        });

        weatherBus.subscribe("temperature", event -> {
            Float temp = (Float) event.getData();
            System.out.println("[Logger] Recording temperature: " + temp + "F");
        });

        System.out.println("   Total subscribers: " + weatherBus.getTotalSubscriberCount());
        System.out.println("   Temperature topic subscribers: " + weatherBus.getSubscriberCount("temperature"));
        System.out.println();

        System.out.println("2. Publishing temperature events:");
        weatherBus.publish(new Event("temperature", 72.5f));
        System.out.println();

        weatherBus.publish(new Event("temperature", 95.0f));
        System.out.println();

        System.out.println("3. Publishing humidity events:");
        weatherBus.publish(new Event("humidity", 65.0f));
        System.out.println();

        System.out.println("4. Publishing severe weather alert:");
        weatherBus.publish(new Event("weather.severe", "Tornado warning issued"));
        System.out.println();

        System.out.println("5. Publishing to non-existent topic:");
        weatherBus.publish(new Event("pressure", 30.1f));
        System.out.println();

        System.out.println("=== Event Bus Benefits Demonstrated ===");
        System.out.println("✓ Complete decoupling - publishers don't know subscribers");
        System.out.println("✓ Topic-based routing - targeted message delivery");
        System.out.println("✓ Multiple subscribers per topic supported");
        System.out.println("✓ Dynamic subscription - can add/remove at runtime");
        System.out.println("✓ Error isolation - one subscriber failure doesn't affect others");

        System.out.println("\n=== Demo Complete ===");
    }
}