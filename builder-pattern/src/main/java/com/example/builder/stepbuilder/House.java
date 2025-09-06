package com.example.builder.stepbuilder;

public final class House {
    private final String foundation;
    private final String structure;
    private final String roof;
    private final String interior;
    private final String exterior;
    private final boolean hasGarage;
    private final boolean hasGarden;
    private final boolean hasPool;
    
    private House(String foundation, String structure, String roof, String interior, 
                 String exterior, boolean hasGarage, boolean hasGarden, boolean hasPool) {
        this.foundation = foundation;
        this.structure = structure;
        this.roof = roof;
        this.interior = interior;
        this.exterior = exterior;
        this.hasGarage = hasGarage;
        this.hasGarden = hasGarden;
        this.hasPool = hasPool;
    }
    
    public String getFoundation() { return foundation; }
    public String getStructure() { return structure; }
    public String getRoof() { return roof; }
    public String getInterior() { return interior; }
    public String getExterior() { return exterior; }
    public boolean hasGarage() { return hasGarage; }
    public boolean hasGarden() { return hasGarden; }
    public boolean hasPool() { return hasPool; }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("House specifications:\n");
        sb.append("  Foundation: ").append(foundation).append("\n");
        sb.append("  Structure: ").append(structure).append("\n");
        sb.append("  Roof: ").append(roof).append("\n");
        sb.append("  Interior: ").append(interior).append("\n");
        sb.append("  Exterior: ").append(exterior).append("\n");
        sb.append("  Garage: ").append(hasGarage ? "Yes" : "No").append("\n");
        sb.append("  Garden: ").append(hasGarden ? "Yes" : "No").append("\n");
        sb.append("  Pool: ").append(hasPool ? "Yes" : "No");
        return sb.toString();
    }
    
    public static FoundationStep builder() {
        return new Builder();
    }
    
    public interface FoundationStep {
        StructureStep foundation(String foundation);
    }
    
    public interface StructureStep {
        RoofStep structure(String structure);
    }
    
    public interface RoofStep {
        OptionalStep roof(String roof);
    }
    
    public interface OptionalStep {
        OptionalStep interior(String interior);
        OptionalStep exterior(String exterior);
        OptionalStep withGarage();
        OptionalStep withGarden();
        OptionalStep withPool();
        House build();
    }
    
    private static class Builder implements FoundationStep, StructureStep, RoofStep, OptionalStep {
        private String foundation;
        private String structure;
        private String roof;
        private String interior = "Standard";
        private String exterior = "Painted";
        private boolean hasGarage = false;
        private boolean hasGarden = false;
        private boolean hasPool = false;
        
        @Override
        public StructureStep foundation(String foundation) {
            this.foundation = foundation;
            return this;
        }
        
        @Override
        public RoofStep structure(String structure) {
            this.structure = structure;
            return this;
        }
        
        @Override
        public OptionalStep roof(String roof) {
            this.roof = roof;
            return this;
        }
        
        @Override
        public OptionalStep interior(String interior) {
            this.interior = interior;
            return this;
        }
        
        @Override
        public OptionalStep exterior(String exterior) {
            this.exterior = exterior;
            return this;
        }
        
        @Override
        public OptionalStep withGarage() {
            this.hasGarage = true;
            return this;
        }
        
        @Override
        public OptionalStep withGarden() {
            this.hasGarden = true;
            return this;
        }
        
        @Override
        public OptionalStep withPool() {
            this.hasPool = true;
            return this;
        }
        
        @Override
        public House build() {
            return new House(foundation, structure, roof, interior, exterior,
                           hasGarage, hasGarden, hasPool);
        }
    }
}