package com.acertainbank.business;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class BankAccount {
	public double balance;
	public int accountId;
	public ReadWriteLock lock = new ReentrantReadWriteLock();
	public BankAccount(int accountId) {
		this.accountId = accountId;
	}
	public BankAccount(int accountId,double balance) {
		this.accountId = accountId;
		this.balance = balance;
	}
}
