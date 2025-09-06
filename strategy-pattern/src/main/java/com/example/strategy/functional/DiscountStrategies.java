package com.example.strategy.functional;

import java.util.function.BiFunction;

public class DiscountStrategies {
    
    public static final DiscountStrategy NO_DISCOUNT = 
        (price, quantity) -> price * quantity;
    
    public static final DiscountStrategy PERCENTAGE_DISCOUNT = 
        (price, quantity) -> price * quantity * 0.9;
    
    public static final DiscountStrategy BULK_DISCOUNT = 
        (price, quantity) -> quantity >= 10 ? price * quantity * 0.8 : price * quantity;
    
    public static final DiscountStrategy SEASONAL_DISCOUNT = 
        (price, quantity) -> price * quantity * 0.75;
    
    public static DiscountStrategy createPercentageDiscount(double percentage) {
        return (price, quantity) -> price * quantity * (1 - percentage / 100);
    }
    
    public static DiscountStrategy createTieredDiscount() {
        return (price, quantity) -> {
            double total = price * quantity;
            if (total > 1000) return total * 0.7;
            if (total > 500) return total * 0.8;
            if (total > 100) return total * 0.9;
            return total;
        };
    }
    
    public static DiscountStrategy combineStrategies(DiscountStrategy... strategies) {
        return (price, quantity) -> {
            double minPrice = price * quantity;
            for (DiscountStrategy strategy : strategies) {
                double discountedPrice = strategy.applyDiscount(price, quantity);
                minPrice = Math.min(minPrice, discountedPrice);
            }
            return minPrice;
        };
    }
    
    public static BiFunction<Double, Integer, Double> createDynamicDiscount(double threshold) {
        return (price, quantity) -> {
            double total = price * quantity;
            return total > threshold ? total * 0.85 : total;
        };
    }
}