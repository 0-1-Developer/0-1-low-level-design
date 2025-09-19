package com.example.state.functional;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class FunctionalStateMachine<S, E> {
    private S currentState;
    private final Map<S, Consumer<S>> entryActions = new HashMap<>();
    private final Map<S, Consumer<S>> exitActions = new HashMap<>();
    private final Map<String, BiFunction<S, E, S>> transitions = new HashMap<>();
    private final Map<String, BiConsumer<S, E>> transitionActions = new HashMap<>();
    
    public FunctionalStateMachine(S initialState) {
        this.currentState = initialState;
    }
    
    public FunctionalStateMachine<S, E> onEntry(S state, Consumer<S> action) {
        entryActions.put(state, action);
        return this;
    }
    
    public FunctionalStateMachine<S, E> onExit(S state, Consumer<S> action) {
        exitActions.put(state, action);
        return this;
    }
    
    public FunctionalStateMachine<S, E> addTransition(S from, E event, S to) {
        String key = makeKey(from, event);
        transitions.put(key, (s, e) -> to);
        return this;
    }
    
    public FunctionalStateMachine<S, E> addTransition(S from, E event, S to, BiConsumer<S, E> action) {
        String key = makeKey(from, event);
        transitions.put(key, (s, e) -> to);
        transitionActions.put(key, action);
        return this;
    }
    
    public FunctionalStateMachine<S, E> addConditionalTransition(
            S from, E event, BiFunction<S, E, S> transitionFunction) {
        String key = makeKey(from, event);
        transitions.put(key, transitionFunction);
        return this;
    }
    
    public boolean handleEvent(E event) {
        String key = makeKey(currentState, event);
        BiFunction<S, E, S> transition = transitions.get(key);
        
        if (transition == null) {
            System.out.println("No transition for event " + event + " from state " + currentState);
            return false;
        }
        
        S nextState = transition.apply(currentState, event);
        
        if (nextState != null && !nextState.equals(currentState)) {
            Consumer<S> exitAction = exitActions.get(currentState);
            if (exitAction != null) {
                exitAction.accept(currentState);
            }
            
            BiConsumer<S, E> transitionAction = transitionActions.get(key);
            if (transitionAction != null) {
                transitionAction.accept(currentState, event);
            }
            
            System.out.println("Transition: " + currentState + " --[" + event + "]--> " + nextState);
            currentState = nextState;
            
            Consumer<S> entryAction = entryActions.get(currentState);
            if (entryAction != null) {
                entryAction.accept(currentState);
            }
            
            return true;
        }
        
        return false;
    }
    
    public S getCurrentState() {
        return currentState;
    }
    
    private String makeKey(S state, E event) {
        return state.toString() + ":" + event.toString();
    }
    
    public static <S, E> Builder<S, E> builder(S initialState) {
        return new Builder<>(initialState);
    }
    
    public static class Builder<S, E> {
        private final FunctionalStateMachine<S, E> machine;
        
        private Builder(S initialState) {
            this.machine = new FunctionalStateMachine<>(initialState);
        }
        
        public Builder<S, E> onEntry(S state, Consumer<S> action) {
            machine.onEntry(state, action);
            return this;
        }
        
        public Builder<S, E> onExit(S state, Consumer<S> action) {
            machine.onExit(state, action);
            return this;
        }
        
        public Builder<S, E> transition(S from, E event, S to) {
            machine.addTransition(from, event, to);
            return this;
        }
        
        public Builder<S, E> transition(S from, E event, S to, BiConsumer<S, E> action) {
            machine.addTransition(from, event, to, action);
            return this;
        }
        
        public Builder<S, E> conditionalTransition(S from, E event, BiFunction<S, E, S> condition) {
            machine.addConditionalTransition(from, event, condition);
            return this;
        }
        
        public FunctionalStateMachine<S, E> build() {
            return machine;
        }
    }
}