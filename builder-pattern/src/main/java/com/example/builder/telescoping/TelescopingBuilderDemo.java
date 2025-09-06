package com.example.builder.telescoping;

public class TelescopingBuilderDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Telescoping Constructor Problem & Builder Solution ===\n");
        
        System.out.println("1. The Telescoping Constructor Problem:");
        System.out.println("   Creating pizzas with multiple overloaded constructors becomes unwieldy:\n");
        
        Pizza basicPizza = new Pizza("Thin", "Marinara", "Provolone");
        System.out.println("Basic Pizza (3 args):");
        System.out.println(basicPizza);
        System.out.println();
        
        Pizza mediumPizza = new Pizza("Regular", "Tomato", "Mozzarella", 
                                     new String[]{"Pepperoni", "Mushrooms"}, 
                                     true, false, "Large");
        System.out.println("Complex Pizza (7 args - hard to read):");
        System.out.println(mediumPizza);
        System.out.println();
        
        Pizza confusingPizza = new Pizza("Whole Wheat", "BBQ", "Cheddar", 
                                        new String[]{"Chicken", "Onions"}, 
                                        false, true, "Small", true);
        System.out.println("Very Complex Pizza (8 args - very confusing):");
        System.out.println("  - Which boolean is extraCheese? thickCrust? glutenFree?");
        System.out.println("  - Parameter order is hard to remember");
        System.out.println("  - Easy to pass wrong values in wrong positions");
        System.out.println(confusingPizza);
        System.out.println();
        
        System.out.println("2. Builder Pattern Solution (Much Cleaner):");
        System.out.println();
        
        Pizza builderPizza1 = new PizzaBuilder()
            .base("Thin")
            .sauce("Pesto")
            .cheese("Goat Cheese")
            .addTopping("Sun-dried Tomatoes")
            .addTopping("Arugula")
            .size("Medium")
            .build();
        
        System.out.println("Gourmet Pizza (using builder):");
        System.out.println(builderPizza1);
        System.out.println();
        
        Pizza builderPizza2 = new PizzaBuilder()
            .sauce("Buffalo")
            .addTopping("Chicken")
            .addTopping("Blue Cheese")
            .addTopping("Celery")
            .extraCheese()
            .thickCrust()
            .size("Large")
            .build();
        
        System.out.println("Buffalo Chicken Pizza (using builder):");
        System.out.println(builderPizza2);
        System.out.println();
        
        Pizza healthyPizza = new PizzaBuilder()
            .base("Cauliflower")
            .sauce("Olive Oil")
            .cheese("Vegan Mozzarella")
            .addTopping("Spinach")
            .addTopping("Bell Peppers")
            .addTopping("Olives")
            .glutenFree()
            .size("Small")
            .build();
        
        System.out.println("Healthy Pizza (using builder):");
        System.out.println(healthyPizza);
        System.out.println();
        
        System.out.println("Key Improvements with Builder:");
        System.out.println("- Self-documenting method names");
        System.out.println("- Optional parameters handled naturally");
        System.out.println("- Flexible parameter ordering");
        System.out.println("- No need to remember parameter positions");
        System.out.println("- Easy to add/remove optional parameters");
        System.out.println("- Prevents parameter confusion (boolean hell)");
        
        System.out.println("\n=== Demo Complete ===");
    }
}