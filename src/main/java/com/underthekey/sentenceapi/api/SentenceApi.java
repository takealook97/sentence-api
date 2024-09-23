package com.underthekey.sentenceapi.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.underthekey.sentenceapi.dto.SentenceDto;
import com.underthekey.sentenceapi.service.SentenceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class SentenceApi {
	private final SentenceService sentenceService;

	@GetMapping("/{sentenceId}")
	public ResponseEntity<SentenceDto> getSentenceById(@PathVariable Long sentenceId) {
		SentenceDto sentenceDto = sentenceService.getSentenceById(sentenceId);
		return ResponseEntity.ok(sentenceDto);
	}

	@GetMapping("/random")
	public ResponseEntity<List<SentenceDto>> getRandomSentences(
		@RequestParam(name = "count", required = false, defaultValue = "1") Long count) {
		return ResponseEntity.ok(sentenceService.getRandomSentences(count));
	}

	@GetMapping("/language")
	public ResponseEntity<List<SentenceDto>> getRandomSentencesByLanguage(
		@RequestParam(name = "language", required = false, defaultValue = "kor") String language,
		@RequestParam(name = "count", required = false, defaultValue = "1") Long count) {
		return ResponseEntity.ok(sentenceService.getRandomSentencesByLanguage(language, count));
	}

	@GetMapping("/sort")
	public ResponseEntity<List<SentenceDto>> getRandomSentencesByCategorySort(
		@RequestParam(name = "sort", required = false, defaultValue = "가1") String sort,
		@RequestParam(name = "count", required = false, defaultValue = "1") Long count) {
		return ResponseEntity.ok(sentenceService.getRandomSentencesByCategorySort(sort, count));
	}
}
