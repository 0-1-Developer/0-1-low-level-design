package com.example.factorymethod.staticmethod;

import com.example.factorymethod.shared.Document;
import com.example.factorymethod.shared.TextDocument;
import com.example.factorymethod.shared.PdfDocument;
import com.example.factorymethod.shared.WordDocument;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DocumentFactory {
    
    private static final Map<String, Document> cache = new ConcurrentHashMap<>();
    
    public static Document createTextDocument(String title) {
        System.out.println("🏭 Static factory method creating text document...");
        return new TextDocument(title);
    }
    
    public static Document createPdfDocument(String title) {
        System.out.println("🏭 Static factory method creating PDF document...");
        return new PdfDocument(title);
    }
    
    public static Document createWordDocument(String title) {
        System.out.println("🏭 Static factory method creating Word document...");
        return new WordDocument(title);
    }
    
    public static Document createCachedTextDocument(String title) {
        String key = "text_" + title;
        return cache.computeIfAbsent(key, k -> {
            System.out.println("🏭 Creating and caching text document: " + title);
            return new TextDocument(title);
        });
    }
    
    public static Document createCachedPdfDocument(String title) {
        String key = "pdf_" + title;
        return cache.computeIfAbsent(key, k -> {
            System.out.println("🏭 Creating and caching PDF document: " + title);
            return new PdfDocument(title);
        });
    }
    
    public static Document createCachedWordDocument(String title) {
        String key = "word_" + title;
        return cache.computeIfAbsent(key, k -> {
            System.out.println("🏭 Creating and caching Word document: " + title);
            return new WordDocument(title);
        });
    }
    
    public static Document createDocumentFromTemplate(String title, String templateType) {
        System.out.println("🏭 Creating document from template: " + templateType);
        switch (templateType.toLowerCase()) {
            case "report":
                Document doc = new TextDocument(title);
                doc.setContent("REPORT TEMPLATE: Executive Summary, Analysis, Conclusions");
                return doc;
            case "letter":
                Document letterDoc = new WordDocument(title);
                letterDoc.setContent("LETTER TEMPLATE: Header, Body, Signature");
                return letterDoc;
            case "manual":
                Document manualDoc = new PdfDocument(title);
                manualDoc.setContent("MANUAL TEMPLATE: Table of Contents, Chapters, Index");
                return manualDoc;
            default:
                return new TextDocument(title);
        }
    }
    
    public static void clearCache() {
        cache.clear();
        System.out.println("🧹 Document cache cleared");
    }
    
    public static int getCacheSize() {
        return cache.size();
    }
    
    private DocumentFactory() {
        // Private constructor to prevent instantiation
    }
}