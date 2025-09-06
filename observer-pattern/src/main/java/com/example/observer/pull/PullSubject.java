package com.example.observer.pull;

import java.util.ArrayList;
import java.util.List;

public abstract class PullSubject {
    private final List<PullObserver> observers = new ArrayList<>();

    public void attach(PullObserver observer) {
        observers.add(observer);
    }

    public void detach(PullObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (PullObserver observer : observers) {
            observer.update(this);
        }
    }

    public int getObserverCount() {
        return observers.size();
    }
}