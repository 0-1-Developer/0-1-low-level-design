# Composite Strategy Pattern - UML Diagrams

## Class Diagram

```mermaid
classDiagram
    class CompositeStrategy~T,R~ {
        <<interface>>
        +execute(input: T, context: ExecutionContext) R
    }
    
    class CompositeStrategyProcessor {
        <<utility>>
        +executeSequential(input: T, context: ExecutionContext, strategies: List~CompositeStrategy~T,T~~) T
        +executeParallel(input: T, context: ExecutionContext, strategies: List~CompositeStrategy~T,R~~) List~R~
        +executeConditional(input: T, context: ExecutionContext, conditionalStrategies: List~ConditionalStrategy~T,R~~) List~R~
        +executeUntilSuccess(input: T, context: ExecutionContext, strategies: CompositeStrategy~T,R~...) Optional~R~
        +executeWithPriority(input: T, context: ExecutionContext, priorityStrategies: List~PriorityStrategy~T,R~~, maxExecutions: int) List~R~
        +executeWithVoting(input: T, context: ExecutionContext, strategies: CompositeStrategy~T,R~...) R
    }
    
    class ConditionalStrategy~T,R~ {
        -condition: Predicate~T~
        -strategy: CompositeStrategy~T,R~
        +ConditionalStrategy(condition: Predicate~T~, strategy: CompositeStrategy~T,R~)
        +shouldExecute(input: T) boolean
        +execute(input: T, context: ExecutionContext) R
    }
    
    class PriorityStrategy~T,R~ {
        +strategy: CompositeStrategy~T,R~
        +priority: int
        +PriorityStrategy(strategy: CompositeStrategy~T,R~, priority: int)
    }
    
    class ExecutionContext {
        -data: Map~String,Object~
        -metadata: Map~String,String~
        -startTime: long
        +put(key: String, value: Object) void
        +get(key: String) T
        +get(key: String, defaultValue: T) T
        +putMetadata(key: String, value: String) void
        +getElapsedTime() long
        +getAllData() Map~String,Object~
    }
    
    class TextDocument {
        -content: String
        -title: String
        -format: String
        +getContent() String
        +getTitle() String
        +getFormat() String
        +getWordCount() int
        +getContentPreview() String
        +withContent(newContent: String) TextDocument
    }
    
    class DocumentProcessingStrategies {
        <<utility>>
        +WHITESPACE_NORMALIZER: CompositeStrategy~TextDocument,TextDocument~
        +SENTENCE_CAPITALIZER: CompositeStrategy~TextDocument,TextDocument~
        +HEADER_ADDER: CompositeStrategy~TextDocument,TextDocument~
        +FOOTER_ADDER: CompositeStrategy~TextDocument,TextDocument~
        +LINE_NUMBER_ADDER: CompositeStrategy~TextDocument,TextDocument~
        +WORD_COUNTER: CompositeStrategy~TextDocument,String~
        +createTextReplacer(searchText: String, replaceText: String) CompositeStrategy~TextDocument,TextDocument~
        +createWordFilter(wordsToRemove: String...) CompositeStrategy~TextDocument,TextDocument~
    }
    
    CompositeStrategyProcessor ..> CompositeStrategy
    CompositeStrategyProcessor ..> ConditionalStrategy
    CompositeStrategyProcessor ..> PriorityStrategy
    CompositeStrategyProcessor ..> ExecutionContext
    ConditionalStrategy --> CompositeStrategy
    PriorityStrategy --> CompositeStrategy
    DocumentProcessingStrategies ..|> CompositeStrategy
    DocumentProcessingStrategies ..> TextDocument
```

## Sequence Diagram - Sequential Processing

```mermaid
sequenceDiagram
    participant Client
    participant CompositeStrategyProcessor
    participant ExecutionContext
    participant Strategy1 as WhitespaceNormalizer
    participant Strategy2 as SentenceCapitalizer
    participant Strategy3 as HeaderAdder
    
    Client->>CompositeStrategyProcessor: executeSequential(document, context, [Strategy1, Strategy2, Strategy3])
    CompositeStrategyProcessor->>ExecutionContext: put("operation", "sequential_processing")
    
    loop For each strategy
        CompositeStrategyProcessor->>Strategy1: execute(document, context)
        Strategy1->>Strategy1: Process document content
        Strategy1->>ExecutionContext: put("step_1_completed", true)
        Strategy1-->>CompositeStrategyProcessor: processedDocument
        
        CompositeStrategyProcessor->>Strategy2: execute(processedDocument, context)
        Strategy2->>Strategy2: Capitalize sentences
        Strategy2->>ExecutionContext: put("step_2_completed", true)
        Strategy2-->>CompositeStrategyProcessor: furtherProcessed
        
        CompositeStrategyProcessor->>Strategy3: execute(furtherProcessed, context)
        Strategy3->>Strategy3: Add header
        Strategy3->>ExecutionContext: put("step_3_completed", true)
        Strategy3-->>CompositeStrategyProcessor: finalDocument
    end
    
    CompositeStrategyProcessor->>ExecutionContext: put("total_steps", 3)
    CompositeStrategyProcessor-->>Client: finalDocument
```

## Activity Diagram - Conditional Execution

```mermaid
graph TD
    A[Start Conditional Processing] --> B[Initialize Context]
    B --> C[Get Next ConditionalStrategy]
    
    C --> D{More Strategies?}
    D -->|No| E[Return Results]
    D -->|Yes| F[Evaluate Condition]
    
    F --> G{Condition Met?}
    G -->|No| H[Skip Strategy]
    G -->|Yes| I[Execute Strategy]
    
    H --> J[Update Context - Skipped]
    I --> K[Update Context - Executed]
    
    J --> L[Add to Context Count]
    K --> M[Add Result to List]
    M --> L
    L --> C
    
    E --> N[Update Final Context]
    N --> O[End]
    
    style G fill:#e1f5fe
    style I fill:#c8e6c9
    style H fill:#fff3e0
```

## State Diagram - Execution Context Lifecycle

```mermaid
stateDiagram-v2
    [*] --> Initialized
    
    Initialized --> Processing: start execution
    Processing --> Sequential: executeSequential()
    Processing --> Parallel: executeParallel()
    Processing --> Conditional: executeConditional()
    Processing --> UntilSuccess: executeUntilSuccess()
    Processing --> Voting: executeWithVoting()
    
    Sequential --> StrategyExecution: for each strategy
    Parallel --> ParallelExecution: fork strategies
    Conditional --> ConditionalCheck: evaluate conditions
    UntilSuccess --> RetryLoop: until success or exhausted
    Voting --> VoteCollection: collect results
    
    StrategyExecution --> ContextUpdate: update context
    ParallelExecution --> ResultAggregation: combine results
    ConditionalCheck --> ConditionalExecution: if condition met
    RetryLoop --> SuccessCheck: check result
    VoteCollection --> VoteCount: tally votes
    
    ContextUpdate --> Sequential: next strategy
    ContextUpdate --> Completed: all strategies done
    ResultAggregation --> Completed
    ConditionalExecution --> ContextUpdate
    SuccessCheck --> RetryLoop: if failed
    SuccessCheck --> Completed: if successful
    VoteCount --> Completed
    
    Completed --> [*]
    
    note right of ContextUpdate : Context tracks progress and intermediate results
    note right of ParallelExecution : Strategies execute concurrently
    note right of VoteCollection : Democratic strategy selection
```