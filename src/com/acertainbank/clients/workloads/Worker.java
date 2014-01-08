package com.acertainbank.clients.workloads;

import com.acertainbank.exceptions.AccountManagerException;
import com.acertainbank.interfaces.AccountManager;

/**
 * 
 * Worker represents the workload runner which runs the workloads with
 * parameters using WorkloadConfiguration and then reports the results
 * 
 */
public class Worker {
	private WorkloadConfiguration configuration = null;

	public Worker(WorkloadConfiguration config) {
		configuration = config;
	}
	private boolean runInteraction() {
		try {
			// TODO: Add code for Customer Interaction
			AccountManager accountManager = configuration.getAccountManager();
			BankGenerator bankGenerator = configuration.getBankGenerator();
			
			
			int branchId = 0;
			int accountId = 0;
			double amount = 100.00;
			
			accountManager.debit(branchId, accountId, amount);
		} catch (AccountManagerException ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Run the workloads trying to respect the distributions of the interactions
	 * and return result in the end
	 */
	public WorkerRunResult call() throws Exception {
		int count = 1;
		long startTimeInNanoSecs = 0;
		long endTimeInNanoSecs = 0;
		int successfulInteractions = 0;
		long timeForRunsInNanoSecs = 0;

		// Perform the warm-up runs
		while (count++ <= configuration.getWarmUpRuns()) {
			runInteraction();
		}

		count = 1;

		// Perform the actual runs
		startTimeInNanoSecs = System.nanoTime();
		while (count++ <= configuration.getNumActuralRuns()) {
			if (runInteraction()) {
				successfulInteractions++;
			}
		}
		endTimeInNanoSecs = System.nanoTime();
		timeForRunsInNanoSecs += (endTimeInNanoSecs - startTimeInNanoSecs);
		return new WorkerRunResult(successfulInteractions,
				timeForRunsInNanoSecs, configuration.getNumActuralRuns());
	}
}
