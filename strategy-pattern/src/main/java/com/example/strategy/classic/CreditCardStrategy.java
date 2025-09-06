package com.example.strategy.classic;

import java.util.Scanner;

public class CreditCardStrategy implements PaymentStrategy {
    private String cardNumber;
    private String cvv;
    private String dateOfExpiry;
    private double balance;

    public CreditCardStrategy() {
        this.balance = 1000.0;
    }

    @Override
    public void collectPaymentDetails() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter card number: ");
        cardNumber = scanner.nextLine();
        System.out.print("Enter CVV: ");
        cvv = scanner.nextLine();
        System.out.print("Enter expiry date (MM/YY): ");
        dateOfExpiry = scanner.nextLine();
    }

    @Override
    public boolean validatePaymentDetails() {
        if (cardNumber == null || cardNumber.length() != 16) {
            System.out.println("Invalid card number!");
            return false;
        }
        if (cvv == null || cvv.length() != 3) {
            System.out.println("Invalid CVV!");
            return false;
        }
        System.out.println("Credit card details validated successfully.");
        return true;
    }

    @Override
    public boolean pay(double amount) {
        if (balance >= amount) {
            System.out.printf("Paying $%.2f using Credit Card%n", amount);
            balance -= amount;
            System.out.printf("Payment successful! Remaining balance: $%.2f%n", balance);
            return true;
        } else {
            System.out.println("Insufficient funds on credit card!");
            return false;
        }
    }

    public void setTestData(String cardNumber, String cvv, String dateOfExpiry) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.dateOfExpiry = dateOfExpiry;
    }
}