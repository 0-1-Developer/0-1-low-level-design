package com.example.strategy.registry;

import java.util.zip.*;
import java.io.*;

public class ZipCompressionStrategy implements CompressionStrategy {
    private double lastCompressionRatio = 0.0;
    
    @Override
    public byte[] compress(String data) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (GZIPOutputStream gzip = new GZIPOutputStream(baos)) {
                gzip.write(data.getBytes("UTF-8"));
            }
            byte[] compressed = baos.toByteArray();
            lastCompressionRatio = (double) compressed.length / data.getBytes("UTF-8").length;
            return compressed;
        } catch (IOException e) {
            System.err.println("ZIP compression failed: " + e.getMessage());
            return data.getBytes();
        }
    }
    
    @Override
    public String decompress(byte[] compressedData) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(compressedData);
            try (GZIPInputStream gzip = new GZIPInputStream(bais)) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = gzip.read(buffer)) > 0) {
                    baos.write(buffer, 0, len);
                }
                return new String(baos.toByteArray(), "UTF-8");
            }
        } catch (IOException e) {
            System.err.println("ZIP decompression failed: " + e.getMessage());
            return new String(compressedData);
        }
    }
    
    @Override
    public String getAlgorithmName() {
        return "GZIP";
    }
    
    @Override
    public double getCompressionRatio() {
        return lastCompressionRatio;
    }
}