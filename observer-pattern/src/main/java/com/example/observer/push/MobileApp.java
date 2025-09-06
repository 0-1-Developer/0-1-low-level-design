package com.example.observer.push;

public class MobileApp implements PushObserver<WeatherData> {
    private final String appName;
    private final String userId;

    public MobileApp(String appName, String userId) {
        this.appName = appName;
        this.userId = userId;
    }

    @Override
    public void update(WeatherData data) {
        System.out.println("[" + appName + " - User: " + userId + "] Push notification: " +
                "It's " + data.getTemperature() + "F with " + data.getHumidity() + "% humidity in " + data.getLocation());
        
        if (data.getTemperature() > 85) {
            System.out.println("    Hot weather alert sent to " + userId);
        } else if (data.getTemperature() < 32) {
            System.out.println("    Freeze warning sent to " + userId);
        }
    }

    public String getAppName() {
        return appName;
    }

    public String getUserId() {
        return userId;
    }
}