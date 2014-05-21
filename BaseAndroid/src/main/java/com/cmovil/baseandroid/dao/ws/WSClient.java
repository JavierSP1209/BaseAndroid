/**
 * File: WSClient.java
 * CreationDate: 09/19/2013
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description: 
 * 		Class that contains all the common base methods that should be implemented by the WS clients for an application
 */
package com.cmovil.baseandroid.dao.ws;

import android.content.Context;
import android.util.Log;

import com.cmovil.baseandroid.model.ws.MessageErrorCode;
import com.cmovil.baseandroid.util.KeyDictionary;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Class that contains all the common base methods that should be implemented by the WS clients for an application
 *
 * @param <T1>
 * 	Generic type for request message objects
 * @param <T2>
 * 	Generic type for response message objects
 * @author "M. en C. Javier Silva Perez (JSP)"
 * @version 1.0
 * @since 09/19/13
 */
public abstract class WSClient<T1, T2> {

	private static final Integer TIME_OUT = 100000;
	private String service;

	protected Gson gsonBuilder;
	private FieldNamingStrategy fieldNamingStrategy;
	private Context context;


	/**
	 * Default constructor for ws client
	 *
	 * @param serviceURL
	 * 	URL of the service that will be used
	 */
	public WSClient(String serviceURL) {
		service = serviceURL;
	}

	/**
	 * Base constructor initialize necessary controllers or class attributes
	 *
	 * @param context
	 * 	Application context
	 * @param service
	 * 	URL of the service that will be used
	 */
	public WSClient(Context context, String service) {
		this.context = context;
		fieldNamingStrategy = null;
		this.service = service;
	}

	/**
	 * Constructor that contains an specific field naming strategy
	 *
	 * @param context
	 * 	Application context
	 * @param fieldNamingStrategy
	 * 	the actual naming strategy to apply to the fields
	 * @param service
	 * 	URL of the service that will be used
	 */
	public WSClient(Context context, FieldNamingStrategy fieldNamingStrategy, String service) {
		this.service = service;
		this.context = context;
		this.fieldNamingStrategy = fieldNamingStrategy;
	}

	public Context getContext() {
		return context;
	}

	/**
	 * Method that creates the model object that will be serialized and added to the request on the invoke step
	 *
	 * @param methodName
	 * 	Web Service method name that will be invoked.
	 * @param requestParams
	 * 	Object to be used as parameters to invoke que web service
	 * @return A object with its corresponding attributes filled out.
	 */
	protected abstract List<BasicNameValuePair> buildRequestContent(String methodName, T1 requestParams);

	/**
	 * Method that evaluates the web service method name that was invoked. It parses the extracted data and passes the
	 * parsed objects to the adequate method so that the synchronization can be fulfilled.
	 *
	 * @param methodName
	 * 	Web Service method name that was invoked.
	 * @param extractedData
	 * 	String in JSON format that represents the "data" part of the web service response.
	 */
	protected abstract List<T2> manageResponse(String methodName, String extractedData);

	/**
	 * Tries to parse the response as a error, if an exception is thrown means that the response does not correspond to
	 * a error and try to parse it correctly
	 *
	 * @param serverResponse
	 * 	Server JSON response string
	 * @throws ResponseErrorException
	 * 	If an error is detected on the response
	 */
	protected abstract void manageErrors(String serverResponse) throws ResponseErrorException;


	/**
	 * Creates the corresponding request for the selected method and process the response from the server
	 *
	 * @param methodName
	 * 	Name of the method that will be invoked, the available methods are at:
	 * 	{@link com.cmovil.baseandroid.util.KeyDictionary.WSParameters.RequestWebServiceMethods}
	 * @param requestData
	 * 	Object to be used for fill oout request parameters
	 * @throws InvalidResponseException
	 * 	If the server response contains invalid fields or does not full fill the established protocol
	 * @throws java.io.IOException
	 * 	If the connection could not be done due to network problems
	 * @throws com.cmovil.baseandroid.dao.ws.ResponseErrorException
	 * 	If the server response contains an error message
	 */
	public List<T2> invokePOST(String methodName, T1 requestData)
		throws InvalidResponseException, IOException, ResponseErrorException {

		if (fieldNamingStrategy != null) {
			gsonBuilder = new GsonBuilder().setFieldNamingStrategy(fieldNamingStrategy).create();
		} else {
			gsonBuilder = new GsonBuilder().create();
		}

		String jsonServerResponse = makeWSPostRequest(methodName, buildRequestContent(methodName, requestData));

		//Receiving and parsing the response
		if (jsonServerResponse != null) {

			manageErrors(jsonServerResponse);

			try {
				//Send the body to the next method, so it could be parsed depending on the method invoked
				return manageResponse(methodName, jsonServerResponse);
			} catch (JsonSyntaxException ex) {
				throw new InvalidResponseException("Server response invalid or unsupported format");
			}
		} else {
			throw new InvalidResponseException("Server response invalid or unsupported format");
		}
	}

	/**
	 * Creates the corresponding request for the selected method and process the response from the server
	 *
	 * @param methodName
	 * 	Name of the method that will be invoked, the available methods are at:
	 * 	{@link com.cmovil.baseandroid.util.KeyDictionary.WSParameters.RequestWebServiceMethods}
	 * @param jsonRequest
	 * 	Service request parameters in json format
	 * @throws InvalidResponseException
	 * 	If the server response contains invalid fields or does not full fill the established protocol
	 * @throws java.io.IOException
	 * 	If the connection could not be done due to network problems
	 * @throws com.cmovil.baseandroid.dao.ws.ResponseErrorException
	 * 	If the server response contains an error message
	 */
	public List<T2> invokePOST(String methodName, String jsonRequest)
		throws InvalidResponseException, IOException, ResponseErrorException {

		if (fieldNamingStrategy != null) {
			gsonBuilder = new GsonBuilder().setFieldNamingStrategy(fieldNamingStrategy).create();
		} else {
			gsonBuilder = new GsonBuilder().create();
		}

		String jsonServerResponse = makeWSPostRequest(methodName, jsonRequest);
		//Receiving and parsing the response
		if (jsonServerResponse != null) {

			manageErrors(jsonServerResponse);

			try {
				//Send the body to the next method, so it could be parsed depending on the method invoked
				return manageResponse(methodName, jsonServerResponse);
			} catch (JsonSyntaxException ex) {
				throw new InvalidResponseException("Server response invalid or unsupported format");
			}
		} else {
			throw new InvalidResponseException("Server response invalid or unsupported format");
		}
	}

	/**
	 * Prints out the error list
	 *
	 * @param errorCodeList
	 * 	Message error list
	 */
	protected void printErrors(List<MessageErrorCode> errorCodeList) {
		for (MessageErrorCode messageErrorCode : errorCodeList) {
			Log.e(KeyDictionary.TAG, messageErrorCode.toString());
		}
	}


	/**
	 * Creates the corresponding request for the selected method and process the response from the server
	 *
	 * @param methodName
	 * 	Name of the method that will be invoked, the available methods are at:
	 * 	{@link com.cmovil.baseandroid.util.KeyDictionary.WSParameters.RequestWebServiceMethods}
	 * @param data
	 * 	Data to be sent to the server, must be on GET parameters format
	 * @throws InvalidResponseException
	 * 	If the server response contains invalid fields or does not full fill the established protocol
	 * @throws java.io.IOException
	 * 	If the connection could not be done due to network problems
	 * @throws com.cmovil.baseandroid.dao.ws.ResponseErrorException
	 * 	If the server response contains an error message
	 */
	public List<T2> invokeGET(String methodName, String data)
		throws InvalidResponseException, IOException, ResponseErrorException {

		//Make the post request to the server
		String jsonServerResponse = makeWSGetRequest(methodName, data);
		//Receiving and parsing the response
		if (jsonServerResponse != null) {
			manageErrors(jsonServerResponse);

			try {
				//Send the body to the next method, so it could be parsed depending on the method invoked
				return manageResponse(methodName, jsonServerResponse);
			} catch (JsonSyntaxException ex) {
				throw new InvalidResponseException("Server response invalid or unsupported format");
			}
		} else {
			throw new InvalidResponseException("Server response invalid or unsupported format");
		}
	}

	/**
	 * Sets the time out parameters to the http request
	 *
	 * @param establish
	 * 	time out for establish the connection with the server
	 * @param waitData
	 * 	time out for wait for the response of the server
	 * @return An object with the Http time out parameters established
	 */
	protected HttpParams getTimeoutParameters(int establish, int waitData) {
		// ------ Request Set Timeouts
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, establish);
		HttpConnectionParams.setSoTimeout(httpParameters, waitData);
		return httpParameters;
	}

	/**
	 * Makes a WS request through POST to the WS server sending the corresponding data
	 *
	 * @param methodName
	 * 	Name of the method to post, this will be appended to the service URL
	 * @param data
	 * 	JSON object with the corresponding parameters
	 * @return A String encoded in JSON with the server response or null if the response is invalid
	 *
	 * @throws java.io.IOException
	 */
	protected String makeWSPostRequest(String methodName, JSONObject data) throws IOException {
		HttpClient httpClient = new DefaultHttpClient(getTimeoutParameters(TIME_OUT, TIME_OUT));
		HttpPost post = new HttpPost(service + "/" + methodName);
		post.setHeader("content-type", "application/json");

		StringEntity entity = new StringEntity(data.toString(), HTTP.UTF_8);
		post.setEntity(entity);

		HttpResponse response = httpClient.execute(post);

		HttpEntity entityRes = response.getEntity();
		if (entityRes != null) return EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
		else return null;
	}

	/**
	 * Makes a WS request through POST to the WS server sending the corresponding data
	 *
	 * @param methodName
	 * 	Name of the method to post, this will be appended to the service URL
	 * @param data
	 * 	JSON string with the corresponding parameters
	 * @return A String encoded in JSON with the server response or null if the response is invalid
	 *
	 * @throws java.io.IOException
	 */
	protected String makeWSPostRequest(String methodName, String data) throws IOException {
		HttpClient httpClient = new DefaultHttpClient(getTimeoutParameters(TIME_OUT, TIME_OUT));
		HttpPost post = new HttpPost(service + "/" + methodName);
		Log.d(KeyDictionary.TAG, "Sending request to: " + service + "/" + methodName);
		post.setHeader("content-type", "application/json");

		StringEntity entity = new StringEntity(data, HTTP.UTF_8);
		post.setEntity(entity);

		HttpResponse response = httpClient.execute(post);

		HttpEntity entityRes = response.getEntity();
		if (entityRes != null) return EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
		else return null;
	}

	/**
	 * Makes a WS request through POST to the WS server sending the corresponding data using UrlEncoded format for
	 * params
	 *
	 * @param methodName
	 * 	Name of the method to post, this will be appended to the service URL
	 * @param data
	 * 	Object with the parameters for the web service
	 * @return A String encoded in JSON with the server response or null if the response is invalid
	 *
	 * @throws java.io.IOException
	 */
	protected String makeWSPostRequest(String methodName, List<BasicNameValuePair> data) throws IOException {
		HttpClient httpClient = new DefaultHttpClient(getTimeoutParameters(TIME_OUT, TIME_OUT));
		HttpPost post = new HttpPost(service + "/" + methodName);

		AbstractHttpEntity entity;
		entity = new UrlEncodedFormEntity(data, HTTP.UTF_8);
		//Log.d(KeyDictionary.TAG, "Plain Content (URL format): " + entityData);

		post.setEntity(entity);

		HttpResponse response = httpClient.execute(post);

		HttpEntity entityRes = response.getEntity();
		if (entityRes != null) return EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
		else return null;
	}

	/**
	 * Makes a WS request through GET to the WS server sending the corresponding data
	 *
	 * @param methodName
	 * 	Name of the method to get, this will be appended to the service URL
	 * @param data
	 * 	Data to be sent to the server, must be on GET parameters format
	 * @return A string decoded with JSON with the server response
	 *
	 * @throws org.apache.http.client.ClientProtocolException
	 * @throws IOException
	 */
	protected String makeWSGetRequest(String methodName, String data) throws IOException {
		HttpClient httpClient = new DefaultHttpClient(getTimeoutParameters(TIME_OUT, TIME_OUT));
		String url = service + "/" + methodName + "?" + data;
		HttpGet get = new HttpGet(url);

		HttpResponse resp;
		resp = httpClient.execute(get);
		HttpEntity responseEntity = resp.getEntity();
		if (responseEntity == null) return null;
		BufferedReader reader = new BufferedReader(new InputStreamReader(resp.getEntity().getContent(), "UTF-8"));

		//Read the full server answer
		StringBuilder builder = new StringBuilder();
		String aux = "";

		while ((aux = reader.readLine()) != null) {
			builder.append(aux);
		}
		return builder.toString();
	}
}
