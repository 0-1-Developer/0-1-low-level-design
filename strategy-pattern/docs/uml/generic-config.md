# Generic Type-Safe & Config-Driven Strategy Patterns - UML Diagrams

## Generic Type-Safe Strategy Pattern - Class Diagram

```mermaid
classDiagram
    class Strategy~T,R~ {
        <<interface>>
        +execute(input: T, context: ValidationContext) ValidationResult~R~
    }
    
    class ValidationContext {
        -metadata: Map~String,Object~
        -validationRules: Map~String,Object~
        +put(key: String, value: Object) void
        +get(key: String) T
        +addValidationRule(rule: String, value: Object) void
        +hasValidationRule(rule: String) boolean
    }
    
    class ValidationResult~T~ {
        -valid: boolean
        -result: T
        -errors: List~String~
        -warnings: List~String~
        +isValid() boolean
        +getResult() T
        +getErrors() List~String~
        +addError(error: String) void
        +addWarning(warning: String) void
    }
    
    class ValidationStrategies {
        <<utility>>
        +EMAIL_VALIDATOR: Strategy~String,String~
        +USERNAME_VALIDATOR: Strategy~String,String~
        +PASSWORD_VALIDATOR: Strategy~String,String~
        +AGE_VALIDATOR: Strategy~Integer,Integer~
        +ADULT_AGE_VALIDATOR: Strategy~Integer,Integer~
        +USER_VALIDATOR: Strategy~User,User~
        +ADULT_USER_VALIDATOR: Strategy~User,User~
        +createLengthValidator(min: int, max: int) Strategy~String,String~
        +createRangeValidator(min: int, max: int) Strategy~Integer,Integer~
        +combineValidators(validators: Strategy~T,T~...) Strategy~T,T~
    }
    
    class User {
        -username: String
        -email: String
        -age: int
        +getUsername() String
        +getEmail() String
        +getAge() int
        +setters()
    }
    
    Strategy --> ValidationContext
    Strategy --> ValidationResult
    ValidationStrategies ..|> Strategy
    ValidationStrategies ..> User
    User ..> ValidationResult
```

## Config-Driven Strategy Pattern - Class Diagram

```mermaid
classDiagram
    class ConfigurableStrategy~T,R~ {
        <<interface>>
        +execute(input: T, config: StrategyConfig) R
        +getName() String
        +getDefaultConfig() StrategyConfig
        +validateConfig(config: StrategyConfig) boolean
    }
    
    class ConfigurableStrategyManager~T,R~ {
        -strategies: Map~String,ConfigurableStrategy~T,R~~
        -configurations: Map~String,StrategyConfig~
        -activeStrategy: String
        +registerStrategy(name: String, strategy: ConfigurableStrategy~T,R~) void
        +registerStrategy(name: String, strategy: ConfigurableStrategy~T,R~, config: StrategyConfig) void
        +setActiveStrategy(name: String) void
        +updateConfiguration(strategyName: String, config: StrategyConfig) void
        +execute(input: T) R
        +execute(input: T, customConfig: StrategyConfig) R
        +getConfiguration(strategyName: String) StrategyConfig
        +loadConfiguration(configSource: String) void
        +getRegisteredStrategies() Set~String~
    }
    
    class StrategyConfig {
        -properties: Map~String,Object~
        +put(key: String, value: Object) void
        +get(key: String) T
        +get(key: String, defaultValue: T) T
        +containsKey(key: String) boolean
        +remove(key: String) Object
        +clear() void
        +size() int
        +merge(other: StrategyConfig) StrategyConfig
        +clone() StrategyConfig
    }
    
    class ReportRequest {
        -id: String
        -type: String
        -fields: List~String~
        -dateRange: DateRange
        -user: String
        +getters/setters()
    }
    
    class ReportResult {
        -id: String
        -format: String
        -content: String
        -size: int
        -generationTime: long
        -warnings: int
        +getters/setters()
        +getContentPreview() String
    }
    
    class ReportGenerationStrategies {
        <<utility>>
        +CSV_STRATEGY: ConfigurableStrategy~ReportRequest,ReportResult~
        +JSON_STRATEGY: ConfigurableStrategy~ReportRequest,ReportResult~
        +HTML_STRATEGY: ConfigurableStrategy~ReportRequest,ReportResult~
        +XML_STRATEGY: ConfigurableStrategy~ReportRequest,ReportResult~
        +SUMMARY_STRATEGY: ConfigurableStrategy~ReportRequest,ReportResult~
        -generateSampleData(request: ReportRequest, config: StrategyConfig) List~Map~String,Object~~
        -formatDateTime(date: Date, format: String) String
        -generateReportId() String
    }
    
    ConfigurableStrategyManager --> ConfigurableStrategy
    ConfigurableStrategyManager --> StrategyConfig
    ConfigurableStrategy --> StrategyConfig
    ReportGenerationStrategies ..|> ConfigurableStrategy
    ReportGenerationStrategies ..> ReportRequest
    ReportGenerationStrategies ..> ReportResult
```

## Sequence Diagram - Generic Validation Pipeline

```mermaid
sequenceDiagram
    participant Client
    participant ValidationContext
    participant UserValidator
    participant EmailValidator
    participant AgeValidator
    participant ValidationResult
    
    Client->>ValidationContext: create context
    Client->>ValidationContext: addValidationRule("strict_mode", true)
    
    Client->>UserValidator: execute(user, context)
    UserValidator->>EmailValidator: execute(user.getEmail(), context)
    EmailValidator->>EmailValidator: validate email format
    EmailValidator-->>UserValidator: ValidationResult(valid=true)
    
    UserValidator->>AgeValidator: execute(user.getAge(), context)
    AgeValidator->>AgeValidator: check age >= 18
    AgeValidator-->>UserValidator: ValidationResult(valid=false, "Must be 18+")
    
    UserValidator->>ValidationResult: create combined result
    ValidationResult->>ValidationResult: merge validation results
    UserValidator-->>Client: ValidationResult(valid=false, errors=["Must be 18+"])
```

## Activity Diagram - Config-Driven Strategy Execution

```mermaid
graph TD
    A[Start Config-Driven Execution] --> B[Load Strategy Configuration]
    B --> C{Configuration Source?}
    
    C -->|File| D[Load from Properties File]
    C -->|Database| E[Load from Database]
    C -->|Environment| F[Load from Environment Variables]
    C -->|Runtime| G[Use Runtime Configuration]
    
    D --> H[Parse Configuration]
    E --> H
    F --> H
    G --> H
    
    H --> I[Validate Configuration]
    I --> J{Configuration Valid?}
    
    J -->|No| K[Use Default Configuration]
    J -->|Yes| L[Apply Configuration]
    
    K --> M[Log Configuration Warning]
    L --> N[Execute Strategy with Config]
    M --> N
    
    N --> O[Strategy Execution]
    O --> P{Execution Successful?}
    
    P -->|No| Q[Log Error and Return Failure]
    P -->|Yes| R[Collect Metrics]
    
    R --> S[Return Result]
    Q --> T[End]
    S --> T
    
    style H fill:#e3f2fd
    style I fill:#fff3e0
    style N fill:#c8e6c9
    style Q fill:#ffcdd2
```

## State Diagram - Configuration Lifecycle

```mermaid
stateDiagram-v2
    [*] --> Uninitialized
    
    Uninitialized --> Loading: loadConfiguration()
    Loading --> Validating: configuration loaded
    Loading --> Failed: load error
    
    Validating --> Valid: validation passed
    Validating --> Invalid: validation failed
    Invalid --> Loading: retry with corrections
    
    Valid --> Active: applyConfiguration()
    Active --> Updating: updateConfiguration()
    Active --> Executing: strategy execution
    
    Updating --> Validating: new config loaded
    Executing --> Active: execution complete
    
    Failed --> [*]
    
    state Loading {
        [*] --> FileSystem: from file
        [*] --> Database: from database
        [*] --> Environment: from env vars
        [*] --> Runtime: runtime config
        
        FileSystem --> [*]
        Database --> [*]
        Environment --> [*]
        Runtime --> [*]
    }
    
    state Executing {
        [*] --> PreExecution
        PreExecution --> ConfigMerge: merge with defaults
        ConfigMerge --> StrategyExecution: execute with config
        StrategyExecution --> PostExecution: collect metrics
        PostExecution --> [*]
    }
    
    note right of Active : Configuration ready for strategy execution
    note left of Updating : Hot configuration updates supported
    note right of Executing : Strategy uses merged configuration
```