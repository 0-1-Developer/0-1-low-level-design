package com.example.state.persistent;

import java.util.List;

public class PersistentStateDemo {
    public static void main(String[] args) {
        System.out.println("=== Distributed Persistent State Pattern Demo ===\n");
        
        StateStore stateStore = new StateStore("state-snapshots");
        PersistentWorkflowEngine engine = new PersistentWorkflowEngine(stateStore);
        
        System.out.println("--- Scenario 1: Creating and Executing Workflow ---");
        PersistentWorkflow orderWorkflow = engine.createWorkflow("ORDER-001", "CREATED");
        
        orderWorkflow.setContextValue("customerId", "CUST-123");
        orderWorkflow.setContextValue("amount", 299.99);
        orderWorkflow.setContextValue("items", 3);
        
        orderWorkflow.transitionTo("PAYMENT_PENDING");
        engine.saveCheckpoint("ORDER-001");
        
        orderWorkflow.setContextValue("paymentId", "PAY-456");
        orderWorkflow.transitionTo("PAID");
        engine.saveCheckpoint("ORDER-001");
        
        orderWorkflow.transitionTo("PROCESSING");
        orderWorkflow.setContextValue("warehouseId", "WH-001");
        engine.saveCheckpoint("ORDER-001");
        
        orderWorkflow.transitionTo("SHIPPED");
        orderWorkflow.setContextValue("trackingNumber", "TRK-789");
        engine.saveCheckpoint("ORDER-001");
        
        orderWorkflow.printStatus();
        
        System.out.println("\n--- Scenario 2: Simulating System Crash and Recovery ---");
        System.out.println("Simulating system crash...");
        engine.pauseWorkflow("ORDER-001");
        
        System.out.println("\nSystem restarted. Recovering workflow...");
        PersistentWorkflow recoveredWorkflow = engine.recoverWorkflow("ORDER-001");
        if (recoveredWorkflow != null) {
            recoveredWorkflow.printStatus();
            
            recoveredWorkflow.transitionTo("DELIVERED");
            recoveredWorkflow.setContextValue("deliveryDate", "2024-01-15");
            engine.saveCheckpoint("ORDER-001");
        }
        
        System.out.println("\n--- Scenario 3: Rollback to Previous Version ---");
        System.out.println("Rolling back to processing state...");
        engine.rollback("ORDER-001", 3);
        
        recoveredWorkflow = engine.recoverWorkflow("ORDER-001");
        if (recoveredWorkflow != null) {
            recoveredWorkflow.printStatus();
        }
        
        System.out.println("\n--- Scenario 4: Multiple Concurrent Workflows ---");
        PersistentWorkflow workflow2 = engine.createWorkflow("ORDER-002", "CREATED");
        workflow2.setContextValue("customerId", "CUST-456");
        workflow2.setContextValue("amount", 149.99);
        workflow2.transitionTo("PAYMENT_PENDING");
        engine.saveCheckpoint("ORDER-002");
        
        PersistentWorkflow workflow3 = engine.createWorkflow("ORDER-003", "CREATED");
        workflow3.setContextValue("customerId", "CUST-789");
        workflow3.setContextValue("amount", 99.99);
        workflow3.transitionTo("PAID");
        workflow3.transitionTo("PROCESSING");
        engine.saveCheckpoint("ORDER-003");
        
        System.out.println("\n--- Scenario 5: Workflow History Analysis ---");
        List<StateSnapshot> history = engine.getWorkflowHistory("ORDER-001");
        System.out.println("ORDER-001 History (" + history.size() + " versions):");
        for (StateSnapshot snapshot : history) {
            System.out.println("  " + snapshot);
            if (!snapshot.validate()) {
                System.err.println("  WARNING: Corrupted snapshot detected!");
            }
        }
        
        System.out.println("\n--- Scenario 6: Cleanup Old Versions ---");
        System.out.println("Cleaning up old versions (keeping last 2)...");
        engine.cleanupOldVersions("ORDER-001", 2);
        
        System.out.println("\nAfter cleanup:");
        history = engine.getWorkflowHistory("ORDER-001");
        System.out.println("ORDER-001 History (" + history.size() + " versions):");
        for (StateSnapshot snapshot : history) {
            System.out.println("  " + snapshot);
        }
        
        System.out.println("\n--- Scenario 7: Long-Running Workflow Simulation ---");
        PersistentWorkflow longWorkflow = engine.createWorkflow("APPROVAL-001", "SUBMITTED");
        
        for (int i = 0; i < 5; i++) {
            longWorkflow.setContextValue("step", i + 1);
            longWorkflow.setContextValue("approver", "MANAGER-" + (i + 1));
            
            switch (i) {
                case 0:
                    longWorkflow.transitionTo("LEVEL1_REVIEW");
                    break;
                case 1:
                    longWorkflow.transitionTo("LEVEL2_REVIEW");
                    break;
                case 2:
                    longWorkflow.transitionTo("FINAL_REVIEW");
                    break;
                case 3:
                    longWorkflow.transitionTo("APPROVED");
                    break;
                case 4:
                    longWorkflow.transitionTo("COMPLETED");
                    break;
            }
            
            engine.saveCheckpoint("APPROVAL-001");
            
            if (i == 2) {
                System.out.println("Pausing long-running workflow at step " + (i + 1));
                engine.pauseWorkflow("APPROVAL-001");
                
                System.out.println("Recovering workflow after pause...");
                longWorkflow = engine.recoverWorkflow("APPROVAL-001");
            }
        }
        
        if (longWorkflow != null) {
            longWorkflow.printStatus();
        }
        
        System.out.println("\n--- Benefits of Persistent State Pattern ---");
        System.out.println("+ Fault tolerance and crash recovery");
        System.out.println("+ Long-running workflow support");
        System.out.println("+ Audit trail and versioning");
        System.out.println("+ Rollback capabilities");
        System.out.println("+ Distributed system state consistency");
        
        System.out.println("\n--- Use Cases ---");
        System.out.println("- Order processing systems");
        System.out.println("- Approval workflows");
        System.out.println("- Batch processing jobs");
        System.out.println("- Distributed transactions");
        System.out.println("- Saga pattern implementation");
        System.out.println("- Workflow orchestration engines");
    }
}