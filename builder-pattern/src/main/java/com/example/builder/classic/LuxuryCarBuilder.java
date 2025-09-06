package com.example.builder.classic;

public class LuxuryCarBuilder extends Builder {
    
    @Override
    public void buildEngine() {
        product.setEngine("V8 Twin-Turbo");
    }
    
    @Override
    public void buildTransmission() {
        product.setTransmission("8-Speed Automatic");
    }
    
    @Override
    public void buildInterior() {
        product.setInterior("Premium Leather");
    }
    
    @Override
    public void buildColor() {
        product.setColor("Pearl White");
    }
    
    @Override
    public void buildWheels() {
        product.setWheels("21-inch Alloy");
    }
    
    @Override
    public void buildGPS() {
        product.setHasGPS(true);
    }
    
    @Override
    public void buildSunroof() {
        product.setHasSunroof(true);
    }
    
    @Override
    public void buildAirConditioning() {
        product.setHasAirConditioning(true);
    }
}