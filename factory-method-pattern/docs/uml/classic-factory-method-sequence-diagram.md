# Classic Factory Method Pattern - Sequence Diagram

This diagram illustrates the runtime interactions and method call flow for the classic inheritance-based Factory Method pattern.

## 🔄 Sequence Flow

```mermaid
sequenceDiagram
    participant Client
    participant TextEditor as TextEditor<br/>(Concrete Creator)
    participant Application as Application<br/>(Abstract Creator)
    participant TextDocument as TextDocument<br/>(Concrete Product)
    
    Note over Client,TextDocument: Document Processing Flow
    
    Client->>TextEditor: processDocument("Report", "Content")
    activate TextEditor
    
    TextEditor->>Application: processDocument("Report", "Content")
    activate Application
    Note over Application: Template method execution starts
    
    Application->>TextEditor: createDocument("Report")
    activate TextEditor
    Note over TextEditor: Factory method override
    
    TextEditor->>TextDocument: new TextDocument("Report")
    activate TextDocument
    TextDocument->>TextEditor: document instance
    deactivate TextDocument
    
    TextEditor->>Application: return document
    deactivate TextEditor
    
    Note over Application: Continue with template method
    Application->>TextDocument: open()
    activate TextDocument
    Note over TextDocument: Document-specific implementation
    TextDocument->>Application: opened
    deactivate TextDocument
    
    Application->>TextDocument: setContent("Content")
    activate TextDocument
    TextDocument->>Application: content set
    deactivate TextDocument
    
    Application->>TextDocument: save()
    activate TextDocument
    Note over TextDocument: Save with .txt extension
    TextDocument->>Application: saved
    deactivate TextDocument
    
    Application->>TextDocument: close()
    activate TextDocument
    TextDocument->>Application: closed
    deactivate TextDocument
    
    Note over Application: Template method complete
    Application->>TextEditor: processing complete
    deactivate Application
    
    TextEditor->>Client: success
    deactivate TextEditor
```

## 🎯 Key Interaction Points

### 1. Template Method Pattern Integration
The Factory Method works seamlessly with the Template Method pattern:
- **Template Method**: `processDocument()` defines the algorithm skeleton
- **Factory Method**: `createDocument()` allows subclasses to customize object creation
- **Business Logic**: Remains constant across all creators

### 2. Factory Method Override
```mermaid
sequenceDiagram
    participant Client
    participant PdfReader
    participant Application
    participant PdfDocument
    
    Note over Client,PdfDocument: Same flow, different product
    
    Client->>PdfReader: processDocument("Manual", "Instructions")
    PdfReader->>Application: processDocument("Manual", "Instructions")
    Application->>PdfReader: createDocument("Manual")
    
    Note over PdfReader: Different factory implementation
    PdfReader->>PdfDocument: new PdfDocument("Manual")
    PdfDocument->>PdfReader: document instance
    PdfReader->>Application: return document
    
    Note over Application: Same template method continues...
    Application->>PdfDocument: open()
    Application->>PdfDocument: setContent("Instructions")
    Application->>PdfDocument: save()
    Note over PdfDocument: Saves as .pdf file
    Application->>PdfDocument: close()
```

### 3. Polymorphism in Action
Each concrete creator produces a different product type, but the client and template method treat all products uniformly through the Document interface.

## 📊 Timing and Lifecycle

```mermaid
sequenceDiagram
    participant Client
    participant Creator
    participant Product
    
    rect rgb(255, 240, 240)
        Note over Client,Product: Creation Phase
        Client->>Creator: processDocument()
        Creator->>Creator: createDocument() - Factory Method
        Creator->>Product: new ConcreteProduct()
        Product->>Creator: instance
    end
    
    rect rgb(240, 255, 240)
        Note over Client,Product: Usage Phase
        Creator->>Product: open()
        Creator->>Product: setContent()
        Creator->>Product: save()
        Creator->>Product: close()
    end
    
    rect rgb(240, 240, 255)
        Note over Client,Product: Completion Phase
        Creator->>Client: processing complete
        Note over Product: Product lifecycle managed by creator
    end
```

## 🔍 Pattern Variations

### Multiple Products in Single Operation
```mermaid
sequenceDiagram
    participant Client
    participant Creator
    participant Product1 as Document 1
    participant Product2 as Document 2
    
    Client->>Creator: processBatch(["Doc1", "Doc2"])
    
    loop For each document
        Creator->>Creator: createDocument(title)
        Creator->>Product1: new Document(title)
        Product1->>Creator: instance
        Creator->>Product1: process operations
    end
    
    Creator->>Client: batch complete
```

### Error Handling Flow
```mermaid
sequenceDiagram
    participant Client
    participant Creator
    participant Product
    
    Client->>Creator: processDocument("Invalid", "Content")
    Creator->>Creator: createDocument("Invalid")
    
    alt Factory method fails
        Creator->>Creator: handle creation error
        Creator->>Client: error: Unable to create document
    else Factory method succeeds
        Creator->>Product: new Product("Invalid")
        Product->>Creator: instance
        
        alt Product operation fails
            Creator->>Product: open()
            Product->>Creator: error: Cannot open
            Creator->>Client: error: Processing failed
        else Success path
            Creator->>Product: open(), setContent(), save(), close()
            Creator->>Client: success
        end
    end
```

## 🎯 Key Benefits Illustrated

### 1. **Encapsulation of Creation Logic**
- Factory method encapsulates the decision of which concrete product to create
- Template method doesn't need to know about specific product types

### 2. **Extensibility**
- New creator/product pairs can be added without modifying existing code
- Each creator can have specialized creation logic

### 3. **Consistent Interface Usage**
- All products used through the same Document interface
- Template method works with any product type

## 💼 Real-World Timing Considerations

### Performance Characteristics
- **Creation Time**: Single object creation per operation
- **Memory Usage**: One product instance at a time in template method
- **Scalability**: Linear with number of documents processed

### Thread Safety
```mermaid
sequenceDiagram
    participant Thread1 as Thread 1
    participant Thread2 as Thread 2  
    participant Creator1 as TextEditor Instance 1
    participant Creator2 as TextEditor Instance 2
    participant Product1 as TextDocument 1
    participant Product2 as TextDocument 2
    
    Note over Thread1,Product2: Concurrent Processing
    
    par Thread 1 Processing
        Thread1->>Creator1: processDocument("Doc1", "Content1")
        Creator1->>Product1: create and process
        Product1->>Creator1: complete
        Creator1->>Thread1: success
    and Thread 2 Processing
        Thread2->>Creator2: processDocument("Doc2", "Content2")
        Creator2->>Product2: create and process
        Product2->>Creator2: complete
        Creator2->>Thread2: success
    end
    
    Note over Thread1,Product2: Safe - separate instances
```

## 🔗 Related Pattern Interactions

The sequence diagram shows how Factory Method integrates with:
- **Template Method**: Providing customization points in algorithms
- **Strategy Pattern**: Different creators can be seen as different strategies
- **Prototype Pattern**: Could be combined for cloning-based creation