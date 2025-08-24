# Singleton Pattern - Class Diagrams

## Overview Class Diagram - All Variations

```mermaid
classDiagram
    class SingletonPattern {
        <<interface>>
        +getInstance() Object
    }
    
    class EagerSingleton {
        -static instance: EagerSingleton
        -data: String
        -requestCount: int
        -EagerSingleton()
        +static getInstance() EagerSingleton
        +performOperation() void
        +getData() String
        +setData(String) void
    }
    
    class LazySingleton {
        -static instance: LazySingleton
        -data: String
        -requestCount: int
        -creationTime: long
        -LazySingleton()
        +static getInstance() LazySingleton
        +performOperation() void
        +demonstrateThreadIssue() void
    }
    
    class SynchronizedSingleton {
        -static instance: SynchronizedSingleton
        -data: String
        -requestCount: int
        -SynchronizedSingleton()
        +static synchronized getInstance() SynchronizedSingleton
        +performOperation() void
        +demonstrateThreadSafety() void
    }
    
    class DoubleCheckedLockingSingleton {
        -static volatile instance: DCLSingleton
        -data: String
        -requestCount: int
        -DoubleCheckedLockingSingleton()
        +static getInstance() DCLSingleton
        +performOperation() void
        +demonstrateOptimization() void
    }
    
    class BillPughSingleton {
        -data: String
        -requestCount: int
        -BillPughSingleton()
        +static getInstance() BillPughSingleton
        +performOperation() void
    }
    
    class SingletonHolder {
        <<static>>
        -static final INSTANCE: BillPughSingleton
    }
    
    class EnumSingleton {
        <<enumeration>>
        INSTANCE
        -data: String
        -requestCount: int
        +performOperation() void
        +doBusinessLogic() void
    }
    
    SingletonPattern <|.. EagerSingleton
    SingletonPattern <|.. LazySingleton
    SingletonPattern <|.. SynchronizedSingleton
    SingletonPattern <|.. DoubleCheckedLockingSingleton
    SingletonPattern <|.. BillPughSingleton
    SingletonPattern <|.. EnumSingleton
    
    BillPughSingleton ..> SingletonHolder : uses
    
    note for EagerSingleton "Instance created at\nclass loading time"
    note for LazySingleton "NOT thread-safe\nMultiple instances possible"
    note for SynchronizedSingleton "Thread-safe but\nsynchronization overhead"
    note for DoubleCheckedLockingSingleton "Requires volatile\nOptimized locking"
    note for BillPughSingleton "RECOMMENDED\nBest practice"
    note for EnumSingleton "SIMPLEST & SAFEST\nReflection-proof"
```

## Detailed Implementation Diagrams

### 1. Eager Initialization

```mermaid
classDiagram
    class EagerSingleton {
        -static final instance: EagerSingleton = new EagerSingleton()
        -data: String
        -requestCount: int
        -EagerSingleton()
        +static getInstance() EagerSingleton
        +performOperation() void
        +getData() String
        +setData(String) void
        +getRequestCount() int
        +showState() void
    }
    
    class Client {
        +main(String[]) void
    }
    
    Client --> EagerSingleton : uses
    
    note for EagerSingleton "✅ Thread-safe by JVM\n✅ Simple implementation\n❌ Memory wasted if unused\n❌ No lazy initialization"
```

### 2. Lazy Initialization (Non-Thread-Safe)

```mermaid
classDiagram
    class LazySingleton {
        -static instance: LazySingleton
        -data: String
        -requestCount: int
        -creationTime: long
        -LazySingleton()
        +static getInstance() LazySingleton
        +performOperation() void
        +getData() String
        +setData(String) void
        +demonstrateThreadIssue() void
        +showState() void
    }
    
    class Client {
        +main(String[]) void
    }
    
    Client --> LazySingleton : uses
    
    note for LazySingleton "✅ Memory efficient\n✅ Lazy loading\n❌ NOT thread-safe\n❌ Race conditions possible"
```

### 3. Synchronized Method

```mermaid
classDiagram
    class SynchronizedSingleton {
        -static instance: SynchronizedSingleton
        -data: String
        -requestCount: int
        -creationTime: long
        -SynchronizedSingleton()
        +static synchronized getInstance() SynchronizedSingleton
        +performOperation() void
        +getData() String
        +setData(String) void
        +demonstrateThreadSafety() void
        +demonstratePerformanceOverhead() void
    }
    
    class Client {
        +main(String[]) void
    }
    
    Client --> SynchronizedSingleton : uses
    
    note for SynchronizedSingleton "✅ Thread-safe\n✅ Lazy initialization\n❌ Performance overhead\n❌ Lock on every call"
```

### 4. Double-Checked Locking

```mermaid
classDiagram
    class DoubleCheckedLockingSingleton {
        -static volatile instance: DCLSingleton
        -data: String
        -requestCount: int
        -creationTime: long
        -DoubleCheckedLockingSingleton()
        +static getInstance() DCLSingleton
        +performOperation() void
        +getData() String
        +setData(String) void
        +demonstrateOptimization() void
        +demonstrateVolatileImportance() void
    }
    
    class Client {
        +main(String[]) void
    }
    
    Client --> DoubleCheckedLockingSingleton : uses
    
    note for DoubleCheckedLockingSingleton "✅ Thread-safe\n✅ Efficient after init\n✅ Lazy initialization\n❌ Complex implementation\n⚠️ Requires volatile"
```

### 5. Bill Pugh (Inner Static Helper)

```mermaid
classDiagram
    class BillPughSingleton {
        -data: String
        -requestCount: int
        -creationTime: long
        -BillPughSingleton()
        +static getInstance() BillPughSingleton
        +performOperation() void
        +getData() String
        +setData(String) void
        +showState() void
    }
    
    class SingletonHolder {
        <<private static>>
        -static final INSTANCE: BillPughSingleton
    }
    
    class Client {
        +main(String[]) void
    }
    
    BillPughSingleton *-- SingletonHolder : contains
    SingletonHolder --> BillPughSingleton : creates
    Client --> BillPughSingleton : requests instance
    BillPughSingleton ..> SingletonHolder : delegates to
    
    note for SingletonHolder "Loaded only when\ngetInstance() is called"
    note for BillPughSingleton "✅ Thread-safe\n✅ Lazy initialization\n✅ No synchronization\n⭐ RECOMMENDED"
```

### 6. Enum Singleton

```mermaid
classDiagram
    class Enum~T~ {
        <<java.lang>>
        #name: String
        #ordinal: int
        +name() String
        +ordinal() int
        +valueOf(String) T
    }
    
    class EnumSingleton {
        <<enumeration>>
        INSTANCE
        -data: String
        -requestCount: int
        -creationTime: long
        +performOperation() void
        +doBusinessLogic() void
        +getData() String
        +setData(String) void
        +showState() void
        +demonstrateUsage() void
        +demonstrateThreadSafety() void
        +demonstrateReflectionSafety() void
    }
    
    class Client {
        +useEnumSingleton() void
    }
    
    Enum~T~ <|-- EnumSingleton
    Client --> EnumSingleton : EnumSingleton.INSTANCE
    
    note for EnumSingleton "✅ Thread-safe\n✅ Serialization-safe\n✅ Reflection-proof\n✅ Clone-proof\n⭐ SIMPLEST"
```

## Thread Safety Comparison

```mermaid
graph TB
    subgraph TS["Thread-Safe Implementations"]
        E["Eager<br/>Always Safe"]
        S["Synchronized<br/>Lock Every Call"]
        D["DCL<br/>Lock Once"]
        B["Bill Pugh<br/>JVM Guaranteed"]
        EN["Enum<br/>JVM Guaranteed"]
    end
    
    subgraph NTS["NOT Thread-Safe"]
        L["Lazy<br/>Race Condition"]
    end
    
    style E fill:#90EE90
    style S fill:#FFD700
    style D fill:#87CEEB
    style B fill:#98FB98
    style EN fill:#98FB98
    style L fill:#FFB6C1
```

## Memory & Performance Characteristics

```mermaid
graph TB
    subgraph IT["Initialization Timing"]
        E1["Eager: Class Load Time"]
        L1["Lazy: First Use"]
        S1["Synchronized: First Use"]
        D1["DCL: First Use"]
        B1["Bill Pugh: First Use"]
        EN1["Enum: Class Load Time"]
    end
    
    subgraph PA["Performance After Init"]
        E2["Eager: Direct Access"]
        L2["Lazy: Direct Access*"]
        S2["Synchronized: Lock Required"]
        D2["DCL: Direct Access"]
        B2["Bill Pugh: Direct Access"]
        EN2["Enum: Direct Access"]
    end
    
    E1 --> E2
    L1 --> L2
    S1 --> S2
    D1 --> D2
    B1 --> B2
    EN1 --> EN2
    
    style E1 fill:#FFE4B5
    style EN1 fill:#FFE4B5
    style L1 fill:#E0FFE0
    style S1 fill:#E0FFE0
    style D1 fill:#E0FFE0
    style B1 fill:#E0FFE0
    style S2 fill:#FFB6C1
    style L2 fill:#FFFACD
```

## Implementation Decision Tree

```mermaid
graph TD
    Start["Need Singleton?"] --> Q1{"Need lazy<br/>initialization?"}
    Q1 -->|No| Eager["Use Eager<br/>Initialization"]
    Q1 -->|Yes| Q2{"Multi-threaded<br/>environment?"}
    Q2 -->|No| Lazy["Use Lazy<br/>Initialization"]
    Q2 -->|Yes| Q3{"Performance<br/>critical?"}
    Q3 -->|No| Q4{"Simplicity<br/>important?"}
    Q3 -->|Yes| Q5{"Need<br/>inheritance?"}
    Q4 -->|Yes| Enum["Use Enum<br/>Singleton"]
    Q4 -->|No| Bill["Use Bill Pugh<br/>Pattern"]
    Q5 -->|Yes| DCL["Use Double-<br/>Checked Locking"]
    Q5 -->|No| Enum2["Use Enum<br/>Singleton"]
    
    style Enum fill:#98FB98
    style Enum2 fill:#98FB98
    style Bill fill:#87CEEB
    style DCL fill:#FFE4B5
    style Eager fill:#FFFACD
    style Lazy fill:#FFB6C1
```