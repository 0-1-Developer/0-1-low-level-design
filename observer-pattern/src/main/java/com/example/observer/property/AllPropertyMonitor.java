package com.example.observer.property;

public class AllPropertyMonitor implements PropertyChangeListener {
    private final String monitorName;
    private int totalChanges = 0;

    public AllPropertyMonitor(String monitorName) {
        this.monitorName = monitorName;
    }

    @Override
    public void propertyChanged(PropertyChangeEvent event) {
        totalChanges++;
        
        System.out.println("[" + monitorName + "] Property '" + event.getPropertyName() + 
                "' changed: " + event.getOldValue() + "  " + event.getNewValue());
        System.out.println("[" + monitorName + "] Total property changes tracked: " + totalChanges);
        
        String propertyName = event.getPropertyName();
        Object newValue = event.getNewValue();
        
        switch (propertyName) {
            case PropertyWeatherStation.PROPERTY_TEMPERATURE:
                float temp = (Float) newValue;
                if (temp > 100) {
                    System.out.println("[" + monitorName + "] EXTREME HEAT WARNING!");
                } else if (temp < 0) {
                    System.out.println("[" + monitorName + "] FREEZING ALERT!");
                }
                break;
                
            case PropertyWeatherStation.PROPERTY_HUMIDITY:
                float humidity = (Float) newValue;
                if (humidity > 90) {
                    System.out.println("[" + monitorName + "] EXTREMELY HUMID CONDITIONS!");
                }
                break;
                
            case PropertyWeatherStation.PROPERTY_PRESSURE:
                float pressure = (Float) newValue;
                if (pressure < 29.0) {
                    System.out.println("[" + monitorName + "] SEVERE LOW PRESSURE - STORM WARNING!");
                }
                break;
                
            case PropertyWeatherStation.PROPERTY_LOCATION:
                System.out.println("[" + monitorName + "] Weather station has moved to: " + newValue);
                break;
        }
    }

    public int getTotalChanges() {
        return totalChanges;
    }
}