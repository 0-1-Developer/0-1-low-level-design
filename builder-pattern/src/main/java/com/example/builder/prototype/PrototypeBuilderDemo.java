package com.example.builder.prototype;

public class PrototypeBuilderDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Prototype + Builder Pattern Demo ===");
        System.out.println("Start from prototypes and modify specific attributes using builders\n");
        
        System.out.println("1. Available prototype templates:");
        System.out.println("Web Server: " + ServerProfile.Prototypes.WEB_SERVER);
        System.out.println("Database Server: " + ServerProfile.Prototypes.DATABASE_SERVER);
        System.out.println("Development Server: " + ServerProfile.Prototypes.DEVELOPMENT_SERVER);
        System.out.println();
        
        System.out.println("2. Creating production web servers from prototype:");
        ServerProfile webServer1 = ServerProfile.Prototypes.WEB_SERVER.toBuilder()
            .name("web-01.prod")
            .cpuCores(6)
            .memoryGB(16)
            .environmentVariable("SERVER_NAME", "web-01")
            .withBackup()
            .build();
        
        System.out.println("Production Web Server 1: " + webServer1);
        
        ServerProfile webServer2 = ServerProfile.Prototypes.WEB_SERVER.toBuilder()
            .name("web-02.prod")
            .cpuCores(6)
            .memoryGB(16)
            .environmentVariable("SERVER_NAME", "web-02")
            .withBackup()
            .build();
        
        System.out.println("Production Web Server 2: " + webServer2);
        System.out.println();
        
        System.out.println("3. Creating development databases from database prototype:");
        ServerProfile devDB = ServerProfile.Prototypes.DATABASE_SERVER.toBuilder()
            .name("dev-mysql-01")
            .cpuCores(4)
            .memoryGB(8)
            .storageGB(100)
            .networkConfig("internal")
            .environmentVariable("MYSQL_ROOT_PASSWORD", "dev_password")
            .environmentVariable("MYSQL_DATABASE", "dev_db")
            .build();
        
        System.out.println("Development Database: " + devDB);
        System.out.println();
        
        System.out.println("4. Creating test environments from development prototype:");
        ServerProfile testServer1 = ServerProfile.Prototypes.DEVELOPMENT_SERVER.toBuilder()
            .name("test-api-server")
            .environmentVariable("NODE_ENV", "test")
            .environmentVariable("API_PORT", "3001")
            .build();
        
        ServerProfile testServer2 = ServerProfile.Prototypes.DEVELOPMENT_SERVER.toBuilder()
            .name("test-frontend-server")
            .environmentVariable("NODE_ENV", "test")
            .environmentVariable("REACT_PORT", "3000")
            .build();
        
        System.out.println("Test API Server: " + testServer1);
        System.out.println("Test Frontend Server: " + testServer2);
        System.out.println();
        
        System.out.println("5. Creating completely new prototype for microservices:");
        ServerProfile microserviceTemplate = ServerProfile.builder()
            .name("microservice-template")
            .os("Alpine Linux")
            .cpuCores(1)
            .memoryGB(2)
            .storageGB(20)
            .networkConfig("container")
            .environmentVariable("CONTAINER_TYPE", "microservice")
            .environmentVariable("HEALTH_CHECK_INTERVAL", "30s")
            .withMonitoring()
            .build();
        
        System.out.println("New Microservice Template: " + microserviceTemplate);
        
        ServerProfile userService = microserviceTemplate.toBuilder()
            .name("user-service")
            .environmentVariable("SERVICE_NAME", "user-service")
            .environmentVariable("DATABASE_URL", "mysql://user-db:3306/users")
            .build();
        
        ServerProfile orderService = microserviceTemplate.toBuilder()
            .name("order-service")
            .memoryGB(4)
            .environmentVariable("SERVICE_NAME", "order-service")
            .environmentVariable("DATABASE_URL", "mysql://order-db:3306/orders")
            .build();
        
        System.out.println("User Service: " + userService);
        System.out.println("Order Service: " + orderService);
        System.out.println();
        
        System.out.println("Key Benefits of Prototype + Builder:");
        System.out.println("- Efficient creation of similar objects");
        System.out.println("- Reuse common configurations as templates");
        System.out.println("- Modify only specific attributes from base");
        System.out.println("- Faster than building from scratch");
        System.out.println("- Consistent base configurations");
        System.out.println("- Easy to create families of related objects");
        System.out.println("- Combines benefits of both patterns");
        
        System.out.println("\n=== Demo Complete ===");
    }
}