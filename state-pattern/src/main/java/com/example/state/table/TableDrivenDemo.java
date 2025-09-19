package com.example.state.table;

public class TableDrivenDemo {
    public static void main(String[] args) {
        System.out.println("=== Table-Driven State Machine Demo ===\n");
        
        TrafficLightMachine trafficLight = new TrafficLightMachine();
        
        System.out.println("Traffic Light State Machine initialized");
        trafficLight.printTransitionTable();
        
        System.out.println("Initial state: " + trafficLight.getCurrentState());
        
        System.out.println("\n--- Normal Operation Cycle ---");
        for (int cycle = 0; cycle < 3; cycle++) {
            System.out.println("\nCycle " + (cycle + 1));
            simulateTimer(trafficLight);
            simulateTimer(trafficLight);
            simulateTimer(trafficLight);
        }
        
        System.out.println("\n--- Emergency Scenario ---");
        System.out.println("Current state: " + trafficLight.getCurrentState());
        trafficLight.handleEvent("EMERGENCY");
        System.out.println("State during emergency: " + trafficLight.getCurrentState());
        trafficLight.handleEvent("RESET");
        System.out.println("State after reset: " + trafficLight.getCurrentState());
        
        System.out.println("\n--- Pedestrian Request ---");
        trafficLight.handleEvent("PEDESTRIAN_REQUEST");
        System.out.println("Pedestrian waiting: " + trafficLight.getContextValue("pedestrian_waiting"));
        
        System.out.println("\n--- Invalid Event Handling ---");
        trafficLight.handleEvent("INVALID_EVENT");
        
        System.out.println("\n--- Creating Custom Document Workflow Machine ---");
        StateMachine docWorkflow = createDocumentWorkflow();
        docWorkflow.printTransitionTable();
        
        System.out.println("Document workflow simulation:");
        docWorkflow.handleEvent("SUBMIT");
        docWorkflow.handleEvent("APPROVE");
        docWorkflow.handleEvent("PUBLISH");
        
        System.out.println("\nTrying to publish from draft (invalid):");
        StateMachine doc2 = createDocumentWorkflow();
        doc2.handleEvent("PUBLISH");
    }
    
    private static void simulateTimer(TrafficLightMachine machine) {
        machine.resetTimer(1);
        machine.tick();
    }
    
    private static StateMachine createDocumentWorkflow() {
        StateMachine workflow = new StateMachine("DRAFT");
        
        workflow.addTransition("DRAFT", "SUBMIT", "REVIEW", 
            sm -> System.out.println("Document submitted for review"));
        
        workflow.addTransition("DRAFT", "DELETE", "DELETED",
            sm -> System.out.println("Document deleted"));
        
        workflow.addTransition("REVIEW", "APPROVE", "APPROVED",
            sm -> System.out.println("Document approved"));
        
        workflow.addTransition("REVIEW", "REJECT", "DRAFT",
            sm -> System.out.println("Document rejected, back to draft"));
        
        workflow.addTransition("APPROVED", "PUBLISH", "PUBLISHED",
            sm -> System.out.println("Document published"));
        
        workflow.addTransition("PUBLISHED", "ARCHIVE", "ARCHIVED",
            sm -> System.out.println("Document archived"));
        
        workflow.addTransition("ARCHIVED", "RESTORE", "DRAFT",
            sm -> System.out.println("Document restored to draft"));
        
        return workflow;
    }
}