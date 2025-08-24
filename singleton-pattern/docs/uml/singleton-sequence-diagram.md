# Singleton Pattern - Sequence Diagrams

## 1. Eager Initialization Sequence

```mermaid
sequenceDiagram
    participant JVM
    participant ClassLoader
    participant EagerSingleton
    participant Client
    
    JVM->>ClassLoader: Load EagerSingleton class
    ClassLoader->>EagerSingleton: Initialize static fields
    EagerSingleton->>EagerSingleton: new EagerSingleton()
    Note over EagerSingleton: Instance created immediately
    
    Client->>EagerSingleton: getInstance()
    EagerSingleton-->>Client: Return existing instance
    Client->>EagerSingleton: performOperation()
    Note over Client,EagerSingleton: Instance already exists,<br/>no creation overhead
```

## 2. Lazy Initialization Sequence (Thread Safety Issue)

```mermaid
sequenceDiagram
    participant Thread1
    participant Thread2
    participant LazySingleton
    
    par Thread 1 execution
        Thread1->>LazySingleton: getInstance()
        LazySingleton->>LazySingleton: Check instance == null
        Note over LazySingleton: true
        LazySingleton->>LazySingleton: new LazySingleton()
    and Thread 2 execution
        Thread2->>LazySingleton: getInstance()
        LazySingleton->>LazySingleton: Check instance == null
        Note over LazySingleton: Still true!
        LazySingleton->>LazySingleton: new LazySingleton()
    end
    
    LazySingleton-->>Thread1: Return instance A
    LazySingleton-->>Thread2: Return instance B
    
    Note over Thread1,Thread2: ❌ Two different instances created!
```

## 3. Synchronized Method Sequence

```mermaid
sequenceDiagram
    participant Thread1
    participant Thread2
    participant Lock
    participant SynchronizedSingleton
    
    Thread1->>Lock: Acquire lock
    Lock-->>Thread1: Lock granted
    Thread1->>SynchronizedSingleton: getInstance()
    SynchronizedSingleton->>SynchronizedSingleton: Check instance == null
    SynchronizedSingleton->>SynchronizedSingleton: new SynchronizedSingleton()
    SynchronizedSingleton-->>Thread1: Return instance
    Thread1->>Lock: Release lock
    
    Thread2->>Lock: Acquire lock
    Note over Thread2,Lock: Waits for lock...
    Lock-->>Thread2: Lock granted
    Thread2->>SynchronizedSingleton: getInstance()
    SynchronizedSingleton->>SynchronizedSingleton: Check instance == null
    Note over SynchronizedSingleton: false (already created)
    SynchronizedSingleton-->>Thread2: Return existing instance
    Thread2->>Lock: Release lock
    
    Note over Thread1,Thread2: ✅ Same instance, but lock overhead on every call
```

## 4. Double-Checked Locking Sequence

```mermaid
sequenceDiagram
    participant Thread1
    participant Thread2
    participant DCLSingleton
    
    Thread1->>DCLSingleton: getInstance()
    DCLSingleton->>DCLSingleton: Check instance == null (no lock)
    Note over DCLSingleton: true
    
    rect rgb(255, 230, 230)
        Note over DCLSingleton: Synchronized block
        DCLSingleton->>DCLSingleton: Acquire lock
        DCLSingleton->>DCLSingleton: Check instance == null again
        Note over DCLSingleton: Still true
        DCLSingleton->>DCLSingleton: new DCLSingleton()
        DCLSingleton->>DCLSingleton: Release lock
    end
    
    DCLSingleton-->>Thread1: Return new instance
    
    Thread2->>DCLSingleton: getInstance()
    DCLSingleton->>DCLSingleton: Check instance == null (no lock)
    Note over DCLSingleton: false (already exists)
    DCLSingleton-->>Thread2: Return existing instance
    
    Note over Thread2,DCLSingleton: ✅ No synchronization needed after creation!
```

## 5. Bill Pugh (Inner Static Helper) Sequence

```mermaid
sequenceDiagram
    participant Client
    participant BillPughSingleton
    participant JVM
    participant SingletonHolder
    
    Client->>BillPughSingleton: getInstance()
    BillPughSingleton->>SingletonHolder: Access INSTANCE field
    
    alt SingletonHolder not loaded
        SingletonHolder->>JVM: Request class initialization
        JVM->>JVM: Lock class initialization
        JVM->>SingletonHolder: Initialize static fields
        SingletonHolder->>BillPughSingleton: new BillPughSingleton()
        BillPughSingleton-->>SingletonHolder: Return instance
        SingletonHolder->>SingletonHolder: Store in INSTANCE
        JVM->>JVM: Unlock class initialization
    end
    
    SingletonHolder-->>BillPughSingleton: Return INSTANCE
    BillPughSingleton-->>Client: Return instance
    
    Note over Client,JVM: JVM guarantees thread-safe initialization
```

## 6. Enum Singleton Sequence

```mermaid
sequenceDiagram
    participant Client1
    participant Client2
    participant JVM
    participant EnumSingleton
    
    Client1->>EnumSingleton: Access INSTANCE
    
    alt First access
        EnumSingleton->>JVM: Enum class initialization
        JVM->>JVM: Lock initialization
        JVM->>EnumSingleton: Create INSTANCE
        JVM->>JVM: Unlock initialization
    end
    
    EnumSingleton-->>Client1: Return INSTANCE
    Client1->>EnumSingleton: performOperation()
    
    Client2->>EnumSingleton: Access INSTANCE
    Note over EnumSingleton: Already initialized
    EnumSingleton-->>Client2: Return same INSTANCE
    
    Note over Client1,Client2: ✅ Same instance guaranteed by JVM
```

## Comparison: First Access Performance

```mermaid
graph LR
    subgraph "Initialization Cost"
        E[Eager<br/>No cost - already created]
        L[Lazy<br/>Creation cost]
        S[Synchronized<br/>Lock + Creation]
        D[DCL<br/>Lock + Creation]
        B[Bill Pugh<br/>Class load + Creation]
        EN[Enum<br/>No cost - already created]
    end
    
    subgraph "Subsequent Access Cost"
        E2[Eager<br/>Direct return]
        L2[Lazy<br/>Null check + return]
        S2[Synchronized<br/>Lock + check + return]
        D2[DCL<br/>Null check + return]
        B2[Bill Pugh<br/>Direct return]
        EN2[Enum<br/>Direct return]
    end
    
    E --> E2
    L --> L2
    S --> S2
    D --> D2
    B --> B2
    EN --> EN2
    
    style S fill:#FFB6C1
    style S2 fill:#FFB6C1
    style E fill:#90EE90
    style E2 fill:#90EE90
    style B fill:#98FB98
    style B2 fill:#98FB98
    style EN fill:#98FB98
    style EN2 fill:#98FB98
```

## Thread Contention Scenario

```mermaid
sequenceDiagram
    participant T1 as Thread 1
    participant T2 as Thread 2
    participant T3 as Thread 3
    participant S as Singleton
    
    Note over T1,S: Synchronized Method Approach
    T1->>S: getInstance() [acquire lock]
    T2->>S: getInstance() [wait...]
    T3->>S: getInstance() [wait...]
    S-->>T1: Return instance [release lock]
    S-->>T2: Return instance [after lock]
    S-->>T3: Return instance [after lock]
    
    Note over T1,S: Bill Pugh / Enum Approach
    par Parallel Access
        T1->>S: getInstance()
        S-->>T1: Return instance
    and
        T2->>S: getInstance()
        S-->>T2: Return instance
    and
        T3->>S: getInstance()
        S-->>T3: Return instance
    end
    
    Note over T1,T3: No waiting - all threads proceed immediately!
```