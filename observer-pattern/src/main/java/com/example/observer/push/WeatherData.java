package com.example.observer.push;

public class WeatherData {
    private final float temperature;
    private final float humidity;
    private final float pressure;
    private final String location;
    private final long timestamp;

    public WeatherData(float temperature, float humidity, float pressure, String location) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.location = location;
        this.timestamp = System.currentTimeMillis();
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

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("WeatherData{temp=%.1fF, humidity=%.1f%%, pressure=%.1f, location='%s'}",
                temperature, humidity, pressure, location);
    }
}