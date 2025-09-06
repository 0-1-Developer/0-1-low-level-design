package com.example.observer.eventbus;

public class Event {
    private final String topic;
    private final Object data;
    private final long timestamp;

    public Event(String topic, Object data) {
        this.topic = topic;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    public String getTopic() {
        return topic;
    }

    public Object getData() {
        return data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("Event{topic='%s', data=%s}", topic, data);
    }
}