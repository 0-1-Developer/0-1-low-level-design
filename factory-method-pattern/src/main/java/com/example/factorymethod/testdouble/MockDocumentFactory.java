package com.example.factorymethod.testdouble;

import com.example.factorymethod.shared.Document;

public class MockDocumentFactory implements DocumentFactory {
    
    private int createCallCount = 0;
    private String lastRequestedTitle;
    
    @Override
    public Document createDocument(String title) {
        createCallCount++;
        lastRequestedTitle = title;
        System.out.println("🎭 Mock factory creating test document (call #" + createCallCount + ")...");
        return new MockDocument(title);
    }
    
    @Override
    public String getFactoryType() {
        return "Mock Factory";
    }
    
    public int getCreateCallCount() {
        return createCallCount;
    }
    
    public String getLastRequestedTitle() {
        return lastRequestedTitle;
    }
    
    public void reset() {
        createCallCount = 0;
        lastRequestedTitle = null;
        System.out.println("🔄 Mock factory reset");
    }
    
    static class MockDocument extends Document {
        private boolean opened = false;
        private boolean saved = false;
        private boolean closed = false;
        
        public MockDocument(String title) {
            super(title);
        }
        
        @Override
        public void open() {
            opened = true;
            System.out.println("🎭 Mock document opened: " + title);
        }
        
        @Override
        public void save() {
            saved = true;
            System.out.println("🎭 Mock document saved: " + title);
        }
        
        @Override
        public void close() {
            closed = true;
            System.out.println("🎭 Mock document closed: " + title);
        }
        
        @Override
        public String getDocumentType() {
            return "MOCK";
        }
        
        public boolean wasOpened() { return opened; }
        public boolean wasSaved() { return saved; }
        public boolean wasClosed() { return closed; }
    }
}