/**
 * File: Copyable
 * CreationDate: 23/10/14
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description: 
 *
 */
package com.cmovil.baseandroid.model.db;

/**
 * Interface for models that should create a copy from themselves
 *
 * @author "M. en C. Javier Silva Perez (JSP)"
 * @version 1.0
 * @since 23/10/14
 */
public interface Copyable<T extends BaseModel> {

	/**
	 * Creates a new object with the same value from this
	 *
	 * @return A new instance of the object
	 */
	public T copy();
}
