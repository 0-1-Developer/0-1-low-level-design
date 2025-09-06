package com.example.factorymethod.parameterized;

import com.example.factorymethod.shared.*;
import java.util.Map;
import java.util.function.Function;
import java.util.EnumMap;

public class ParameterizedDocumentFactory {
    
    private static final Map<DocumentType, Function<String, Document>> factories = 
        new EnumMap<>(DocumentType.class);
    
    static {
        factories.put(DocumentType.TEXT, TextDocument::new);
        factories.put(DocumentType.PDF, PdfDocument::new);
        factories.put(DocumentType.WORD, WordDocument::new);
        factories.put(DocumentType.HTML, HtmlDocument::new);
        factories.put(DocumentType.XML, XmlDocument::new);
    }
    
    public static Document createDocument(DocumentType type, String title) {
        System.out.println("🏭 Parameterized factory creating " + type.getDisplayName() + "...");
        
        Function<String, Document> factory = factories.get(type);
        if (factory == null) {
            throw new IllegalArgumentException("Unsupported document type: " + type);
        }
        
        return factory.apply(title);
    }
    
    public static Document createDocument(String typeString, String title) {
        try {
            DocumentType type = DocumentType.valueOf(typeString.toUpperCase());
            return createDocument(type, title);
        } catch (IllegalArgumentException e) {
            System.out.println("⚠️  Unknown type '" + typeString + "', defaulting to TEXT");
            return createDocument(DocumentType.TEXT, title);
        }
    }
    
    public static Document createDocumentWithFormat(DocumentType type, String title, String format) {
        System.out.println("🏭 Creating " + type.getDisplayName() + " with format: " + format);
        
        Document document = createDocument(type, title);
        
        switch (format.toLowerCase()) {
            case "compressed":
                document.setContent("[COMPRESSED] " + document.getContent());
                break;
            case "encrypted":
                document.setContent("[ENCRYPTED] " + document.getContent());
                break;
            case "template":
                setTemplateContent(document, type);
                break;
            default:
                System.out.println("📄 Using default format");
        }
        
        return document;
    }
    
    private static void setTemplateContent(Document document, DocumentType type) {
        switch (type) {
            case TEXT:
                document.setContent("TEXT TEMPLATE: Header\nBody Content\nFooter");
                break;
            case PDF:
                document.setContent("PDF TEMPLATE: Title Page\nTable of Contents\nContent Sections");
                break;
            case WORD:
                document.setContent("WORD TEMPLATE: Document Header\nFormatted Content\nFooter Information");
                break;
            case HTML:
                document.setContent("HTML TEMPLATE: <html><head><title>" + document.getTitle() + "</title></head><body>Content</body></html>");
                break;
            case XML:
                document.setContent("XML TEMPLATE: <?xml version=\"1.0\"?><root><title>" + document.getTitle() + "</title><content/></root>");
                break;
        }
    }
    
    public static DocumentType[] getSupportedTypes() {
        return DocumentType.values();
    }
    
    private ParameterizedDocumentFactory() {
    }
}