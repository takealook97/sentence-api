package com.underthekey.sentenceapi.util;

import java.util.function.Predicate;

import com.underthekey.sentenceapi.domain.type.Language;
import com.underthekey.sentenceapi.domain.type.Sort;
import com.underthekey.sentenceapi.domain.type.Threshold;
import com.underthekey.sentenceapi.exception.BaseException;
import com.underthekey.sentenceapi.exception.ErrorCode;

public class ValidationUtil {
	public static void validateCount(Long count) {
		if (count <= 0 || count > Threshold.REQUEST_LIMIT.getNum()) {
			throw new BaseException(ErrorCode.RANGE_OUT_OF_BOUND);
		}
	}

	public static void validateLanguage(String language) {
		validate(language, Language::isSupported, ErrorCode.LANGUAGE_NOT_FOUND);
	}

	public static void validateSort(String sort) {
		validate(sort, Sort::isSupported, ErrorCode.SORT_NOT_FOUND);
	}

	public static void validate(String value, Predicate<String> condition, ErrorCode errorCode) {
		if (value == null || value.isEmpty() || !condition.test(value)) {
			throw new BaseException(errorCode);
		}
	}
}
