/**
 * File: BaseModel.java
 * CreationDate: 19/08/13
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description: 
 *  Public abstract class that should be implemented by all the models of the application
 */
package com.cmovil.baseandroid.model.db;

/**
 * Public abstract class that should be implemented by all the models of the application
 *
 * @author "M. en C. Javier Silva Perez (JSP)"
 * @version 1.0
 * @since 19/08/13
 */
public interface BaseModel {

	/**
	 * Return the description that will be shown in the view for these element
	 *
	 * @return A string that will be shown in the UI
	 */
	public abstract String getShownDescription();

	public Integer getId();

}
