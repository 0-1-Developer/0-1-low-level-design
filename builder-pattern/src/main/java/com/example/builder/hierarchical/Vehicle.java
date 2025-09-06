package com.example.builder.hierarchical;

public abstract class Vehicle {
    protected final String make;
    protected final String model;
    protected final int year;
    protected final String color;
    protected final String engine;
    protected final int wheels;
    
    protected Vehicle(VehicleBuilder<?, ?> builder) {
        this.make = builder.make;
        this.model = builder.model;
        this.year = builder.year;
        this.color = builder.color;
        this.engine = builder.engine;
        this.wheels = builder.wheels;
    }
    
    public String getMake() { return make; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public String getColor() { return color; }
    public String getEngine() { return engine; }
    public int getWheels() { return wheels; }
    
    @Override
    public String toString() {
        return String.format("%s{make='%s', model='%s', year=%d, color='%s', engine='%s', wheels=%d}",
                           getClass().getSimpleName(), make, model, year, color, engine, wheels);
    }
    
    public abstract static class VehicleBuilder<T extends Vehicle, B extends VehicleBuilder<T, B>> {
        protected String make;
        protected String model;
        protected int year;
        protected String color = "White";
        protected String engine = "Standard";
        protected int wheels = 4;
        
        protected abstract B self();
        
        public B make(String make) {
            this.make = make;
            return self();
        }
        
        public B model(String model) {
            this.model = model;
            return self();
        }
        
        public B year(int year) {
            this.year = year;
            return self();
        }
        
        public B color(String color) {
            this.color = color;
            return self();
        }
        
        public B engine(String engine) {
            this.engine = engine;
            return self();
        }
        
        public B wheels(int wheels) {
            this.wheels = wheels;
            return self();
        }
        
        public abstract T build();
        
        protected void validateBase() {
            if (make == null || make.trim().isEmpty()) {
                throw new IllegalStateException("Make is required");
            }
            if (model == null || model.trim().isEmpty()) {
                throw new IllegalStateException("Model is required");
            }
            if (year < 1900 || year > 2030) {
                throw new IllegalStateException("Year must be between 1900 and 2030");
            }
        }
    }
}