package com.example.state.shared;

public interface StateContext {
    void setState(Object state);
    Object getState();
    String getStateName();
    void handle(String event);
}