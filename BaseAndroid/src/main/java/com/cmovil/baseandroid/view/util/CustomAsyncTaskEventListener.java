package com.cmovil.baseandroid.view.util;

/**
 * Public abstract class that should be implemented for customize both {@link android.os.AsyncTask#onPreExecute()} and
 * {@link android.os.AsyncTask#onPostExecute(Object)} functions of an async task, this will allow the calling class to
 * manage its results depending on its needs
 *
 * @param <T>
 * 	Type of parameter used on {@link android.os.AsyncTask#onPostExecute(Object)}
 * @author "M. en C. Javier Silva Perez (JSP)"
 * @version 2.0
 * @since 21/01/2014
 */
public abstract class CustomAsyncTaskEventListener<T> {


	/**
	 * In case that the task result is null and an exception its raised while execution
	 * {@link android.os.AsyncTask#doInBackground(Object[])}
	 */
	private Exception errorCause;

	/**
	 * @return The exception that causes the null result, or an null otherwise
	 */
	public Exception getErrorCause() {
		return errorCause;
	}

	public void setErrorCause(Exception errorCause) {
		this.errorCause = errorCause;
	}

	/**
	 * Function called after doInBackground with the resulting bitmap
	 *
	 * @param result
	 * 	Downloaded bitmap, if an error occurs during download or the
	 * 	stream wasn't a bitmap null will be send
	 */
	public abstract void onPostExecute(T result);

	/**
	 * To be called during {@link android.os.AsyncTask#onPreExecute()} function
	 */
	public abstract void onPreExecute();
}
