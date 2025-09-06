package com.example.factorymethod.interfacebased;

import com.example.factorymethod.shared.Document;

public class DocumentProcessor {
    private final DocumentFactory factory;
    
    public DocumentProcessor(DocumentFactory factory) {
        this.factory = factory;
    }
    
    public void processDocument(String title, String content) {
        System.out.println("📄 Using " + factory.getFactoryType() + " to process document");
        
        Document document = factory.createDocument(title);
        document.open();
        document.setContent(content);
        document.save();
        document.close();
        
        System.out.println("✅ Successfully processed " + document.getDocumentType() + 
                          " document using interface-based factory");
        System.out.println();
    }
    
    public void switchFactory(DocumentFactory newFactory) {
        System.out.println("🔄 Switching from " + factory.getFactoryType() + 
                          " to " + newFactory.getFactoryType());
    }
}