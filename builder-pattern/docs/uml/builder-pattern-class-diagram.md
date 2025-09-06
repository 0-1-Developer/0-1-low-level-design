# Builder Pattern - Class Diagrams

## Classic GoF Builder Pattern

```mermaid
classDiagram
    class Director {
        -Builder builder
        +setBuilder(builder: Builder)
        +construct(): Product
        +constructMinimal(): Product
    }
    
    class Builder {
        #product: Product
        +createProduct()
        +getProduct(): Product
        +buildEngine()*
        +buildTransmission()*
        +buildInterior()*
        +buildColor()*
        +buildWheels()*
        +buildGPS()*
        +buildSunroof()*
        +buildAirConditioning()*
    }
    
    class LuxuryCarBuilder {
        +buildEngine()
        +buildTransmission()
        +buildInterior()
        +buildColor()
        +buildWheels()
        +buildGPS()
        +buildSunroof()
        +buildAirConditioning()
    }
    
    class EconomyCarBuilder {
        +buildEngine()
        +buildTransmission()
        +buildInterior()
        +buildColor()
        +buildWheels()
        +buildGPS()
        +buildSunroof()
        +buildAirConditioning()
    }
    
    class SportCarBuilder {
        +buildEngine()
        +buildTransmission()
        +buildInterior()
        +buildColor()
        +buildWheels()
        +buildGPS()
        +buildSunroof()
        +buildAirConditioning()
    }
    
    class Product {
        -engine: String
        -transmission: String
        -interior: String
        -color: String
        -wheels: String
        -hasGPS: boolean
        -hasSunroof: boolean
        -hasAirConditioning: boolean
        +setEngine(engine: String)
        +setTransmission(transmission: String)
        +setInterior(interior: String)
        +setColor(color: String)
        +setWheels(wheels: String)
        +setHasGPS(hasGPS: boolean)
        +setHasSunroof(hasSunroof: boolean)
        +setHasAirConditioning(hasAirConditioning: boolean)
        +toString(): String
    }
    
    Director --> Builder
    Builder <|-- LuxuryCarBuilder
    Builder <|-- EconomyCarBuilder
    Builder <|-- SportCarBuilder
    Builder --> Product
```

## Fluent Builder Pattern

```mermaid
classDiagram
    class Computer {
        -cpu: String
        -memory: String
        -storage: String
        -graphics: String
        -motherboard: String
        -hasWifi: boolean
        -hasBluetooth: boolean
        -hasWebcam: boolean
        -ports: int
        -operatingSystem: String
        +Computer(builder: ComputerBuilder)
        +getCpu(): String
        +getMemory(): String
        +getStorage(): String
        +toString(): String
    }
    
    class ComputerBuilder {
        -cpu: String
        -memory: String
        -storage: String
        -graphics: String
        -motherboard: String
        -hasWifi: boolean
        -hasBluetooth: boolean
        -hasWebcam: boolean
        -ports: int
        -operatingSystem: String
        +cpu(cpu: String): ComputerBuilder
        +memory(memory: String): ComputerBuilder
        +storage(storage: String): ComputerBuilder
        +graphics(graphics: String): ComputerBuilder
        +motherboard(motherboard: String): ComputerBuilder
        +withWifi(): ComputerBuilder
        +withBluetooth(): ComputerBuilder
        +withWebcam(): ComputerBuilder
        +ports(ports: int): ComputerBuilder
        +operatingSystem(os: String): ComputerBuilder
        +build(): Computer
    }
    
    Computer +-- ComputerBuilder
    ComputerBuilder --> Computer : creates
```

## Step Builder Pattern

```mermaid
classDiagram
    class House {
        -foundation: String
        -structure: String
        -roof: String
        -interior: String
        -exterior: String
        -hasGarage: boolean
        -hasGarden: boolean
        -hasPool: boolean
        +builder(): FoundationStep
    }
    
    class FoundationStep {
        <<interface>>
        +foundation(foundation: String): StructureStep
    }
    
    class StructureStep {
        <<interface>>
        +structure(structure: String): RoofStep
    }
    
    class RoofStep {
        <<interface>>
        +roof(roof: String): OptionalStep
    }
    
    class OptionalStep {
        <<interface>>
        +interior(interior: String): OptionalStep
        +exterior(exterior: String): OptionalStep
        +withGarage(): OptionalStep
        +withGarden(): OptionalStep
        +withPool(): OptionalStep
        +build(): House
    }
    
    class Builder {
        -foundation: String
        -structure: String
        -roof: String
        -interior: String
        -exterior: String
        -hasGarage: boolean
        -hasGarden: boolean
        -hasPool: boolean
        +foundation(foundation: String): StructureStep
        +structure(structure: String): RoofStep
        +roof(roof: String): OptionalStep
        +interior(interior: String): OptionalStep
        +exterior(exterior: String): OptionalStep
        +withGarage(): OptionalStep
        +withGarden(): OptionalStep
        +withPool(): OptionalStep
        +build(): House
    }
    
    House --> FoundationStep
    FoundationStep --> StructureStep
    StructureStep --> RoofStep
    RoofStep --> OptionalStep
    Builder ..|> FoundationStep
    Builder ..|> StructureStep
    Builder ..|> RoofStep
    Builder ..|> OptionalStep
    Builder --> House : creates
```

## Hierarchical Builder Pattern

```mermaid
classDiagram
    class Vehicle {
        <<abstract>>
        #make: String
        #model: String
        #year: int
        #color: String
        #engine: String
        #wheels: int
        +Vehicle(builder: VehicleBuilder)
        +getMake(): String
        +getModel(): String
        +toString(): String
    }
    
    class VehicleBuilder {
        <<abstract>>
        #make: String
        #model: String
        #year: int
        #color: String
        #engine: String
        #wheels: int
        +make(make: String): B
        +model(model: String): B
        +year(year: int): B
        +color(color: String): B
        +engine(engine: String): B
        +wheels(wheels: int): B
        #self(): B*
        +build(): T*
        #validateBase()
    }
    
    class Car {
        -doors: int
        -transmission: String
        -hasAirConditioning: boolean
        -hasSunroof: boolean
        +Car(builder: CarBuilder)
        +getDoors(): int
        +getTransmission(): String
        +builder(): CarBuilder
    }
    
    class CarBuilder {
        -doors: int
        -transmission: String
        -hasAirConditioning: boolean
        -hasSunroof: boolean
        +doors(doors: int): CarBuilder
        +transmission(transmission: String): CarBuilder
        +withAirConditioning(): CarBuilder
        +withoutAirConditioning(): CarBuilder
        +withSunroof(): CarBuilder
        #self(): CarBuilder
        +build(): Car
    }
    
    class Motorcycle {
        -bikeType: String
        -engineCC: int
        -hasWindscreen: boolean
        -hasSidecar: boolean
        +Motorcycle(builder: MotorcycleBuilder)
        +getBikeType(): String
        +getEngineCC(): int
        +builder(): MotorcycleBuilder
    }
    
    class MotorcycleBuilder {
        -bikeType: String
        -engineCC: int
        -hasWindscreen: boolean
        -hasSidecar: boolean
        +bikeType(bikeType: String): MotorcycleBuilder
        +engineCC(engineCC: int): MotorcycleBuilder
        +withWindscreen(): MotorcycleBuilder
        +withSidecar(): MotorcycleBuilder
        #self(): MotorcycleBuilder
        +build(): Motorcycle
    }
    
    Vehicle <|-- Car
    Vehicle <|-- Motorcycle
    VehicleBuilder <|-- CarBuilder
    VehicleBuilder <|-- MotorcycleBuilder
    Car --> CarBuilder : creates with
    Motorcycle --> MotorcycleBuilder : creates with
    VehicleBuilder --> Vehicle : creates
```

## Nested Builder Pattern

```mermaid
classDiagram
    class DatabaseConnection {
        -host: String
        -port: int
        -database: String
        -username: String
        -password: String
        -driver: String
        -connectionTimeout: int
        -readTimeout: int
        -maxPoolSize: int
        -autoCommit: boolean
        -isolationLevel: String
        -additionalProperties: Properties
        +DatabaseConnection(builder: Builder)
        +getHost(): String
        +getPort(): int
        +getConnectionString(): String
        +builder(): Builder
    }
    
    class Builder {
        -host: String
        -port: int
        -database: String
        -username: String
        -password: String
        -driver: String
        -connectionTimeout: int
        -readTimeout: int
        -maxPoolSize: int
        -autoCommit: boolean
        -isolationLevel: String
        -additionalProperties: Map
        +host(host: String): Builder
        +port(port: int): Builder
        +database(database: String): Builder
        +username(username: String): Builder
        +password(password: String): Builder
        +driver(driver: String): Builder
        +connectionTimeout(timeoutMs: int): Builder
        +readTimeout(timeoutMs: int): Builder
        +maxPoolSize(maxPoolSize: int): Builder
        +autoCommit(autoCommit: boolean): Builder
        +isolationLevel(isolationLevel: String): Builder
        +property(key: String, value: String): Builder
        +properties(properties: Map): Builder
        +build(): DatabaseConnection
    }
    
    DatabaseConnection +-- Builder
    Builder --> DatabaseConnection : creates
```