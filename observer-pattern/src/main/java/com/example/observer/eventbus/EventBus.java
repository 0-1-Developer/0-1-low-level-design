package com.example.observer.eventbus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EventBus {
    private final Map<String, List<Consumer<Event>>> subscribers = new HashMap<>();
    private final String busName;

    public EventBus(String busName) {
        this.busName = busName;
    }

    public void subscribe(String topic, Consumer<Event> subscriber) {
        subscribers.computeIfAbsent(topic, k -> new ArrayList<>()).add(subscriber);
        System.out.println("[" + busName + "] New subscriber added to topic: " + topic);
    }

    public void unsubscribe(String topic, Consumer<Event> subscriber) {
        List<Consumer<Event>> topicSubscribers = subscribers.get(topic);
        if (topicSubscribers != null) {
            topicSubscribers.remove(subscriber);
            if (topicSubscribers.isEmpty()) {
                subscribers.remove(topic);
            }
        }
    }

    public void publish(Event event) {
        System.out.println("[" + busName + "] Publishing: " + event);
        List<Consumer<Event>> topicSubscribers = subscribers.get(event.getTopic());
        if (topicSubscribers != null) {
            for (Consumer<Event> subscriber : topicSubscribers) {
                try {
                    subscriber.accept(event);
                } catch (Exception e) {
                    System.out.println("[" + busName + "] Error processing event: " + e.getMessage());
                }
            }
        } else {
            System.out.println("[" + busName + "] No subscribers for topic: " + event.getTopic());
        }
    }

    public int getSubscriberCount(String topic) {
        List<Consumer<Event>> topicSubscribers = subscribers.get(topic);
        return topicSubscribers != null ? topicSubscribers.size() : 0;
    }

    public int getTotalSubscriberCount() {
        return subscribers.values().stream().mapToInt(List::size).sum();
    }
}