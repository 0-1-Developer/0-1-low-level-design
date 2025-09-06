package com.example.strategy.composite;

/**
 * Represents a text document for processing.
 * Used as input/output for composite text processing strategies.
 */
public class TextDocument {
    private final String content;
    private final String title;
    private final String format;
    private final int wordCount;

    public TextDocument(String content, String title, String format) {
        this.content = content;
        this.title = title;
        this.format = format;
        this.wordCount = content == null ? 0 : content.trim().split("\\s+").length;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public String getFormat() {
        return format;
    }

    public int getWordCount() {
        return wordCount;
    }

    /**
     * Creates a new document with updated content
     */
    public TextDocument withContent(String newContent) {
        return new TextDocument(newContent, this.title, this.format);
    }

    /**
     * Creates a new document with updated title
     */
    public TextDocument withTitle(String newTitle) {
        return new TextDocument(this.content, newTitle, this.format);
    }

    /**
     * Creates a new document with updated format
     */
    public TextDocument withFormat(String newFormat) {
        return new TextDocument(this.content, this.title, newFormat);
    }

    @Override
    public String toString() {
        return String.format("TextDocument{title='%s', format='%s', wordCount=%d, contentPreview='%s...'}", 
                           title, format, wordCount, 
                           content != null && content.length() > 50 ? content.substring(0, 50) : content);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        TextDocument that = (TextDocument) obj;
        
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        return format != null ? format.equals(that.format) : that.format == null;
    }

    @Override
    public int hashCode() {
        int result = content != null ? content.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (format != null ? format.hashCode() : 0);
        return result;
    }
}