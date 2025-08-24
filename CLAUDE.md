# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Repository Overview

This is a comprehensive educational repository focused on low-level design patterns and system design concepts. The repository demonstrates various design patterns with practical Java implementations, particularly focusing on the Registry Pattern as the first implemented pattern.

## Build and Run Commands

### Java Pattern Demonstrations

For any Java pattern implementation, navigate to the pattern directory and use:

```bash
# Compile all Java files (creates build directory)
javac -d build -sourcepath src/main/java src/main/java/com/example/**/*.java

# Run specific demo classes
java -cp build com.example.registry.basic.BasicRegistryDemo
java -cp build com.example.registry.factory.FactoryRegistryDemo
java -cp build com.example.registry.typesafe.TypeSafeRegistryDemo
```

Single command compilation and execution:
```bash
javac -d build -sourcepath src/main/java src/main/java/com/example/**/*.java && java -cp build [FullyQualifiedClassName]
```

## Architecture and Structure

### Repository Organization

The repository follows a pattern-based organization where each design pattern or system design has its own directory:

```
0-1-low-level-design/
├── [pattern-name]/           # Each pattern gets its own directory
│   ├── README.md             # Pattern-specific documentation
│   ├── docs/                 # UML diagrams and additional documentation
│   └── src/main/java/        # Java implementation following standard Maven structure
└── README.md                 # Main repository overview and roadmap
```

### Code Architecture Patterns

1. **Java Package Structure**: All Java code follows the standard Maven/Gradle structure under `src/main/java/com/example/[pattern]/`

2. **Pattern Variants**: Each pattern implementation includes multiple variants to demonstrate different approaches:
   - Basic/Simple implementation
   - Advanced variations (Factory-based, Type-safe, etc.)
   - Common service interfaces shared across implementations

3. **Documentation Structure**: Each pattern includes:
   - Comprehensive README with usage examples
   - UML diagrams (class and sequence) using Mermaid syntax
   - Comparison tables between different implementations
   - Real-world usage scenarios

### Key Design Principles

- **Educational Focus**: Code is written for clarity and learning, with extensive comments and documentation
- **Multiple Implementations**: Each pattern shows different approaches to solve similar problems
- **Self-Contained Examples**: Each demo class can run independently with clear output
- **Progressive Complexity**: Start with basic implementations and progress to more advanced variations

### Current Implementation Status

- **Registry Pattern**: Fully implemented with three variants (Basic, Factory, Type-Safe)
- **Other Patterns**: Planned but not yet implemented (check README.md for roadmap)

### Working with New Patterns

When implementing new patterns:
1. Create a new directory at the root level named after the pattern (e.g., `singleton-pattern/`)
2. Follow the existing structure from `registry-pattern/` as a template
3. Include multiple implementation variants to demonstrate different approaches
4. Add comprehensive UML diagrams in the `docs/uml/` subdirectory
5. Ensure all demos are self-contained and runnable with clear console output