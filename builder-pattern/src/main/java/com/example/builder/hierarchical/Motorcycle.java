package com.example.builder.hierarchical;

public class Motorcycle extends Vehicle {
    private final String bikeType;
    private final int engineCC;
    private final boolean hasWindscreen;
    private final boolean hasSidecar;
    
    private Motorcycle(MotorcycleBuilder builder) {
        super(builder);
        this.bikeType = builder.bikeType;
        this.engineCC = builder.engineCC;
        this.hasWindscreen = builder.hasWindscreen;
        this.hasSidecar = builder.hasSidecar;
    }
    
    public String getBikeType() { return bikeType; }
    public int getEngineCC() { return engineCC; }
    public boolean hasWindscreen() { return hasWindscreen; }
    public boolean hasSidecar() { return hasSidecar; }
    
    @Override
    public String toString() {
        return String.format("Motorcycle{make='%s', model='%s', year=%d, color='%s', " +
                           "type='%s', engineCC=%d, windscreen=%b, sidecar=%b}",
                           make, model, year, color, bikeType, engineCC, hasWindscreen, hasSidecar);
    }
    
    public static MotorcycleBuilder builder() {
        return new MotorcycleBuilder();
    }
    
    public static class MotorcycleBuilder extends VehicleBuilder<Motorcycle, MotorcycleBuilder> {
        private String bikeType = "Sport";
        private int engineCC = 600;
        private boolean hasWindscreen = false;
        private boolean hasSidecar = false;
        
        public MotorcycleBuilder() {
            super.wheels = 2;
        }
        
        @Override
        protected MotorcycleBuilder self() {
            return this;
        }
        
        public MotorcycleBuilder bikeType(String bikeType) {
            this.bikeType = bikeType;
            return this;
        }
        
        public MotorcycleBuilder engineCC(int engineCC) {
            if (engineCC < 50 || engineCC > 2000) {
                throw new IllegalArgumentException("Engine CC must be between 50 and 2000");
            }
            this.engineCC = engineCC;
            return this;
        }
        
        public MotorcycleBuilder withWindscreen() {
            this.hasWindscreen = true;
            return this;
        }
        
        public MotorcycleBuilder withSidecar() {
            this.hasSidecar = true;
            return this;
        }
        
        @Override
        public MotorcycleBuilder wheels(int wheels) {
            if (wheels != 2 && wheels != 3) {
                throw new IllegalArgumentException("Motorcycles can only have 2 or 3 wheels");
            }
            return super.wheels(wheels);
        }
        
        @Override
        public Motorcycle build() {
            validateBase();
            if (hasSidecar && wheels == 2) {
                wheels = 3;
            }
            return new Motorcycle(this);
        }
    }
}