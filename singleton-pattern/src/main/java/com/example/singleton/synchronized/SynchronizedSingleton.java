package com.example.singleton.synchronizedversion;

/**
 * Synchronized Method Singleton Pattern (Thread-Safe Lazy)
 * 
 * Uses synchronized keyword to ensure thread safety.
 * 
 * Pros:
 * - Thread-safe
 * - Lazy initialization
 * - Simple to understand
 * 
 * Cons:
 * - Synchronization adds performance cost for EVERY call to getInstance()
 * - Even after instance is created, threads must acquire lock
 * - Can become a bottleneck in high-concurrency scenarios
 */
public class SynchronizedSingleton {
    
    private static SynchronizedSingleton instance;
    
    private String data;
    private int requestCount = 0;
    private long creationTime;
    
    private SynchronizedSingleton() {
        creationTime = System.currentTimeMillis();
        System.out.println("SynchronizedSingleton: Instance created with thread safety at " + creationTime);
        this.data = "Synchronized Singleton Data";
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public static synchronized SynchronizedSingleton getInstance() {
        if (instance == null) {
            instance = new SynchronizedSingleton();
        }
        return instance;
    }
    
    public void performOperation() {
        requestCount++;
        System.out.println("SynchronizedSingleton: Performing operation #" + requestCount + 
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
        System.out.println("SynchronizedSingleton State: data=" + data + 
                         ", requests=" + requestCount + 
                         ", created=" + creationTime);
    }
    
    public static void demonstrateThreadSafety() {
        System.out.println("\n=== Demonstrating Thread Safety ===");
        instance = null;
        
        Runnable task = () -> {
            long startTime = System.currentTimeMillis();
            SynchronizedSingleton singleton = SynchronizedSingleton.getInstance();
            long endTime = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() + 
                             " got instance: " + singleton.hashCode() +
                             " (took " + (endTime - startTime) + "ms)");
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
        
        System.out.println("All threads got the same instance (same hashCode)!");
    }
    
    public static void demonstratePerformanceOverhead() {
        System.out.println("\n=== Demonstrating Performance Overhead ===");
        
        getInstance();
        
        long startTime = System.nanoTime();
        for (int i = 0; i < 1000000; i++) {
            getInstance();
        }
        long endTime = System.nanoTime();
        
        System.out.println("1,000,000 getInstance() calls took: " + 
                         (endTime - startTime) / 1_000_000 + "ms");
        System.out.println("Note: Every call requires acquiring a lock!");
    }
}