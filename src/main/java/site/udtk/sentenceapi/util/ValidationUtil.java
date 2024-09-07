package site.udtk.sentenceapi.util;

import static site.udtk.sentenceapi.domain.type.Threshold.*;
import static site.udtk.sentenceapi.exception.ErrorCode.*;

import java.util.function.Predicate;

import site.udtk.sentenceapi.domain.type.Language;
import site.udtk.sentenceapi.domain.type.Sort;
import site.udtk.sentenceapi.exception.BaseException;
import site.udtk.sentenceapi.exception.ErrorCode;

public class ValidationUtil {
	public static void validateCount(Long count) {
		if (count <= 0 || count > REQUEST_LIMIT.getNum()) {
			throw new BaseException(RANGE_OUT_OF_BOUND);
		}
	}

	public static void validateLanguage(String language) {
		validate(language, Language::isSupported, LANGUAGE_NOT_FOUND);
	}

	public static void validateSort(String sort) {
		validate(sort, Sort::isSupported, SORT_NOT_FOUND);
	}

	public static void validate(String value, Predicate<String> condition, ErrorCode errorCode) {
		if (value == null || value.isEmpty() || !condition.test(value)) {
			throw new BaseException(errorCode);
		}
	}
}
