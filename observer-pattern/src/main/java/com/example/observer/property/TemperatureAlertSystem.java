package com.example.observer.property;

public class TemperatureAlertSystem implements PropertyChangeListener {
    private final String systemName;
    private final float alertThreshold;

    public TemperatureAlertSystem(String systemName, float alertThreshold) {
        this.systemName = systemName;
        this.alertThreshold = alertThreshold;
    }

    @Override
    public void propertyChanged(PropertyChangeEvent event) {
        if (PropertyWeatherStation.PROPERTY_TEMPERATURE.equals(event.getPropertyName())) {
            float newTemp = (Float) event.getNewValue();
            float oldTemp = (Float) event.getOldValue();
            
            System.out.println("[" + systemName + "] Temperature changed: " + oldTemp + "F  " + newTemp + "F");
            
            if (newTemp >= alertThreshold && oldTemp < alertThreshold) {
                System.out.println("[" + systemName + "] ¨ HIGH TEMPERATURE ALERT: " + newTemp + "F reached!");
            } else if (newTemp < alertThreshold && oldTemp >= alertThreshold) {
                System.out.println("[" + systemName + "]  Temperature back to normal: " + newTemp + "F");
            }
        }
    }

    public float getAlertThreshold() {
        return alertThreshold;
    }
}