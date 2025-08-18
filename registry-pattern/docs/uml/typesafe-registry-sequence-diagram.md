# Type-Safe Registry Pattern - Sequence Diagram

## Type-Safe Registration and Retrieval Flow

```mermaid
sequenceDiagram
    participant Demo as TypeSafeRegistryDemo
    participant Registry as TypeSafeRegistry
    participant RegMap as registry (Map)
    participant EmailClass as EmailService.class
    participant SMSClass as SMSService.class
    participant EmailSvc as EmailService
    participant SMSSvc as SMSService
    
    Note over Demo: Type-Safe Registration Phase
    
    %% Get Registry Instance
    Demo->>Registry: getInstance()
    Registry-->>Demo: registry instance
    
    %% Create Services (Eager Initialization)
    Demo->>EmailSvc: new EmailService()
    EmailSvc-->>Demo: emailService
    
    Demo->>SMSSvc: new SMSService()
    SMSSvc-->>Demo: smsService
    
    %% Type-Safe Registration
    Demo->>Registry: register(EmailService.class, emailService)
    Note over Registry: Generics ensure type T matches Class<T>
    Registry->>RegMap: put(EmailService.class, emailService)
    RegMap-->>Registry: stored
    Registry-->>Demo: void
    
    Demo->>Registry: register(SMSService.class, smsService)
    Registry->>RegMap: put(SMSService.class, smsService)
    RegMap-->>Registry: stored
    Registry-->>Demo: void
    
    Note over Demo: Type-Safe Retrieval Phase
    
    %% Get Email Service (Type-Safe)
    Demo->>Registry: get(EmailService.class)
    Registry->>RegMap: get(EmailService.class)
    RegMap-->>Registry: emailService (Object)
    Registry->>EmailClass: cast(emailService)
    EmailClass-->>Registry: emailService (EmailService)
    Registry-->>Demo: emailService (EmailService)
    
    Note over Demo: No casting needed! Return type inferred
    
    %% Direct Method Call (No Casting Required)
    Demo->>EmailSvc: sendEmail("user@example.com", "Welcome!")
    EmailSvc-->>Demo: "Sending email to: user@example.com..."
    
    %% Get SMS Service (Type-Safe)
    Demo->>Registry: get(SMSService.class)
    Registry->>RegMap: get(SMSService.class)
    RegMap-->>Registry: smsService (Object)
    Registry->>SMSClass: cast(smsService)
    SMSClass-->>Registry: smsService (SMSService)
    Registry-->>Demo: smsService (SMSService)
    
    %% Direct Method Call (No Casting Required)
    Demo->>SMSSvc: sendSMS("+1234567890", "Hello World!")
    SMSSvc-->>Demo: "Sending SMS to: +1234567890..."
    
    Note over Demo: Compile-Time Safety Demonstration
    
    %% This would cause COMPILE ERROR (shown for illustration)
    Note over Demo: SMSService wrong = registry.get(EmailService.class);<br/>// ❌ Compile Error: Type mismatch!
    
    Note over Demo: Generic Method Usage
    
    %% Generic Method Call
    Demo->>Demo: demonstrateGenericRetrieval(registry, EmailService.class)
    Demo->>Registry: get(EmailService.class)
    Registry->>RegMap: get(EmailService.class)
    RegMap-->>Registry: emailService
    Registry->>EmailClass: cast(emailService)
    EmailClass-->>Registry: emailService (EmailService)
    Registry-->>Demo: emailService (EmailService)
    
    Note over Demo: Type Management Operations
    
    %% Check Type Existence
    Demo->>Registry: hasType(EmailService.class)
    Registry->>RegMap: containsKey(EmailService.class)
    RegMap-->>Registry: true
    Registry-->>Demo: true
    
    %% Unregister Type-Safe
    Demo->>Registry: unregister(EmailService.class)
    Registry->>RegMap: remove(EmailService.class)
    RegMap-->>Registry: emailService (Object)
    Registry->>EmailClass: cast(emailService)
    EmailClass-->>Registry: emailService (EmailService)
    Registry-->>Demo: emailService (EmailService)
    
    %% Get Registered Types
    Demo->>Registry: getRegisteredTypes()
    Registry->>RegMap: keySet()
    RegMap-->>Registry: Set<Class<?>>
    Registry-->>Demo: Set<Class<?>>
```

## Type Safety Benefits Demonstrated

### 1. Compile-Time Type Checking
```java
// ✅ This works - types match
EmailService email = registry.get(EmailService.class);

// ❌ This won't compile - type mismatch detected at compile time
SMSService sms = registry.get(EmailService.class); // Compile Error!
```

### 2. No Manual Casting Required
```java
// Basic Registry (requires casting)
EmailService email = (EmailService) basicRegistry.getService("EMAIL");

// Type-Safe Registry (no casting)
EmailService email = typeSafeRegistry.get(EmailService.class);
```

### 3. Refactoring Safety
- Class renames automatically update registry keys
- No string literals to maintain
- IDE refactoring tools work seamlessly

### 4. Generic Method Support
```java
public static <T> void processService(TypeSafeRegistry registry, Class<T> type) {
    T service = registry.get(type); // Type T guaranteed
    // No casting needed, full type safety
}
```

## Key Interaction Points

1. **Class Objects as Keys**: Uses `Class<T>` instead of strings
2. **Generic Registration**: `register<T>(Class<T>, T)` ensures type consistency
3. **Safe Casting**: Internal `Class.cast()` guaranteed safe by registration contract
4. **Type Inference**: Return types automatically inferred from `Class<T>` parameter
5. **Compile-Time Validation**: Type mismatches caught during compilation
6. **IDE Integration**: Full auto-completion and refactoring support