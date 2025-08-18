# Factory Registry Pattern - Class Diagram

```mermaid
classDiagram
    class Service {
        <<interface>>
        +getName() String
        +execute() void
    }
    
    class EmailService {
        +getName() String
        +execute() void
        +sendEmail(recipient: String, subject: String) void
    }
    
    class SMSService {
        +getName() String
        +execute() void
        +sendSMS(phoneNumber: String, message: String) void
    }
    
    class PushNotificationService {
        +getName() String
        +execute() void
        +sendPushNotification(deviceId: String, title: String, body: String) void
    }
    
    class Supplier~Service~ {
        <<interface>>
        +get() Service
    }
    
    class FactoryServiceRegistry {
        -INSTANCE: FactoryServiceRegistry$
        -factoryMap: Map~String, Supplier~Service~~
        -singletonCache: Map~String, Service~
        -FactoryServiceRegistry()
        +getInstance()$ FactoryServiceRegistry
        +registerPrototypeFactory(key: String, factory: Supplier~Service~) void
        +registerSingletonFactory(key: String, factory: Supplier~Service~) void
        +getService(key: String) Service
        +hasFactory(key: String) boolean
        +unregisterFactory(key: String) boolean
        +getFactoryCount() int
        +clear() void
        +clearSingletonCache() void
        +getSingletonCacheSize() int
        -validateInputs(key: String, factory: Supplier~Service~) void
    }
    
    class FactoryRegistryDemo {
        +main(args: String[])$ void
    }
    
    %% Relationships
    Service <|.. EmailService : implements
    Service <|.. SMSService : implements
    Service <|.. PushNotificationService : implements
    
    FactoryServiceRegistry --> Supplier : stores factories
    Supplier --> Service : creates
    FactoryServiceRegistry --> Service : caches singletons
    FactoryRegistryDemo --> FactoryServiceRegistry : uses
    FactoryRegistryDemo ..> EmailService : references in factory
    FactoryRegistryDemo ..> SMSService : references in factory
    FactoryRegistryDemo ..> PushNotificationService : references in factory
    
    %% Notes
    note for FactoryServiceRegistry "Singleton pattern\nStores factory functions\nSupports prototype & singleton scopes\nLazy initialization"
    note for Supplier "Java 8 functional interface\nFactory function for services"
```

## Key Features

- **Factory Storage**: Stores `Supplier<Service>` functions instead of instances
- **Lazy Initialization**: Services created only when first requested
- **Dual Scopes**: 
  - **Prototype**: New instance each time (`registerPrototypeFactory`)
  - **Singleton**: Cached instance (`registerSingletonFactory`)
- **Memory Efficient**: Unused services are never created
- **Flexible Lifecycle**: Different object creation strategies per service
- **Caching**: Singleton instances cached for reuse