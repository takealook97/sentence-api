package com.underthekey.sentenceapi.domain.type;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Language {
	// 한국어
	KOREAN("kor"),
	// 영어
	ENGLISH("eng");

	private final String name;

	public static boolean isSupported(String language) {
		return Arrays.stream(Language.values()).anyMatch(l -> l.getName().equals(language));
	}
}
