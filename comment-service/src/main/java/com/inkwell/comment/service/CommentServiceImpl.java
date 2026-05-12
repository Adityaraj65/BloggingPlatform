package com.inkwell.comment.service;

import com.inkwell.comment.client.PostClient;
import com.inkwell.comment.dto.CommentRequestDTO;
import com.inkwell.comment.dto.CommentResponseDTO;
import com.inkwell.comment.entity.Comment;
import com.inkwell.comment.repository.CommentRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository repo;

    private final PostClient postClient;

    public CommentServiceImpl(
            CommentRepository repo,
            PostClient postClient
    ) {

        this.repo = repo;
        this.postClient = postClient;
    }

    // ================= ADD COMMENT =================

    @Override
    public CommentResponseDTO addComment(
            CommentRequestDTO dto
    ) {

        // Validate post exists
        postClient.getPost(dto.getPostId());

        Comment c = new Comment();

        c.setPostId(dto.getPostId());

        c.setAuthorId(dto.getAuthorId());

        c.setAuthorName(dto.getAuthorName());

        c.setAuthorUsername(dto.getAuthorUsername());

        c.setParentCommentId(
                dto.getParentCommentId()
        );

        c.setContent(dto.getContent());

        c.setLikesCount(0);

        c.setStatus("APPROVED");

        c.setCreatedAt(LocalDateTime.now());

        return map(
                repo.save(c)
        );
    }

    // ================= GET COMMENTS =================

    @Override
    public List<CommentResponseDTO>
    getCommentsByPost(Long postId) {

        return repo
                .findByPostIdAndParentCommentIdIsNull(postId)
                .stream()
                .map(comment -> {

                    CommentResponseDTO dto =
                            map(comment);

                    dto.setReplies(
                            getReplies(
                                    comment.getCommentId()
                            )
                    );

                    return dto;
                })
                .collect(Collectors.toList());
    }

    // ================= GET REPLIES =================

    @Override
    public List<CommentResponseDTO>
    getReplies(Long parentId) {

        return repo
                .findByParentCommentId(parentId)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    // ================= UPDATE =================

    @Override
    public CommentResponseDTO updateComment(
            Long id,
            String content
    ) {

        Comment c = find(id);

        c.setContent(content);

        c.setUpdatedAt(LocalDateTime.now());

        return map(
                repo.save(c)
        );
    }

    // ================= DELETE =================

    @Override
    public void deleteComment(Long id) {

        Comment c = find(id);

        c.setStatus("DELETED");

        repo.save(c);
    }

    // ================= APPROVE =================

    @Override
    public void approveComment(Long id) {

        Comment c = find(id);

        c.setStatus("APPROVED");

        repo.save(c);
    }

    // ================= REJECT =================

    @Override
    public void rejectComment(Long id) {

        Comment c = find(id);

        c.setStatus("REJECTED");

        repo.save(c);
    }

    // ================= LIKE =================

    @Override
    public void likeComment(Long id) {

        Comment c = find(id);

        c.setLikesCount(
                c.getLikesCount() + 1
        );

        repo.save(c);
    }

    // ================= UNLIKE =================

    @Override
    public void unlikeComment(Long id) {

        Comment c = find(id);

        if (c.getLikesCount() > 0) {

            c.setLikesCount(
                    c.getLikesCount() - 1
            );

            repo.save(c);
        }
    }

    // ================= COUNT =================

    @Override
    public int getCommentCount(Long postId) {

        return repo.countByPostId(postId);
    }

    // ================= FIND =================

    private Comment find(Long id) {

        return repo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Comment not found"
                        )
                );
    }

    // ================= MAP =================

    private CommentResponseDTO map(Comment c) {

        CommentResponseDTO dto =
                new CommentResponseDTO();

        dto.setCommentId(
                c.getCommentId()
        );

        dto.setPostId(
                c.getPostId()
        );

        dto.setAuthorId(
                c.getAuthorId()
        );

        dto.setAuthorName(
                c.getAuthorName()
        );

        dto.setAuthorUsername(
                c.getAuthorUsername()
        );

        dto.setParentCommentId(
                c.getParentCommentId()
        );

        dto.setContent(
                c.getContent()
        );

        dto.setLikesCount(
                c.getLikesCount()
        );

        dto.setStatus(
                c.getStatus()
        );

        dto.setCreatedAt(
                c.getCreatedAt()
        );

        return dto;
    }
}