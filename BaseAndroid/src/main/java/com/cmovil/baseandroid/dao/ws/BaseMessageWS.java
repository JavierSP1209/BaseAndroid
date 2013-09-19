/**
 * File: BaseMessageWS
 * CreationDate: 09/07/13
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description: 
 *  This class will only be used for create a dummy request object and parse a dummy response in order to test the
 * cryptographic operations and parsers for the base request and response models
 */
package com.cmovil.baseandroid.dao.ws;

import android.content.Context;
import android.util.Log;

import com.cmovil.baseandroid.util.KeyDictionary;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Class that implements a base invoke example to be as generic client class for most common web services clients that
 * uses JSON as application data format
 *
 * @param <T1>
 * 	Generic type for message request object
 * @param <T2>
 * 	Generic type for message response object
 * @author "M. en C. Javier Silva Perez (JSP)"
 * @version 1.0
 * @since 09/07/13
 */
public abstract class BaseMessageWS<T1, T2> extends WSClient<T1, T2> {

	protected static Gson gsonBuilder;
	private FieldNamingStrategy fieldNamingStrategy;
	private Context context;


	/**
	 * Base constructor initialize necessary controllers or class attributes
	 *
	 * @param context
	 * 	Application context
	 */
	public BaseMessageWS(Context context) {
		this.context = context;
		fieldNamingStrategy = null;
	}

	/**
	 * Constructor that contains an specific field naming strategy
	 *
	 * @param context
	 * 	Application context
	 * @param fieldNamingStrategy
	 * 	the actual naming strategy to apply to the fields
	 */
	public BaseMessageWS(Context context, FieldNamingStrategy fieldNamingStrategy) {
		this.context = context;
		this.fieldNamingStrategy = fieldNamingStrategy;
	}

	public Context getContext() {
		return context;
	}

	/**
	 * Creates the corresponding request for the selected method and process the response from the server
	 *
	 * @param methodName
	 * 	Name of the method that will be invoked, the available methods are at:
	 * 	{@link com.cmovil.baseandroid.util.KeyDictionary.WSParameters.RequestWebServiceMethods}
	 * @param requestClassType
	 * 	Class type of the server request
	 * @param responseClassType
	 * 	Class type of the server response
	 * @throws InvalidResponseException
	 * 	If the server response contains invalid fields or does not full fill the established protocol
	 * @throws java.io.IOException
	 * 	If the connection could not be done due to network problems
	 */
	public T2 invoke(String methodName, Type requestClassType, Type responseClassType)
		throws InvalidResponseException, IOException {

		if (fieldNamingStrategy != null) {
			gsonBuilder = new GsonBuilder().setFieldNamingStrategy(fieldNamingStrategy).create();
		} else {
			gsonBuilder = new GsonBuilder().create();
		}

		String jsonRequest = gsonBuilder.toJson(buildContentRequest(methodName));

		Log.d(KeyDictionary.TAG, "Plain Content: " + jsonRequest);

		Gson gson = new Gson();
		//Parse the RequestMessage into JSON format
		String requestMessageJSON = gson.toJson(jsonRequest, requestClassType);
		Log.d(KeyDictionary.TAG, "Request: " + requestMessageJSON);

		//Calling the services that will download and update the data received from the PDA
		String jsonServerResponse = makeWSPostRequest(methodName, requestMessageJSON);
		Log.d(KeyDictionary.TAG, "Original Response:" + jsonServerResponse);

		//Receiving and parsing the response
		if (jsonServerResponse != null) {
			//Send the body to the next method, depending on the "methodName" variable
			return manageResponse(methodName, jsonServerResponse);
		} else {
			throw new InvalidResponseException("Server response invalid or unsupported format");
		}

	}

	/**
	 * Method to know if an Id indicates an empty object
	 *
	 * @param id
	 * 	The searched id
	 * @return True if it exists. Otherwise, False.
	 */
	protected boolean existsInDB(int id) {
		return id < KeyDictionary.EMPTY_OBJECT_ID;
	}
}
