# Classic Factory Method Pattern - Class Diagram

This diagram illustrates the traditional Gang of Four Factory Method pattern implementation with inheritance-based creators.

## 🏗️ Class Structure

```mermaid
classDiagram
    class Document {
        <<abstract>>
        -String title
        -String content
        +Document(String title)
        +setContent(String content)
        +getTitle() String
        +getContent() String
        +open()*
        +save()*
        +close()*
        +getDocumentType()* String
    }
    
    class TextDocument {
        +TextDocument(String title)
        +open()
        +save() 
        +close()
        +getDocumentType() String
    }
    
    class PdfDocument {
        +PdfDocument(String title)
        +open()
        +save()
        +close()
        +getDocumentType() String
    }
    
    class WordDocument {
        +WordDocument(String title)
        +open()
        +save()
        +close()
        +getDocumentType() String
    }
    
    class Application {
        <<abstract>>
        +processDocument(String title, String content)
        #createDocument(String title)* Document
        +getApplicationName()* String
    }
    
    class TextEditor {
        #createDocument(String title) Document
        +getApplicationName() String
    }
    
    class PdfReader {
        #createDocument(String title) Document
        +getApplicationName() String
    }
    
    class WordProcessor {
        #createDocument(String title) Document
        +getApplicationName() String
    }
    
    %% Inheritance relationships
    Document <|-- TextDocument
    Document <|-- PdfDocument
    Document <|-- WordDocument
    Application <|-- TextEditor
    Application <|-- PdfReader
    Application <|-- WordProcessor
    
    %% Creation relationships
    Application ..> Document : creates
    TextEditor ..> TextDocument : creates
    PdfReader ..> PdfDocument : creates
    WordProcessor ..> WordDocument : creates
    
    %% Styling
    classDef abstract fill:#ffe6e6,stroke:#ff0000,stroke-width:2px
    classDef concrete fill:#e6ffe6,stroke:#00aa00,stroke-width:2px
    classDef creator fill:#e6e6ff,stroke:#0000ff,stroke-width:2px
    
    class Document abstract
    class Application abstract
    class TextDocument,PdfDocument,WordDocument concrete
    class TextEditor,PdfReader,WordProcessor creator
```

## 🔍 Key Components

### Abstract Product (Document)
- **Purpose**: Defines the interface for objects the factory method creates
- **Key Features**: 
  - Common document operations (open, save, close)
  - Abstract methods that subclasses must implement
  - Shared state (title, content)

### Concrete Products (TextDocument, PdfDocument, WordDocument)
- **Purpose**: Implement the Document interface with specific behavior
- **Key Features**:
  - Document type-specific implementations
  - Unique file extensions and operations
  - Override all abstract methods from Document

### Abstract Creator (Application)
- **Purpose**: Declares the factory method that returns Document objects
- **Key Features**:
  - `processDocument()` - Template method that uses factory method
  - `createDocument()` - Abstract factory method overridden by subclasses
  - Business logic independent of specific product creation

### Concrete Creators (TextEditor, PdfReader, WordProcessor)
- **Purpose**: Override the factory method to return specific Document types
- **Key Features**:
  - Each creator produces one specific document type
  - Factory method implementation determines the concrete product
  - Application-specific behavior while reusing common processing logic

## 🎯 Pattern Benefits

### ✅ Advantages
- **Encapsulation**: Creation logic is encapsulated in creator subclasses
- **Extensibility**: New document types can be added without modifying existing code
- **Single Responsibility**: Each creator is responsible for creating one type
- **Open/Closed Principle**: Open for extension, closed for modification

### ⚠️ Considerations
- **Class Proliferation**: Each product type requires a corresponding creator
- **Inheritance Dependency**: Relies on inheritance hierarchy
- **Compile-time Binding**: Product type determined at compile time

## 🔄 Method Flow

1. **Client** calls `processDocument()` on a concrete creator (e.g., TextEditor)
2. **Template Method** `processDocument()` calls the factory method `createDocument()`
3. **Factory Method** in TextEditor returns a TextDocument instance
4. **Template Method** continues with document processing using the created product

## 💼 Real-World Usage

This pattern is ideal when:
- You have a stable set of product types
- Each application/context needs to create a specific type of product
- You want to encapsulate product creation logic
- The creation process might involve complex initialization

## 🔗 Relationships

- **Inheritance**: `Document ← ConcreteDocuments`, `Application ← ConcreteCreators`
- **Association**: `Application → Document` (creates and uses)
- **Dependency**: Each concrete creator depends on its specific product type