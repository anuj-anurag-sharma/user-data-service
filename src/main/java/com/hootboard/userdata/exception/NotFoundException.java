package com.hootboard.userdata.exception;

public class NotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8062821506636012690L;

	public NotFoundException() {
		super();
	}

	public NotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotFoundException(String message) {
		super(message);
	}

	public NotFoundException(Throwable cause) {
		super(cause);
	}

}
