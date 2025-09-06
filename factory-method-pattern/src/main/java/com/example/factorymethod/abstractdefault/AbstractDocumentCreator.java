package com.example.factorymethod.abstractdefault;

import com.example.factorymethod.shared.Document;
import com.example.factorymethod.shared.TextDocument;

public abstract class AbstractDocumentCreator {
    
    public void processDocument(String title, String content) {
        Document document = createDocument(title);
        document.open();
        document.setContent(content);
        document.save();
        document.close();
        
        System.out.println("✅ Processed " + document.getDocumentType() + 
                          " document using " + this.getClass().getSimpleName());
        System.out.println();
    }
    
    protected Document createDocument(String title) {
        System.out.println("🏭 Using default factory method to create text document...");
        return new TextDocument(title);
    }
    
    public abstract String getCreatorType();
}