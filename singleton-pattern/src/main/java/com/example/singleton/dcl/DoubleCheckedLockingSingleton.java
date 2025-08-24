package com.example.singleton.dcl;

/**
 * Double-Checked Locking (DCL) Singleton Pattern
 * 
 * Optimized thread-safe lazy initialization.
 * 
 * Pros:
 * - Thread-safe
 * - Lazy initialization
 * - Avoids synchronization overhead after initialization
 * - Better performance than synchronized method
 * 
 * Cons:
 * - More complex implementation
 * - Requires volatile keyword (Java 5+)
 * - Historical issues before Java 5 (now fixed)
 */
public class DoubleCheckedLockingSingleton {
    
    private static volatile DoubleCheckedLockingSingleton instance;
    
    private String data;
    private int requestCount = 0;
    private long creationTime;
    
    private DoubleCheckedLockingSingleton() {
        creationTime = System.currentTimeMillis();
        System.out.println("DoubleCheckedLockingSingleton: Instance created with DCL at " + creationTime);
        this.data = "Double-Checked Locking Singleton Data";
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public static DoubleCheckedLockingSingleton getInstance() {
        if (instance == null) {
            synchronized (DoubleCheckedLockingSingleton.class) {
                if (instance == null) {
                    instance = new DoubleCheckedLockingSingleton();
                }
            }
        }
        return instance;
    }
    
    public void performOperation() {
        requestCount++;
        System.out.println("DCL Singleton: Performing operation #" + requestCount + 
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
        System.out.println("DCL Singleton State: data=" + data + 
                         ", requests=" + requestCount + 
                         ", created=" + creationTime);
    }
    
    public static void demonstrateOptimization() {
        System.out.println("\n=== Demonstrating DCL Optimization ===");
        instance = null;
        
        Runnable task = () -> {
            String threadName = Thread.currentThread().getName();
            
            long startTime = System.nanoTime();
            DoubleCheckedLockingSingleton singleton = getInstance();
            long firstCallTime = System.nanoTime() - startTime;
            
            startTime = System.nanoTime();
            getInstance();
            long secondCallTime = System.nanoTime() - startTime;
            
            System.out.println(threadName + ":");
            System.out.println("  First call (may create): " + firstCallTime + "ns");
            System.out.println("  Second call (no lock): " + secondCallTime + "ns");
            System.out.println("  Instance: " + singleton.hashCode());
        };
        
        Thread[] threads = new Thread[3];
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
        
        System.out.println("\nNotice: After creation, subsequent calls avoid synchronization!");
    }
    
    public static void demonstrateVolatileImportance() {
        System.out.println("\n=== Why volatile is Important ===");
        System.out.println("Without volatile:");
        System.out.println("1. Thread A starts creating instance");
        System.out.println("2. Memory allocation happens");
        System.out.println("3. Reference assigned (instance != null)");
        System.out.println("4. Constructor not yet finished!");
        System.out.println("5. Thread B sees instance != null");
        System.out.println("6. Thread B returns partially constructed object!");
        System.out.println("\nWith volatile:");
        System.out.println("- Ensures happens-before relationship");
        System.out.println("- Constructor completes before reference is visible");
        System.out.println("- All threads see fully initialized object");
    }
}