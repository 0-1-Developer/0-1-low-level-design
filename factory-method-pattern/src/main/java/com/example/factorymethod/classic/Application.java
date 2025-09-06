package com.example.factorymethod.classic;

import com.example.factorymethod.shared.Document;

public abstract class Application {
    
    public void processDocument(String title, String content) {
        Document document = createDocument(title);
        document.open();
        document.setContent(content);
        document.save();
        document.close();
        
        System.out.println("✅ Successfully processed " + document.getDocumentType() + 
                          " document with factory: " + this.getClass().getSimpleName());
        System.out.println();
    }
    
    protected abstract Document createDocument(String title);
    
    public abstract String getApplicationName();
}