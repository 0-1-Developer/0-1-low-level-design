package com.example.strategy.classic;

public class ClassicStrategyDemo {
    public static void main(String[] args) {
        System.out.println("=== Classic Strategy Pattern Demo ===\n");
        
        PaymentContext context = new PaymentContext();
        
        System.out.println("1. Testing Credit Card Payment:");
        System.out.println("--------------------------------");
        CreditCardStrategy creditCard = new CreditCardStrategy();
        creditCard.setTestData("1234567890123456", "123", "12/25");
        context.setStrategy(creditCard);
        context.executePayment(150.00);
        
        System.out.println("\n2. Testing PayPal Payment:");
        System.out.println("--------------------------------");
        PayPalStrategy payPal = new PayPalStrategy();
        payPal.setTestData("user@example.com", "password123");
        context.setStrategy(payPal);
        context.executePayment(200.00);
        
        System.out.println("\n3. Testing Crypto Payment:");
        System.out.println("--------------------------------");
        CryptoStrategy crypto = new CryptoStrategy();
        crypto.setTestData("0x742d35Cc6634C0532925a3b844Bc9e7595f0bEb8", "privateKey123456");
        context.setStrategy(crypto);
        context.executePayment(50000.00);
        
        System.out.println("\n4. Testing Strategy Switch at Runtime:");
        System.out.println("---------------------------------------");
        System.out.println("Switching from Credit Card to PayPal...");
        context.setStrategy(creditCard);
        context.executePayment(50.00);
        context.setStrategy(payPal);
        context.executePayment(75.00);
        
        System.out.println("\n=== Demo Complete ===");
    }
}