# Abstract Factory Pattern

## Overview

The **Abstract Factory Pattern** provides an interface for creating families of related or dependent objects without specifying their concrete classes. It ensures that products created by a factory are compatible with each other, maintaining consistency across an entire product family.

## Intent & Problem Solved

### Intent
- Create families of related objects that must work together harmoniously
- Decouple client code from concrete product classes
- Ensure products within a family are compatible
- Enable switching between product families at runtime

### Problem Solved
When developing systems with multiple UI themes, database providers, or platform-specific components, you need to ensure that all components work together correctly. The Abstract Factory pattern prevents mixing incompatible products from different families (e.g., Windows buttons with macOS scrollbars).

## When to Use

Use the Abstract Factory Pattern when:
- Your system needs to work with multiple families of related products
- You want to ensure products from the same family are used together
- You need to provide a library of products revealing only interfaces, not implementations
- Product families vary but their interfaces remain constant
- You want to support multiple platforms or configurations

## Comparison with Related Patterns

### Abstract Factory vs Factory Method

| Aspect | Abstract Factory | Factory Method |
|--------|-----------------|----------------|
| **Purpose** | Creates families of related products | Creates a single product |
| **Scope** | Multiple product types | Single product type |
| **Flexibility** | Switch entire families at once | Customize individual product creation |
| **Complexity** | More complex, multiple methods | Simpler, single method |
| **Use Case** | UI themes, database families | Document creation, logging |

### Abstract Factory vs Builder

| Aspect | Abstract Factory | Builder |
|--------|-----------------|---------|
| **Intent** | Create families of related objects | Construct complex objects step-by-step |
| **Creation Process** | Single-step creation | Multi-step construction |
| **Products** | Multiple related products | Single complex product |
| **Configuration** | Factory selection | Build steps customization |
| **Use Case** | Theme systems, platform abstraction | Document generation, query building |

## Implementation Variants

This repository includes **9 comprehensive implementations** of the Abstract Factory pattern:

### 1. Classic Abstract Factory (GoF)
**Location**: `classic/`
- Pure interface-based approach with concrete factories
- Clear separation between abstract and concrete classes
- **Best for**: Traditional OOP systems, clear documentation needs

### 2. Factory Method-backed
**Location**: `factorymethod/`
- Each abstract factory method is itself a factory method
- Supports template methods for additional behavior
- **Best for**: When you need hook methods or shared creation logic

### 3. Parameterized Factory
**Location**: `parameterized/`
- Uses enums/parameters to select product variants
- Reduces class proliferation
- **Best for**: Systems with many similar product families

### 4. Registry-backed Factory
**Location**: `registry/`
- Dynamic factory registration and lookup
- Supports runtime plugin systems
- **Best for**: Plugin architectures, extensible systems

### 5. Reflection-based Factory
**Location**: `reflection/`
- Creates products using class names from configuration
- Maximum flexibility, reduced compile-time safety
- **Best for**: Framework development, configuration-driven systems

### 6. Functional/Lambda Factory
**Location**: `functional/`
- Uses function composition and suppliers
- Lightweight, no class hierarchies needed
- **Best for**: Simple products, functional programming style

### 7. Prototype-backed Factory
**Location**: `prototype/`
- Creates products by cloning prototypes
- Efficient for expensive object creation
- **Best for**: Complex initialization, performance-critical systems

### 8. Config-driven Factory
**Location**: `config/`
- Loads factory configuration from external sources
- Supports hot-reloading and A/B testing
- **Best for**: Multi-tenant systems, dynamic theming

### 9. Test Harness
**Location**: `TestHarness.java`
- Comprehensive testing across all implementations
- Validates family consistency and functionality
- **Best for**: Quality assurance, learning by example

## Decision Matrix

| Variant | Type Safety | Flexibility | Performance | Complexity | Testability |
|---------|------------|-------------|-------------|------------|-------------|
| **Classic** | ⭐⭐⭐⭐⭐ | ⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐ | ⭐⭐⭐⭐⭐ |
| **Factory Method** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐ |
| **Parameterized** | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐ | ⭐⭐⭐⭐ |
| **Registry** | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐ |
| **Reflection** | ⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐ |
| **Functional** | ⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐ | ⭐⭐⭐ |
| **Prototype** | ⭐⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐ |
| **Config-driven** | ⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐ |

## Trade-offs

### Class Explosion vs Flexibility
- **Classic approach**: Many classes but clear structure
- **Parameterized approach**: Fewer classes but complex switches
- **Recommendation**: Start simple, refactor when patterns emerge

### Type Safety vs Runtime Flexibility
- **Compile-time (Classic, Factory Method)**: Catch errors early
- **Runtime (Reflection, Config)**: Maximum flexibility
- **Recommendation**: Prefer compile-time safety unless flexibility is critical

### Performance Considerations
- **Direct instantiation**: Fastest but least flexible
- **Prototype cloning**: Fast after initial creation
- **Reflection**: Slowest due to runtime resolution
- **Recommendation**: Profile before optimizing

## Running the Examples

### Compile All Implementations
```bash
# From the abstract-factory-pattern directory
javac -d build -sourcepath src/main/java src/main/java/com/example/abstractfactory/**/*.java
```

### Run Individual Demos
```bash
# Classic implementation
java -cp build com.example.abstractfactory.classic.ClassicAbstractFactoryDemo

# Factory Method variant
java -cp build com.example.abstractfactory.factorymethod.FactoryMethodDemo

# Parameterized variant
java -cp build com.example.abstractfactory.parameterized.ParameterizedFactoryDemo

# Registry-backed variant
java -cp build com.example.abstractfactory.registry.RegistryFactoryDemo

# Reflection-based variant
java -cp build com.example.abstractfactory.reflection.ReflectionFactoryDemo

# Functional variant
java -cp build com.example.abstractfactory.functional.FunctionalFactoryDemo

# Prototype-backed variant
java -cp build com.example.abstractfactory.prototype.PrototypeFactoryDemo

# Config-driven variant
java -cp build com.example.abstractfactory.config.ConfigDrivenFactoryDemo

# Run comprehensive test harness
java -cp build com.example.abstractfactory.TestHarness
```

## Real-World Applications

### 1. **UI Framework Development**
- Swing/JavaFX theme systems
- Cross-platform GUI libraries
- Web component libraries (Material, Bootstrap themes)

### 2. **Database Abstraction Layers**
- JDBC driver families
- ORM provider switching
- NoSQL/SQL abstraction

### 3. **Game Development**
- Platform-specific renderers (DirectX/OpenGL/Vulkan)
- Enemy/item families per game level
- Difficulty-based component sets

### 4. **Cloud Provider Abstraction**
- AWS/Azure/GCP service families
- Storage/compute/network abstraction
- Multi-cloud deployment strategies

### 5. **Document Processing**
- PDF/Word/HTML generator families
- Import/export format families
- Printer/screen/web output families

## Exercise Challenges

### Beginner
1. **Add a new product type**: Extend the existing families with a `TextField` component
2. **Create a Linux theme**: Add Linux-specific implementations to the classic variant
3. **Implement theme persistence**: Save and load the selected theme preference

### Intermediate
1. **Hybrid factory**: Combine parameterized and registry approaches
2. **Lazy initialization**: Modify prototype variant to create prototypes on-demand
3. **Factory chain**: Implement fallback factories when products aren't available

### Advanced
1. **Abstract factory factory**: Create a meta-factory that produces abstract factories
2. **Async factory**: Implement factories that load resources asynchronously
3. **Dependency injection**: Integrate with a DI container for factory selection
4. **Performance monitoring**: Add metrics collection to track factory usage patterns

### Expert
1. **Hot-swappable factories**: Implement runtime factory replacement without restart
2. **Distributed factories**: Create factories that can instantiate products on remote systems
3. **Versioned families**: Support multiple versions of product families simultaneously
4. **AI-driven selection**: Use machine learning to select optimal factories based on context

## Best Practices

1. **Keep interfaces focused**: Don't add methods to the abstract factory that only some families need
2. **Ensure family consistency**: All products from a factory must work together
3. **Document family requirements**: Clearly specify what makes products compatible
4. **Consider immutability**: Make products immutable when possible
5. **Handle missing products gracefully**: Provide sensible defaults or clear error messages
6. **Test family interactions**: Don't just test individual products
7. **Version your interfaces**: Plan for interface evolution
8. **Profile before optimizing**: Don't assume prototype/reflection is faster

## Common Pitfalls

1. **Mixing products from different families**: Leads to inconsistent UI/behavior
2. **Over-engineering**: Don't use Abstract Factory for single products
3. **Ignoring disposal**: Some products may need cleanup (close connections, release resources)
4. **Tight coupling to factories**: Clients shouldn't know concrete factory classes
5. **Incomplete families**: Ensure all factories implement all product methods
6. **Configuration complexity**: Config-driven approaches need validation
7. **Reflection security**: Be cautious with reflection in security-sensitive contexts

## Summary

The Abstract Factory pattern is essential for managing families of related objects that must work together. This implementation provides nine different approaches, from the classic GoF pattern to modern functional and configuration-driven variants. Choose the variant that best matches your system's requirements for type safety, flexibility, and performance.

Remember: **Products within a family must be compatible** - this is the core guarantee of the Abstract Factory pattern.