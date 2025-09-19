package com.example.state.switchbased;

public class SimpleStateMachine {
    private String state;
    
    public static final String STATE_IDLE = "IDLE";
    public static final String STATE_RUNNING = "RUNNING";
    public static final String STATE_PAUSED = "PAUSED";
    public static final String STATE_STOPPED = "STOPPED";
    
    private int processedItems = 0;
    private boolean hasError = false;
    
    public SimpleStateMachine() {
        this.state = STATE_IDLE;
    }
    
    public void handleEvent(String event) {
        System.out.println("\nEvent: " + event + " (Current state: " + state + ")");
        
        switch (state) {
            case STATE_IDLE:
                handleIdleState(event);
                break;
            case STATE_RUNNING:
                handleRunningState(event);
                break;
            case STATE_PAUSED:
                handlePausedState(event);
                break;
            case STATE_STOPPED:
                handleStoppedState(event);
                break;
            default:
                System.out.println("Unknown state: " + state);
        }
    }
    
    private void handleIdleState(String event) {
        switch (event) {
            case "START":
                setState(STATE_RUNNING);
                System.out.println("Process started");
                processedItems = 0;
                break;
            case "STOP":
                System.out.println("Already idle");
                break;
            default:
                System.out.println("Invalid event '" + event + "' in IDLE state");
        }
    }
    
    private void handleRunningState(String event) {
        switch (event) {
            case "PAUSE":
                setState(STATE_PAUSED);
                System.out.println("Process paused at item " + processedItems);
                break;
            case "STOP":
                setState(STATE_STOPPED);
                System.out.println("Process stopped after processing " + processedItems + " items");
                break;
            case "PROCESS_ITEM":
                processedItems++;
                System.out.println("Processed item #" + processedItems);
                break;
            case "ERROR":
                hasError = true;
                setState(STATE_STOPPED);
                System.out.println("Error occurred! Process stopped");
                break;
            default:
                System.out.println("Invalid event '" + event + "' in RUNNING state");
        }
    }
    
    private void handlePausedState(String event) {
        switch (event) {
            case "RESUME":
                setState(STATE_RUNNING);
                System.out.println("Process resumed from item " + processedItems);
                break;
            case "STOP":
                setState(STATE_STOPPED);
                System.out.println("Process stopped from paused state");
                break;
            default:
                System.out.println("Invalid event '" + event + "' in PAUSED state");
        }
    }
    
    private void handleStoppedState(String event) {
        switch (event) {
            case "RESET":
                setState(STATE_IDLE);
                processedItems = 0;
                hasError = false;
                System.out.println("Process reset to idle");
                break;
            case "START":
                System.out.println("Cannot start from stopped state - please reset first");
                break;
            default:
                System.out.println("Invalid event '" + event + "' in STOPPED state");
        }
    }
    
    private void setState(String newState) {
        if (!state.equals(newState)) {
            System.out.println("State transition: " + state + " -> " + newState);
            state = newState;
        }
    }
    
    public String getState() {
        return state;
    }
    
    public int getProcessedItems() {
        return processedItems;
    }
    
    public boolean hasError() {
        return hasError;
    }
    
    public void printStatus() {
        System.out.println("\n=== Machine Status ===");
        System.out.println("Current State: " + state);
        System.out.println("Processed Items: " + processedItems);
        System.out.println("Has Error: " + hasError);
    }
}