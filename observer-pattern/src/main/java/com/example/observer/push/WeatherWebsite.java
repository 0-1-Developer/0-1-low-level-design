package com.example.observer.push;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherWebsite implements PushObserver<WeatherData> {
    private final String websiteName;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public WeatherWebsite(String websiteName) {
        this.websiteName = websiteName;
    }

    @Override
    public void update(WeatherData data) {
        String timestamp = dateFormat.format(new Date(data.getTimestamp()));
        System.out.println("[" + websiteName + "] Website updated at " + timestamp + ":");
        System.out.println("   Location: " + data.getLocation());
        System.out.println("   � Temperature: " + data.getTemperature() + "F");
        System.out.println("   � Humidity: " + data.getHumidity() + "%");
        System.out.println("   � Pressure: " + data.getPressure() + " inHg");
    }

    public String getWebsiteName() {
        return websiteName;
    }
}