package com.example.builder.immutable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public final class User {
    private final long id;
    private final String username;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final LocalDateTime dateOfBirth;
    private final String phoneNumber;
    private final Address address;
    private final List<String> interests;
    private final boolean isActive;
    private final LocalDateTime createdAt;
    private final LocalDateTime lastLoginAt;
    
    private User(UserBuilder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.email = builder.email;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.dateOfBirth = builder.dateOfBirth;
        this.phoneNumber = builder.phoneNumber;
        this.address = builder.address != null ? new Address(builder.address) : null;
        this.interests = Collections.unmodifiableList(new ArrayList<>(builder.interests));
        this.isActive = builder.isActive;
        this.createdAt = builder.createdAt;
        this.lastLoginAt = builder.lastLoginAt;
    }
    
    public long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public LocalDateTime getDateOfBirth() { return dateOfBirth; }
    public String getPhoneNumber() { return phoneNumber; }
    public Address getAddress() { return address != null ? new Address(address) : null; }
    public List<String> getInterests() { return interests; }
    public boolean isActive() { return isActive; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getLastLoginAt() { return lastLoginAt; }
    
    public String getFullName() {
        return (firstName != null ? firstName : "") + 
               (lastName != null ? " " + lastName : "").trim();
    }
    
    public int getAge() {
        if (dateOfBirth == null) return -1;
        return LocalDateTime.now().getYear() - dateOfBirth.getYear();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User{id=").append(id);
        sb.append(", username='").append(username).append("'");
        sb.append(", email='").append(email).append("'");
        sb.append(", fullName='").append(getFullName()).append("'");
        if (dateOfBirth != null) {
            sb.append(", age=").append(getAge());
        }
        if (phoneNumber != null) {
            sb.append(", phone='").append(phoneNumber).append("'");
        }
        if (address != null) {
            sb.append(", address=").append(address);
        }
        sb.append(", interests=").append(interests);
        sb.append(", active=").append(isActive);
        sb.append(", created=").append(createdAt);
        if (lastLoginAt != null) {
            sb.append(", lastLogin=").append(lastLoginAt);
        }
        sb.append("}");
        return sb.toString();
    }
    
    public static class UserBuilder {
        private long id;
        private String username;
        private String email;
        private String firstName;
        private String lastName;
        private LocalDateTime dateOfBirth;
        private String phoneNumber;
        private Address address;
        private List<String> interests = new ArrayList<>();
        private boolean isActive = true;
        private LocalDateTime createdAt = LocalDateTime.now();
        private LocalDateTime lastLoginAt;
        
        public UserBuilder id(long id) {
            this.id = id;
            return this;
        }
        
        public UserBuilder username(String username) {
            this.username = username;
            return this;
        }
        
        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }
        
        public UserBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }
        
        public UserBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }
        
        public UserBuilder dateOfBirth(LocalDateTime dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }
        
        public UserBuilder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }
        
        public UserBuilder address(Address address) {
            this.address = address;
            return this;
        }
        
        public UserBuilder addInterest(String interest) {
            this.interests.add(interest);
            return this;
        }
        
        public UserBuilder interests(List<String> interests) {
            this.interests = new ArrayList<>(interests);
            return this;
        }
        
        public UserBuilder active(boolean isActive) {
            this.isActive = isActive;
            return this;
        }
        
        public UserBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        
        public UserBuilder lastLoginAt(LocalDateTime lastLoginAt) {
            this.lastLoginAt = lastLoginAt;
            return this;
        }
        
        public User build() {
            if (username == null || username.trim().isEmpty()) {
                throw new IllegalStateException("Username is required");
            }
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalStateException("Email is required");
            }
            if (id <= 0) {
                throw new IllegalStateException("ID must be positive");
            }
            
            return new User(this);
        }
    }
}