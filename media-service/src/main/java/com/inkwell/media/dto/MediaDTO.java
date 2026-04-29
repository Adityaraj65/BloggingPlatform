package com.inkwell.media.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MediaDTO {
    private Long mediaId;
    private Long uploaderId;
    private String originalName;
    private String url;
    private String mimeType;
    private Long sizeKb;
    private Long linkedPostId;
    private LocalDateTime uploadedAt;
	public Long getMediaId() {
		return mediaId;
	}
	public void setMediaId(Long mediaId) {
		this.mediaId = mediaId;
	}
	public Long getUploaderId() {
		return uploaderId;
	}
	public void setUploaderId(Long uploaderId) {
		this.uploaderId = uploaderId;
	}
	public String getOriginalName() {
		return originalName;
	}
	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public Long getSizeKb() {
		return sizeKb;
	}
	public void setSizeKb(Long sizeKb) {
		this.sizeKb = sizeKb;
	}
	public Long getLinkedPostId() {
		return linkedPostId;
	}
	public void setLinkedPostId(Long linkedPostId) {
		this.linkedPostId = linkedPostId;
	}
	public LocalDateTime getUploadedAt() {
		return uploadedAt;
	}
	public void setUploadedAt(LocalDateTime uploadedAt) {
		this.uploadedAt = uploadedAt;
	}
}