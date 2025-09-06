package com.example.builder.classic;

public class Director {
    private Builder builder;
    
    public void setBuilder(Builder builder) {
        this.builder = builder;
    }
    
    public Product construct() {
        builder.createProduct();
        builder.buildEngine();
        builder.buildTransmission();
        builder.buildInterior();
        builder.buildColor();
        builder.buildWheels();
        builder.buildGPS();
        builder.buildSunroof();
        builder.buildAirConditioning();
        return builder.getProduct();
    }
    
    public Product constructMinimal() {
        builder.createProduct();
        builder.buildEngine();
        builder.buildTransmission();
        builder.buildColor();
        return builder.getProduct();
    }
}