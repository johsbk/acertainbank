package com.acertainbank.clients.workloads;

/**
 * 
 * WorkerRunResult class represents the result returned by a worker class after
 * running the workload interactions
 * 
 */
public class WorkerRunResult {
	private int successfulInteractions; // total number of successful interactions
	private int totalRuns; // total number of interactions run 
	private long elapsedTimeInNanoSecs; // total time taken to run all
										// interactions
	
	public WorkerRunResult(int successfulInteractions, long elapsedTimeInNanoSecs,
			int totalRuns) {
		this.setSuccessfulInteractions(successfulInteractions);
		this.setElapsedTimeInNanoSecs(elapsedTimeInNanoSecs);
		this.setTotalRuns(totalRuns);
	}
	
	public int getSuccessfulInterations()
	{
		return this.successfulInteractions;
	}
	
	public int getTotalRuns(){
		return this.totalRuns;
	}
	
	public long getElapsedTimeInNanoSecs()
	{
		return this.elapsedTimeInNanoSecs;
	}
	public void setSuccessfulInteractions(int _successfulInterations)
	{
		this.successfulInteractions = _successfulInterations;
	}
	
	public void setElapsedTimeInNanoSecs(long _nanoSecs)
	{
		this.elapsedTimeInNanoSecs = _nanoSecs;
	}
	
	public void setTotalRuns(int _totalRuns)
	{
		this.totalRuns = _totalRuns;
	}
	
}
