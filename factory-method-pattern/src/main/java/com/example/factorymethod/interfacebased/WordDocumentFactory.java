package com.example.factorymethod.interfacebased;

import com.example.factorymethod.shared.Document;
import com.example.factorymethod.shared.WordDocument;

public class WordDocumentFactory implements DocumentFactory {
    
    @Override
    public Document createDocument(String title) {
        System.out.println("🏭 Interface-based WordDocumentFactory creating document...");
        return new WordDocument(title);
    }
    
    @Override
    public String getFactoryType() {
        return "Word Document Factory";
    }
}