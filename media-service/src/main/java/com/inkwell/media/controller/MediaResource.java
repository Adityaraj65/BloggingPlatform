package com.inkwell.media.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.inkwell.media.entity.Media;
import com.inkwell.media.service.MediaService;

@RestController
@RequestMapping("/media")
public class MediaResource {

    private final MediaService service;

    public MediaResource(MediaService service) {
        this.service = service;
    }

    // Only logged-in users
    @PostMapping("/upload")
    public ResponseEntity<Media> upload(@RequestParam MultipartFile file,
                                        @RequestParam Long uploaderId) {
        return ResponseEntity.ok(service.uploadMedia(file, uploaderId));
    }

    // Public
    @GetMapping("/files/{filename}")
    public ResponseEntity<String> file(@PathVariable String filename) {
        return ResponseEntity.ok("Served by static config");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Media> get(@PathVariable Long id) {
        return service.getMediaById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Media not found"));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Media>> all() {
        return ResponseEntity.ok(service.getAllMedia());
    }

    // Only admin can delete
//    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteMedia(id);
        return ResponseEntity.ok().build();
    }
}