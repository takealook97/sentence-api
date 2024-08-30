package site.udtk.sentenceapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import site.udtk.sentenceapi.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
