package com.example.strategy.classic;

public class PaymentContext {
    private PaymentStrategy strategy;

    public void setStrategy(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public boolean executePayment(double amount) {
        if (strategy == null) {
            System.out.println("No payment strategy set!");
            return false;
        }
        
        if (!strategy.validatePaymentDetails()) {
            System.out.println("Payment validation failed!");
            return false;
        }
        
        return strategy.pay(amount);
    }

    public void collectPaymentDetails() {
        if (strategy != null) {
            strategy.collectPaymentDetails();
        } else {
            System.out.println("No payment strategy set!");
        }
    }
}