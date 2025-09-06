package com.example.factorymethod.shared;

public class XmlDocument extends Document {
    
    public XmlDocument(String title) {
        super(title);
    }
    
    @Override
    public void open() {
        System.out.println("📋 Opening XML document: " + title);
    }
    
    @Override
    public void save() {
        System.out.println("💾 Saving XML document: " + title + ".xml");
    }
    
    @Override
    public void close() {
        System.out.println("🔐 Closing XML document: " + title);
    }
    
    @Override
    public String getDocumentType() {
        return "XML";
    }
}