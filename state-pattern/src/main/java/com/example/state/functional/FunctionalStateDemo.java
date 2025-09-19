package com.example.state.functional;

public class FunctionalStateDemo {
    public static void main(String[] args) {
        System.out.println("=== Functional State Pattern Demo ===\n");
        
        System.out.println("--- Part 1: Order Processing State Machine ---");
        demoOrderProcessing();
        
        System.out.println("\n\n--- Part 2: Simple String-based State Machine ---");
        demoSimpleMachine();
        
        System.out.println("\n\n--- Part 3: Lambda Composition Demo ---");
        demoLambdaComposition();
    }
    
    private static void demoOrderProcessing() {
        OrderStateMachine.OrderContext order1 = 
            new OrderStateMachine.OrderContext("ORD-001", 299.99);
        
        FunctionalStateMachine<OrderStateMachine.OrderState, OrderStateMachine.OrderEvent> orderMachine = 
            OrderStateMachine.createOrderMachine(order1);
        
        System.out.println("\n-- Successful order flow --");
        orderMachine.handleEvent(OrderStateMachine.OrderEvent.PAY);
        orderMachine.handleEvent(OrderStateMachine.OrderEvent.CONFIRM);
        orderMachine.handleEvent(OrderStateMachine.OrderEvent.SHIP);
        orderMachine.handleEvent(OrderStateMachine.OrderEvent.SHIP);
        orderMachine.handleEvent(OrderStateMachine.OrderEvent.DELIVER);
        
        order1.printHistory();
        
        System.out.println("\n-- Cancelled order flow --");
        OrderStateMachine.OrderContext order2 = 
            new OrderStateMachine.OrderContext("ORD-002", 149.99);
        
        FunctionalStateMachine<OrderStateMachine.OrderState, OrderStateMachine.OrderEvent> orderMachine2 = 
            OrderStateMachine.createOrderMachine(order2);
        
        orderMachine2.handleEvent(OrderStateMachine.OrderEvent.PAY);
        orderMachine2.handleEvent(OrderStateMachine.OrderEvent.CANCEL);
        orderMachine2.handleEvent(OrderStateMachine.OrderEvent.REFUND);
        
        order2.printHistory();
        
        System.out.println("\n-- Return after delivery --");
        OrderStateMachine.OrderContext order3 = 
            new OrderStateMachine.OrderContext("ORD-003", 89.99);
        
        FunctionalStateMachine<OrderStateMachine.OrderState, OrderStateMachine.OrderEvent> orderMachine3 = 
            OrderStateMachine.createOrderMachine(order3);
        
        orderMachine3.handleEvent(OrderStateMachine.OrderEvent.PAY);
        orderMachine3.handleEvent(OrderStateMachine.OrderEvent.CONFIRM);
        
        for (int i = 0; i < 5; i++) {
            orderMachine3.handleEvent(OrderStateMachine.OrderEvent.SHIP);
            if (orderMachine3.getCurrentState() == OrderStateMachine.OrderState.SHIPPED) {
                break;
            }
        }
        
        if (orderMachine3.getCurrentState() == OrderStateMachine.OrderState.SHIPPED) {
            orderMachine3.handleEvent(OrderStateMachine.OrderEvent.DELIVER);
            orderMachine3.handleEvent(OrderStateMachine.OrderEvent.RETURN);
        }
        
        order3.printHistory();
    }
    
    private static void demoSimpleMachine() {
        FunctionalStateMachine<String, String> machine = 
            OrderStateMachine.createSimpleMachine();
        
        System.out.println("Initial state: " + machine.getCurrentState());
        
        machine.handleEvent("POWER");
        machine.handleEvent("SLEEP");
        machine.handleEvent("WAKE");
        machine.handleEvent("POWER");
        machine.handleEvent("POWER");
        machine.handleEvent("INVALID");
    }
    
    private static void demoLambdaComposition() {
        System.out.println("Creating state machine with composed lambdas:");
        
        class Counter {
            int value = 0;
        }
        
        Counter counter = new Counter();
        
        FunctionalStateMachine<Integer, String> countMachine = 
            FunctionalStateMachine.<Integer, String>builder(0)
                .onEntry(0, s -> System.out.println("Counter at ZERO"))
                .onEntry(1, s -> System.out.println("Counter at ONE"))
                .onEntry(2, s -> System.out.println("Counter at TWO"))
                .onEntry(3, s -> System.out.println("Counter at THREE"))
                
                .conditionalTransition(0, "INCREMENT", (s, e) -> {
                    counter.value++;
                    return Math.min(counter.value, 3);
                })
                
                .conditionalTransition(1, "INCREMENT", (s, e) -> {
                    counter.value++;
                    return Math.min(counter.value, 3);
                })
                
                .conditionalTransition(2, "INCREMENT", (s, e) -> {
                    counter.value++;
                    return Math.min(counter.value, 3);
                })
                
                .transition(3, "INCREMENT", 3,
                    (s, e) -> System.out.println("Counter at maximum!"))
                
                .conditionalTransition(1, "DECREMENT", (s, e) -> {
                    counter.value--;
                    return Math.max(counter.value, 0);
                })
                
                .conditionalTransition(2, "DECREMENT", (s, e) -> {
                    counter.value--;
                    return Math.max(counter.value, 0);
                })
                
                .conditionalTransition(3, "DECREMENT", (s, e) -> {
                    counter.value--;
                    return Math.max(counter.value, 0);
                })
                
                .transition(0, "DECREMENT", 0,
                    (s, e) -> System.out.println("Counter at minimum!"))
                
                .transition(0, "RESET", 0, (s, e) -> counter.value = 0)
                .transition(1, "RESET", 0, (s, e) -> counter.value = 0)
                .transition(2, "RESET", 0, (s, e) -> counter.value = 0)
                .transition(3, "RESET", 0, (s, e) -> counter.value = 0)
                
                .build();
        
        System.out.println("\nTesting counter state machine:");
        countMachine.handleEvent("INCREMENT");
        countMachine.handleEvent("INCREMENT");
        countMachine.handleEvent("INCREMENT");
        countMachine.handleEvent("INCREMENT");
        countMachine.handleEvent("INCREMENT");
        System.out.println("Current state: " + countMachine.getCurrentState());
        
        countMachine.handleEvent("DECREMENT");
        countMachine.handleEvent("DECREMENT");
        System.out.println("Current state: " + countMachine.getCurrentState());
        
        countMachine.handleEvent("RESET");
        System.out.println("Current state: " + countMachine.getCurrentState());
        
        System.out.println("\n--- Advantages of Functional Approach ---");
        System.out.println("+ Concise and expressive");
        System.out.println("+ No class proliferation");
        System.out.println("+ Easy to compose behaviors");
        System.out.println("+ Fluent builder pattern");
        System.out.println("+ Type-safe with generics");
        
        System.out.println("\n--- Trade-offs ---");
        System.out.println("- Can become hard to read with complex logic");
        System.out.println("- Debugging lambdas can be challenging");
        System.out.println("- Less obvious structure than OO approach");
    }
}