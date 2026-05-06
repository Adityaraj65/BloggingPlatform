package com.inkwell.newsletter.controller;

import com.inkwell.newsletter.dto.SubscriberDTO;
import com.inkwell.newsletter.dto.SubscriptionRequest;
import com.inkwell.newsletter.service.NewsletterService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/newsletter")
public class NewsletterResource {

    private final NewsletterService service;

    public NewsletterResource(NewsletterService service) {
        this.service = service;
    }

    // PUBLIC
    @PostMapping("/subscribe")
    public ResponseEntity<SubscriberDTO> subscribe(@Valid @RequestBody SubscriptionRequest request) {
        return ResponseEntity.ok(service.subscribe(request));
    }

    // PUBLIC
    @GetMapping("/confirm")
    public ResponseEntity<String> confirm(@RequestParam String token) {
        return ResponseEntity.ok(service.confirmSubscription(token));
    }

    // ADMIN ONLY
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/send")
    public ResponseEntity<String> sendNewsletter(@RequestBody Map<String, Object> payload) {

        service.sendNewsletter(
                payload.get("subject").toString(),
                payload.get("content").toString(),
                null
        );

        return ResponseEntity.ok("Newsletter sent");
    }

    // USER
    @PutMapping("/preferences/{id}")
    public ResponseEntity<Void> updatePrefs(@PathVariable Long id,
                                            @RequestParam String prefs) {
        service.updatePreferences(id, prefs);
        return ResponseEntity.ok().build();
    }

    // ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/count")
    public ResponseEntity<Long> getCount() {
        return ResponseEntity.ok(service.getSubscriberCount());
    }
}