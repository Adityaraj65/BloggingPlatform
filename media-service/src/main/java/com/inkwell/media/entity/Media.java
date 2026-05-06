package com.inkwell.media.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "media")
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mediaId;

    @Column(nullable = false)
    private Long uploaderId;

    private String filename;
    private String originalName;
    private String url;
    private String mimeType;
    private Long sizeKb;
    private String altText;
    private Long linkedPostId;

    private LocalDateTime uploadedAt = LocalDateTime.now();

    private boolean isDeleted = false;

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

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
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

	public String getAltText() {
		return altText;
	}

	public void setAltText(String altText) {
		this.altText = altText;
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

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

    // getters & setters
}