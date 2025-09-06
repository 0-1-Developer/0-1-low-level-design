package com.example.strategy.composite;

import java.util.*;

public class CompositeStrategyDemo {
    public static void main(String[] args) {
        System.out.println("=== Composite Strategy Pattern Demo ===\n");
        
        // Create sample documents for processing
        TextDocument document1 = new TextDocument(
            "hello world! this is a sample document.   it has   extra   spaces.\n" +
            "this is another line with some text.\n" +
            "final line of the document.",
            "Sample Document",
            "plain_text"
        );
        
        TextDocument document2 = new TextDocument(
            "the quick brown fox jumps over the lazy dog. " +
            "this sentence contains all letters of the alphabet! " +
            "it's commonly used for testing purposes.",
            "Test Document",
            "text"
        );
        
        System.out.println("Original Document 1: " + document1);
        System.out.println("Original Document 2: " + document2);
        System.out.println();
        
        // 1. Sequential Processing (Pipeline)
        System.out.println("1. Sequential Processing (Document Pipeline):");
        System.out.println("--------------------------------------------");
        
        ExecutionContext context1 = new ExecutionContext();
        TextDocument processedDoc = CompositeStrategyProcessor.executeSequential(
            document1, 
            context1,
            DocumentProcessingStrategies.WHITESPACE_NORMALIZER,
            DocumentProcessingStrategies.SENTENCE_CAPITALIZER,
            DocumentProcessingStrategies.HEADER_ADDER,
            DocumentProcessingStrategies.FOOTER_ADDER
        );
        
        System.out.println("Pipeline Result: " + processedDoc);
        System.out.println("Processing Steps: " + context1.get("step_count"));
        System.out.println("Execution Time: " + context1.getElapsedTime() + "ms");
        System.out.println();
        
        // 2. Parallel Processing (Multiple Analyses)
        System.out.println("2. Parallel Processing (Multiple Document Analyses):");
        System.out.println("---------------------------------------------------");
        
        ExecutionContext context2 = new ExecutionContext();
        List<String> analyses = CompositeStrategyProcessor.executeParallel(
            document2,
            context2,
            DocumentProcessingStrategies.WORD_COUNTER,
            (doc, ctx) -> {
                ctx.put("operation", "character_analysis");
                long vowels = doc.getContent().chars().filter(c -> "aeiouAEIOU".indexOf(c) != -1).count();
                long consonants = doc.getContent().chars().filter(Character::isLetter).count() - vowels;
                return String.format("Vowels: %d, Consonants: %d", vowels, consonants);
            },
            (doc, ctx) -> {
                ctx.put("operation", "sentence_analysis");
                int sentences = doc.getContent().split("[.!?]+").length;
                return String.format("Sentences: %d", sentences);
            }
        );
        
        System.out.println("Parallel Analysis Results:");
        for (int i = 0; i < analyses.size(); i++) {
            System.out.println("  Analysis " + (i + 1) + ": " + analyses.get(i));
        }
        System.out.println("Total Strategies: " + context2.get("strategy_count"));
        System.out.println();
        
        // 3. Conditional Processing
        System.out.println("3. Conditional Processing:");
        System.out.println("-------------------------");
        
        ExecutionContext context3 = new ExecutionContext();
        List<CompositeStrategyProcessor.ConditionalStrategy<TextDocument, TextDocument>> conditionalStrategies = Arrays.asList(
            new CompositeStrategyProcessor.ConditionalStrategy<>(
                doc -> doc.getWordCount() > 20,
                DocumentProcessingStrategies.LINE_NUMBER_ADDER
            ),
            new CompositeStrategyProcessor.ConditionalStrategy<>(
                doc -> doc.getContent().contains("quick"),
                DocumentProcessingStrategies.UPPERCASE_CONVERTER
            ),
            new CompositeStrategyProcessor.ConditionalStrategy<>(
                doc -> doc.getTitle().contains("Test"),
                DocumentProcessingStrategies.createTextReplacer("the", "THE")
            )
        );
        
        List<TextDocument> conditionalResults = CompositeStrategyProcessor.executeConditional(
            document2, context3, conditionalStrategies);
        
        System.out.println("Conditional Processing Results:");
        System.out.println("  Total Conditions: " + context3.get("total_conditions"));
        System.out.println("  Executed Strategies: " + context3.get("executed_count"));
        for (int i = 0; i < conditionalResults.size(); i++) {
            System.out.println("  Result " + (i + 1) + ": " + conditionalResults.get(i).getContent().substring(0, Math.min(60, conditionalResults.get(i).getContent().length())) + "...");
        }
        System.out.println();
        
        // 4. Until Success Processing (Fallback Chain)
        System.out.println("4. Until Success Processing (Fallback Chain):");
        System.out.println("---------------------------------------------");
        
        ExecutionContext context4 = new ExecutionContext();
        Optional<Boolean> qualityResult = CompositeStrategyProcessor.executeUntilSuccess(
            document1,
            context4,
            // Strategy that might fail
            (doc, ctx) -> {
                ctx.put("operation", "strict_quality_check");
                // This will fail for our sample document
                if (doc.getWordCount() < 50) return null; // Simulate failure
                return true;
            },
            // Fallback strategy
            DocumentProcessingStrategies.QUALITY_CHECKER,
            // Final fallback
            (doc, ctx) -> {
                ctx.put("operation", "basic_validation");
                return doc.getContent() != null && !doc.getContent().trim().isEmpty();
            }
        );
        
        System.out.println("Quality Check Result: " + qualityResult.orElse(false));
        System.out.println("Attempts Made: " + context4.get("attempts_made"));
        System.out.println("Successful Strategy Index: " + context4.get("successful_strategy_index", -1));
        System.out.println();
        
        // 5. Priority-Based Processing
        System.out.println("5. Priority-Based Processing:");
        System.out.println("-----------------------------");
        
        ExecutionContext context5 = new ExecutionContext();
        context5.put("header", "=== HIGH PRIORITY DOCUMENT ===");
        
        List<CompositeStrategyProcessor.PriorityStrategy<TextDocument, TextDocument>> priorityStrategies = Arrays.asList(
            new CompositeStrategyProcessor.PriorityStrategy<>(10, DocumentProcessingStrategies.HEADER_ADDER),
            new CompositeStrategyProcessor.PriorityStrategy<>(8, DocumentProcessingStrategies.WHITESPACE_NORMALIZER),
            new CompositeStrategyProcessor.PriorityStrategy<>(6, DocumentProcessingStrategies.SENTENCE_CAPITALIZER),
            new CompositeStrategyProcessor.PriorityStrategy<>(4, DocumentProcessingStrategies.LINE_NUMBER_ADDER),
            new CompositeStrategyProcessor.PriorityStrategy<>(2, DocumentProcessingStrategies.FOOTER_ADDER)
        );
        
        List<TextDocument> priorityResults = CompositeStrategyProcessor.executeWithPriority(
            document1, context5, priorityStrategies, 3); // Execute only top 3
        
        System.out.println("Priority Processing Results (Top 3):");
        System.out.println("  Max Executions: " + context5.get("max_executions"));
        System.out.println("  Strategies Executed: " + context5.get("strategies_executed"));
        TextDocument finalResult = priorityResults.get(priorityResults.size() - 1);
        System.out.println("  Final Result Preview: " + finalResult.getContent().substring(0, Math.min(80, finalResult.getContent().length())) + "...");
        System.out.println();
        
        // 6. Voting-Based Processing
        System.out.println("6. Voting-Based Processing:");
        System.out.println("---------------------------");
        
        ExecutionContext context6 = new ExecutionContext();
        
        // Create a simple document for voting
        TextDocument voteDoc = new TextDocument("Hello World", "Vote Document", "text");
        
        String votingResult = CompositeStrategyProcessor.executeWithVoting(
            voteDoc,
            context6,
            // Three strategies that return different case transformations
            (doc, ctx) -> doc.getContent().toUpperCase(),
            (doc, ctx) -> doc.getContent().toLowerCase(),
            (doc, ctx) -> doc.getContent().toUpperCase(), // Another vote for uppercase
            (doc, ctx) -> doc.getContent().toUpperCase(), // Another vote for uppercase
            (doc, ctx) -> doc.getContent().toLowerCase()  // Another vote for lowercase
        );
        
        System.out.println("Original: " + voteDoc.getContent());
        System.out.println("Voting Result: " + votingResult);
        System.out.println("Winning Votes: " + context6.get("winning_votes"));
        System.out.println("Total Unique Results: " + context6.get("total_unique_results"));
        System.out.println();
        
        // 7. Complex Composition Example
        System.out.println("7. Complex Composition (Nested Processing):");
        System.out.println("-------------------------------------------");
        
        ExecutionContext masterContext = new ExecutionContext();
        masterContext.put("header", "=== PROCESSED DOCUMENT ===");
        masterContext.put("footer", "=== PROCESSING COMPLETE ===");
        
        // First, clean the document
        TextDocument cleanedDoc = CompositeStrategyProcessor.executeSequential(
            document1,
            masterContext,
            DocumentProcessingStrategies.WHITESPACE_NORMALIZER,
            DocumentProcessingStrategies.PUNCTUATION_REMOVER
        );
        
        // Then apply conditional formatting
        List<CompositeStrategyProcessor.ConditionalStrategy<TextDocument, TextDocument>> nestedConditionals = Arrays.asList(
            new CompositeStrategyProcessor.ConditionalStrategy<>(
                doc -> doc.getWordCount() > 5,
                DocumentProcessingStrategies.SENTENCE_CAPITALIZER
            ),
            new CompositeStrategyProcessor.ConditionalStrategy<>(
                doc -> true, // Always apply
                DocumentProcessingStrategies.HEADER_ADDER
            )
        );
        
        List<TextDocument> nestedResults = CompositeStrategyProcessor.executeConditional(
            cleanedDoc, masterContext, nestedConditionals);
        
        // Finally, add footer
        TextDocument finalDoc = nestedResults.isEmpty() ? cleanedDoc : nestedResults.get(nestedResults.size() - 1);
        finalDoc = DocumentProcessingStrategies.FOOTER_ADDER.execute(finalDoc, masterContext);
        
        System.out.println("Complex Composition Result:");
        System.out.println("  Original Length: " + document1.getContent().length());
        System.out.println("  Final Length: " + finalDoc.getContent().length());
        System.out.println("  Total Processing Time: " + masterContext.getElapsedTime() + "ms");
        System.out.println("  Final Content Preview: " + finalDoc.getContent().substring(0, Math.min(100, finalDoc.getContent().length())) + "...");
        
        System.out.println("\n=== Demo Complete ===");
    }
}