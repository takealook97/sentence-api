package site.udtk.sentenceapi.service;

import static site.udtk.sentenceapi.exception.ErrorCode.*;
import static site.udtk.sentenceapi.util.RandomIdsGenerator.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import site.udtk.sentenceapi.domain.Category;
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

		Sentence sentence = sentenceRepository.findById(id)
			.orElseThrow(() -> new BaseException(SENTENCE_NOT_FOUND));
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

		// 해당 언어의 모든 카테고리에 속한 문장의 ID 가져오기
		List<Long> sentenceIds = categoryRepository.findAllByLanguage(language).stream()
			.flatMap(category -> category.getSentences().stream())
			.map(Sentence::getId)
			.toList();

		// 필요한 문장 수만큼 랜덤하게 선택
		List<Long> randomIds = generateRandomIds(count);
		return getRandomSentencesByIds(randomIds);
	}

	public List<SentenceDto> getRandomSentencesByCategorySort(int count, String sort) {
		validateCount((long)count);

		// 특정 sort에 해당하는 카테고리에 속한 문장의 ID 가져오기
		Category category = categoryRepository.findBySort(sort)
			.orElseThrow(() -> new BaseException(SORT_NOT_FOUND));

		// 필요한 문장 수만큼 랜덤하게 선택
		List<Long> randomIds = generateRandomIds(count);
		return getRandomSentencesByIds(randomIds);
	}

	private List<SentenceDto> getRandomSentencesByIds(List<Long> randomIds) {
		List<Object> cachedObjects = redisTemplate.opsForValue().multiGet(randomIds);

		List<SentenceDto> result = processCachedSentences(cachedObjects);
		List<Long> missingIds = getMissingIds(randomIds, cachedObjects);

		if (!missingIds.isEmpty()) {
			result.addAll(getSentencesFromDbAndCache(missingIds));
		}

		return result;
	}

	private List<SentenceDto> processCachedSentences(List<Object> cachedObjects) {
		List<SentenceDto> result = new ArrayList<>();

		if (cachedObjects != null) {
			for (Object cached : cachedObjects) {
				if (cached != null) {
					result.add((SentenceDto)cached);
				}
			}
		}

		return result;
	}

	private List<Long> getMissingIds(List<Long> randomIds, List<Object> cachedObjects) {
		List<Long> missingIds = new ArrayList<>();

		if (cachedObjects != null) {
			for (int i = 0; i < cachedObjects.size(); i++) {
				if (cachedObjects.get(i) == null) {
					missingIds.add(randomIds.get(i));
				}
			}
		}

		return missingIds;
	}

	private List<SentenceDto> getSentencesFromDbAndCache(List<Long> missingIds) {
		List<Sentence> sentencesFromDb = sentenceRepository.findAllById(missingIds);

		return sentencesFromDb.stream()
			.map(sentence -> {
				SentenceDto dto = SentenceDto.of(sentence);
				cacheSentence(sentence.getId(), dto);
				return dto;
			})
			.collect(Collectors.toList());
	}

	private void cacheSentence(Long id, SentenceDto sentenceDto) {
		redisTemplate.opsForValue().set(id, sentenceDto, Duration.ofHours(1));
	}

	private SentenceDto getCachedSentence(Long id) {
		return (SentenceDto)redisTemplate.opsForValue().get(id);
	}

	private void validateCount(Long count) {
		if (count <= 0 || count > 20) {
			throw new BaseException(RANGE_OUT_OF_BOUND);
		}
	}
}
