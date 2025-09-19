package com.example.state.reactive;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class EventBus {
    private final Map<String, List<Consumer<Event>>> subscribers = new ConcurrentHashMap<>();
    private final BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private volatile boolean running = false;
    
    public EventBus() {
        start();
    }
    
    public void subscribe(String eventType, Consumer<Event> handler) {
        subscribers.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(handler);
        System.out.println("Subscribed to event type: " + eventType);
    }
    
    public void unsubscribe(String eventType, Consumer<Event> handler) {
        List<Consumer<Event>> handlers = subscribers.get(eventType);
        if (handlers != null) {
            handlers.remove(handler);
        }
    }
    
    public void publish(Event event) {
        System.out.println("Publishing event: " + event.getType());
        try {
            eventQueue.offer(event, 100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public void publishSync(Event event) {
        System.out.println("Publishing synchronous event: " + event.getType());
        processEvent(event);
    }
    
    private void processEvent(Event event) {
        List<Consumer<Event>> handlers = subscribers.get(event.getType());
        if (handlers != null) {
            for (Consumer<Event> handler : handlers) {
                try {
                    handler.accept(event);
                } catch (Exception e) {
                    System.err.println("Error handling event: " + e.getMessage());
                }
            }
        }
        
        List<Consumer<Event>> wildcardHandlers = subscribers.get("*");
        if (wildcardHandlers != null) {
            for (Consumer<Event> handler : wildcardHandlers) {
                try {
                    handler.accept(event);
                } catch (Exception e) {
                    System.err.println("Error in wildcard handler: " + e.getMessage());
                }
            }
        }
    }
    
    private void start() {
        running = true;
        executor.submit(() -> {
            while (running) {
                try {
                    Event event = eventQueue.poll(100, TimeUnit.MILLISECONDS);
                    if (event != null) {
                        processEvent(event);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }
    
    public void stop() {
        running = false;
        executor.shutdown();
        try {
            if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }
}