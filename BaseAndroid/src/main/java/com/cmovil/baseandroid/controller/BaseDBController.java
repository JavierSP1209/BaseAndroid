/**
 * File: BaseDBController
 * CreationDate: 31/07/13
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description: 
 *
 * Class that will be the connection between database and view classes, so it will include functions for inserting,
 * deleting, updating and getting rows in the data base, but will use DAO objects for getting and returning data
 */
package com.cmovil.baseandroid.controller;

import android.database.Cursor;
import android.util.Log;

import com.cmovil.baseandroid.dao.db.BaseDBDAO;
import com.cmovil.baseandroid.dao.db.DBException;
import com.cmovil.baseandroid.model.db.BaseModel;
import com.cmovil.baseandroid.util.KeyDictionary;

import java.lang.reflect.ParameterizedType;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Class that will be the connection between database and view classes, so it will include generic functions for
 * inserting,
 * deleting, updating and getting rows in the data base, but will use DAO objects for getting and returning data
 *
 * @author "M. en C. Javier Silva Perez (JSP)"
 * @version 1.0
 * @since 19/09/13
 */
public abstract class BaseDBController<T extends BaseModel> {

	BaseDBDAO<T> baseDBDAO;


	protected BaseDBDAO<T> getBaseDBDAO() {
		return baseDBDAO;
	}

	/**
	 * Constructor
	 *
	 * @param baseDBDAO
	 * 	Data base dao to be used in this controller
	 */
	protected BaseDBController(BaseDBDAO<T> baseDBDAO) {
		this.baseDBDAO = baseDBDAO;
	}

	/**
	 * Columns that will be returned by the sql statements
	 *
	 * @return A string array with the columns that will be returned in all sql statements
	 */
	protected abstract String[] getColumns();

	/**
	 * Projection map that will be used by default on the query statement, by default the projection map will be null
	 *
	 * @return The projection map maps from column names that the caller passes into query to database column names.
	 * This is useful for renaming columns as well as disambiguating column names when doing joins. For example you
	 * could map "name" to "people.name". If a projection map is set it must contain all column names the user may
	 * request, even if the key and value are the same.
	 */
	protected Map<String, String> getProjectionMap() {
		return null;
	}

	/**
	 * Insert an object to the data base
	 *
	 * @param insertObject
	 * 	Object to be get the values to insert
	 * @return The id of the inserted row, or -1 if an error occurs while opening the data base
	 *
	 * @throws com.cmovil.baseandroid.dao.db.DBException
	 * 	if something goes wrong during SQL statements execution
	 */
	public Integer insert(T insertObject) throws DBException {
		return baseDBDAO.insert(insertObject);
	}

	/**
	 * Insert a list of object to the data base, this method must be used for performance, in other cases use the base
	 * insert one
	 *
	 * @param insertObjects
	 * 	List of objects to insert
	 * @throws com.cmovil.baseandroid.dao.db.DBException
	 * 	if something goes wrong during SQL statements execution
	 */
	public void insert(List<T> insertObjects) throws DBException {
		baseDBDAO.insert(insertObjects);
	}

	/**
	 * Delete an specific row from the selected table in database
	 *
	 * @param id
	 * 	Database id to be deleted from the data base table
	 * @return the number of rows affected, 0 otherwise. To remove all rows and get a
	 * count pass "1" as the whereClause.
	 *
	 * @throws DBException
	 * 	if something goes wrong during SQL statements execution
	 */
	public Integer delete(Integer id) throws DBException {
		return baseDBDAO.delete(id);
	}

	/**
	 * Delete all the rows from the selected table in database
	 *
	 * @return the number of rows affected, 0 otherwise.
	 *
	 * @throws DBException
	 * 	if something goes wrong during SQL statements execution
	 */
	public Integer delete() throws DBException {
		return baseDBDAO.delete();
	}

	/**
	 * Updates all the fields of the object, this function will update all the columns of the row,
	 * so be sure to set the correct values to it
	 *
	 * @param objectToUpdate
	 * 	Object with the all the values to update
	 * @return the number of rows affected, 0 otherwise. To remove all rows and get a
	 * count pass "1" as the whereClause.
	 *
	 * @throws DBException
	 * 	if something goes wrong during SQL statements execution
	 */
	public Integer update(T objectToUpdate) throws DBException {
		return baseDBDAO.update(objectToUpdate);
	}

	/**
	 * Fill up an object with cursor values, the cursor must be valid or exceptions could be thrown
	 *
	 * @param cursor
	 * 	Valid cursor for extract object information
	 * @return A State object fill up with cursor information
	 *
	 * @throws DBException
	 * 	if something goes wrong during SQL statements execution
	 */
	public abstract T fillUpObject(Cursor cursor) throws DBException;

	/**
	 * Gets the object with the selected id
	 *
	 * @param id
	 * 	Id that will be searched in the database
	 * @return An object with the values from the database or an empty
	 * object
	 * if no results where found
	 *
	 * @throws com.cmovil.baseandroid.dao.db.DBException
	 * 	if something goes wrong during SQL statements execution
	 */
	public T getById(Integer id) throws DBException {

		Cursor res = baseDBDAO.getById(id, getColumns(), getProjectionMap());
		//If the cursor has at least one element, create the corresponding State object, if not,
		// return an empty object
		if (res != null && res.moveToFirst()) {
			T object = fillUpObject(res);
			res.close();
			return object;

		}
		if (res != null) {
			res.close();
		}

		try {
			//Return an empty instance of the object using the no-parameters constructor
			return (T) ((Class) ((ParameterizedType) this.getClass().getGenericSuperclass())
				.getActualTypeArguments()[0]).newInstance();
		} catch (InstantiationException e) {
			Log.e(KeyDictionary.TAG, "Return error");
		} catch (IllegalAccessException e) {
			Log.e(KeyDictionary.TAG, "Return error");
		}
		return null;

	}

	/**
	 * @return A list of all the objects of the desired table
	 *
	 * @throws com.cmovil.baseandroid.dao.db.DBException
	 * 	if something goes wrong during SQL statements execution
	 */
	@Deprecated
	public List<T> getAll(String tableName) throws DBException {
		Cursor cursor = baseDBDAO.getAll(tableName, getColumns(), getProjectionMap());
		return processGetAll(cursor);
	}

	/**
	 * @return A list of all the objects of the desired table
	 *
	 * @throws com.cmovil.baseandroid.dao.db.DBException
	 * 	if something goes wrong during SQL statements execution
	 */
	public List<T> getAll() throws DBException {
		Cursor cursor = baseDBDAO.getAll(getColumns(), getProjectionMap());
		return processGetAll(cursor);
	}

	/**
	 * Process the cursor returned
	 *
	 * @param cursor
	 * 	the cursor to be used
	 * @return The resulting list for the cursor
	 *
	 * @throws DBException
	 */
	private List<T> processGetAll(Cursor cursor) throws DBException {
		List<T> res = new LinkedList<T>();
		//If the cursor has at least one element, create the corresponding State object, if not,
		// return an empty object
		if (cursor != null && cursor.moveToFirst()) {
			do {
				T t = fillUpObject(cursor);
				res.add(t);
			} while (cursor.moveToNext());
			cursor.close();
		}
		if (cursor != null) {
			cursor.close();
		}
		return res;
	}
}
