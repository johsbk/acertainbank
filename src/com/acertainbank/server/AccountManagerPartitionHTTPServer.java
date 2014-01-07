 /**
 * 
 */
package com.acertainbank.server;


/**
 * Starts the master bookstore HTTP server.
 */
public class AccountManagerPartitionHTTPServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AccountManagerPartitionHTTPMessageHandler handler = new AccountManagerPartitionHTTPMessageHandler();
		if (AccountManagerHTTPServerUtility.createServer(8081, handler)) {
			;
		}
	}

}
