package com.example.registry.services;

/**
 * Email service implementation for sending email notifications.
 */
public class EmailService implements Service {
    
    @Override
    public String getName() {
        return "EmailService";
    }

    @Override
    public void execute() {
        System.out.println("Executing Email Service: Sending an email notification...");
    }
    
    /**
     * Email-specific method that demonstrates type-specific functionality.
     * @param recipient the email recipient
     * @param subject the email subject
     */
    public void sendEmail(String recipient, String subject) {
        System.out.println("Sending email to: " + recipient + " with subject: " + subject);
    }
}