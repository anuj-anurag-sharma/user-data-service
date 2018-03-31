package com.hootboard.userdata.exception;

public class LoginException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7092651267560478113L;

	public LoginException(String string) {
		super(string);
	}

	public LoginException() {
		super();
	}

	public LoginException(String message, Throwable cause) {
		super(message, cause);
	}

	public LoginException(Throwable cause) {
		super(cause);
	}

}
