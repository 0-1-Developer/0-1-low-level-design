package com.example.abstractfactory.common;

/**
 * Abstract product interface for buttons.
 * All button implementations must provide these operations.
 */
public interface Button {
    void render();
    void onClick();
    String getStyle();
}