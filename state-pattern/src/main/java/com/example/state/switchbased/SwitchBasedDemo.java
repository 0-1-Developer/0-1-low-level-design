package com.example.state.switchbased;

public class SwitchBasedDemo {
    public static void main(String[] args) {
        System.out.println("=== Switch-Based State Pattern Demo ===\n");
        
        System.out.println("--- Part 1: Simple State Machine ---");
        demoSimpleStateMachine();
        
        System.out.println("\n\n--- Part 2: ATM Machine (Integer States) ---");
        demoATMMachine();
        
        System.out.println("\n--- Comparison with OO Approach ---");
        compareApproaches();
    }
    
    private static void demoSimpleStateMachine() {
        SimpleStateMachine machine = new SimpleStateMachine();
        
        System.out.println("Initial state:");
        machine.printStatus();
        
        System.out.println("\n-- Normal workflow --");
        machine.handleEvent("START");
        machine.handleEvent("PROCESS_ITEM");
        machine.handleEvent("PROCESS_ITEM");
        machine.handleEvent("PAUSE");
        machine.handleEvent("RESUME");
        machine.handleEvent("PROCESS_ITEM");
        machine.handleEvent("STOP");
        machine.handleEvent("RESET");
        
        System.out.println("\n-- Error scenario --");
        machine.handleEvent("START");
        machine.handleEvent("PROCESS_ITEM");
        machine.handleEvent("ERROR");
        machine.handleEvent("START");
        machine.handleEvent("RESET");
        machine.handleEvent("START");
        
        System.out.println("\n-- Invalid transitions --");
        machine.handleEvent("PAUSE");
        machine.handleEvent("STOP");
        machine.handleEvent("PAUSE");
        
        System.out.println("\nFinal status:");
        machine.printStatus();
    }
    
    private static void demoATMMachine() {
        ATMMachine atm = new ATMMachine();
        
        System.out.println("-- Scenario 1: Successful transaction --");
        atm.insertCard("1234567890123456");
        atm.enterPin("1234");
        atm.selectTransaction("BALANCE");
        atm.selectTransaction("WITHDRAW");
        atm.selectTransaction("DEPOSIT");
        atm.selectTransaction("EXIT");
        
        System.out.println("\n-- Scenario 2: Wrong PIN --");
        atm.insertCard("9876543210987654");
        atm.enterPin("0000");
        atm.enterPin("1111");
        atm.enterPin("1234");
        atm.selectTransaction("BALANCE");
        atm.selectTransaction("EXIT");
        
        System.out.println("\n-- Scenario 3: Card blocked --");
        atm.insertCard("1111222233334444");
        atm.enterPin("9999");
        atm.enterPin("8888");
        atm.enterPin("7777");
        atm.selectTransaction("BALANCE");
        atm.ejectCard();
        
        System.out.println("\n-- Scenario 4: Invalid operations --");
        atm.enterPin("1234");
        atm.selectTransaction("BALANCE");
        atm.insertCard("5555666677778888");
        atm.selectTransaction("WITHDRAW");
        atm.ejectCard();
        
        atm.printStatus();
    }
    
    private static void compareApproaches() {
        System.out.println("Switch-Based vs Object-Oriented State Pattern:\n");
        
        System.out.println("SWITCH-BASED APPROACH:");
        System.out.println("+ Simple and direct for small state machines");
        System.out.println("+ All logic in one place");
        System.out.println("+ No class proliferation");
        System.out.println("+ Minimal memory overhead");
        System.out.println("- Violates Open/Closed Principle");
        System.out.println("- Large switch statements become unwieldy");
        System.out.println("- Adding states requires modifying existing code");
        System.out.println("- Harder to test individual states");
        
        System.out.println("\nOBJECT-ORIENTED APPROACH:");
        System.out.println("+ Follows Open/Closed Principle");
        System.out.println("+ Each state is encapsulated");
        System.out.println("+ Easy to add new states");
        System.out.println("+ States can be tested independently");
        System.out.println("- More complex for simple cases");
        System.out.println("- Class proliferation");
        System.out.println("- Higher memory overhead");
        System.out.println("- Logic scattered across multiple files");
        
        System.out.println("\nRECOMMENDATION:");
        System.out.println("Use switch-based for:");
        System.out.println("- Simple state machines (< 5 states)");
        System.out.println("- Performance-critical code");
        System.out.println("- Embedded systems with memory constraints");
        System.out.println("\nUse OO approach for:");
        System.out.println("- Complex state machines");
        System.out.println("- Systems that change frequently");
        System.out.println("- When states have complex behavior");
        System.out.println("- When testability is important");
    }
}