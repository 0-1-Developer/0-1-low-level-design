package com.example.builder.classic;

public abstract class Builder {
    protected Product product;
    
    public void createProduct() {
        product = new Product();
    }
    
    public Product getProduct() {
        return product;
    }
    
    public abstract void buildEngine();
    public abstract void buildTransmission();
    public abstract void buildInterior();
    public abstract void buildColor();
    public abstract void buildWheels();
    public abstract void buildGPS();
    public abstract void buildSunroof();
    public abstract void buildAirConditioning();
}