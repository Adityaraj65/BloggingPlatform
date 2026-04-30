package com.inkwell.newsletter.controller;

import com.inkwell.newsletter.service.NewsletterService;
import com.inkwell.newsletter.dto.SubscriberDTO;
import com.inkwell.newsletter.dto.SubscriptionRequest;
import com.inkwell.newsletter.entity.Subscriber;
import com.inkwell.newsletter.service.NewsletterServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RestController
@RequestMapping("/newsletter")
public class NewsletterResource {

    @Autowired
    private NewsletterService service;

    // 1. Subscribe
    @PostMapping("/subscribe")
    public ResponseEntity<SubscriberDTO> subscribe(@RequestBody SubscriptionRequest request) {
        return ResponseEntity.ok(service.subscribe(request));
    }

    // 2. Confirm (Double Opt-in)
    @GetMapping("/confirm")
    public ResponseEntity<String> confirm(@RequestParam String token) {
        return ResponseEntity.ok(service.confirmSubscription(token));
    }

    // 3. Send Newsletter (Admin Only Logic)
    @PostMapping("/send")
    public ResponseEntity<String> sendNewsletter(@RequestBody Map<String, Object> payload) {
        // Payload will have: subject, content, targetPreferences
        service.sendNewsletter(payload.get("subject").toString(), 
                               payload.get("content").toString(), null);
        return ResponseEntity.ok("Newsletter campaign started!");
    }

    // 4. Update Preferences
    @PutMapping("/preferences/{id}")
    public ResponseEntity<Void> updatePrefs(@PathVariable Long id, @RequestParam String prefs) {
        service.updatePreferences(id, prefs);
        return ResponseEntity.ok().build();
    }

    // 5. Get Count (For Admin Panel)
    @GetMapping("/count")
    public ResponseEntity<Long> getCount() {
        return ResponseEntity.ok(service.getSubscriberCount());
    }
}