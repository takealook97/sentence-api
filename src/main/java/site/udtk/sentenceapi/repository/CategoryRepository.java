package site.udtk.sentenceapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import site.udtk.sentenceapi.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	@Query(value = "SELECT s.id FROM sentence s "
		+ "JOIN category c ON s.category_id = c.id "
		+ "WHERE c.language = :language ORDER BY RAND() LIMIT :count", nativeQuery = true)
	List<Long> findRandomSentenceIdsByLanguage(@Param("language") String language, @Param("count") Long count);

	@Query(value = "SELECT s.id FROM sentence s "
		+ "JOIN category c ON s.category_id = c.id "
		+ "WHERE c.sort = :sort ORDER BY RAND() LIMIT :count", nativeQuery = true)
	List<Long> findRandomSentenceIdsBySort(@Param("sort") String sort, @Param("count") Long count);
}
