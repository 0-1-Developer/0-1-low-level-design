package com.example.observer.classic;

public class ForecastDisplay implements Observer {
    private float currentPressure = 29.92f;
    private float lastPressure;
    private final String displayName;

    public ForecastDisplay(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public void update(Subject subject) {
        if (subject instanceof WeatherStation) {
            WeatherStation weatherStation = (WeatherStation) subject;
            lastPressure = currentPressure;
            currentPressure = weatherStation.getPressure();
            display();
        }
    }

    public void display() {
        System.out.print("[" + displayName + "] Forecast: ");
        if (currentPressure > lastPressure) {
            System.out.println("Improving weather on the way!");
        } else if (currentPressure == lastPressure) {
            System.out.println("More of the same");
        } else if (currentPressure < lastPressure) {
            System.out.println("Watch out for cooler, rainy weather");
        }
    }

    public float getCurrentPressure() {
        return currentPressure;
    }
}