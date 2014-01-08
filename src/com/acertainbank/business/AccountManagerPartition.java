package com.acertainbank.business;
import com.acertainbank.interfaces.AccountManager;
import com.acertainbank.exceptions.ExistentBranchException;
import com.acertainbank.exceptions.InexistentBranchException;
import com.acertainbank.exceptions.InexistentAccountException;
import com.acertainbank.exceptions.ExistentAccountException;
import com.acertainbank.exceptions.NegativeAmountException;

import java.util.HashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
public class AccountManagerPartition implements AccountManager {
	private static AccountManagerPartition singleInstance;
	private String partitionId;
	private HashMap<Integer,BankBranch> branches = new HashMap<Integer,BankBranch>();
	private ReadWriteLock ixLock = new ReentrantReadWriteLock();
	
	public AccountManagerPartition(String partitionId) {
		// TODO Auto-generated constructor stub
		this.partitionId = partitionId;
	}
	public AccountManagerPartition() {
		// TODO Auto-generated constructor stub
	}
	public static AccountManagerPartition getInstance() {
		if (singleInstance != null) {
		    return singleInstance;
		} else {
		    singleInstance = new AccountManagerPartition();
		}
		return singleInstance;
    }
	
	public String getPartitionId(){
		return this.partitionId;
	}
	
	public void addBranch(int branchId) throws ExistentBranchException {
		ixLock.writeLock().lock(); // X
		if(branches.containsKey(branchId)) throw new ExistentBranchException(branchId);
		this.branches.put(branchId, new BankBranch(branchId));
		ixLock.writeLock().unlock();
	}
	public void  addAccount(int branchId, int accountId) throws InexistentBranchException, ExistentAccountException {
		this.addAccount(branchId,accountId,0.0);
	}
	public void addAccount(int branchId, int accountId, double balance) throws InexistentBranchException, ExistentAccountException {
		try {
			ixLock.readLock().lock(); // S / IX
			if (!branches.containsKey(branchId)) throw new InexistentBranchException(branchId);
			BankBranch branch =branches.get(branchId);
			try {
				branch.ixLock.readLock().lock(); // IX lock
				branch.addAccount(accountId, balance);
			} finally {
				branch.ixLock.readLock().unlock();				
			}
		} finally {
			ixLock.readLock().unlock();
		}
	}
	public synchronized void reset() {
		try {
			ixLock.writeLock().lock(); // X
			branches = new HashMap<Integer,BankBranch>();
		} finally {
			ixLock.writeLock().unlock();
		}
	}
	public  double getAccountBalance(int branchId,int accountId) throws InexistentBranchException,InexistentAccountException {
		double balance = 0;
		try {
			ixLock.readLock().lock(); // IS
			if (!branches.containsKey(branchId)) throw new InexistentBranchException(branchId);
			BankBranch branch =branches.get(branchId);
			try {
				branch.ixLock.readLock().lock(); // IS lock
				
				BankAccount account = branch.getAccount(accountId);
				account.lock.readLock().lock(); // S 
				balance =  account.balance;
				account.lock.readLock().unlock();
			} finally {
				branch.ixLock.readLock().unlock(); // IS lock
			}
		} finally {
			ixLock.readLock().unlock();
		}
		return balance;
	}
	/**
	 * This operation credits the specified account at the given
	 * branch with the provided amount. The operation raises
	 * appropriate exceptions is the branch or the account are
	 * inexistent, or if the value given is negative.
	 * 
	 * @param branchId Branch where the account resides.
	 * @param accountId	Account to be credited.
	 * @param amount Amount with which to credit the account.
	 * @throws InexistentBranchException 
	 * @throws InexistentAccountException 
	 * @throws NegativeAmountException 
	 */
	public void credit (int branchId, int accountId, double amount) throws InexistentBranchException, InexistentAccountException, NegativeAmountException {
		if (amount <0.) throw new NegativeAmountException(amount);
		try {
			ixLock.readLock().lock(); //IX
			if (!branches.containsKey(branchId)) throw new InexistentBranchException(branchId);
			
			BankBranch branch =branches.get(branchId);
			try {
				branch.ixLock.readLock().lock(); // IX lock
				BankAccount account = branch.getAccount(accountId);
				account.lock.writeLock().lock(); //X
				account.balance += amount;
				account.lock.writeLock().unlock();
			} finally {
				branch.ixLock.readLock().unlock();
			}
		} finally {
			ixLock.readLock().unlock();
		}
	}
	
	/**
	 * This operation debits the specified account at the given
	 * branch with the provided amount. The operation raises
	 * appropriate exceptions is the branch or the account are
	 * inexistent, or if the value given is negative.
	 * 
	 * @param branchId Branch where the account resides.
	 * @param accountId	Account to be debited.
	 * @param amount Amount with which to debit the account.
	 * @throws InexistentBranchException If the branch does not exist
	 * in the system.
	 * @throws InexistentAccountException If the account does not exist
	 * in the system.
	 * @throws NegativeAmountException If the amount used is negative.
	 */
	public void debit (int branchId, int accountId, double amount) throws InexistentBranchException, InexistentAccountException, NegativeAmountException {
		if (amount <0.) throw new NegativeAmountException(amount);
		try {
			ixLock.readLock().lock(); //IX
			if (!branches.containsKey(branchId)) throw new InexistentBranchException(branchId);
			BankBranch branch =branches.get(branchId);
			try {
				branch.ixLock.readLock().lock(); // IX lock
				BankAccount account = branch.getAccount(accountId);
				account.lock.writeLock().lock();
				account.balance -= amount;
				account.lock.writeLock().unlock();
			} finally {
				branch.ixLock.readLock().unlock();
			}
		} finally {
			ixLock.readLock().unlock();
		}
	}
	
	/**
	 * This operation transfers the provided amount from the
	 * origin account to the destination account at the given
	 * branch. The operation raises appropriate exceptions if
	 * the branch or either account are inexistent, or if the
	 * value given is negative.
	 * 
	 * @param branchId Branch where the accounts reside.
	 * @param accountIdOrig Account to be debited.
	 * @param accountIdDest Account to be credited.
	 * @param amount Amount to transfer between accounts.
	 * @throws InexistentBranchException If the branch does not exist
	 * in the system.
	 * @throws InexistentAccountException If any of the accounts
	 * does not exist in the system.
	 * @throws NegativeAmountException If the amount used is negative.
	 */
	public void transfer (int branchId, int accountIdOrig, int accountIdDest, double amount) throws InexistentBranchException, InexistentAccountException, NegativeAmountException {
		if (amount <0.) throw new NegativeAmountException(amount);
		try {
			ixLock.readLock().lock(); //IX
			if (!branches.containsKey(branchId)) throw new InexistentBranchException(branchId);			
			BankBranch branch = branches.get(branchId);
			try {
				branch.ixLock.readLock().lock(); // IX lock
				BankAccount accountOrig = branch.getAccount(accountIdOrig);
				BankAccount accountDest = branch.getAccount(accountIdDest);
				accountOrig.lock.writeLock().lock();
				accountDest.lock.writeLock().lock();
				accountOrig.balance -= amount;
				accountDest.balance += amount;
				accountOrig.lock.writeLock().unlock();
				accountDest.lock.writeLock().unlock();
			} finally {
				branch.ixLock.readLock().unlock();
			}
		} finally {
			ixLock.readLock().unlock();
		}
	}
	
	/**
	 * This operation calculates the sum of the balances
	 * of all overdrafted accounts in the given branch
	 * and returns its absolute value as a simple estimate
	 * of the total exposure for that branch. An overdrafted
	 * account is an account with a negative balance. If
	 * there are no overdrafted accounts, then the exposure
	 * is zero. The operation raises an appropriate exception
	 * if the branch is inexistent.
	 * 
	 * @param branchId Branch to calculate the exposure.
	 * @return The exposure of the given branch.
	 * @throws InexistentBranchException If the branch does not exist
	 * in the system.
	 */
	public double calculateExposure (int branchId) throws InexistentBranchException {
		double result = 0.0;
		try {
			ixLock.readLock().lock(); //IX
			if (!branches.containsKey(branchId)) throw new InexistentBranchException(branchId);
			BankBranch branch = branches.get(branchId);
			branch.ixLock.writeLock().lock(); // S/X lock
			for (BankAccount account : branch.getAccounts()) {
				if (account.balance<0) {
					result -= account.balance;
				}
			}
			branch.ixLock.writeLock().unlock();
		} finally {
			ixLock.readLock().unlock();
		}
		return result;
	}
}
