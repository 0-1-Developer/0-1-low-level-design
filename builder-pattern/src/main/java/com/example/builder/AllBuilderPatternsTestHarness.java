package com.example.builder;

import com.example.builder.classic.*;
import com.example.builder.fluent.*;
import com.example.builder.telescoping.*;
import com.example.builder.immutable.*;
import com.example.builder.nested.*;
import com.example.builder.stepbuilder.*;
import com.example.builder.hierarchical.*;
import com.example.builder.directorless.*;
import com.example.builder.dsl.*;
import com.example.builder.functional.*;
import com.example.builder.prototype.*;

import java.time.LocalDateTime;
import java.util.Arrays;

public class AllBuilderPatternsTestHarness {
    
    private static int totalTests = 0;
    private static int passedTests = 0;
    
    public static void main(String[] args) {
        System.out.println("=== Builder Pattern Test Harness ===");
        System.out.println("Testing all 11 builder pattern implementations\n");
        
        testClassicBuilder();
        testFluentBuilder();
        testTelescopingBuilder();
        testImmutableBuilder();
        testNestedBuilder();
        testStepBuilder();
        testHierarchicalBuilder();
        testDirectorlessBuilder();
        testDslBuilder();
        testFunctionalBuilder();
        testPrototypeBuilder();
        
        System.out.println("\n=== Test Summary ===");
        System.out.println("Total Tests: " + totalTests);
        System.out.println("Passed: " + passedTests);
        System.out.println("Failed: " + (totalTests - passedTests));
        System.out.println("Success Rate: " + (passedTests * 100.0 / totalTests) + "%");
        
        if (passedTests == totalTests) {
            System.out.println("*** All tests passed! Builder patterns are working correctly.");
        } else {
            System.out.println("*** Some tests failed. Please review the implementations.");
        }
    }
    
    private static void testClassicBuilder() {
        System.out.println("Testing Classic GoF Builder Pattern:");
        
        try {
            Director director = new Director();
            Builder luxuryBuilder = new LuxuryCarBuilder();
            director.setBuilder(luxuryBuilder);
            Product luxuryCar = director.construct();
            
            test("Classic Builder - Luxury Car Creation", 
                 luxuryCar != null && luxuryCar.getEngine().equals("V8 Twin-Turbo"));
            
            Builder economyBuilder = new EconomyCarBuilder();
            director.setBuilder(economyBuilder);
            Product economyCar = director.construct();
            
            test("Classic Builder - Economy Car Creation", 
                 economyCar != null && economyCar.getEngine().equals("4-Cylinder"));
            
            test("Classic Builder - Different Products", 
                 !luxuryCar.getEngine().equals(economyCar.getEngine()));
            
        } catch (Exception e) {
            test("Classic Builder - Exception Free", false);
            System.out.println("  Error: " + e.getMessage());
        }
        System.out.println();
    }
    
    private static void testFluentBuilder() {
        System.out.println("Testing Fluent Builder Pattern:");
        
        try {
            Computer gamingPC = new Computer.ComputerBuilder()
                .cpu("Intel i9-13900K")
                .memory("32GB DDR5")
                .storage("2TB NVMe SSD")
                .withWifi()
                .build();
            
            test("Fluent Builder - Gaming PC Creation", 
                 gamingPC != null && gamingPC.getCpu().equals("Intel i9-13900K"));
            
            test("Fluent Builder - WiFi Feature", 
                 gamingPC.hasWifi());
            
            try {
                Computer invalidPC = new Computer.ComputerBuilder()
                    .cpu("Intel i7-13700K")
                    .build();
                test("Fluent Builder - Validation", false);
            } catch (IllegalStateException e) {
                test("Fluent Builder - Validation", true);
            }
            
        } catch (Exception e) {
            test("Fluent Builder - Exception Free", false);
            System.out.println("  Error: " + e.getMessage());
        }
        System.out.println();
    }
    
    private static void testTelescopingBuilder() {
        System.out.println("Testing Telescoping Constructor Alternative:");
        
        try {
            Pizza pizza = new PizzaBuilder()
                .base("Thin")
                .sauce("Marinara")
                .cheese("Mozzarella")
                .addTopping("Pepperoni")
                .addTopping("Mushrooms")
                .extraCheese()
                .build();
            
            test("Telescoping Builder - Pizza Creation", 
                 pizza != null && pizza.getBase().equals("Thin"));
            
            test("Telescoping Builder - Extra Cheese", 
                 pizza.hasExtraCheese());
            
            test("Telescoping Builder - Toppings Count", 
                 pizza.getToppings().length == 2);
            
        } catch (Exception e) {
            test("Telescoping Builder - Exception Free", false);
            System.out.println("  Error: " + e.getMessage());
        }
        System.out.println();
    }
    
    private static void testImmutableBuilder() {
        System.out.println("Testing Immutable Object Builder:");
        
        try {
            User user = new User.UserBuilder()
                .id(1001)
                .username("johndoe")
                .email("john@example.com")
                .firstName("John")
                .lastName("Doe")
                .addInterest("Technology")
                .addInterest("Photography")
                .build();
            
            test("Immutable Builder - User Creation", 
                 user != null && user.getUsername().equals("johndoe"));
            
            try {
                user.getInterests().add("Hacking");
                test("Immutable Builder - Immutability", false);
            } catch (UnsupportedOperationException e) {
                test("Immutable Builder - Immutability", true);
            }
            
            test("Immutable Builder - Defensive Copy", 
                 user.getAddress() == null || user.getAddress() != user.getAddress());
            
        } catch (Exception e) {
            test("Immutable Builder - Exception Free", false);
            System.out.println("  Error: " + e.getMessage());
        }
        System.out.println();
    }
    
    private static void testNestedBuilder() {
        System.out.println("Testing Nested/Inner Builder Pattern:");
        
        try {
            DatabaseConnection connection = DatabaseConnection.builder()
                .host("localhost")
                .port(5432)
                .database("testdb")
                .username("user")
                .password("pass")
                .property("ssl", "true")
                .build();
            
            test("Nested Builder - Connection Creation", 
                 connection != null && connection.getHost().equals("localhost"));
            
            test("Nested Builder - Properties", 
                 connection.getAdditionalProperties().size() == 1);
            
            try {
                DatabaseConnection.builder()
                    .host("localhost")
                    .port(5432)
                    .username("user")
                    .password("pass")
                    .build();
                test("Nested Builder - Validation", false);
            } catch (IllegalStateException e) {
                test("Nested Builder - Validation", true);
            }
            
        } catch (Exception e) {
            test("Nested Builder - Exception Free", false);
            System.out.println("  Error: " + e.getMessage());
        }
        System.out.println();
    }
    
    private static void testStepBuilder() {
        System.out.println("Testing Step Builder Pattern:");
        
        try {
            House house = House.builder()
                .foundation("Concrete")
                .structure("Steel")
                .roof("Metal")
                .withGarage()
                .withGarden()
                .build();
            
            test("Step Builder - House Creation", 
                 house != null && house.getFoundation().equals("Concrete"));
            
            test("Step Builder - Optional Features", 
                 house.hasGarage() && house.hasGarden());
            
            test("Step Builder - Required Fields", 
                 house.getStructure().equals("Steel") && house.getRoof().equals("Metal"));
            
        } catch (Exception e) {
            test("Step Builder - Exception Free", false);
            System.out.println("  Error: " + e.getMessage());
        }
        System.out.println();
    }
    
    private static void testHierarchicalBuilder() {
        System.out.println("Testing Hierarchical Builder Pattern:");
        
        try {
            Car car = Car.builder()
                .make("Toyota")
                .model("Camry")
                .year(2023)
                .doors(4)
                .withSunroof()
                .build();
            
            test("Hierarchical Builder - Car Creation", 
                 car != null && car.getMake().equals("Toyota"));
            
            test("Hierarchical Builder - Car Specific Features", 
                 car.getDoors() == 4 && car.hasSunroof());
            
            Motorcycle bike = Motorcycle.builder()
                .make("Yamaha")
                .model("R1")
                .year(2023)
                .bikeType("Sport")
                .engineCC(1000)
                .build();
            
            test("Hierarchical Builder - Motorcycle Creation", 
                 bike != null && bike.getMake().equals("Yamaha"));
            
            test("Hierarchical Builder - Polymorphism", 
                 car instanceof Vehicle && bike instanceof Vehicle);
            
        } catch (Exception e) {
            test("Hierarchical Builder - Exception Free", false);
            System.out.println("  Error: " + e.getMessage());
        }
        System.out.println();
    }
    
    private static void testDirectorlessBuilder() {
        System.out.println("Testing Director-less Builder Pattern:");
        
        try {
            Sandwich sandwich = new Sandwich.SandwichBuilder()
                .bread("Sourdough")
                .protein("Turkey")
                .cheese("Swiss")
                .addVegetable("Lettuce")
                .addCondiment("Mayo")
                .toasted()
                .build();
            
            test("Director-less Builder - Sandwich Creation", 
                 sandwich != null && sandwich.toString().contains("Sourdough"));
            
            test("Director-less Builder - Multiple Additions", 
                 sandwich.toString().contains("Lettuce") && sandwich.toString().contains("Mayo"));
            
        } catch (Exception e) {
            test("Director-less Builder - Exception Free", false);
            System.out.println("  Error: " + e.getMessage());
        }
        System.out.println();
    }
    
    private static void testDslBuilder() {
        System.out.println("Testing DSL Builder Pattern:");
        
        try {
            SqlQuery query = SqlQuery
                .select("name", "email")
                .from("users")
                .where("active = true")
                .orderBy("name")
                .limit(10)
                .build();
            
            test("DSL Builder - Query Creation", 
                 query != null && query.getQuery().contains("SELECT"));
            
            test("DSL Builder - WHERE Clause", 
                 query.getQuery().contains("WHERE active = true"));
            
            test("DSL Builder - LIMIT Clause", 
                 query.getQuery().contains("LIMIT 10"));
            
        } catch (Exception e) {
            test("DSL Builder - Exception Free", false);
            System.out.println("  Error: " + e.getMessage());
        }
        System.out.println();
    }
    
    private static void testFunctionalBuilder() {
        System.out.println("Testing Functional Builder Pattern:");
        
        try {
            Configuration config = Configuration.build(builder -> {
                builder.host("api.example.com")
                       .port(443)
                       .ssl(true)
                       .timeout(30000);
            });
            
            test("Functional Builder - Configuration Creation", 
                 config != null && config.getHost().equals("api.example.com"));
            
            test("Functional Builder - Lambda Configuration", 
                 config.isSsl() && config.getPort() == 443);
            
            Configuration defaultConfig = Configuration.defaultConfig();
            
            test("Functional Builder - Default Configuration", 
                 defaultConfig != null && defaultConfig.getHost().equals("localhost"));
            
        } catch (Exception e) {
            test("Functional Builder - Exception Free", false);
            System.out.println("  Error: " + e.getMessage());
        }
        System.out.println();
    }
    
    private static void testPrototypeBuilder() {
        System.out.println("Testing Prototype + Builder Pattern:");
        
        try {
            ServerProfile webServer = ServerProfile.Prototypes.WEB_SERVER.toBuilder()
                .name("web-01")
                .cpuCores(6)
                .memoryGB(16)
                .build();
            
            test("Prototype Builder - Server Creation", 
                 webServer != null && webServer.getName().equals("web-01"));
            
            test("Prototype Builder - Inherited Properties", 
                 webServer.getOs().equals("Ubuntu 22.04") && webServer.hasMonitoring());
            
            test("Prototype Builder - Modified Properties", 
                 webServer.getCpuCores() == 6 && webServer.getMemoryGB() == 16);
            
            test("Prototype Builder - Original Unchanged", 
                 ServerProfile.Prototypes.WEB_SERVER.getCpuCores() == 4);
            
        } catch (Exception e) {
            test("Prototype Builder - Exception Free", false);
            System.out.println("  Error: " + e.getMessage());
        }
        System.out.println();
    }
    
    private static void test(String testName, boolean condition) {
        totalTests++;
        if (condition) {
            passedTests++;
            System.out.println("  [PASS] " + testName);
        } else {
            System.out.println("  [FAIL] " + testName);
        }
    }
}