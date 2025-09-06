package com.example.observer.pull;

import java.util.ArrayList;
import java.util.List;

public class HumidityTracker implements PullObserver {
    private final String trackerName;
    private final List<Float> humidityReadings = new ArrayList<>();

    public HumidityTracker(String trackerName) {
        this.trackerName = trackerName;
    }

    @Override
    public void update(PullSubject subject) {
        if (subject instanceof PullWeatherStation) {
            PullWeatherStation station = (PullWeatherStation) subject;
            
            System.out.println("[" + trackerName + "] Update notification received - pulling humidity data only");
            
            if (station.hasValidData()) {
                float humidity = station.getHumidity();
                humidityReadings.add(humidity);
                
                System.out.println("[" + trackerName + "] Current humidity: " + humidity + "%");
                System.out.println("[" + trackerName + "] Total readings collected: " + humidityReadings.size());
                
                if (humidityReadings.size() >= 2) {
                    float previous = humidityReadings.get(humidityReadings.size() - 2);
                    float change = humidity - previous;
                    if (Math.abs(change) > 10) {
                        System.out.println("[" + trackerName + "] Š Significant humidity change: " + 
                                (change > 0 ? "+" : "") + String.format("%.1f%%", change));
                    }
                }
            }
        }
    }

    public int getReadingCount() {
        return humidityReadings.size();
    }
}