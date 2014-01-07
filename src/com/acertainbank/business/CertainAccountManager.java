package com.acertainbank.business;
import com.acertainbank.interfaces.AccountManager;
import com.acertainbank.exceptions.InexistentBranchException;
import com.acertainbank.exceptions.InexistentAccountException;
import com.acertainbank.exceptions.ExistentAccountException;
import com.acertainbank.exceptions.NegativeAmountException;

import java.util.HashMap;
import java.util.Map.Entry;
public class CertainAccountManager implements AccountManager {
	private static CertainAccountManager singleInstance;
	public int branchId =0;
	private HashMap<Integer,Double> accounts = new HashMap<Integer,Double>();
	
	public synchronized static CertainAccountManager getInstance() {
		if (singleInstance != null) {
		    return singleInstance;
		} else {
		    singleInstance = new CertainAccountManager();
		}
		return singleInstance;
    }
	public synchronized void  addAccount(int branchId, int accountId) throws InexistentBranchException, ExistentAccountException {
		this.addAccount(branchId,accountId,0.0);
	}
	public synchronized void addAccount(int branchId, int accountId, double balance) throws InexistentBranchException, ExistentAccountException {
		if (this.branchId!=branchId) throw new InexistentBranchException(branchId);
		if (accounts.containsKey(accountId)) throw new ExistentAccountException(accountId);
		accounts.put(accountId,new Double(balance));
	}
	public synchronized double getAccountBalance(int branchId,int accountId) throws InexistentBranchException,InexistentAccountException {
		if (this.branchId!=branchId) throw new InexistentBranchException(branchId);
		if (!accounts.containsKey(accountId)) throw new InexistentAccountException(accountId);
		return accounts.get(accountId);
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
		if (this.branchId!=branchId) throw new InexistentBranchException(branchId);
		if (!accounts.containsKey(accountId)) throw new InexistentAccountException(accountId);
		if (amount <0.) throw new NegativeAmountException(amount);
		Double balance = accounts.get(accountId);
		balance += amount;
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
		if (this.branchId!=branchId) throw new InexistentBranchException(branchId);
		if (!accounts.containsKey(accountId)) throw new InexistentAccountException(accountId);
		if (amount <0.) throw new NegativeAmountException(amount);
		Double balance = accounts.get(accountId);
		balance -= amount;
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
		if (this.branchId!=branchId) throw new InexistentBranchException(branchId);
		if (!accounts.containsKey(accountIdOrig)) throw new InexistentAccountException(accountIdOrig);
		if (!accounts.containsKey(accountIdDest)) throw new InexistentAccountException(accountIdDest);
		if (amount <0.) throw new NegativeAmountException(amount);
		Double balanceOrig = accounts.get(accountIdOrig);
		Double balanceDest = accounts.get(accountIdDest);
		balanceOrig -= amount;
		balanceDest += amount;
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
		if (this.branchId!=branchId) throw new InexistentBranchException(branchId);
		double result = 0.0;
		for (Entry<Integer,Double> entry : accounts.entrySet()) {
			if (entry.getValue()<0) {
				result -= entry.getValue();
			}
		}
		return result;
	}
}