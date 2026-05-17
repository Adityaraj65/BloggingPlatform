package com.inkwell.media.service;

import com.inkwell.media.entity.Media;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

public interface MediaService {
    Media uploadMedia(MultipartFile file, Long uploaderId);
    Optional<Media> getMediaById(Long mediaId);
    List<Media> getMediaByUploader(Long uploaderId);
    List<Media> getMediaByPost(Long postId);
    void deleteMedia(Long mediaId);
    Media updateAltText(Long mediaId, String altText);
    void linkToPost(Long mediaId, Long postId);
    void unlinkFromPost(Long mediaId);
    List<Media> getAllMedia();
    void cleanupDeleted();
}