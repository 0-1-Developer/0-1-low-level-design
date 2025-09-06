package com.example.builder.hierarchical;

public class Truck extends Vehicle {
    private final double payloadCapacity;
    private final String bedType;
    private final boolean hasTrailerHitch;
    private final boolean isFourWheelDrive;
    
    private Truck(TruckBuilder builder) {
        super(builder);
        this.payloadCapacity = builder.payloadCapacity;
        this.bedType = builder.bedType;
        this.hasTrailerHitch = builder.hasTrailerHitch;
        this.isFourWheelDrive = builder.isFourWheelDrive;
    }
    
    public double getPayloadCapacity() { return payloadCapacity; }
    public String getBedType() { return bedType; }
    public boolean hasTrailerHitch() { return hasTrailerHitch; }
    public boolean isFourWheelDrive() { return isFourWheelDrive; }
    
    @Override
    public String toString() {
        return String.format("Truck{make='%s', model='%s', year=%d, color='%s', engine='%s', " +
                           "payload=%.1f tons, bedType='%s', trailerHitch=%b, 4WD=%b}",
                           make, model, year, color, engine, payloadCapacity, bedType,
                           hasTrailerHitch, isFourWheelDrive);
    }
    
    public static TruckBuilder builder() {
        return new TruckBuilder();
    }
    
    public static class TruckBuilder extends VehicleBuilder<Truck, TruckBuilder> {
        private double payloadCapacity = 1.0;
        private String bedType = "Standard";
        private boolean hasTrailerHitch = false;
        private boolean isFourWheelDrive = false;
        
        public TruckBuilder() {
            super.engine = "V6 Diesel";
        }
        
        @Override
        protected TruckBuilder self() {
            return this;
        }
        
        public TruckBuilder payloadCapacity(double tons) {
            if (tons < 0.5 || tons > 10.0) {
                throw new IllegalArgumentException("Payload capacity must be between 0.5 and 10.0 tons");
            }
            this.payloadCapacity = tons;
            return this;
        }
        
        public TruckBuilder bedType(String bedType) {
            this.bedType = bedType;
            return this;
        }
        
        public TruckBuilder withTrailerHitch() {
            this.hasTrailerHitch = true;
            return this;
        }
        
        public TruckBuilder withFourWheelDrive() {
            this.isFourWheelDrive = true;
            return this;
        }
        
        @Override
        public Truck build() {
            validateBase();
            return new Truck(this);
        }
    }
}