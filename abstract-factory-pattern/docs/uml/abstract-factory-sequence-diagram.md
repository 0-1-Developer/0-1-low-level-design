# Abstract Factory Pattern - Sequence Diagrams

## Classic Abstract Factory Sequence

```mermaid
sequenceDiagram
    participant Client
    participant Factory as WindowsFactory
    participant Button as WindowsButton
    participant Checkbox as WindowsCheckbox
    participant ScrollBar as WindowsScrollBar
    
    Note over Client,ScrollBar: Creating Windows UI Family
    
    Client->>Factory: new WindowsFactory()
    activate Factory
    Factory-->>Client: factory instance
    deactivate Factory
    
    Note over Client,ScrollBar: Creating UI Components
    
    Client->>Factory: createButton()
    activate Factory
    Factory->>Button: new WindowsButton()
    activate Button
    Button-->>Factory: button instance
    deactivate Button
    Factory-->>Client: WindowsButton
    deactivate Factory
    
    Client->>Factory: createCheckbox()
    activate Factory
    Factory->>Checkbox: new WindowsCheckbox()
    activate Checkbox
    Checkbox-->>Factory: checkbox instance
    deactivate Checkbox
    Factory-->>Client: WindowsCheckbox
    deactivate Factory
    
    Client->>Factory: createScrollBar()
    activate Factory
    Factory->>ScrollBar: new WindowsScrollBar()
    activate ScrollBar
    ScrollBar-->>Factory: scrollbar instance
    deactivate ScrollBar
    Factory-->>Client: WindowsScrollBar
    deactivate Factory
    
    Note over Client,ScrollBar: Using Components
    
    Client->>Button: render()
    activate Button
    Button-->>Client: "Rendering Windows button..."
    deactivate Button
    
    Client->>Checkbox: render()
    activate Checkbox
    Checkbox-->>Client: "Rendering Windows checkbox..."
    deactivate Checkbox
    
    Client->>ScrollBar: render()
    activate ScrollBar
    ScrollBar-->>Client: "Rendering Windows scroll bar..."
    deactivate ScrollBar
    
    Note over Client,ScrollBar: All components are from Windows family
```

## Factory Method-backed Sequence

```mermaid
sequenceDiagram
    participant Client
    participant AbstractFactory as DarkThemeFactory
    participant Button as DarkButton
    participant Checkbox as DarkCheckbox
    
    Note over Client,Checkbox: Factory Method Pattern Integration
    
    Client->>AbstractFactory: new DarkThemeFactory()
    activate AbstractFactory
    AbstractFactory-->>Client: factory instance
    deactivate AbstractFactory
    
    Client->>AbstractFactory: createAndConfigureUI()
    activate AbstractFactory
    
    Note over AbstractFactory: Template Method
    AbstractFactory->>AbstractFactory: createButton()
    activate AbstractFactory
    AbstractFactory->>Button: new DarkButton()
    activate Button
    Button-->>AbstractFactory: button instance
    deactivate Button
    deactivate AbstractFactory
    
    AbstractFactory->>AbstractFactory: createCheckbox()
    activate AbstractFactory
    AbstractFactory->>Checkbox: new DarkCheckbox()
    activate Checkbox
    Checkbox-->>AbstractFactory: checkbox instance
    deactivate Checkbox
    deactivate AbstractFactory
    
    AbstractFactory->>AbstractFactory: getThemeName()
    activate AbstractFactory
    AbstractFactory-->>AbstractFactory: "Dark Theme"
    deactivate AbstractFactory
    
    AbstractFactory->>Button: render()
    activate Button
    Button-->>AbstractFactory: "Rendering dark button..."
    deactivate Button
    
    AbstractFactory->>Checkbox: render()
    activate Checkbox
    Checkbox-->>AbstractFactory: "Rendering dark checkbox..."
    deactivate Checkbox
    
    AbstractFactory-->>Client: UI created and configured
    deactivate AbstractFactory
```

## Registry-backed Factory Sequence

```mermaid
sequenceDiagram
    participant Client
    participant Registry as UIComponentRegistry
    participant Factory as RegistryBackedFactory
    participant Bundle as FactoryBundle
    participant Button as MinimalButton
    
    Note over Client,Button: Runtime Factory Selection
    
    Client->>Registry: getInstance()
    activate Registry
    Registry-->>Client: registry instance
    deactivate Registry
    
    Note over Client,Button: Registration Phase (typically at startup)
    
    Client->>Registry: registerFactory("minimal", bundle)
    activate Registry
    Registry->>Registry: store in factories map
    Registry-->>Client: registration complete
    deactivate Registry
    
    Note over Client,Button: Factory Creation
    
    Client->>Factory: new RegistryBackedFactory("minimal")
    activate Factory
    
    Factory->>Registry: getFactory("minimal")
    activate Registry
    Registry->>Registry: lookup in factories map
    Registry-->>Factory: FactoryBundle
    deactivate Registry
    
    Factory-->>Client: factory instance
    deactivate Factory
    
    Note over Client,Button: Component Creation
    
    Client->>Factory: createButton()
    activate Factory
    
    Factory->>Bundle: createButton()
    activate Bundle
    Bundle->>Button: buttonSupplier.get()
    activate Button
    Button-->>Bundle: button instance
    deactivate Button
    Bundle-->>Factory: MinimalButton
    deactivate Bundle
    
    Factory-->>Client: MinimalButton
    deactivate Factory
    
    Client->>Button: render()
    activate Button
    Button-->>Client: "Rendering minimal button..."
    deactivate Button
```

## Config-driven Factory Sequence

```mermaid
sequenceDiagram
    participant Client
    participant ConfigLoader as ThemeConfig.ConfigLoader
    participant Config as ThemeConfig
    participant Factory as ConfigDrivenFactory
    participant Reflection as Class Loader
    participant Button as EnterpriseButton
    
    Note over Client,Button: Configuration-Driven Creation
    
    Client->>ConfigLoader: loadFromFile("enterprise")
    activate ConfigLoader
    
    ConfigLoader->>ConfigLoader: parse configuration
    ConfigLoader->>Config: new ThemeConfig("Enterprise", "professional")
    activate Config
    Config-->>ConfigLoader: config instance
    deactivate Config
    
    ConfigLoader->>Config: addComponent("button", componentConfig)
    activate Config
    Config-->>ConfigLoader: component added
    deactivate Config
    
    ConfigLoader-->>Client: ThemeConfig
    deactivate ConfigLoader
    
    Client->>Factory: new ConfigDrivenFactory(config)
    activate Factory
    Factory-->>Client: factory instance
    deactivate Factory
    
    Note over Client,Button: Dynamic Component Creation
    
    Client->>Factory: createButton()
    activate Factory
    
    Factory->>Config: getComponentConfig("button")
    activate Config
    Config-->>Factory: ComponentConfig
    deactivate Config
    
    Factory->>Reflection: Class.forName("...EnterpriseButton")
    activate Reflection
    Reflection-->>Factory: Class object
    deactivate Reflection
    
    Factory->>Reflection: constructor.newInstance(properties)
    activate Reflection
    Reflection->>Button: new EnterpriseButton(properties)
    activate Button
    Button-->>Reflection: button instance
    deactivate Button
    Reflection-->>Factory: EnterpriseButton
    deactivate Reflection
    
    Factory-->>Client: EnterpriseButton
    deactivate Factory
    
    Client->>Button: render()
    activate Button
    Button-->>Client: "Rendering enterprise button..."
    deactivate Button
```

## Prototype-backed Factory Sequence

```mermaid
sequenceDiagram
    participant Client
    participant Factory as PrototypeFactory
    participant ButtonProto as VintageButton (Prototype)
    participant ButtonClone as VintageButton (Clone)
    participant CheckboxProto as VintageCheckbox (Prototype)
    participant CheckboxClone as VintageCheckbox (Clone)
    
    Note over Client,CheckboxClone: Prototype-based Creation
    
    Note over Client,CheckboxClone: Initial Setup (expensive)
    
    Client->>ButtonProto: new VintageButton()
    activate ButtonProto
    ButtonProto->>ButtonProto: performExpensiveInitialization()
    ButtonProto-->>Client: prototype instance
    deactivate ButtonProto
    
    Client->>CheckboxProto: new VintageCheckbox()
    activate CheckboxProto
    CheckboxProto-->>Client: prototype instance
    deactivate CheckboxProto
    
    Client->>Factory: new PrototypeFactory("Vintage", prototypes...)
    activate Factory
    Factory-->>Client: factory instance
    deactivate Factory
    
    Note over Client,CheckboxClone: Fast Cloning
    
    Client->>Factory: createButton()
    activate Factory
    
    Factory->>ButtonProto: clone()
    activate ButtonProto
    ButtonProto->>ButtonClone: new VintageButton(this)
    activate ButtonClone
    Note right of ButtonClone: Copy existing data<br/>Skip expensive init
    ButtonClone-->>ButtonProto: cloned instance
    deactivate ButtonClone
    ButtonProto-->>Factory: VintageButton clone
    deactivate ButtonProto
    
    Factory-->>Client: VintageButton (cloned)
    deactivate Factory
    
    Client->>Factory: createCheckbox()
    activate Factory
    
    Factory->>CheckboxProto: clone()
    activate CheckboxProto
    CheckboxProto->>CheckboxClone: new VintageCheckbox(this)
    activate CheckboxClone
    Note right of CheckboxClone: State reset on clone<br/>(checked = false)
    CheckboxClone-->>CheckboxProto: cloned instance
    deactivate CheckboxClone
    CheckboxProto-->>Factory: VintageCheckbox clone
    deactivate CheckboxProto
    
    Factory-->>Client: VintageCheckbox (cloned)
    deactivate Factory
    
    Note over Client,CheckboxClone: Verify Independence
    
    Client->>ButtonClone: render()
    activate ButtonClone
    ButtonClone-->>Client: "Rendering vintage button..."
    deactivate ButtonClone
    
    Client->>CheckboxClone: check()
    activate CheckboxClone
    CheckboxClone-->>Client: "Vintage checkbox toggled on..."
    deactivate CheckboxClone
    
    Note over Client,CheckboxClone: Original prototype unchanged
```

## Family Consistency Verification Sequence

```mermaid
sequenceDiagram
    participant Test as TestHarness
    participant Factory as Any Factory
    participant Button
    participant Checkbox
    participant ScrollBar
    
    Note over Test,ScrollBar: Family Consistency Test
    
    Test->>Factory: createButton()
    activate Factory
    Factory-->>Test: Button instance
    deactivate Factory
    
    Test->>Factory: createCheckbox()
    activate Factory
    Factory-->>Test: Checkbox instance
    deactivate Factory
    
    Test->>Factory: createScrollBar()
    activate Factory
    Factory-->>Test: ScrollBar instance
    deactivate Factory
    
    Note over Test,ScrollBar: Style Verification
    
    Test->>Button: getStyle()
    activate Button
    Button-->>Test: "Windows"
    deactivate Button
    
    Test->>Checkbox: getStyle()
    activate Checkbox
    Checkbox-->>Test: "Windows"
    deactivate Checkbox
    
    Test->>ScrollBar: getStyle()
    activate ScrollBar
    ScrollBar-->>Test: "Windows"
    deactivate ScrollBar
    
    Test->>Test: verify all styles match
    activate Test
    alt All styles match
        Test-->>Test: PASS - Family consistency maintained
    else Styles don't match
        Test-->>Test: FAIL - Family consistency violated
    end
    deactivate Test
    
    Note over Test,ScrollBar: Functional Test
    
    Test->>Button: render() & onClick()
    activate Button
    Button-->>Test: successful interaction
    deactivate Button
    
    Test->>Checkbox: render(), check(), isChecked()
    activate Checkbox
    Checkbox-->>Test: successful interaction
    deactivate Checkbox
    
    Test->>ScrollBar: render(), scrollTo(50), getPosition()
    activate ScrollBar
    ScrollBar-->>Test: successful interaction
    deactivate ScrollBar
```