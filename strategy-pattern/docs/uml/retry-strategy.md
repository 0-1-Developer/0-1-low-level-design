# Retry/Fallback Strategy Pattern - UML Diagrams

## Class Diagram

```mermaid
classDiagram
    class RetryableStrategy~T,R~ {
        <<interface>>
        +execute(input: T) R
        +getName() String
    }
    
    class RetryableStrategyExecutor~T,R~ {
        -strategies: Map~String,RetryableStrategy~T,R~~
        -retryPolicies: Map~String,RetryPolicy~
        -fallbackStrategies: Map~String,FallbackStrategy~T,R~~
        -activeStrategy: String
        -defaultRetryPolicy: RetryPolicy
        
        +registerStrategy(name: String, strategy: RetryableStrategy~T,R~) void
        +registerStrategy(name: String, strategy: RetryableStrategy~T,R~, retryPolicy: RetryPolicy) void
        +registerWithFallback(name: String, strategy: RetryableStrategy~T,R~, fallback: FallbackStrategy~T,R~) void
        +setActiveStrategy(name: String) void
        +executeWithRetry(input: T) RetryResult~R~
        +executeRace(input: T, strategyNames: String...) RetryResult~R~
        +executeFallbackChain(input: T, strategyNames: String...) RetryResult~R~
        -shouldRetry(exception: Exception, policy: RetryPolicy) boolean
        -calculateDelay(attempt: int, policy: RetryPolicy) Duration
    }
    
    class RetryPolicy {
        -maxAttempts: int
        -baseDelay: Duration
        -maxDelay: Duration
        -backoffMultiplier: double
        -jitter: boolean
        -retryableExceptions: Set~Class~Exception~~
        
        +getMaxAttempts() int
        +getBaseDelay() Duration
        +shouldRetry(exception: Exception) boolean
        +calculateDelay(attempt: int) Duration
        +withJitter(delay: Duration) Duration
    }
    
    class FallbackStrategy~T,R~ {
        <<interface>>
        +fallback(input: T, originalException: Exception) R
        +getName() String
    }
    
    class RetryResult~R~ {
        -success: boolean
        -result: R
        -attempts: int
        -executionTime: long
        -strategy: String
        -fallbackUsed: boolean
        -finalError: String
        
        +isSuccess() boolean
        +getResult() R
        +getAttempts() int
        +getExecutionTime() long
        +wasFallbackUsed() boolean
    }
    
    class NetworkRequest {
        -id: String
        -method: String
        -url: String
        -timeout: int
        +getters/setters()
    }
    
    class NetworkResponse {
        -id: String
        -status: int
        -responseTime: long
        -server: String
        -fromCache: boolean
        +getters/setters()
    }
    
    class NetworkStrategies {
        <<utility>>
        +PRIMARY_SERVER: RetryableStrategy~NetworkRequest,NetworkResponse~
        +SECONDARY_SERVER: RetryableStrategy~NetworkRequest,NetworkResponse~
        +CACHE_STRATEGY: RetryableStrategy~NetworkRequest,NetworkResponse~
        +DATABASE_STRATEGY: RetryableStrategy~NetworkRequest,NetworkResponse~
        +createMockStrategy(name: String) RetryableStrategy~NetworkRequest,NetworkResponse~
        +createUnreliableStrategy(failureRate: double) RetryableStrategy~NetworkRequest,NetworkResponse~
    }
    
    RetryableStrategyExecutor --> RetryableStrategy
    RetryableStrategyExecutor --> RetryPolicy
    RetryableStrategyExecutor --> FallbackStrategy
    RetryableStrategyExecutor ..> RetryResult
    NetworkStrategies ..|> RetryableStrategy
    NetworkStrategies ..> NetworkRequest
    NetworkStrategies ..> NetworkResponse
```

## Sequence Diagram - Retry with Fallback

```mermaid
sequenceDiagram
    participant Client
    participant RetryableStrategyExecutor
    participant RetryPolicy
    participant PrimaryStrategy
    participant FallbackStrategy
    
    Client->>RetryableStrategyExecutor: executeWithRetry(networkRequest)
    RetryableStrategyExecutor->>RetryPolicy: getMaxAttempts()
    RetryPolicy-->>RetryableStrategyExecutor: 3
    
    loop Retry attempts (max 3)
        RetryableStrategyExecutor->>PrimaryStrategy: execute(networkRequest)
        PrimaryStrategy-->>RetryableStrategyExecutor: throws Exception("Service unavailable")
        
        RetryableStrategyExecutor->>RetryPolicy: shouldRetry(exception)
        RetryPolicy-->>RetryableStrategyExecutor: true
        
        RetryableStrategyExecutor->>RetryPolicy: calculateDelay(attemptNumber)
        RetryPolicy-->>RetryableStrategyExecutor: Duration.ofSeconds(2^attempt)
        
        RetryableStrategyExecutor->>RetryableStrategyExecutor: sleep(delay)
    end
    
    note over RetryableStrategyExecutor: All retries exhausted
    
    RetryableStrategyExecutor->>FallbackStrategy: fallback(networkRequest, lastException)
    FallbackStrategy-->>RetryableStrategyExecutor: NetworkResponse (from cache)
    
    RetryableStrategyExecutor->>RetryableStrategyExecutor: Create RetryResult
    RetryableStrategyExecutor-->>Client: RetryResult(success=true, fallbackUsed=true)
```

## Activity Diagram - Race Execution

```mermaid
graph TD
    A[Start Race Execution] --> B[Create CompletableFutures for all strategies]
    B --> C[Submit all strategies to executor]
    
    C --> D[CompletableFuture.anyOf()]
    D --> E{First strategy completes}
    
    E -->|Success| F[Cancel remaining futures]
    E -->|Failure| G{All strategies failed?}
    
    G -->|No| H[Wait for next completion]
    G -->|Yes| I[All strategies failed]
    
    H --> E
    
    F --> J[Create successful RetryResult]
    I --> K[Create failed RetryResult with all errors]
    
    J --> L[Return result]
    K --> L
    L --> M[End]
    
    style F fill:#c8e6c9
    style I fill:#ffcdd2
    style J fill:#c8e6c9
    style K fill:#ffcdd2
```

## State Diagram - Strategy Execution States

```mermaid
stateDiagram-v2
    [*] --> Ready
    
    Ready --> Executing: execute()
    
    Executing --> Success: operation successful
    Executing --> Retrying: operation failed, retries available
    Executing --> FallbackRequired: operation failed, no retries left
    
    Retrying --> Waiting: calculate delay
    Waiting --> Executing: delay elapsed
    
    FallbackRequired --> ExecutingFallback: fallback available
    FallbackRequired --> Failed: no fallback available
    
    ExecutingFallback --> Success: fallback successful
    ExecutingFallback --> Failed: fallback failed
    
    Success --> [*]
    Failed --> [*]
    
    state Executing {
        [*] --> AttemptInProgress
        AttemptInProgress --> CheckResult
        CheckResult --> [*]
    }
    
    state Retrying {
        [*] --> PolicyCheck
        PolicyCheck --> DelayCalculation: should retry
        PolicyCheck --> [*]: max attempts reached
        DelayCalculation --> [*]
    }
    
    note right of Retrying : Exponential backoff with jitter
    note right of ExecutingFallback : Last resort strategy
    note left of Success : Record metrics and timing
```

## Component Diagram - Retry Strategy Architecture

```mermaid
graph TB
    subgraph "Client Layer"
        Client[Client Application]
    end
    
    subgraph "Strategy Execution Layer"
        Executor[RetryableStrategyExecutor]
        PolicyManager[RetryPolicy Manager]
        FallbackManager[Fallback Manager]
    end
    
    subgraph "Strategy Implementations"
        Primary[Primary Strategy]
        Secondary[Secondary Strategy]
        Cache[Cache Strategy]
        Database[Database Strategy]
        Mock[Mock Strategy]
    end
    
    subgraph "Fallback Strategies"
        DefaultResponse[Default Response]
        CacheFallback[Cache Fallback]
        MockResponse[Mock Response]
    end
    
    subgraph "Monitoring & Metrics"
        Metrics[Performance Metrics]
        Logger[Error Logger]
        HealthChecker[Health Checker]
    end
    
    Client --> Executor
    Executor --> PolicyManager
    Executor --> FallbackManager
    Executor --> Primary
    Executor --> Secondary
    Executor --> Cache
    Executor --> Database
    Executor --> Mock
    
    FallbackManager --> DefaultResponse
    FallbackManager --> CacheFallback
    FallbackManager --> MockResponse
    
    Executor --> Metrics
    Executor --> Logger
    Executor --> HealthChecker
    
    style Executor fill:#e3f2fd
    style PolicyManager fill:#f3e5f5
    style FallbackManager fill:#e8f5e8
```