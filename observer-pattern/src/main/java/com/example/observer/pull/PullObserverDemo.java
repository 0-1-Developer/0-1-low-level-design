package com.example.observer.pull;

public class PullObserverDemo {
    public static void main(String[] args) {
        System.out.println("=== Pull Model Observer Pattern Demo ===");
        System.out.println("Demonstrating selective data retrieval by observers\n");

        PullWeatherStation weatherStation = new PullWeatherStation("WS-PULL-001");

        TemperatureOnlyDisplay tempDisplay = new TemperatureOnlyDisplay("TempDisplay");
        HumidityTracker humidityTracker = new HumidityTracker("HumidityTracker");
        CompleteWeatherMonitor completeMonitor = new CompleteWeatherMonitor("FullMonitor");

        System.out.println("1. Attaching observers to weather station...");
        weatherStation.attach(tempDisplay);
        weatherStation.attach(humidityTracker);
        weatherStation.attach(completeMonitor);
        System.out.println("   Observers attached: " + weatherStation.getObserverCount() + "\n");

        System.out.println("2. First weather update - observers pull what they need:");
        System.out.println("   (Notice how each observer pulls different data)");
        weatherStation.setMeasurements(75f, 60f, 30.1f, "Denver");
        System.out.println();

        System.out.println("3. Second weather update - demonstrating selective pulling:");
        weatherStation.setMeasurements(85f, 45f, 29.8f, "Las Vegas");
        System.out.println();

        System.out.println("4. Third weather update - humidity tracker shows change detection:");
        weatherStation.setMeasurements(78f, 80f, 29.5f, "Miami");
        System.out.println();

        System.out.println("5. Removing temperature display and updating:");
        weatherStation.detach(tempDisplay);
        System.out.println("   Remaining observers: " + weatherStation.getObserverCount());
        weatherStation.setMeasurements(32f, 85f, 30.8f, "Seattle");
        System.out.println();

        System.out.println("=== Pull Model Benefits Demonstrated ===");
        System.out.println(" Observers only request data they actually need");
        System.out.println(" Reduced bandwidth - no unnecessary data transfer");
        System.out.println(" Flexible - different observers can pull different subsets");
        System.out.println(" Subject doesn't need to know what data observers want");
        System.out.println(" Humidity tracker collected " + humidityTracker.getReadingCount() + " readings");

        System.out.println("\n=== Trade-offs Shown ===");
        System.out.println(" Multiple method calls per update");
        System.out.println(" Potential consistency issues if data changes between pulls");
        System.out.println(" More complex observer implementations");

        System.out.println("\n=== Demo Complete ===");
    }
}