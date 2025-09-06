package com.example.builder.classic;

public class ClassicBuilderDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Classic GoF Builder Pattern Demo ===\n");
        
        Director director = new Director();
        
        System.out.println("1. Building Luxury Car:");
        Builder luxuryBuilder = new LuxuryCarBuilder();
        director.setBuilder(luxuryBuilder);
        Product luxuryCar = director.construct();
        System.out.println("Result: " + luxuryCar);
        System.out.println();
        
        System.out.println("2. Building Economy Car:");
        Builder economyBuilder = new EconomyCarBuilder();
        director.setBuilder(economyBuilder);
        Product economyCar = director.construct();
        System.out.println("Result: " + economyCar);
        System.out.println();
        
        System.out.println("3. Building Sport Car:");
        Builder sportBuilder = new SportCarBuilder();
        director.setBuilder(sportBuilder);
        Product sportCar = director.construct();
        System.out.println("Result: " + sportCar);
        System.out.println();
        
        System.out.println("4. Building Minimal Sport Car (Partial Construction):");
        director.setBuilder(sportBuilder);
        Product minimalSportCar = director.constructMinimal();
        System.out.println("Result: " + minimalSportCar);
        System.out.println();
        
        System.out.println("Key Benefits:");
        System.out.println("- Same construction process creates different representations");
        System.out.println("- Director encapsulates construction logic");
        System.out.println("- Easy to add new concrete builders");
        System.out.println("- Construction steps are clearly defined");
        
        System.out.println("\n=== Demo Complete ===");
    }
}