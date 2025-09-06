package com.example.strategy.classic;

import java.util.Scanner;

public class CryptoStrategy implements PaymentStrategy {
    private String walletAddress;
    private String privateKey;
    private double balance;

    public CryptoStrategy() {
        this.balance = 5.0;
    }

    @Override
    public void collectPaymentDetails() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter crypto wallet address: ");
        walletAddress = scanner.nextLine();
        System.out.print("Enter private key: ");
        privateKey = scanner.nextLine();
    }

    @Override
    public boolean validatePaymentDetails() {
        if (walletAddress == null || !walletAddress.startsWith("0x") || walletAddress.length() != 42) {
            System.out.println("Invalid wallet address!");
            return false;
        }
        if (privateKey == null || privateKey.length() < 10) {
            System.out.println("Invalid private key!");
            return false;
        }
        System.out.println("Crypto wallet validated successfully.");
        return true;
    }

    @Override
    public boolean pay(double amount) {
        double cryptoAmount = amount / 50000;
        if (balance >= cryptoAmount) {
            System.out.printf("Paying %.6f BTC (equivalent to $%.2f) from wallet %s%n", 
                             cryptoAmount, amount, walletAddress);
            balance -= cryptoAmount;
            System.out.printf("Transaction successful! Remaining balance: %.6f BTC%n", balance);
            return true;
        } else {
            System.out.println("Insufficient crypto balance!");
            return false;
        }
    }

    public void setTestData(String walletAddress, String privateKey) {
        this.walletAddress = walletAddress;
        this.privateKey = privateKey;
    }
}