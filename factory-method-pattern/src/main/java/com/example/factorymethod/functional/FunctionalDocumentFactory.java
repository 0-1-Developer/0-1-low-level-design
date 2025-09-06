package com.example.factorymethod.functional;

import com.example.factorymethod.shared.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

public class FunctionalDocumentFactory {
    
    private static final Map<String, Function<String, Document>> factories = new HashMap<>();
    private static final Map<String, Supplier<Document>> zeroArgFactories = new HashMap<>();
    
    static {
        initializeFactories();
    }
    
    private static void initializeFactories() {
        factories.put("text", TextDocument::new);
        factories.put("pdf", PdfDocument::new);
        factories.put("word", WordDocument::new);
        factories.put("html", HtmlDocument::new);
        factories.put("xml", XmlDocument::new);
        
        zeroArgFactories.put("default_text", () -> new TextDocument("Default Text"));
        zeroArgFactories.put("default_pdf", () -> new PdfDocument("Default PDF"));
        zeroArgFactories.put("template_report", () -> {
            TextDocument doc = new TextDocument("Report Template");
            doc.setContent("TEMPLATE: Executive Summary\n1. Analysis\n2. Conclusions");
            return doc;
        });
    }
    
    public static Document createDocument(String type, String title) {
        return Optional.ofNullable(factories.get(type.toLowerCase()))
                .map(factory -> {
                    System.out.println("🏭 Functional factory creating " + type + " document...");
                    return factory.apply(title);
                })
                .orElseThrow(() -> new IllegalArgumentException("Unsupported type: " + type));
    }
    
    public static Document createDocument(String type) {
        return Optional.ofNullable(zeroArgFactories.get(type.toLowerCase()))
                .map(factory -> {
                    System.out.println("🏭 Zero-arg functional factory creating " + type + "...");
                    return factory.get();
                })
                .orElseThrow(() -> new IllegalArgumentException("No zero-arg factory for type: " + type));
    }
    
    public static Document createDocumentWithProcessor(String type, String title, 
                                                      Function<Document, Document> processor) {
        Document document = createDocument(type, title);
        System.out.println("🔄 Applying document processor...");
        return processor.apply(document);
    }
    
    public static Document createConditionalDocument(String type, String title, 
                                                    boolean condition, 
                                                    Function<Document, Document> transformer) {
        Document document = createDocument(type, title);
        if (condition) {
            System.out.println("✅ Condition met, applying transformation...");
            return transformer.apply(document);
        }
        return document;
    }
    
    public static void registerFactory(String type, Function<String, Document> factory) {
        factories.put(type.toLowerCase(), factory);
        System.out.println("📝 Registered functional factory for: " + type);
    }
    
    public static void registerZeroArgFactory(String type, Supplier<Document> factory) {
        zeroArgFactories.put(type.toLowerCase(), factory);
        System.out.println("📝 Registered zero-arg functional factory for: " + type);
    }
    
    public static Document composeFactories(String type1, String type2, String title) {
        Function<String, Document> factory1 = factories.get(type1.toLowerCase());
        Function<String, Document> factory2 = factories.get(type2.toLowerCase());
        
        if (factory1 == null || factory2 == null) {
            throw new IllegalArgumentException("One or both factory types not found");
        }
        
        System.out.println("🔗 Composing factories: " + type1 + " -> " + type2);
        Document doc1 = factory1.apply(title + "_" + type1);
        Document doc2 = factory2.apply(title + "_" + type2);
        
        doc1.setContent("Composed from " + type1 + " and " + type2 + ": " + 
                       doc1.getContent() + " | " + doc2.getContent());
        return doc1;
    }
}