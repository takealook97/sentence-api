package site.udtk.sentenceapi.config.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RateLimitConstant {
	BUCKET_CAPACITY(20),
	REFILL_AMOUNT(20),
	REFILL_DURATION_MINUTE(10),
	REQUEST_COST_IN_TOKENS(1);

	private final int value;
}
