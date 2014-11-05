/**
 * File: SampleWS
 * CreationDate: 09/07/13
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description: 
 *  This class will only be used for create a dummy request object and parse a dummy response in order to test the
 * cryptographic operations and parsers for the base request and response models
 */
package com.cmovil.baseandroidtest.dao.ws;

import android.content.Context;

import com.cmovil.baseandroid.dao.ws.ResponseErrorException;
import com.cmovil.baseandroid.dao.ws.WSClient;
import com.cmovil.baseandroid.model.ws.MessageErrorCode;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.LinkedList;
import java.util.List;

/**
 * Class that implements a base invoke example to be as generic client class for most common web services clients that
 * uses JSON as application data format
 *
 * @param <T1>
 * 	Generic type for request message objects
 * @param <T2>
 * 	Generic type for message response object
 * @author "M. en C. Javier Silva Perez (JSP)"
 * @version 1.0
 * @since 09/07/13
 */
public abstract class SampleWS<T1, T2> extends WSClient<T1, T2> {

	/**
	 * Default constructor for ws client
	 *
	 * @param service
	 * 	URL of the service that will be used
	 */
	public SampleWS(String service) {
		super(service);
	}

	/**
	 * Tries to parse the response as a error, if an exception is thrown means that the response does not correspond to
	 * a error and try to parse it correctly
	 *
	 * @param serverResponse
	 * 	Server JSON response string
	 * @throws com.cmovil.baseandroid.dao.ws.ResponseErrorException
	 * 	If an error is detected on the response
	 */
	protected void manageErrors(String serverResponse) throws ResponseErrorException {
		try {
			Gson gson = new Gson();
			MessageErrorCode errorCode = gson.fromJson(serverResponse, MessageErrorCode.class);
			if (errorCode.getCode() != null && errorCode.getCode() != 1) {

				List<MessageErrorCode> errorCodeList = new LinkedList<MessageErrorCode>();
				errorCodeList.add(errorCode);
				printErrors(errorCodeList);

				throw new ResponseErrorException(errorCodeList);
			}
		} catch (JsonSyntaxException ex) {
		}
	}
}
