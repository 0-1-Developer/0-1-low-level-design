package com.example.builder.classic;

public class Product {
    private String engine;
    private String transmission;
    private String interior;
    private String color;
    private String wheels;
    private boolean hasGPS;
    private boolean hasSunroof;
    private boolean hasAirConditioning;
    
    public void setEngine(String engine) {
        this.engine = engine;
    }
    
    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }
    
    public void setInterior(String interior) {
        this.interior = interior;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public void setWheels(String wheels) {
        this.wheels = wheels;
    }
    
    public void setHasGPS(boolean hasGPS) {
        this.hasGPS = hasGPS;
    }
    
    public void setHasSunroof(boolean hasSunroof) {
        this.hasSunroof = hasSunroof;
    }
    
    public void setHasAirConditioning(boolean hasAirConditioning) {
        this.hasAirConditioning = hasAirConditioning;
    }
    
    public String getEngine() { return engine; }
    public String getTransmission() { return transmission; }
    public String getInterior() { return interior; }
    public String getColor() { return color; }
    public String getWheels() { return wheels; }
    public boolean hasGPS() { return hasGPS; }
    public boolean hasSunroof() { return hasSunroof; }
    public boolean hasAirConditioning() { return hasAirConditioning; }
    
    @Override
    public String toString() {
        return String.format("Car: %s %s with %s interior, %s wheels%s%s%s", 
            color != null ? color : "Default",
            engine != null ? engine : "Standard Engine",
            interior != null ? interior : "cloth",
            wheels != null ? wheels : "16-inch",
            hasGPS ? ", GPS" : "",
            hasSunroof ? ", Sunroof" : "",
            hasAirConditioning ? ", A/C" : ""
        );
    }
}