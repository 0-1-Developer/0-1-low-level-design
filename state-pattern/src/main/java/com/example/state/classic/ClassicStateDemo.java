package com.example.state.classic;

public class ClassicStateDemo {
    public static void main(String[] args) {
        System.out.println("=== Classic GoF State Pattern Demo ===\n");
        
        VendingMachine machine = new VendingMachine();
        
        System.out.println("Initial state:");
        machine.showStatus();
        
        System.out.println("\n--- Scenario 1: Normal Purchase ---");
        machine.handle("INSERT_COIN");
        machine.handle("INSERT_COIN");
        machine.handle("SELECT_PRODUCT");
        
        System.out.println("\n--- Scenario 2: Insufficient Funds ---");
        machine.handle("INSERT_COIN");
        machine.handle("SELECT_PRODUCT");
        machine.handle("INSERT_COIN");
        machine.handle("SELECT_PRODUCT");
        
        System.out.println("\n--- Scenario 3: Cancel Transaction ---");
        machine.handle("INSERT_COIN");
        machine.handle("CANCEL");
        
        System.out.println("\n--- Scenario 4: Buy until out of stock ---");
        for (int i = 0; i < 4; i++) {
            machine.handle("INSERT_COIN");
            machine.handle("INSERT_COIN");
            machine.handle("SELECT_PRODUCT");
        }
        
        System.out.println("\n--- Scenario 5: Try to buy when out of stock ---");
        machine.handle("INSERT_COIN");
        machine.handle("SELECT_PRODUCT");
        
        System.out.println("\n--- Scenario 6: Maintenance Mode ---");
        machine.handle("MAINTENANCE");
        machine.handle("INSERT_COIN");
        machine.handle("MAINTENANCE");
        
        System.out.println("\nFinal state:");
        machine.showStatus();
    }
}