# Observer Pattern - Sequence Diagrams

## Classic GoF Observer - Registration and Notification Flow

```mermaid
sequenceDiagram
    participant Client
    participant WS as WeatherStation
    participant CD as CurrentDisplay
    participant SD as StatsDisplay
    participant FD as ForecastDisplay
    
    Note over Client, FD: Observer Registration Phase
    Client->>WS: attach(CurrentDisplay)
    WS->>WS: observers.add(CurrentDisplay)
    Client->>WS: attach(StatsDisplay)
    WS->>WS: observers.add(StatsDisplay)
    Client->>WS: attach(ForecastDisplay)
    WS->>WS: observers.add(ForecastDisplay)
    
    Note over Client, FD: State Change and Notification Phase
    Client->>WS: setMeasurements(80, 65, 30.4)
    WS->>WS: temperature = 80
    WS->>WS: humidity = 65
    WS->>WS: pressure = 30.4
    WS->>WS: notifyObservers()
    
    loop for each observer in observers list
        WS->>CD: update(this)
        CD->>WS: getTemperature()
        WS-->>CD: 80.0
        CD->>WS: getHumidity()
        WS-->>CD: 65.0
        CD->>CD: display()
        
        WS->>SD: update(this)
        SD->>WS: getTemperature()
        WS-->>SD: 80.0
        SD->>SD: temperatures.add(80.0)
        SD->>SD: display()
        
        WS->>FD: update(this)
        FD->>WS: getPressure()
        WS-->>FD: 30.4
        FD->>FD: currentPressure = 30.4
        FD->>FD: display()
    end
    
    Note over Client, FD: Observer Removal Phase
    Client->>WS: detach(ForecastDisplay)
    WS->>WS: observers.remove(ForecastDisplay)
    
    Client->>WS: setMeasurements(82, 70, 29.2)
    WS->>WS: notifyObservers()
    
    Note over WS, FD: Only CD and SD receive updates now
    WS->>CD: update(this)
    WS->>SD: update(this)
    Note over FD: ForecastDisplay no longer notified
```

## Push Model Observer - Data Broadcasting Flow

```mermaid
sequenceDiagram
    participant Client
    participant PWS as PushWeatherStation
    participant MA as MobileApp
    participant WW as WeatherWebsite
    participant DAS as DataAnalyticsService
    
    Note over Client, DAS: Subscription Phase
    Client->>PWS: subscribe(MobileApp)
    PWS->>PWS: observers.add(MobileApp)
    Client->>PWS: subscribe(WeatherWebsite)
    PWS->>PWS: observers.add(WeatherWebsite)
    Client->>PWS: subscribe(DataAnalyticsService)
    PWS->>PWS: observers.add(DataAnalyticsService)
    
    Note over Client, DAS: Weather Data Broadcasting
    Client->>PWS: reportWeather(72, 45, 30.1, "New York")
    PWS->>PWS: create WeatherData(72, 45, 30.1, "New York")
    PWS->>PWS: notifyObservers(weatherData)
    
    par Parallel notification to all observers
        PWS->>MA: update(WeatherData{temp=72, humidity=45, pressure=30.1, location="New York"})
        MA->>MA: process complete weather data
        MA->>MA: check for alerts (temp < 85, no alert)
        MA->>MA: display notification to user
    and
        PWS->>WW: update(WeatherData{temp=72, humidity=45, pressure=30.1, location="New York"})
        WW->>WW: format timestamp
        WW->>WW: update website display with all data
    and
        PWS->>DAS: update(WeatherData{temp=72, humidity=45, pressure=30.1, location="New York"})
        DAS->>DAS: dataHistory.add(weatherData)
        DAS->>DAS: analyzeData(weatherData)
        DAS->>DAS: generate analytics report
    end
    
    Note over Client, DAS: Subsequent Update with Alert
    Client->>PWS: reportWeather(88, 65, 29.8, "Miami")
    PWS->>PWS: create WeatherData(88, 65, 29.8, "Miami")
    PWS->>PWS: notifyObservers(weatherData)
    
    par Hot weather alert triggered
        PWS->>MA: update(WeatherData{temp=88, humidity=65, pressure=29.8, location="Miami"})
        MA->>MA: temp > 85, trigger hot weather alert
        MA->>MA: send push notification to user
    and
        PWS->>WW: update(WeatherData{temp=88, humidity=65, pressure=29.8, location="Miami"})
        WW->>WW: update website with Miami weather
    and
        PWS->>DAS: update(WeatherData{temp=88, humidity=65, pressure=29.8, location="Miami"})
        DAS->>DAS: analyze temperature trend (72→88 increase)
        DAS->>DAS: log significant temperature change
    end
```

## Pull Model Observer - Selective Data Access Flow

```mermaid
sequenceDiagram
    participant Client
    participant PWS as PullWeatherStation
    participant TOD as TemperatureDisplay
    participant HT as HumidityTracker
    participant CWM as CompleteWeatherMonitor
    
    Note over Client, CWM: Observer Registration
    Client->>PWS: attach(TemperatureDisplay)
    Client->>PWS: attach(HumidityTracker)
    Client->>PWS: attach(CompleteWeatherMonitor)
    
    Note over Client, CWM: Data Update and Selective Pulling
    Client->>PWS: setMeasurements(75, 60, 30.1, "Denver")
    PWS->>PWS: store all measurements
    PWS->>PWS: lastUpdated = now()
    PWS->>PWS: notifyObservers()
    
    par Observers pull different data subsets
        PWS->>TOD: update(this)
        TOD->>PWS: getTemperature()
        PWS-->>TOD: 75.0 (logs: "Temperature requested")
        TOD->>TOD: display temperature only
        Note over TOD: Only pulled temperature data
    and
        PWS->>HT: update(this)
        HT->>PWS: getHumidity()
        PWS-->>HT: 60.0 (logs: "Humidity requested")
        HT->>HT: humidityReadings.add(60.0)
        HT->>HT: analyze humidity trend
        Note over HT: Only pulled humidity data
    and
        PWS->>CWM: update(this)
        CWM->>PWS: getLocation()
        PWS-->>CWM: "Denver" (logs: "Location requested")
        CWM->>PWS: getTemperature()
        PWS-->>CWM: 75.0 (logs: "Temperature requested")
        CWM->>PWS: getHumidity()
        PWS-->>CWM: 60.0 (logs: "Humidity requested")
        CWM->>PWS: getPressure()
        PWS-->>CWM: 30.1 (logs: "Pressure requested")
        CWM->>PWS: getLastUpdated()
        PWS-->>CWM: timestamp
        CWM->>CWM: generateWeatherSummary()
        CWM->>CWM: display complete report
        Note over CWM: Pulled all available data
    end
    
    Note over Client, CWM: Observer Removal and Subsequent Update
    Client->>PWS: detach(TemperatureDisplay)
    
    Client->>PWS: setMeasurements(85, 45, 29.8, "Las Vegas")
    PWS->>PWS: notifyObservers()
    
    Note over TOD: TemperatureDisplay no longer receives updates
    
    par Only remaining observers pull data
        PWS->>HT: update(this)
        HT->>PWS: getHumidity()
        PWS-->>HT: 45.0
        HT->>HT: detect significant humidity change (60→45)
    and
        PWS->>CWM: update(this)
        CWM->>PWS: getLocation()
        CWM->>PWS: getTemperature()
        CWM->>PWS: getHumidity()
        CWM->>PWS: getPressure()
        CWM->>CWM: display Las Vegas weather report
    end
```

## Property-based Observer - Targeted Property Notifications

```mermaid
sequenceDiagram
    participant Client
    participant PWS as PropertyWeatherStation
    participant PCS as PropertyChangeSupport
    participant TAS as TempAlertSystem
    participant HM as HumidityMonitor
    participant LL as LocationLogger
    participant APM as AllPropertyMonitor
    
    Note over Client, APM: Listener Registration Phase
    Client->>PWS: addPropertyChangeListener("temperature", TempAlertSystem)
    PWS->>PCS: addPropertyChangeListener("temperature", TempAlertSystem)
    PCS->>PCS: propertyListeners.get("temperature").add(TempAlertSystem)
    
    Client->>PWS: addPropertyChangeListener("humidity", HumidityMonitor)
    PWS->>PCS: addPropertyChangeListener("humidity", HumidityMonitor)
    
    Client->>PWS: addPropertyChangeListener("location", LocationLogger)
    PWS->>PCS: addPropertyChangeListener("location", LocationLogger)
    
    Client->>PWS: addPropertyChangeListener(AllPropertyMonitor)
    PWS->>PCS: addPropertyChangeListener(AllPropertyMonitor)
    PCS->>PCS: globalListeners.add(AllPropertyMonitor)
    
    Note over Client, APM: Property Change Notifications
    Client->>PWS: setTemperature(75.0)
    PWS->>PWS: oldValue = current temperature
    PWS->>PWS: temperature = 75.0
    PWS->>PCS: firePropertyChange("temperature", oldValue, 75.0)
    PCS->>PCS: create PropertyChangeEvent("temperature", oldValue, 75.0)
    
    par Temperature property change notifications
        PCS->>APM: propertyChanged(PropertyChangeEvent)
        APM->>APM: totalChanges++
        APM->>APM: log property change
    and
        PCS->>TAS: propertyChanged(PropertyChangeEvent)
        TAS->>TAS: check if temp >= alertThreshold
        TAS->>TAS: log temperature change
        Note over TAS: Only temperature listeners notified
    end
    
    Note over HM, LL: HumidityMonitor and LocationLogger not notified
    
    Client->>PWS: setHumidity(60.0)
    PWS->>PCS: firePropertyChange("humidity", oldValue, 60.0)
    
    par Humidity property change notifications
        PCS->>APM: propertyChanged(PropertyChangeEvent)
        APM->>APM: check for extreme humidity
    and
        PCS->>HM: propertyChanged(PropertyChangeEvent)
        HM->>HM: humidityHistory.add(60.0)
        HM->>HM: analyze humidity trend
        Note over HM: Only humidity listeners notified
    end
    
    Client->>PWS: setLocation("Chicago")
    PWS->>PCS: firePropertyChange("location", oldValue, "Chicago")
    
    par Location property change notifications
        PCS->>APM: propertyChanged(PropertyChangeEvent)
        APM->>APM: log location change
    and
        PCS->>LL: propertyChanged(PropertyChangeEvent)
        LL->>LL: locationCounts.put("Chicago", count + 1)
        LL->>LL: check if returning to previous location
        Note over LL: Only location listeners notified
    end
    
    Note over Client, APM: Bulk Update - Multiple Property Changes
    Client->>PWS: updateAllMeasurements(90, 85, 29.5, "Miami")
    
    par Sequential property change events
        PWS->>PCS: firePropertyChange("temperature", 75.0, 90.0)
        PCS->>TAS: propertyChanged(PropertyChangeEvent)
        TAS->>TAS: temp >= alertThreshold, trigger HIGH TEMP ALERT
        PCS->>APM: propertyChanged(PropertyChangeEvent)
    and
        PWS->>PCS: firePropertyChange("humidity", 60.0, 85.0)
        PCS->>HM: propertyChanged(PropertyChangeEvent)
        HM->>HM: detect high humidity warning
        PCS->>APM: propertyChanged(PropertyChangeEvent)
    and
        PWS->>PCS: firePropertyChange("location", "Chicago", "Miami")
        PCS->>LL: propertyChanged(PropertyChangeEvent)
        PCS->>APM: propertyChanged(PropertyChangeEvent)
    end
```

## Event Bus Observer - Decoupled Pub-Sub Flow

```mermaid
sequenceDiagram
    participant P1 as WeatherPublisher
    participant P2 as EmergencyPublisher
    participant EB as EventBus
    participant S1 as TempAlertSubscriber
    participant S2 as HumiditySubscriber
    participant S3 as EmergencySubscriber
    participant S4 as LoggerSubscriber
    
    Note over P1, S4: Subscription Phase
    S1->>EB: subscribe("temperature", tempAlertHandler)
    EB->>EB: subscribers.get("temperature").add(tempAlertHandler)
    
    S2->>EB: subscribe("humidity", humidityHandler)
    EB->>EB: subscribers.get("humidity").add(humidityHandler)
    
    S3->>EB: subscribe("weather.severe", emergencyHandler)
    EB->>EB: subscribers.get("weather.severe").add(emergencyHandler)
    
    S4->>EB: subscribe("temperature", loggerHandler)
    EB->>EB: subscribers.get("temperature").add(loggerHandler)
    Note over EB: Multiple subscribers can listen to same topic
    
    Note over P1, S4: Publishing and Routing Phase
    P1->>EB: publish(Event("temperature", 72.5))
    EB->>EB: find subscribers for "temperature" topic
    
    par Temperature event routed to multiple subscribers
        EB->>S1: tempAlertHandler.accept(Event("temperature", 72.5))
        S1->>S1: process temperature alert logic
        S1->>S1: temp < threshold, no alert needed
    and
        EB->>S4: loggerHandler.accept(Event("temperature", 72.5))
        S4->>S4: log temperature reading
        S4->>S4: store in temperature log file
    end
    
    Note over S2, S3: Other subscribers not affected
    
    P1->>EB: publish(Event("humidity", 65.0))
    EB->>EB: find subscribers for "humidity" topic
    EB->>S2: humidityHandler.accept(Event("humidity", 65.0))
    S2->>S2: process humidity data
    S2->>S2: check humidity levels
    
    Note over P2, S4: Emergency Alert Publishing
    P2->>EB: publish(Event("weather.severe", "Tornado warning"))
    EB->>EB: find subscribers for "weather.severe" topic
    EB->>S3: emergencyHandler.accept(Event("weather.severe", "Tornado warning"))
    S3->>S3: process emergency alert
    S3->>S3: broadcast emergency notification
    S3->>S3: activate emergency protocols
    
    Note over P1, S4: High Temperature Alert
    P1->>EB: publish(Event("temperature", 95.0))
    EB->>EB: route to temperature subscribers
    
    par High temperature processed by both subscribers
        EB->>S1: tempAlertHandler.accept(Event("temperature", 95.0))
        S1->>S1: temp > threshold, trigger HIGH TEMP WARNING
        S1->>S1: send alert notifications
    and
        EB->>S4: loggerHandler.accept(Event("temperature", 95.0))
        S4->>S4: log extreme temperature reading
        S4->>S4: flag as significant event
    end
    
    Note over P1, S4: Non-existent Topic
    P1->>EB: publish(Event("pressure", 30.1))
    EB->>EB: find subscribers for "pressure" topic
    EB->>EB: no subscribers found
    Note over EB: Event discarded, no subscribers to notify
```