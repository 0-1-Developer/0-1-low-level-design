package com.example.strategy.registry;

import java.util.*;

public class LzwCompressionStrategy implements CompressionStrategy {
    private double lastCompressionRatio = 0.0;
    
    @Override
    public byte[] compress(String data) {
        if (data == null || data.isEmpty()) {
            return new byte[0];
        }
        
        Map<String, Integer> dictionary = new HashMap<>();
        for (int i = 0; i < 256; i++) {
            dictionary.put("" + (char) i, i);
        }
        
        String w = "";
        List<Integer> compressed = new ArrayList<>();
        int dictSize = 256;
        
        for (char c : data.toCharArray()) {
            String wc = w + c;
            if (dictionary.containsKey(wc)) {
                w = wc;
            } else {
                compressed.add(dictionary.get(w));
                dictionary.put(wc, dictSize++);
                w = "" + c;
            }
        }
        
        if (!w.isEmpty()) {
            compressed.add(dictionary.get(w));
        }
        
        StringBuilder result = new StringBuilder();
        for (Integer code : compressed) {
            result.append((char) code.intValue());
        }
        
        byte[] compressedBytes = result.toString().getBytes();
        lastCompressionRatio = (double) compressedBytes.length / data.getBytes().length;
        return compressedBytes;
    }
    
    @Override
    public String decompress(byte[] compressedData) {
        String compressed = new String(compressedData);
        Map<Integer, String> dictionary = new HashMap<>();
        for (int i = 0; i < 256; i++) {
            dictionary.put(i, "" + (char) i);
        }
        
        List<Integer> codes = new ArrayList<>();
        for (char c : compressed.toCharArray()) {
            codes.add((int) c);
        }
        
        if (codes.isEmpty()) {
            return "";
        }
        
        String w = dictionary.get(codes.remove(0));
        StringBuilder decompressed = new StringBuilder(w);
        int dictSize = 256;
        
        for (int k : codes) {
            String entry;
            if (dictionary.containsKey(k)) {
                entry = dictionary.get(k);
            } else if (k == dictSize) {
                entry = w + w.charAt(0);
            } else {
                throw new IllegalArgumentException("Invalid compressed data");
            }
            
            decompressed.append(entry);
            dictionary.put(dictSize++, w + entry.charAt(0));
            w = entry;
        }
        
        return decompressed.toString();
    }
    
    @Override
    public String getAlgorithmName() {
        return "LZW (Lempel-Ziv-Welch)";
    }
    
    @Override
    public double getCompressionRatio() {
        return lastCompressionRatio;
    }
}