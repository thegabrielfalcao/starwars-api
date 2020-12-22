package io.b2w.starwars.exception.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.b2w.starwars.model.ApiError;

@ControllerAdvice
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {

	private static final String RESOURCE_NOT_FOUND = "The resource was not found by your filters";
	private static final String NULL_RESOURCE = "The resource cannot be null";

	@ExceptionHandler(value = { NoSuchElementException.class })
	protected ResponseEntity<Object> handleElementNotFound(NoSuchElementException ex, WebRequest request) {
		return handleExceptionInternal(ex, new ApiError(RESOURCE_NOT_FOUND, ex.getMessage()), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return handleExceptionInternal(ex, new ApiError(NULL_RESOURCE, ex.getMessage()), headers, status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
	    List<ApiError> errors = new ArrayList<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			FieldError fieldError = (FieldError) error;
			errors.add(new ApiError(String.format("%s: %s", StringUtils.capitalize(fieldError.getObjectName()), fieldError.getDefaultMessage()), ex.getMessage()));
	    });
		
		return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
}