package com.inkwell.media.service;

import com.inkwell.media.dto.MediaDTO;
import com.inkwell.media.entity.Media;
import com.inkwell.media.repository.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MediaServiceImpl implements MediaService {

    @Autowired
    private MediaRepository mediaRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public MediaDTO uploadMedia(MultipartFile file, Long uploaderId) {
        try {
            Path path = Paths.get(uploadDir);
            if (!Files.exists(path)) Files.createDirectories(path);

            String originalName = file.getOriginalFilename();
            String extension = originalName.substring(originalName.lastIndexOf("."));
            String uniqueName = UUID.randomUUID().toString() + extension;

            Files.copy(file.getInputStream(), path.resolve(uniqueName), StandardCopyOption.REPLACE_EXISTING);

            Media media = new Media();
            media.setUploaderId(uploaderId);
            media.setFilename(uniqueName);
            media.setOriginalName(originalName);
            media.setMimeType(file.getContentType());
            media.setSizeKb(file.getSize() / 1024);
            media.setUrl("http://localhost:8083/media/files/" + uniqueName);
            
            Media savedMedia = mediaRepository.save(media);
            return mapToDTO(savedMedia); // Converting Entity to DTO
        } catch (IOException e) {
            throw new RuntimeException("Could not store file: " + e.getMessage());
        }
    }

    @Override
    public MediaDTO getMediaById(Long mediaId) {
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found"));
        return mapToDTO(media);
    }

    @Override
    public List<MediaDTO> getMediaByPost(Long postId) {
        return mediaRepository.findByLinkedPostId(postId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteMedia(Long mediaId) {
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found"));
        media.setDeleted(true); 
        mediaRepository.save(media);
    }

    @Override
    public void linkToPost(Long mediaId, Long postId) {
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found"));
        media.setLinkedPostId(postId);
        mediaRepository.save(media);
    }

    @Override
    public void unlinkFromPost(Long mediaId) {
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found"));
        media.setLinkedPostId(null);
        mediaRepository.save(media);
    }

    // 🔥 MAPPING HELPER METHOD
    private MediaDTO mapToDTO(Media media) {
        MediaDTO dto = new MediaDTO();
        dto.setMediaId(media.getMediaId());
        dto.setUploaderId(media.getUploaderId());
        dto.setOriginalName(media.getOriginalName());
        dto.setUrl(media.getUrl());
        dto.setMimeType(media.getMimeType());
        dto.setSizeKb(media.getSizeKb());
        dto.setLinkedPostId(media.getLinkedPostId());
        dto.setUploadedAt(media.getUploadedAt());
        return dto;
    }
}