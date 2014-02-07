/**
 * File: BaseModel.java
 * CreationDate: 19/08/13
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description: 
 *  Public abstract class that should be implemented by all the models of the application
 */
package com.cmovil.baseandroid.model.db;

import com.cmovil.baseandroid.util.KeyDictionary;

/**
 * Public abstract class that should be implemented by all the models of the application
 *
 * @author "M. en C. Javier Silva Perez (JSP)"
 * @version 1.0
 * @since 19/08/13
 */
public abstract class BaseModel {

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
	 * @param dbId Model row id
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

}
