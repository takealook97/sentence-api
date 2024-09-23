package com.underthekey.sentenceapi.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Type {
	// 문자 (첫 글자)
	LETTER("letter"),
	// 테마
	THEME("theme");

	private final String name;
}
