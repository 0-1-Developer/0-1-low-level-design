package com.example.factorymethod.registrybacked;

import com.example.factorymethod.shared.*;

public class PluginManager {
    
    public static void initializeCorePlugins() {
        System.out.println("🔌 Initializing core document plugins...");
        
        DocumentFactoryRegistry.registerFactory("text", TextDocument::new);
        DocumentFactoryRegistry.registerFactory("txt", TextDocument::new);
        
        DocumentFactoryRegistry.registerFactory("pdf", PdfDocument::new);
        
        DocumentFactoryRegistry.registerFactory("word", WordDocument::new);
        DocumentFactoryRegistry.registerFactory("docx", WordDocument::new);
        DocumentFactoryRegistry.registerFactory("doc", WordDocument::new);
        
        DocumentFactoryRegistry.registerFactory("html", HtmlDocument::new);
        DocumentFactoryRegistry.registerFactory("htm", HtmlDocument::new);
        
        DocumentFactoryRegistry.registerFactory("xml", XmlDocument::new);
        
        System.out.println("✅ Core plugins initialized. Registered types: " + 
            DocumentFactoryRegistry.getRegisteredTypes());
        System.out.println();
    }
    
    public static void loadAdvancedPlugins() {
        System.out.println("🚀 Loading advanced document plugins...");
        
        // Simulated plugin loading for advanced document types
        DocumentFactoryRegistry.registerFactory("rtf", title -> new CustomDocument(title, "RTF"));
        DocumentFactoryRegistry.registerFactory("odt", title -> new CustomDocument(title, "ODT"));
        DocumentFactoryRegistry.registerFactory("csv", title -> new CustomDocument(title, "CSV"));
        DocumentFactoryRegistry.registerFactory("json", title -> new CustomDocument(title, "JSON"));
        
        System.out.println("✅ Advanced plugins loaded. Total registered types: " + 
            DocumentFactoryRegistry.getRegisteredFactoryCount());
        System.out.println();
    }
    
    public static void unloadPlugin(String pluginType) {
        System.out.println("🔌 Unloading plugin: " + pluginType);
        DocumentFactoryRegistry.unregisterFactory(pluginType);
    }
    
    static class CustomDocument extends Document {
        private final String documentFormat;
        
        public CustomDocument(String title, String format) {
            super(title);
            this.documentFormat = format;
        }
        
        @Override
        public void open() {
            System.out.println("📄 Opening " + documentFormat + " document: " + title);
        }
        
        @Override
        public void save() {
            System.out.println("💾 Saving " + documentFormat + " document: " + title + "." + documentFormat.toLowerCase());
        }
        
        @Override
        public void close() {
            System.out.println("🔐 Closing " + documentFormat + " document: " + title);
        }
        
        @Override
        public String getDocumentType() {
            return documentFormat;
        }
    }
}