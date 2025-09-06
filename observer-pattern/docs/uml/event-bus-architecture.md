# Event Bus Architecture - Pub-Sub Observer Pattern

## High-Level Event Bus Architecture

```mermaid
graph TB
    subgraph "Publishers Layer"
        WS[Weather Station<br/>Publisher]
        ES[Emergency System<br/>Publisher]
        SS[Sensor System<br/>Publisher]
        MS[Monitoring Service<br/>Publisher]
    end
    
    subgraph "Event Bus Core"
        EB[Event Bus<br/>Message Router]
        subgraph "Topic Registry"
            TR[Topic Registry<br/>temperature: [S1, S4]<br/>humidity: [S2]<br/>weather.severe: [S3]<br/>sensor.data: [S5, S6]]
        end
        subgraph "Event Queue"
            EQ[Event Processing<br/>Queue & Dispatcher]
        end
    end
    
    subgraph "Subscribers Layer"
        S1[Temperature Alert<br/>Subscriber]
        S2[Humidity Monitor<br/>Subscriber]
        S3[Emergency Alert<br/>Subscriber]
        S4[Data Logger<br/>Subscriber]
        S5[Analytics Service<br/>Subscriber]
        S6[Archive Service<br/>Subscriber]
    end
    
    WS -->|publish events| EB
    ES -->|publish events| EB
    SS -->|publish events| EB
    MS -->|publish events| EB
    
    EB --> TR
    EB --> EQ
    
    EB -->|route by topic| S1
    EB -->|route by topic| S2
    EB -->|route by topic| S3
    EB -->|route by topic| S4
    EB -->|route by topic| S5
    EB -->|route by topic| S6
    
    style EB fill:#e1f5fe
    style TR fill:#f3e5f5
    style EQ fill:#e8f5e8
```

## Event Bus Component Detail

```mermaid
classDiagram
    class EventBus {
        -subscribers: Map~String, List~Consumer~Event~~~
        -busName: String
        -eventQueue: Queue~Event~
        -dispatcher: EventDispatcher
        +subscribe(String topic, Consumer~Event~ subscriber) void
        +unsubscribe(String topic, Consumer~Event~ subscriber) void
        +publish(Event event) void
        +publishAsync(Event event) CompletableFuture~Void~
        +getSubscriberCount(String topic) int
        +getTotalSubscriberCount() int
        +getTopics() Set~String~
        -dispatchEvent(Event event) void
        -handleSubscriberError(Throwable error, Event event) void
    }
    
    class Event {
        -topic: String
        -data: Object
        -timestamp: long
        -eventId: String
        -priority: EventPriority
        -metadata: Map~String, Object~
        +getTopic() String
        +getData() Object
        +getTimestamp() long
        +getEventId() String
        +getPriority() EventPriority
        +getMetadata() Map~String, Object~
        +addMetadata(String key, Object value) void
        +toString() String
    }
    
    class EventDispatcher {
        -executorService: ExecutorService
        -errorHandler: Consumer~EventError~
        +dispatch(Event event, List~Consumer~Event~~) void
        +dispatchAsync(Event event, List~Consumer~Event~~) CompletableFuture~Void~
        +shutdown() void
    }
    
    class EventSubscriber {
        <<interface>>
        +onEvent(Event event) void
        +getSubscribedTopics() Set~String~
        +canHandle(Event event) boolean
    }
    
    class Publisher {
        <<abstract>>
        #eventBus: EventBus
        +publish(String topic, Object data) void
        +publishWithMetadata(String topic, Object data, Map~String, Object~) void
        #createEvent(String topic, Object data) Event
    }
    
    class Subscriber {
        <<abstract>>
        #subscribedTopics: Set~String~
        +subscribe(EventBus bus) void
        +unsubscribe(EventBus bus) void
        +handleEvent(Event event) void
    }
    
    EventBus *-- EventDispatcher
    EventBus ..> Event : processes
    Publisher --> EventBus : publishes to
    Subscriber --> EventBus : subscribes to
    EventSubscriber <|.. Subscriber
    Event --> EventSubscriber : delivered to
```

## Topic-based Routing Architecture

```mermaid
graph LR
    subgraph "Event Sources"
        ES1[Weather Station<br/>Events]
        ES2[IoT Sensors<br/>Events]
        ES3[Emergency Systems<br/>Events]
        ES4[User Actions<br/>Events]
    end
    
    subgraph "Event Bus Router"
        subgraph "Topic Namespace"
            TN1[weather.*<br/>├── weather.temperature<br/>├── weather.humidity<br/>├── weather.severe<br/>└── weather.forecast]
            TN2[sensor.*<br/>├── sensor.data<br/>├── sensor.status<br/>├── sensor.alert<br/>└── sensor.diagnostic]
            TN3[system.*<br/>├── system.startup<br/>├── system.shutdown<br/>├── system.error<br/>└── system.maintenance]
            TN4[user.*<br/>├── user.login<br/>├── user.action<br/>├── user.preference<br/>└── user.feedback]
        end
        
        subgraph "Routing Engine"
            RE[Topic Matcher<br/>• Exact match<br/>• Wildcard support<br/>• Pattern matching<br/>• Priority routing]
        end
    end
    
    subgraph "Subscriber Groups"
        SG1[Weather Apps<br/>Subscribe to: weather.*]
        SG2[Monitoring Systems<br/>Subscribe to: sensor.*, system.*]
        SG3[Emergency Services<br/>Subscribe to: weather.severe, sensor.alert]
        SG4[Analytics<br/>Subscribe to: *.data, *.status]
        SG5[Audit Systems<br/>Subscribe to: user.*, system.*]
    end
    
    ES1 --> TN1
    ES2 --> TN2
    ES3 --> TN3
    ES4 --> TN4
    
    TN1 --> RE
    TN2 --> RE
    TN3 --> RE
    TN4 --> RE
    
    RE --> SG1
    RE --> SG2
    RE --> SG3
    RE --> SG4
    RE --> SG5
    
    style RE fill:#fff3e0
    style TN1 fill:#e3f2fd
    style TN2 fill:#e8f5e8
    style TN3 fill:#fce4ec
    style TN4 fill:#f3e5f5
```

## Event Processing Pipeline

```mermaid
flowchart TD
    A[Event Published] --> B{Event Validation}
    B -->|Invalid| C[Reject Event]
    B -->|Valid| D[Add to Event Queue]
    D --> E[Topic Resolution]
    E --> F{Subscribers Found?}
    F -->|No| G[Log No Subscribers]
    F -->|Yes| H[Create Delivery Tasks]
    H --> I{Sync or Async?}
    I -->|Sync| J[Sequential Delivery]
    I -->|Async| K[Parallel Delivery]
    J --> L[Deliver to Subscriber 1]
    L --> M[Deliver to Subscriber 2]
    M --> N[Deliver to Subscriber N]
    K --> O[Concurrent Delivery Pool]
    O --> P[Subscriber 1 Task]
    O --> Q[Subscriber 2 Task]
    O --> R[Subscriber N Task]
    N --> S[Delivery Complete]
    P --> S
    Q --> S
    R --> S
    S --> T{Any Errors?}
    T -->|Yes| U[Error Handling]
    T -->|No| V[Success Metrics]
    U --> W[Dead Letter Queue]
    U --> X[Retry Logic]
    U --> Y[Error Notifications]
    V --> Z[Event Processing Complete]
    W --> Z
    X --> Z
    Y --> Z
    
    style D fill:#e8f5e8
    style H fill:#fff3e0
    style O fill:#e1f5fe
    style U fill:#ffebee
```

## Error Handling and Resilience

```mermaid
graph TB
    subgraph "Error Handling Strategy"
        EH[Event Bus Error Handling]
        EH --> EH1[Subscriber Isolation<br/>One subscriber failure<br/>doesn't affect others]
        EH --> EH2[Dead Letter Queue<br/>Failed events stored<br/>for later processing]
        EH --> EH3[Retry Mechanism<br/>Configurable retry<br/>with backoff strategy]
        EH --> EH4[Circuit Breaker<br/>Disable failing<br/>subscribers temporarily]
        EH --> EH5[Error Notifications<br/>Alert administrators<br/>of system issues]
    end
    
    subgraph "Resilience Features"
        RF[Resilience Architecture]
        RF --> RF1[Async Processing<br/>Non-blocking event<br/>delivery options]
        RF --> RF2[Bounded Queues<br/>Prevent memory<br/>overflow scenarios]
        RF --> RF3[Graceful Shutdown<br/>Complete pending<br/>events before stop]
        RF --> RF4[Health Monitoring<br/>Track subscriber<br/>and bus health]
        RF --> RF5[Event Replay<br/>Reprocess events<br/>from specific point]
    end
    
    subgraph "Monitoring and Metrics"
        MM[Monitoring System]
        MM --> MM1[Event Throughput<br/>Events/second<br/>processing rates]
        MM --> MM2[Subscriber Metrics<br/>Success/failure<br/>rates per subscriber]
        MM --> MM3[Topic Analytics<br/>Popular topics<br/>and usage patterns]
        MM --> MM4[Performance Metrics<br/>Latency and<br/>processing times]
        MM --> MM5[System Health<br/>Queue sizes and<br/>resource usage]
    end
    
    style EH fill:#ffebee
    style RF fill:#e8f5e8
    style MM fill:#e3f2fd
```

## Scalability Architecture

```mermaid
graph TB
    subgraph "Single Node Event Bus"
        SN[Single Node Architecture]
        SN --> SN1[In-Memory Topics<br/>Fast but limited<br/>by single machine]
        SN --> SN2[Local Subscribers<br/>All subscribers<br/>in same process]
        SN --> SN3[Synchronous Processing<br/>Direct method calls<br/>for event delivery]
    end
    
    subgraph "Clustered Event Bus"
        CN[Clustered Architecture]
        CN --> CN1[Distributed Topics<br/>Topics replicated<br/>across cluster nodes]
        CN --> CN2[Remote Subscribers<br/>Subscribers can be<br/>on different nodes]
        CN --> CN3[Network Communication<br/>Events sent over<br/>network protocols]
    end
    
    subgraph "Enterprise Event Bus"
        EN[Enterprise Architecture]
        EN --> EN1[Message Broker<br/>External message<br/>broker (Kafka, RabbitMQ)]
        EN --> EN2[Persistent Storage<br/>Events stored<br/>for durability]
        EN --> EN3[Load Balancing<br/>Multiple event bus<br/>instances for scale]
        EN --> EN4[Cross-System Integration<br/>Connect different<br/>systems and services]
    end
    
    SN -.->|Scale Up| CN
    CN -.->|Scale Out| EN
    
    style SN fill:#e8f5e8
    style CN fill:#fff3e0
    style EN fill:#e1f5fe
```

## Comparison with Direct Observer

```mermaid
graph LR
    subgraph "Direct Observer Pattern"
        DO[Direct Observer]
        DO --> DO1[Subject ↔ Observer<br/>Direct coupling<br/>Subject knows observers]
        DO --> DO2[Method Calls<br/>Direct method<br/>invocation on observers]
        DO --> DO3[Synchronous<br/>Sequential notification<br/>of all observers]
        DO --> DO4[Error Propagation<br/>Observer errors<br/>affect subject]
    end
    
    subgraph "Event Bus Observer"
        EBO[Event Bus Observer]
        EBO --> EBO1[Publisher ↔ Bus ↔ Subscriber<br/>Loose coupling<br/>via message bus]
        EBO --> EBO2[Event Messages<br/>Structured event<br/>objects with metadata]
        EBO --> EBO3[Async Capable<br/>Parallel processing<br/>and delivery options]
        EBO --> EBO4[Error Isolation<br/>Subscriber errors<br/>don't affect others]
    end
    
    DO -.->|Evolution| EBO
    
    style DO fill:#ffebee
    style EBO fill:#e8f5e8
    
    subgraph "Trade-offs"
        T[Comparison Summary]
        T --> T1[Direct: Faster, Simpler]
        T --> T2[Event Bus: More Flexible, Scalable]
        T --> T3[Direct: Tighter Coupling]
        T --> T4[Event Bus: Better Isolation]
    end
```