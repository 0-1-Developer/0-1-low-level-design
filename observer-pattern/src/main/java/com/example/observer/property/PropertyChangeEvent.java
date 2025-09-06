package com.example.observer.property;

public class PropertyChangeEvent {
    private final String propertyName;
    private final Object oldValue;
    private final Object newValue;
    private final Object source;

    public PropertyChangeEvent(Object source, String propertyName, Object oldValue, Object newValue) {
        this.source = source;
        this.propertyName = propertyName;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public Object getNewValue() {
        return newValue;
    }

    public Object getSource() {
        return source;
    }

    @Override
    public String toString() {
        return String.format("PropertyChangeEvent{property='%s', old=%s, new=%s}", 
                propertyName, oldValue, newValue);
    }
}