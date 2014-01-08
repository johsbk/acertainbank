package com.acertainbank.business;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.acertainbank.exceptions.ExistentAccountException;
import com.acertainbank.exceptions.InexistentAccountException;

public class BankBranch {
	private int branchId;
	private HashMap<Integer,BankAccount> accounts = new HashMap<Integer,BankAccount>();
	public ReadWriteLock ixLock = new ReentrantReadWriteLock(); // when getting read lock it is really IX, when getting write it is X
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
		Collection<BankAccount> result = accounts.values();
		return result;
	}
	public void addAccount(int accountId, double balance) throws ExistentAccountException {
		if (accounts.containsKey(accountId)) throw new ExistentAccountException(accountId);
		accounts.put(accountId,new BankAccount(accountId,balance));
	}
}
