package com.acertainbank.business;

import java.util.HashMap;

import com.acertainbank.exceptions.AccountManagerException;

public class Bank {
	private static Bank singleInstance;
	private HashMap<String,AccountManagerPartition> accountManagerPartitions = new HashMap<String,AccountManagerPartition>();
	
	public synchronized static Bank getInstance() {
		if (singleInstance != null) {
		    return singleInstance;
		} else {
		    singleInstance = new Bank();
		}
		return singleInstance;
    }
	
	public synchronized void addPartition(String partitionId) throws AccountManagerException {
		if(accountManagerPartitions.containsKey(partitionId)) throw new AccountManagerException("Partition is already taken");
		this.accountManagerPartitions.put(partitionId, new AccountManagerPartition(partitionId));
	}
}
