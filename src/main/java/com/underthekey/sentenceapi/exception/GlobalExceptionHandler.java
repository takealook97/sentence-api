package com.underthekey.sentenceapi.exception;

import static com.underthekey.sentenceapi.exception.ErrorCode.*;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BaseException.class)
	public ResponseEntity<ErrorResponse> handleCustomException(BaseException e) {
		return ResponseEntity.status(e.getErrorCode().getHttpStatus())
			.body(new ErrorResponse(e.getErrorCode().getHttpStatus().value(), e.getErrorCode().getErrorMsg()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(new ErrorResponse(METHOD_ARGUMENT_NOT_VALID.getHttpStatus().value(), Objects.requireNonNull(
				e.getFieldError()).getDefaultMessage()));
	}
}
