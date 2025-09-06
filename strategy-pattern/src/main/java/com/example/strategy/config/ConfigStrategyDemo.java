package com.example.strategy.config;

import java.io.IOException;
import java.util.*;

public class ConfigStrategyDemo {
    public static void main(String[] args) {
        System.out.println("=== Config-Driven Strategy Pattern Demo ===\n");
        
        // Create sample report request
        ReportRequest request = new ReportRequest(
            "RPT-2024-001",
            "Sales Report",
            Arrays.asList("customer_name", "email", "order_id", "amount", "status", "order_date"),
            new Date(System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000), // 30 days ago
            new Date(),
            "john.doe@example.com"
        );
        
        System.out.println("Report Request: " + request);
        System.out.println();
        
        // Initialize the configurable strategy manager
        ConfigurableStrategyManager<ReportRequest, ReportResult> manager = 
            new ConfigurableStrategyManager<>();
        
        // 1. Register strategies with default configurations
        System.out.println("1. Registering Strategies with Default Configurations:");
        System.out.println("-----------------------------------------------------");
        
        // CSV Strategy with default config
        StrategyConfig csvConfig = new StrategyConfig()
            .set("delimiter", ",")
            .set("include_headers", true)
            .set("max_rows", 100);
        
        manager.registerStrategy("csv", ReportGenerationStrategies.CSV_GENERATOR, csvConfig);
        
        // JSON Strategy with default config
        StrategyConfig jsonConfig = new StrategyConfig()
            .set("pretty_print", true)
            .set("include_metadata", true)
            .set("max_records", 50);
        
        manager.registerStrategy("json", ReportGenerationStrategies.JSON_GENERATOR, jsonConfig);
        
        // HTML Strategy with default config
        StrategyConfig htmlConfig = new StrategyConfig()
            .set("theme", "bootstrap")
            .set("include_css", true)
            .set("title", "Sales Report")
            .set("show_row_numbers", true);
        
        manager.registerStrategy("html", ReportGenerationStrategies.HTML_GENERATOR, htmlConfig);
        
        // XML Strategy with default config
        StrategyConfig xmlConfig = new StrategyConfig()
            .set("include_xml_declaration", true)
            .set("root_element", "sales_report")
            .set("record_element", "sale")
            .set("pretty_format", true);
        
        manager.registerStrategy("xml", ReportGenerationStrategies.XML_GENERATOR, xmlConfig);
        
        // Summary Strategy with default config
        StrategyConfig summaryConfig = new StrategyConfig()
            .set("summary_type", "detailed")
            .set("include_statistics", true)
            .set("include_charts", false);
        
        manager.registerStrategy("summary", ReportGenerationStrategies.SUMMARY_GENERATOR, summaryConfig);
        
        System.out.println(manager.getSummary());
        
        // 2. Execute strategies with default configurations
        System.out.println("2. Executing Strategies with Default Configurations:");
        System.out.println("----------------------------------------------------");
        
        for (String strategyName : Arrays.asList("csv", "json", "html", "summary")) {
            ReportResult result = manager.execute(strategyName, request);
            System.out.println(strategyName.toUpperCase() + " Result: " + result);
            if (result.isSuccess()) {
                String preview = result.getContent().length() > 100 ? 
                    result.getContent().substring(0, 100) + "..." : result.getContent();
                System.out.println("  Content Preview: " + preview.replaceAll("\n", " "));
            }
            System.out.println();
        }
        
        // 3. Runtime configuration updates
        System.out.println("3. Runtime Configuration Updates:");
        System.out.println("---------------------------------");
        
        // Update CSV configuration
        manager.updateConfigProperty("csv", "delimiter", ";");
        manager.updateConfigProperty("csv", "include_headers", false);
        manager.updateConfigProperty("csv", "max_rows", 20);
        
        System.out.println("Updated CSV configuration - using semicolon delimiter, no headers, max 20 rows");
        ReportResult updatedCsvResult = manager.execute("csv", request);
        System.out.println("Updated CSV Result: " + updatedCsvResult);
        System.out.println();
        
        // Update JSON configuration
        manager.updateConfigProperty("json", "pretty_print", false);
        manager.updateConfigProperty("json", "root_element", "sales_data");
        
        System.out.println("Updated JSON configuration - compact format, different root element");
        ReportResult updatedJsonResult = manager.execute("json", request);
        System.out.println("Updated JSON Result: " + updatedJsonResult);
        System.out.println();
        
        // 4. Configuration loading from string (simulating file loading)
        System.out.println("4. Loading Configuration from External Source:");
        System.out.println("---------------------------------------------");
        
        String htmlConfigString = 
            "theme=default\n" +
            "include_css=false\n" +
            "title=Custom Sales Report\n" +
            "show_row_numbers=false\n" +
            "max_rows=15";
        
        try {
            manager.loadConfigurationFromString("html", htmlConfigString);
            System.out.println("Loaded HTML configuration from external source");
            
            ReportResult configuredHtmlResult = manager.execute("html", request);
            System.out.println("Configured HTML Result: " + configuredHtmlResult);
        } catch (IOException e) {
            System.out.println("Failed to load configuration: " + e.getMessage());
        }
        System.out.println();
        
        // 5. Custom configuration per execution
        System.out.println("5. Custom Configuration Per Execution:");
        System.out.println("-------------------------------------");
        
        // Execute XML strategy with custom configuration without changing default
        StrategyConfig customXmlConfig = new StrategyConfig()
            .set("root_element", "custom_report")
            .set("record_element", "data_record")
            .set("pretty_format", false)
            .set("include_xml_declaration", false);
        
        ReportResult customXmlResult = manager.execute("xml", request, customXmlConfig);
        System.out.println("Custom XML Result: " + customXmlResult);
        System.out.println("XML content starts with: " + 
                          customXmlResult.getContent().substring(0, Math.min(50, customXmlResult.getContent().length())));
        System.out.println();
        
        System.out.println("\n=== Demo Complete ===");
    }
}