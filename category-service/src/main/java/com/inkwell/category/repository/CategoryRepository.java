package com.inkwell.category.repository;

import com.inkwell.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findBySlug(String slug);

    boolean existsByName(String name);

    boolean existsBySlug(String slug);

    List<Category> findByParentCategoryId(Long parentId);
}