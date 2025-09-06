package com.example.observer.property;

import java.util.HashMap;
import java.util.Map;

public class LocationLogger implements PropertyChangeListener {
    private final String loggerName;
    private final Map<String, Integer> locationCounts = new HashMap<>();

    public LocationLogger(String loggerName) {
        this.loggerName = loggerName;
    }

    @Override
    public void propertyChanged(PropertyChangeEvent event) {
        if (PropertyWeatherStation.PROPERTY_LOCATION.equals(event.getPropertyName())) {
            String newLocation = (String) event.getNewValue();
            String oldLocation = (String) event.getOldValue();
            
            System.out.println("[" + loggerName + "] Location changed: '" + oldLocation + "'  '" + newLocation + "'");
            
            locationCounts.put(newLocation, locationCounts.getOrDefault(newLocation, 0) + 1);
            
            System.out.println("[" + loggerName + "] Location visit count for '" + newLocation + "': " + 
                    locationCounts.get(newLocation));
            
            if (locationCounts.get(newLocation) > 1) {
                System.out.println("[" + loggerName + "] Returning to previously visited location: " + newLocation);
            }
        }
    }

    public Map<String, Integer> getLocationCounts() {
        return new HashMap<>(locationCounts);
    }

    public int getTotalLocations() {
        return locationCounts.size();
    }
}