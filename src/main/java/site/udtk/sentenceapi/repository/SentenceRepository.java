package site.udtk.sentenceapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import site.udtk.sentenceapi.domain.Sentence;

@Repository
public interface SentenceRepository extends JpaRepository<Sentence, Long> {
}
