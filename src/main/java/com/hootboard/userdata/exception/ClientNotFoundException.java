package com.hootboard.userdata.exception;

public class ClientNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8062821506636012690L;

	public ClientNotFoundException() {
		super();
	}

	public ClientNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ClientNotFoundException(String message) {
		super(message);
	}

	public ClientNotFoundException(Throwable cause) {
		super(cause);
	}

}
