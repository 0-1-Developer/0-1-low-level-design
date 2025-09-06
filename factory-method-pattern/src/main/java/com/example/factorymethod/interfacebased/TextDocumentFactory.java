package com.example.factorymethod.interfacebased;

import com.example.factorymethod.shared.Document;
import com.example.factorymethod.shared.TextDocument;

public class TextDocumentFactory implements DocumentFactory {
    
    @Override
    public Document createDocument(String title) {
        System.out.println("🏭 Interface-based TextDocumentFactory creating document...");
        return new TextDocument(title);
    }
    
    @Override
    public String getFactoryType() {
        return "Text Document Factory";
    }
}