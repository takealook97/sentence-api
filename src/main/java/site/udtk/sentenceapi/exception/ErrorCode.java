package site.udtk.sentenceapi.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	SENTENCE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 문장을 찾을 수 없습니다."),
	SORT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 카테고리 종류를 찾을 수 없습니다."),
	LANGUAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 언어를 찾을 수 없습니다."),
	RANGE_OUT_OF_BOUND(HttpStatus.BAD_REQUEST, "범위를 벗어난 값입니다."),

	// 4xx: Client Errors
	NOT_FOUND(HttpStatus.NOT_FOUND, "Not Found"),
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "Method Not Allowed"),
	CONFLICT(HttpStatus.CONFLICT, "Conflict"),

	// 5xx: Server Errors
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
	NOT_IMPLEMENTED(HttpStatus.NOT_IMPLEMENTED, "Not Implemented"),
	BAD_GATEWAY(HttpStatus.BAD_GATEWAY, "Bad Gateway"),
	SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "Service Unavailable"),
	GATEWAY_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, "Gateway Timeout"),
	HTTP_VERSION_NOT_SUPPORTED(HttpStatus.HTTP_VERSION_NOT_SUPPORTED, "HTTP Version Not Supported"),

	// validation Errors
	METHOD_ARGUMENT_NOT_VALID(HttpStatus.BAD_REQUEST, "유효성 검사 실패");

	private final HttpStatus httpStatus;
	private final String errorMsg;
}
