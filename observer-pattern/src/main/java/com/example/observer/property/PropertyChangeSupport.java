package com.example.observer.property;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertyChangeSupport {
    private final Object sourceBean;
    private final Map<String, List<PropertyChangeListener>> propertyListeners = new HashMap<>();
    private final List<PropertyChangeListener> globalListeners = new ArrayList<>();

    public PropertyChangeSupport(Object sourceBean) {
        this.sourceBean = sourceBean;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        globalListeners.add(listener);
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyListeners.computeIfAbsent(propertyName, k -> new ArrayList<>()).add(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        globalListeners.remove(listener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        List<PropertyChangeListener> listeners = propertyListeners.get(propertyName);
        if (listeners != null) {
            listeners.remove(listener);
            if (listeners.isEmpty()) {
                propertyListeners.remove(propertyName);
            }
        }
    }

    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        if (oldValue != null && oldValue.equals(newValue)) {
            return;
        }

        PropertyChangeEvent event = new PropertyChangeEvent(sourceBean, propertyName, oldValue, newValue);

        for (PropertyChangeListener listener : globalListeners) {
            listener.propertyChanged(event);
        }

        List<PropertyChangeListener> specificListeners = propertyListeners.get(propertyName);
        if (specificListeners != null) {
            for (PropertyChangeListener listener : specificListeners) {
                listener.propertyChanged(event);
            }
        }
    }

    public int getGlobalListenerCount() {
        return globalListeners.size();
    }

    public int getPropertyListenerCount(String propertyName) {
        List<PropertyChangeListener> listeners = propertyListeners.get(propertyName);
        return listeners != null ? listeners.size() : 0;
    }

    public int getTotalListenerCount() {
        int total = globalListeners.size();
        for (List<PropertyChangeListener> listeners : propertyListeners.values()) {
            total += listeners.size();
        }
        return total;
    }
}