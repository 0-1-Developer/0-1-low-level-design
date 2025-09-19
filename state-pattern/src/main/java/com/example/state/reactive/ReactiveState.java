package com.example.state.reactive;

import java.util.Set;
import java.util.HashSet;

public abstract class ReactiveState {
    protected final String name;
    protected final EventBus eventBus;
    protected ReactiveContext context;
    protected Set<String> subscribedEvents = new HashSet<>();
    
    public ReactiveState(String name, EventBus eventBus) {
        this.name = name;
        this.eventBus = eventBus;
    }
    
    public void setContext(ReactiveContext context) {
        this.context = context;
    }
    
    public void enter() {
        System.out.println("Entering state: " + name);
        subscribeToEvents();
        onEnter();
        
        eventBus.publish(new Event.Builder()
            .type("STATE_ENTERED")
            .source(name)
            .payload("state", name)
            .build());
    }
    
    public void exit() {
        System.out.println("Exiting state: " + name);
        unsubscribeFromEvents();
        onExit();
        
        eventBus.publish(new Event.Builder()
            .type("STATE_EXITED")
            .source(name)
            .payload("state", name)
            .build());
    }
    
    protected abstract void onEnter();
    protected abstract void onExit();
    protected abstract void defineEventHandlers();
    
    protected void on(String eventType, java.util.function.Consumer<Event> handler) {
        subscribedEvents.add(eventType);
        eventBus.subscribe(eventType, event -> {
            if (context != null && context.getCurrentState() == this) {
                handler.accept(event);
            }
        });
    }
    
    private void subscribeToEvents() {
        defineEventHandlers();
    }
    
    private void unsubscribeFromEvents() {
    }
    
    protected void transitionTo(String stateName) {
        if (context != null) {
            context.transitionTo(stateName);
        }
    }
    
    protected void publishEvent(String type, String key, Object value) {
        eventBus.publish(new Event.Builder()
            .type(type)
            .source(name)
            .payload(key, value)
            .build());
    }
    
    public String getName() {
        return name;
    }
}