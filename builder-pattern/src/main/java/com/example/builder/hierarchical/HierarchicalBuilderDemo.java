package com.example.builder.hierarchical;

public class HierarchicalBuilderDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Hierarchical Builder Pattern Demo ===\n");
        
        System.out.println("1. Building different types of vehicles using hierarchical builders:");
        System.out.println();
        
        System.out.println("Building a sedan car:");
        Car sedan = Car.builder()
            .make("Toyota")
            .model("Camry")
            .year(2023)
            .color("Blue")
            .engine("2.5L Hybrid")
            .doors(4)
            .transmission("CVT")
            .withAirConditioning()
            .withSunroof()
            .build();
        
        System.out.println(sedan);
        System.out.println();
        
        System.out.println("Building a sport motorcycle:");
        Motorcycle sportBike = Motorcycle.builder()
            .make("Yamaha")
            .model("R1")
            .year(2023)
            .color("Racing Blue")
            .bikeType("Super Sport")
            .engineCC(1000)
            .withWindscreen()
            .build();
        
        System.out.println(sportBike);
        System.out.println();
        
        System.out.println("Building a pickup truck:");
        Truck pickup = Truck.builder()
            .make("Ford")
            .model("F-150")
            .year(2023)
            .color("Black")
            .engine("5.0L V8")
            .payloadCapacity(1.5)
            .bedType("Short Bed")
            .withTrailerHitch()
            .withFourWheelDrive()
            .build();
        
        System.out.println(pickup);
        System.out.println();
        
        System.out.println("2. Demonstrating polymorphic behavior:");
        Vehicle[] vehicles = {sedan, sportBike, pickup};
        
        System.out.println("All vehicles in the collection:");
        for (int i = 0; i < vehicles.length; i++) {
            Vehicle vehicle = vehicles[i];
            System.out.println((i + 1) + ". " + vehicle.getYear() + " " + 
                              vehicle.getMake() + " " + vehicle.getModel() + 
                              " (" + vehicle.getClass().getSimpleName() + ")");
        }
        System.out.println();
        
        System.out.println("3. Demonstrating type-specific builder methods:");
        
        System.out.println("Building a touring motorcycle with sidecar:");
        Motorcycle touringBike = Motorcycle.builder()
            .make("BMW")
            .model("R1250GS")
            .year(2023)
            .color("White")
            .bikeType("Adventure")
            .engineCC(1250)
            .withWindscreen()
            .withSidecar()
            .build();
        
        System.out.println(touringBike);
        System.out.println("Note: Sidecar automatically adjusted wheels to 3");
        System.out.println();
        
        System.out.println("4. Demonstrating validation across hierarchy:");
        
        try {
            Car invalidCar = Car.builder()
                .make("Honda")
                .model("Civic")
                .year(1800)
                .build();
        } catch (IllegalStateException e) {
            System.out.println("✓ Base validation caught invalid year: " + e.getMessage());
        }
        
        try {
            Motorcycle invalidBike = Motorcycle.builder()
                .make("Harley")
                .model("Sportster")
                .engineCC(3000)
                .build();
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Subclass validation caught invalid engine CC: " + e.getMessage());
        }
        
        try {
            Truck invalidTruck = Truck.builder()
                .make("Chevy")
                .model("Silverado")
                .year(2023)
                .payloadCapacity(15.0)
                .build();
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Subclass validation caught invalid payload: " + e.getMessage());
        }
        
        System.out.println();
        System.out.println("Key Benefits of Hierarchical Builder:");
        System.out.println("- Maintains type safety across inheritance hierarchies");
        System.out.println("- Each subclass builder returns its own type (not parent)");
        System.out.println("- Shared validation and common properties in base builder");
        System.out.println("- Subclass-specific methods and validation");
        System.out.println("- Self-type pattern prevents casting issues");
        System.out.println("- Polymorphic usage of built objects");
        System.out.println("- Clean separation of concerns between hierarchy levels");
        
        System.out.println("\n=== Demo Complete ===");
    }
}