package com.example.observer.push;

public class PushWeatherStation extends PushSubject<WeatherData> {
    private final String stationId;

    public PushWeatherStation(String stationId) {
        this.stationId = stationId;
    }

    public void reportWeather(float temperature, float humidity, float pressure, String location) {
        WeatherData weatherData = new WeatherData(temperature, humidity, pressure, location);
        System.out.println("[Station " + stationId + "] Broadcasting weather data: " + weatherData);
        notifyObservers(weatherData);
    }

    public String getStationId() {
        return stationId;
    }
}