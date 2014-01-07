package com.acertainbank.tests;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import com.acertainbank.business.CertainAccountManager;
public class CertainAccountManagerTest {
	private static CertainAccountManager accountManager;
	@BeforeClass
    public static void setUpBeforeClass() {
		accountManager = CertainAccountManager.getInstance();
		accountManager.branchId = 1;
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
}