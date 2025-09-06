package com.example.builder.classic;

public class SportCarBuilder extends Builder {
    
    @Override
    public void buildEngine() {
        product.setEngine("V6 Turbo");
    }
    
    @Override
    public void buildTransmission() {
        product.setTransmission("6-Speed Manual");
    }
    
    @Override
    public void buildInterior() {
        product.setInterior("Sport Seats");
    }
    
    @Override
    public void buildColor() {
        product.setColor("Racing Red");
    }
    
    @Override
    public void buildWheels() {
        product.setWheels("19-inch Performance");
    }
    
    @Override
    public void buildGPS() {
        product.setHasGPS(true);
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