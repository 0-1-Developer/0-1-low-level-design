package com.example.abstractfactory.common;

/**
 * Abstract product interface for scroll bars.
 * All scroll bar implementations must provide these operations.
 */
public interface ScrollBar {
    void render();
    void scrollTo(int position);
    int getPosition();
    String getStyle();
}