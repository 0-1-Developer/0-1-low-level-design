package com.example.builder.directorless;

import java.util.ArrayList;
import java.util.List;

public class Sandwich {
    private final String bread;
    private final String protein;
    private final List<String> vegetables;
    private final List<String> condiments;
    private final String cheese;
    private final boolean toasted;
    private final String size;
    
    private Sandwich(SandwichBuilder builder) {
        this.bread = builder.bread;
        this.protein = builder.protein;
        this.vegetables = new ArrayList<>(builder.vegetables);
        this.condiments = new ArrayList<>(builder.condiments);
        this.cheese = builder.cheese;
        this.toasted = builder.toasted;
        this.size = builder.size;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(size).append(" sandwich on ").append(bread).append(" bread\n");
        if (protein != null) sb.append("  Protein: ").append(protein).append("\n");
        if (cheese != null) sb.append("  Cheese: ").append(cheese).append("\n");
        if (!vegetables.isEmpty()) sb.append("  Vegetables: ").append(String.join(", ", vegetables)).append("\n");
        if (!condiments.isEmpty()) sb.append("  Condiments: ").append(String.join(", ", condiments)).append("\n");
        if (toasted) sb.append("  Toasted: Yes\n");
        return sb.toString().trim();
    }
    
    public static class SandwichBuilder {
        private String bread = "White";
        private String protein;
        private List<String> vegetables = new ArrayList<>();
        private List<String> condiments = new ArrayList<>();
        private String cheese;
        private boolean toasted = false;
        private String size = "Regular";
        
        public SandwichBuilder bread(String bread) {
            this.bread = bread;
            return this;
        }
        
        public SandwichBuilder protein(String protein) {
            this.protein = protein;
            return this;
        }
        
        public SandwichBuilder addVegetable(String vegetable) {
            this.vegetables.add(vegetable);
            return this;
        }
        
        public SandwichBuilder addCondiment(String condiment) {
            this.condiments.add(condiment);
            return this;
        }
        
        public SandwichBuilder cheese(String cheese) {
            this.cheese = cheese;
            return this;
        }
        
        public SandwichBuilder toasted() {
            this.toasted = true;
            return this;
        }
        
        public SandwichBuilder size(String size) {
            this.size = size;
            return this;
        }
        
        public Sandwich build() {
            return new Sandwich(this);
        }
    }
}