package com.example.factorymethod.registrybacked;

import com.example.factorymethod.shared.Document;
import java.util.Arrays;
import java.util.List;

public class RegistryBackedFactoryDemo {
    
    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("🏭 FACTORY METHOD PATTERN - REGISTRY-BACKED DEMO");
        System.out.println("=".repeat(70));
        System.out.println();
        
        System.out.println("📋 This demo shows registry-backed factory methods with:");
        System.out.println("   • Dynamic registration of factory functions");
        System.out.println("   • Plugin-like architecture for extensibility");
        System.out.println("   • Runtime factory discovery and management");
        System.out.println("   • Useful for modular and plugin-based systems");
        System.out.println();
        
        // Initialize core plugins
        PluginManager.initializeCorePlugins();
        
        // Test core functionality
        System.out.println("📄 TESTING CORE DOCUMENT TYPES:");
        List<String> coreTypes = Arrays.asList("text", "pdf", "word", "html", "xml");
        for (String type : coreTypes) {
            if (DocumentFactoryRegistry.isTypeSupported(type)) {
                Document doc = DocumentFactoryRegistry.createDocument(type, "Core " + type.toUpperCase());
                processDocument(doc, "Standard content for " + type + " document");
            }
        }
        
        // Test aliases
        System.out.println("🔄 TESTING TYPE ALIASES:");
        List<String> aliases = Arrays.asList("txt", "docx", "htm");
        for (String alias : aliases) {
            Document doc = DocumentFactoryRegistry.createDocument(alias, "Alias " + alias.toUpperCase());
            processDocument(doc, "Content using alias: " + alias);
        }
        
        // Load advanced plugins
        PluginManager.loadAdvancedPlugins();
        
        // Test advanced functionality
        System.out.println("🚀 TESTING ADVANCED PLUGIN TYPES:");
        List<String> advancedTypes = Arrays.asList("rtf", "odt", "csv", "json");
        for (String type : advancedTypes) {
            Document doc = DocumentFactoryRegistry.createDocument(type, "Advanced " + type.toUpperCase());
            processDocument(doc, "Advanced plugin content for " + type);
        }
        
        // Test error handling
        System.out.println("⚠️  TESTING ERROR HANDLING:");
        try {
            DocumentFactoryRegistry.createDocument("unsupported", "Unknown Type");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Expected error: " + e.getMessage());
            System.out.println();
        }
        
        // Dynamic plugin management
        System.out.println("🔧 DYNAMIC PLUGIN MANAGEMENT:");
        System.out.println("📊 Currently registered types: " + DocumentFactoryRegistry.getRegisteredTypes());
        
        PluginManager.unloadPlugin("csv");
        System.out.println("📊 Types after unloading CSV: " + DocumentFactoryRegistry.getRegisteredTypes());
        
        // Custom runtime registration
        System.out.println("🔗 CUSTOM RUNTIME REGISTRATION:");
        DocumentFactoryRegistry.registerFactory("custom", title -> 
            new PluginManager.CustomDocument(title, "CUSTOM"));
        
        Document customDoc = DocumentFactoryRegistry.createDocument("custom", "Runtime Custom");
        processDocument(customDoc, "Dynamically registered custom document");
        
        // Cleanup
        DocumentFactoryRegistry.clearRegistry();
        
        System.out.println("🎯 KEY BENEFITS:");
        System.out.println("   ✅ Runtime extensibility through plugin registration");
        System.out.println("   ✅ Decoupled factory management from client code");
        System.out.println("   ✅ Support for type aliases and multiple names");
        System.out.println("   ✅ Dynamic loading/unloading of factory implementations");
        System.out.println("   ✅ Perfect for plugin architectures and modular systems");
        System.out.println();
        
        System.out.println("⚠️  CONSIDERATIONS:");
        System.out.println("   • Requires initialization of registry before use");
        System.out.println("   • Runtime errors for unregistered types");
        System.out.println("   • Global registry state can complicate testing");
        System.out.println("   • Need careful management of plugin lifecycles");
    }
    
    private static void processDocument(Document document, String content) {
        document.open();
        document.setContent(content);
        document.save();
        document.close();
        System.out.println("✅ Processed " + document.getDocumentType() + " document: " + document.getTitle());
        System.out.println();
    }
}