package com.example.strategy.classic;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PayPalStrategy implements PaymentStrategy {
    private static final Map<String, String> DATABASE = new HashMap<>();
    private String email;
    private String password;
    private boolean signedIn;

    static {
        DATABASE.put("user@example.com", "password123");
        DATABASE.put("john@test.com", "john123");
    }

    @Override
    public void collectPaymentDetails() {
        Scanner scanner = new Scanner(System.in);
        while (!signedIn) {
            System.out.print("Enter PayPal email: ");
            email = scanner.nextLine();
            System.out.print("Enter password: ");
            password = scanner.nextLine();
            if (verify()) {
                System.out.println("PayPal authentication successful!");
            } else {
                System.out.println("Wrong email or password! Try again.");
            }
        }
    }

    private boolean verify() {
        signedIn = DATABASE.containsKey(email) && DATABASE.get(email).equals(password);
        return signedIn;
    }

    @Override
    public boolean validatePaymentDetails() {
        return signedIn;
    }

    @Override
    public boolean pay(double amount) {
        if (signedIn) {
            System.out.printf("Paying $%.2f using PayPal account %s%n", amount, email);
            System.out.println("Payment successful via PayPal!");
            return true;
        } else {
            System.out.println("PayPal authentication required!");
            return false;
        }
    }

    public void setTestData(String email, String password) {
        this.email = email;
        this.password = password;
        this.signedIn = verify();
    }
}