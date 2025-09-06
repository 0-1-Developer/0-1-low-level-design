package com.example.factorymethod.classic;

import com.example.factorymethod.shared.Document;
import com.example.factorymethod.shared.TextDocument;

public class TextEditor extends Application {
    
    @Override
    protected Document createDocument(String title) {
        System.out.println("🏭 TextEditor factory creating text document...");
        return new TextDocument(title);
    }
    
    @Override
    public String getApplicationName() {
        return "Text Editor";
    }
}