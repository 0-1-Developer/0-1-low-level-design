package com.example.builder.hierarchical;

public class Car extends Vehicle {
    private final int doors;
    private final String transmission;
    private final boolean hasAirConditioning;
    private final boolean hasSunroof;
    
    private Car(CarBuilder builder) {
        super(builder);
        this.doors = builder.doors;
        this.transmission = builder.transmission;
        this.hasAirConditioning = builder.hasAirConditioning;
        this.hasSunroof = builder.hasSunroof;
    }
    
    public int getDoors() { return doors; }
    public String getTransmission() { return transmission; }
    public boolean hasAirConditioning() { return hasAirConditioning; }
    public boolean hasSunroof() { return hasSunroof; }
    
    @Override
    public String toString() {
        return String.format("Car{make='%s', model='%s', year=%d, color='%s', engine='%s', " +
                           "doors=%d, transmission='%s', airConditioning=%b, sunroof=%b}",
                           make, model, year, color, engine, doors, transmission,
                           hasAirConditioning, hasSunroof);
    }
    
    public static CarBuilder builder() {
        return new CarBuilder();
    }
    
    public static class CarBuilder extends VehicleBuilder<Car, CarBuilder> {
        private int doors = 4;
        private String transmission = "Automatic";
        private boolean hasAirConditioning = true;
        private boolean hasSunroof = false;
        
        @Override
        protected CarBuilder self() {
            return this;
        }
        
        public CarBuilder doors(int doors) {
            if (doors < 2 || doors > 5) {
                throw new IllegalArgumentException("Cars must have 2-5 doors");
            }
            this.doors = doors;
            return this;
        }
        
        public CarBuilder transmission(String transmission) {
            this.transmission = transmission;
            return this;
        }
        
        public CarBuilder withAirConditioning() {
            this.hasAirConditioning = true;
            return this;
        }
        
        public CarBuilder withoutAirConditioning() {
            this.hasAirConditioning = false;
            return this;
        }
        
        public CarBuilder withSunroof() {
            this.hasSunroof = true;
            return this;
        }
        
        @Override
        public Car build() {
            validateBase();
            return new Car(this);
        }
    }
}