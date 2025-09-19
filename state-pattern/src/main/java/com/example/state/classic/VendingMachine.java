package com.example.state.classic;

public class VendingMachine {
    private State currentState;
    private int balance;
    private int productPrice = 50;
    private int productCount = 5;

    public VendingMachine() {
        this.currentState = new IdleState();
        this.balance = 0;
    }

    public void setState(State state) {
        System.out.println("State transition: " + currentState.getName() + " -> " + state.getName());
        this.currentState = state;
    }

    public void handle(String event) {
        System.out.println("\nEvent: " + event);
        System.out.println("Current state: " + currentState.getName());
        currentState.handle(this, event);
    }

    public State getState() {
        return currentState;
    }

    public int getBalance() {
        return balance;
    }

    public void addMoney(int amount) {
        balance += amount;
        System.out.println("Added money: " + amount + ", Balance: " + balance);
    }

    public void refundMoney() {
        System.out.println("Refunding: " + balance);
        balance = 0;
    }

    public boolean hasEnoughBalance() {
        return balance >= productPrice;
    }

    public boolean hasProduct() {
        return productCount > 0;
    }

    public void dispenseProduct() {
        if (productCount > 0 && balance >= productPrice) {
            productCount--;
            balance -= productPrice;
            System.out.println("Product dispensed! Remaining products: " + productCount);
            if (balance > 0) {
                System.out.println("Returning change: " + balance);
                balance = 0;
            }
        }
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void showStatus() {
        System.out.println("=== Machine Status ===");
        System.out.println("State: " + currentState.getName());
        System.out.println("Balance: " + balance);
        System.out.println("Product count: " + productCount);
        System.out.println("Product price: " + productPrice);
    }
}