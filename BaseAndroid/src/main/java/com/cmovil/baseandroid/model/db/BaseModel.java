/**
 * File: BaseModel.java
 * CreationDate: 19/08/13
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description: 
 *  Public abstract class that should be implemented by all the models of the application
 */
package com.cmovil.baseandroid.model.db;

import com.cmovil.baseandroid.util.KeyDictionary;

import java.io.Serializable;

/**
 * Public abstract class that should be implemented by all the models of the application
 *
 * @author "M. en C. Javier Silva Perez (JSP)"
 * @version 1.0
 * @since 19/08/13
 */
public abstract class BaseModel implements Serializable {
	protected Integer dbId;

	/**
	 * Initialize attributes with default values
	 */
	public BaseModel() {
		dbId = KeyDictionary.EMPTY_OBJECT_ID;
	}

	/**
	 * Base constructor with id
	 *
	 * @param dbId
	 * 	Model row id
	 */
	protected BaseModel(Integer dbId) {
		this.dbId = dbId;
	}

	/**
	 * Return the description that will be shown in the view for these element
	 *
	 * @return A string that will be shown in the UI
	 */
	public abstract String getShownDescription();

	public Integer getDbId() {
		return dbId;
	}

	public void setDbId(Integer dbId) {
		this.dbId = dbId;
	}

	/**
	 * Return a string array with the table primary key values, by default this method returns a string array with the
	 * {@link #dbId} field, if the primary key is an other field or is a composite this method should be redefined by
	 * the corresponding model, the order on which the fields are added must be the same as defined on
	 * {@link com.cmovil.baseandroid.dao.db.BaseDBDAO#getPrimaryKeyFilter()}
	 *
	 * @return The string args array to be used on a sql prepared statement
	 */
	public String[] getPrimaryKeySelectionArgs() {
		return new String[]{String.valueOf(dbId)};
	}
}
