package com.example.builder.stepbuilder;

public class StepBuilderDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Step Builder Pattern Demo ===\n");
        
        System.out.println("1. Building a basic house (required fields only):");
        
        House basicHouse = House.builder()
            .foundation("Concrete Slab")
            .structure("Wood Frame")
            .roof("Asphalt Shingles")
            .build();
        
        System.out.println("Basic house:");
        System.out.println(basicHouse);
        System.out.println();
        
        System.out.println("2. Building a luxury house (with optional features):");
        
        House luxuryHouse = House.builder()
            .foundation("Reinforced Concrete")
            .structure("Steel Frame")
            .roof("Clay Tiles")
            .interior("Luxury Finishes")
            .exterior("Natural Stone")
            .withGarage()
            .withGarden()
            .withPool()
            .build();
        
        System.out.println("Luxury house:");
        System.out.println(luxuryHouse);
        System.out.println();
        
        System.out.println("3. Building a modern house (partial optional features):");
        
        House modernHouse = House.builder()
            .foundation("Concrete Foundation")
            .structure("Modern Steel")
            .roof("Metal Roofing")
            .exterior("Glass and Steel")
            .withGarage()
            .build();
        
        System.out.println("Modern house:");
        System.out.println(modernHouse);
        System.out.println();
        
        System.out.println("4. Demonstrating compile-time enforcement:");
        System.out.println("   The following code would NOT compile:");
        System.out.println("   House.builder().build(); // Missing required steps");
        System.out.println("   House.builder().foundation(\"X\").build(); // Missing structure and roof");
        System.out.println("   House.builder().structure(\"Y\").foundation(\"X\"); // Wrong order");
        System.out.println();
        
        System.out.println("5. IDE Support demonstration:");
        System.out.println("   Try typing 'House.builder().' - IDE will only show foundation() method");
        System.out.println("   After foundation(), IDE will only show structure() method");
        System.out.println("   After roof(), IDE shows all optional methods plus build()");
        System.out.println();
        
        System.out.println("Key Benefits of Step Builder:");
        System.out.println("- Compile-time enforcement of required fields");
        System.out.println("- Clear ordering of mandatory construction steps");
        System.out.println("- Prevents incomplete object creation");
        System.out.println("- Excellent IDE support and discoverability");
        System.out.println("- No runtime validation needed for required fields");
        System.out.println("- Type-safe progression through build steps");
        System.out.println("- Optional parameters remain flexible");
        
        System.out.println("\n=== Demo Complete ===");
    }
}