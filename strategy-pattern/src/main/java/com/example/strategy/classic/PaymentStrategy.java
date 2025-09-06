package com.example.strategy.classic;

public interface PaymentStrategy {
    boolean pay(double amount);
    void collectPaymentDetails();
    boolean validatePaymentDetails();
}