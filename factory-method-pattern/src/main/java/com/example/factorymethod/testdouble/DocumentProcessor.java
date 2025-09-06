package com.example.factorymethod.testdouble;

import com.example.factorymethod.shared.Document;

public class DocumentProcessor {
    private final DocumentFactory factory;
    
    public DocumentProcessor(DocumentFactory factory) {
        this.factory = factory;
    }
    
    public void processDocument(String title, String content) {
        System.out.println("📄 Processing document with " + factory.getFactoryType());
        
        Document document = factory.createDocument(title);
        document.open();
        document.setContent(content);
        document.save();
        document.close();
        
        System.out.println("✅ Document processing completed");
        System.out.println();
    }
    
    public void processBatch(String[] titles, String content) {
        System.out.println("📦 Processing batch of " + titles.length + " documents");
        for (String title : titles) {
            processDocument(title, content);
        }
    }
    
    public String getFactoryType() {
        return factory.getFactoryType();
    }
}