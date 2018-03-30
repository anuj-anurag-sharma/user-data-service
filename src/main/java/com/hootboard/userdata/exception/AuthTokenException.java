package com.hootboard.userdata.exception;

public class AuthTokenException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7313213335015151134L;

	public AuthTokenException() {

	}

	public AuthTokenException(String message) {
		super(message);
	}

	public AuthTokenException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthTokenException(Throwable cause) {
		super(cause);
	}

}
