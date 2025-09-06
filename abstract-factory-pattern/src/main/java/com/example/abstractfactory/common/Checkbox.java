package com.example.abstractfactory.common;

/**
 * Abstract product interface for checkboxes.
 * All checkbox implementations must provide these operations.
 */
public interface Checkbox {
    void render();
    void check();
    void uncheck();
    boolean isChecked();
    String getStyle();
}