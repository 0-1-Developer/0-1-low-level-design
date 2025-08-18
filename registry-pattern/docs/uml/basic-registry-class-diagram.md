# Basic Registry Pattern - Class Diagram

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
    
    class BasicServiceRegistry {
        -INSTANCE: BasicServiceRegistry$
        -serviceMap: Map~String, Service~
        -BasicServiceRegistry()
        +getInstance()$ BasicServiceRegistry
        +registerService(key: String, service: Service) void
        +getService(key: String) Service
        +hasService(key: String) boolean
        +unregisterService(key: String) Service
        +getServiceCount() int
        +clear() void
    }
    
    class BasicRegistryDemo {
        +main(args: String[])$ void
    }
    
    %% Relationships
    Service <|.. EmailService : implements
    Service <|.. SMSService : implements
    Service <|.. PushNotificationService : implements
    
    BasicServiceRegistry --> Service : stores
    BasicRegistryDemo --> BasicServiceRegistry : uses
    BasicRegistryDemo --> EmailService : creates
    BasicRegistryDemo --> SMSService : creates
    BasicRegistryDemo --> PushNotificationService : creates
    
    %% Notes
    note for BasicServiceRegistry "Singleton pattern\nThread-safe with ConcurrentHashMap\nStores pre-created instances\nString-based key lookup"
    note for Service "Common interface\nEnsures consistent contract"
```

## Key Features

- **Singleton Pattern**: `BasicServiceRegistry` ensures only one instance exists
- **String Keys**: Services are identified by string keys like "EMAIL", "SMS"
- **Eager Initialization**: Services must be created before registration
- **Thread Safety**: Uses `ConcurrentHashMap` for thread-safe operations
- **Simple Lookup**: Direct key-to-service mapping