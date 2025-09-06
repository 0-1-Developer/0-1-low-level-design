package com.example.observer.property;

public class PropertyWeatherStation {
    public static final String PROPERTY_TEMPERATURE = "temperature";
    public static final String PROPERTY_HUMIDITY = "humidity";
    public static final String PROPERTY_PRESSURE = "pressure";
    public static final String PROPERTY_LOCATION = "location";

    private final PropertyChangeSupport support;
    private final String stationId;
    
    private float temperature;
    private float humidity;
    private float pressure;
    private String location;

    public PropertyWeatherStation(String stationId) {
        this.stationId = stationId;
        this.support = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(propertyName, listener);
    }

    public void setTemperature(float temperature) {
        float oldValue = this.temperature;
        this.temperature = temperature;
        support.firePropertyChange(PROPERTY_TEMPERATURE, oldValue, temperature);
    }

    public void setHumidity(float humidity) {
        float oldValue = this.humidity;
        this.humidity = humidity;
        support.firePropertyChange(PROPERTY_HUMIDITY, oldValue, humidity);
    }

    public void setPressure(float pressure) {
        float oldValue = this.pressure;
        this.pressure = pressure;
        support.firePropertyChange(PROPERTY_PRESSURE, oldValue, pressure);
    }

    public void setLocation(String location) {
        String oldValue = this.location;
        this.location = location;
        support.firePropertyChange(PROPERTY_LOCATION, oldValue, location);
    }

    public void updateAllMeasurements(float temperature, float humidity, float pressure, String location) {
        System.out.println("[Station " + stationId + "] Updating all measurements - will fire individual property events");
        setTemperature(temperature);
        setHumidity(humidity);
        setPressure(pressure);
        setLocation(location);
    }

    public float getTemperature() {
        return temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getPressure() {
        return pressure;
    }

    public String getLocation() {
        return location;
    }

    public String getStationId() {
        return stationId;
    }

    public int getTotalListeners() {
        return support.getTotalListenerCount();
    }

    public int getGlobalListeners() {
        return support.getGlobalListenerCount();
    }

    public int getPropertyListeners(String propertyName) {
        return support.getPropertyListenerCount(propertyName);
    }
}