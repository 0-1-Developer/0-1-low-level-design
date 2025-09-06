package com.example.builder.nested;

import java.util.HashMap;
import java.util.Map;

public class NestedBuilderDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Nested/Inner Builder Pattern Demo ===\n");
        
        System.out.println("1. Building a PostgreSQL connection with default settings:");
        
        DatabaseConnection pgConnection = DatabaseConnection.builder()
            .host("db.example.com")
            .port(5432)
            .database("ecommerce")
            .username("app_user")
            .password("secure_password123")
            .driver("postgresql")
            .build();
        
        System.out.println("PostgreSQL connection:");
        System.out.println(pgConnection);
        System.out.println("Connection string: " + pgConnection.getConnectionString());
        System.out.println();
        
        System.out.println("2. Building a MySQL connection with custom settings:");
        
        Map<String, String> customProps = new HashMap<>();
        customProps.put("useSSL", "true");
        customProps.put("serverTimezone", "UTC");
        customProps.put("cachePrepStmts", "true");
        
        DatabaseConnection mysqlConnection = DatabaseConnection.builder()
            .host("mysql-cluster.internal")
            .port(3306)
            .database("user_management")
            .username("mysql_user")
            .password("mysql_pass_456")
            .driver("mysql")
            .connectionTimeout(45000)
            .readTimeout(30000)
            .maxPoolSize(25)
            .autoCommit(false)
            .isolationLevel("REPEATABLE_READ")
            .properties(customProps)
            .property("useUnicode", "true")
            .property("characterEncoding", "utf8")
            .build();
        
        System.out.println("MySQL connection:");
        System.out.println(mysqlConnection);
        System.out.println("Connection string: " + mysqlConnection.getConnectionString());
        System.out.println("Additional properties: " + mysqlConnection.getAdditionalProperties());
        System.out.println();
        
        System.out.println("3. Building a local development connection:");
        
        DatabaseConnection devConnection = DatabaseConnection.builder()
            .database("dev_db")
            .username("dev")
            .password("dev123")
            .maxPoolSize(5)
            .property("logging", "true")
            .build();
        
        System.out.println("Development connection:");
        System.out.println(devConnection);
        System.out.println("Connection string: " + devConnection.getConnectionString());
        System.out.println();
        
        System.out.println("4. Demonstrating validation:");
        
        try {
            DatabaseConnection invalidConnection = DatabaseConnection.builder()
                .host("some-host")
                .port(5432)
                .username("user")
                .password("pass")
                .build();
        } catch (IllegalStateException e) {
            System.out.println("✓ Validation caught missing database: " + e.getMessage());
        }
        
        try {
            DatabaseConnection invalidConnection = DatabaseConnection.builder()
                .database("test_db")
                .username("user")
                .password("pass")
                .port(70000)
                .build();
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Validation caught invalid port: " + e.getMessage());
        }
        
        try {
            DatabaseConnection invalidConnection = DatabaseConnection.builder()
                .database("test_db")
                .username("user")
                .password("pass")
                .isolationLevel("INVALID_LEVEL")
                .build();
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Validation caught invalid isolation level: " + e.getMessage());
        }
        
        System.out.println();
        System.out.println("Key Benefits of Nested Builder:");
        System.out.println("- Clean, idiomatic API: Product.builder().method().build()");
        System.out.println("- Builder is tightly coupled with the product class");
        System.out.println("- No need for separate builder class files");
        System.out.println("- Builder has access to private constructors");
        System.out.println("- Encapsulation: builder is part of the product's interface");
        System.out.println("- Easy to discover via IDE auto-complete");
        System.out.println("- Validation can be comprehensive before object creation");
        
        System.out.println("\n=== Demo Complete ===");
    }
}