# Singleton Design Pattern in Java

The Singleton pattern ensures that a class has only one instance and provides a global point of access to that instance. This is one of the most commonly used design patterns, though it should be used judiciously as it can introduce global state into an application.

## 📋 Table of Contents

- [Overview](#overview)
- [When to Use Singleton](#when-to-use-singleton)
- [Implementation Variations](#implementation-variations)
- [Running the Examples](#running-the-examples)
- [Comparison Table](#comparison-table)
- [Best Practices](#best-practices)
- [Common Pitfalls](#common-pitfalls)
- [Modern Alternatives](#modern-alternatives)

## 🎯 Overview

The Singleton pattern restricts the instantiation of a class to a single instance. This is useful when exactly one object is needed to coordinate actions across the system.

### Core Components

1. **Private Constructor**: Prevents direct instantiation
2. **Static Instance**: Holds the single instance
3. **Public Access Method**: Provides global access point

## 🤔 When to Use Singleton

### Good Use Cases
- **Logging**: Single logger instance for the entire application
- **Configuration**: Single configuration object with application settings
- **Connection Pool**: Managing a pool of database connections
- **Cache**: In-memory cache that should be shared across the application
- **Thread Pool**: Managing a shared pool of threads

### When NOT to Use
- When you need multiple instances with different configurations
- When the singleton creates tight coupling
- When it makes unit testing difficult
- When you're using it just to avoid passing objects around

## 🔧 Implementation Variations

This project demonstrates **6 different ways** to implement the Singleton pattern in Java:

### 1. 🚀 Eager Initialization
```java
public class EagerSingleton {
    private static final EagerSingleton instance = new EagerSingleton();
    private EagerSingleton() {}
    public static EagerSingleton getInstance() {
        return instance;
    }
}
```
- ✅ Simple, thread-safe
- ❌ Instance created even if never used

### 2. 🐌 Lazy Initialization (Non-Thread-Safe)
```java
public class LazySingleton {
    private static LazySingleton instance;
    private LazySingleton() {}
    public static LazySingleton getInstance() {
        if (instance == null) {
            instance = new LazySingleton();
        }
        return instance;
    }
}
```
- ✅ Instance created only when needed
- ❌ NOT thread-safe

### 3. 🔒 Synchronized Method
```java
public class SynchronizedSingleton {
    private static SynchronizedSingleton instance;
    private SynchronizedSingleton() {}
    public static synchronized SynchronizedSingleton getInstance() {
        if (instance == null) {
            instance = new SynchronizedSingleton();
        }
        return instance;
    }
}
```
- ✅ Thread-safe
- ❌ Performance overhead on every call

### 4. 🎯 Double-Checked Locking (DCL)
```java
public class DoubleCheckedLockingSingleton {
    private static volatile DoubleCheckedLockingSingleton instance;
    private DoubleCheckedLockingSingleton() {}
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
}
```
- ✅ Thread-safe, efficient after initialization
- ❌ Complex, requires `volatile`

### 5. ⭐ Bill Pugh (Inner Static Helper) - **RECOMMENDED**
```java
public class BillPughSingleton {
    private BillPughSingleton() {}
    private static class SingletonHolder {
        private static final BillPughSingleton INSTANCE = new BillPughSingleton();
    }
    public static BillPughSingleton getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
```
- ✅ Thread-safe, lazy, no synchronization overhead
- ✅ Most recommended approach

### 6. 🏆 Enum Singleton - **SIMPLEST & SAFEST**
```java
public enum EnumSingleton {
    INSTANCE;
    public void doSomething() {
        // business logic
    }
}
```
- ✅ Thread-safe, serialization-safe, reflection-proof
- ❌ Cannot extend other classes

## 🚀 Running the Examples

### Prerequisites
- Java 8 or higher
- Command line terminal or any Java IDE

### Quick Start

1. **Navigate to the project directory:**
   ```bash
   cd singleton-pattern
   ```

2. **Compile all Java files:**
   ```bash
   javac -d build -sourcepath src/main/java src/main/java/com/example/singleton/**/*.java
   ```

3. **Run the main demonstration:**
   ```bash
   java -cp build com.example.singleton.SingletonDemo
   ```

### Running Specific Demos

You can run individual demonstrations:

```bash
# Run specific implementation demo
java -cp build com.example.singleton.SingletonDemo eager
java -cp build com.example.singleton.SingletonDemo lazy
java -cp build com.example.singleton.SingletonDemo synchronized
java -cp build com.example.singleton.SingletonDemo dcl
java -cp build com.example.singleton.SingletonDemo billpugh
java -cp build com.example.singleton.SingletonDemo enum

# Show comparison table only
java -cp build com.example.singleton.SingletonDemo comparison
```

### One-Command Execution

Compile and run in a single command:

```bash
javac -d build -sourcepath src/main/java src/main/java/com/example/singleton/**/*.java && java -cp build com.example.singleton.SingletonDemo
```

## 📊 Comparison Table

| Implementation | Thread-Safe | Lazy | Efficient | Simple | Best For |
|---|:---:|:---:|:---:|:---:|---|
| **Eager Initialization** | ✅ | ❌ | ✅ | ⭐⭐⭐ | Simple, always-used objects |
| **Lazy (non-sync)** | ❌ | ✅ | ✅ | ⭐⭐ | Single-threaded only |
| **Synchronized** | ✅ | ✅ | ❌ | ⭐⭐ | Low-frequency access |
| **Double-Checked** | ✅ | ✅ | ✅ | ⭐ | High-performance needs |
| **Bill Pugh** | ✅ | ✅ | ✅ | ⭐⭐⭐ | **Most use cases** ⭐ |
| **Enum** | ✅ | ✅ | ✅ | ⭐⭐⭐⭐ | **Simplest & safest** ⭐ |

### Performance Characteristics

| Implementation | Creation Cost | Access Cost | Memory Usage |
|---|---|---|---|
| Eager | High (startup) | O(1) | High (always allocated) |
| Lazy | Low (deferred) | O(1)* | Low (until needed) |
| Synchronized | Low (deferred) | O(n) - lock | Low (until needed) |
| DCL | Low (deferred) | O(1)** | Low (until needed) |
| Bill Pugh | Low (deferred) | O(1) | Low (until needed) |
| Enum | Medium (startup) | O(1) | Medium |

*Not thread-safe  
**After initialization

## ✅ Best Practices

### DO:
1. **Use Bill Pugh pattern** for most singleton needs
2. **Use Enum singleton** when you need maximum safety and simplicity
3. **Consider dependency injection** as an alternative
4. **Document why** you need a singleton
5. **Make it thread-safe** unless you're certain it's single-threaded

### DON'T:
1. **Don't use singleton** for objects that might need multiple instances later
2. **Don't use lazy non-synchronized** in multi-threaded environments
3. **Don't expose mutable state** without proper synchronization
4. **Don't use singleton** just to avoid passing parameters
5. **Don't make everything a singleton** - it's often overused

## ⚠️ Common Pitfalls

### 1. Breaking Singleton via Reflection
```java
// This can break most singleton implementations
Constructor<Singleton> constructor = Singleton.class.getDeclaredConstructor();
constructor.setAccessible(true);
Singleton instance2 = constructor.newInstance();
```
**Solution**: Use Enum singleton (reflection-proof)

### 2. Breaking Singleton via Serialization
```java
// Deserializing creates a new instance
ObjectInputStream ois = new ObjectInputStream(new FileInputStream("singleton.ser"));
Singleton instance2 = (Singleton) ois.readObject();
```
**Solution**: Implement `readResolve()` or use Enum

### 3. Breaking Singleton via Cloning
```java
// If singleton implements Cloneable
Singleton instance2 = (Singleton) instance1.clone();
```
**Solution**: Override `clone()` to throw exception or use Enum

### 4. Multiple Classloaders
Different classloaders can create multiple instances of the same singleton class.
**Solution**: Ensure singleton is loaded by a single classloader

## 🔄 Modern Alternatives

### Dependency Injection (Preferred)
Instead of using singleton pattern, modern frameworks provide better alternatives:

#### Spring Framework
```java
@Component
@Scope("singleton")  // Default scope
public class MyService {
    // Automatically managed as singleton by Spring
}
```

#### Google Guice
```java
@Singleton
public class MyService {
    // Managed by Guice container
}
```

### Why DI is Better
1. **Testability**: Easy to mock/stub in tests
2. **Flexibility**: Can change scope without code changes
3. **Decoupling**: No global state
4. **Lifecycle Management**: Container handles creation/destruction

## 📁 Project Structure

```
singleton-pattern/
├── src/main/java/com/example/singleton/
│   ├── eager/
│   │   └── EagerSingleton.java
│   ├── lazy/
│   │   └── LazySingleton.java
│   ├── synchronized/
│   │   └── SynchronizedSingleton.java
│   ├── dcl/
│   │   └── DoubleCheckedLockingSingleton.java
│   ├── billpugh/
│   │   └── BillPughSingleton.java
│   ├── enums/
│   │   └── EnumSingleton.java
│   └── SingletonDemo.java
├── docs/uml/
│   └── [UML diagrams]
└── README.md
```

## 🎓 Learning Objectives

After studying this implementation, you should understand:

1. **Why** singleton pattern exists and when to use it
2. **Different ways** to implement singleton in Java
3. **Trade-offs** between different implementations
4. **Thread safety** considerations
5. **Performance implications** of each approach
6. **Modern alternatives** to singleton pattern

## 📚 Further Reading

- [Effective Java by Joshua Bloch](https://www.oreilly.com/library/view/effective-java/9780134686097/) - Item 3: Enforce singleton with enum
- [Design Patterns by Gang of Four](https://www.amazon.com/Design-Patterns-Elements-Reusable-Object-Oriented/dp/0201633612)
- [Refactoring Guru - Singleton](https://refactoring.guru/design-patterns/singleton)
- [Double-Checked Locking is Broken](https://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html)

## 🤝 Contributing

Feel free to submit issues and enhancement requests!

---

**Remember**: The best singleton is often no singleton. Consider if you really need global state, or if dependency injection would be a better solution for your use case.