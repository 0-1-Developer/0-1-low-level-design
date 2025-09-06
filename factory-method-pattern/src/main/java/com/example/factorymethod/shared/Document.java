package com.example.factorymethod.shared;

public abstract class Document {
    protected String title;
    protected String content;
    
    public Document(String title) {
        this.title = title;
        this.content = "";
    }
    
    public abstract void open();
    public abstract void save();
    public abstract void close();
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getContent() {
        return content;
    }
    
    public abstract String getDocumentType();
}