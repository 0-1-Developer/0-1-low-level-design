package com.example.builder.immutable;

public final class Address {
    private final String street;
    private final String city;
    private final String state;
    private final String zipCode;
    private final String country;
    
    public Address(String street, String city, String state, String zipCode, String country) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
    }
    
    public Address(Address other) {
        this.street = other.street;
        this.city = other.city;
        this.state = other.state;
        this.zipCode = other.zipCode;
        this.country = other.country;
    }
    
    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getZipCode() { return zipCode; }
    public String getCountry() { return country; }
    
    @Override
    public String toString() {
        return String.format("%s, %s, %s %s, %s", 
                           street != null ? street : "",
                           city != null ? city : "",
                           state != null ? state : "",
                           zipCode != null ? zipCode : "",
                           country != null ? country : "");
    }
}