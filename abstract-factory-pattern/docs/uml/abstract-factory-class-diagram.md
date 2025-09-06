# Abstract Factory Pattern - Class Diagram

## Classic Implementation Class Diagram

```mermaid
classDiagram
    class GUIFactory {
        <<interface>>
        +createButton() Button
        +createCheckbox() Checkbox
        +createScrollBar() ScrollBar
    }
    
    class Button {
        <<interface>>
        +render() void
        +onClick() void
        +getStyle() String
    }
    
    class Checkbox {
        <<interface>>
        +render() void
        +check() void
        +uncheck() void
        +isChecked() boolean
        +getStyle() String
    }
    
    class ScrollBar {
        <<interface>>
        +render() void
        +scrollTo(position: int) void
        +getPosition() int
        +getStyle() String
    }
    
    class WindowsFactory {
        +createButton() Button
        +createCheckbox() Checkbox
        +createScrollBar() ScrollBar
    }
    
    class MacOSFactory {
        +createButton() Button
        +createCheckbox() Checkbox
        +createScrollBar() ScrollBar
    }
    
    class WindowsButton {
        +render() void
        +onClick() void
        +getStyle() String
    }
    
    class WindowsCheckbox {
        -checked: boolean
        +render() void
        +check() void
        +uncheck() void
        +isChecked() boolean
        +getStyle() String
    }
    
    class WindowsScrollBar {
        -position: int
        +render() void
        +scrollTo(position: int) void
        +getPosition() int
        +getStyle() String
    }
    
    class MacOSButton {
        +render() void
        +onClick() void
        +getStyle() String
    }
    
    class MacOSCheckbox {
        -checked: boolean
        +render() void
        +check() void
        +uncheck() void
        +isChecked() boolean
        +getStyle() String
    }
    
    class MacOSScrollBar {
        -position: int
        +render() void
        +scrollTo(position: int) void
        +getPosition() int
        +getStyle() String
    }
    
    class Client {
        -factory: GUIFactory
        -button: Button
        -checkbox: Checkbox
        -scrollBar: ScrollBar
        +Client(factory: GUIFactory)
        +createUI() void
        +renderUI() void
    }
    
    GUIFactory <|.. WindowsFactory : implements
    GUIFactory <|.. MacOSFactory : implements
    
    Button <|.. WindowsButton : implements
    Button <|.. MacOSButton : implements
    
    Checkbox <|.. WindowsCheckbox : implements
    Checkbox <|.. MacOSCheckbox : implements
    
    ScrollBar <|.. WindowsScrollBar : implements
    ScrollBar <|.. MacOSScrollBar : implements
    
    WindowsFactory ..> WindowsButton : creates
    WindowsFactory ..> WindowsCheckbox : creates
    WindowsFactory ..> WindowsScrollBar : creates
    
    MacOSFactory ..> MacOSButton : creates
    MacOSFactory ..> MacOSCheckbox : creates
    MacOSFactory ..> MacOSScrollBar : creates
    
    Client --> GUIFactory : uses
    Client --> Button : uses
    Client --> Checkbox : uses
    Client --> ScrollBar : uses
```

## Parameterized Factory Variant

```mermaid
classDiagram
    class ParameterizedUIFactory {
        -theme: ThemeStyle
        +ParameterizedUIFactory(theme: ThemeStyle)
        +createButton() Button
        +createCheckbox() Checkbox
        +createScrollBar() ScrollBar
        +createComponent(type: ComponentType) T
        -createMaterialComponent(type: ComponentType) Object
        -createFluentComponent(type: ComponentType) Object
        -createNeumorphicComponent(type: ComponentType) Object
    }
    
    class ThemeStyle {
        <<enumeration>>
        MATERIAL
        FLUENT
        NEUMORPHIC
        +getDisplayName() String
    }
    
    class ComponentType {
        <<enumeration>>
        BUTTON
        CHECKBOX
        SCROLLBAR
    }
    
    class MaterialButton {
        +render() void
        +onClick() void
        +getStyle() String
    }
    
    class FluentButton {
        +render() void
        +onClick() void
        +getStyle() String
    }
    
    class NeumorphicButton {
        +render() void
        +onClick() void
        +getStyle() String
    }
    
    ParameterizedUIFactory --> ThemeStyle : uses
    ParameterizedUIFactory --> ComponentType : uses
    ParameterizedUIFactory ..> MaterialButton : creates
    ParameterizedUIFactory ..> FluentButton : creates
    ParameterizedUIFactory ..> NeumorphicButton : creates
    
    Button <|.. MaterialButton : implements
    Button <|.. FluentButton : implements
    Button <|.. NeumorphicButton : implements
```

## Registry-backed Factory Variant

```mermaid
classDiagram
    class UIComponentRegistry {
        -INSTANCE: UIComponentRegistry
        -factories: Map~String,FactoryBundle~
        -UIComponentRegistry()
        +getInstance() UIComponentRegistry$
        +registerFactory(themeName: String, bundle: FactoryBundle) void
        +getFactory(themeName: String) FactoryBundle
        +hasFactory(themeName: String) boolean
    }
    
    class FactoryBundle {
        -buttonSupplier: Supplier~Button~
        -checkboxSupplier: Supplier~Checkbox~
        -scrollBarSupplier: Supplier~ScrollBar~
        +createButton() Button
        +createCheckbox() Checkbox
        +createScrollBar() ScrollBar
    }
    
    class RegistryBackedFactory {
        -registry: UIComponentRegistry
        -themeName: String
        -bundle: FactoryBundle
        +RegistryBackedFactory(themeName: String)
        +createButton() Button
        +createCheckbox() Checkbox
        +createScrollBar() ScrollBar
        +switchTheme(newTheme: String) void
    }
    
    UIComponentRegistry --> FactoryBundle : manages
    RegistryBackedFactory --> UIComponentRegistry : uses
    RegistryBackedFactory --> FactoryBundle : delegates to
    
    FactoryBundle ..> Button : creates
    FactoryBundle ..> Checkbox : creates
    FactoryBundle ..> ScrollBar : creates
```

## Functional Factory Variant

```mermaid
classDiagram
    class FunctionalFactory {
        -buttonSupplier: Supplier~Button~
        -checkboxSupplier: Supplier~Checkbox~
        -scrollBarSupplier: Supplier~ScrollBar~
        -themeName: String
        +FunctionalFactory(themeName, suppliers...)
        +createButton() Button
        +createCheckbox() Checkbox
        +createScrollBar() ScrollBar
    }
    
    class Builder {
        -themeName: String
        -buttonSupplier: Supplier~Button~
        -checkboxSupplier: Supplier~Checkbox~
        -scrollBarSupplier: Supplier~ScrollBar~
        +withTheme(themeName: String) Builder
        +withButton(supplier: Supplier~Button~) Builder
        +withCheckbox(supplier: Supplier~Checkbox~) Builder
        +withScrollBar(supplier: Supplier~ScrollBar~) Builder
        +build() FunctionalFactory
    }
    
    class Themes {
        +createFlatDesign() FunctionalFactory$
        +createSkeuomorphic() FunctionalFactory$
        +createAccessible() FunctionalFactory$
    }
    
    FunctionalFactory +-- Builder : nested
    FunctionalFactory +-- Themes : nested
    
    Builder --> FunctionalFactory : builds
    Themes --> FunctionalFactory : creates
    
    FunctionalFactory ..> Button : creates via supplier
    FunctionalFactory ..> Checkbox : creates via supplier
    FunctionalFactory ..> ScrollBar : creates via supplier
```

## Config-driven Factory Variant

```mermaid
classDiagram
    class ConfigDrivenFactory {
        -config: ThemeConfig
        +ConfigDrivenFactory(config: ThemeConfig)
        +createButton() Button
        +createCheckbox() Checkbox
        +createScrollBar() ScrollBar
        -createComponent(type: String, expectedType: Class) T
        +reloadConfig(newConfig: ThemeConfig) void
    }
    
    class ThemeConfig {
        -themeName: String
        -factoryType: String
        -components: Map~String,ComponentConfig~
        +getThemeName() String
        +getComponentConfig(type: String) ComponentConfig
    }
    
    class ComponentConfig {
        -className: String
        -properties: Map~String,String~
        +getClassName() String
        +getProperties() Map~String,String~
    }
    
    class ConfigLoader {
        +loadFromEnvironment() ThemeConfig$
        +loadFromFile(filename: String) ThemeConfig$
        +loadFromProperties(props: Map) ThemeConfig$
    }
    
    class Configurable {
        <<interface>>
        +configure(properties: Map~String,String~) void
    }
    
    ConfigDrivenFactory --> ThemeConfig : uses
    ThemeConfig --> ComponentConfig : contains
    ThemeConfig +-- ConfigLoader : nested
    
    ConfigDrivenFactory ..> Button : creates
    ConfigDrivenFactory ..> Checkbox : creates
    ConfigDrivenFactory ..> ScrollBar : creates
    
    Button <|-- Configurable : may implement
    Checkbox <|-- Configurable : may implement
    ScrollBar <|-- Configurable : may implement
```