package com.example.factorymethod.interfacebased;

import com.example.factorymethod.shared.Document;
import com.example.factorymethod.shared.PdfDocument;

public class PdfDocumentFactory implements DocumentFactory {
    
    @Override
    public Document createDocument(String title) {
        System.out.println("🏭 Interface-based PdfDocumentFactory creating document...");
        return new PdfDocument(title);
    }
    
    @Override
    public String getFactoryType() {
        return "PDF Document Factory";
    }
}