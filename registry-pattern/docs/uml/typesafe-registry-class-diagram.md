# Type-Safe Registry Pattern - Class Diagram

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
    
    class Class~T~ {
        <<Java Class Object>>
        +cast(obj: Object) T
        +getSimpleName() String
    }
    
    class TypeSafeRegistry {
        -INSTANCE: TypeSafeRegistry$
        -registry: Map~Class~?~, Object~
        -TypeSafeRegistry()
        +getInstance()$ TypeSafeRegistry
        +register~T~(type: Class~T~, instance: T) void
        +get~T~(type: Class~T~) T
        +hasType(type: Class~?~) boolean
        +unregister~T~(type: Class~T~) T
        +getRegisteredCount() int
        +clear() void
        +getRegisteredTypes() Set~Class~?~~
    }
    
    class TypeSafeRegistryDemo {
        +main(args: String[])$ void
        -demonstrateGenericRetrieval~T~(registry: TypeSafeRegistry, type: Class~T~)$ void
    }
    
    %% Relationships
    Service <|.. EmailService : implements
    Service <|.. SMSService : implements
    Service <|.. PushNotificationService : implements
    
    TypeSafeRegistry --> Class : uses as keys
    TypeSafeRegistry --> Object : stores instances
    TypeSafeRegistryDemo --> TypeSafeRegistry : uses
    TypeSafeRegistryDemo --> EmailService : creates & registers
    TypeSafeRegistryDemo --> SMSService : creates & registers
    TypeSafeRegistryDemo --> PushNotificationService : creates & registers
    
    %% Notes
    note for TypeSafeRegistry "Singleton pattern\nUses Class~T~ as keys\nCompile-time type safety\nNo manual casting required"
    note for Class "Java reflection Class object\nProvides type information\nEnables safe casting"
```

## Key Features

- **Type Safety**: Uses `Class<T>` objects as keys instead of strings
- **Compile-Time Checking**: Prevents type mismatches at compile time
- **No Casting**: Return types automatically inferred from `Class<T>` parameter
- **Generics**: Leverages Java generics for type safety
- **Refactoring Safe**: Class renames automatically update keys
- **IDE Support**: Full auto-completion and type hints
- **Safe Casting**: Internal casting guaranteed safe by registration contract

## Type Safety Benefits

```java
// ✅ Type-safe - no casting needed
EmailService emailService = registry.get(EmailService.class);

// ❌ Compile error - prevents wrong assignments
SMSService wrongType = registry.get(EmailService.class); // Won't compile!