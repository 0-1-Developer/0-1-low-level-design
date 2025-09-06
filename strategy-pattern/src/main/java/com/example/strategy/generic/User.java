package com.example.strategy.generic;

/**
 * Example data model for demonstrating generic validation strategies.
 */
public class User {
    private final String username;
    private final String email;
    private final int age;

    public User(String username, String email, int age) {
        this.username = username;
        this.email = email;
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return String.format("User{username='%s', email='%s', age=%d}", username, email, age);
    }
}