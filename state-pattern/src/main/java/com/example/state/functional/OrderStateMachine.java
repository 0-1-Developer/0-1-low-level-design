package com.example.state.functional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderStateMachine {
    
    public enum OrderState {
        CREATED, PAID, CONFIRMED, SHIPPED, DELIVERED, CANCELLED, REFUNDED
    }
    
    public enum OrderEvent {
        PAY, CONFIRM, SHIP, DELIVER, CANCEL, REFUND, RETURN
    }
    
    public static class OrderContext {
        private String orderId;
        private double amount;
        private List<String> history = new ArrayList<>();
        private AtomicInteger retryCount = new AtomicInteger(0);
        
        public OrderContext(String orderId, double amount) {
            this.orderId = orderId;
            this.amount = amount;
        }
        
        public void addHistory(String event) {
            history.add(event);
        }
        
        public void printHistory() {
            System.out.println("\nOrder " + orderId + " History:");
            history.forEach(h -> System.out.println("  - " + h));
        }
        
        public int incrementRetry() {
            return retryCount.incrementAndGet();
        }
        
        public void resetRetry() {
            retryCount.set(0);
        }
    }
    
    public static FunctionalStateMachine<OrderState, OrderEvent> createOrderMachine(OrderContext context) {
        return FunctionalStateMachine.<OrderState, OrderEvent>builder(OrderState.CREATED)
            .onEntry(OrderState.CREATED, state -> {
                System.out.println("Order " + context.orderId + " created with amount $" + context.amount);
                context.addHistory("Order created");
            })
            
            .onEntry(OrderState.PAID, state -> {
                System.out.println("Payment received for order " + context.orderId);
                context.addHistory("Payment processed");
            })
            
            .onEntry(OrderState.CONFIRMED, state -> {
                System.out.println("Order " + context.orderId + " confirmed by warehouse");
                context.addHistory("Order confirmed");
            })
            
            .onEntry(OrderState.SHIPPED, state -> {
                System.out.println("Order " + context.orderId + " shipped");
                context.addHistory("Order shipped");
            })
            
            .onEntry(OrderState.DELIVERED, state -> {
                System.out.println("Order " + context.orderId + " delivered successfully!");
                context.addHistory("Order delivered");
            })
            
            .onEntry(OrderState.CANCELLED, state -> {
                System.out.println("Order " + context.orderId + " has been cancelled");
                context.addHistory("Order cancelled");
            })
            
            .onEntry(OrderState.REFUNDED, state -> {
                System.out.println("Refund issued for order " + context.orderId);
                context.addHistory("Refund processed");
            })
            
            .transition(OrderState.CREATED, OrderEvent.PAY, OrderState.PAID,
                (state, event) -> System.out.println("Processing payment of $" + context.amount))
            
            .transition(OrderState.PAID, OrderEvent.CONFIRM, OrderState.CONFIRMED,
                (state, event) -> System.out.println("Checking inventory..."))
            
            .conditionalTransition(OrderState.CONFIRMED, OrderEvent.SHIP,
                (state, event) -> {
                    if (context.incrementRetry() <= 2) {
                        System.out.println("Attempting to ship (attempt " + context.retryCount.get() + ")");
                        if (Math.random() > 0.3) {
                            context.resetRetry();
                            return OrderState.SHIPPED;
                        } else {
                            System.out.println("Shipping delayed, will retry");
                            return OrderState.CONFIRMED;
                        }
                    } else {
                        System.out.println("Shipping failed after multiple attempts");
                        return OrderState.CANCELLED;
                    }
                })
            
            .transition(OrderState.SHIPPED, OrderEvent.DELIVER, OrderState.DELIVERED)
            
            .transition(OrderState.CREATED, OrderEvent.CANCEL, OrderState.CANCELLED)
            .transition(OrderState.PAID, OrderEvent.CANCEL, OrderState.CANCELLED,
                (state, event) -> System.out.println("Cancellation after payment - refund will be processed"))
            
            .transition(OrderState.CANCELLED, OrderEvent.REFUND, OrderState.REFUNDED)
            
            .transition(OrderState.DELIVERED, OrderEvent.RETURN, OrderState.REFUNDED,
                (state, event) -> {
                    System.out.println("Return accepted - processing refund");
                    context.addHistory("Return processed");
                })
            
            .onExit(OrderState.PAID, state -> 
                System.out.println("Moving order out of payment stage"))
            
            .onExit(OrderState.SHIPPED, state -> 
                System.out.println("Order no longer in transit"))
            
            .build();
    }
    
    public static FunctionalStateMachine<String, String> createSimpleMachine() {
        return FunctionalStateMachine.<String, String>builder("OFF")
            .transition("OFF", "POWER", "ON", 
                (s, e) -> System.out.println("Powering on..."))
            
            .transition("ON", "POWER", "OFF",
                (s, e) -> System.out.println("Powering off..."))
            
            .transition("ON", "SLEEP", "STANDBY",
                (s, e) -> System.out.println("Entering standby mode"))
            
            .transition("STANDBY", "WAKE", "ON",
                (s, e) -> System.out.println("Waking up"))
            
            .transition("STANDBY", "POWER", "OFF",
                (s, e) -> System.out.println("Shutting down from standby"))
            
            .onEntry("ON", s -> System.out.println("System is now ON"))
            .onEntry("OFF", s -> System.out.println("System is now OFF"))
            .onEntry("STANDBY", s -> System.out.println("System in STANDBY mode"))
            
            .build();
    }
}