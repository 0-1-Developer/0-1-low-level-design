package com.example.strategy.composite;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Collection of document processing strategies for demonstrating composite pattern.
 * Each strategy performs a specific text transformation operation.
 */
public class DocumentProcessingStrategies {

    /**
     * Converts text to uppercase
     */
    public static final CompositeStrategy<TextDocument, TextDocument> UPPERCASE_CONVERTER = 
        (document, context) -> {
            context.put("operation", "uppercase_conversion");
            context.put("original_length", document.getContent().length());
            
            String upperContent = document.getContent().toUpperCase();
            TextDocument result = document.withContent(upperContent);
            
            context.put("result_length", result.getContent().length());
            return result;
        };

    /**
     * Converts text to lowercase
     */
    public static final CompositeStrategy<TextDocument, TextDocument> LOWERCASE_CONVERTER = 
        (document, context) -> {
            context.put("operation", "lowercase_conversion");
            context.put("original_length", document.getContent().length());
            
            String lowerContent = document.getContent().toLowerCase();
            TextDocument result = document.withContent(lowerContent);
            
            context.put("result_length", result.getContent().length());
            return result;
        };

    /**
     * Removes extra whitespace and normalizes spacing
     */
    public static final CompositeStrategy<TextDocument, TextDocument> WHITESPACE_NORMALIZER = 
        (document, context) -> {
            context.put("operation", "whitespace_normalization");
            context.put("original_word_count", document.getWordCount());
            
            String normalizedContent = document.getContent()
                .replaceAll("\\s+", " ")  // Replace multiple spaces with single space
                .trim();                   // Remove leading/trailing whitespace
            
            TextDocument result = document.withContent(normalizedContent);
            
            context.put("result_word_count", result.getWordCount());
            context.put("whitespace_removed", document.getContent().length() - normalizedContent.length());
            return result;
        };

    /**
     * Adds line numbers to the document
     */
    public static final CompositeStrategy<TextDocument, TextDocument> LINE_NUMBER_ADDER = 
        (document, context) -> {
            context.put("operation", "line_numbering");
            
            String[] lines = document.getContent().split("\n");
            context.put("line_count", lines.length);
            
            StringBuilder numberedContent = new StringBuilder();
            for (int i = 0; i < lines.length; i++) {
                numberedContent.append(String.format("%d: %s", i + 1, lines[i]));
                if (i < lines.length - 1) {
                    numberedContent.append("\n");
                }
            }
            
            TextDocument result = document.withContent(numberedContent.toString());
            context.put("result_length", result.getContent().length());
            return result;
        };

    /**
     * Removes line numbers from the document
     */
    public static final CompositeStrategy<TextDocument, TextDocument> LINE_NUMBER_REMOVER = 
        (document, context) -> {
            context.put("operation", "line_number_removal");
            
            String[] lines = document.getContent().split("\n");
            StringBuilder unnumberedContent = new StringBuilder();
            
            for (int i = 0; i < lines.length; i++) {
                // Remove line numbers (pattern: "number: ")
                String line = lines[i].replaceFirst("^\\d+:\\s*", "");
                unnumberedContent.append(line);
                if (i < lines.length - 1) {
                    unnumberedContent.append("\n");
                }
            }
            
            TextDocument result = document.withContent(unnumberedContent.toString());
            context.put("lines_processed", lines.length);
            return result;
        };

    /**
     * Capitalizes the first letter of each sentence
     */
    public static final CompositeStrategy<TextDocument, TextDocument> SENTENCE_CAPITALIZER = 
        (document, context) -> {
            context.put("operation", "sentence_capitalization");
            
            String content = document.getContent();
            StringBuilder result = new StringBuilder();
            boolean capitalizeNext = true;
            int sentenceCount = 0;
            
            for (char c : content.toCharArray()) {
                if (capitalizeNext && Character.isLetter(c)) {
                    result.append(Character.toUpperCase(c));
                    capitalizeNext = false;
                } else {
                    result.append(c);
                }
                
                if (c == '.' || c == '!' || c == '?') {
                    capitalizeNext = true;
                    sentenceCount++;
                }
            }
            
            context.put("sentence_count", sentenceCount);
            return document.withContent(result.toString());
        };

    /**
     * Removes punctuation from the document
     */
    public static final CompositeStrategy<TextDocument, TextDocument> PUNCTUATION_REMOVER = 
        (document, context) -> {
            context.put("operation", "punctuation_removal");
            
            String originalContent = document.getContent();
            String cleanContent = originalContent.replaceAll("[\\p{Punct}]", "");
            
            context.put("punctuation_removed", originalContent.length() - cleanContent.length());
            return document.withContent(cleanContent);
        };

    /**
     * Adds a header to the document
     */
    public static final CompositeStrategy<TextDocument, TextDocument> HEADER_ADDER = 
        (document, context) -> {
            context.put("operation", "header_addition");
            
            String header = context.get("header", "=== DOCUMENT HEADER ===");
            String contentWithHeader = header + "\n\n" + document.getContent();
            
            context.put("header_added", header);
            return document.withContent(contentWithHeader);
        };

    /**
     * Adds a footer to the document
     */
    public static final CompositeStrategy<TextDocument, TextDocument> FOOTER_ADDER = 
        (document, context) -> {
            context.put("operation", "footer_addition");
            
            String footer = context.get("footer", "=== END OF DOCUMENT ===");
            String contentWithFooter = document.getContent() + "\n\n" + footer;
            
            context.put("footer_added", footer);
            return document.withContent(contentWithFooter);
        };

    /**
     * Word counter strategy that returns analytics instead of modifying content
     */
    public static final CompositeStrategy<TextDocument, String> WORD_COUNTER = 
        (document, context) -> {
            context.put("operation", "word_counting");
            
            String content = document.getContent();
            String[] words = content.trim().split("\\s+");
            int wordCount = words.length;
            int characterCount = content.length();
            int lineCount = content.split("\n").length;
            
            context.put("word_count", wordCount);
            context.put("character_count", characterCount);
            context.put("line_count", lineCount);
            
            return String.format("Analysis: %d words, %d characters, %d lines", 
                               wordCount, characterCount, lineCount);
        };

    /**
     * Quality checker strategy that validates document quality
     */
    public static final CompositeStrategy<TextDocument, Boolean> QUALITY_CHECKER = 
        (document, context) -> {
            context.put("operation", "quality_check");
            
            String content = document.getContent();
            boolean hasTitle = document.getTitle() != null && !document.getTitle().trim().isEmpty();
            boolean hasContent = content != null && content.trim().length() > 0;
            boolean hasMinimumWords = document.getWordCount() >= 10;
            boolean hasProperCapitalization = content.matches("^[A-Z].*");
            
            context.put("has_title", hasTitle);
            context.put("has_content", hasContent);
            context.put("has_minimum_words", hasMinimumWords);
            context.put("has_proper_capitalization", hasProperCapitalization);
            
            boolean isQualityDocument = hasTitle && hasContent && hasMinimumWords && hasProperCapitalization;
            context.put("quality_score", isQualityDocument ? 100 : 50);
            
            return isQualityDocument;
        };

    /**
     * Factory method to create a custom text replacement strategy
     */
    public static CompositeStrategy<TextDocument, TextDocument> createTextReplacer(String searchText, String replaceText) {
        return (document, context) -> {
            context.put("operation", "text_replacement");
            context.put("search_text", searchText);
            context.put("replace_text", replaceText);
            
            String originalContent = document.getContent();
            String replacedContent = originalContent.replace(searchText, replaceText);
            
            int lengthDiff = searchText.length() - replaceText.length();
            int replacementCount = 0;
            if (lengthDiff != 0) {
                replacementCount = (originalContent.length() - replacedContent.length()) / lengthDiff;
            } else {
                // If replacement text is same length, count occurrences by counting splits
                String[] parts = originalContent.split(java.util.regex.Pattern.quote(searchText), -1);
                replacementCount = parts.length - 1;
            }
            context.put("replacement_count", Math.max(0, replacementCount));
            
            return document.withContent(replacedContent);
        };
    }

    /**
     * Factory method to create a word filter strategy
     */
    public static CompositeStrategy<TextDocument, TextDocument> createWordFilter(String... wordsToRemove) {
        return (document, context) -> {
            context.put("operation", "word_filtering");
            context.put("words_to_filter", Arrays.asList(wordsToRemove));
            
            String content = document.getContent();
            String filteredContent = content;
            
            int totalFiltered = 0;
            for (String word : wordsToRemove) {
                String beforeFilter = filteredContent;
                filteredContent = filteredContent.replaceAll("\\b" + word + "\\b", "");
                if (word.length() > 0) {
                    totalFiltered += (beforeFilter.length() - filteredContent.length()) / word.length();
                }
            }
            
            // Clean up extra spaces
            filteredContent = filteredContent.replaceAll("\\s+", " ").trim();
            
            context.put("words_filtered", totalFiltered);
            return document.withContent(filteredContent);
        };
    }
}