package com.example.registry.typesafe;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Type-Safe Registry Pattern Implementation
 * 
 * This registry uses Class objects as keys instead of strings, providing compile-time
 * type safety and eliminating the need for manual casting. The registry leverages
 * Java's generics system to ensure type correctness.
 * 
 * Key Characteristics:
 * - Uses Class<T> objects as keys instead of strings
 * - Provides compile-time type safety
 * - No manual casting required by clients
 * - Eliminates ClassCastException risks
 * - Thread-safe using ConcurrentHashMap
 */
public class TypeSafeRegistry {
    
    // Singleton instance
    private static final TypeSafeRegistry INSTANCE = new TypeSafeRegistry();
    
    // Storage using Class as key and Object as value
    // We use Object as the value type and rely on the register method's generics
    // to ensure type safety
    private final Map<Class<?>, Object> registry;
    
    // Private constructor
    private TypeSafeRegistry() {
        registry = new ConcurrentHashMap<>();
    }
    
    /**
     * Gets the singleton instance of the type-safe registry.
     * @return the registry instance
     */
    public static TypeSafeRegistry getInstance() {
        return INSTANCE;
    }
    
    /**
     * Registers an instance with its Class type as the key.
     * The generics ensure that the instance is of the type specified by the Class key.
     * 
     * @param <T> the type of the instance
     * @param type the Class object representing the type
     * @param instance the instance to register
     * @throws IllegalArgumentException if type or instance is null
     */
    public <T> void register(Class<T> type, T instance) {
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }
        if (instance == null) {
            throw new IllegalArgumentException("Instance cannot be null");
        }
        
        System.out.println("Registering instance of type: " + type.getSimpleName() + 
                         " -> " + instance.getClass().getSimpleName());
        registry.put(type, instance);
    }
    
    /**
     * Retrieves an instance by its Class type.
     * The return type is automatically inferred from the Class parameter,
     * providing compile-time type safety without manual casting.
     * 
     * @param <T> the type to retrieve
     * @param type the Class object representing the type
     * @return the instance of the specified type, or null if not found
     */
    public <T> T get(Class<T> type) {
        if (type == null) {
            return null;
        }
        
        Object instance = registry.get(type);
        if (instance == null) {
            System.err.println("Warning: No instance found for type: " + type.getSimpleName());
            return null;
        }
        
        // This cast is safe because the register method guarantees type correctness
        return type.cast(instance);
    }
    
    /**
     * Checks if an instance is registered for the given type.
     * 
     * @param type the Class type to check
     * @return true if an instance is registered, false otherwise
     */
    public boolean hasType(Class<?> type) {
        return type != null && registry.containsKey(type);
    }
    
    /**
     * Unregisters an instance for the given type.
     * 
     * @param <T> the type to unregister
     * @param type the Class type to remove
     * @return the removed instance, or null if not found
     */
    @SuppressWarnings("unchecked")
    public <T> T unregister(Class<T> type) {
        if (type == null) {
            return null;
        }
        
        Object removed = registry.remove(type);
        if (removed != null) {
            System.out.println("Unregistered instance of type: " + type.getSimpleName());
            return (T) removed; // Safe cast due to register method's type guarantee
        }
        return null;
    }
    
    /**
     * Gets the number of registered instances.
     * 
     * @return the count of registered instances
     */
    public int getRegisteredCount() {
        return registry.size();
    }
    
    /**
     * Clears all registered instances.
     */
    public void clear() {
        System.out.println("Clearing all registered instances");
        registry.clear();
    }
    
    /**
     * Gets all registered types.
     * Useful for debugging or introspection.
     * 
     * @return a set of all registered Class types
     */
    public java.util.Set<Class<?>> getRegisteredTypes() {
        return new java.util.HashSet<>(registry.keySet());
    }
}