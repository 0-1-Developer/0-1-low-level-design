package com.example.builder.functional;

public class FunctionalBuilderDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Functional Builder Pattern Demo (Java 8+) ===");
        System.out.println("Using lambdas and functional interfaces for configuration\n");
        
        System.out.println("1. Building with lambda configuration:");
        Configuration webConfig = Configuration.build(builder -> {
            builder.host("api.example.com")
                   .port(443)
                   .ssl(true)
                   .timeout(60000)
                   .retries(5)
                   .setting("compression", "gzip")
                   .setting("keepAlive", "true");
        });
        
        System.out.println("Web config: " + webConfig);
        System.out.println();
        
        System.out.println("2. Building with method reference style:");
        Configuration devConfig = Configuration.build(FunctionalBuilderDemo::configureDevelopment);
        
        System.out.println("Dev config: " + devConfig);
        System.out.println();
        
        System.out.println("3. Building with composed configuration functions:");
        Configuration prodConfig = Configuration.build(
            configureSsl()
                .andThen(configureProduction())
                .andThen(configureLogging())
        );
        
        System.out.println("Prod config: " + prodConfig);
        System.out.println();
        
        System.out.println("4. Building with default configuration:");
        Configuration defaultConfig = Configuration.defaultConfig();
        
        System.out.println("Default config: " + defaultConfig);
        System.out.println();
        
        System.out.println("5. Building with conditional configuration:");
        boolean isProduction = true;
        Configuration conditionalConfig = Configuration.build(builder -> {
            builder.host("app.domain.com")
                   .port(8080);
            
            if (isProduction) {
                builder.ssl(true)
                       .timeout(30000)
                       .retries(5)
                       .setting("logLevel", "ERROR");
            } else {
                builder.ssl(false)
                       .timeout(5000)
                       .retries(1)
                       .setting("logLevel", "DEBUG");
            }
        });
        
        System.out.println("Conditional config: " + conditionalConfig);
        System.out.println();
        
        System.out.println("Key Benefits of Functional Builder:");
        System.out.println("- Concise lambda-based configuration");
        System.out.println("- Composable configuration functions");
        System.out.println("- Easy to create configuration templates");
        System.out.println("- Supports conditional logic within builders");
        System.out.println("- Functional programming paradigm");
        System.out.println("- Clean separation of configuration logic");
        
        System.out.println("\n=== Demo Complete ===");
    }
    
    private static void configureDevelopment(Configuration.Builder builder) {
        builder.host("localhost")
               .port(3000)
               .ssl(false)
               .timeout(10000)
               .setting("debug", "true")
               .setting("hotReload", "true");
    }
    
    private static java.util.function.Consumer<Configuration.Builder> configureSsl() {
        return builder -> builder.ssl(true).port(443);
    }
    
    private static java.util.function.Consumer<Configuration.Builder> configureProduction() {
        return builder -> builder.host("prod.example.com")
                                 .timeout(30000)
                                 .retries(3);
    }
    
    private static java.util.function.Consumer<Configuration.Builder> configureLogging() {
        return builder -> builder.setting("logLevel", "INFO")
                                 .setting("logFile", "/var/log/app.log");
    }
}