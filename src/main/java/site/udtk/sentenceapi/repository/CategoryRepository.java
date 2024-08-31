package site.udtk.sentenceapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import site.udtk.sentenceapi.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	List<Category> findAllByLanguage(String language);

	Optional<Category> findBySort(String sort);
}
