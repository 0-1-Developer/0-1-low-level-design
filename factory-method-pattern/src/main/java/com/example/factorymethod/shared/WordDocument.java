package com.example.factorymethod.shared;

public class WordDocument extends Document {
    
    public WordDocument(String title) {
        super(title);
    }
    
    @Override
    public void open() {
        System.out.println("📄 Opening Word document: " + title);
    }
    
    @Override
    public void save() {
        System.out.println("💾 Saving Word document: " + title + ".docx");
    }
    
    @Override
    public void close() {
        System.out.println("🔐 Closing Word document: " + title);
    }
    
    @Override
    public String getDocumentType() {
        return "WORD";
    }
}