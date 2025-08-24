package com.example.singleton.enums;

/**
 * Enum Singleton Pattern
 * 
 * The EASIEST and SAFEST way to implement Singleton in Java.
 * 
 * Pros:
 * - Thread-safe by default
 * - Serialization-safe (no need for readResolve)
 * - Reflection-proof (cannot create instances via reflection)
 * - Simple and concise syntax
 * - Prevents multiple instantiation even in sophisticated attacks
 * 
 * Cons:
 * - Cannot extend other classes (enums implicitly extend java.lang.Enum)
 * - Not suitable if you need inheritance
 * - Cannot be lazily initialized with parameters
 * - Slightly less intuitive for developers unfamiliar with the pattern
 */
public enum EnumSingleton {
    INSTANCE;
    
    private String data;
    private int requestCount;
    private final long creationTime;
    
    EnumSingleton() {
        creationTime = System.currentTimeMillis();
        System.out.println("EnumSingleton: Instance created at " + creationTime);
        this.data = "Enum Singleton Data";
        this.requestCount = 0;
    }
    
    public void performOperation() {
        requestCount++;
        System.out.println("EnumSingleton: Performing operation #" + requestCount + 
                         " by " + Thread.currentThread().getName());
    }
    
    public String getData() {
        return data;
    }
    
    public void setData(String data) {
        this.data = data;
    }
    
    public int getRequestCount() {
        return requestCount;
    }
    
    public long getCreationTime() {
        return creationTime;
    }
    
    public void showState() {
        System.out.println("EnumSingleton State: data=" + data + 
                         ", requests=" + requestCount + 
                         ", created=" + creationTime);
    }
    
    public void doBusinessLogic() {
        System.out.println("Executing business logic in EnumSingleton");
        System.out.println("Current data: " + data);
    }
    
    public static void demonstrateUsage() {
        System.out.println("\n=== Demonstrating Enum Singleton Usage ===");
        
        EnumSingleton singleton1 = EnumSingleton.INSTANCE;
        singleton1.setData("First Access");
        singleton1.performOperation();
        
        EnumSingleton singleton2 = EnumSingleton.INSTANCE;
        singleton2.performOperation();
        
        System.out.println("singleton1 == singleton2: " + (singleton1 == singleton2));
        System.out.println("Data from singleton2: " + singleton2.getData());
    }
    
    public static void demonstrateThreadSafety() {
        System.out.println("\n=== Demonstrating Thread Safety ===");
        
        Runnable task = () -> {
            EnumSingleton singleton = EnumSingleton.INSTANCE;
            singleton.performOperation();
            System.out.println(Thread.currentThread().getName() + 
                             " - Instance hash: " + singleton.hashCode());
        };
        
        Thread[] threads = new Thread[5];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(task, "Thread-" + (i + 1));
            threads[i].start();
        }
        
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    public static void demonstrateReflectionSafety() {
        System.out.println("\n=== Demonstrating Reflection Safety ===");
        System.out.println("Attempting to create instance via reflection...");
        
        try {
            Class<?> clazz = EnumSingleton.class;
            java.lang.reflect.Constructor<?>[] constructors = clazz.getDeclaredConstructors();
            for (java.lang.reflect.Constructor<?> constructor : constructors) {
                System.out.println("Found constructor: " + constructor);
                constructor.setAccessible(true);
                
                System.out.println("Trying to invoke constructor...");
                Object newInstance = constructor.newInstance("INSTANCE", 0);
                System.out.println("Success! Created: " + newInstance);
            }
        } catch (Exception e) {
            System.out.println("Failed! " + e.getClass().getSimpleName() + ": " + e.getMessage());
            System.out.println("Enum singleton is protected against reflection attacks!");
        }
    }
    
    public static void explainEnumSingleton() {
        System.out.println("\n=== Why Enum Singleton is Recommended ===");
        System.out.println("1. Simplicity: Just 'public enum Singleton { INSTANCE; }'");
        System.out.println("2. Thread Safety: JVM handles it during class loading");
        System.out.println("3. Serialization: Automatically handles serialization correctly");
        System.out.println("4. Reflection Protection: Cannot create instances via reflection");
        System.out.println("5. Clone Protection: Enums cannot be cloned");
        System.out.println("\nJoshua Bloch (Effective Java) says:");
        System.out.println("'A single-element enum type is the best way to implement a singleton'");
    }
}