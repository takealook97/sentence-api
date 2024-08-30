package site.udtk.sentenceapi.domain.type;

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
}
