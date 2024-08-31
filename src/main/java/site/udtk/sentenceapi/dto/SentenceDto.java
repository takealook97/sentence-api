package site.udtk.sentenceapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.udtk.sentenceapi.domain.Sentence;

@Getter
@AllArgsConstructor
@NoArgsConstructor
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
