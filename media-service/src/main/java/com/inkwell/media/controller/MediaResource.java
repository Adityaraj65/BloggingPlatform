package com.inkwell.media.controller;

import com.inkwell.media.entity.Media;
import com.inkwell.media.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/media")
public class MediaResource {

    @Autowired private MediaService service;

    @PostMapping("/upload")
    public ResponseEntity<Media> upload(@RequestParam("file") MultipartFile file, @RequestParam("uploaderId") Long uploaderId) {
        return ResponseEntity.ok(service.uploadMedia(file, uploaderId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Media> getById(@PathVariable Long id) {
        return service.getMediaById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/uploader/{uploaderId}")
    public ResponseEntity<List<Media>> getByUploader(@PathVariable Long uploaderId) {
        return ResponseEntity.ok(service.getMediaByUploader(uploaderId));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Media>> getByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(service.getMediaByPost(postId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Media>> getAll() {
        return ResponseEntity.ok(service.getAllMedia());
    }

    @PutMapping("/update-alt")
    public ResponseEntity<Media> updateAlt(@RequestParam Long mediaId, @RequestParam String altText) {
        return ResponseEntity.ok(service.updateAltText(mediaId, altText));
    }

    @PostMapping("/link")
    public ResponseEntity<Void> link(@RequestParam Long mediaId, @RequestParam Long postId) {
        service.linkToPost(mediaId, postId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unlink")
    public ResponseEntity<Void> unlink(@RequestParam Long mediaId) {
        service.unlinkFromPost(mediaId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteMedia(id);
        return ResponseEntity.ok().build();
    }
}