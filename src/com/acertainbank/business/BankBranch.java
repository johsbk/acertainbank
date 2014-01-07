package com.acertainbank.business;

import java.util.Collection;
import java.util.HashMap;

import com.acertainbank.exceptions.ExistentAccountException;
import com.acertainbank.exceptions.InexistentAccountException;

public class BankBranch {
	private int branchId;
	private HashMap<Integer,BankAccount> accounts = new HashMap<Integer,BankAccount>();
	public BankBranch(int branchId) {
		this.branchId = branchId;
	}
	public BankAccount getAccount(int accountId) throws InexistentAccountException {
		return accounts.get(accountId);
	}
	public int getBranchId() {
		return this.branchId;
	}
	public Collection<BankAccount> getAccounts() {
		return accounts.values();
	}
	public void addAccount(int accountId, double balance) throws ExistentAccountException {
		if (accounts.containsKey(accountId)) throw new ExistentAccountException(accountId);
		accounts.put(accountId,new BankAccount(accountId,balance));
	}
}
