# State Pattern - Sequence Diagrams

## Classic State Transition

```mermaid
sequenceDiagram
    participant Client
    participant VendingMachine
    participant IdleState
    participant AcceptingMoneyState
    
    Client->>VendingMachine: handle("INSERT_COIN")
    VendingMachine->>IdleState: handle(machine, "INSERT_COIN")
    IdleState->>VendingMachine: setState(AcceptingMoneyState)
    VendingMachine->>AcceptingMoneyState: create new instance
    VendingMachine->>VendingMachine: currentState = newState
    IdleState-->>VendingMachine: transition complete
    VendingMachine-->>Client: event handled
```

## Table-Driven State Processing

```mermaid
sequenceDiagram
    participant Client
    participant StateMachine
    participant TransitionTable
    participant StateTransition
    
    Client->>StateMachine: handleEvent("TIMER_EXPIRED")
    StateMachine->>StateMachine: makeKey(currentState, event)
    StateMachine->>TransitionTable: get(key)
    TransitionTable-->>StateMachine: StateTransition
    StateMachine->>StateTransition: getToState()
    StateMachine->>StateTransition: getAction()
    StateTransition-->>StateMachine: action.accept(this)
    StateMachine->>StateMachine: currentState = newState
    StateMachine-->>Client: transition complete
```

## Enum State Transition

```mermaid
sequenceDiagram
    participant Client
    participant Connection
    participant ConnectionState
    
    Client->>Connection: connect()
    Connection->>ConnectionState: DISCONNECTED.connect(this)
    ConnectionState->>ConnectionState: state-specific logic
    ConnectionState-->>Connection: return CONNECTING
    Connection->>Connection: state = newState
    Connection-->>Client: state changed
```

## Dynamic State Registration and Transition

```mermaid
sequenceDiagram
    participant Client
    participant DynamicContext
    participant StateRegistry
    participant GameState
    
    Client->>StateRegistry: registerState("PLAYING", gameState)
    StateRegistry->>StateRegistry: states.put("PLAYING", gameState)
    
    Client->>DynamicContext: transitionTo("PLAYING")
    DynamicContext->>StateRegistry: getState("PLAYING")
    StateRegistry-->>DynamicContext: GameState instance
    DynamicContext->>GameState: exit() [if current state exists]
    DynamicContext->>GameState: enter()
    DynamicContext->>DynamicContext: currentState = newState
```

## Functional State Machine Execution

```mermaid
sequenceDiagram
    participant Client
    participant FunctionalStateMachine
    participant EntryAction
    participant TransitionFunction
    participant ExitAction
    
    Client->>FunctionalStateMachine: handleEvent(event)
    FunctionalStateMachine->>FunctionalStateMachine: makeKey(state, event)
    FunctionalStateMachine->>TransitionFunction: apply(currentState, event)
    TransitionFunction-->>FunctionalStateMachine: newState
    
    alt state change
        FunctionalStateMachine->>ExitAction: accept(currentState)
        FunctionalStateMachine->>EntryAction: accept(newState)
        FunctionalStateMachine->>FunctionalStateMachine: currentState = newState
    end
    
    FunctionalStateMachine-->>Client: transition result
```

## Hierarchical State Event Handling

```mermaid
sequenceDiagram
    participant Client
    participant HierarchicalContext
    participant ParentState
    participant ChildState
    
    Client->>HierarchicalContext: handleEvent("UNLOCK")
    HierarchicalContext->>ParentState: handleEvent("UNLOCK")
    ParentState->>ChildState: handleEvent("UNLOCK")
    
    alt child handles event
        ChildState->>ChildState: handleLocalEvent("UNLOCK")
        ChildState-->>ParentState: true (handled)
        ParentState-->>HierarchicalContext: true
    else child doesn't handle
        ChildState-->>ParentState: false (not handled)
        ParentState->>ParentState: handleLocalEvent("UNLOCK")
        ParentState->>ParentState: transitionToChild("UNLOCKED")
        ParentState-->>HierarchicalContext: true (handled)
    end
    
    HierarchicalContext-->>Client: event processed
```

## Reactive Event-Driven State Flow

```mermaid
sequenceDiagram
    participant EventSource
    participant EventBus
    participant ReactiveState
    participant ReactiveContext
    
    EventSource->>EventBus: publish(Event("WIFI_AVAILABLE"))
    EventBus->>EventBus: queue event
    
    loop event processing
        EventBus->>ReactiveState: eventHandler.accept(event)
        ReactiveState->>ReactiveState: process event
        ReactiveState->>ReactiveContext: transitionTo("CONNECTING")
        ReactiveContext->>ReactiveState: exit() [current state]
        ReactiveContext->>ReactiveState: enter() [new state]
        ReactiveState->>EventBus: publish(StateChangeEvent)
    end
```

## Persistent State Recovery

```mermaid
sequenceDiagram
    participant Client
    participant WorkflowEngine
    participant StateStore
    participant PersistentWorkflow
    
    Client->>WorkflowEngine: recoverWorkflow("ORDER-001")
    WorkflowEngine->>StateStore: loadLatest("ORDER-001")
    StateStore->>StateStore: search storage for latest version
    StateStore-->>WorkflowEngine: StateSnapshot
    
    WorkflowEngine->>StateSnapshot: validate()
    StateSnapshot-->>WorkflowEngine: true
    
    WorkflowEngine->>PersistentWorkflow: create("ORDER-001", snapshot.state)
    WorkflowEngine->>PersistentWorkflow: restoreFromSnapshot(snapshot)
    PersistentWorkflow->>PersistentWorkflow: restore context and state
    WorkflowEngine-->>Client: recovered workflow
```

## State Machine Error Handling

```mermaid
sequenceDiagram
    participant Client
    participant StateMachine
    participant State
    participant ErrorHandler
    
    Client->>StateMachine: handleEvent("INVALID_EVENT")
    StateMachine->>State: handleEvent("INVALID_EVENT")
    
    alt invalid event for current state
        State-->>StateMachine: false (not handled)
        StateMachine->>ErrorHandler: handleInvalidTransition(state, event)
        ErrorHandler->>ErrorHandler: log error
        ErrorHandler-->>StateMachine: error handled
        StateMachine-->>Client: error response
    else valid event
        State->>StateMachine: transition to new state
        StateMachine-->>Client: success response
    end
```

## Concurrent State Access

```mermaid
sequenceDiagram
    participant Thread1
    participant Thread2
    participant StateMachine
    participant SynchronizedState
    
    par concurrent access
        Thread1->>StateMachine: handleEvent("EVENT_A")
        and
        Thread2->>StateMachine: handleEvent("EVENT_B")
    end
    
    StateMachine->>SynchronizedState: synchronized access
    
    alt Thread1 gets lock first
        SynchronizedState->>SynchronizedState: process EVENT_A
        SynchronizedState-->>StateMachine: state change
        StateMachine->>SynchronizedState: process EVENT_B
        SynchronizedState-->>StateMachine: state change
    end
    
    par return results
        StateMachine-->>Thread1: result A
        and
        StateMachine-->>Thread2: result B
    end
```