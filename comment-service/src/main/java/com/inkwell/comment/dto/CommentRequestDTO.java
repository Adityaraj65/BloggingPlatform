package com.inkwell.comment.dto;

import jakarta.validation.constraints.*;

public class CommentRequestDTO {

    @NotNull
    private Long postId;

    @NotNull
    private Long authorId;

    private Long parentCommentId;

    @NotBlank
    private String content;

    // getters/setters
    
	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	public Long getParentCommentId() {
		return parentCommentId;
	}

	public void setParentCommentId(Long parentCommentId) {
		this.parentCommentId = parentCommentId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

   
}