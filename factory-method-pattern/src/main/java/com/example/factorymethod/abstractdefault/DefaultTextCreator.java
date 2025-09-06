package com.example.factorymethod.abstractdefault;

public class DefaultTextCreator extends AbstractDocumentCreator {
    
    @Override
    public String getCreatorType() {
        return "Default Text Creator (uses inherited factory method)";
    }
}