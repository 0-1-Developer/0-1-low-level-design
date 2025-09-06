# Builder Pattern - Sequence Diagrams

## Classic GoF Builder - Construction Process

```mermaid
sequenceDiagram
    participant Client
    participant Director
    participant Builder as ConcreteBuilder
    participant Product
    
    Client->>Director: new Director()
    Client->>Builder: new ConcreteBuilder()
    Client->>Director: setBuilder(builder)
    Client->>Director: construct()
    
    Director->>Builder: createProduct()
    Builder->>Product: new Product()
    
    Director->>Builder: buildEngine()
    Builder->>Product: setEngine()
    
    Director->>Builder: buildTransmission()
    Builder->>Product: setTransmission()
    
    Director->>Builder: buildInterior()
    Builder->>Product: setInterior()
    
    Director->>Builder: buildColor()
    Builder->>Product: setColor()
    
    Director->>Builder: buildWheels()
    Builder->>Product: setWheels()
    
    Director->>Builder: buildGPS()
    Builder->>Product: setHasGPS()
    
    Director->>Builder: buildSunroof()
    Builder->>Product: setHasSunroof()
    
    Director->>Builder: buildAirConditioning()
    Builder->>Product: setHasAirConditioning()
    
    Director->>Builder: getProduct()
    Builder-->>Director: product
    Director-->>Client: product
```

## Fluent Builder - Method Chaining

```mermaid
sequenceDiagram
    participant Client
    participant Builder as ComputerBuilder
    participant Computer
    
    Client->>Builder: new ComputerBuilder()
    Client->>Builder: cpu("Intel i9")
    Builder-->>Client: this
    Client->>Builder: memory("32GB DDR5")
    Builder-->>Client: this
    Client->>Builder: storage("2TB NVMe SSD")
    Builder-->>Client: this
    Client->>Builder: withWifi()
    Builder-->>Client: this
    Client->>Builder: withBluetooth()
    Builder-->>Client: this
    Client->>Builder: build()
    Builder->>Computer: new Computer(this)
    Computer-->>Builder: computer
    Builder-->>Client: computer
```

## Step Builder - Enforced Sequence

```mermaid
sequenceDiagram
    participant Client
    participant House
    participant FoundationStep
    participant StructureStep
    participant RoofStep
    participant OptionalStep
    participant Builder
    
    Client->>House: builder()
    House->>Builder: new Builder()
    House-->>Client: FoundationStep interface
    
    Client->>FoundationStep: foundation("Concrete")
    FoundationStep-->>Client: StructureStep interface
    
    Client->>StructureStep: structure("Steel Frame")
    StructureStep-->>Client: RoofStep interface
    
    Client->>RoofStep: roof("Metal Roofing")
    RoofStep-->>Client: OptionalStep interface
    
    Client->>OptionalStep: withGarage()
    OptionalStep-->>Client: OptionalStep interface
    
    Client->>OptionalStep: withGarden()
    OptionalStep-->>Client: OptionalStep interface
    
    Client->>OptionalStep: build()
    OptionalStep->>House: new House(...)
    House-->>OptionalStep: house
    OptionalStep-->>Client: house
```

## Hierarchical Builder - Inheritance Chain

```mermaid
sequenceDiagram
    participant Client
    participant Car
    participant CarBuilder
    participant VehicleBuilder
    participant Vehicle
    
    Client->>Car: builder()
    Car->>CarBuilder: new CarBuilder()
    Car-->>Client: carBuilder
    
    Client->>CarBuilder: make("Toyota")
    CarBuilder->>VehicleBuilder: make("Toyota")
    VehicleBuilder-->>CarBuilder: this (self-type)
    CarBuilder-->>Client: carBuilder
    
    Client->>CarBuilder: model("Camry")
    CarBuilder->>VehicleBuilder: model("Camry")
    VehicleBuilder-->>CarBuilder: this (self-type)
    CarBuilder-->>Client: carBuilder
    
    Client->>CarBuilder: doors(4)
    CarBuilder-->>Client: carBuilder
    
    Client->>CarBuilder: withSunroof()
    CarBuilder-->>Client: carBuilder
    
    Client->>CarBuilder: build()
    CarBuilder->>VehicleBuilder: validateBase()
    CarBuilder->>Car: new Car(this)
    Car->>Vehicle: super(this)
    Vehicle-->>Car: 
    Car-->>CarBuilder: car
    CarBuilder-->>Client: car
```

## Functional Builder - Lambda Configuration

```mermaid
sequenceDiagram
    participant Client
    participant Configuration
    participant Builder as ConfigBuilder
    participant Lambda as Consumer~Builder~
    
    Client->>Configuration: build(lambda)
    Configuration->>Builder: new Builder()
    Configuration->>Lambda: accept(builder)
    
    Lambda->>Builder: host("api.example.com")
    Builder-->>Lambda: this
    Lambda->>Builder: port(443)
    Builder-->>Lambda: this
    Lambda->>Builder: ssl(true)
    Builder-->>Lambda: this
    Lambda->>Builder: timeout(60000)
    Builder-->>Lambda: this
    
    Configuration->>Builder: build()
    Builder->>Configuration: new Configuration(...)
    Configuration-->>Builder: configuration
    Builder-->>Configuration: configuration
    Configuration-->>Client: configuration
```

## Prototype + Builder - Template-based Construction

```mermaid
sequenceDiagram
    participant Client
    participant Prototype as ServerProfile.Prototypes
    participant Template as WEB_SERVER
    participant Builder as ServerProfileBuilder
    participant NewProfile as ServerProfile
    
    Client->>Prototype: WEB_SERVER
    Prototype-->>Client: webServerTemplate
    
    Client->>Template: toBuilder()
    Template->>Builder: new ServerProfileBuilder(this)
    Template-->>Client: builder (pre-configured)
    
    Client->>Builder: name("web-01.prod")
    Builder-->>Client: this
    Client->>Builder: cpuCores(6)
    Builder-->>Client: this
    Client->>Builder: memoryGB(16)
    Builder-->>Client: this
    Client->>Builder: withBackup()
    Builder-->>Client: this
    
    Client->>Builder: build()
    Builder->>NewProfile: new ServerProfile(...)
    NewProfile-->>Builder: newProfile
    Builder-->>Client: newProfile
```