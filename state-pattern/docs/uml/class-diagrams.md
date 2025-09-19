# State Pattern - Class Diagrams

## Classic GoF State Pattern

```mermaid
classDiagram
    class VendingMachine {
        -State currentState
        -int balance
        -int productPrice
        -int productCount
        +setState(State)
        +handle(String)
        +addMoney(int)
        +refundMoney()
        +hasEnoughBalance() bool
        +hasProduct() bool
        +dispenseProduct()
    }
    
    class State {
        <<interface>>
        +handle(VendingMachine, String)
        +getName() String
    }
    
    class IdleState {
        +handle(VendingMachine, String)
        +getName() String
    }
    
    class AcceptingMoneyState {
        +handle(VendingMachine, String)
        +getName() String
    }
    
    class SelectingProductState {
        +handle(VendingMachine, String)
        +getName() String
    }
    
    class DispensingState {
        +handle(VendingMachine, String)
        +getName() String
    }
    
    class OutOfStockState {
        +handle(VendingMachine, String)
        +getName() String
    }
    
    VendingMachine --> State
    State <|.. IdleState
    State <|.. AcceptingMoneyState
    State <|.. SelectingProductState
    State <|.. DispensingState
    State <|.. OutOfStockState
```

## Table-Driven State Machine

```mermaid
classDiagram
    class StateMachine {
        -String currentState
        -Map~String,StateTransition~ transitionTable
        -Map~String,Object~ context
        +addTransition(StateTransition)
        +handleEvent(String) bool
        +getCurrentState() String
        +setContextValue(String, Object)
        +getContextValue(String) Object
    }
    
    class StateTransition {
        -String fromState
        -String event
        -String toState
        -Consumer~StateMachine~ action
        +getFromState() String
        +getEvent() String
        +getToState() String
        +getAction() Consumer
        +getKey() String
    }
    
    class TrafficLightMachine {
        -int timer
        +resetTimer(int)
        +tick()
        +getTimer() int
    }
    
    StateMachine <|-- TrafficLightMachine
    StateMachine --> StateTransition
```

## Enum-Based State Pattern

```mermaid
classDiagram
    class Connection {
        -ConnectionState state
        -String message
        -int packetsSent
        -int packetsReceived
        -int reconnectAttempts
        +connect()
        +disconnect()
        +sendData(String)
        +receiveData()
        +timeout()
        +getState() ConnectionState
    }
    
    class ConnectionState {
        <<enumeration>>
        DISCONNECTED
        CONNECTING
        CONNECTED
        RECONNECTING
        ERROR
        +connect(Connection) ConnectionState
        +disconnect(Connection) ConnectionState
        +sendData(Connection, String) ConnectionState
        +receiveData(Connection) ConnectionState
        +timeout(Connection) ConnectionState
    }
    
    Connection --> ConnectionState
```

## Dynamic Registry State Pattern

```mermaid
classDiagram
    class StateRegistry {
        -Map~String,DynamicState~ states
        -Map~String,String~ aliases
        +registerState(String, DynamicState)
        +registerAlias(String, String)
        +getState(String) DynamicState
        +unregisterState(String)
        +hasState(String) bool
    }
    
    class DynamicState {
        <<interface>>
        +enter(DynamicContext)
        +execute(DynamicContext, String)
        +exit(DynamicContext)
        +getName() String
    }
    
    class DynamicContext {
        -StateRegistry registry
        -DynamicState currentState
        -Map~String,Object~ data
        +transitionTo(String)
        +handleEvent(String)
        +setData(String, Object)
        +getData(String) Object
    }
    
    DynamicContext --> StateRegistry
    DynamicContext --> DynamicState
    StateRegistry --> DynamicState
```

## Functional State Pattern

```mermaid
classDiagram
    class FunctionalStateMachine~S,E~ {
        -S currentState
        -Map~S,Consumer~S~~ entryActions
        -Map~S,Consumer~S~~ exitActions
        -Map~String,BiFunction~S,E,S~~ transitions
        +onEntry(S, Consumer~S~) FunctionalStateMachine
        +onExit(S, Consumer~S~) FunctionalStateMachine
        +addTransition(S, E, S) FunctionalStateMachine
        +handleEvent(E) bool
        +getCurrentState() S
    }
    
    class Builder~S,E~ {
        -FunctionalStateMachine~S,E~ machine
        +onEntry(S, Consumer~S~) Builder
        +onExit(S, Consumer~S~) Builder
        +transition(S, E, S) Builder
        +build() FunctionalStateMachine
    }
    
    FunctionalStateMachine --> Builder
```

## Hierarchical State Pattern

```mermaid
classDiagram
    class HierarchicalState {
        #String name
        #HierarchicalState parent
        #HierarchicalState activeChild
        #Map~String,HierarchicalState~ children
        +enter()
        +exit()
        +handleEvent(String) bool
        +transitionToChild(String)
        +transitionToSibling(String)
        +getName() String
        +getFullPath() String
        +addChild(HierarchicalState)
    }
    
    class HierarchicalContext {
        -HierarchicalState rootState
        -Stack~HierarchicalState~ activeStateStack
        +setRootState(HierarchicalState)
        +start()
        +handleEvent(String)
        +getCurrentPath() String
        +isStateActive(HierarchicalState) bool
    }
    
    class PhoneRootState {
        +getDefaultChild() HierarchicalState
    }
    
    class OnState {
        +handleLocalEvent(String) bool
    }
    
    class LockedState {
        +handleLocalEvent(String) bool
    }
    
    HierarchicalContext --> HierarchicalState
    HierarchicalState <|-- PhoneRootState
    HierarchicalState <|-- OnState
    HierarchicalState <|-- LockedState
    HierarchicalState --> HierarchicalState : children
```

## Reactive Event-Driven State Pattern

```mermaid
classDiagram
    class EventBus {
        -Map~String,List~Consumer~Event~~~ subscribers
        -BlockingQueue~Event~ eventQueue
        +subscribe(String, Consumer~Event~)
        +unsubscribe(String, Consumer~Event~)
        +publish(Event)
        +publishSync(Event)
    }
    
    class Event {
        -String type
        -String source
        -Instant timestamp
        -Map~String,Object~ payload
        +getType() String
        +getSource() String
        +get(String) Object
    }
    
    class ReactiveState {
        #String name
        #EventBus eventBus
        #ReactiveContext context
        +enter()
        +exit()
        +on(String, Consumer~Event~)
        +transitionTo(String)
        +publishEvent(String, String, Object)
    }
    
    class ReactiveContext {
        -Map~String,ReactiveState~ states
        -EventBus eventBus
        -ReactiveState currentState
        +addState(ReactiveState)
        +transitionTo(String)
        +handleEvent(Event)
    }
    
    ReactiveContext --> EventBus
    ReactiveContext --> ReactiveState
    ReactiveState --> EventBus
    EventBus --> Event
```

## Persistent State Pattern

```mermaid
classDiagram
    class StateStore {
        -Path storageDir
        -Map~String,StateSnapshot~ inMemoryCache
        +saveSnapshot(StateSnapshot)
        +loadSnapshot(String, int) StateSnapshot
        +loadLatest(String) StateSnapshot
        +getHistory(String) List~StateSnapshot~
        +cleanup(String, int)
    }
    
    class StateSnapshot {
        -String workflowId
        -String stateName
        -int version
        -Instant timestamp
        -Map~String,Object~ context
        -String checksum
        +validate() bool
        +getWorkflowId() String
        +getStateName() String
        +getVersion() int
    }
    
    class PersistentWorkflowEngine {
        -StateStore stateStore
        -Map~String,PersistentWorkflow~ activeWorkflows
        +createWorkflow(String, String) PersistentWorkflow
        +recoverWorkflow(String) PersistentWorkflow
        +saveCheckpoint(String)
        +rollback(String, int)
        +pauseWorkflow(String)
    }
    
    class PersistentWorkflow {
        -String workflowId
        -String currentState
        -Map~String,Object~ context
        +transitionTo(String)
        +setContextValue(String, Object)
        +restoreFromSnapshot(StateSnapshot)
    }
    
    PersistentWorkflowEngine --> StateStore
    PersistentWorkflowEngine --> PersistentWorkflow
    StateStore --> StateSnapshot
    PersistentWorkflow --> StateSnapshot
```