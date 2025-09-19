package com.example.state.table;

import java.util.HashMap;
import java.util.Map;

public class StateMachine {
    private String currentState;
    private final Map<String, StateTransition> transitionTable;
    private final Map<String, Object> context;

    public StateMachine(String initialState) {
        this.currentState = initialState;
        this.transitionTable = new HashMap<>();
        this.context = new HashMap<>();
    }

    public void addTransition(StateTransition transition) {
        transitionTable.put(transition.getKey(), transition);
    }

    public void addTransition(String fromState, String event, String toState, Runnable action) {
        addTransition(new StateTransition(fromState, event, toState, sm -> action.run()));
    }
    
    public void addTransition(String fromState, String event, String toState, java.util.function.Consumer<StateMachine> action) {
        addTransition(new StateTransition(fromState, event, toState, action));
    }

    public void addTransition(String fromState, String event, String toState) {
        addTransition(new StateTransition(fromState, event, toState, sm -> {}));
    }

    public boolean handleEvent(String event) {
        String key = currentState + ":" + event;
        StateTransition transition = transitionTable.get(key);
        
        if (transition == null) {
            System.out.println("No transition for " + event + " from state " + currentState);
            return false;
        }

        System.out.println("Transition: " + currentState + " --[" + event + "]--> " + transition.getToState());
        
        if (transition.getAction() != null) {
            transition.getAction().accept(this);
        }
        
        currentState = transition.getToState();
        return true;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setContextValue(String key, Object value) {
        context.put(key, value);
    }

    public Object getContextValue(String key) {
        return context.get(key);
    }

    public void printTransitionTable() {
        System.out.println("\n=== Transition Table ===");
        System.out.println("From State\tEvent\t\tTo State");
        System.out.println("----------------------------------------");
        for (StateTransition t : transitionTable.values()) {
            System.out.printf("%-15s\t%-15s\t%-15s%n", 
                t.getFromState(), t.getEvent(), t.getToState());
        }
        System.out.println();
    }
}