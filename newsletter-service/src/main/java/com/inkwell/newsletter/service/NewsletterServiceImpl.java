package com.inkwell.newsletter.service;

import com.inkwell.newsletter.service.NewsletterService;
import com.inkwell.newsletter.dto.SubscriberDTO;
import com.inkwell.newsletter.dto.SubscriptionRequest;
import com.inkwell.newsletter.entity.Subscriber;
import com.inkwell.newsletter.repository.SubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NewsletterServiceImpl implements NewsletterService {

    @Autowired
    private SubscriberRepository repository;

    // 1. Subscribe: Creates a PENDING record and generates a unique token
    @Override
    public SubscriberDTO subscribe(SubscriptionRequest request) {
        Subscriber s = new Subscriber();
        s.setEmail(request.getEmail());
        s.setFullName(request.getFullName());
        s.setPreferences(request.getPreferences());
        s.setStatus("PENDING"); // Default status as per Figure 7
        s.setToken(UUID.randomUUID().toString()); // Inbuilt UUID for security
        
        Subscriber saved = repository.save(s);
        
        // TODO: Call Notification-Service to send "Confirmation Email" with this token
        System.out.println("Confirmation email triggered for: " + s.getEmail());
        
        return mapToDTO(saved);
    }

    // 2. Confirm Subscription: Changes status from PENDING to ACTIVE
    @Override
    public String confirmSubscription(String token) {
        Subscriber s = repository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or Expired Token"));
        
        s.setStatus("ACTIVE"); // Double Opt-in completion
        repository.save(s);
        
        // TODO: Call Notification-Service to send "Welcome Email"
        System.out.println("Welcome email triggered for: " + s.getEmail());
        
        return "Subscription confirmed! You are now an ACTIVE subscriber.";
    }

    // 3. Unsubscribe: Simple logic to mark user as UNSUBSCRIBED
    @Override
    public void unsubscribe(String email) {
        Subscriber s = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email not found"));
        
        s.setStatus("UNSUBSCRIBED");
        s.setUnsubscribedAt(LocalDateTime.now());
        repository.save(s);
    }

    // 4. Send Newsletter: Logic to fetch all active users and notify them
    @Override
    public void sendNewsletter(String subject, String content, List<Integer> preferenceIds) {
        // Logic: Fetch all subscribers who are ACTIVE
        List<Subscriber> activeSubscribers = repository.findAll().stream()
                .filter(s -> "ACTIVE".equals(s.getStatus()))
                .collect(Collectors.toList());

        // For each subscriber, trigger the Notification-Service
        activeSubscribers.forEach(s -> {
            System.out.println("Sending Newsletter to: " + s.getEmail());
            // Feign call to Notification-Service would go here
        });
    }

    // 5. Send Post Notification: Triggered when a new blog is published
    @Override
    public void sendPostNotification(Long postId) {
        System.out.println("New Post Alert! Post ID: " + postId);
        // Similar logic: fetch ACTIVE users and send notification
    }

    // 6. Update Preferences: Change user interest tags
    @Override
    public void updatePreferences(Long subscriberId, String preferences) {
        Subscriber s = repository.findById(subscriberId)
                .orElseThrow(() -> new RuntimeException("Subscriber not found"));
        s.setPreferences(preferences);
        repository.save(s);
    }

    // 7. Get Subscriber Count: For Admin dashboard
    @Override
    public long getSubscriberCount() {
        return repository.count();
    }

    // 🔥 Inbuilt-Style Helper Method: Manual Mapping (No extra library needed)
    private SubscriberDTO mapToDTO(Subscriber s) {
        SubscriberDTO dto = new SubscriberDTO();
        dto.setSubscriberId(s.getSubscriberId());
        dto.setEmail(s.getEmail());
        dto.setFullName(s.getFullName());
        dto.setStatus(s.getStatus());
        dto.setSubscribedAt(s.getSubscribedAt());
        dto.setPreferences(s.getPreferences());
        return dto;
    }
}