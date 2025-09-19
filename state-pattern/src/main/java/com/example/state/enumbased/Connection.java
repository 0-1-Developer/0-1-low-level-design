package com.example.state.enumbased;

import java.util.ArrayList;
import java.util.List;

public class Connection {
    private ConnectionState state;
    private String message;
    private String lastData;
    private int packetsSent;
    private int packetsReceived;
    private int reconnectAttempts;
    private List<String> bufferedData;
    
    public Connection() {
        this.state = ConnectionState.DISCONNECTED;
        this.bufferedData = new ArrayList<>();
        this.packetsSent = 0;
        this.packetsReceived = 0;
        this.reconnectAttempts = 0;
    }
    
    public void connect() {
        System.out.println("Current state: " + state);
        ConnectionState newState = state.connect(this);
        if (newState != state) {
            System.out.println("State transition: " + state + " -> " + newState);
            state = newState;
        }
    }
    
    public void disconnect() {
        System.out.println("Current state: " + state);
        ConnectionState newState = state.disconnect(this);
        if (newState != state) {
            System.out.println("State transition: " + state + " -> " + newState);
            state = newState;
        }
    }
    
    public void sendData(String data) {
        System.out.println("Current state: " + state);
        ConnectionState newState = state.sendData(this, data);
        if (newState != state) {
            System.out.println("State transition: " + state + " -> " + newState);
            state = newState;
        }
    }
    
    public void receiveData() {
        System.out.println("Current state: " + state);
        ConnectionState newState = state.receiveData(this);
        if (newState != state) {
            System.out.println("State transition: " + state + " -> " + newState);
            state = newState;
        }
    }
    
    public void timeout() {
        System.out.println("Current state: " + state);
        ConnectionState newState = state.timeout(this);
        if (newState != state) {
            System.out.println("State transition: " + state + " -> " + newState);
            state = newState;
        }
    }
    
    public void simulateConnectionSuccess() {
        if (state == ConnectionState.CONNECTING) {
            setMessage("ACK");
            receiveData();
        }
    }
    
    public ConnectionState getState() {
        return state;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getLastData() {
        return lastData;
    }
    
    public void setLastData(String lastData) {
        this.lastData = lastData;
    }
    
    public void incrementPacketsSent() {
        packetsSent++;
    }
    
    public void incrementPacketsReceived() {
        packetsReceived++;
    }
    
    public void incrementReconnectAttempts() {
        reconnectAttempts++;
    }
    
    public void resetReconnectAttempts() {
        reconnectAttempts = 0;
    }
    
    public int getReconnectAttempts() {
        return reconnectAttempts;
    }
    
    public void bufferData(String data) {
        bufferedData.add(data);
        System.out.println("Data buffered for later transmission: " + data);
    }
    
    public void showStatistics() {
        System.out.println("\n=== Connection Statistics ===");
        System.out.println("Current State: " + state);
        System.out.println("Message: " + message);
        System.out.println("Packets Sent: " + packetsSent);
        System.out.println("Packets Received: " + packetsReceived);
        System.out.println("Reconnect Attempts: " + reconnectAttempts);
        System.out.println("Buffered Data Items: " + bufferedData.size());
    }
}