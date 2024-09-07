package site.udtk.sentenceapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.micrometer.common.lang.NonNullApi;
import site.udtk.sentenceapi.domain.Sentence;

@Repository
@NonNullApi
public interface SentenceRepository extends JpaRepository<Sentence, Long> {
	@EntityGraph(attributePaths = {"category"})
	Optional<Sentence> findById(Long id);

	@EntityGraph(attributePaths = {"category"})
	@Query("SELECT s FROM Sentence s WHERE s.id IN :ids")
	List<Sentence> findAllById(List<Long> ids);
}
