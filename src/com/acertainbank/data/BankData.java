package com.acertainbank.data;

import java.util.Random;

// this is the class for set bank data and partition data

public class BankData {
	// partition ids
	private int numPartitions = 20;
	private String[] partitionIds = new String[numPartitions];
	
	// one partition has 1-5 branches
	// one branch has 500-1000 accounts
	
	private Random random = new Random();
	
	public BankData(){
		//set partitions ids
		setPartitionIds();
		setBankData();
	}
	
	private void setPartitionIds(){
		for(int i = 1; i <= numPartitions; i++)
		{
			String id = Integer.toString(i);
			if(id.length()<2)
			{
				id = "0"+id;
			}
			partitionIds[i-1] = id;
		}
	}
	
	private void setBankData()
	{
		for(int i = 0; i< numPartitions; i++)
		{
			String partitionId = this.partitionIds[i];
			int numBranch = random.nextInt(5)+1;
			for(int j = 0; j<numBranch; j++)
			{
				String branchId = Integer.toString(j+1);
				if(branchId.length()<2)
				{
					branchId = "0"+branchId;
				}
				int numAccount = random.nextInt(500)+500;
				for(int k = 0; k< numAccount; k++)
				{
					String accountId = Integer.toString(random.nextInt(1000)+1) ;
					if(branchId.length()<2)
					{
						branchId = "0"+branchId;
					}
				}
				System.out.println(partitionId + " " + branchId);
			}
		}
	}
	
	public static void main(String[] args)
	{
		new BankData();
	}
}
