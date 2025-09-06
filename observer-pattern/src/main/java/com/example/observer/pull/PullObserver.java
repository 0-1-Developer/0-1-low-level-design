package com.example.observer.pull;

public interface PullObserver {
    void update(PullSubject subject);
}