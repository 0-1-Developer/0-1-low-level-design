package com.example.factorymethod.interfacebased;

import com.example.factorymethod.shared.Document;

public interface DocumentFactory {
    Document createDocument(String title);
    String getFactoryType();
}