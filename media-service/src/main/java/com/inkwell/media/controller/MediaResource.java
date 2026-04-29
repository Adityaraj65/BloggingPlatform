package com.inkwell.media.controller;

import com.inkwell.media.dto.MediaDTO;
import com.inkwell.media.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/media")
public class MediaResource {

    @Autowired
    private MediaService mediaService;

    @PostMapping("/upload")
    public ResponseEntity<MediaDTO> upload(@RequestParam("file") MultipartFile file, 
                                           @RequestParam("uploaderId") Long uploaderId) {
        return ResponseEntity.ok(mediaService.uploadMedia(file, uploaderId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MediaDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(mediaService.getMediaById(id));
    }

    @PostMapping("/link")
    public ResponseEntity<Void> link(@RequestParam Long mediaId, @RequestParam Long postId) {
        mediaService.linkToPost(mediaId, postId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<MediaDTO>> getByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(mediaService.getMediaByPost(postId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        mediaService.deleteMedia(id);
        return ResponseEntity.ok().build();
    }
}