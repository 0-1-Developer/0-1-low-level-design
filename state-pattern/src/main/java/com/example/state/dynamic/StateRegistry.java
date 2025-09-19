package com.example.state.dynamic;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class StateRegistry {
    private final Map<String, DynamicState> states = new HashMap<>();
    private final Map<String, String> aliases = new HashMap<>();
    
    public void registerState(String name, DynamicState state) {
        states.put(name, state);
        System.out.println("Registered state: " + name);
    }
    
    public void registerAlias(String alias, String stateName) {
        aliases.put(alias, stateName);
        System.out.println("Registered alias: " + alias + " -> " + stateName);
    }
    
    public DynamicState getState(String name) {
        String actualName = aliases.getOrDefault(name, name);
        DynamicState state = states.get(actualName);
        if (state == null) {
            System.out.println("Warning: State '" + name + "' not found, using fallback");
            return new FallbackState(name);
        }
        return state;
    }
    
    public void unregisterState(String name) {
        states.remove(name);
        aliases.values().removeIf(v -> v.equals(name));
        System.out.println("Unregistered state: " + name);
    }
    
    public boolean hasState(String name) {
        String actualName = aliases.getOrDefault(name, name);
        return states.containsKey(actualName);
    }
    
    public Set<String> getRegisteredStates() {
        return states.keySet();
    }
    
    public void listStates() {
        System.out.println("\n=== Registered States ===");
        states.forEach((name, state) -> {
            System.out.println("- " + name + " (" + state.getClass().getSimpleName() + ")");
        });
        if (!aliases.isEmpty()) {
            System.out.println("\n=== Aliases ===");
            aliases.forEach((alias, name) -> {
                System.out.println("- " + alias + " -> " + name);
            });
        }
    }
    
    public void replaceState(String name, DynamicState newState) {
        if (states.containsKey(name)) {
            System.out.println("Replacing state: " + name);
            states.put(name, newState);
        } else {
            System.out.println("Cannot replace non-existent state: " + name);
        }
    }
    
    public static class FallbackState implements DynamicState {
        private final String attemptedName;
        
        public FallbackState(String attemptedName) {
            this.attemptedName = attemptedName;
        }
        
        @Override
        public void enter(DynamicContext context) {
            System.out.println("FALLBACK: Attempted to enter undefined state '" + attemptedName + "'");
        }
        
        @Override
        public void execute(DynamicContext context, String event) {
            System.out.println("FALLBACK: Cannot handle event '" + event + "' in undefined state '" + attemptedName + "'");
        }
        
        @Override
        public void exit(DynamicContext context) {
            System.out.println("FALLBACK: Exiting undefined state '" + attemptedName + "'");
        }
        
        @Override
        public String getName() {
            return "FALLBACK[" + attemptedName + "]";
        }
    }
}