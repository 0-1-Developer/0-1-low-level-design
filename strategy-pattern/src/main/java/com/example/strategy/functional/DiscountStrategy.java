package com.example.strategy.functional;

@FunctionalInterface
public interface DiscountStrategy {
    double applyDiscount(double price, int quantity);
}