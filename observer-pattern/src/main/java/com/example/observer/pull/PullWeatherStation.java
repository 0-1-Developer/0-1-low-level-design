package com.example.observer.pull;

import java.util.Date;

public class PullWeatherStation extends PullSubject {
    private float temperature;
    private float humidity;
    private float pressure;
    private String location;
    private Date lastUpdated;
    private final String stationId;

    public PullWeatherStation(String stationId) {
        this.stationId = stationId;
    }

    public void setMeasurements(float temperature, float humidity, float pressure, String location) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.location = location;
        this.lastUpdated = new Date();
        
        System.out.println("[Station " + stationId + "] Data updated - notifying observers to pull latest data");
        notifyObservers();
    }

    public float getTemperature() {
        System.out.println("[Station " + stationId + "] Temperature requested: " + temperature + "F");
        return temperature;
    }

    public float getHumidity() {
        System.out.println("[Station " + stationId + "] Humidity requested: " + humidity + "%");
        return humidity;
    }

    public float getPressure() {
        System.out.println("[Station " + stationId + "] Pressure requested: " + pressure + " inHg");
        return pressure;
    }

    public String getLocation() {
        System.out.println("[Station " + stationId + "] Location requested: " + location);
        return location;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public String getStationId() {
        return stationId;
    }

    public boolean hasValidData() {
        return lastUpdated != null;
    }
}