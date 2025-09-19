package com.example.state.persistent;

import java.util.HashMap;
import java.util.Map;

public class PersistentWorkflow {
    private final String workflowId;
    private String currentState;
    private final Map<String, Object> context = new HashMap<>();
    private final StateStore stateStore;
    
    public PersistentWorkflow(String workflowId, String initialState, StateStore stateStore) {
        this.workflowId = workflowId;
        this.currentState = initialState;
        this.stateStore = stateStore;
    }
    
    public void transitionTo(String newState) {
        System.out.println("Workflow " + workflowId + " transitioning: " + currentState + " -> " + newState);
        currentState = newState;
    }
    
    public void setContextValue(String key, Object value) {
        context.put(key, value);
    }
    
    public Object getContextValue(String key) {
        return context.get(key);
    }
    
    public void restoreFromSnapshot(StateSnapshot snapshot) {
        this.currentState = snapshot.getStateName();
        this.context.clear();
        this.context.putAll(snapshot.getContext());
        System.out.println("Restored workflow " + workflowId + " to state: " + currentState);
    }
    
    public String getCurrentState() {
        return currentState;
    }
    
    public Map<String, Object> getContext() {
        return new HashMap<>(context);
    }
    
    public String getWorkflowId() {
        return workflowId;
    }
    
    public void printStatus() {
        System.out.println("\n=== Workflow Status ===");
        System.out.println("ID: " + workflowId);
        System.out.println("Current State: " + currentState);
        System.out.println("Context: " + context);
    }
}