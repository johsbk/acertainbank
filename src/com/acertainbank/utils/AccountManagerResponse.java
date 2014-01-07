package com.acertainbank.utils;

import org.json.simple.JSONObject;

import com.acertainbank.exceptions.AccountManagerException;

/**
 * 
 * Data Structure that we use to communicate objects and error messages from the
 * server to the client.
 * 
 */
public class AccountManagerResponse {
	private AccountManagerException exception = null;
	private JSONObject result = null;

	public AccountManagerResponse() {

	}

	public AccountManagerResponse(AccountManagerException exception,
			JSONObject result) {		
		this.setException(exception);
		this.setResult(result);
	}

	public AccountManagerException getException() {
		return exception;
	}

	public void setException(AccountManagerException exception) {
		this.exception = exception;
	}

	public JSONObject getResult() {
		return result;
	}

	public void setResult(JSONObject result) {
		this.result = result;
	}

}
