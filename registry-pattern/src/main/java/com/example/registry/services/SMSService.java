package com.example.registry.services;

/**
 * SMS service implementation for sending text message notifications.
 */
public class SMSService implements Service {
    
    @Override
    public String getName() {
        return "SMSService";
    }

    @Override
    public void execute() {
        System.out.println("Executing SMS Service: Sending a text message notification...");
    }
    
    /**
     * SMS-specific method that demonstrates type-specific functionality.
     * @param phoneNumber the recipient's phone number
     * @param message the SMS message content
     */
    public void sendSMS(String phoneNumber, String message) {
        System.out.println("Sending SMS to: " + phoneNumber + " with message: " + message);
    }
}