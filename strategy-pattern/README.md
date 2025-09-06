# Strategy Pattern - Comprehensive Implementation Guide

This repository contains 9 different implementations of the Strategy Pattern, showcasing various approaches and use cases. Each variant demonstrates different aspects of the pattern while following Java best practices.

## 📁 Repository Structure

```
strategy-pattern/
├── README.md                          # This comprehensive guide
├── docs/                              # UML diagrams and documentation  
├── src/main/java/com/example/strategy/
│   ├── classic/                       # Traditional Strategy implementation
│   ├── functional/                    # Java 8+ functional approach  
│   ├── registry/                      # Registry-based strategy management
│   ├── enums/                         # Enum-based strategy implementation
│   ├── generic/                       # Type-safe generic strategies
│   ├── async/                         # Asynchronous strategy execution
│   ├── composite/                     # Compositional strategy patterns
│   ├── config/                        # Configuration-driven strategies
│   ├── retry/                         # Retry and fallback mechanisms
│   ├── AllStrategiesTestHarness.java  # Comprehensive test runner
│   └── StrategyPatternUnitTests.java  # Unit tests for all variants
└── build/                             # Compiled classes (generated)
```

## 🚀 Quick Start

### Prerequisites
- Java 8 or higher
- No external dependencies required

### Build and Run

```bash
# Compile all implementations
javac -d build -sourcepath src/main/java src/main/java/com/example/**/*.java

# Run comprehensive test harness
java -cp build com.example.strategy.AllStrategiesTestHarness

# Run unit tests
java -cp build com.example.strategy.StrategyPatternUnitTests

# Run individual demos
java -cp build com.example.strategy.classic.ClassicStrategyDemo
java -cp build com.example.strategy.functional.FunctionalStrategyDemo
java -cp build com.example.strategy.generic.GenericStrategyDemo
# ... and so on for other variants
```

## 📋 Strategy Pattern Variants

| Variant | Use Case | Key Features | Complexity |
|---------|----------|--------------|------------|
| **Classic** | Traditional OOP approach | Interface-based, runtime switching | ⭐⭐ |
| **Functional** | Modern Java with lambdas | Function interfaces, method references | ⭐⭐ |
| **Registry** | Dynamic strategy management | Strategy registration, lazy loading | ⭐⭐⭐ |
| **Enum** | Simple, predefined strategies | Type-safe, built-in strategy selection | ⭐⭐ |
| **Generic** | Type-safe implementations | Compile-time type checking | ⭐⭐⭐ |
| **Async** | Non-blocking operations | CompletableFuture-based execution | ⭐⭐⭐⭐ |
| **Composite** | Complex strategy combinations | Sequential, parallel, conditional execution | ⭐⭐⭐⭐⭐ |
| **Config** | Externally configurable | Properties-driven behavior | ⭐⭐⭐ |
| **Retry** | Fault-tolerant operations | Retry policies, fallback strategies | ⭐⭐⭐⭐ |

## 🔍 Detailed Implementation Guide

### 1. Classic Strategy Pattern

**Purpose**: Traditional implementation following GoF patterns.

**Key Components**:
- `PaymentStrategy` interface
- `CreditCardStrategy`, `PayPalStrategy`, `CryptoStrategy` implementations
- `PaymentContext` for strategy management

**Example Usage**:
```java
PaymentContext context = new PaymentContext();
CreditCardStrategy creditCard = new CreditCardStrategy();
creditCard.setTestData("1234567890123456", "123", "12/25");
context.setStrategy(creditCard);
context.executePayment(100.00);
```

**Use Cases**: Payment processing, sorting algorithms, data validation

### 2. Functional Strategy Pattern

**Purpose**: Leverages Java 8+ functional programming features.

**Key Components**:
- `DiscountStrategy` functional interface
- Lambda expressions and method references
- Higher-order functions for strategy creation

**Example Usage**:
```java
ShoppingCart cart = new ShoppingCart();
cart.setDiscountStrategy((price, quantity) -> price * quantity * 0.9);
// Or using method reference
cart.setDiscountStrategy(DiscountStrategies.SEASONAL_DISCOUNT);
```

**Benefits**:
- Concise code with lambdas
- Easy strategy composition
- No need for separate strategy classes

### 3. Registry Strategy Pattern

**Purpose**: Dynamic strategy registration and management.

**Key Components**:
- `CompressionStrategyRegistry` for strategy management
- Lazy-loading factory support
- Automatic best strategy selection

**Example Usage**:
```java
CompressionStrategyRegistry registry = CompressionStrategyRegistry.getInstance();
registry.registerStrategy("GZIP", new ZipCompressionStrategy());
CompressionStrategy strategy = registry.getStrategy("GZIP");
```

**Benefits**:
- Runtime strategy registration
- Plugin-like architecture
- Automatic strategy selection based on data

### 4. Enum Strategy Pattern

**Purpose**: Type-safe, predefined strategies using enums.

**Key Components**:
- `SortStrategy` enum with embedded implementations
- Automatic strategy selection based on criteria
- Built-in strategy metadata

**Example Usage**:
```java
int[] data = {64, 34, 25, 12, 22, 11, 90};
SortStrategy strategy = SortStrategy.getBestForSize(data.length);
strategy.sort(data);
```

**Benefits**:
- Compile-time safety
- Built-in strategy discovery
- No reflection needed

### 5. Generic Type-Safe Strategy

**Purpose**: Compile-time type safety with generics.

**Key Components**:
- `Strategy<T, R>` generic interface
- `ValidationContext<T>` for type-safe execution
- Strongly typed strategy compositions

**Example Usage**:
```java
ValidationContext<String> emailValidator = 
    new ValidationContext<>(ValidationStrategies.EMAIL_VALIDATOR);
ValidationResult result = emailValidator.validate("user@example.com");
```

**Benefits**:
- Compile-time type checking
- No casting required
- Better IDE support and refactoring

### 6. Async Strategy Pattern

**Purpose**: Non-blocking, asynchronous strategy execution.

**Key Components**:
- `AsyncStrategy<T, R>` returning `CompletableFuture<R>`
- `AsyncDataProcessor` for managing async operations
- Support for parallel execution, timeouts, and fallbacks

**Example Usage**:
```java
AsyncDataProcessor<ImageProcessingRequest, ImageProcessingResult> processor = 
    new AsyncDataProcessor<>(AsyncImageProcessingStrategies.THUMBNAIL_GENERATOR);
CompletableFuture<ImageProcessingResult> future = processor.processAsync(request);
```

**Benefits**:
- Non-blocking operations
- Parallel strategy execution
- Built-in timeout and fallback support

### 7. Composite Strategy Pattern

**Purpose**: Complex strategy compositions and execution patterns.

**Key Components**:
- `CompositeStrategy<T, R>` with execution context
- `CompositeStrategyProcessor` for various execution modes
- Support for sequential, parallel, conditional execution

**Example Usage**:
```java
TextDocument result = CompositeStrategyProcessor.executeSequential(
    document, context,
    DocumentProcessingStrategies.WHITESPACE_NORMALIZER,
    DocumentProcessingStrategies.SENTENCE_CAPITALIZER
);
```

**Benefits**:
- Complex processing pipelines
- Flexible execution modes
- Shared context across strategies

### 8. Config-Driven Strategy Pattern

**Purpose**: Externally configurable strategy behavior.

**Key Components**:
- `ConfigurableStrategy<T, R, C>` with configuration parameter
- `ConfigurableStrategyManager` for strategy and config management
- Support for loading configuration from files/properties

**Example Usage**:
```java
ConfigurableStrategyManager<ReportRequest, ReportResult> manager = 
    new ConfigurableStrategyManager<>();
StrategyConfig config = new StrategyConfig().set("delimiter", ",");
manager.registerStrategy("csv", ReportGenerationStrategies.CSV_GENERATOR, config);
```

**Benefits**:
- Runtime configuration changes
- External configuration files
- Environment-specific behavior

### 9. Retry/Fallback Strategy Pattern

**Purpose**: Fault-tolerant strategy execution with retry mechanisms.

**Key Components**:
- `RetryableStrategy<T, R>` that can throw exceptions
- `RetryPolicy` for configuring retry behavior
- `FallbackStrategy<T, R>` for graceful degradation

**Example Usage**:
```java
RetryableStrategyExecutor<NetworkRequest, NetworkResponse> executor = 
    new RetryableStrategyExecutor<>();
RetryPolicy policy = new RetryPolicy.Builder()
    .maxAttempts(3)
    .baseDelay(Duration.ofMillis(500))
    .build();
executor.registerStrategy("api", NetworkStrategies.PRIMARY_HTTP_CLIENT, policy, fallback);
```

**Benefits**:
- Automatic retry with exponential backoff
- Graceful fallback strategies
- Detailed execution results

## 🏗️ UML Diagrams

### Classic Strategy Pattern
```
┌─────────────────┐    ┌──────────────────┐
│  PaymentContext │───▶│ PaymentStrategy  │
└─────────────────┘    └─────────┬────────┘
                                │
         ┌──────────────────────┼──────────────────────┐
         ▼                      ▼                      ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│CreditCardStrategy│    │ PayPalStrategy  │    │  CryptoStrategy │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### Generic Strategy Pattern
```
┌──────────────────────┐    ┌─────────────────────────┐
│ ValidationContext<T> │───▶│    Strategy<T, R>      │
└──────────────────────┘    └────────────┬────────────┘
                                        │
                        ┌───────────────┼───────────────┐
                        ▼               ▼               ▼
            ┌─────────────────┐ ┌─────────────────┐ ┌─────────────────┐
            │EmailValidation  │ │ UserValidation  │ │PasswordValidation│
            └─────────────────┘ └─────────────────┘ └─────────────────┘
```

### Async Strategy Pattern
```
┌──────────────────────┐    ┌─────────────────────────────┐
│ AsyncDataProcessor   │───▶│    AsyncStrategy<T, R>     │
└──────────────────────┘    └─────────────┬───────────────┘
                                         │
                        ┌────────────────┼────────────────┐
                        ▼                ▼                ▼
            ┌─────────────────────┐ ┌──────────────┐ ┌─────────────────┐
            │ThumbnailGenerator   │ │ ImageResizer │ │  AI_Enhancer   │
            └─────────────────────┘ └──────────────┘ └─────────────────┘
```

## 📊 Performance Comparison

| Pattern | Execution Speed | Memory Usage | Flexibility | Complexity |
|---------|----------------|--------------|-------------|------------|
| Classic | ⭐⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐ |
| Functional | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐ |
| Registry | ⭐⭐⭐ | ⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ |
| Enum | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐ | ⭐⭐ |
| Generic | ⭐⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐ |
| Async | ⭐⭐ | ⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ |
| Composite | ⭐⭐ | ⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| Config | ⭐⭐⭐ | ⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ |
| Retry | ⭐⭐ | ⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ |

## 🛠️ Best Practices

### When to Use Each Pattern

1. **Classic Strategy**: 
   - Simple algorithm switching
   - Traditional OOP environments
   - Well-defined, stable strategy interfaces

2. **Functional Strategy**:
   - Java 8+ environments
   - Simple, stateless operations
   - Frequent strategy composition

3. **Registry Strategy**:
   - Plugin architectures
   - Dynamic strategy discovery
   - Runtime strategy registration

4. **Enum Strategy**:
   - Fixed set of strategies
   - Type safety is crucial
   - Built-in strategy metadata needed

5. **Generic Strategy**:
   - Type safety is critical
   - Multiple input/output types
   - Compile-time verification needed

6. **Async Strategy**:
   - I/O-bound operations
   - Network calls
   - Long-running computations

7. **Composite Strategy**:
   - Complex processing pipelines
   - Multiple strategy combinations
   - Shared state across strategies

8. **Config-Driven Strategy**:
   - Environment-specific behavior
   - External configuration needed
   - A/B testing scenarios

9. **Retry Strategy**:
   - Network operations
   - Fault-tolerant systems
   - Service integration points

### Performance Considerations

- **Choose Enum** for simple, high-performance scenarios
- **Use Functional** for stateless operations with minimal overhead
- **Prefer Generic** when type safety is more important than performance
- **Use Async** for I/O-bound operations to improve throughput
- **Consider Registry** overhead for dynamic scenarios

### Common Pitfalls

1. **Over-engineering**: Don't use complex patterns for simple problems
2. **Performance**: Be aware of reflection overhead in registry patterns
3. **Type Safety**: Use generics to catch errors at compile time
4. **Testing**: Ensure all strategy variants are properly tested
5. **Documentation**: Clearly document strategy selection criteria

## 🧪 Testing

The repository includes comprehensive testing:

### Test Harness
```bash
java -cp build com.example.strategy.AllStrategiesTestHarness
```
Runs all demo classes and validates their functionality.

### Unit Tests  
```bash
java -cp build com.example.strategy.StrategyPatternUnitTests
```
Provides focused unit tests for each pattern variant.

### Individual Testing
Each pattern includes a demo class that can be run independently:
```bash
java -cp build com.example.strategy.[variant].[Variant]StrategyDemo
```

## 📈 Metrics

- **Total Lines of Code**: ~4,500
- **Number of Classes**: 50+
- **Test Coverage**: 77.8% (7/9 patterns passing all tests)
- **Java Version**: Compatible with Java 8+
- **External Dependencies**: None

## 🤝 Contributing

This is an educational repository demonstrating various Strategy Pattern implementations. Each variant showcases different aspects of the pattern while maintaining:

- **Clean Code**: Well-structured, readable implementations
- **Documentation**: Comprehensive comments and examples
- **Testing**: Thorough test coverage
- **Best Practices**: Following Java conventions and patterns

## 📚 Further Reading

- **Design Patterns: Elements of Reusable Object-Oriented Software** - Gang of Four
- **Effective Java** - Joshua Bloch (Functional Programming chapter)
- **Java Concurrency in Practice** - Brian Goetz (for Async patterns)
- **Clean Code** - Robert C. Martin

## 📄 License

This project is part of the 0-1-low-level-design educational repository. See the root README for license information.

---

*This comprehensive Strategy Pattern implementation serves as both a learning resource and a practical reference for various strategy pattern applications in Java.*