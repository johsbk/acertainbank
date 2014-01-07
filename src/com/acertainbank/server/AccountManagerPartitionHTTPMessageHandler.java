/**
 * 
 */
package com.acertainbank.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.acertainbank.business.AccountManagerPartition;
import com.acertainbank.exceptions.AccountManagerException;
import com.acertainbank.utils.AccountManagerMessageTag;
import com.acertainbank.utils.AccountManagerResponse;
import com.acertainbank.utils.AccountManagerUtility;

/**
 * 
 * MasterBookStoreHTTPMessageHandler implements the message handler class which
 * is invoked to handle messages received by the master book store HTTP server
 * It decodes the HTTP message and invokes the MasterCertainBookStore API
 * 
 */
public class AccountManagerPartitionHTTPMessageHandler extends AbstractHandler {

	public void handle(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		AccountManagerMessageTag messageTag;
		String requestURI;

		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		requestURI = request.getRequestURI();

		messageTag = AccountManagerUtility.convertURItoMessageTag(requestURI);
		// the RequestURI before the switch
		JSONParser parser = new JSONParser();
		if (messageTag == null) {
			System.out.println("Unknown message tag");
		} else {
			switch (messageTag) {

			case CREDIT:
				String jsonString = AccountManagerUtility
						.extractPOSTDataFromRequest(request);
				AccountManagerResponse accountManagerResponse = new AccountManagerResponse();
				try {
					try {
						JSONObject json = (JSONObject) parser.parse(jsonString);
						int branchId = (Integer) json.get("branchId");
						int accountId = (Integer) json.get("accountId");
						double amount = (Double) json.get("amount");
	
						AccountManagerPartition.getInstance().credit(branchId,
								accountId, amount);
					} catch (ParseException e) {
						throw new AccountManagerException(e);
					}
				} catch (AccountManagerException ex) {
					accountManagerResponse.setException(ex);
				}

				response.getWriter()
						.println(
								AccountManagerUtility
										.serializeObjectToXMLString(accountManagerResponse));
				break;

			case DEBIT:
				jsonString = AccountManagerUtility
						.extractPOSTDataFromRequest(request);
				accountManagerResponse = new AccountManagerResponse();
				try {
					try {
						JSONObject json = (JSONObject) parser.parse(jsonString);
						int branchId = (Integer) json.get("branchId");
						int accountId = (Integer) json.get("accountId");
						double amount = (Double) json.get("amount");
	
						AccountManagerPartition.getInstance().debit(branchId,
								accountId, amount);
					} catch (ParseException e) {
						throw new AccountManagerException(e);
					}
					} catch (AccountManagerException ex) {
					accountManagerResponse.setException(ex);
				}

				response.getWriter()
						.println(
								AccountManagerUtility
										.serializeObjectToXMLString(accountManagerResponse));
				break;

			case TRANSFER:
				jsonString = AccountManagerUtility
						.extractPOSTDataFromRequest(request);
				accountManagerResponse = new AccountManagerResponse();
				try {
					try {
						JSONObject json = (JSONObject) parser.parse(jsonString);

						int branchId = (Integer) json.get("branchId");
						int accountIdOrig = (Integer) json.get("accountIdOrig");
						int accountIdDest = (Integer) json.get("accountIdDest");
						double amount = (Double) json.get("amount");

						AccountManagerPartition.getInstance().transfer(
								branchId, accountIdOrig, accountIdDest, amount);
					} catch (ParseException e) {
						throw new AccountManagerException(e);
					}
				} catch (AccountManagerException ex) {
					accountManagerResponse.setException(ex);
				}

				response.getWriter()
						.println(
								AccountManagerUtility
										.serializeObjectToXMLString(accountManagerResponse));
				break;

			case CALCULATE_EXPOSURE:
				jsonString = AccountManagerUtility
						.extractPOSTDataFromRequest(request);
				accountManagerResponse = new AccountManagerResponse();
				try {

					try {
						JSONObject json = (JSONObject) parser.parse(jsonString);

						int branchId = (Integer) json.get("branchId");

						double exposure = AccountManagerPartition.getInstance()
								.calculateExposure(branchId);
						json = new JSONObject();
						json.put("exposure", new Double(exposure));
						accountManagerResponse.setResult(json);
					} catch (ParseException e) {
						throw new AccountManagerException(e);
					}
				} catch (AccountManagerException ex) {
					accountManagerResponse.setException(ex);
				}

				response.getWriter()
						.println(
								AccountManagerUtility
										.serializeObjectToXMLString(accountManagerResponse));
				break;

			default:
				System.out.println("Unhandled message tag");
				break;
			}
		}
		// Mark the request as handled so that the HTTP response can be sent
		baseRequest.setHandled(true);

	}
}
