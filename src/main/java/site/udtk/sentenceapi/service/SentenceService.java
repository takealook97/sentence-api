package site.udtk.sentenceapi.service;

import static site.udtk.sentenceapi.domain.type.Threshold.*;
import static site.udtk.sentenceapi.exception.ErrorCode.*;
import static site.udtk.sentenceapi.util.RandomIdsGenerator.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import site.udtk.sentenceapi.domain.Sentence;
import site.udtk.sentenceapi.dto.SentenceDto;
import site.udtk.sentenceapi.exception.BaseException;
import site.udtk.sentenceapi.repository.CategoryRepository;
import site.udtk.sentenceapi.repository.SentenceRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class SentenceService {
	private final CategoryRepository categoryRepository;
	private final SentenceRepository sentenceRepository;
	private final RedisTemplate<Long, Object> redisTemplate;

	public SentenceDto getSentenceById(Long id) {
		SentenceDto cachedSentence = getCachedSentence(id);
		if (cachedSentence != null) {
			return cachedSentence;
		}

		Sentence sentence = sentenceRepository.findById(id).orElseThrow(() -> new BaseException(SENTENCE_NOT_FOUND));
		SentenceDto sentenceDto = SentenceDto.of(sentence);
		cacheSentence(id, sentenceDto);
		return sentenceDto;
	}

	public List<SentenceDto> getRandomSentences(Long count) {
		validateCount(count);
		List<Long> randomIds = generateRandomIds(count.intValue());
		return getRandomSentencesByIds(randomIds);
	}

	public List<SentenceDto> getRandomSentencesByLanguage(int count, String language) {
		validateCount((long)count);

		List<Long> randomIds = categoryRepository.findRandomSentenceIdsByLanguage(language, count);
		return getRandomSentencesByIds(randomIds);
	}

	public List<SentenceDto> getRandomSentencesByCategorySort(int count, String sort) {
		validateCount((long)count);

		List<Long> randomIds = categoryRepository.findRandomSentenceIdsBySort(sort, count);
		return getRandomSentencesByIds(randomIds);
	}

	private List<SentenceDto> getRandomSentencesByIds(List<Long> randomIds) {
		if (randomIds.isEmpty()) {
			return new ArrayList<>();
		}

		List<Object> cachedObjects = redisTemplate.opsForValue().multiGet(randomIds);
		if (cachedObjects == null) {
			cachedObjects = new ArrayList<>();
		}

		List<SentenceDto> result = processCachedSentences(cachedObjects);
		List<Long> missingIds = getMissingIds(randomIds, cachedObjects);

		if (!missingIds.isEmpty()) {
			result.addAll(getSentencesFromDbAndCache(missingIds));
		}

		return result;
	}

	private List<SentenceDto> processCachedSentences(List<Object> cachedObjects) {
		return cachedObjects.stream()
			.filter(Objects::nonNull)
			.map(cached -> (SentenceDto)cached)
			.collect(Collectors.toList());
	}

	private List<Long> getMissingIds(List<Long> randomIds, List<Object> cachedObjects) {
		return IntStream.range(0, cachedObjects.size())
			.filter(i -> cachedObjects.get(i) == null)
			.mapToObj(randomIds::get)
			.collect(Collectors.toList());
	}

	private List<SentenceDto> getSentencesFromDbAndCache(List<Long> missingIds) {
		List<Sentence> sentencesFromDb = sentenceRepository.findAllById(missingIds);

		return sentencesFromDb.stream().map(sentence -> {
			SentenceDto dto = SentenceDto.of(sentence);
			cacheSentence(sentence.getId(), dto);
			return dto;
		}).collect(Collectors.toList());
	}

	private void cacheSentence(Long id, SentenceDto sentenceDto) {
		redisTemplate.opsForValue().set(id, sentenceDto, Duration.ofMinutes(CACHE_DURATION_MINUTE.getNum()));
	}

	private SentenceDto getCachedSentence(Long id) {
		return (SentenceDto)redisTemplate.opsForValue().get(id);
	}

	private void validateCount(Long count) {
		if (count <= 0 || count > REQUEST_LIMIT.getNum()) {
			throw new BaseException(RANGE_OUT_OF_BOUND);
		}
	}
}