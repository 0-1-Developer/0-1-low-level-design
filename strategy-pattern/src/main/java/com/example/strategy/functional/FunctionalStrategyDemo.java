package com.example.strategy.functional;

import java.util.function.Function;

public class FunctionalStrategyDemo {
    public static void main(String[] args) {
        System.out.println("=== Functional Strategy Pattern Demo (Java 8+) ===\n");
        
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(new ShoppingCart.Item("Laptop", 1200.00, 1));
        cart.addItem(new ShoppingCart.Item("Mouse", 25.00, 2));
        cart.addItem(new ShoppingCart.Item("Keyboard", 75.00, 1));
        cart.addItem(new ShoppingCart.Item("USB Cable", 10.00, 15));
        
        cart.displayCart();
        
        System.out.println("\n1. Lambda Expression Strategies:");
        System.out.println("---------------------------------");
        
        cart.setDiscountStrategy((price, quantity) -> price * quantity);
        System.out.printf("No Discount: $%.2f%n", cart.calculateTotal());
        
        cart.setDiscountStrategy((price, quantity) -> price * quantity * 0.9);
        System.out.printf("10%% Discount: $%.2f%n", cart.calculateTotal());
        
        cart.setDiscountStrategy((price, quantity) -> 
            quantity >= 10 ? price * quantity * 0.7 : price * quantity);
        System.out.printf("Bulk Discount (30%% off for 10+ items): $%.2f%n", cart.calculateTotal());
        
        System.out.println("\n2. Method References:");
        System.out.println("----------------------");
        
        cart.setDiscountStrategy(DiscountStrategies.NO_DISCOUNT);
        System.out.printf("No Discount (Method Ref): $%.2f%n", cart.calculateTotal());
        
        cart.setDiscountStrategy(DiscountStrategies.SEASONAL_DISCOUNT);
        System.out.printf("Seasonal Discount (25%% off): $%.2f%n", cart.calculateTotal());
        
        System.out.println("\n3. Higher-Order Functions:");
        System.out.println("---------------------------");
        
        DiscountStrategy customDiscount = DiscountStrategies.createPercentageDiscount(15);
        cart.setDiscountStrategy(customDiscount);
        System.out.printf("Custom 15%% Discount: $%.2f%n", cart.calculateTotal());
        
        cart.setDiscountStrategy(DiscountStrategies.createTieredDiscount());
        System.out.printf("Tiered Discount: $%.2f%n", cart.calculateTotal());
        
        System.out.println("\n4. Strategy Composition:");
        System.out.println("-------------------------");
        
        DiscountStrategy combined = DiscountStrategies.combineStrategies(
            DiscountStrategies.PERCENTAGE_DISCOUNT,
            DiscountStrategies.BULK_DISCOUNT,
            DiscountStrategies.createPercentageDiscount(20)
        );
        cart.setDiscountStrategy(combined);
        System.out.printf("Best Available Discount: $%.2f%n", cart.calculateTotal());
        
        System.out.println("\n5. Function as Strategy:");
        System.out.println("-------------------------");
        
        Function<ShoppingCart.Item, Double> premiumMemberPricing = 
            item -> item.getPrice() * item.getQuantity() * 0.85;
        
        Function<ShoppingCart.Item, Double> blackFridayPricing = 
            item -> item.getPrice() * item.getQuantity() * 0.5;
        
        System.out.printf("Premium Member Total: $%.2f%n", 
            cart.calculateTotalWithStrategy(premiumMemberPricing));
        System.out.printf("Black Friday Total: $%.2f%n", 
            cart.calculateTotalWithStrategy(blackFridayPricing));
        
        System.out.println("\n6. Conditional Strategies:");
        System.out.println("---------------------------");
        
        double electronicsOnly = cart.calculateConditionalTotal(
            item -> item.getName().equals("Laptop") || item.getName().equals("Mouse") || item.getName().equals("Keyboard"),
            (price, qty) -> price * qty * 0.8
        );
        System.out.printf("Electronics Only (20%% off): $%.2f%n", electronicsOnly);
        
        double bulkItemsOnly = cart.calculateConditionalTotal(
            item -> item.getQuantity() >= 10,
            DiscountStrategies.createDynamicDiscount(50)
        );
        System.out.printf("Bulk Items Only (Dynamic Discount): $%.2f%n", bulkItemsOnly);
        
        System.out.println("\n=== Demo Complete ===");
    }
}