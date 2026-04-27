package com.inkwell.post.service;

import com.inkwell.post.dto.PostRequestDTO;
import com.inkwell.post.dto.PostResponseDTO;
import com.inkwell.post.entity.Post;
import com.inkwell.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Override
    public PostResponseDTO createPost(PostRequestDTO dto) {
        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setAuthorId(dto.getAuthorId());
        
        // Logic: Unique Slug Generation
        String slug = dto.getTitle().toLowerCase().trim().replaceAll("[^a-z0-9]", "-");
        String uniqueSlug = slug;
        int count = 1;
        while(postRepository.findBySlug(uniqueSlug).isPresent()) {
            uniqueSlug = slug + "-" + count++;
        }
        post.setSlug(uniqueSlug);
        
        post.setStatus("PENDING"); 
        post.setCreatedAt(LocalDateTime.now());
        post.setViewCount(0);
        post.setLikesCount(0);
        
        return mapToDTO(postRepository.save(post));
    }

    @Override
    public PostResponseDTO getPostBySlug(String slug) {
        Post post = postRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Post not found with slug: " + slug));
        
        // Logic: Increment view count on every fetch by slug
        post.setViewCount(post.getViewCount() + 1);
        postRepository.save(post);
        
        return mapToDTO(post);
    }

    @Override
    public PostResponseDTO getPostById(Long id) {
        return mapToDTO(findEntityById(id));
    }

    @Override
    public PostResponseDTO updatePost(Long id, PostRequestDTO dto) {
        Post post = findEntityById(id);
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        // Status resets to PENDING for re-verification after update
        post.setStatus("PENDING"); 
        return mapToDTO(postRepository.save(post));
    }

    @Override
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    @Override
    public void publishPost(Long id) {
        Post post = findEntityById(id);
        post.setStatus("PUBLISHED");
        post.setPublishedAt(LocalDateTime.now());
        postRepository.save(post);
    }

    @Override
    public void unpublishPost(Long id) {
        Post post = findEntityById(id);
        post.setStatus("DRAFT");
        postRepository.save(post);
    }

    @Override
    public void approvePost(Long id) {
        Post post = findEntityById(id);
        post.setStatus("APPROVED");
        postRepository.save(post);
    }

    @Override
    public void rejectPost(Long id) {
        Post post = findEntityById(id);
        post.setStatus("REJECTED");
        postRepository.save(post);
    }

    @Override
    public void likePost(Long id) {
        Post post = findEntityById(id);
        post.setLikesCount(post.getLikesCount() + 1);
        postRepository.save(post);
    }

    @Override
    public void unlikePost(Long id) {
        Post post = findEntityById(id);
        if(post.getLikesCount() > 0) {
            post.setLikesCount(post.getLikesCount() - 1);
            postRepository.save(post);
        }
    }

    @Override
    public void incrementViews(Long id) {
        Post post = findEntityById(id);
        post.setViewCount(post.getViewCount() + 1);
        postRepository.save(post);
    }

    @Override
    public List<PostResponseDTO> searchPosts(String query) {

        return postRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query, query)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostResponseDTO> getPostsByAuthor(Long authorId) {
        return postRepository.findByAuthorId(authorId).stream()
                .map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public List<PostResponseDTO> getPublishedPosts() {
        return postRepository.findByStatus("PUBLISHED").stream()
                .map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public int getPostCountByAuthor(Long authorId) {
        return postRepository.findByAuthorId(authorId).size();
    }

    // Helper to find Entity
    private Post findEntityById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + id));
    }

   
    private PostResponseDTO mapToDTO(Post post) {
        PostResponseDTO res = new PostResponseDTO();
        res.setPostId(post.getPostId());
        res.setTitle(post.getTitle());
        res.setContent(post.getContent());
        res.setSlug(post.getSlug());
        res.setStatus(post.getStatus());
        res.setViewCount(post.getViewCount());
        res.setLikesCount(post.getLikesCount());
        res.setCreatedAt(post.getCreatedAt());
        res.setPublishedAt(post.getPublishedAt());
        return res;
    }
}