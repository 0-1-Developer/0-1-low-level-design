package com.example.state.tests;

import com.example.state.classic.*;
import com.example.state.table.*;
import com.example.state.enumbased.*;
import com.example.state.switchbased.*;
import com.example.state.dynamic.*;
import com.example.state.functional.*;
import com.example.state.hierarchical.*;
import com.example.state.reactive.*;
import com.example.state.persistent.*;

public class StatePatternTestSuite {
    private static int totalTests = 0;
    private static int passedTests = 0;
    
    public static void main(String[] args) {
        System.out.println("=== State Pattern Test Suite ===\n");
        
        testClassicStatePattern();
        testTableDrivenStateMachine();
        testEnumBasedState();
        testSwitchBasedState();
        testDynamicRegistryState();
        testFunctionalState();
        testHierarchicalState();
        testReactiveState();
        testPersistentState();
        
        System.out.println("\n=== Test Summary ===");
        System.out.println("Total Tests: " + totalTests);
        System.out.println("Passed: " + passedTests);
        System.out.println("Failed: " + (totalTests - passedTests));
        System.out.println("Success Rate: " + (passedTests * 100 / totalTests) + "%");
        
        if (passedTests == totalTests) {
            System.out.println("\n*** ALL TESTS PASSED! ***");
        } else {
            System.out.println("\nSome tests failed. Check output above.");
        }
    }
    
    private static void testClassicStatePattern() {
        System.out.println("--- Testing Classic GoF State Pattern ---");
        
        VendingMachine machine = new VendingMachine();
        
        // Test initial state
        test("Initial state should be IDLE", 
            "IDLE".equals(machine.getState().getName()));
        
        // Test normal purchase flow
        machine.handle("INSERT_COIN");
        machine.handle("INSERT_COIN");
        test("Should transition to SELECTING_PRODUCT after sufficient money",
            "SELECTING_PRODUCT".equals(machine.getState().getName()));
        
        machine.handle("SELECT_PRODUCT");
        test("Should complete transaction and return to IDLE after product selection",
            "IDLE".equals(machine.getState().getName()));
        
        // Test cancel functionality
        machine.handle("INSERT_COIN");
        machine.handle("CANCEL");
        test("Should return to IDLE after cancel",
            "IDLE".equals(machine.getState().getName()));
        
        // Test balance check
        test("Balance should be 0 after cancel",
            machine.getBalance() == 0);
    }
    
    private static void testTableDrivenStateMachine() {
        System.out.println("\n--- Testing Table-Driven State Machine ---");
        
        TrafficLightMachine trafficLight = new TrafficLightMachine();
        
        test("Initial state should be RED",
            "RED".equals(trafficLight.getCurrentState()));
        
        // Test normal cycle
        trafficLight.handleEvent("TIMER_EXPIRED");
        test("Should transition from RED to GREEN",
            "GREEN".equals(trafficLight.getCurrentState()));
        
        trafficLight.handleEvent("TIMER_EXPIRED");
        test("Should transition from GREEN to YELLOW",
            "YELLOW".equals(trafficLight.getCurrentState()));
        
        trafficLight.handleEvent("TIMER_EXPIRED");
        test("Should transition from YELLOW to RED",
            "RED".equals(trafficLight.getCurrentState()));
        
        // Test emergency mode
        trafficLight.handleEvent("EMERGENCY");
        test("Should transition to FLASHING_RED on emergency",
            "FLASHING_RED".equals(trafficLight.getCurrentState()));
        
        trafficLight.handleEvent("RESET");
        test("Should return to RED after emergency reset",
            "RED".equals(trafficLight.getCurrentState()));
    }
    
    private static void testEnumBasedState() {
        System.out.println("\n--- Testing Enum-Based State Pattern ---");
        
        Connection connection = new Connection();
        
        test("Initial state should be DISCONNECTED",
            ConnectionState.DISCONNECTED == connection.getState());
        
        // Test connection flow
        connection.connect();
        test("Should transition to CONNECTING",
            ConnectionState.CONNECTING == connection.getState());
        
        connection.simulateConnectionSuccess();
        test("Should transition to CONNECTED",
            ConnectionState.CONNECTED == connection.getState());
        
        // Test data transmission
        connection.sendData("test data");
        test("Should remain CONNECTED after sending data",
            ConnectionState.CONNECTED == connection.getState());
        
        // Test timeout and reconnection
        connection.timeout();
        test("Should transition to RECONNECTING on timeout",
            ConnectionState.RECONNECTING == connection.getState());
        
        connection.disconnect();
        test("Should transition to DISCONNECTED",
            ConnectionState.DISCONNECTED == connection.getState());
    }
    
    private static void testSwitchBasedState() {
        System.out.println("\n--- Testing Switch-Based State Pattern ---");
        
        SimpleStateMachine machine = new SimpleStateMachine();
        
        test("Initial state should be IDLE",
            SimpleStateMachine.STATE_IDLE.equals(machine.getState()));
        
        // Test start/stop cycle
        machine.handleEvent("START");
        test("Should transition to RUNNING",
            SimpleStateMachine.STATE_RUNNING.equals(machine.getState()));
        
        machine.handleEvent("PROCESS_ITEM");
        test("Should process items in RUNNING state",
            machine.getProcessedItems() > 0);
        
        machine.handleEvent("PAUSE");
        test("Should transition to PAUSED",
            SimpleStateMachine.STATE_PAUSED.equals(machine.getState()));
        
        machine.handleEvent("RESUME");
        test("Should transition back to RUNNING",
            SimpleStateMachine.STATE_RUNNING.equals(machine.getState()));
        
        machine.handleEvent("STOP");
        test("Should transition to STOPPED",
            SimpleStateMachine.STATE_STOPPED.equals(machine.getState()));
        
        machine.handleEvent("RESET");
        test("Should transition back to IDLE",
            SimpleStateMachine.STATE_IDLE.equals(machine.getState()));
    }
    
    private static void testDynamicRegistryState() {
        System.out.println("\n--- Testing Dynamic Registry State Pattern ---");
        
        StateRegistry registry = new StateRegistry();
        
        // Create a test state with the expected name
        DynamicState testState = new DynamicState() {
            @Override
            public void enter(DynamicContext context) {
                System.out.println("Entering TEST_STATE");
            }
            
            @Override
            public void execute(DynamicContext context, String event) {
                System.out.println("Handling event in TEST_STATE: " + event);
            }
            
            @Override
            public void exit(DynamicContext context) {
                System.out.println("Exiting TEST_STATE");
            }
            
            @Override
            public String getName() {
                return "TEST_STATE";
            }
        };
        
        registry.registerState("TEST_STATE", testState);
        
        test("Registry should contain registered state",
            registry.hasState("TEST_STATE"));
        
        DynamicContext context = new DynamicContext(registry);
        context.initialize("TEST_STATE");
        
        test("Context should be in TEST_STATE",
            "TEST_STATE".equals(context.getCurrentState().getName()));
        
        // Test alias functionality
        registry.registerAlias("MAIN", "TEST_STATE");
        test("Registry should recognize alias",
            registry.hasState("MAIN"));
        
        // Test state replacement
        registry.registerState("NEW_STATE", new GameStates.PlayingState());
        context.transitionTo("NEW_STATE");
        test("Should transition to new state",
            "PLAYING".equals(context.getCurrentState().getName())); // PlayingState returns "PLAYING"
    }
    
    private static void testFunctionalState() {
        System.out.println("\n--- Testing Functional State Pattern ---");
        
        FunctionalStateMachine<String, String> machine = 
            FunctionalStateMachine.<String, String>builder("OFF")
                .transition("OFF", "POWER", "ON")
                .transition("ON", "POWER", "OFF")
                .build();
        
        test("Initial state should be OFF",
            "OFF".equals(machine.getCurrentState()));
        
        boolean transitioned = machine.handleEvent("POWER");
        test("Should handle POWER event successfully", transitioned);
        test("Should transition to ON",
            "ON".equals(machine.getCurrentState()));
        
        transitioned = machine.handleEvent("POWER");
        test("Should transition back to OFF",
            "OFF".equals(machine.getCurrentState()) && transitioned);
        
        transitioned = machine.handleEvent("INVALID");
        test("Should reject invalid events", !transitioned);
    }
    
    private static void testHierarchicalState() {
        System.out.println("\n--- Testing Hierarchical State Pattern ---");
        
        HierarchicalContext context = new HierarchicalContext();
        HierarchicalState phoneHierarchy = PhoneStates.buildPhoneHierarchy();
        context.setRootState(phoneHierarchy);
        context.start();
        
        // Should start in PHONE.OFF state
        test("Should start in nested OFF state",
            context.getCurrentPath().contains("OFF"));
        
        context.handleEvent("POWER_ON");
        test("Should transition to ON hierarchy",
            context.getCurrentPath().contains("ON"));
        
        context.handleEvent("UNLOCK");
        test("Should transition to UNLOCKED state",
            context.getCurrentPath().contains("UNLOCKED"));
        
        context.handleEvent("OPEN_APP");
        test("Should transition to APP_RUNNING",
            context.getCurrentPath().contains("APP_RUNNING"));
        
        // Test hierarchical event bubbling
        context.handleEvent("POWER_OFF");
        test("Should handle power off from any nested state",
            context.getCurrentPath().contains("OFF"));
    }
    
    private static void testReactiveState() {
        System.out.println("\n--- Testing Reactive Event-Driven State Pattern ---");
        
        EventBus eventBus = new EventBus();
        ReactiveContext iotDevice = new ReactiveContext(eventBus);
        
        iotDevice.addState(new IoTDeviceStates.DisconnectedState(eventBus));
        iotDevice.addState(new IoTDeviceStates.ConnectingState(eventBus));
        iotDevice.addState(new IoTDeviceStates.ConnectedState(eventBus));
        
        iotDevice.initialize("DISCONNECTED");
        test("Should initialize to DISCONNECTED",
            "DISCONNECTED".equals(iotDevice.getCurrentState().getName()));
        
        // Test event publishing
        Event wifiEvent = new Event("WIFI_AVAILABLE", "network");
        iotDevice.handleEvent(wifiEvent);
        
        // Give event bus time to process
        try { Thread.sleep(100); } catch (InterruptedException e) {}
        
        test("Should transition to CONNECTING on WiFi event",
            "CONNECTING".equals(iotDevice.getCurrentState().getName()));
        
        eventBus.stop();
    }
    
    private static void testPersistentState() {
        System.out.println("\n--- Testing Persistent State Pattern ---");
        
        StateStore stateStore = new StateStore("test-state-store");
        PersistentWorkflowEngine engine = new PersistentWorkflowEngine(stateStore);
        
        // Test workflow creation
        PersistentWorkflow workflow = engine.createWorkflow("TEST-001", "CREATED");
        test("Should create workflow",
            workflow != null && "TEST-001".equals(workflow.getWorkflowId()));
        
        // Test state transition and checkpoint
        workflow.transitionTo("PROCESSING");
        workflow.setContextValue("test_key", "test_value");
        engine.saveCheckpoint("TEST-001");
        
        // Test recovery
        PersistentWorkflow recovered = engine.recoverWorkflow("TEST-001");
        test("Should recover workflow",
            recovered != null && "PROCESSING".equals(recovered.getCurrentState()));
        
        test("Should recover context data",
            "test_value".equals(recovered.getContextValue("test_key")));
        
        // Test history
        var history = engine.getWorkflowHistory("TEST-001");
        test("Should have workflow history",
            history.size() >= 2);
        
        // Test snapshot validation
        boolean allValid = history.stream().allMatch(StateSnapshot::validate);
        test("All snapshots should be valid", allValid);
    }
    
    private static void test(String description, boolean condition) {
        totalTests++;
        if (condition) {
            passedTests++;
            System.out.println("[PASS] " + description);
        } else {
            System.out.println("[FAIL] " + description);
        }
    }
}