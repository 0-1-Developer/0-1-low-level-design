package com.example.state.dynamic;

public interface DynamicState {
    void enter(DynamicContext context);
    void execute(DynamicContext context, String event);
    void exit(DynamicContext context);
    String getName();
}