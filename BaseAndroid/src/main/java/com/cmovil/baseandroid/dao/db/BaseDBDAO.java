package com.cmovil.baseandroid.dao.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.cmovil.baseandroid.dao.db.helper.BaseDatabaseOpenHelper;
import com.cmovil.baseandroid.dao.db.helper.DatabaseDictionary;
import com.cmovil.baseandroid.model.db.BaseModel;

import java.util.Map;

/**
 * Base data base helper contains all the base functions for common data base operations like, insert, delete, update
 * getAll and getById
 * @param <T> Catalog class that will be used for perform the data base operations
 * @author "M. en C. Javier Silva Perez (JSP)"
 * @version 1.0
 * @since 29/08/2013
 */
public abstract class BaseDBDAO<T extends BaseModel> {

	protected final BaseDatabaseOpenHelper mDatabaseOpenHelper;
	protected String tableName;

	/**
	 * Constructor
	 *
	 * @param tableName
	 * 	Table name in which this queries will be executed
	 * @param baseDatabaseOpenHelper
	 * 	data base open helper to be used in this class
	 */
	protected BaseDBDAO(String tableName, BaseDatabaseOpenHelper baseDatabaseOpenHelper) {
		this.tableName = tableName;
		mDatabaseOpenHelper = baseDatabaseOpenHelper;
	}

	/**
	 * Fill up a map for the values to be inserted or updated into the data base
	 *
	 * @param insertObject
	 * 	Object to be get the table values
	 * @return A ContentValues object filled up with the corresponding Patient values
	 */
	protected abstract ContentValues fillMapValues(T insertObject) throws DBException;

	/**
	 * Close the data base
	 */
	public void close() {
		mDatabaseOpenHelper.close();
	}

	/**
	 * Returns a Cursor over all base_dictionary in the URI
	 *
	 * @param tableName
	 * 	Table name in which this query will be executed
	 * @param columns
	 * 	The columns to include, if null then all are included
	 * @param projectionMap
	 * 	The projection map maps from column names that the caller passes into query to database column names. This is
	 * 	useful for renaming columns as well as disambiguating column names when doing joins. For example you could map
	 * 	"name" to "people.name". If a projection map is set it must contain all column names the user may request,
	 * 	even if
	 * 	the key and value are the same.
	 * @return Cursor over all words that match, or null if none found.
	 */
	public Cursor getAll(String tableName, String[] columns, Map<String, String> projectionMap) {
		return query(tableName, null, null, columns, projectionMap);
	}

	/**
	 * Performs a database query.
	 *
	 * @param tableName
	 * 	Sets the list of tables to query. Multiple tables can be specified to perform a join. For example: setTables
	 * 	("foo,
	 * 	bar") setTables("foo LEFT OUTER JOIN bar ON (foo.id = bar.foo_id)")
	 * @param selection
	 * 	The selection clause
	 * @param selectionArgs
	 * 	Selection arguments for "?" components in the selection
	 * @param columns
	 * 	The columns to return
	 * @param projectionMap
	 * 	The projection map maps from column names that the caller passes into query to database column names. This is
	 * 	useful for renaming columns as well as disambiguating column names when doing joins. For example you could map
	 * 	"name" to "people.name". If a projection map is set it must contain all column names the user may request,
	 * 	even if
	 * 	the key and value are the same.
	 * @return A Cursor over all rows matching the query
	 */
	protected Cursor query(String tableName, String selection, String[] selectionArgs, String[] columns,
	                     Map<String, String> projectionMap) {
		/*
		 * The SQLiteBuilder provides a map for all possible columns requested
		 * to actual columns in the database, creating a simple column alias
		 * mechanism by which the ContentProvider does not need to know the real
		 * column base_dictionary
		 */
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		builder.setTables(tableName);
		builder.setProjectionMap(projectionMap);

		SQLiteDatabase db = mDatabaseOpenHelper.getReadableDatabase();

		Cursor cursor = builder.query(db, columns, selection, selectionArgs, null, null, null);

		if (cursor == null) {
			db.close();
			return null;
		} else if (!cursor.moveToFirst()) {
			cursor.close();
			db.close();
			close();
			return null;
		}
		db.close();
		return cursor;
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

		try {
			// Gets the data repository in write mode
			SQLiteDatabase db = mDatabaseOpenHelper.getWritableDatabase();

			if (db == null) return -1;
			// Insert the new row, returning the primary key value of the new row
			long newRowId;
			newRowId = db.insertOrThrow(tableName, null, fillMapValues(insertObject));
			db.close();
			return (int) newRowId;
		} catch (SQLException e) {
			throw new DBException(e.getMessage(), e);
		}
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
		try {
			// Gets the data repository in write mode
			SQLiteDatabase db = mDatabaseOpenHelper.getReadableDatabase();
			if (db == null) return 0;
			// Define 'where' part of query.
			String selection = DatabaseDictionary.DBBaseStructure.FILTER_ID;
			// Specify arguments in placeholder order.
			String[] selectionArgs = {String.valueOf(id)};
			// Issue SQL statement.
			Integer res = db.delete(tableName, selection, selectionArgs);
			db.close();
			return res;
		} catch (SQLException e) {
			throw new DBException(e.getMessage(), e);
		}
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
		try {
			SQLiteDatabase db = mDatabaseOpenHelper.getReadableDatabase();

			if (db == null) return 0;
			// Which row to update, based on the ID
			String selection = DatabaseDictionary.DBBaseStructure.FILTER_ID;
			String[] selectionArgs = {String.valueOf(objectToUpdate.getDbId())};

			Integer res = db.update(tableName, fillMapValues(objectToUpdate), selection, selectionArgs);
			db.close();
			return res;
		} catch (SQLException e) {
			throw new DBException(e.getMessage(), e);
		}
	}

	/**
	 * Gets all the objects of the selected table with the selected id
	 *
	 * @param id
	 * 	Id that will be searched in the database
	 * @param columns
	 * 	The columns to include, if null then all are included
	 * @return Cursor positioned to matching word, or null if not found.
	 *
	 * @throws DBException
	 * 	if something goes wrong during SQL statements execution
	 */
	public Cursor getById(Integer id, String[] columns) throws DBException {
		String selection = DatabaseDictionary.DBBaseStructure.FILTER_ID;
		String[] selectionArgs = new String[]{String.valueOf(id)};
		try {
			return query(tableName, selection, selectionArgs, columns, null);
		} catch (SQLException e) {
			throw new DBException(e.getMessage(), e);
		}

		/*
		 * This builds a query that looks like: SELECT <columns> FROM <table>
		 * WHERE rowId = <id>
		 */
	}
}
