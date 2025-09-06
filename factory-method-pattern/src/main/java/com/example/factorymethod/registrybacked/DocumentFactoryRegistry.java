package com.example.factorymethod.registrybacked;

import com.example.factorymethod.shared.Document;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.Set;

public class DocumentFactoryRegistry {
    
    private static final Map<String, Function<String, Document>> factoryRegistry = 
        new ConcurrentHashMap<>();
    
    public static void registerFactory(String type, Function<String, Document> factory) {
        System.out.println("📝 Registering factory for type: " + type);
        factoryRegistry.put(type.toLowerCase(), factory);
    }
    
    public static Document createDocument(String type, String title) {
        String normalizedType = type.toLowerCase();
        Function<String, Document> factory = factoryRegistry.get(normalizedType);
        
        if (factory == null) {
            throw new IllegalArgumentException("No factory registered for type: " + type + 
                ". Available types: " + getRegisteredTypes());
        }
        
        System.out.println("🏭 Registry-backed factory creating " + type + " document...");
        return factory.apply(title);
    }
    
    public static boolean isTypeSupported(String type) {
        return factoryRegistry.containsKey(type.toLowerCase());
    }
    
    public static Set<String> getRegisteredTypes() {
        return factoryRegistry.keySet();
    }
    
    public static void unregisterFactory(String type) {
        String removedType = factoryRegistry.remove(type.toLowerCase()) != null ? type : null;
        if (removedType != null) {
            System.out.println("🗑️  Unregistered factory for type: " + type);
        } else {
            System.out.println("⚠️  No factory was registered for type: " + type);
        }
    }
    
    public static void clearRegistry() {
        factoryRegistry.clear();
        System.out.println("🧹 Factory registry cleared");
    }
    
    public static int getRegisteredFactoryCount() {
        return factoryRegistry.size();
    }
    
    private DocumentFactoryRegistry() {
        // Private constructor to prevent instantiation
    }
}