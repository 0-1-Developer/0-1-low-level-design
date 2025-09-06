package com.example.factorymethod.shared;

public class HtmlDocument extends Document {
    
    public HtmlDocument(String title) {
        super(title);
    }
    
    @Override
    public void open() {
        System.out.println("🌐 Opening HTML document: " + title);
    }
    
    @Override
    public void save() {
        System.out.println("💾 Saving HTML document: " + title + ".html");
    }
    
    @Override
    public void close() {
        System.out.println("🔐 Closing HTML document: " + title);
    }
    
    @Override
    public String getDocumentType() {
        return "HTML";
    }
}