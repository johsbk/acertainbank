/**
 * 
 */
package com.acertainbank.client;


import java.util.HashMap;
import java.util.List;

import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.io.Buffer;
import org.eclipse.jetty.io.ByteArrayBuffer;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.json.simple.JSONObject;

import com.acertainbank.exceptions.InexistentAccountException;
import com.acertainbank.exceptions.InexistentBranchException;
import com.acertainbank.exceptions.NegativeAmountException;
import com.acertainbank.interfaces.AccountManager;
import com.acertainbank.utils.AccountManagerMessageTag;
import com.acertainbank.utils.AccountManagerUtility;
import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;

/**
 * BookStoreHTTPProxy implements the client level synchronous CertainBookStore
 * API declared in the BookStore class
 * 
 */
public class AccountManagerHTTPProxy implements AccountManager {
    protected HttpClient client;
    protected HashMap<Integer, String> serverAddresses;

    /**
     * Initialize the client object
     */
    public AccountManagerHTTPProxy(HashMap<Integer, String> serverAddresses) throws Exception {
	setServerAddresses(serverAddresses);
	client = new HttpClient();
	client.setConnectorType(HttpClient.CONNECTOR_SELECT_CHANNEL);
	client.setMaxConnectionsPerAddress(AccountManagerClientConstants.CLIENT_MAX_CONNECTION_ADDRESS); // max
												    // concurrent
												    // connections
												    // to
												    // every
												    // address
	client.setThreadPool(new QueuedThreadPool(
		AccountManagerClientConstants.CLIENT_MAX_THREADSPOOL_THREADS)); // max
									   // threads
	client.setTimeout(AccountManagerClientConstants.CLIENT_MAX_TIMEOUT_MILLISECS); // seconds
										  // timeout;
										  // if
										  // no
										  // server
										  // reply,
										  // the
										  // request
										  // expires
	client.start();
    }

    public String getServerAddress(int branchId) throws InexistentBranchException {
    	Integer key =new Integer(branchId);
    	if (!serverAddresses.containsKey(key)) throw new InexistentBranchException(branchId);
    	return serverAddresses.get(key);
    }

    public void setServerAddresses(HashMap<Integer, String> serverAddresses) {
    	this.serverAddresses = serverAddresses;
    }


    public void stop() {
		try {
		    client.stop();
		} catch (Exception e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
    }

	@Override
	public void credit(int branchId, int accountId, double amount)
			throws InexistentBranchException, InexistentAccountException,
			NegativeAmountException {
		ContentExchange exchange = new ContentExchange();
		String urlString = getServerAddress(branchId) + "/" + AccountManagerMessageTag.CREDIT;
		JSONObject json = new JSONObject();
		json.put("branchId", new Integer(branchId));
		json.put("accountId", new Integer(accountId));
		json.put("amount", new Double(amount));

		exchange.setMethod("POST");
		exchange.setURL(urlString);
		Buffer requestContent = new ByteArrayBuffer(json.toJSONString());
		exchange.setRequestContent(requestContent);
		
		try {
			AccountManagerUtility.SendAndRecv(this.client, exchange);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void debit(int branchId, int accountId, double amount)
			throws InexistentBranchException, InexistentAccountException,
			NegativeAmountException {
		ContentExchange exchange = new ContentExchange();
		String urlString = getServerAddress(branchId) + "/" + AccountManagerMessageTag.DEBIT;
		JSONObject json = new JSONObject();
		json.put("branchId", new Integer(branchId));
		json.put("accountId", new Integer(accountId));
		json.put("amount", new Double(amount));

		exchange.setMethod("POST");
		exchange.setURL(urlString);
		Buffer requestContent = new ByteArrayBuffer(json.toJSONString());
		exchange.setRequestContent(requestContent);
		
		try {
			AccountManagerUtility.SendAndRecv(this.client, exchange);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void transfer(int branchId, int accountIdOrig, int accountIdDest,
			double amount) throws InexistentBranchException,
			InexistentAccountException, NegativeAmountException {
		ContentExchange exchange = new ContentExchange();
		String urlString = getServerAddress(branchId) + "/" + AccountManagerMessageTag.TRANSFER;
		JSONObject json = new JSONObject();
		json.put("branchId", new Integer(branchId));
		json.put("accountIdOrig", new Integer(accountIdOrig));
		json.put("accountIdDest", new Integer(accountIdDest));
		json.put("amount", new Double(amount));

		exchange.setMethod("POST");
		exchange.setURL(urlString);
		Buffer requestContent = new ByteArrayBuffer(json.toJSONString());
		exchange.setRequestContent(requestContent);
		
		try {
			AccountManagerUtility.SendAndRecv(this.client, exchange);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public double calculateExposure(int branchId)
			throws InexistentBranchException {
		ContentExchange exchange = new ContentExchange();
		String urlString = getServerAddress(branchId) + "/" + AccountManagerMessageTag.CALCULATE_EXPOSURE;
		JSONObject json = new JSONObject();
		json.put("branchId", new Integer(branchId));

		exchange.setMethod("POST");
		exchange.setURL(urlString);
		Buffer requestContent = new ByteArrayBuffer(json.toJSONString());
		exchange.setRequestContent(requestContent);
		
		try {
			JSONObject result = AccountManagerUtility.SendAndRecv(this.client, exchange);
			return (Double) result.get("exposure");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

}
