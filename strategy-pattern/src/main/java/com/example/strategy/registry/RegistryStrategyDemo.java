package com.example.strategy.registry;

public class RegistryStrategyDemo {
    public static void main(String[] args) {
        System.out.println("=== Registry-Backed Strategy Pattern Demo ===\n");
        
        CompressionStrategyRegistry registry = CompressionStrategyRegistry.getInstance();
        
        String testData1 = "AAAAAABBBBBBCCCCCC";
        String testData2 = "The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog.";
        String testData3 = "abcdefghijklmnopqrstuvwxyz1234567890";
        
        System.out.println("1. Using Registered Strategies:");
        System.out.println("---------------------------------");
        
        System.out.println("Available strategies: " + registry.getAvailableStrategies());
        
        CompressionStrategy zipStrategy = registry.getStrategy("ZIP");
        testCompression(zipStrategy, testData2);
        
        CompressionStrategy rleStrategy = registry.getStrategy("RLE");
        testCompression(rleStrategy, testData1);
        
        CompressionStrategy lzwStrategy = registry.getStrategy("LZW");
        testCompression(lzwStrategy, testData2);
        
        System.out.println("\n2. Lazy-Loaded Strategies:");
        System.out.println("---------------------------");
        
        CompressionStrategy lazyZip = registry.getStrategy("ZIP-LAZY");
        testCompression(lazyZip, testData3);
        
        System.out.println("\n3. Dynamic Strategy Registration:");
        System.out.println("----------------------------------");
        
        registry.register("CUSTOM", new CompressionStrategy() {
            @Override
            public byte[] compress(String data) {
                return data.toUpperCase().getBytes();
            }
            
            @Override
            public String decompress(byte[] compressedData) {
                return new String(compressedData).toLowerCase();
            }
            
            @Override
            public String getAlgorithmName() {
                return "Custom Upper/Lower Case";
            }
            
            @Override
            public double getCompressionRatio() {
                return 1.0;
            }
        });
        
        CompressionStrategy customStrategy = registry.getStrategy("CUSTOM");
        testCompression(customStrategy, "Hello World");
        
        System.out.println("\n4. Automatic Best Strategy Selection:");
        System.out.println("---------------------------------------");
        
        System.out.println("Test data 1 (repetitive): " + testData1);
        CompressionStrategy best1 = registry.selectBestStrategy(testData1);
        testCompression(best1, testData1);
        
        System.out.println("\nTest data 2 (text with repetition): " + 
                         testData2.substring(0, 30) + "...");
        CompressionStrategy best2 = registry.selectBestStrategy(testData2);
        testCompression(best2, testData2);
        
        System.out.println("\n5. Default Strategy Fallback:");
        System.out.println("-------------------------------");
        
        CompressionStrategy notFound = registry.getStrategy("NON_EXISTENT");
        System.out.println("Using strategy: " + notFound.getAlgorithmName());
        
        System.out.println("\n6. Strategy Hot-Swapping:");
        System.out.println("--------------------------");
        
        String data = "XXXXXXXXYYYYYYZZZZZZ";
        System.out.println("Original data: " + data);
        
        String[] strategies = {"ZIP", "RLE", "LZW"};
        for (String strategyKey : strategies) {
            CompressionStrategy strategy = registry.getStrategy(strategyKey);
            byte[] compressed = strategy.compress(data);
            System.out.printf("%s - Compressed size: %d bytes, Ratio: %.2f%n",
                            strategy.getAlgorithmName(),
                            compressed.length,
                            strategy.getCompressionRatio());
        }
        
        System.out.println("\n=== Demo Complete ===");
    }
    
    private static void testCompression(CompressionStrategy strategy, String data) {
        System.out.println("\nUsing " + strategy.getAlgorithmName() + ":");
        System.out.println("Original: " + (data.length() > 50 ? data.substring(0, 50) + "..." : data));
        
        byte[] compressed = strategy.compress(data);
        String decompressed = strategy.decompress(compressed);
        
        System.out.printf("Original size: %d bytes%n", data.getBytes().length);
        System.out.printf("Compressed size: %d bytes%n", compressed.length);
        System.out.printf("Compression ratio: %.2f%n", strategy.getCompressionRatio());
        System.out.println("Decompression successful: " + data.equals(decompressed));
    }
}