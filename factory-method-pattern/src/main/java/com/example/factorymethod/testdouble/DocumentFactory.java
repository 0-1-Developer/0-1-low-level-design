package com.example.factorymethod.testdouble;

import com.example.factorymethod.shared.Document;

public interface DocumentFactory {
    Document createDocument(String title);
    String getFactoryType();
}