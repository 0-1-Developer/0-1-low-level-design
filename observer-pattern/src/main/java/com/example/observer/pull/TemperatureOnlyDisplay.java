package com.example.observer.pull;

public class TemperatureOnlyDisplay implements PullObserver {
    private final String displayName;

    public TemperatureOnlyDisplay(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public void update(PullSubject subject) {
        if (subject instanceof PullWeatherStation) {
            PullWeatherStation station = (PullWeatherStation) subject;
            
            System.out.println("[" + displayName + "] Update notification received - pulling only temperature data");
            
            if (station.hasValidData()) {
                float temperature = station.getTemperature();
                System.out.println("[" + displayName + "] Temperature Display: " + temperature + "F");
                
                if (temperature > 80) {
                    System.out.println("[" + displayName + "]  High temperature alert!");
                }
            }
        }
    }
}