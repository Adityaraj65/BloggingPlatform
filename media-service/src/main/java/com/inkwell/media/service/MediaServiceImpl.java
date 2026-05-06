package com.inkwell.media.service;
import org.springframework.transaction.annotation.Transactional;

import com.inkwell.media.client.PostClient;
import com.inkwell.media.entity.Media;
import com.inkwell.media.repository.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
public class MediaServiceImpl implements MediaService {

    @Autowired
    private MediaRepository repository;
    @Autowired
    private PostClient postClient;
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${server.port}")
    private String port;

    @Override
    public Media uploadMedia(MultipartFile file, Long uploaderId) {

        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        try {
            String originalName = file.getOriginalFilename();
            String uniqueName = UUID.randomUUID() + "_" + originalName;

            Path path = Paths.get(uploadDir).resolve(uniqueName);
            Files.createDirectories(path.getParent());
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            Media media = new Media();
            media.setUploaderId(uploaderId);
            media.setFilename(uniqueName);
            media.setOriginalName(originalName);
            media.setMimeType(file.getContentType());
            media.setSizeKb(file.getSize() / 1024);

            // dynamic URL (gateway safe)
            media.setUrl("/media/files/" + uniqueName);

            return repository.save(media);

        } catch (IOException e) {
            throw new RuntimeException("File upload failed");
        }
    }

    @Override
    public Optional<Media> getMediaById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Media> getMediaByUploader(Long id) {
        return repository.findByUploaderId(id);
    }

    @Override
    public List<Media> getMediaByPost(Long id) {
        return repository.findByLinkedPostId(id);
    }

    @Override
    public void deleteMedia(Long id) {
        Media m = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Media not found"));

        m.setDeleted(true);
        repository.save(m);
    }

    @Override
    public Media updateAltText(Long id, String text) {
        Media m = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Media not found"));

        m.setAltText(text);
        return repository.save(m);
    }

    @Override
    public void linkToPost(Long mediaId, Long postId) {

        // 🔥 Validate post exists
        try {
            postClient.getPostById(postId);
        } catch (Exception e) {
            throw new RuntimeException("Post not found");
        }

        Media media = repository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found"));

        media.setLinkedPostId(postId);
        repository.save(media);
    }

    @Override
    public void unlinkFromPost(Long id) {
        Media m = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Media not found"));

        m.setLinkedPostId(null);
        repository.save(m);
    }

    @Override
    public List<Media> getAllMedia() {
        return repository.findAll()
                .stream()
                .filter(m -> !m.isDeleted())
                .toList();
    }

    @Override
    @Transactional
    public void cleanupDeleted() {
        List<Media> deleted = repository.findByIsDeleted(true);
        repository.deleteAll(deleted);
    }
}