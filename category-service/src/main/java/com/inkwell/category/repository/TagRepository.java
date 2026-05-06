package com.inkwell.category.repository;

import com.inkwell.category.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findBySlug(String slug);

    boolean existsByName(String name);

    List<Tag> findTop10ByOrderByPostCountDesc();
}