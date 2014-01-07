package com.acertainbank.tests;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.acertainbank.business.CertainAccountManager;
import com.acertainbank.exceptions.InexistentAccountException;
import com.acertainbank.exceptions.InexistentBranchException;
import com.acertainbank.exceptions.NegativeAmountException;
public class CertainAccountManagerTest {
	private static CertainAccountManager accountManager;
	@BeforeClass
    public static void setUpBeforeClass() {
		accountManager = CertainAccountManager.getInstance();
		accountManager.branchId = 1;
    }
	@Before
    public void setUp() throws Exception {
		accountManager.reset();
    }
    @Test
    public void addAccounts() {
    	try {
	    	accountManager.addAccount(1,1,5.0);
	    	assertEquals(5.0,accountManager.getAccountBalance(1,1),0.1);
    	} catch(Exception e) {
    		e.printStackTrace();
	    	fail();
    	}
    }
    
    @Test
    public void creditAccount() {
    	try {
    		accountManager.addAccount(1,1);
    	} catch(Exception e) {
    		e.printStackTrace();
	    	fail();
    	}
    	try {
			accountManager.credit(1, 1, 5.0);
			System.out.println(accountManager.getAccountBalance(1,1));
			assertEquals(5.0,accountManager.getAccountBalance(1,1),0.1);
		} catch (InexistentBranchException e) {
			fail();
			e.printStackTrace();			
		} catch (InexistentAccountException e) {
			fail();
			e.printStackTrace();
		} catch (NegativeAmountException e) {
			fail();
			e.printStackTrace();
		}
    	
    }
}