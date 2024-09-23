package com.underthekey.sentenceapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.underthekey.sentenceapi.domain.Sentence;

import io.micrometer.common.lang.NonNullApi;

@Repository
@NonNullApi
public interface SentenceRepository extends JpaRepository<Sentence, Long> {
	@EntityGraph(attributePaths = {"category"})
	Optional<Sentence> findById(Long id);

	@EntityGraph(attributePaths = {"category"})
	@Query("SELECT s FROM Sentence s WHERE s.id IN :ids")
	List<Sentence> findAllById(List<Long> ids);
}
