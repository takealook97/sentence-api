package site.udtk.sentenceapi.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class SentenceDto {
	private String author;
	private String content;
}
