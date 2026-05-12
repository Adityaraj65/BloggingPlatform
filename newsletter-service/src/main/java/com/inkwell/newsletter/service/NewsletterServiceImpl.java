package com.inkwell.newsletter.service;

import com.inkwell.newsletter.client.NotificationClient;
import com.inkwell.newsletter.dto.SubscriberDTO;
import com.inkwell.newsletter.dto.SubscriptionRequest;
import com.inkwell.newsletter.entity.Subscriber;
import com.inkwell.newsletter.repository.SubscriberRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class NewsletterServiceImpl implements NewsletterService {

    private final SubscriberRepository repository;
    private final NotificationClient notificationClient;

    public NewsletterServiceImpl(SubscriberRepository repository,
                                 NotificationClient notificationClient) {
        this.repository = repository;
        this.notificationClient = notificationClient;
    }

    // 1. SUBSCRIBE
    @Override
    public SubscriberDTO subscribe(SubscriptionRequest request) {

        if (repository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already subscribed");
        }

        Subscriber s = new Subscriber();
        s.setEmail(request.getEmail());
        s.setFullName(request.getFullName());
        s.setPreferences(request.getPreferences());
        s.setStatus("PENDING");
        s.setToken(UUID.randomUUID().toString());

        Subscriber saved = repository.save(s);

        // send confirmation email
        try {

            notificationClient.sendEmail(
                    s.getEmail(),
                    "Confirm Subscription",
                    "Click to confirm: http://localhost:8080/newsletter/confirm?token=" + s.getToken()
            );

        } catch (Exception e) {

            e.printStackTrace();
        }

        return mapToDTO(saved);
    }

    // 2. CONFIRM
    @Override
    public String confirmSubscription(String token) {

        Subscriber s = repository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        s.setStatus("ACTIVE");
        repository.save(s);

        notificationClient.sendEmail(
                s.getEmail(),
                "Welcome",
                "You are now subscribed!"
        );

        return "Subscription confirmed!";
    }

    // 3. UNSUBSCRIBE
    @Override
    public void unsubscribe(String email) {

        Subscriber s = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email not found"));

        s.setStatus("UNSUBSCRIBED");
        s.setUnsubscribedAt(LocalDateTime.now());

        repository.save(s);
    }

    // 4. SEND NEWSLETTER (ADMIN)
    @Override
    public void sendNewsletter(String subject, String content, List<Integer> preferenceIds) {

        List<Subscriber> active = repository.findByStatus("ACTIVE");

        active.forEach(s ->
                notificationClient.sendEmail(s.getEmail(), subject, content)
        );
    }

    // 5. POST PUBLISH TRIGGER
    @Override
    public void sendPostNotification(Long postId) {

        List<Subscriber> active = repository.findByStatus("ACTIVE");

        active.forEach(s ->
                notificationClient.sendEmail(
                        s.getEmail(),
                        "New Post Published",
                        "Check new post: http://localhost:8080/posts/" + postId
                )
        );
    }

    // 6. UPDATE PREFS
    @Override
    public void updatePreferences(Long id, String prefs) {

        Subscriber s = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscriber not found"));

        s.setPreferences(prefs);
        repository.save(s);
    }

    // 7. COUNT
    @Override
    public long getSubscriberCount() {
        return repository.count();
    }

    // 🔥 Mapper
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