package com.example.builder.telescoping;

public class Pizza {
    private final String base;
    private final String sauce;
    private final String cheese;
    private final String[] toppings;
    private final boolean extraCheese;
    private final boolean thickCrust;
    private final String size;
    private final boolean glutenFree;
    
    public Pizza(String base, String sauce, String cheese) {
        this(base, sauce, cheese, new String[0]);
    }
    
    public Pizza(String base, String sauce, String cheese, String[] toppings) {
        this(base, sauce, cheese, toppings, false);
    }
    
    public Pizza(String base, String sauce, String cheese, String[] toppings, boolean extraCheese) {
        this(base, sauce, cheese, toppings, extraCheese, false);
    }
    
    public Pizza(String base, String sauce, String cheese, String[] toppings, 
                 boolean extraCheese, boolean thickCrust) {
        this(base, sauce, cheese, toppings, extraCheese, thickCrust, "Medium");
    }
    
    public Pizza(String base, String sauce, String cheese, String[] toppings, 
                 boolean extraCheese, boolean thickCrust, String size) {
        this(base, sauce, cheese, toppings, extraCheese, thickCrust, size, false);
    }
    
    public Pizza(String base, String sauce, String cheese, String[] toppings, 
                 boolean extraCheese, boolean thickCrust, String size, boolean glutenFree) {
        this.base = base;
        this.sauce = sauce;
        this.cheese = cheese;
        this.toppings = toppings != null ? toppings.clone() : new String[0];
        this.extraCheese = extraCheese;
        this.thickCrust = thickCrust;
        this.size = size;
        this.glutenFree = glutenFree;
    }
    
    public String getBase() { return base; }
    public String getSauce() { return sauce; }
    public String getCheese() { return cheese; }
    public String[] getToppings() { return toppings.clone(); }
    public boolean hasExtraCheese() { return extraCheese; }
    public boolean hasThickCrust() { return thickCrust; }
    public String getSize() { return size; }
    public boolean isGlutenFree() { return glutenFree; }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(size).append(" ").append(base).append(" pizza with:\n");
        sb.append("  - ").append(sauce).append(" sauce\n");
        sb.append("  - ").append(cheese).append(" cheese");
        if (extraCheese) sb.append(" (extra)");
        sb.append("\n");
        
        if (toppings.length > 0) {
            sb.append("  - Toppings: ");
            for (int i = 0; i < toppings.length; i++) {
                sb.append(toppings[i]);
                if (i < toppings.length - 1) sb.append(", ");
            }
            sb.append("\n");
        }
        
        if (thickCrust) sb.append("  - Thick crust\n");
        if (glutenFree) sb.append("  - Gluten-free base\n");
        
        return sb.toString().trim();
    }
}