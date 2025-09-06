# Property Observer Event Flow Diagrams

## Property Change Event Flow Architecture

```mermaid
graph TB
    subgraph "Property Change Architecture"
        subgraph "Subject Layer"
            PS[Property Subject<br/>PropertyWeatherStation]
            PS --> PS1[setTemperature()]
            PS --> PS2[setHumidity()]
            PS --> PS3[setPressure()]
            PS --> PS4[setLocation()]
        end
        
        subgraph "Event Management Layer"
            PCS[PropertyChangeSupport]
            PCS --> GL[Global Listeners<br/>List~PropertyChangeListener~]
            PCS --> PL[Property-Specific Listeners<br/>Map~String, List~PropertyChangeListener~~]
            PCS --> EF[Event Factory<br/>Creates PropertyChangeEvent objects]
        end
        
        subgraph "Event Objects"
            PCE[PropertyChangeEvent<br/>• propertyName<br/>• oldValue<br/>• newValue<br/>• source<br/>• timestamp]
        end
        
        subgraph "Listener Layer"
            TAS[Temperature Alert System<br/>Listens: temperature]
            HM[Humidity Monitor<br/>Listens: humidity]
            LL[Location Logger<br/>Listens: location]
            APM[All Property Monitor<br/>Listens: ALL properties]
        end
    end
    
    PS --> PCS
    PCS --> GL
    PCS --> PL
    PCS --> EF
    EF --> PCE
    
    PCE -->|temperature events| TAS
    PCE -->|humidity events| HM
    PCE -->|location events| LL
    PCE -->|all events| APM
    
    style PS fill:#e3f2fd
    style PCS fill:#f3e5f5
    style PCE fill:#e8f5e8
```

## Property Change Event Lifecycle

```mermaid
stateDiagram-v2
    [*] --> PropertyChanged : setProperty(newValue)
    PropertyChanged --> ValueComparison : Check old vs new
    ValueComparison --> EventCreation : Values differ
    ValueComparison --> NoEvent : Values same
    NoEvent --> [*] : Skip notification
    
    EventCreation --> GlobalNotification : Create PropertyChangeEvent
    GlobalNotification --> PropertyNotification : Notify global listeners
    PropertyNotification --> EventComplete : Notify property listeners
    EventComplete --> [*] : All listeners notified
    
    note right of ValueComparison : Prevents unnecessary\nevent generation
    note right of GlobalNotification : All global listeners\nreceive every event
    note right of PropertyNotification : Only relevant property\nlisteners receive event
```

## Property-Specific Event Routing

```mermaid
graph LR
    subgraph "Property Updates"
        T[Temperature<br/>Change]
        H[Humidity<br/>Change]
        P[Pressure<br/>Change]
        L[Location<br/>Change]
    end
    
    subgraph "PropertyChangeSupport Router"
        Router[Event Router<br/>Routes by property name]
        Router --> GlobalPool[Global Listener Pool<br/>Receives ALL events]
        Router --> TempPool[Temperature Pool<br/>temperature listeners only]
        Router --> HumPool[Humidity Pool<br/>humidity listeners only]
        Router --> PressPool[Pressure Pool<br/>pressure listeners only]
        Router --> LocPool[Location Pool<br/>location listeners only]
    end
    
    subgraph "Listeners"
        GL1[Global Logger]
        GL2[All Property Monitor]
        TL1[Temperature Alert]
        TL2[Temperature Logger]
        HL1[Humidity Monitor]
        HL2[Humidity Tracker]
        PL1[Pressure Analyzer]
        LL1[Location Logger]
        LL2[GPS Tracker]
    end
    
    T --> Router
    H --> Router
    P --> Router
    L --> Router
    
    GlobalPool --> GL1
    GlobalPool --> GL2
    TempPool --> TL1
    TempPool --> TL2
    HumPool --> HL1
    HumPool --> HL2
    PressPool --> PL1
    LocPool --> LL1
    LocPool --> LL2
    
    style Router fill:#fff3e0
    style GlobalPool fill:#e1f5fe
    style TempPool fill:#ffebee
    style HumPool fill:#e8f5e8
    style PressPool fill:#f3e5f5
    style LocPool fill:#fce4ec
```

## Event Processing Pipeline

```mermaid
flowchart TD
    A[Property Setter Called] --> B[Extract Old Value]
    B --> C[Set New Value]
    C --> D{Values Different?}
    D -->|No| E[Return - No Event]
    D -->|Yes| F[Create PropertyChangeEvent]
    F --> G[Add Event Metadata]
    G --> H[Route to Global Listeners]
    H --> I[Route to Property Listeners]
    I --> J{Global Listeners Exist?}
    J -->|Yes| K[Notify Global Listeners]
    J -->|No| L[Skip Global Notification]
    K --> M{Property Listeners Exist?}
    L --> M
    M -->|Yes| N[Notify Property Listeners]
    M -->|No| O[Skip Property Notification]
    N --> P[Handle Listener Exceptions]
    O --> Q[Event Processing Complete]
    P --> Q
    
    subgraph "Error Handling"
        P --> P1[Log Exception]
        P --> P2[Continue with Next Listener]
        P --> P3[Don't Propagate to Caller]
        P1 --> Q
        P2 --> Q
        P3 --> Q
    end
    
    style F fill:#e8f5e8
    style K fill:#fff3e0
    style N fill:#fff3e0
    style P fill:#ffebee
```

## Multi-Property Update Flow

```mermaid
sequenceDiagram
    participant Client
    participant PWS as PropertyWeatherStation
    participant PCS as PropertyChangeSupport
    participant TAS as TempAlertSystem
    participant HM as HumidityMonitor
    participant APM as AllPropertyMonitor
    
    Note over Client, APM: Bulk Property Update Scenario
    Client->>PWS: updateAllMeasurements(90, 85, 29.5, "Miami")
    
    rect rgb(255, 245, 245)
        Note over PWS, APM: Temperature Property Change
        PWS->>PWS: setTemperature(90.0)
        PWS->>PCS: firePropertyChange("temperature", 75.0, 90.0)
        PCS->>PCS: create PropertyChangeEvent("temperature", 75.0, 90.0)
        par Global Listeners
            PCS->>APM: propertyChanged(tempEvent)
            APM->>APM: log temperature change
        and Property Listeners
            PCS->>TAS: propertyChanged(tempEvent)
            TAS->>TAS: temp >= threshold, trigger alert!
        end
    end
    
    rect rgb(245, 255, 245)
        Note over PWS, APM: Humidity Property Change
        PWS->>PWS: setHumidity(85.0)
        PWS->>PCS: firePropertyChange("humidity", 60.0, 85.0)
        PCS->>PCS: create PropertyChangeEvent("humidity", 60.0, 85.0)
        par Global Listeners
            PCS->>APM: propertyChanged(humidityEvent)
            APM->>APM: log humidity change
        and Property Listeners
            PCS->>HM: propertyChanged(humidityEvent)
            HM->>HM: analyze humidity trend
            HM->>HM: detect high humidity warning
        end
    end
    
    rect rgb(245, 245, 255)
        Note over PWS, APM: Pressure Property Change (No Listeners)
        PWS->>PWS: setPressure(29.5)
        PWS->>PCS: firePropertyChange("pressure", 30.1, 29.5)
        PCS->>PCS: create PropertyChangeEvent("pressure", 30.1, 29.5)
        PCS->>APM: propertyChanged(pressureEvent)
        APM->>APM: log pressure change
        Note over PCS: No property-specific pressure listeners
    end
    
    rect rgb(255, 245, 255)
        Note over PWS, APM: Location Property Change
        PWS->>PWS: setLocation("Miami")
        PWS->>PCS: firePropertyChange("location", "Chicago", "Miami")
        PCS->>PCS: create PropertyChangeEvent("location", "Chicago", "Miami")
        PCS->>APM: propertyChanged(locationEvent)
        APM->>APM: log location change
        Note over PCS: LocationLogger was removed earlier
    end
    
    Note over Client, APM: All property changes completed sequentially
```

## Property Change Event Structure

```mermaid
classDiagram
    class PropertyChangeEvent {
        -propertyName: String
        -oldValue: Object
        -newValue: Object
        -source: Object
        -timestamp: long
        -eventId: UUID
        +getPropertyName() String
        +getOldValue() Object
        +getNewValue() Object
        +getSource() Object
        +getTimestamp() long
        +getEventId() UUID
        +toString() String
        +equals(Object) boolean
        +hashCode() int
    }
    
    class PropertyMetadata {
        -propertyType: Class
        -constraints: Set~Constraint~
        -validators: List~Validator~
        -converters: List~Converter~
        +getPropertyType() Class
        +getConstraints() Set~Constraint~
        +validate(Object value) boolean
        +convert(Object value) Object
    }
    
    class EventMetrics {
        -eventCount: AtomicLong
        -lastEventTime: long
        -averageProcessingTime: double
        -errorCount: AtomicLong
        +incrementEventCount() void
        +updateProcessingTime(long time) void
        +incrementErrorCount() void
        +getMetricsSnapshot() MetricsSnapshot
    }
    
    PropertyChangeEvent --> PropertyMetadata : describes
    PropertyChangeEvent --> EventMetrics : tracked by
    
    note for PropertyChangeEvent "Immutable event object\nwith complete change information"
    note for PropertyMetadata "Optional metadata for\nvalidation and conversion"
    note for EventMetrics "Performance and\nreliability metrics"
```

## Advanced Property Event Features

```mermaid
graph TB
    subgraph "Property Event Enhancement Features"
        subgraph "Validation Layer"
            VL[Property Validation]
            VL --> VL1[Type Validation<br/>Ensure correct data type]
            VL --> VL2[Range Validation<br/>Check value bounds]
            VL --> VL3[Format Validation<br/>Validate string formats]
            VL --> VL4[Custom Validation<br/>Business rule validation]
        end
        
        subgraph "Filtering Layer"
            FL[Event Filtering]
            FL --> FL1[Value Threshold<br/>Only fire if change > threshold]
            FL --> FL2[Time Throttling<br/>Rate limit event frequency]
            FL --> FL3[Conditional Firing<br/>Fire based on conditions]
            FL --> FL4[Batch Accumulation<br/>Collect changes before firing]
        end
        
        subgraph "Transformation Layer"
            TL[Event Transformation]
            TL --> TL1[Value Conversion<br/>Convert between data types]
            TL --> TL2[Event Enrichment<br/>Add computed properties]
            TL --> TL3[Event Aggregation<br/>Combine related changes]
            TL --> TL4[Event Splitting<br/>Split complex changes]
        end
        
        subgraph "Delivery Layer"
            DL[Event Delivery Options]
            DL --> DL1[Synchronous Delivery<br/>Immediate notification]
            DL --> DL2[Asynchronous Delivery<br/>Background notification]
            DL --> DL3[Queued Delivery<br/>Ordered event processing]
            DL --> DL4[Priority Delivery<br/>Critical events first]
        end
    end
    
    VL -.-> FL
    FL -.-> TL
    TL -.-> DL
    
    style VL fill:#ffebee
    style FL fill:#fff3e0
    style TL fill:#e8f5e8
    style DL fill:#e1f5fe
```

## Property Observer vs Classic Observer Comparison

```mermaid
graph LR
    subgraph "Classic Observer"
        CO[Classic Observer Pattern]
        CO --> CO1[Subject-Centric<br/>Observer knows about<br/>entire subject state]
        CO --> CO2[Coarse-Grained<br/>All observers notified<br/>for any change]
        CO --> CO3[Pull-Based Access<br/>Observers pull data<br/>they need from subject]
        CO --> CO4[Simple Events<br/>Basic notification<br/>without change details]
    end
    
    subgraph "Property Observer"
        PO[Property Observer Pattern]
        PO --> PO1[Property-Centric<br/>Observers focus on<br/>specific properties]
        PO --> PO2[Fine-Grained<br/>Only interested observers<br/>notified per property]
        PO --> PO3[Push-Based Events<br/>Change details pushed<br/>to observers in event]
        PO --> PO4[Rich Events<br/>Events contain old/new<br/>values and metadata]
    end
    
    CO -.->|Evolution| PO
    
    subgraph "Use Case Comparison"
        UC[When to Use Each]
        UC --> UC1[Classic: Simple notifications<br/>General state changes<br/>Few observer types]
        UC --> UC2[Property: Fine-grained tracking<br/>UI data binding<br/>Property-specific logic]
    end
    
    style CO fill:#ffebee
    style PO fill:#e8f5e8
    style UC fill:#f3e5f5
```

## Property Event Performance Considerations

```mermaid
graph TB
    subgraph "Performance Factors"
        PF[Property Event Performance]
        
        subgraph "Memory Usage"
            MU[Memory Considerations]
            MU --> MU1[Event Object Creation<br/>Each change creates<br/>new event object]
            MU --> MU2[Listener Registration<br/>Maps and lists for<br/>property-specific listeners]
            MU --> MU3[Event Queue Memory<br/>Async events may<br/>accumulate in queues]
        end
        
        subgraph "Processing Overhead"
            PO[Processing Costs]
            PO --> PO1[Property Lookup<br/>Map access for finding<br/>property-specific listeners]
            PO --> PO2[Event Creation<br/>Object instantiation<br/>and initialization cost]
            PO --> PO3[Listener Iteration<br/>Loop through global<br/>and property listeners]
        end
        
        subgraph "Optimization Strategies"
            OS[Optimization Techniques]
            OS --> OS1[Listener Caching<br/>Cache frequently<br/>accessed listener lists]
            OS --> OS2[Event Pooling<br/>Reuse event objects<br/>to reduce GC pressure]
            OS --> OS3[Batch Processing<br/>Group multiple changes<br/>into single notification]
            OS --> OS4[Lazy Evaluation<br/>Defer expensive<br/>computations until needed]
        end
    end
    
    style MU fill:#ffebee
    style PO fill:#fff3e0
    style OS fill:#e8f5e8
```