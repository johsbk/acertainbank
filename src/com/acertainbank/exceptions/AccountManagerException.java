package com.acertainbank.exceptions;

/**
 * Exception to signal a book store error
 */
public class AccountManagerException extends Exception {
	private static final long serialVersionUID = 1L;

	public AccountManagerException() {
		super();
	}

	public AccountManagerException(String message) {
		super(message);
	}

	public AccountManagerException(String message, Throwable cause) {
		super(message, cause);
	}

	public AccountManagerException(Throwable ex) {
		super(ex);
	}
}
