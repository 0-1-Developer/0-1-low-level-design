package com.example.builder.directorless;

public class DirectorlessBuilderDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Director-less Builder Pattern Demo ===");
        System.out.println("Client orchestrates construction directly without a Director class\n");
        
        System.out.println("1. Building a simple BLT sandwich:");
        Sandwich blt = new Sandwich.SandwichBuilder()
            .bread("Sourdough")
            .protein("Bacon")
            .addVegetable("Lettuce")
            .addVegetable("Tomato")
            .addCondiment("Mayo")
            .toasted()
            .build();
        
        System.out.println(blt);
        System.out.println();
        
        System.out.println("2. Building a veggie sandwich:");
        Sandwich veggie = new Sandwich.SandwichBuilder()
            .bread("Whole Wheat")
            .cheese("Swiss")
            .addVegetable("Cucumber")
            .addVegetable("Sprouts")
            .addVegetable("Avocado")
            .addVegetable("Red Onion")
            .addCondiment("Hummus")
            .addCondiment("Mustard")
            .size("Large")
            .build();
        
        System.out.println(veggie);
        System.out.println();
        
        System.out.println("3. Building a club sandwich:");
        Sandwich club = new Sandwich.SandwichBuilder()
            .bread("White")
            .protein("Turkey")
            .protein("Ham")
            .cheese("Cheddar")
            .addVegetable("Lettuce")
            .addVegetable("Tomato")
            .addCondiment("Mayo")
            .addCondiment("Mustard")
            .toasted()
            .size("Large")
            .build();
        
        System.out.println(club);
        
        System.out.println("\nKey Characteristics of Director-less Builder:");
        System.out.println("- Client has full control over construction process");
        System.out.println("- No separate Director class needed");
        System.out.println("- Simpler architecture for straightforward use cases");
        System.out.println("- Client must know the construction sequence");
        System.out.println("- Good when construction logic is simple or varies greatly");
        
        System.out.println("\n=== Demo Complete ===");
    }
}