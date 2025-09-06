package com.example.observer.push;

import java.util.ArrayList;
import java.util.List;

public class PushSubject<T> {
    private final List<PushObserver<T>> observers = new ArrayList<>();

    public void subscribe(PushObserver<T> observer) {
        observers.add(observer);
    }

    public void unsubscribe(PushObserver<T> observer) {
        observers.remove(observer);
    }

    public void notifyObservers(T data) {
        for (PushObserver<T> observer : observers) {
            observer.update(data);
        }
    }

    public int getSubscriberCount() {
        return observers.size();
    }
}