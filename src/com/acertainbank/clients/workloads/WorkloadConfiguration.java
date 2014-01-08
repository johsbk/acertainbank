package com.acertainbank.clients.workloads;

import com.acertainbank.business.AccountManagerPartition;
import com.acertainbank.client.AccountManagerHTTPProxy;
import com.acertainbank.interfaces.AccountManager;

/**
 * 
 * WorkloadConfiguration represents the configuration parameters to be used by
 * BankWorkers class for running the workloads
 * 
 */

public class WorkloadConfiguration {
	private int warmUpRuns = 10;
	private int numActualRuns = 50;

	private BankGenerator bankGenerator = null;
	private AccountManager accountManager  = null;
	
	public int getWarmUpRuns(){
		return this.warmUpRuns;
	}
	public int getNumActuralRuns(){
		return this.numActualRuns;
	}
	
	public BankGenerator getBankGenerator()
	{
		return this.bankGenerator;
	}
	public void setBankGenerator(BankGenerator _bankGenerator)
	{
		this.bankGenerator = _bankGenerator;
	}
	
	public AccountManager getAccountManager(){
		return this.accountManager;
	}
	
	public void setAccountManager(AccountManager _accountManager){
		this.accountManager = _accountManager;
	}
	
	public WorkloadConfiguration(String serverAddress, boolean localTest)
			throws Exception {
		// Create a new one so that it is not shared
		bankGenerator = new BankGenerator();
		// Initialize the RPC interfaces if its not a localTest
		if (localTest) {
			accountManager = AccountManagerPartition.getInstance();
		} else {
			accountManager = new AccountManagerHTTPProxy(serverAddress);
		}
	}
}
