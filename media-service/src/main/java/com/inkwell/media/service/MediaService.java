package com.inkwell.media.service;

import com.inkwell.media.dto.MediaDTO;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface MediaService {
    MediaDTO uploadMedia(MultipartFile file, Long uploaderId);
    MediaDTO getMediaById(Long mediaId);
    List<MediaDTO> getMediaByPost(Long postId);
    void deleteMedia(Long mediaId);
    void linkToPost(Long mediaId, Long postId);
    void unlinkFromPost(Long mediaId);
}