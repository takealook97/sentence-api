package site.udtk.sentenceapi.exception;

import lombok.Builder;

@Builder
public record ErrorResponse(
	int httpStatus,
	String errorMsg
) {
}
