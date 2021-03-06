package com.acertainbank.exceptions;

/**
 * This Exception is thrown when the branch and account ID
 * with which adding an account AccountManager
 * is called is already present in the system.
 */
public class ExistentAccountException extends AccountManagerException {

	private static final long serialVersionUID = 1L;
	private int accountId;
	
	public ExistentAccountException (String message, int accountId) {
		super(message);
		this.accountId = accountId;
	}

	public ExistentAccountException (int accountId) {
		super("The account "+accountId+" already exists");
		this.accountId = accountId;
	}

	public ExistentAccountException () {
		super("The account already exists");
	}

	public int getAccountId () {
		return accountId;
	}
}
