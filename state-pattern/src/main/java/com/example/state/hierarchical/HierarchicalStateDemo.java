package com.example.state.hierarchical;

public class HierarchicalStateDemo {
    public static void main(String[] args) {
        System.out.println("=== Hierarchical State Pattern Demo ===\n");
        
        HierarchicalContext phoneContext = new HierarchicalContext();
        HierarchicalState phoneHierarchy = PhoneStates.buildPhoneHierarchy();
        
        phoneContext.setRootState(phoneHierarchy);
        phoneContext.printStateHierarchy();
        
        System.out.println("\n--- Starting phone system ---");
        phoneContext.start();
        
        System.out.println("\n--- Scenario 1: Basic phone operations ---");
        phoneContext.handleEvent("POWER_ON");
        phoneContext.handleEvent("WAKE");
        phoneContext.handleEvent("UNLOCK");
        phoneContext.handleEvent("OPEN_APP");
        phoneContext.handleEvent("MINIMIZE");
        phoneContext.handleEvent("RESTORE");
        phoneContext.handleEvent("HOME");
        phoneContext.handleEvent("LOCK");
        phoneContext.handleEvent("POWER_OFF");
        
        System.out.println("\n--- Scenario 2: Phone call flow ---");
        phoneContext.handleEvent("POWER_ON");
        phoneContext.handleEvent("WAKE");
        phoneContext.handleEvent("UNLOCK");
        phoneContext.handleEvent("INCOMING_CALL");
        phoneContext.handleEvent("ANSWER");
        phoneContext.handleEvent("HOLD");
        phoneContext.handleEvent("RESUME");
        phoneContext.handleEvent("END_CALL");
        
        System.out.println("\n--- Scenario 3: Locked phone with emergency ---");
        phoneContext.handleEvent("LOCK");
        phoneContext.handleEvent("TIMEOUT");
        phoneContext.handleEvent("EMERGENCY_CALL");
        phoneContext.handleEvent("WAKE");
        phoneContext.handleEvent("UNLOCK");
        
        System.out.println("\n--- Scenario 4: App interrupted by call ---");
        phoneContext.handleEvent("OPEN_APP");
        phoneContext.handleEvent("INCOMING_CALL");
        phoneContext.handleEvent("REJECT");
        
        System.out.println("\n--- Scenario 5: Invalid events demonstration ---");
        phoneContext.handleEvent("POWER_OFF");
        phoneContext.handleEvent("UNLOCK");
        phoneContext.handleEvent("POWER_ON");
        phoneContext.handleEvent("OPEN_APP");
        
        System.out.println("\nFinal state hierarchy:");
        phoneContext.printStateHierarchy();
        
        System.out.println("\n--- Benefits of Hierarchical States ---");
        System.out.println("1. Shared behavior in parent states");
        System.out.println("2. Natural modeling of nested contexts");
        System.out.println("3. Reduced complexity through state grouping");
        System.out.println("4. Event bubbling from child to parent");
        System.out.println("5. Default child state transitions");
        
        System.out.println("\n--- Use Cases ---");
        System.out.println("- UI navigation (screens, modals, tabs)");
        System.out.println("- Device states (power modes, operational modes)");
        System.out.println("- Game states (menus, gameplay, pause)");
        System.out.println("- Protocol states (connection, authentication, data transfer)");
    }
}