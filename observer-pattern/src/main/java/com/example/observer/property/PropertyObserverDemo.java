package com.example.observer.property;

public class PropertyObserverDemo {
    public static void main(String[] args) {
        System.out.println("=== Property/Listener-based Observer Pattern Demo ===");
        System.out.println("Demonstrating targeted property change notifications\n");

        PropertyWeatherStation station = new PropertyWeatherStation("PROP-WS-001");

        TemperatureAlertSystem tempAlert = new TemperatureAlertSystem("TempAlert", 85.0f);
        HumidityMonitor humidityMonitor = new HumidityMonitor("HumidityMonitor");
        LocationLogger locationLogger = new LocationLogger("LocationLogger");
        AllPropertyMonitor allMonitor = new AllPropertyMonitor("AllPropsMonitor");

        System.out.println("1. Setting up targeted listeners:");
        station.addPropertyChangeListener(PropertyWeatherStation.PROPERTY_TEMPERATURE, tempAlert);
        station.addPropertyChangeListener(PropertyWeatherStation.PROPERTY_HUMIDITY, humidityMonitor);
        station.addPropertyChangeListener(PropertyWeatherStation.PROPERTY_LOCATION, locationLogger);
        
        System.out.println("2. Setting up global listener (monitors all properties):");
        station.addPropertyChangeListener(allMonitor);
        
        System.out.println("\nListener counts:");
        System.out.println("   Temperature listeners: " + station.getPropertyListeners(PropertyWeatherStation.PROPERTY_TEMPERATURE));
        System.out.println("   Humidity listeners: " + station.getPropertyListeners(PropertyWeatherStation.PROPERTY_HUMIDITY));
        System.out.println("   Location listeners: " + station.getPropertyListeners(PropertyWeatherStation.PROPERTY_LOCATION));
        System.out.println("   Global listeners: " + station.getGlobalListeners());
        System.out.println("   Total listeners: " + station.getTotalListeners() + "\n");

        System.out.println("3. Updating individual properties (targeted notifications):");
        System.out.println("   Setting temperature to 75F...");
        station.setTemperature(75f);
        System.out.println();

        System.out.println("   Setting humidity to 60%...");
        station.setHumidity(60f);
        System.out.println();

        System.out.println("   Setting location to Chicago...");
        station.setLocation("Chicago");
        System.out.println();

        System.out.println("4. Triggering temperature alert:");
        station.setTemperature(90f);
        System.out.println();

        System.out.println("5. Multiple humidity changes to show trend analysis:");
        station.setHumidity(70f);
        station.setHumidity(85f);
        System.out.println();

        System.out.println("6. Bulk update (fires multiple property events):");
        station.updateAllMeasurements(32f, 45f, 30.8f, "Minneapolis");
        System.out.println();

        System.out.println("7. Revisiting a location:");
        station.setLocation("Chicago");
        System.out.println();

        System.out.println("8. Removing temperature listener and updating:");
        station.removePropertyChangeListener(PropertyWeatherStation.PROPERTY_TEMPERATURE, tempAlert);
        System.out.println("   Temperature listeners after removal: " + 
                station.getPropertyListeners(PropertyWeatherStation.PROPERTY_TEMPERATURE));
        station.setTemperature(100f);
        System.out.println();

        System.out.println("=== Property-based Observer Benefits Demonstrated ===");
        System.out.println(" Targeted notifications - listeners only get relevant events");
        System.out.println(" Granular subscription - can listen to specific properties");
        System.out.println(" Global listeners can monitor all changes");
        System.out.println(" Event objects carry old and new values");
        System.out.println(" Efficient - no unnecessary notifications");
        
        System.out.println("\n=== Final Statistics ===");
        System.out.println("Total property changes: " + allMonitor.getTotalChanges());
        System.out.println("Humidity readings: " + humidityMonitor.getReadingCount());
        System.out.println("Unique locations visited: " + locationLogger.getTotalLocations());

        System.out.println("\n=== Demo Complete ===");
    }
}