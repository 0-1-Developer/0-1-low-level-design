package com.example.factorymethod.classic;

import com.example.factorymethod.shared.Document;
import com.example.factorymethod.shared.WordDocument;

public class WordProcessor extends Application {
    
    @Override
    protected Document createDocument(String title) {
        System.out.println("🏭 WordProcessor factory creating Word document...");
        return new WordDocument(title);
    }
    
    @Override
    public String getApplicationName() {
        return "Word Processor";
    }
}