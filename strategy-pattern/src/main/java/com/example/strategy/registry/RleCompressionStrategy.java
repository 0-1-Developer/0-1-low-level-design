package com.example.strategy.registry;

public class RleCompressionStrategy implements CompressionStrategy {
    private double lastCompressionRatio = 0.0;
    
    @Override
    public byte[] compress(String data) {
        if (data == null || data.isEmpty()) {
            return new byte[0];
        }
        
        StringBuilder compressed = new StringBuilder();
        char[] chars = data.toCharArray();
        int count = 1;
        
        for (int i = 1; i <= chars.length; i++) {
            if (i < chars.length && chars[i] == chars[i - 1]) {
                count++;
            } else {
                compressed.append(chars[i - 1]);
                if (count > 1) {
                    compressed.append(count);
                }
                count = 1;
            }
        }
        
        byte[] result = compressed.toString().getBytes();
        lastCompressionRatio = (double) result.length / data.getBytes().length;
        return result;
    }
    
    @Override
    public String decompress(byte[] compressedData) {
        String compressed = new String(compressedData);
        StringBuilder decompressed = new StringBuilder();
        
        for (int i = 0; i < compressed.length(); i++) {
            char ch = compressed.charAt(i);
            
            if (i + 1 < compressed.length() && Character.isDigit(compressed.charAt(i + 1))) {
                StringBuilder numStr = new StringBuilder();
                int j = i + 1;
                while (j < compressed.length() && Character.isDigit(compressed.charAt(j))) {
                    numStr.append(compressed.charAt(j));
                    j++;
                }
                int count = Integer.parseInt(numStr.toString());
                for (int k = 0; k < count; k++) {
                    decompressed.append(ch);
                }
                i = j - 1;
            } else {
                decompressed.append(ch);
            }
        }
        
        return decompressed.toString();
    }
    
    @Override
    public String getAlgorithmName() {
        return "RLE (Run-Length Encoding)";
    }
    
    @Override
    public double getCompressionRatio() {
        return lastCompressionRatio;
    }
}