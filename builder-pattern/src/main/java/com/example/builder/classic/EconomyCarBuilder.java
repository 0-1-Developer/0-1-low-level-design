package com.example.builder.classic;

public class EconomyCarBuilder extends Builder {
    
    @Override
    public void buildEngine() {
        product.setEngine("4-Cylinder");
    }
    
    @Override
    public void buildTransmission() {
        product.setTransmission("CVT");
    }
    
    @Override
    public void buildInterior() {
        product.setInterior("Cloth");
    }
    
    @Override
    public void buildColor() {
        product.setColor("Silver");
    }
    
    @Override
    public void buildWheels() {
        product.setWheels("16-inch Steel");
    }
    
    @Override
    public void buildGPS() {
        product.setHasGPS(false);
    }
    
    @Override
    public void buildSunroof() {
        product.setHasSunroof(false);
    }
    
    @Override
    public void buildAirConditioning() {
        product.setHasAirConditioning(true);
    }
}