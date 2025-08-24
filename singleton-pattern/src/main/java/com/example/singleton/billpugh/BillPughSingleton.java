package com.example.singleton.billpugh;

/**
 * Bill Pugh Singleton Pattern (Inner Static Helper Class)
 * 
 * Uses nested static class for lazy initialization.
 * This is considered the BEST PRACTICE for Singleton in modern Java.
 * 
 * Pros:
 * - Thread-safe (JVM classloader guarantees)
 * - Lazy initialization (created only when first used)
 * - No synchronization overhead
 * - Simple and elegant
 * - Handles exceptions during initialization
 * 
 * Cons:
 * - None significant (this is why it's recommended!)
 * 
 * How it works:
 * - The nested class is not loaded until it's referenced
 * - The JVM's class initialization is thread-safe
 * - No need for explicit synchronization
 */
public class BillPughSingleton {
    
    private String data;
    private int requestCount = 0;
    private long creationTime;
    
    private BillPughSingleton() {
        creationTime = System.currentTimeMillis();
        System.out.println("BillPughSingleton: Instance created using holder pattern at " + creationTime);
        this.data = "Bill Pugh Singleton Data";
        
        if (Math.random() < 0.0001) {
            throw new RuntimeException("Simulated initialization failure");
        }
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private static class SingletonHolder {
        private static final BillPughSingleton INSTANCE = new BillPughSingleton();
        
        static {
            System.out.println("SingletonHolder: Static block executed (class loaded)");
        }
    }
    
    public static BillPughSingleton getInstance() {
        return SingletonHolder.INSTANCE;
    }
    
    public void performOperation() {
        requestCount++;
        System.out.println("BillPugh Singleton: Performing operation #" + requestCount + 
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
        System.out.println("BillPugh Singleton State: data=" + data + 
                         ", requests=" + requestCount + 
                         ", created=" + creationTime);
    }
    
    public static void demonstrateLazyLoading() {
        System.out.println("\n=== Demonstrating Lazy Loading ===");
        System.out.println("1. Class BillPughSingleton is loaded");
        System.out.println("2. But SingletonHolder is NOT loaded yet");
        System.out.println("3. No instance created so far...\n");
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("4. Now calling getInstance()...");
        BillPughSingleton instance = getInstance();
        System.out.println("5. SingletonHolder loaded, instance created!");
        System.out.println("6. Instance hashCode: " + instance.hashCode());
    }
    
    public static void demonstrateThreadSafety() {
        System.out.println("\n=== Demonstrating Thread Safety (No Synchronization Needed) ===");
        
        Runnable task = () -> {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + " requesting instance...");
            
            long startTime = System.nanoTime();
            BillPughSingleton singleton = getInstance();
            long endTime = System.nanoTime();
            
            System.out.println(threadName + " got instance: " + singleton.hashCode() + 
                             " (took " + (endTime - startTime) / 1000 + "μs)");
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
        
        System.out.println("\nAll threads got the same instance without any synchronization!");
    }
    
    public static void explainMechanism() {
        System.out.println("\n=== How Bill Pugh Singleton Works ===");
        System.out.println("1. SingletonHolder class is NOT loaded when BillPughSingleton is loaded");
        System.out.println("2. SingletonHolder is loaded only when getInstance() is called");
        System.out.println("3. JVM guarantees class initialization is thread-safe");
        System.out.println("4. INSTANCE is created when SingletonHolder is initialized");
        System.out.println("5. Subsequent calls return the same INSTANCE");
        System.out.println("\nBenefits:");
        System.out.println("- No synchronization overhead");
        System.out.println("- Lazy initialization");
        System.out.println("- Thread-safe by JVM guarantee");
        System.out.println("- Simple and clean code");
    }
}