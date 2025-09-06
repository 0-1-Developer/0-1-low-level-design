package com.example.factorymethod.parameterized;

public enum DocumentType {
    TEXT("txt", "Text Document"),
    PDF("pdf", "PDF Document"),
    WORD("docx", "Word Document"),
    HTML("html", "HTML Document"),
    XML("xml", "XML Document");
    
    private final String extension;
    private final String displayName;
    
    DocumentType(String extension, String displayName) {
        this.extension = extension;
        this.displayName = displayName;
    }
    
    public String getExtension() {
        return extension;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}