package com.example.observer.pull;

import java.text.SimpleDateFormat;

public class CompleteWeatherMonitor implements PullObserver {
    private final String monitorName;
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    public CompleteWeatherMonitor(String monitorName) {
        this.monitorName = monitorName;
    }

    @Override
    public void update(PullSubject subject) {
        if (subject instanceof PullWeatherStation) {
            PullWeatherStation station = (PullWeatherStation) subject;
            
            System.out.println("[" + monitorName + "] Update notification received - pulling all available data");
            
            if (station.hasValidData()) {
                String location = station.getLocation();
                float temperature = station.getTemperature();
                float humidity = station.getHumidity();
                float pressure = station.getPressure();
                String timestamp = timeFormat.format(station.getLastUpdated());
                
                System.out.println("[" + monitorName + "] Complete Weather Report:");
                System.out.println("   Location: " + location);
                System.out.println("   � Temperature: " + temperature + "F");
                System.out.println("   � Humidity: " + humidity + "%");
                System.out.println("   � Pressure: " + pressure + " inHg");
                System.out.println("    Last Updated: " + timestamp);
                
                generateWeatherSummary(temperature, humidity, pressure);
            }
        }
    }

    private void generateWeatherSummary(float temp, float humidity, float pressure) {
        System.out.print("[" + monitorName + "] Summary: ");
        
        if (temp > 85 && humidity > 70) {
            System.out.println("Hot and humid - heat index warning");
        } else if (temp < 32 && pressure > 30.2) {
            System.out.println("Cold and clear - frost possible");
        } else if (pressure < 29.5) {
            System.out.println("Low pressure - storm approaching");
        } else {
            System.out.println("Pleasant weather conditions");
        }
    }
}