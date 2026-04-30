package com.inkwell.media.service;
import org.springframework.transaction.annotation.Transactional;
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
    @Autowired private MediaRepository repository;
    @Value("${file.upload-dir}") private String uploadDir;

    @Override
    public Media uploadMedia(MultipartFile file, Long uploaderId) {
        try {
            String originalName = file.getOriginalFilename();
            String uniqueName = UUID.randomUUID().toString() + "_" + originalName;
            Path path = Paths.get(uploadDir).resolve(uniqueName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            Media media = new Media();
            media.setUploaderId(uploaderId);
            media.setFilename(uniqueName);
            media.setOriginalName(originalName);
            media.setUrl("http://localhost:8080/media/files/" + uniqueName);
            media.setMimeType(file.getContentType());
            media.setSizeKb(file.getSize() / 1024);
            return repository.save(media);
        } catch (IOException e) { throw new RuntimeException("Upload failed"); }
    }

    @Override 
    public Optional<Media> getMediaById(Long id) { return repository.findByMediaId(id); }
    @Override 
    public List<Media> getMediaByUploader(Long id) { return repository.findByUploaderId(id); }
    @Override 
    public List<Media> getMediaByPost(Long id) { return repository.findByLinkedPostId(id); }
    
    @Override public void deleteMedia(Long id) {
        repository.findById(id).ifPresent(m -> { m.setDeleted(true); repository.save(m); });
    }

    @Override public Media updateAltText(Long id, String text) {
        Media m = repository.findById(id).orElseThrow();
        m.setAltText(text);
        return repository.save(m);
    }

    @Override public void linkToPost(Long mId, Long pId) {
        repository.findById(mId).ifPresent(m -> { m.setLinkedPostId(pId); repository.save(m); });
    }

    @Override public void unlinkFromPost(Long id) {
        repository.findById(id).ifPresent(m -> { m.setLinkedPostId(null); repository.save(m); });
    }

    @Override public List<Media> getAllMedia() { return repository.findAll(); }

    @Override 
    @Transactional
    public void cleanupDeleted() {
        List<Media> deleted = repository.findByIsDeleted(true);
        repository.deleteAll(deleted);
    }
}