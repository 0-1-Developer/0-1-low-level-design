package com.example.factorymethod.reflection;

import com.example.factorymethod.shared.Document;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReflectionDocumentFactory {
    
    private static final Map<String, Class<? extends Document>> typeRegistry = 
        new ConcurrentHashMap<>();
    
    private static final Map<String, Document> singletonCache = 
        new ConcurrentHashMap<>();
    
    static {
        registerDefaultTypes();
    }
    
    private static void registerDefaultTypes() {
        try {
            registerType("text", "com.example.factorymethod.shared.TextDocument");
            registerType("pdf", "com.example.factorymethod.shared.PdfDocument");
            registerType("word", "com.example.factorymethod.shared.WordDocument");
            registerType("html", "com.example.factorymethod.shared.HtmlDocument");
            registerType("xml", "com.example.factorymethod.shared.XmlDocument");
        } catch (ClassNotFoundException e) {
            System.err.println("⚠️  Error registering default types: " + e.getMessage());
        }
    }
    
    public static void registerType(String type, String className) throws ClassNotFoundException {
        Class<?> clazz = Class.forName(className);
        if (!Document.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("Class " + className + " must extend Document");
        }
        
        @SuppressWarnings("unchecked")
        Class<? extends Document> documentClass = (Class<? extends Document>) clazz;
        typeRegistry.put(type.toLowerCase(), documentClass);
        
        System.out.println("📝 Registered type '" + type + "' -> " + className);
    }
    
    public static void registerType(String type, Class<? extends Document> documentClass) {
        typeRegistry.put(type.toLowerCase(), documentClass);
        System.out.println("📝 Registered type '" + type + "' -> " + documentClass.getSimpleName());
    }
    
    public static Document createDocument(String type, String title) throws ReflectionFactoryException {
        String normalizedType = type.toLowerCase();
        Class<? extends Document> documentClass = typeRegistry.get(normalizedType);
        
        if (documentClass == null) {
            throw new ReflectionFactoryException("No class registered for type: " + type);
        }
        
        try {
            System.out.println("🏭 Reflection factory creating " + type + " document using " + documentClass.getSimpleName());
            Constructor<? extends Document> constructor = documentClass.getConstructor(String.class);
            return constructor.newInstance(title);
            
        } catch (Exception e) {
            throw new ReflectionFactoryException("Failed to create document of type " + type + 
                ": " + e.getMessage(), e);
        }
    }
    
    public static Document createSingletonDocument(String type, String title) throws ReflectionFactoryException {
        String cacheKey = type.toLowerCase() + "_" + title;
        return singletonCache.computeIfAbsent(cacheKey, key -> {
            try {
                System.out.println("🏭 Creating singleton document via reflection: " + type);
                return createDocument(type, title);
            } catch (ReflectionFactoryException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    public static Document createDocumentByClassName(String className, String title) throws ReflectionFactoryException {
        try {
            System.out.println("🏭 Creating document by class name: " + className);
            Class<?> clazz = Class.forName(className);
            
            if (!Document.class.isAssignableFrom(clazz)) {
                throw new ReflectionFactoryException("Class " + className + " must extend Document");
            }
            
            @SuppressWarnings("unchecked")
            Class<? extends Document> documentClass = (Class<? extends Document>) clazz;
            Constructor<? extends Document> constructor = documentClass.getConstructor(String.class);
            return constructor.newInstance(title);
            
        } catch (Exception e) {
            throw new ReflectionFactoryException("Failed to create document from class " + className + 
                ": " + e.getMessage(), e);
        }
    }
    
    public static boolean isTypeRegistered(String type) {
        return typeRegistry.containsKey(type.toLowerCase());
    }
    
    public static void unregisterType(String type) {
        Class<? extends Document> removed = typeRegistry.remove(type.toLowerCase());
        if (removed != null) {
            System.out.println("🗑️  Unregistered type: " + type);
        }
    }
    
    public static void clearCache() {
        singletonCache.clear();
        System.out.println("🧹 Singleton cache cleared");
    }
    
    public static void clearRegistry() {
        typeRegistry.clear();
        singletonCache.clear();
        System.out.println("🧹 Registry and cache cleared");
        registerDefaultTypes();
    }
    
    public static class ReflectionFactoryException extends Exception {
        public ReflectionFactoryException(String message) {
            super(message);
        }
        
        public ReflectionFactoryException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}