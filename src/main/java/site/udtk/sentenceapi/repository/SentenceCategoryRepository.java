package site.udtk.sentenceapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import site.udtk.sentenceapi.domain.SentenceCategory;

public interface SentenceCategoryRepository extends JpaRepository<SentenceCategory, Long> {
}
