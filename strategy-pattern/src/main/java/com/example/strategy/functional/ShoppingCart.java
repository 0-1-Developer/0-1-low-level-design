package com.example.strategy.functional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class ShoppingCart {
    private List<Item> items = new ArrayList<>();
    private DiscountStrategy discountStrategy;
    
    public static class Item {
        private String name;
        private double price;
        private int quantity;
        
        public Item(String name, double price, int quantity) {
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }
        
        public String getName() { return name; }
        public double getPrice() { return price; }
        public int getQuantity() { return quantity; }
    }
    
    public void addItem(Item item) {
        items.add(item);
    }
    
    public void setDiscountStrategy(DiscountStrategy strategy) {
        this.discountStrategy = strategy;
    }
    
    public double calculateTotal() {
        return items.stream()
            .mapToDouble(item -> {
                double basePrice = item.getPrice() * item.getQuantity();
                if (discountStrategy != null) {
                    return discountStrategy.applyDiscount(item.getPrice(), item.getQuantity());
                }
                return basePrice;
            })
            .sum();
    }
    
    public double calculateTotalWithStrategy(Function<Item, Double> priceCalculator) {
        return items.stream()
            .mapToDouble(item -> priceCalculator.apply(item))
            .sum();
    }
    
    public double calculateConditionalTotal(Predicate<Item> condition, 
                                           BiFunction<Double, Integer, Double> calculator) {
        return items.stream()
            .filter(condition)
            .mapToDouble(item -> calculator.apply(item.getPrice(), item.getQuantity()))
            .sum();
    }
    
    public void displayCart() {
        System.out.println("\nShopping Cart Contents:");
        System.out.println("------------------------");
        items.forEach(item -> 
            System.out.printf("%s: $%.2f x %d = $%.2f%n", 
                item.getName(), item.getPrice(), item.getQuantity(), 
                item.getPrice() * item.getQuantity())
        );
    }
    
    public List<Item> getItems() {
        return new ArrayList<>(items);
    }
}