package com.example.observer.classic;

public class ClassicObserverDemo {
    public static void main(String[] args) {
        System.out.println("=== Classic GoF Observer Pattern Demo ===");
        System.out.println("Demonstrating one-to-many dependency with weather station and displays\n");

        WeatherStation weatherStation = new WeatherStation();

        CurrentConditionsDisplay currentDisplay = new CurrentConditionsDisplay("Current Conditions");
        StatisticsDisplay statsDisplay = new StatisticsDisplay("Statistics");
        ForecastDisplay forecastDisplay = new ForecastDisplay("Forecast");

        System.out.println("1. Registering observers with weather station...");
        weatherStation.attach(currentDisplay);
        weatherStation.attach(statsDisplay);
        weatherStation.attach(forecastDisplay);
        System.out.println("   Observers registered: " + weatherStation.getObserverCount() + "\n");

        System.out.println("2. First weather update:");
        weatherStation.setMeasurements(80f, 65f, 30.4f);
        System.out.println();

        System.out.println("3. Second weather update:");
        weatherStation.setMeasurements(82f, 70f, 29.2f);
        System.out.println();

        System.out.println("4. Third weather update:");
        weatherStation.setMeasurements(78f, 90f, 29.2f);
        System.out.println();

        System.out.println("5. Removing forecast display and updating again:");
        weatherStation.detach(forecastDisplay);
        System.out.println("   Observers remaining: " + weatherStation.getObserverCount());
        weatherStation.setMeasurements(75f, 85f, 30.1f);
        System.out.println();

        System.out.println("6. Adding forecast display back:");
        weatherStation.attach(forecastDisplay);
        System.out.println("   Observers registered: " + weatherStation.getObserverCount());
        weatherStation.setMeasurements(77f, 80f, 30.5f);

        System.out.println("\n=== Demo Complete ===");
    }
}