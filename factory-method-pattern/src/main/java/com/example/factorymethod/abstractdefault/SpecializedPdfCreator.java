package com.example.factorymethod.abstractdefault;

import com.example.factorymethod.shared.Document;
import com.example.factorymethod.shared.PdfDocument;

public class SpecializedPdfCreator extends AbstractDocumentCreator {
    
    @Override
    protected Document createDocument(String title) {
        System.out.println("🏭 Overriding default factory method to create PDF document...");
        return new PdfDocument(title);
    }
    
    @Override
    public String getCreatorType() {
        return "Specialized PDF Creator (overrides factory method)";
    }
}