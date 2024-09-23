package com.underthekey.sentenceapi.config.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RateLimitConstant {
	BUCKET_CAPACITY(20), // Maximum capacity of the bucket
	REFILL_AMOUNT(20), // Refills 20 tokens at a time
	REFILL_DURATION_MINUTE(5), // Refills every 5 minutes
	REQUEST_COST_IN_TOKENS(1), // Each request consumes 1 token
	EXPIRATION_OF_HOUR(1); // Resets the IP bucket after 1 hour of inactivity

	private final int value;
}
