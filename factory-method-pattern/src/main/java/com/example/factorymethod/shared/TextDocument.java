package com.example.factorymethod.shared;

public class TextDocument extends Document {
    
    public TextDocument(String title) {
        super(title);
    }
    
    @Override
    public void open() {
        System.out.println("📝 Opening text document: " + title);
    }
    
    @Override
    public void save() {
        System.out.println("💾 Saving text document: " + title + ".txt");
    }
    
    @Override
    public void close() {
        System.out.println("🔐 Closing text document: " + title);
    }
    
    @Override
    public String getDocumentType() {
        return "TEXT";
    }
}