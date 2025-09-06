package com.example.observer.property;

import java.util.ArrayList;
import java.util.List;

public class HumidityMonitor implements PropertyChangeListener {
    private final String monitorName;
    private final List<Float> humidityHistory = new ArrayList<>();

    public HumidityMonitor(String monitorName) {
        this.monitorName = monitorName;
    }

    @Override
    public void propertyChanged(PropertyChangeEvent event) {
        if (PropertyWeatherStation.PROPERTY_HUMIDITY.equals(event.getPropertyName())) {
            float newHumidity = (Float) event.getNewValue();
            float oldHumidity = (Float) event.getOldValue();
            
            humidityHistory.add(newHumidity);
            
            System.out.println("[" + monitorName + "] Humidity changed: " + oldHumidity + "%  " + newHumidity + "%");
            
            if (newHumidity > 80) {
                System.out.println("[" + monitorName + "] High humidity warning - potential mold risk");
            } else if (newHumidity < 30) {
                System.out.println("[" + monitorName + "] Low humidity alert - dry air conditions");
            }
            
            if (humidityHistory.size() >= 3) {
                analyzeHumidityTrend();
            }
        }
    }

    private void analyzeHumidityTrend() {
        int size = humidityHistory.size();
        float current = humidityHistory.get(size - 1);
        float previous = humidityHistory.get(size - 2);
        float beforePrevious = humidityHistory.get(size - 3);
        
        if (current > previous && previous > beforePrevious) {
            System.out.println("[" + monitorName + "] Humidity trending upward - storm approaching?");
        } else if (current < previous && previous < beforePrevious) {
            System.out.println("[" + monitorName + "] Humidity trending downward - clearing weather");
        }
    }

    public int getReadingCount() {
        return humidityHistory.size();
    }
}