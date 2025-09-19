package com.example.state.enumbased;

public class EnumStateDemo {
    public static void main(String[] args) {
        System.out.println("=== Enum-Based State Pattern Demo ===\n");
        
        Connection connection = new Connection();
        
        System.out.println("--- Scenario 1: Normal Connection Flow ---");
        connection.connect();
        connection.simulateConnectionSuccess();
        connection.sendData("Hello Server");
        connection.sendData("More data");
        connection.receiveData();
        connection.showStatistics();
        connection.disconnect();
        
        System.out.println("\n--- Scenario 2: Connection Timeout and Reconnection ---");
        connection.connect();
        connection.simulateConnectionSuccess();
        connection.sendData("Important data");
        connection.timeout();
        connection.sendData("Buffered data while reconnecting");
        connection.connect();
        connection.simulateConnectionSuccess();
        connection.showStatistics();
        
        System.out.println("\n--- Scenario 3: Failed Reconnection (Error State) ---");
        connection.timeout();
        for (int i = 0; i < 5; i++) {
            System.out.println("\nReconnection attempt " + (i + 1));
            connection.connect();
        }
        connection.sendData("This won't work");
        connection.disconnect();
        
        System.out.println("\n--- Scenario 4: Invalid Operations ---");
        connection.connect();
        connection.sendData("Can't send when not connected");
        connection.receiveData();
        connection.connect();
        connection.timeout();
        
        System.out.println("\n--- Scenario 5: Cancel During Connection ---");
        connection.connect();
        connection.disconnect();
        
        System.out.println("\nFinal Statistics:");
        connection.showStatistics();
        
        System.out.println("\n--- Demonstrating All State Transitions ---");
        demonstrateAllStates();
    }
    
    private static void demonstrateAllStates() {
        System.out.println("\nAvailable states in ConnectionState enum:");
        for (ConnectionState state : ConnectionState.values()) {
            System.out.println("- " + state.name());
        }
        
        System.out.println("\nState method capabilities:");
        System.out.println("Each state implements:");
        System.out.println("- connect()");
        System.out.println("- disconnect()");
        System.out.println("- sendData()");
        System.out.println("- receiveData()");
        System.out.println("- timeout()");
    }
}