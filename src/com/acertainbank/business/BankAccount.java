package com.acertainbank.business;

public class BankAccount {
	public double balance;
	public int accountId;
	public BankAccount(int accountId) {
		this.accountId = accountId;
	}
	public BankAccount(int accountId,double balance) {
		this.accountId = accountId;
		this.balance = balance;
	}
}
