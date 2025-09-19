package com.example.state.dynamic;

import java.util.HashMap;
import java.util.Map;

public class DynamicContext {
    private final StateRegistry registry;
    private DynamicState currentState;
    private final Map<String, Object> data = new HashMap<>();
    
    public DynamicContext(StateRegistry registry) {
        this.registry = registry;
    }
    
    public void initialize(String stateName) {
        DynamicState state = registry.getState(stateName);
        this.currentState = state;
        state.enter(this);
    }
    
    public void transitionTo(String stateName) {
        if (currentState != null) {
            System.out.println("Transitioning from " + currentState.getName() + " to " + stateName);
            currentState.exit(this);
        }
        
        DynamicState newState = registry.getState(stateName);
        this.currentState = newState;
        newState.enter(this);
    }
    
    public void handleEvent(String event) {
        if (currentState == null) {
            System.out.println("No current state to handle event: " + event);
            return;
        }
        
        System.out.println("\nEvent: " + event + " in state: " + currentState.getName());
        currentState.execute(this, event);
    }
    
    public void setData(String key, Object value) {
        data.put(key, value);
    }
    
    public Object getData(String key) {
        return data.get(key);
    }
    
    public <T> T getData(String key, Class<T> type) {
        Object value = data.get(key);
        if (value != null && type.isInstance(value)) {
            return type.cast(value);
        }
        return null;
    }
    
    public DynamicState getCurrentState() {
        return currentState;
    }
    
    public StateRegistry getRegistry() {
        return registry;
    }
    
    public void printStatus() {
        System.out.println("\n=== Context Status ===");
        System.out.println("Current State: " + (currentState != null ? currentState.getName() : "None"));
        System.out.println("Data entries: " + data.size());
        data.forEach((k, v) -> System.out.println("  " + k + " = " + v));
    }
}