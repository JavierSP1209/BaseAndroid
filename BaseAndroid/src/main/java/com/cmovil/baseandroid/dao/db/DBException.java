/**
 * File: DBException
 * CreationDate: $(DATE)
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description: 
 *
 */
package com.cmovil.baseandroid.dao.db;

import android.database.SQLException;

/**
 * Custom exception for database errors
 *
 * @author "M. en C. Javier Silva Perez (JSP)"
 * @version 1.0
 * @since 28/05/13
 */
public class DBException extends Exception {

	SQLException dbError;

	/**
	 * Constructs a new {@code Exception} that includes the current stack trace.
	 */
	public DBException() {
	}

	/**
	 * Constructs a new {@code Exception} with the current stack trace and the specified detail message.
	 *
	 * @param detailMessage
	 * 	the detail message for this exception.
	 */
	public DBException(String detailMessage) {
		super(detailMessage);
	}

	/**
	 * Constructs a new {@code Exception} with the current stack trace, the specified detail message and the specified
	 * cause.
	 *
	 * @param detailMessage
	 * 	the detail message for this exception.
	 * @param throwable
	 * 	the cause of this exception.
	 */
	public DBException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	/**
	 * Constructs a new {@code Exception} with the current stack trace and the specified cause.
	 *
	 * @param throwable
	 * 	the cause of this exception.
	 */
	public DBException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * Constructs a new {@code Exception} that includes the current stack trace.
	 *
	 * @param dbError
	 * 	{@code SQLException} SQL exception
	 */
	public DBException(SQLException dbError) {
		this.dbError = dbError;
	}

	/**
	 * Constructs a new {@code Exception} with the current stack trace and the specified detail message.
	 *
	 * @param detailMessage
	 * 	the detail message for this exception.
	 * @param dbError
	 * 	{@code SQLException} SQL exception
	 */
	public DBException(String detailMessage, SQLException dbError) {
		super(detailMessage);
		this.dbError = dbError;
	}
}
