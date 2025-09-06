package com.example.observer.push;

import java.util.ArrayList;
import java.util.List;

public class DataAnalyticsService implements PushObserver<WeatherData> {
    private final String serviceName;
    private final List<WeatherData> dataHistory = new ArrayList<>();

    public DataAnalyticsService(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public void update(WeatherData data) {
        dataHistory.add(data);
        System.out.println("[" + serviceName + "] Received complete weather dataset:");
        System.out.println("   Raw data: " + data);
        System.out.println("   Total data points collected: " + dataHistory.size());
        
        analyzeData(data);
    }

    private void analyzeData(WeatherData data) {
        if (dataHistory.size() < 2) {
            System.out.println("   Analysis: Need more data points for trending");
            return;
        }

        WeatherData previous = dataHistory.get(dataHistory.size() - 2);
        float tempChange = data.getTemperature() - previous.getTemperature();
        float humidityChange = data.getHumidity() - previous.getHumidity();

        System.out.println("   Analysis: Temperature " + 
                (tempChange > 0 ? "increased" : tempChange < 0 ? "decreased" : "unchanged") +
                " by " + Math.abs(tempChange) + "F");
        System.out.println("   Analysis: Humidity " + 
                (humidityChange > 0 ? "increased" : humidityChange < 0 ? "decreased" : "unchanged") +
                " by " + Math.abs(humidityChange) + "%");
    }

    public int getDataPointCount() {
        return dataHistory.size();
    }
}