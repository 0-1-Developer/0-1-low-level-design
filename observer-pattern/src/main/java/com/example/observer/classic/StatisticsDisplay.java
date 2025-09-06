package com.example.observer.classic;

import java.util.ArrayList;
import java.util.List;

public class StatisticsDisplay implements Observer {
    private final List<Float> temperatures = new ArrayList<>();
    private final String displayName;

    public StatisticsDisplay(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public void update(Subject subject) {
        if (subject instanceof WeatherStation) {
            WeatherStation weatherStation = (WeatherStation) subject;
            temperatures.add(weatherStation.getTemperature());
            display();
        }
    }

    public void display() {
        if (temperatures.isEmpty()) {
            System.out.println("[" + displayName + "] No temperature data available");
            return;
        }

        float sum = 0;
        float max = temperatures.get(0);
        float min = temperatures.get(0);

        for (float temp : temperatures) {
            sum += temp;
            max = Math.max(max, temp);
            min = Math.min(min, temp);
        }

        float avg = sum / temperatures.size();
        System.out.println("[" + displayName + "] Avg/Max/Min temperature: " +
                String.format("%.1f/%.1f/%.1f", avg, max, min));
    }

    public float getAverageTemperature() {
        if (temperatures.isEmpty()) return 0;
        return (float) temperatures.stream().mapToDouble(Float::doubleValue).average().orElse(0);
    }
}