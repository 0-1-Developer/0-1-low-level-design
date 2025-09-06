package com.example.factorymethod.testdouble;

import com.example.factorymethod.shared.Document;
import com.example.factorymethod.shared.TextDocument;

public class ProductionDocumentFactory implements DocumentFactory {
    
    @Override
    public Document createDocument(String title) {
        System.out.println("🏭 Production factory creating real document...");
        return new TextDocument(title);
    }
    
    @Override
    public String getFactoryType() {
        return "Production Factory";
    }
}