package com.example.factorymethod.classic;

import com.example.factorymethod.shared.Document;
import com.example.factorymethod.shared.PdfDocument;

public class PdfReader extends Application {
    
    @Override
    protected Document createDocument(String title) {
        System.out.println("🏭 PdfReader factory creating PDF document...");
        return new PdfDocument(title);
    }
    
    @Override
    public String getApplicationName() {
        return "PDF Reader";
    }
}