package com.example.state.classic;

public interface State {
    void handle(VendingMachine context, String event);
    String getName();
}