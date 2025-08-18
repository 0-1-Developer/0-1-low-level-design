# Basic Registry Pattern - Sequence Diagram

## Service Registration and Usage Flow

```mermaid
sequenceDiagram
    participant Demo as BasicRegistryDemo
    participant Registry as BasicServiceRegistry
    participant EmailSvc as EmailService
    participant SMSSvc as SMSService
    participant Map as serviceMap
    
    Note over Demo: Application Startup
    
    %% Get Registry Instance
    Demo->>Registry: getInstance()
    Registry-->>Demo: registry instance
    
    %% Create Services (Eager Initialization)
    Demo->>EmailSvc: new EmailService()
    EmailSvc-->>Demo: emailService
    
    Demo->>SMSSvc: new SMSService()
    SMSSvc-->>Demo: smsService
    
    %% Register Services
    Demo->>Registry: registerService("EMAIL", emailService)
    Registry->>Map: put("EMAIL", emailService)
    Map-->>Registry: stored
    Registry-->>Demo: void
    
    Demo->>Registry: registerService("SMS", smsService)
    Registry->>Map: put("SMS", smsService)
    Map-->>Registry: stored
    Registry-->>Demo: void
    
    Note over Demo: Service Usage Phase
    
    %% Retrieve and Use Email Service
    Demo->>Registry: getService("EMAIL")
    Registry->>Map: get("EMAIL")
    Map-->>Registry: emailService
    Registry-->>Demo: emailService
    
    Demo->>EmailSvc: execute()
    EmailSvc-->>Demo: "Executing Email Service..."
    
    %% Retrieve and Use SMS Service
    Demo->>Registry: getService("SMS")
    Registry->>Map: get("SMS")
    Map-->>Registry: smsService
    Registry-->>Demo: smsService
    
    Demo->>SMSSvc: execute()
    SMSSvc-->>Demo: "Executing SMS Service..."
    
    Note over Demo: Error Scenario
    
    %% Typo in Key
    Demo->>Registry: getService("EMIAL")
    Registry->>Map: get("EMIAL")
    Map-->>Registry: null
    Registry-->>Demo: null
    
    Note over Demo: Registry Management
    
    %% Check Service Existence
    Demo->>Registry: hasService("EMAIL")
    Registry->>Map: containsKey("EMAIL")
    Map-->>Registry: true
    Registry-->>Demo: true
    
    %% Unregister Service
    Demo->>Registry: unregisterService("EMAIL")
    Registry->>Map: remove("EMAIL")
    Map-->>Registry: emailService
    Registry-->>Demo: emailService
    
    %% Clear Registry
    Demo->>Registry: clear()
    Registry->>Map: clear()
    Map-->>Registry: cleared
    Registry-->>Demo: void
```

## Key Interaction Points

1. **Singleton Access**: Registry instance obtained via `getInstance()`
2. **Eager Creation**: Services created before registration
3. **String-Based Lookup**: Services retrieved using string keys
4. **Shared Instances**: Same service instance returned on multiple calls
5. **Error Handling**: Null returned for non-existent keys
6. **Management Operations**: Support for checking, removing, and clearing services

## Potential Issues Demonstrated

- **Typo Errors**: `getService("EMIAL")` returns null instead of email service
- **Runtime Casting**: Manual casting required for service-specific methods
- **No Compile-Time Safety**: Errors only discovered at runtime