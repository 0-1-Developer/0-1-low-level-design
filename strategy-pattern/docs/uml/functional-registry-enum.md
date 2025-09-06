# Functional, Registry & Enum Strategy Patterns - UML Diagrams

## Functional Strategy Pattern - Class Diagram

```mermaid
classDiagram
    class DiscountStrategy {
        <<functional interface>>
        +applyDiscount(price: double, quantity: int) double
    }
    
    class ShoppingCart {
        -items: List~Item~
        -discountStrategy: DiscountStrategy
        +addItem(item: Item) void
        +setDiscountStrategy(strategy: DiscountStrategy) void
        +calculateTotal() double
        +calculateTotalWithStrategy(priceCalculator: Function~Item,Double~) double
        +calculateConditionalTotal(condition: Predicate~Item~, calculator: BiFunction~Double,Integer,Double~) double
    }
    
    class Item {
        -name: String
        -price: double
        -quantity: int
        +getName() String
        +getPrice() double
        +getQuantity() int
    }
    
    class DiscountStrategies {
        <<utility>>
        +NO_DISCOUNT: DiscountStrategy
        +PERCENTAGE_DISCOUNT: DiscountStrategy
        +BULK_DISCOUNT: DiscountStrategy
        +SEASONAL_DISCOUNT: DiscountStrategy
        +createPercentageDiscount(percentage: double) DiscountStrategy
        +createTieredDiscount() DiscountStrategy
        +combineStrategies(strategies: DiscountStrategy...) DiscountStrategy
        +createDynamicDiscount(threshold: double) BiFunction~Double,Integer,Double~
    }
    
    ShoppingCart --> DiscountStrategy
    ShoppingCart *-- Item
    DiscountStrategies ..> DiscountStrategy
```

## Registry Strategy Pattern - Class Diagram

```mermaid
classDiagram
    class CompressionStrategy {
        <<interface>>
        +compress(data: String) byte[]
        +decompress(compressedData: byte[]) String
        +getAlgorithmName() String
        +getCompressionRatio() double
    }
    
    class CompressionStrategyRegistry {
        -instance: CompressionStrategyRegistry
        -strategies: Map~String,CompressionStrategy~
        -strategyFactories: Map~String,Supplier~CompressionStrategy~~
        -defaultStrategy: CompressionStrategy
        
        +getInstance() CompressionStrategyRegistry
        +register(key: String, strategy: CompressionStrategy) void
        +registerFactory(key: String, factory: Supplier~CompressionStrategy~) void
        +getStrategy(key: String) CompressionStrategy
        +unregister(key: String) void
        +setDefaultStrategy(key: String) void
        +getAvailableStrategies() Set~String~
        +selectBestStrategy(data: String) CompressionStrategy
        +clearRegistry() void
    }
    
    class ZipCompressionStrategy {
        -lastCompressionRatio: double
        +compress(data: String) byte[]
        +decompress(compressedData: byte[]) String
        +getAlgorithmName() String
        +getCompressionRatio() double
    }
    
    class RleCompressionStrategy {
        -lastCompressionRatio: double
        +compress(data: String) byte[]
        +decompress(compressedData: byte[]) String
        +getAlgorithmName() String
        +getCompressionRatio() double
    }
    
    class LzwCompressionStrategy {
        -lastCompressionRatio: double
        +compress(data: String) byte[]
        +decompress(compressedData: byte[]) String
        +getAlgorithmName() String
        +getCompressionRatio() double
    }
    
    CompressionStrategy <|.. ZipCompressionStrategy
    CompressionStrategy <|.. RleCompressionStrategy
    CompressionStrategy <|.. LzwCompressionStrategy
    CompressionStrategyRegistry --> CompressionStrategy
    CompressionStrategyRegistry ..> ZipCompressionStrategy
    CompressionStrategyRegistry ..> RleCompressionStrategy
    CompressionStrategyRegistry ..> LzwCompressionStrategy
```

## Enum Strategy Pattern - Class Diagram

```mermaid
classDiagram
    class SortStrategy {
        <<enumeration>>
        BUBBLE_SORT
        QUICK_SORT
        MERGE_SORT
        INSERTION_SORT
        HEAP_SORT
        TIM_SORT
        
        -name: String
        -timeComplexity: String
        -spaceComplexity: String
        
        +sort~T extends Comparable~T~~(list: List~T~) void
        +getName() String
        +getTimeComplexity() String
        +getSpaceComplexity() String
        +selectByDataSize(size: int) SortStrategy
        +selectByStability(needsStable: boolean) SortStrategy
        +selectByMemoryConstraint(lowMemory: boolean) SortStrategy
    }
    
    note for SortStrategy "Each enum constant implements\nits own sorting algorithm\nusing anonymous classes"
```

## Sequence Diagram - Registry Pattern Dynamic Loading

```mermaid
sequenceDiagram
    participant Client
    participant Registry as CompressionStrategyRegistry
    participant Factory as StrategyFactory
    participant Strategy as ConcreteStrategy
    
    Client->>Registry: getInstance()
    Registry-->>Client: registryInstance
    
    Client->>Registry: registerFactory("CUSTOM", factory)
    Registry->>Registry: strategyFactories.put("CUSTOM", factory)
    
    Client->>Registry: getStrategy("CUSTOM")
    Registry->>Registry: Check strategies map (not found)
    Registry->>Registry: Check strategyFactories map (found)
    Registry->>Factory: get()
    Factory-->>Registry: newStrategyInstance
    Registry->>Registry: strategies.put("CUSTOM", instance)
    Registry-->>Client: strategyInstance
    
    Client->>Strategy: compress(data)
    Strategy-->>Client: compressedData
    
    Note over Registry: Strategy is now cached for future use
    
    Client->>Registry: getStrategy("CUSTOM")
    Registry->>Registry: Check strategies map (found)
    Registry-->>Client: cachedStrategyInstance
```

## Activity Diagram - Functional Strategy Composition

```mermaid
graph TD
    A[Start Strategy Composition] --> B[Define Base Strategies]
    B --> C[Create Lambda Expressions]
    
    C --> D{Composition Type?}
    D -->|Method Reference| E[Use Static Methods]
    D -->|Lambda Chain| F[Chain Multiple Lambdas]
    D -->|Higher-Order Function| G[Create Strategy Factory]
    D -->|Stream Operations| H[Use Stream API]
    
    E --> I[DiscountStrategies.NO_DISCOUNT]
    F --> J[strategy1.andThen(strategy2)]
    G --> K[createPercentageDiscount(10)]
    H --> L[items.stream().map(strategy)]
    
    I --> M[Apply to Shopping Cart]
    J --> M
    K --> M
    L --> M
    
    M --> N[Calculate Final Total]
    N --> O[Return Result]
    O --> P[End]
    
    style D fill:#e1f5fe
    style M fill:#c8e6c9
```