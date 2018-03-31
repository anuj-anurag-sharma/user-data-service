package com.hootboard.userdata.exception;

import org.springframework.validation.BindingResult;

public class RequestValidationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2378812776236788720L;

	private BindingResult result;

	public RequestValidationException(BindingResult result) {
		super();
		this.result = result;
	}

	public RequestValidationException() {
		super();
	}

	public RequestValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public RequestValidationException(String message) {
		super(message);
	}

	public RequestValidationException(Throwable cause) {
		super(cause);
	}

	public BindingResult getResult() {
		return result;
	}

}
