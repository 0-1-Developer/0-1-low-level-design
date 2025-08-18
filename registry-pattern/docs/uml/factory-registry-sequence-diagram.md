# Factory Registry Pattern - Sequence Diagram

## Factory Registration and Lazy Initialization Flow

```mermaid
sequenceDiagram
    participant Demo as FactoryRegistryDemo
    participant Registry as FactoryServiceRegistry
    participant FactoryMap as factoryMap
    participant Cache as singletonCache
    participant EmailFactory as EmailService::new
    participant SMSFactory as SMSService::new
    participant EmailSvc as EmailService
    participant SMSSvc as SMSService
    
    Note over Demo: Factory Registration Phase
    
    %% Get Registry Instance
    Demo->>Registry: getInstance()
    Registry-->>Demo: registry instance
    
    %% Register Prototype Factory
    Demo->>Registry: registerPrototypeFactory("EMAIL_PROTO", EmailService::new)
    Registry->>FactoryMap: put("EMAIL_PROTO", factory)
    FactoryMap-->>Registry: stored
    Registry-->>Demo: void
    
    %% Register Singleton Factory
    Demo->>Registry: registerSingletonFactory("SMS_SINGLE", SMSService::new)
    Note over Registry: Wraps factory with singleton behavior
    Registry->>FactoryMap: put("SMS_SINGLE", singletonWrapper)
    FactoryMap-->>Registry: stored
    Registry-->>Demo: void
    
    Note over Demo: No services created yet! (Lazy)
    
    Note over Demo: First Service Request (Prototype)
    
    %% Get Prototype Service - First Call
    Demo->>Registry: getService("EMAIL_PROTO")
    Registry->>FactoryMap: get("EMAIL_PROTO")
    FactoryMap-->>Registry: factory
    Registry->>EmailFactory: get() // Factory invocation
    EmailFactory->>EmailSvc: new EmailService()
    EmailSvc-->>EmailFactory: instance1
    EmailFactory-->>Registry: instance1
    Registry-->>Demo: instance1
    
    %% Get Prototype Service - Second Call
    Demo->>Registry: getService("EMAIL_PROTO")
    Registry->>FactoryMap: get("EMAIL_PROTO")
    FactoryMap-->>Registry: factory
    Registry->>EmailFactory: get() // Factory invocation again
    EmailFactory->>EmailSvc: new EmailService()
    EmailSvc-->>EmailFactory: instance2
    EmailFactory-->>Registry: instance2
    Registry-->>Demo: instance2
    
    Note over Demo: instance1 != instance2 (Different objects)
    
    Note over Demo: First Singleton Request
    
    %% Get Singleton Service - First Call
    Demo->>Registry: getService("SMS_SINGLE")
    Registry->>FactoryMap: get("SMS_SINGLE")
    FactoryMap-->>Registry: singletonWrapper
    Registry->>Cache: computeIfAbsent("SMS_SINGLE", factory)
    Cache->>SMSFactory: get() // Factory invocation
    SMSFactory->>SMSSvc: new SMSService()
    SMSSvc-->>SMSFactory: singletonInstance
    SMSFactory-->>Cache: singletonInstance
    Cache->>Cache: put("SMS_SINGLE", singletonInstance)
    Cache-->>Registry: singletonInstance
    Registry-->>Demo: singletonInstance
    
    Note over Demo: Second Singleton Request
    
    %% Get Singleton Service - Second Call
    Demo->>Registry: getService("SMS_SINGLE")
    Registry->>FactoryMap: get("SMS_SINGLE")
    FactoryMap-->>Registry: singletonWrapper
    Registry->>Cache: computeIfAbsent("SMS_SINGLE", factory)
    Cache-->>Registry: singletonInstance (cached)
    Registry-->>Demo: singletonInstance
    
    Note over Demo: Same instance returned (Cached)
    
    Note over Demo: Cache Management
    
    %% Clear Singleton Cache
    Demo->>Registry: clearSingletonCache()
    Registry->>Cache: clear()
    Cache-->>Registry: cleared
    Registry-->>Demo: void
    
    Note over Demo: Next singleton request will create new instance
```

## Factory Pattern Benefits Demonstrated

### 1. Lazy Initialization
- **No Upfront Cost**: Services created only when first requested
- **Memory Efficient**: Unused services never consume memory
- **Startup Performance**: Faster application startup

### 2. Flexible Object Lifecycles

#### Prototype Scope
```java
registry.registerPrototypeFactory("EMAIL", EmailService::new);
// Each call creates a new instance
```

#### Singleton Scope
```java
registry.registerSingletonFactory("SMS", SMSService::new);
// First call creates and caches, subsequent calls return cached instance
```

### 3. Factory Function Storage
- Stores `Supplier<Service>` functions, not instances
- Factory invoked on-demand
- Supports complex initialization logic

## Key Interaction Points

1. **Factory Registration**: Stores factory functions, not instances
2. **Lazy Creation**: Objects created on first `getService()` call
3. **Prototype vs Singleton**: Different caching strategies
4. **Cache Management**: Singleton instances cached and reused
5. **Memory Efficiency**: Unused factories never execute