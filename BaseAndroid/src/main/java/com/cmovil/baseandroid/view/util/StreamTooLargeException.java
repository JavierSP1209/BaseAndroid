/**
 * File: StreamTooLargeException
 * CreationDate: 21/01/14
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description: 
 *
 */
package com.cmovil.baseandroid.view.util;

import android.annotation.TargetApi;
import android.os.Build;

import java.io.IOException;

/**
 * Special kind of exception raised when a Stream is too big to be decoded by the
 * {@link com.cmovil.baseandroid.view.util.BitmapDecoderTask}
 *
 * @author "M. en C. Javier Silva Perez (JSP)"
 * @version 1.0
 * @since 21/01/14
 */
public class StreamTooLargeException extends IOException {

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	/**
	 * Constructs a new instance of this class with detail message and cause
	 * filled in.
	 *
	 * @param message
	 * 	The detail message for the exception.
	 * @param cause
	 * 	The detail cause for the exception.
	 * @since 1.6
	 */
	public StreamTooLargeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new {@code IOException} with its stack trace and detail
	 * message filled in.
	 *
	 * @param detailMessage
	 * 	the detail message for this exception.
	 */
	public StreamTooLargeException(String detailMessage) {
		super(detailMessage);
	}


}
