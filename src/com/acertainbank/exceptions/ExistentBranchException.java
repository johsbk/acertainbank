package com.acertainbank.exceptions;

/**
 * This Exception is thrown when the branch ID
 * with which adding a branch to AccountManager
 * is called is already present in the system.
 */
public class ExistentBranchException extends Exception {

	private static final long serialVersionUID = 1L;
	private int branchId;
	
	public ExistentBranchException (String message, int branchId) {
		super(message);
		this.branchId = branchId;
	}

	public ExistentBranchException (int branchId) {
		super("The branch "+branchId+" already exists");
		this.branchId = branchId;
	}

	public ExistentBranchException () {
		super("The branch already exists");
	}

	public int getBranchId () {
		return branchId;
	}
}
