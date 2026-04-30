package com.inkwell.newsletter.service;

import com.inkwell.newsletter.dto.SubscriberDTO;
import com.inkwell.newsletter.dto.SubscriptionRequest;
import java.util.List;
import java.util.Map;

public interface NewsletterService {
    SubscriberDTO subscribe(SubscriptionRequest request);
    String confirmSubscription(String token);
    void unsubscribe(String email);
    
    // Missing Methods as per Figure 7
    void sendNewsletter(String subject, String content, List<Integer> preferenceIds);
    void sendPostNotification(Long postId); 
    void updatePreferences(Long subscriberId, String preferences);
    long getSubscriberCount();
}