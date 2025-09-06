package com.example.observer.push;

public class PushObserverDemo {
    public static void main(String[] args) {
        System.out.println("=== Push Model Observer Pattern Demo ===");
        System.out.println("Demonstrating data push to observers with complete payloads\n");

        PushWeatherStation weatherStation = new PushWeatherStation("WS-001");

        MobileApp weatherApp = new MobileApp("WeatherNow", "user123");
        MobileApp alertApp = new MobileApp("WeatherAlert", "user456");
        WeatherWebsite website = new WeatherWebsite("WeatherCentral.com");
        DataAnalyticsService analytics = new DataAnalyticsService("WeatherAnalytics");

        System.out.println("1. Subscribing observers to weather station...");
        weatherStation.subscribe(weatherApp);
        weatherStation.subscribe(alertApp);
        weatherStation.subscribe(website);
        weatherStation.subscribe(analytics);
        System.out.println("   Subscribers: " + weatherStation.getSubscriberCount() + "\n");

        System.out.println("2. Broadcasting first weather report:");
        System.out.println("   (All observers receive complete weather data package)");
        weatherStation.reportWeather(72f, 45f, 30.1f, "New York City");
        System.out.println();

        System.out.println("3. Broadcasting second weather report:");
        weatherStation.reportWeather(88f, 65f, 29.8f, "Miami");
        System.out.println();

        System.out.println("4. Unsubscribing one mobile app:");
        weatherStation.unsubscribe(alertApp);
        System.out.println("   Remaining subscribers: " + weatherStation.getSubscriberCount());
        weatherStation.reportWeather(95f, 70f, 29.5f, "Phoenix");
        System.out.println();

        System.out.println("5. Broadcasting extreme weather (triggers special alerts):");
        weatherStation.reportWeather(25f, 80f, 30.8f, "Minneapolis");
        System.out.println();

        System.out.println("=== Push Model Benefits Demonstrated ===");
        System.out.println(" Observers receive complete data payload immediately");
        System.out.println(" No additional method calls needed by observers");
        System.out.println(" Subject pushes all relevant data in one notification");
        System.out.println(" Analytics service has " + analytics.getDataPointCount() + " complete data points");

        System.out.println("\n=== Demo Complete ===");
    }
}