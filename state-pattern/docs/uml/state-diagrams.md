# State Pattern - State Diagrams

## Vending Machine States (Classic Pattern)

```mermaid
stateDiagram-v2
    [*] --> Idle
    
    Idle --> AcceptingMoney : INSERT_COIN
    Idle --> OutOfService : MAINTENANCE
    
    AcceptingMoney --> AcceptingMoney : INSERT_COIN
    AcceptingMoney --> SelectingProduct : sufficient_balance
    AcceptingMoney --> Idle : CANCEL / refund_money
    
    SelectingProduct --> Dispensing : SELECT_PRODUCT / has_products
    SelectingProduct --> OutOfStock : SELECT_PRODUCT / no_products
    SelectingProduct --> Idle : CANCEL / refund_money
    SelectingProduct --> AcceptingMoney : INSERT_COIN
    
    Dispensing --> Idle : DISPENSE / has_products
    Dispensing --> OutOfStock : DISPENSE / no_products
    
    OutOfStock --> OutOfService : MAINTENANCE
    OutOfStock --> OutOfStock : INSERT_COIN / reject
    OutOfStock --> OutOfStock : SELECT_PRODUCT / show_error
    
    OutOfService --> Idle : MAINTENANCE / repair_complete
    
    note right of AcceptingMoney
        Accumulates money until
        sufficient for purchase
    end note
    
    note right of Dispensing
        Dispenses product and
        returns change if any
    end note
```

## Traffic Light State Machine (Table-Driven)

```mermaid
stateDiagram-v2
    [*] --> Red
    
    Red --> Green : TIMER_EXPIRED / cars_go
    Red --> FlashingRed : EMERGENCY
    Red --> Red : PEDESTRIAN_REQUEST / queue_request
    
    Green --> Yellow : TIMER_EXPIRED / prepare_stop
    Green --> FlashingRed : EMERGENCY
    Green --> Green : PEDESTRIAN_REQUEST / queue_request
    
    Yellow --> Red : TIMER_EXPIRED / stop
    Yellow --> FlashingRed : EMERGENCY
    
    FlashingRed --> Red : RESET / normal_operation
    
    state Red {
        [*] --> WaitingCars
        WaitingCars --> WaitingCars : timer_tick
    }
    
    state Green {
        [*] --> AllowingTraffic
        AllowingTraffic --> PedestrianCrossing : pedestrian_signal
        PedestrianCrossing --> AllowingTraffic : pedestrian_complete
    }
```

## Connection States (Enum-Based)

```mermaid
stateDiagram-v2
    [*] --> DISCONNECTED
    
    DISCONNECTED --> CONNECTING : connect()
    
    CONNECTING --> CONNECTED : connection_success
    CONNECTING --> DISCONNECTED : connection_failed / max_retries
    CONNECTING --> DISCONNECTED : disconnect() / cancel
    CONNECTING --> DISCONNECTED : timeout
    
    CONNECTED --> DISCONNECTED : disconnect()
    CONNECTED --> RECONNECTING : timeout / connection_lost
    CONNECTED --> CONNECTED : send_data
    CONNECTED --> CONNECTED : receive_data
    
    RECONNECTING --> CONNECTING : retry_connection
    RECONNECTING --> ERROR : max_reconnects_exceeded
    RECONNECTING --> DISCONNECTED : disconnect() / cancel
    
    ERROR --> DISCONNECTED : reset() / clear_error
    ERROR --> ERROR : any_operation / show_error
    
    note right of CONNECTED
        Normal operation state
        - Send/receive data
        - Monitor connection health
    end note
    
    note right of RECONNECTING
        Automatic recovery
        - Limited retry attempts
        - Exponential backoff
    end note
```

## Phone State Hierarchy

```mermaid
stateDiagram-v2
    state PHONE {
        [*] --> OFF
        
        OFF --> ON : POWER_ON
        ON --> OFF : POWER_OFF
        
        state ON {
            [*] --> LOCKED
            LOCKED --> UNLOCKED : UNLOCK
            UNLOCKED --> LOCKED : LOCK / timeout
            
            state LOCKED {
                [*] --> DISPLAY_OFF
                DISPLAY_OFF --> DISPLAY_ON : WAKE
                DISPLAY_ON --> DISPLAY_OFF : SLEEP / timeout
                
                DISPLAY_ON --> DISPLAY_ON : EMERGENCY_CALL
            }
            
            state UNLOCKED {
                [*] --> IDLE
                IDLE --> APP_RUNNING : OPEN_APP
                IDLE --> CALL_ACTIVE : INCOMING_CALL
                
                APP_RUNNING --> IDLE : CLOSE_APP / HOME
                APP_RUNNING --> CALL_ACTIVE : INCOMING_CALL
                
                state APP_RUNNING {
                    [*] --> FOREGROUND
                    FOREGROUND --> BACKGROUND : MINIMIZE
                    BACKGROUND --> FOREGROUND : RESTORE
                }
                
                state CALL_ACTIVE {
                    [*] --> RINGING
                    RINGING --> TALKING : ANSWER
                    RINGING --> IDLE : REJECT
                    TALKING --> ON_HOLD : HOLD
                    ON_HOLD --> TALKING : RESUME
                    TALKING --> IDLE : END_CALL
                    ON_HOLD --> IDLE : END_CALL
                }
            }
        }
    }
```

## IoT Device States (Reactive)

```mermaid
stateDiagram-v2
    [*] --> DISCONNECTED
    
    DISCONNECTED --> CONNECTING : WIFI_AVAILABLE / MANUAL_CONNECT
    
    CONNECTING --> CONNECTED : CONNECTION_SUCCESS
    CONNECTING --> DISCONNECTED : CONNECTION_FAILED / max_retries
    CONNECTING --> CONNECTING : CONNECTION_FAILED / retry
    
    CONNECTED --> TRANSMITTING : SEND_DATA
    CONNECTED --> SLEEP : LOW_POWER
    CONNECTED --> UPDATING : FIRMWARE_UPDATE
    CONNECTED --> DISCONNECTED : CONNECTION_LOST
    
    TRANSMITTING --> CONNECTED : TRANSMISSION_COMPLETE
    TRANSMITTING --> CONNECTED : TRANSMISSION_ERROR
    
    SLEEP --> CONNECTED : WAKE_UP / SCHEDULED_WAKE / SENSOR_TRIGGER
    
    UPDATING --> DISCONNECTED : UPDATE_COMPLETE / reboot
    UPDATING --> CONNECTED : UPDATE_FAILED
    UPDATING --> UPDATING : UPDATE_PROGRESS
    
    note right of CONNECTED
        Event-driven state
        - Responds to sensor events
        - Handles remote commands
        - Monitors connectivity
    end note
    
    note right of SLEEP
        Power conservation
        - Reduced functionality
        - Scheduled wake-ups
        - Emergency wake triggers
    end note
```

## Order Workflow States (Persistent)

```mermaid
stateDiagram-v2
    [*] --> CREATED
    
    CREATED --> PAYMENT_PENDING : submit_order
    CREATED --> CANCELLED : customer_cancel
    
    PAYMENT_PENDING --> PAID : payment_received
    PAYMENT_PENDING --> CANCELLED : payment_failed / timeout
    
    PAID --> PROCESSING : payment_confirmed
    PAID --> REFUNDING : customer_cancel
    
    PROCESSING --> SHIPPED : items_picked_and_packed
    PROCESSING --> CANCELLED : out_of_stock
    PROCESSING --> REFUNDING : customer_cancel
    
    SHIPPED --> DELIVERED : delivery_confirmed
    SHIPPED --> PROCESSING : shipping_failed
    
    DELIVERED --> COMPLETED : customer_satisfied
    DELIVERED --> RETURNING : return_requested
    
    RETURNING --> REFUNDED : return_processed
    
    CANCELLED --> REFUNDED : refund_processed
    REFUNDING --> REFUNDED : refund_processed
    
    COMPLETED --> [*]
    REFUNDED --> [*]
    
    note right of PROCESSING
        Persistent checkpoint
        - Inventory reserved
        - Warehouse notified
        - Audit trail created
    end note
    
    note right of SHIPPED
        External tracking
        - Carrier integration
        - Customer notifications
        - Delivery monitoring
    end note
```

## Game State Machine (Dynamic)

```mermaid
stateDiagram-v2
    [*] --> MENU
    
    MENU --> PLAYING : START_GAME
    MENU --> OPTIONS : CONFIGURE
    MENU --> EXIT : QUIT
    
    OPTIONS --> MENU : SAVE / CANCEL
    
    PLAYING --> PAUSED : PAUSE
    PLAYING --> GAME_OVER : PLAYER_DIED / TIME_UP
    PLAYING --> VICTORY : LEVEL_COMPLETE
    PLAYING --> MENU : QUIT
    
    PAUSED --> PLAYING : RESUME
    PAUSED --> MENU : QUIT
    PAUSED --> EXIT : QUIT
    
    GAME_OVER --> PLAYING : RETRY
    GAME_OVER --> MENU : MAIN_MENU
    
    VICTORY --> PLAYING : NEXT_LEVEL
    VICTORY --> MENU : MAIN_MENU
    
    EXIT --> [*]
    
    state PLAYING {
        state "Dynamic States" as DynamicStates
        note right of DynamicStates
            States can be registered
            at runtime via plugins:
            - MOD_BONUS_LEVEL
            - MOD_TUTORIAL
            - MOD_CHALLENGE
        end note
    }
```

## ATM State Machine (Switch-Based)

```mermaid
stateDiagram-v2
    [*] --> NO_CARD
    
    NO_CARD --> CARD_INSERTED : insert_card
    
    CARD_INSERTED --> PIN_ENTERED : enter_correct_pin
    CARD_INSERTED --> ERROR : enter_wrong_pin / 3_attempts
    CARD_INSERTED --> NO_CARD : eject_card
    
    PIN_ENTERED --> TRANSACTION : select_transaction
    PIN_ENTERED --> NO_CARD : eject_card
    
    TRANSACTION --> TRANSACTION : balance_inquiry / deposit / withdrawal
    TRANSACTION --> NO_CARD : eject_card / session_complete
    TRANSACTION --> ERROR : insufficient_funds / system_error
    
    ERROR --> NO_CARD : eject_card / timeout
    
    note right of CARD_INSERTED
        PIN validation
        - Max 3 attempts
        - Card blocking on failure
        - Security logging
    end note
    
    note right of TRANSACTION
        Banking operations
        - Balance checks
        - Withdrawal limits
        - Transaction logging
    end note
```

## Functional State Machine Example

```mermaid
stateDiagram-v2
    [*] --> OFF
    
    OFF --> ON : POWER / boot_sequence
    ON --> OFF : POWER / shutdown_sequence
    
    ON --> STANDBY : SLEEP / save_state
    STANDBY --> ON : WAKE / restore_state
    STANDBY --> OFF : POWER / force_shutdown
    
    note right of ON
        Functional composition:
        - Entry actions as lambdas
        - Transition guards as predicates
        - State behavior as functions
    end note
    
    note right of STANDBY
        Lambda-based logic:
        - onEnter: () -> saveState()
        - onExit: () -> restoreState()
        - condition: (event) -> checkWakeCondition(event)
    end note
```