# Interface-Based Factory Pattern - Sequence Diagram

This diagram illustrates the runtime interactions and method call flow for the interface-based Factory Method implementation with composition.

## 🔄 Basic Sequence Flow

```mermaid
sequenceDiagram
    participant Client
    participant Processor as DocumentProcessor<br/>(Client)
    participant Factory as TextDocumentFactory<br/>(Concrete Factory)
    participant Product as TextDocument<br/>(Concrete Product)
    
    Note over Client,Product: Initialization Phase
    
    Client->>Factory: new TextDocumentFactory()
    activate Factory
    Factory->>Client: factory instance
    deactivate Factory
    
    Client->>Processor: new DocumentProcessor(factory)
    activate Processor
    Note over Processor: Store factory reference
    Processor->>Client: processor instance
    deactivate Processor
    
    Note over Client,Product: Processing Phase
    
    Client->>Processor: processDocument("Report", "Analysis data")
    activate Processor
    
    Processor->>Factory: getFactoryType()
    activate Factory
    Factory->>Processor: "Text Document Factory"
    deactivate Factory
    
    Note over Processor: Log which factory is being used
    
    Processor->>Factory: createDocument("Report")
    activate Factory
    
    Factory->>Product: new TextDocument("Report")
    activate Product
    Product->>Factory: document instance
    deactivate Product
    
    Factory->>Processor: return document
    deactivate Factory
    
    Note over Processor: Process document using factory-created instance
    
    Processor->>Product: open()
    activate Product
    Note over Product: Text-specific open behavior
    Product->>Processor: opened
    deactivate Product
    
    Processor->>Product: setContent("Analysis data")
    activate Product
    Product->>Processor: content set
    deactivate Product
    
    Processor->>Product: save()
    activate Product
    Note over Product: Save as .txt file
    Product->>Processor: saved
    deactivate Product
    
    Processor->>Product: close()
    activate Product
    Product->>Processor: closed
    deactivate Product
    
    Processor->>Client: processing complete
    deactivate Processor
```

## 🔄 Runtime Factory Switching

```mermaid
sequenceDiagram
    participant Client
    participant Processor as DocumentProcessor
    participant TextFactory as TextDocumentFactory
    participant PdfFactory as PdfDocumentFactory
    participant TextDoc as TextDocument
    participant PdfDoc as PdfDocument
    
    Note over Client,PdfDoc: Initial Processing
    
    Client->>Processor: processDocument("Doc1", "Content1")
    Processor->>TextFactory: createDocument("Doc1")
    TextFactory->>TextDoc: new TextDocument("Doc1")
    TextDoc->>Processor: process and complete
    
    Note over Client,PdfDoc: Runtime Factory Switch
    
    Client->>Processor: switchFactory(pdfFactory)
    activate Processor
    Note over Processor: Replace factory reference
    Processor->>Client: factory switched
    deactivate Processor
    
    Note over Client,PdfDoc: Processing with New Factory
    
    Client->>Processor: processDocument("Doc2", "Content2")
    Processor->>PdfFactory: createDocument("Doc2")
    PdfFactory->>PdfDoc: new PdfDocument("Doc2")
    PdfDoc->>Processor: process and complete
    
    Note over Client,PdfDoc: Same processor, different products!
```

## 🧪 Testing Sequence with Mock Factory

```mermaid
sequenceDiagram
    participant Test as Test Case
    participant Processor as DocumentProcessor
    participant MockFactory as MockDocumentFactory<br/>(Test Double)
    participant MockDoc as MockDocument<br/>(Test Product)
    
    Note over Test,MockDoc: Test Setup Phase
    
    Test->>MockFactory: new MockDocumentFactory()
    activate MockFactory
    MockFactory->>Test: mock factory instance
    deactivate MockFactory
    
    Test->>Processor: new DocumentProcessor(mockFactory)
    activate Processor
    Processor->>Test: processor instance
    deactivate Processor
    
    Note over Test,MockDoc: Test Execution Phase
    
    Test->>Processor: processDocument("TestDoc", "TestContent")
    activate Processor
    
    Processor->>MockFactory: createDocument("TestDoc")
    activate MockFactory
    Note over MockFactory: Increment call counter<br/>Store last title
    
    MockFactory->>MockDoc: new MockDocument("TestDoc")
    activate MockDoc
    MockDoc->>MockFactory: mock instance
    deactivate MockDoc
    
    MockFactory->>Processor: return mock document
    deactivate MockFactory
    
    Processor->>MockDoc: open()
    activate MockDoc
    Note over MockDoc: Set opened flag
    MockDoc->>Processor: mock opened
    deactivate MockDoc
    
    Processor->>MockDoc: setContent("TestContent")
    activate MockDoc
    Note over MockDoc: Set content and flag
    MockDoc->>Processor: mock content set
    deactivate MockDoc
    
    Processor->>MockDoc: save()
    activate MockDoc
    Note over MockDoc: Set saved flag
    MockDoc->>Processor: mock saved
    deactivate MockDoc
    
    Processor->>MockDoc: close()
    activate MockDoc
    Note over MockDoc: Set closed flag
    MockDoc->>Processor: mock closed
    deactivate MockDoc
    
    Processor->>Test: processing complete
    deactivate Processor
    
    Note over Test,MockDoc: Test Verification Phase
    
    Test->>MockFactory: getCreateCallCount()
    activate MockFactory
    MockFactory->>Test: 1
    deactivate MockFactory
    
    Test->>MockFactory: getLastRequestedTitle()
    activate MockFactory
    MockFactory->>Test: "TestDoc"
    deactivate MockFactory
    
    Test->>MockDoc: wasOpened()
    activate MockDoc
    MockDoc->>Test: true
    deactivate MockDoc
    
    Test->>MockDoc: wasSaved()
    activate MockDoc
    MockDoc->>Test: true
    deactivate MockDoc
    
    Note over Test: Assert all verifications pass
```

## 📊 Parallel Processing Capabilities

```mermaid
sequenceDiagram
    participant Client
    participant Processor1 as DocumentProcessor 1
    participant Processor2 as DocumentProcessor 2
    participant Factory1 as TextDocumentFactory
    participant Factory2 as PdfDocumentFactory
    participant Product1 as TextDocument
    participant Product2 as PdfDocument
    
    Note over Client,Product2: Concurrent Processing with Different Factories
    
    par Thread 1 - Text Processing
        Client->>Processor1: processDocument("Text Report", "Data")
        Processor1->>Factory1: createDocument("Text Report")
        Factory1->>Product1: new TextDocument("Text Report")
        Product1->>Factory1: instance
        Factory1->>Processor1: document
        Processor1->>Product1: open(), setContent(), save(), close()
        Processor1->>Client: Text processing complete
    and Thread 2 - PDF Processing
        Client->>Processor2: processDocument("PDF Report", "Data")
        Processor2->>Factory2: createDocument("PDF Report")
        Factory2->>Product2: new PdfDocument("PDF Report")
        Product2->>Factory2: instance
        Factory2->>Processor2: document
        Processor2->>Product2: open(), setContent(), save(), close()
        Processor2->>Client: PDF processing complete
    end
    
    Note over Client,Product2: Both processors operate independently
```

## 🔄 Batch Processing Sequence

```mermaid
sequenceDiagram
    participant Client
    participant Processor as DocumentProcessor
    participant Factory as DocumentFactory
    participant Products as Multiple Documents
    
    Client->>Processor: processBatch(["Doc1", "Doc2", "Doc3"], "Content")
    activate Processor
    
    Note over Processor: Log batch processing start
    
    loop For each document title
        Processor->>Factory: createDocument(title)
        activate Factory
        Factory->>Products: new Document(title)
        activate Products
        Products->>Factory: instance
        deactivate Products
        Factory->>Processor: document
        deactivate Factory
        
        Note over Processor: Process individual document
        Processor->>Products: open(), setContent(), save(), close()
        activate Products
        Products->>Processor: operations complete
        deactivate Products
    end
    
    Processor->>Client: batch processing complete
    deactivate Processor
```

## 🎯 Key Advantages Illustrated

### 1. **Flexible Composition**
- DocumentProcessor can work with any factory implementation
- Runtime switching enables different processing strategies
- No inheritance coupling between processor and factories

### 2. **Easy Testing**
- Mock factories can be injected for isolated testing
- Verification of factory interactions possible
- Test doubles can simulate various scenarios (errors, delays)

### 3. **Scalability**
- Multiple processors can use different factories concurrently
- Factory instances can be shared or dedicated per processor
- Easy to add new factory types without changing existing code

## 💼 Error Handling Flows

```mermaid
sequenceDiagram
    participant Client
    participant Processor as DocumentProcessor
    participant Factory as DocumentFactory
    
    Client->>Processor: processDocument("Invalid", "Content")
    activate Processor
    
    Processor->>Factory: createDocument("Invalid")
    activate Factory
    
    alt Factory throws exception
        Factory->>Processor: CreationException
        deactivate Factory
        Processor->>Client: ProcessingException: "Cannot create document"
        deactivate Processor
    else Factory returns null
        Factory->>Processor: null
        deactivate Factory
        Processor->>Client: ProcessingException: "Factory returned null"
        deactivate Processor
    else Success path
        Factory->>Processor: valid document
        deactivate Factory
        Note over Processor: Continue with normal processing
        Processor->>Client: success
        deactivate Processor
    end
```

## 🔗 Integration Patterns

The interface-based approach integrates well with:
- **Dependency Injection**: Factories injected via containers
- **Observer Pattern**: Factories can notify observers of creation events
- **Decorator Pattern**: Factory results can be decorated with additional behavior
- **Command Pattern**: Factory creation can be encapsulated in commands