package com.hootboard.userdata.exception;

public class UserException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -977603515598737078L;

	public UserException() {

	}

	public UserException(String message) {
		super(message);
	}

	public UserException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserException(Throwable cause) {
		super(cause);
	}
	
}
