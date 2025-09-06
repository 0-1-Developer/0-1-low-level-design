package com.example.builder.fluent;

public class FluentBuilderDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Fluent Builder Pattern Demo ===\n");
        
        System.out.println("1. Building a Gaming Computer with method chaining:");
        Computer gamingPC = new Computer.ComputerBuilder()
            .cpu("Intel i9-13900K")
            .memory("32GB DDR5")
            .storage("2TB NVMe SSD")
            .graphics("NVIDIA RTX 4080")
            .motherboard("ASUS ROG Strix Z790")
            .withWifi()
            .withBluetooth()
            .withWebcam()
            .ports(8)
            .operatingSystem("Windows 11 Pro")
            .build();
        
        System.out.println(gamingPC);
        System.out.println();
        
        System.out.println("2. Building a Basic Office Computer:");
        Computer officePC = new Computer.ComputerBuilder()
            .cpu("Intel i5-13400")
            .memory("16GB DDR4")
            .storage("512GB SSD")
            .withWifi()
            .operatingSystem("Windows 11 Home")
            .build();
        
        System.out.println(officePC);
        System.out.println();
        
        System.out.println("3. Building a Developer Workstation:");
        Computer devMachine = new Computer.ComputerBuilder()
            .cpu("AMD Ryzen 9 7950X")
            .memory("64GB DDR5")
            .storage("1TB NVMe SSD")
            .graphics("NVIDIA RTX 4070")
            .withWifi()
            .withBluetooth()
            .ports(12)
            .operatingSystem("Ubuntu 22.04 LTS")
            .build();
        
        System.out.println(devMachine);
        System.out.println();
        
        System.out.println("4. Demonstrating validation (missing required field):");
        try {
            Computer invalidPC = new Computer.ComputerBuilder()
                .cpu("Intel i7-13700K")
                .build();
        } catch (IllegalStateException e) {
            System.out.println("Build failed: " + e.getMessage());
        }
        
        System.out.println();
        System.out.println("Key Benefits:");
        System.out.println("- Highly readable method chaining");
        System.out.println("- Optional parameters handled elegantly");
        System.out.println("- Immutable product after building");
        System.out.println("- Validation at build time");
        System.out.println("- No telescoping constructor problem");
        
        System.out.println("\n=== Demo Complete ===");
    }
}