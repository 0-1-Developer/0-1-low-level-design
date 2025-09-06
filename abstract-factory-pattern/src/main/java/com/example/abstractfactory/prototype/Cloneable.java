package com.example.abstractfactory.prototype;

/**
 * Interface for prototype pattern support.
 * Components that can be cloned should implement this.
 */
public interface Cloneable<T> {
    T createClone();
}