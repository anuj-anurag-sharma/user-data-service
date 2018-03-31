package com.hootboard.userdata.exception;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.hootboard.userdata.response.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

	private Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(RequestValidationException.class)
	public ResponseEntity<ErrorResponse> requestValidationException(final RequestValidationException e) {
		String message = formErrorMessage(e.getResult());
		ErrorResponse errorResponse = new ErrorResponse(message);
		LOGGER.error("ValidationException, {} ", message);
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UserException.class)
	public ResponseEntity<ErrorResponse> userException(final UserException e) {
		String message = e.getMessage();
		ErrorResponse errorResponse = new ErrorResponse(message);
		LOGGER.error("UserException, {} ", message);
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(AuthTokenException.class)
	public ResponseEntity<ErrorResponse> authTokenException(final AuthTokenException e) {
		String message = e.getMessage();
		ErrorResponse errorResponse = new ErrorResponse(message);
		LOGGER.error("AuthTokenException, {} ", message);
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ErrorResponse> clientNotFoundException(final NotFoundException e) {
		String message = e.getMessage();
		ErrorResponse errorResponse = new ErrorResponse(message);
		LOGGER.error("NotFoundException, {} ", message);
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(LoginException.class)
	public ResponseEntity<ErrorResponse> loginFailureException(final LoginException e) {
		String message = e.getMessage();
		ErrorResponse errorResponse = new ErrorResponse(message);
		LOGGER.error("LoginException, {} ", message);
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(RegistrationException.class)
	public ResponseEntity<ErrorResponse> registrationException(final RegistrationException e) {
		String message = e.getMessage();
		ErrorResponse errorResponse = new ErrorResponse(message);
		LOGGER.error("RegistrationException, {} ", message);
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> genericException(final Exception e) {
		String message = e.getMessage();
		ErrorResponse errorResponse = new ErrorResponse(message);
		LOGGER.error("Exception, {} ", message);
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	private String formErrorMessage(BindingResult bindingResult) {
		List<FieldError> errors = bindingResult.getFieldErrors();
		List<String> message = new ArrayList<>();
		for (FieldError e : errors) {
			message.add("@" + e.getField() + ":" + e.getDefaultMessage());
		}
		return message.toString();
	}

}
