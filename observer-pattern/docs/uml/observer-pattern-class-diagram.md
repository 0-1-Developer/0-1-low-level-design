# Observer Pattern - Class Diagrams

## Classic GoF Observer Pattern

```mermaid
classDiagram
    class Subject {
        <<abstract>>
        -observers: List~Observer~
        +attach(Observer observer) void
        +detach(Observer observer) void
        #notifyObservers() void
        +getObserverCount() int
    }
    
    class Observer {
        <<interface>>
        +update(Subject subject) void
    }
    
    class WeatherStation {
        -temperature: float
        -humidity: float
        -pressure: float
        +setMeasurements(float temp, float humidity, float pressure) void
        +getTemperature() float
        +getHumidity() float
        +getPressure() float
    }
    
    class CurrentConditionsDisplay {
        -temperature: float
        -humidity: float
        -displayName: String
        +update(Subject subject) void
        +display() void
        +getTemperature() float
        +getHumidity() float
    }
    
    class StatisticsDisplay {
        -temperatures: List~Float~
        -displayName: String
        +update(Subject subject) void
        +display() void
        +getAverageTemperature() float
    }
    
    class ForecastDisplay {
        -currentPressure: float
        -lastPressure: float
        -displayName: String
        +update(Subject subject) void
        +display() void
        +getCurrentPressure() float
    }
    
    Subject <|-- WeatherStation
    Observer <|.. CurrentConditionsDisplay
    Observer <|.. StatisticsDisplay
    Observer <|.. ForecastDisplay
    Subject o-- Observer : notifies *
    
    note for Subject "Maintains list of observers\nand notifies them of changes"
    note for Observer "Receives update notifications\nfrom subject"
    note for WeatherStation "Concrete subject that\nstores weather data"
```

## Push Model Observer Pattern

```mermaid
classDiagram
    class PushSubject~T~ {
        -observers: List~PushObserver~T~~
        +subscribe(PushObserver~T~ observer) void
        +unsubscribe(PushObserver~T~ observer) void
        +notifyObservers(T data) void
        +getSubscriberCount() int
    }
    
    class PushObserver~T~ {
        <<interface>>
        +update(T data) void
    }
    
    class WeatherData {
        -temperature: float
        -humidity: float
        -pressure: float
        -location: String
        -timestamp: long
        +getTemperature() float
        +getHumidity() float
        +getPressure() float
        +getLocation() String
        +getTimestamp() long
        +toString() String
    }
    
    class PushWeatherStation {
        -stationId: String
        +reportWeather(float temp, float humidity, float pressure, String location) void
        +getStationId() String
    }
    
    class MobileApp {
        -appName: String
        -userId: String
        +update(WeatherData data) void
        +getAppName() String
        +getUserId() String
    }
    
    class WeatherWebsite {
        -websiteName: String
        -dateFormat: SimpleDateFormat
        +update(WeatherData data) void
        +getWebsiteName() String
    }
    
    class DataAnalyticsService {
        -serviceName: String
        -dataHistory: List~WeatherData~
        +update(WeatherData data) void
        -analyzeData(WeatherData data) void
        +getDataPointCount() int
    }
    
    PushSubject <|-- PushWeatherStation
    PushObserver <|.. MobileApp
    PushObserver <|.. WeatherWebsite
    PushObserver <|.. DataAnalyticsService
    PushSubject o-- PushObserver : pushes data to *
    PushWeatherStation ..> WeatherData : creates
    
    note for PushSubject "Generic subject that pushes\ncomplete data payload"
    note for WeatherData "Immutable data transfer object\ncontaining complete weather info"
    note for PushWeatherStation "Creates and broadcasts\ncomplete weather data objects"
```

## Pull Model Observer Pattern

```mermaid
classDiagram
    class PullSubject {
        <<abstract>>
        -observers: List~PullObserver~
        +attach(PullObserver observer) void
        +detach(PullObserver observer) void
        #notifyObservers() void
        +getObserverCount() int
    }
    
    class PullObserver {
        <<interface>>
        +update(PullSubject subject) void
    }
    
    class PullWeatherStation {
        -temperature: float
        -humidity: float
        -pressure: float
        -location: String
        -lastUpdated: Date
        -stationId: String
        +setMeasurements(float temp, float humidity, float pressure, String location) void
        +getTemperature() float
        +getHumidity() float
        +getPressure() float
        +getLocation() String
        +getLastUpdated() Date
        +getStationId() String
        +hasValidData() boolean
    }
    
    class TemperatureOnlyDisplay {
        -displayName: String
        +update(PullSubject subject) void
    }
    
    class HumidityTracker {
        -trackerName: String
        -humidityReadings: List~Float~
        +update(PullSubject subject) void
        +getReadingCount() int
    }
    
    class CompleteWeatherMonitor {
        -monitorName: String
        -timeFormat: SimpleDateFormat
        +update(PullSubject subject) void
        -generateWeatherSummary(float temp, float humidity, float pressure) void
    }
    
    PullSubject <|-- PullWeatherStation
    PullObserver <|.. TemperatureOnlyDisplay
    PullObserver <|.. HumidityTracker
    PullObserver <|.. CompleteWeatherMonitor
    PullSubject o-- PullObserver : notifies *
    
    note for PullObserver "Observers pull only the\ndata they actually need"
    note for PullWeatherStation "Provides selective data access\nvia getter methods"
    note for TemperatureOnlyDisplay "Only pulls temperature data\nfor efficiency"
```

## Property-based Observer Pattern

```mermaid
classDiagram
    class PropertyChangeSupport {
        -sourceBean: Object
        -globalListeners: List~PropertyChangeListener~
        -propertyListeners: Map~String, List~PropertyChangeListener~~
        +addPropertyChangeListener(PropertyChangeListener listener) void
        +addPropertyChangeListener(String property, PropertyChangeListener listener) void
        +removePropertyChangeListener(PropertyChangeListener listener) void
        +removePropertyChangeListener(String property, PropertyChangeListener listener) void
        +firePropertyChange(String property, Object oldValue, Object newValue) void
        +getGlobalListenerCount() int
        +getPropertyListenerCount(String property) int
        +getTotalListenerCount() int
    }
    
    class PropertyChangeListener {
        <<interface>>
        +propertyChanged(PropertyChangeEvent event) void
    }
    
    class PropertyChangeEvent {
        -propertyName: String
        -oldValue: Object
        -newValue: Object
        -source: Object
        +getPropertyName() String
        +getOldValue() Object
        +getNewValue() Object
        +getSource() Object
        +toString() String
    }
    
    class PropertyWeatherStation {
        +PROPERTY_TEMPERATURE: String
        +PROPERTY_HUMIDITY: String
        +PROPERTY_PRESSURE: String
        +PROPERTY_LOCATION: String
        -support: PropertyChangeSupport
        -stationId: String
        -temperature: float
        -humidity: float
        -pressure: float
        -location: String
        +setTemperature(float temperature) void
        +setHumidity(float humidity) void
        +setPressure(float pressure) void
        +setLocation(String location) void
        +addPropertyChangeListener(PropertyChangeListener listener) void
        +addPropertyChangeListener(String property, PropertyChangeListener listener) void
        +removePropertyChangeListener(PropertyChangeListener listener) void
        +removePropertyChangeListener(String property, PropertyChangeListener listener) void
        +updateAllMeasurements(float temp, float humidity, float pressure, String location) void
        +getTemperature() float
        +getHumidity() float
        +getPressure() float
        +getLocation() String
        +getStationId() String
        +getTotalListeners() int
        +getGlobalListeners() int
        +getPropertyListeners(String property) int
    }
    
    class TemperatureAlertSystem {
        -systemName: String
        -alertThreshold: float
        +propertyChanged(PropertyChangeEvent event) void
        +getAlertThreshold() float
    }
    
    class HumidityMonitor {
        -monitorName: String
        -humidityHistory: List~Float~
        +propertyChanged(PropertyChangeEvent event) void
        -analyzeHumidityTrend() void
        +getReadingCount() int
    }
    
    class LocationLogger {
        -loggerName: String
        -locationCounts: Map~String, Integer~
        +propertyChanged(PropertyChangeEvent event) void
        +getLocationCounts() Map~String, Integer~
        +getTotalLocations() int
    }
    
    class AllPropertyMonitor {
        -monitorName: String
        -totalChanges: int
        +propertyChanged(PropertyChangeEvent event) void
        +getTotalChanges() int
    }
    
    PropertyWeatherStation *-- PropertyChangeSupport
    PropertyChangeSupport ..> PropertyChangeEvent : creates
    PropertyChangeListener <|.. TemperatureAlertSystem
    PropertyChangeListener <|.. HumidityMonitor
    PropertyChangeListener <|.. LocationLogger
    PropertyChangeListener <|.. AllPropertyMonitor
    PropertyChangeEvent --> PropertyChangeListener : delivered to
    
    note for PropertyChangeSupport "Central event dispatcher\nsupports both global and\nproperty-specific listeners"
    note for PropertyChangeEvent "Immutable event object\ncontaining change details"
    note for PropertyWeatherStation "Fires property change events\nfor individual property updates"
```

## Event Bus Observer Pattern

```mermaid
classDiagram
    class EventBus {
        -subscribers: Map~String, List~Consumer~Event~~~
        -busName: String
        +subscribe(String topic, Consumer~Event~ subscriber) void
        +unsubscribe(String topic, Consumer~Event~ subscriber) void
        +publish(Event event) void
        +getSubscriberCount(String topic) int
        +getTotalSubscriberCount() int
    }
    
    class Event {
        -topic: String
        -data: Object
        -timestamp: long
        +getTopic() String
        +getData() Object
        +getTimestamp() long
        +toString() String
    }
    
    class Publisher {
        <<abstract>>
        #eventBus: EventBus
        +publish(String topic, Object data) void
    }
    
    class Subscriber {
        <<abstract>>
        -subscriberName: String
        +handleEvent(Event event) void
        +getSubscriberName() String
    }
    
    EventBus o-- "many" Consumer : routes events to
    EventBus ..> Event : processes
    Publisher --> EventBus : publishes to
    Subscriber --> EventBus : subscribes to
    Event --> Consumer : delivered to
    
    note for EventBus "Central message router\ndecouples publishers\nfrom subscribers"
    note for Event "Immutable message object\nwith topic and payload"
    note for Publisher "Any component can publish\nevents to topics"
    note for Subscriber "Components subscribe to\nspecific topics of interest"
```