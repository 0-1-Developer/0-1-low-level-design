package com.example.registry.services;

/**
 * Push notification service implementation for sending mobile push notifications.
 */
public class PushNotificationService implements Service {
    
    @Override
    public String getName() {
        return "PushNotificationService";
    }

    @Override
    public void execute() {
        System.out.println("Executing Push Notification Service: Sending a push notification...");
    }
    
    /**
     * Push notification-specific method that demonstrates type-specific functionality.
     * @param deviceId the target device identifier
     * @param title the notification title
     * @param body the notification body
     */
    public void sendPushNotification(String deviceId, String title, String body) {
        System.out.println("Sending push notification to device: " + deviceId + 
                         " with title: '" + title + "' and body: '" + body + "'");
    }
}