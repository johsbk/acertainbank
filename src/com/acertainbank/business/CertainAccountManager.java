package com.acertainbank.business;
import com.acertainbank.interfaces.AccountManager;
import com.acertainbank.exceptions.ExistentBranchException;
import com.acertainbank.exceptions.InexistentBranchException;
import com.acertainbank.exceptions.InexistentAccountException;
import com.acertainbank.exceptions.ExistentAccountException;
import com.acertainbank.exceptions.NegativeAmountException;

import java.util.HashMap;
public class CertainAccountManager implements AccountManager {
	private static CertainAccountManager singleInstance;
	private HashMap<Integer,BankBranch> branches = new HashMap<Integer,BankBranch>();
	
	public synchronized static CertainAccountManager getInstance() {
		if (singleInstance != null) {
		    return singleInstance;
		} else {
		    singleInstance = new CertainAccountManager();
		}
		return singleInstance;
    }
	public synchronized void addBranch(int branchId) throws ExistentBranchException {
		if(branches.containsKey(branchId)) throw new ExistentBranchException(branchId);
		this.branches.put(branchId, new BankBranch(branchId));
	}
	public synchronized void  addAccount(int branchId, int accountId) throws InexistentBranchException, ExistentAccountException {
		this.addAccount(branchId,accountId,0.0);
	}
	public synchronized void addAccount(int branchId, int accountId, double balance) throws InexistentBranchException, ExistentAccountException {
		if (!branches.containsKey(branchId)) throw new InexistentBranchException(branchId);
		branches.get(branchId).addAccount(accountId, balance);
	}
	public synchronized void reset() {
		branches = new HashMap<Integer,BankBranch>();
	}
	public synchronized double getAccountBalance(int branchId,int accountId) throws InexistentBranchException,InexistentAccountException {
		if (!branches.containsKey(branchId)) throw new InexistentBranchException(branchId);
		return branches.get(branchId).getAccount(accountId).balance;
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
	public synchronized void credit (int branchId, int accountId, double amount) throws InexistentBranchException, InexistentAccountException, NegativeAmountException {
		if (!branches.containsKey(branchId)) throw new InexistentBranchException(branchId);
		if (amount <0.) throw new NegativeAmountException(amount);
		BankAccount account = branches.get(branchId).getAccount(accountId);
		account.balance += amount;
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
	public synchronized void debit (int branchId, int accountId, double amount) throws InexistentBranchException, InexistentAccountException, NegativeAmountException {
		if (!branches.containsKey(branchId)) throw new InexistentBranchException(branchId);
		if (amount <0.) throw new NegativeAmountException(amount);
		BankAccount account = branches.get(branchId).getAccount(accountId);
		account.balance -= amount;
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
	public synchronized void transfer (int branchId, int accountIdOrig, int accountIdDest, double amount) throws InexistentBranchException, InexistentAccountException, NegativeAmountException {
		if (!branches.containsKey(branchId)) throw new InexistentBranchException(branchId);
		if (amount <0.) throw new NegativeAmountException(amount);
		BankBranch branch = branches.get(branchId);
		BankAccount accountOrig = branch.getAccount(accountIdOrig);
		BankAccount accountDest = branch.getAccount(accountIdDest);
		accountOrig.balance -= amount;
		accountDest.balance += amount;
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
	public synchronized double calculateExposure (int branchId) throws InexistentBranchException {
		if (!branches.containsKey(branchId)) throw new InexistentBranchException(branchId);
		double result = 0.0;
		BankBranch branch = branches.get(branchId);
		for (BankAccount account : branch.getAccounts()) {
			if (account.balance<0) {
				result -= account.balance;
			}
		}
		return result;
	}
}