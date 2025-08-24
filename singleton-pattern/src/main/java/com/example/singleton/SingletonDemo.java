package com.example.singleton;

import com.example.singleton.eager.EagerSingleton;
import com.example.singleton.lazy.LazySingleton;
import com.example.singleton.synchronizedversion.SynchronizedSingleton;
import com.example.singleton.dcl.DoubleCheckedLockingSingleton;
import com.example.singleton.billpugh.BillPughSingleton;
import com.example.singleton.enums.EnumSingleton;

/**
 * Main demonstration class for all Singleton pattern variations.
 * Shows the differences, use cases, and trade-offs of each implementation.
 */
public class SingletonDemo {
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("    SINGLETON PATTERN DEMONSTRATION    ");
        System.out.println("========================================\n");
        
        printMenu();
        
        if (args.length > 0) {
            runSpecificDemo(args[0]);
        } else {
            runAllDemos();
        }
    }
    
    private static void printMenu() {
        System.out.println("Available Singleton Implementations:");
        System.out.println("1. Eager Initialization");
        System.out.println("2. Lazy Initialization (Non-Thread-Safe)");
        System.out.println("3. Synchronized Method");
        System.out.println("4. Double-Checked Locking");
        System.out.println("5. Bill Pugh (Inner Static Helper) - RECOMMENDED");
        System.out.println("6. Enum Singleton - SIMPLEST & SAFEST");
        System.out.println("----------------------------------------\n");
    }
    
    private static void runAllDemos() {
        System.out.println("Running all demonstrations...\n");
        
        demonstrateEagerSingleton();
        pause();
        
        demonstrateLazySingleton();
        pause();
        
        demonstrateSynchronizedSingleton();
        pause();
        
        demonstrateDoubleCheckedLocking();
        pause();
        
        demonstrateBillPughSingleton();
        pause();
        
        demonstrateEnumSingleton();
        
        printComparison();
    }
    
    private static void runSpecificDemo(String demo) {
        switch (demo.toLowerCase()) {
            case "eager":
                demonstrateEagerSingleton();
                break;
            case "lazy":
                demonstrateLazySingleton();
                break;
            case "synchronized":
                demonstrateSynchronizedSingleton();
                break;
            case "dcl":
                demonstrateDoubleCheckedLocking();
                break;
            case "billpugh":
                demonstrateBillPughSingleton();
                break;
            case "enum":
                demonstrateEnumSingleton();
                break;
            case "comparison":
                printComparison();
                break;
            default:
                System.out.println("Unknown demo: " + demo);
                System.out.println("Valid options: eager, lazy, synchronized, dcl, billpugh, enum, comparison");
        }
    }
    
    private static void demonstrateEagerSingleton() {
        printHeader("EAGER INITIALIZATION SINGLETON");
        
        System.out.println("Getting first instance...");
        EagerSingleton instance1 = EagerSingleton.getInstance();
        instance1.setData("Modified by first access");
        instance1.performOperation();
        
        System.out.println("\nGetting second instance...");
        EagerSingleton instance2 = EagerSingleton.getInstance();
        instance2.performOperation();
        
        System.out.println("\nVerifying singleton property:");
        System.out.println("instance1 == instance2: " + (instance1 == instance2));
        System.out.println("instance2.getData(): " + instance2.getData());
        instance2.showState();
        
        System.out.println("\nPros: Simple, thread-safe");
        System.out.println("Cons: Memory wasted if never used, no lazy loading");
    }
    
    private static void demonstrateLazySingleton() {
        printHeader("LAZY INITIALIZATION SINGLETON (Non-Thread-Safe)");
        
        System.out.println("Getting instance lazily...");
        LazySingleton instance = LazySingleton.getInstance();
        instance.performOperation();
        instance.showState();
        
        LazySingleton.demonstrateThreadIssue();
        
        System.out.println("\nPros: Memory efficient, lazy loading");
        System.out.println("Cons: NOT thread-safe, can create multiple instances");
    }
    
    private static void demonstrateSynchronizedSingleton() {
        printHeader("SYNCHRONIZED METHOD SINGLETON");
        
        SynchronizedSingleton.demonstrateThreadSafety();
        SynchronizedSingleton.demonstratePerformanceOverhead();
        
        System.out.println("\nPros: Thread-safe, lazy loading");
        System.out.println("Cons: Performance overhead on every call");
    }
    
    private static void demonstrateDoubleCheckedLocking() {
        printHeader("DOUBLE-CHECKED LOCKING SINGLETON");
        
        DoubleCheckedLockingSingleton.demonstrateOptimization();
        DoubleCheckedLockingSingleton.demonstrateVolatileImportance();
        
        System.out.println("\nPros: Thread-safe, lazy, efficient after creation");
        System.out.println("Cons: Complex, requires volatile (Java 5+)");
    }
    
    private static void demonstrateBillPughSingleton() {
        printHeader("BILL PUGH SINGLETON (Inner Static Helper)");
        
        BillPughSingleton.demonstrateLazyLoading();
        BillPughSingleton.demonstrateThreadSafety();
        BillPughSingleton.explainMechanism();
        
        System.out.println("\nPros: Thread-safe, lazy, no overhead, simple");
        System.out.println("*** RECOMMENDED for most use cases! ***");
    }
    
    private static void demonstrateEnumSingleton() {
        printHeader("ENUM SINGLETON");
        
        EnumSingleton.demonstrateUsage();
        EnumSingleton.demonstrateThreadSafety();
        EnumSingleton.demonstrateReflectionSafety();
        EnumSingleton.explainEnumSingleton();
        
        System.out.println("\nPros: Simplest, safest, serialization-safe");
        System.out.println("Cons: Cannot extend classes, less flexible");
        System.out.println("*** BEST for simple singletons! ***");
    }
    
    private static void printComparison() {
        printHeader("SINGLETON PATTERNS COMPARISON");
        
        System.out.println("+---------------------+-------------+------+------------+------------+-------------------------+");
        System.out.println("| Implementation      | Thread-Safe | Lazy | Efficient  | Simplicity | Best For                |");
        System.out.println("+---------------------+-------------+------+------------+------------+-------------------------+");
        System.out.println("| Eager               |     YES     |  NO  |     YES    |    ***     | Simple, always-used     |");
        System.out.println("| Lazy (non-sync)     |     NO      |  YES |     YES    |    **      | Single-threaded only    |");
        System.out.println("| Synchronized        |     YES     |  YES |     NO     |    **      | Low-frequency access    |");
        System.out.println("| Double-Checked      |     YES     |  YES |     YES    |    *       | High-performance needs  |");
        System.out.println("| Bill Pugh          |     YES     |  YES |     YES    |    ***     | Most use cases (BEST)   |");
        System.out.println("| Enum               |     YES     |  YES |     YES    |    ****    | Simple & safe (BEST)    |");
        System.out.println("+---------------------+-------------+------+------------+------------+-------------------------+");
        
        System.out.println("\nRecommendations:");
        System.out.println("- For most cases: Use Bill Pugh (Inner Static Helper)");
        System.out.println("- For simplest & safest: Use Enum Singleton");
        System.out.println("- For framework/DI: Let the framework manage it");
        System.out.println("- Avoid: Lazy non-synchronized (unless single-threaded)");
    }
    
    private static void printHeader(String title) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("  " + title);
        System.out.println("=".repeat(50));
    }
    
    private static void pause() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}