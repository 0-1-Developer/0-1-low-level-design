package com.example.state.table;

import java.util.function.Consumer;

public class StateTransition {
    private final String fromState;
    private final String event;
    private final String toState;
    private final Consumer<StateMachine> action;

    public StateTransition(String fromState, String event, String toState, Consumer<StateMachine> action) {
        this.fromState = fromState;
        this.event = event;
        this.toState = toState;
        this.action = action;
    }

    public String getFromState() {
        return fromState;
    }

    public String getEvent() {
        return event;
    }

    public String getToState() {
        return toState;
    }

    public Consumer<StateMachine> getAction() {
        return action;
    }

    public String getKey() {
        return fromState + ":" + event;
    }
}