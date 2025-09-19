package com.example.state.reactive;

import java.util.HashMap;
import java.util.Map;

public class ReactiveContext {
    private final Map<String, ReactiveState> states = new HashMap<>();
    private final EventBus eventBus;
    private ReactiveState currentState;
    
    public ReactiveContext(EventBus eventBus) {
        this.eventBus = eventBus;
    }
    
    public void addState(ReactiveState state) {
        state.setContext(this);
        states.put(state.getName(), state);
    }
    
    public void initialize(String stateName) {
        ReactiveState state = states.get(stateName);
        if (state != null) {
            currentState = state;
            currentState.enter();
        } else {
            System.err.println("State not found: " + stateName);
        }
    }
    
    public void transitionTo(String stateName) {
        ReactiveState newState = states.get(stateName);
        if (newState == null) {
            System.err.println("Cannot transition to unknown state: " + stateName);
            return;
        }
        
        if (currentState != null) {
            currentState.exit();
        }
        
        System.out.println("Transition: " + 
            (currentState != null ? currentState.getName() : "NULL") + " -> " + stateName);
        
        currentState = newState;
        currentState.enter();
    }
    
    public ReactiveState getCurrentState() {
        return currentState;
    }
    
    public void handleEvent(Event event) {
        eventBus.publish(event);
    }
}