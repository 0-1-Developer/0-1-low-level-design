package com.example.strategy.async;

/**
 * Represents an image processing request with metadata.
 * Used as input for asynchronous image processing strategies.
 */
public class ImageProcessingRequest {
    private final String imagePath;
    private final String format;
    private final int width;
    private final int height;
    private final long fileSize;

    public ImageProcessingRequest(String imagePath, String format, int width, int height, long fileSize) {
        this.imagePath = imagePath;
        this.format = format;
        this.width = width;
        this.height = height;
        this.fileSize = fileSize;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getFormat() {
        return format;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public long getFileSize() {
        return fileSize;
    }

    @Override
    public String toString() {
        return String.format("ImageRequest{path='%s', format='%s', size=%dx%d, fileSize=%d bytes}", 
                           imagePath, format, width, height, fileSize);
    }
}