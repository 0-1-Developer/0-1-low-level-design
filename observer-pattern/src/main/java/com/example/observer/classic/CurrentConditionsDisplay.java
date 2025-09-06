package com.example.observer.classic;

public class CurrentConditionsDisplay implements Observer {
    private float temperature;
    private float humidity;
    private final String displayName;

    public CurrentConditionsDisplay(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public void update(Subject subject) {
        if (subject instanceof WeatherStation) {
            WeatherStation weatherStation = (WeatherStation) subject;
            this.temperature = weatherStation.getTemperature();
            this.humidity = weatherStation.getHumidity();
            display();
        }
    }

    public void display() {
        System.out.println("[" + displayName + "] Current conditions: " +
                temperature + "F, " + humidity + "% humidity");
    }

    public float getTemperature() {
        return temperature;
    }

    public float getHumidity() {
        return humidity;
    }
}