package com.example.singleton.eager;

/**
 * Eager Initialization Singleton Pattern
 * 
 * The instance is created at class loading time.
 * 
 * Pros:
 * - Simple implementation
 * - Thread-safe by default (JVM handles class loading)
 * - No synchronization overhead
 * 
 * Cons:
 * - Instance created even if never used (wastes memory)
 * - No exception handling during instance creation
 * - Cannot pass parameters to constructor
 */
public class EagerSingleton {
    
    private static final EagerSingleton instance = new EagerSingleton();
    
    private String data;
    private int requestCount = 0;
    
    private EagerSingleton() {
        System.out.println("EagerSingleton: Instance created at class loading time");
        this.data = "Eager Singleton Data";
    }
    
    public static EagerSingleton getInstance() {
        return instance;
    }
    
    public void performOperation() {
        requestCount++;
        System.out.println("EagerSingleton: Performing operation #" + requestCount);
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
    
    public void showState() {
        System.out.println("EagerSingleton State: data=" + data + ", requests=" + requestCount);
    }
}