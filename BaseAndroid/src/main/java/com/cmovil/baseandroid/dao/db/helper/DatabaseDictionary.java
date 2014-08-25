/**
 * File: DatabaseDictionary
 * CreationDate: 28/05/13
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description: 
 *  Class which contains static values for all the Data base base_dictionary needed across the application,
 *  this values could
 * include database name, tables base_dictionary, attributes base_dictionary, etc.
 */
package com.cmovil.baseandroid.dao.db.helper;

import android.provider.BaseColumns;

/**
 * Class which contains static values for all the Data base base_dictionary needed across the application, this values
 * could include database name, tables base_dictionary, attributes base_dictionary, etc.
 *
 * @author "M. en C. Javier Silva Perez (JSP)"
 * @version 1.0
 * @since 28/05/13
 */
public class DatabaseDictionary {

	/**
	 * Prevents the DatabaseDictionary class from being instantiated.
	 */
	private DatabaseDictionary() {
	}

	/**
	 * Base SQL Insert statement
	 */
	public static final String SQL_INSERT = "INSERT INTO ";

	/**
	 * Base com.cmovil.baseandroid.dao.db structure which contains filter id value
	 */
	public interface DBBaseStructure extends BaseColumns {
		public static final String FILTER_ID = _ID + " = ?";
	}
}
