package com.example.observer.push;

public interface PushObserver<T> {
    void update(T data);
}