package com.example.strategy.registry;

public interface CompressionStrategy {
    byte[] compress(String data);
    String decompress(byte[] compressedData);
    String getAlgorithmName();
    double getCompressionRatio();
}