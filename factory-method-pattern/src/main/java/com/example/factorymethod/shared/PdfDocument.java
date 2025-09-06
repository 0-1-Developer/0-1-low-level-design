package com.example.factorymethod.shared;

public class PdfDocument extends Document {
    
    public PdfDocument(String title) {
        super(title);
    }
    
    @Override
    public void open() {
        System.out.println("📖 Opening PDF document: " + title);
    }
    
    @Override
    public void save() {
        System.out.println("💾 Saving PDF document: " + title + ".pdf");
    }
    
    @Override
    public void close() {
        System.out.println("🔐 Closing PDF document: " + title);
    }
    
    @Override
    public String getDocumentType() {
        return "PDF";
    }
}