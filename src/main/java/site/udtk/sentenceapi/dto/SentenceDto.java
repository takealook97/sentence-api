package site.udtk.sentenceapi.dto;

import lombok.Builder;
import lombok.Getter;
import site.udtk.sentenceapi.domain.Sentence;

@Getter
@Builder
public class SentenceDto {
	private String author;
	private String content;

	public static SentenceDto of(Sentence sentence) {
		return SentenceDto.builder()
			.author(sentence.getAuthor())
			.content(sentence.getContent())
			.build();
	}
}
