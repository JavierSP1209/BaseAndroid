/**
 * File: WSClient.java
 * CreationDate: 09/19/2013
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description: 
 * 		Class that contains all the common base methods that should be implemented by the WS clients for an application
 */
package com.cmovil.baseandroid.dao.ws;

import android.util.Log;

import com.cmovil.baseandroid.util.KeyDictionary;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Class that contains all the common base methods that should be implemented by the WS clients for an application
 *
 * @param <T1> Generic type for message request object
 * @param <T2> Generic type for message response object
 * @author "M. en C. Javier Silva Perez (JSP)"
 * @version 1.0
 * @since 09/19/13
 */

public abstract class WSClient<T1, T2> {

	protected static final String SERVICE = "http://www.plataformadigitalamanece.org.mx:81/amaneceRest.svc";
	private static final Integer TIME_OUT = 100000;

	/**
	 * Method that creates the model object that will be serialized and added as request on the invoke step
	 *
	 * @param methodName
	 * 	Web Service method name that will be invoked.
	 * @return A object with its corresponding attributes filled out.
	 */
	protected abstract T1 buildContentRequest(String methodName);

	/**
	 * Method that evaluates the web service method name that was invoked. It parses the extracted data and passes the
	 * parsed objects to the adequate method so that the synchronization can be fulfilled.
	 *
	 * @param methodName
	 * 	Web Service method name that was invoked.
	 * @param extractedData
	 * 	String in JSON format that represents the "data" part of the web service response.
	 */
	protected abstract T2 manageResponse(String methodName, String extractedData);

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
	public abstract T2 invoke(String methodName, Type requestClassType, Type responseClassType)
		throws InvalidResponseException, IOException;

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
	 * @param data
	 * 	JSON object with the corresponding parameters
	 * @return A String encoded in JSON with the server response or null if the response is invalid
	 *
	 * @throws java.io.IOException
	 */
	protected String makeWSPostRequest(JSONObject data) throws IOException {
		HttpClient httpClient = new DefaultHttpClient(getTimeoutParameters(TIME_OUT, TIME_OUT));
		HttpPost post = new HttpPost(SERVICE);
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
		HttpPost post = new HttpPost(SERVICE + "/" + methodName);
		Log.d(KeyDictionary.TAG, "Sending request to: " + SERVICE + "/" + methodName);
		post.setHeader("content-type", "application/json");

		StringEntity entity = new StringEntity(data, HTTP.UTF_8);
		post.setEntity(entity);

		HttpResponse response = httpClient.execute(post);

		HttpEntity entityRes = response.getEntity();
		if (entityRes != null) return EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
		else return null;
	}
}
