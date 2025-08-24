package com.example.singleton.lazy;

/**
 * Lazy Initialization Singleton Pattern (Non-Thread-Safe)
 * 
 * The instance is created only when first requested.
 * 
 * Pros:
 * - Instance created only when needed (saves memory)
 * - Can handle exceptions during instance creation
 * 
 * Cons:
 * - NOT thread-safe (multiple threads can create multiple instances)
 * - Should only be used in single-threaded environments
 */
public class LazySingleton {
    
    private static LazySingleton instance;
    
    private String data;
    private int requestCount = 0;
    private long creationTime;
    
    private LazySingleton() {
        creationTime = System.currentTimeMillis();
        System.out.println("LazySingleton: Instance created lazily at " + creationTime);
        this.data = "Lazy Singleton Data";
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public static LazySingleton getInstance() {
        if (instance == null) {
            instance = new LazySingleton();
        }
        return instance;
    }
    
    public void performOperation() {
        requestCount++;
        System.out.println("LazySingleton: Performing operation #" + requestCount);
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
        System.out.println("LazySingleton State: data=" + data + 
                         ", requests=" + requestCount + 
                         ", created=" + creationTime);
    }
    
    public static void demonstrateThreadIssue() {
        System.out.println("\n=== Demonstrating Thread Safety Issue ===");
        instance = null;
        
        Runnable task = () -> {
            LazySingleton singleton = LazySingleton.getInstance();
            System.out.println(Thread.currentThread().getName() + 
                             " got instance: " + singleton.hashCode());
        };
        
        Thread t1 = new Thread(task, "Thread-1");
        Thread t2 = new Thread(task, "Thread-2");
        Thread t3 = new Thread(task, "Thread-3");
        
        t1.start();
        t2.start();
        t3.start();
        
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("Note: Different hashCodes indicate multiple instances were created!");
    }
}