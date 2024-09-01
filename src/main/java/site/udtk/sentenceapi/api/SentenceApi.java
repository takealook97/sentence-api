package site.udtk.sentenceapi.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.udtk.sentenceapi.dto.SentenceDto;
import site.udtk.sentenceapi.service.SentenceService;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class SentenceApi {
	private final SentenceService sentenceService;

	// @GetMapping("/{sentenceId}")
	// public ResponseEntity<SentenceDto> getSentenceById(@PathVariable Long sentenceId) {
	// 	SentenceDto sentenceDto = sentenceService.getSentenceById(sentenceId);
	// 	return ResponseEntity.ok(sentenceDto);
	// }

	@GetMapping("/random/{count}")
	public ResponseEntity<List<SentenceDto>> getRandomSentences(@PathVariable Long count) {
		return ResponseEntity.ok(sentenceService.getRandomSentences(count));
	}

	@GetMapping("/language/{language}/random/{count}")
	public ResponseEntity<List<SentenceDto>> getRandomSentencesByLanguage(
		@PathVariable String language,
		@PathVariable Integer count) {
		return ResponseEntity.ok(sentenceService.getRandomSentencesByLanguage(count, language));
	}

	@GetMapping("sort/{sort}/random/{count}")
	public ResponseEntity<List<SentenceDto>> getRandomSentencesByCategorySort(
		@PathVariable String sort,
		@PathVariable Integer count) {
		return ResponseEntity.ok(sentenceService.getRandomSentencesByCategorySort(count, sort));
	}

}
