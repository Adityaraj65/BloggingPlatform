package com.inkwell.comment.dto;

import jakarta.validation.constraints.*;

public class CommentRequestDTO {

    @NotNull(message = "Post ID is required")
    private Long postId;

    @NotNull(message = "Author ID is required")
    private Long authorId;

    private Long parentCommentId;

    @NotBlank(message = "Comment content is required")
    @Size(min = 3, max = 1000, message = "Comment content must be between 3 and 1000 characters")
    private String content;
    
    private String authorName;
    private String authorUsername;

    // getters/setters
    
	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getAuthorUsername() {
		return authorUsername;
	}

	public void setAuthorUsername(String authorUsername) {
		this.authorUsername = authorUsername;
	}

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