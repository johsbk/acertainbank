package com.acertainbank.tests;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.acertainbank.business.CertainAccountManager;
import com.acertainbank.exceptions.ExistentAccountException;
import com.acertainbank.exceptions.ExistentBranchException;
import com.acertainbank.exceptions.InexistentAccountException;
import com.acertainbank.exceptions.InexistentBranchException;
import com.acertainbank.exceptions.NegativeAmountException;
public class CertainAccountManagerTest {
	private static CertainAccountManager accountManager;
	@BeforeClass
    public static void setUpBeforeClass() {
		accountManager = CertainAccountManager.getInstance();
		
    }
	@Before
    public void setUp() throws Exception {
		accountManager.reset();
    }
    @Test
    public void addAccounts() {
    	try {
			accountManager.addBranch(1);
		} catch (ExistentBranchException e) {
			fail();
		}
    	try {
			accountManager.addAccount(2,1);
			fail();
		} catch (InexistentBranchException e1) {
			assertTrue(true);
		} catch (ExistentAccountException e1) {
			fail();
			e1.printStackTrace();
		}
    	try {
	    	accountManager.addAccount(1,1,5.0);
	    	assertEquals(5.0,accountManager.getAccountBalance(1,1),0.1);
    	} catch(Exception e) {
    		e.printStackTrace();
	    	fail();
    	}
    
	    try {
			accountManager.addAccount(1,1,5.0);
			fail();
		} catch (InexistentBranchException e) {
			fail();
			e.printStackTrace();
		} catch (ExistentAccountException e) {
			assertTrue(true);
		}
    }
    
    @Test
    public void creditAccount() {
    	try {
			accountManager.addBranch(1);
		} catch (ExistentBranchException e) {
			fail();
		}
    	try {
    		accountManager.addAccount(1,1);
    	} catch(Exception e) {
    		e.printStackTrace();
	    	fail("");
    	}
    	try {
			accountManager.credit(1, 1, 5.0);
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
        try {
			accountManager.credit(1, 1, -5.0);
			fail();
		} catch (InexistentBranchException e) {
			fail();
			e.printStackTrace();
		} catch (InexistentAccountException e) {
			fail();
			e.printStackTrace();
		} catch (NegativeAmountException e) {
			assertTrue(true);
		}
    	
    }
    
    @Test
    public void debitAccount() {
    	try {
			accountManager.addBranch(1);
		} catch (ExistentBranchException e) {
			fail();
		}
    	try {
    		accountManager.addAccount(1,1);
    	} catch(Exception e) {
    		e.printStackTrace();
	    	fail();
    	}
    	try {
			accountManager.debit(1, 1, 5.0);
			assertEquals(-5.0,accountManager.getAccountBalance(1,1),0.1);
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
        try {
			accountManager.debit(1, 1, -5.0);
			fail();
		} catch (InexistentBranchException e) {
			fail();
			e.printStackTrace();
		} catch (InexistentAccountException e) {
			fail();
			e.printStackTrace();
		} catch (NegativeAmountException e) {
			assertTrue(true);
		}
    	
    }
    
    @Test
    public void calculateExposure() {
    	try {
			accountManager.addBranch(1);
		} catch (ExistentBranchException e) {
			fail();
		}
    	try {
    		accountManager.addAccount(1,1,-5.);
    	} catch(Exception e) {
    		e.printStackTrace();
	    	fail();
    	}
    	try {
			double exposure = accountManager.calculateExposure(1);
			assertEquals(exposure,5.,0.1);
		} catch (InexistentBranchException e) {
			fail();
			e.printStackTrace();
		}
    }
}