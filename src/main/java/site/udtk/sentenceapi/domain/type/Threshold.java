package site.udtk.sentenceapi.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Threshold {
	DATA_SIZE(4758),
	REQUEST_LIMIT(20),
	CACHE_DURATION_MINUTE(60);

	private final int num;
}
