package com.example.builder.telescoping;

import java.util.ArrayList;
import java.util.List;

public class PizzaBuilder {
    private String base = "Regular";
    private String sauce = "Tomato";
    private String cheese = "Mozzarella";
    private List<String> toppings = new ArrayList<>();
    private boolean extraCheese = false;
    private boolean thickCrust = false;
    private String size = "Medium";
    private boolean glutenFree = false;
    
    public PizzaBuilder base(String base) {
        this.base = base;
        return this;
    }
    
    public PizzaBuilder sauce(String sauce) {
        this.sauce = sauce;
        return this;
    }
    
    public PizzaBuilder cheese(String cheese) {
        this.cheese = cheese;
        return this;
    }
    
    public PizzaBuilder addTopping(String topping) {
        this.toppings.add(topping);
        return this;
    }
    
    public PizzaBuilder extraCheese() {
        this.extraCheese = true;
        return this;
    }
    
    public PizzaBuilder thickCrust() {
        this.thickCrust = true;
        return this;
    }
    
    public PizzaBuilder size(String size) {
        this.size = size;
        return this;
    }
    
    public PizzaBuilder glutenFree() {
        this.glutenFree = true;
        return this;
    }
    
    public Pizza build() {
        return new Pizza(base, sauce, cheese, toppings.toArray(new String[0]),
                        extraCheese, thickCrust, size, glutenFree);
    }
}