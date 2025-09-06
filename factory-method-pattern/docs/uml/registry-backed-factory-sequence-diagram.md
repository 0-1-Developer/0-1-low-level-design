# Registry-Backed Factory Pattern - Sequence Diagram

This diagram illustrates the runtime interactions and method call flow for the registry-backed Factory Method implementation with dynamic factory registration.

## 🔄 Complete Lifecycle: Registration → Creation → Usage

```mermaid
sequenceDiagram
    participant Client
    participant PluginManager
    participant Registry as DocumentFactoryRegistry
    participant Lambda as λ TextDocument::new
    participant Product as TextDocument
    
    Note over Client,Product: Phase 1: Plugin Initialization
    
    Client->>PluginManager: initializeCorePlugins()
    activate PluginManager
    
    Note over PluginManager: Register core document types
    
    PluginManager->>Registry: registerFactory("text", TextDocument::new)
    activate Registry
    Registry->>Registry: store("text" -> λ function)
    Registry->>PluginManager: registration complete
    deactivate Registry
    
    PluginManager->>Registry: registerFactory("pdf", PdfDocument::new)
    activate Registry
    Registry->>Registry: store("pdf" -> λ function)
    Registry->>PluginManager: registration complete
    deactivate Registry
    
    PluginManager->>Registry: registerFactory("word", WordDocument::new)
    activate Registry
    Registry->>Registry: store("word" -> λ function)
    Registry->>PluginManager: registration complete
    deactivate Registry
    
    PluginManager->>Client: core plugins initialized
    deactivate PluginManager
    
    Note over Client,Product: Phase 2: Document Creation and Processing
    
    Client->>Registry: createDocument("text", "Project Report")
    activate Registry
    
    Registry->>Registry: lookup("text")
    Note over Registry: Find registered λ function
    
    Registry->>Lambda: apply("Project Report")
    activate Lambda
    
    Lambda->>Product: new TextDocument("Project Report")
    activate Product
    Product->>Lambda: document instance
    deactivate Product
    
    Lambda->>Registry: return document
    deactivate Lambda
    
    Registry->>Client: TextDocument instance
    deactivate Registry
    
    Note over Client,Product: Phase 3: Document Usage
    
    Client->>Product: open()
    activate Product
    Product->>Client: opened
    deactivate Product
    
    Client->>Product: setContent("Quarterly Analysis")
    activate Product
    Product->>Client: content set
    deactivate Product
    
    Client->>Product: save()
    activate Product
    Product->>Client: saved as .txt
    deactivate Product
    
    Client->>Product: close()
    activate Product
    Product->>Client: closed
    deactivate Product
```

## 🚀 Advanced Plugin Loading

```mermaid
sequenceDiagram
    participant Client
    participant PluginManager
    participant Registry as DocumentFactoryRegistry
    participant CustomPlugin as Custom Plugin
    participant CustomDoc as CustomDocument
    
    Note over Client,CustomDoc: Dynamic Plugin Registration
    
    Client->>PluginManager: loadAdvancedPlugins()
    activate PluginManager
    
    Note over PluginManager: Load custom plugins
    
    PluginManager->>Registry: registerFactory("rtf", λ)
    activate Registry
    Note over Registry: λ = title -> new CustomDocument(title, "RTF")
    Registry->>PluginManager: registered
    deactivate Registry
    
    PluginManager->>Registry: registerFactory("odt", λ)
    activate Registry
    Note over Registry: λ = title -> new CustomDocument(title, "ODT")
    Registry->>PluginManager: registered
    deactivate Registry
    
    PluginManager->>Client: advanced plugins loaded
    deactivate PluginManager
    
    Note over Client,CustomDoc: Use Newly Registered Type
    
    Client->>Registry: createDocument("rtf", "Rich Format Doc")
    activate Registry
    
    Registry->>Registry: lookup("rtf")
    Registry->>CustomPlugin: apply("Rich Format Doc")
    activate CustomPlugin
    
    CustomPlugin->>CustomDoc: new CustomDocument("Rich Format Doc", "RTF")
    activate CustomDoc
    CustomDoc->>CustomPlugin: instance
    deactivate CustomDoc
    
    CustomPlugin->>Registry: custom document
    deactivate CustomPlugin
    
    Registry->>Client: CustomDocument instance
    deactivate Registry
    
    Client->>CustomDoc: open(), save(), close()
    activate CustomDoc
    Note over CustomDoc: RTF-specific behavior
    CustomDoc->>Client: operations complete
    deactivate CustomDoc
```

## 🔧 Runtime Factory Management

```mermaid
sequenceDiagram
    participant Client
    participant Registry as DocumentFactoryRegistry
    participant OldFactory as Existing λ Function
    participant NewFactory as New λ Function
    participant Product as Document
    
    Note over Client,Product: Check Factory Availability
    
    Client->>Registry: isTypeSupported("excel")
    activate Registry
    Registry->>Registry: check registry
    Registry->>Client: false
    deactivate Registry
    
    Note over Client,Product: Runtime Registration
    
    Client->>Registry: registerFactory("excel", λ)
    activate Registry
    Note over Registry: λ = title -> new ExcelDocument(title)
    Registry->>Registry: store("excel" -> λ)
    Registry->>Client: registration complete
    deactivate Registry
    
    Note over Client,Product: Verify Registration
    
    Client->>Registry: isTypeSupported("excel")
    activate Registry
    Registry->>Registry: check registry
    Registry->>Client: true
    deactivate Registry
    
    Client->>Registry: getRegisteredTypes()
    activate Registry
    Registry->>Client: ["text", "pdf", "word", "excel", ...]
    deactivate Registry
    
    Note over Client,Product: Factory Override/Update
    
    Client->>Registry: registerFactory("excel", λ_new)
    activate Registry
    Note over Registry: Replace existing factory
    Registry->>OldFactory: ✗ remove
    Registry->>Registry: store("excel" -> λ_new)
    Registry->>Client: factory updated
    deactivate Registry
    
    Note over Client,Product: Use Updated Factory
    
    Client->>Registry: createDocument("excel", "Spreadsheet")
    activate Registry
    Registry->>NewFactory: apply("Spreadsheet")
    activate NewFactory
    NewFactory->>Product: new ImprovedExcelDocument("Spreadsheet")
    activate Product
    Product->>NewFactory: instance
    deactivate Product
    NewFactory->>Registry: document
    deactivate NewFactory
    Registry->>Client: improved document
    deactivate Registry
```

## 🔍 Type Alias Support

```mermaid
sequenceDiagram
    participant Client
    participant Registry as DocumentFactoryRegistry
    participant SharedFactory as Shared λ Function
    participant Product as TextDocument
    
    Note over Client,Product: Register Type Aliases
    
    Client->>Registry: registerFactory("text", TextDocument::new)
    activate Registry
    Registry->>Registry: store("text" -> λ)
    Registry->>Client: registered
    deactivate Registry
    
    Client->>Registry: registerFactory("txt", TextDocument::new)
    activate Registry
    Note over Registry: Same factory, different alias
    Registry->>Registry: store("txt" -> λ)
    Registry->>Client: alias registered
    deactivate Registry
    
    Note over Client,Product: Use Different Aliases
    
    par Access via "text"
        Client->>Registry: createDocument("text", "Doc1")
        Registry->>SharedFactory: apply("Doc1")
        SharedFactory->>Product: new TextDocument("Doc1")
        Product->>Registry: instance
        Registry->>Client: TextDocument
    and Access via "txt"  
        Client->>Registry: createDocument("txt", "Doc2")
        Registry->>SharedFactory: apply("Doc2")
        SharedFactory->>Product: new TextDocument("Doc2")
        Product->>Registry: instance
        Registry->>Client: TextDocument
    end
    
    Note over Client,Product: Both aliases create same product type
```

## 🚫 Error Handling and Validation

```mermaid
sequenceDiagram
    participant Client
    participant Registry as DocumentFactoryRegistry
    participant Factory as λ Function
    
    Note over Client,Factory: Error Scenarios
    
    Client->>Registry: createDocument("unsupported", "Doc")
    activate Registry
    
    Registry->>Registry: lookup("unsupported")
    Note over Registry: Factory not found
    
    Registry->>Client: IllegalArgumentException:<br/>"No factory registered for type: unsupported"
    deactivate Registry
    
    Note over Client,Factory: Factory Exception Handling
    
    Client->>Registry: createDocument("problematic", "Doc")
    activate Registry
    
    Registry->>Registry: lookup("problematic")
    Registry->>Factory: apply("Doc")
    activate Factory
    
    Factory->>Factory: RuntimeException during creation
    Factory->>Registry: RuntimeException: "Creation failed"
    deactivate Factory
    
    Registry->>Client: RuntimeException: "Creation failed"
    deactivate Registry
    
    Note over Client,Factory: Null Safety
    
    alt Factory returns null
        Registry->>Factory: apply("Doc")
        Factory->>Registry: null
        Registry->>Client: IllegalStateException: "Factory returned null"
    else Factory returns valid instance
        Registry->>Factory: apply("Doc")
        Factory->>Registry: valid document
        Registry->>Client: document instance
    end
```

## 🧹 Registry Management Operations

```mermaid
sequenceDiagram
    participant Admin
    participant Registry as DocumentFactoryRegistry
    participant Factory1 as λ Function 1
    participant Factory2 as λ Function 2
    
    Note over Admin,Factory2: Registry Maintenance
    
    Admin->>Registry: getRegisteredFactoryCount()
    activate Registry
    Registry->>Admin: 5
    deactivate Registry
    
    Admin->>Registry: getRegisteredTypes()
    activate Registry
    Registry->>Admin: ["text", "pdf", "word", "html", "xml"]
    deactivate Registry
    
    Note over Admin,Factory2: Selective Cleanup
    
    Admin->>Registry: unregisterFactory("xml")
    activate Registry
    Registry->>Registry: remove("xml")
    Registry->>Factory2: ✗ remove reference
    Registry->>Admin: "xml" factory removed
    deactivate Registry
    
    Note over Admin,Factory2: Bulk Cleanup
    
    Admin->>Registry: clearRegistry()
    activate Registry
    Registry->>Registry: clear all mappings
    Registry->>Factory1: ✗ remove all references
    Registry->>Admin: registry cleared
    deactivate Registry
    
    Admin->>Registry: getRegisteredFactoryCount()
    activate Registry
    Registry->>Admin: 0
    deactivate Registry
```

## 📈 Performance Characteristics

### Memory Usage
- **Registry Storage**: O(n) where n = number of registered types
- **Function References**: Minimal memory overhead per factory
- **Product Creation**: One instance per creation call

### Lookup Performance
- **Type Lookup**: O(1) HashMap access
- **Factory Execution**: O(1) function call
- **Registration**: O(1) HashMap insertion

### Concurrency Considerations
```mermaid
sequenceDiagram
    participant Thread1
    participant Thread2
    participant Registry as ConcurrentHashMap
    participant Factory as λ Function
    
    Note over Thread1,Factory: Concurrent Access
    
    par Thread 1
        Thread1->>Registry: createDocument("text", "Doc1")
        Registry->>Factory: apply("Doc1")
        Factory->>Thread1: TextDocument 1
    and Thread 2
        Thread2->>Registry: createDocument("pdf", "Doc2")  
        Registry->>Factory: apply("Doc2")
        Factory->>Thread2: PdfDocument 2
    end
    
    Note over Thread1,Factory: Thread-safe registry operations
```

## 🎯 Key Benefits Demonstrated

1. **Runtime Extensibility**: New factories can be registered without code changes
2. **Type Flexibility**: Multiple aliases for same factory function
3. **Plugin Architecture**: Perfect for modular, extensible applications
4. **Performance**: Fast O(1) lookups with minimal memory overhead
5. **Error Handling**: Comprehensive validation and error reporting
6. **Management**: Full lifecycle management of factory registrations