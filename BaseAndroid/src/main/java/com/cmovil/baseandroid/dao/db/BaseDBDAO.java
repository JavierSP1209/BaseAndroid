package com.cmovil.baseandroid.dao.db;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Build;

import com.cmovil.baseandroid.dao.db.helper.BaseDatabaseOpenHelper;
import com.cmovil.baseandroid.dao.db.helper.DatabaseDictionary;
import com.cmovil.baseandroid.model.db.BaseModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Base data base helper contains all the base functions for common data base operations like, insert, delete, update
 * getAll and getById
 *
 * @param <T>
 * 	Catalog class that will be used for perform the data base operations
 * @author "M. en C. Javier Silva Perez (JSP)"
 * @version 1.0
 * @since 29/08/2013
 */
public abstract class BaseDBDAO<T extends BaseModel> {

	/**
	 * Maximum SQLite query params
	 */
	public static final int MAX_QUERY_PARAMS = 999;
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
	 * Gets the default table join string
	 *
	 * @return A string with all the necessary joins for one table
	 */
	protected String getDefaultTableJoin() {
		return tableName;
	}

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
	@Deprecated
	public Cursor getAll(String tableName, String[] columns, Map<String, String> projectionMap) {
		return query(tableName, null, null, columns, projectionMap);
	}

	/**
	 * Returns a Cursor over all base_dictionary in the URI
	 *
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
	public Cursor getAll(String[] columns, Map<String, String> projectionMap) {
		return query(getDefaultTableJoin(), null, null, columns, projectionMap);
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
		return query(tableName, selection, selectionArgs, columns, projectionMap, null, null, null, null);
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
	 * @param groupBy
	 * 	A filter declaring how to group rows, formatted as an SQL GROUP BY clause (excluding the GROUP BY itself).
	 * 	Passing null will cause the rows to not be grouped.
	 * @param having
	 * 	A filter declare which row groups to include in the cursor, if row grouping is being used, formatted as an SQL
	 * 	HAVING clause (excluding the HAVING itself). Passing null will cause all row groups to be included, and is
	 * 	required when row grouping is not being used.
	 * @param sortOrder
	 * 	How to order the rows, formatted as an SQL ORDER BY clause (excluding the ORDER BY itself). Passing null will
	 * 	use the default sort order, which may be unordered.
	 * @param limit
	 * 	Limits the number of rows returned by the query, formatted as LIMIT clause. Passing null denotes no LIMIT
	 * 	clause.
	 * @return A Cursor over all rows matching the query
	 */
	protected Cursor query(String tableName, String selection, String[] selectionArgs, String[] columns,
	                       Map<String, String> projectionMap, String groupBy, String having, String sortOrder,
	                       String limit) {
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

		Cursor cursor = builder.query(db, columns, selection, selectionArgs, groupBy, having, sortOrder, limit);

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
	 * Insert a list of object to the data base, this method must be used for performance, in other cases use the base
	 * insert one
	 *
	 * @param insertObjects
	 * 	List of objects to insert
	 * @throws com.cmovil.baseandroid.dao.db.DBException
	 * 	if something goes wrong during SQL statements execution
	 */
	public void insert(List<T> insertObjects) throws DBException {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			insertV16(insertObjects);
		} else {
			insertV8(insertObjects);
		}
	}


	/**
	 * Insert a list of object to the data base, this method should be used prior API 16, due to SQLite version
	 *
	 * @param insertObjects
	 * 	List of objects to insert
	 * @throws com.cmovil.baseandroid.dao.db.DBException
	 * 	if something goes wrong during SQL statements execution
	 */
	private void insertV8(List<T> insertObjects) throws DBException {

		// Gets the data repository in write mode
		SQLiteDatabase db = mDatabaseOpenHelper.getWritableDatabase();

		db.acquireReference();
		try {
			Object[] bindArgs;
			if (insertObjects != null && !insertObjects.isEmpty()) {

				//Add ? as many insert objects are on the movement list, this can pre-processed because  all the
				// inserts will have the same  parameters

				//Get a sample object in order to get the content values to insert
				ContentValues initialValues = fillMapValues(insertObjects.get(0));
				StringBuilder sql = new StringBuilder();
				String params;

				//Create a simple ? param string in order to reuse it on each insert statement
				int size = (initialValues != null && initialValues.size() > 0) ? initialValues.size() : 0;
				for (int i = 0; i < size; i++) {
					sql.append((i > 0) ? ",?" : "?");
				}
				params = sql.toString();

				for (T anInsertObject : insertObjects) {
					sql = new StringBuilder();
					initialValues = fillMapValues(anInsertObject);
					//Create base insert statement
					sql.append(DatabaseDictionary.SQL_INSERT);
					sql.append(tableName);
					sql.append(" (");

					bindArgs = new Object[size];

					int i = 0;
					Set<Map.Entry<String, Object>> valueSet = initialValues.valueSet();
					for (Map.Entry<String, Object> entry : valueSet) {
						String colName = entry.getKey();
						sql.append((i > 0) ? "," : "");
						sql.append(colName);
						bindArgs[i++] = entry.getValue();
					}
					sql.append(')');
					sql.append(" VALUES (");
					sql.append(params);

					sql.append(')');

					db.execSQL(sql.toString(), bindArgs);
				}

				db.close();
			}
		} catch (SQLException e) {
			throw new DBException(e.getMessage(), e);
		} finally {
			db.releaseReference();
		}
	}

	/**
	 * Insert a list of object to the data base, this method must be used for performance, in other cases use the base
	 * insert one, this method should be used post API 16 due to SQLite version
	 *
	 * @param insertObjects
	 * 	* 	List of objects to insert
	 * @throws com.cmovil.baseandroid.dao.db.DBException
	 * 	if something goes wrong during SQL statements execution
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void insertV16(List<T> insertObjects) throws DBException {

		// Gets the data repository in write mode
		SQLiteDatabase db = mDatabaseOpenHelper.getWritableDatabase();

		db.acquireReference();
		try {
			Object[] bindArgs;
			if (insertObjects != null && !insertObjects.isEmpty()) {
				//Pre-process insert query, in order to optimize query construction
				//Get a sample object in order to get the content values to insert
				ContentValues initialValues = fillMapValues(insertObjects.get(0));
				StringBuilder sql = new StringBuilder();
				String params;

				//Create a simple ?-param string in order to reuse it on each insert statement
				int size = (initialValues != null && initialValues.size() > 0) ? initialValues.size() : 0;
				//If the map has at least one column
				if (size > 0) {
					//Add ? as many insert objects are on the movement list
					for (int i = 0; i < size; i++) {
						sql.append((i > 0) ? ",?" : "?");
					}
					//Save params string
					params = sql.toString();
					//Get the maximum number of batch inserts that can be used, since the max number of SQL parameters
					// are 999
					int maxInsertCount = MAX_QUERY_PARAMS / size;
					//Get the remaining number of insert operations
					int remainingInserts = insertObjects.size();

					//Use an iterator to go over the object list
					Iterator<T> iterator = insertObjects.iterator();
					while (iterator.hasNext()) {

						//Get the number of inserts for the next batch operation, it could be the max number of
						// inserts or the remaining one (for the last iteration)
						int insertCount = remainingInserts >= maxInsertCount ? maxInsertCount : remainingInserts;

						//Initialize insert query
						sql = new StringBuilder();
						createBaseInsert(sql, initialValues);
						bindArgs = new Object[size * insertCount];

						//Add the corresponding number of insert parameters and bindArgs
						int i = 0;
						for (int j = 0; j < insertCount; j++) {
							T element = iterator.next();
							initialValues = fillMapValues(element);

							for (String columnName : initialValues.keySet()) {
								bindArgs[i++] = initialValues.get(columnName);
							}
							//Add the ? to the insert
							sql.append(j == 0 ? "(" : ",(");
							sql.append(params);
							sql.append(')');
						}
						//Execute batch insert and update remaining insert count
						db.execSQL(sql.toString(), bindArgs);
						remainingInserts -= insertCount;
					}
				}

				db.close();
			}
		} catch (SQLException e) {
			throw new DBException(e.getMessage(), e);
		} finally {
			db.releaseReference();
		}
	}

	/**
	 * Initialize SQL Insert statement for a batch insert
	 *
	 * @param sql
	 * 	Builder to initialize
	 * @param initialValues
	 * 	ContentValues to be used on the insert statement
	 */
	private void createBaseInsert(StringBuilder sql, ContentValues initialValues) {
		//Create base insert statement
		sql.append(DatabaseDictionary.SQL_INSERT);
		sql.append(tableName);
		sql.append(" (");
		int j = 0;
		Set<Map.Entry<String, Object>> valueSet = initialValues.valueSet();
		for (Map.Entry<String, Object> entry : valueSet) {
			String colName = entry.getKey();
			sql.append((j > 0) ? "," : "");
			sql.append(colName);
			j++;
		}
		sql.append(") VALUES ");
	}

	/**
	 * Insert a list of object to the data base, this method must be used for performance, in other cases use the base
	 * insert one
	 *
	 * @param insertObjects
	 * 	Object to be get the values to insert
	 * @throws com.cmovil.baseandroid.dao.db.DBException
	 * 	if something goes wrong during SQL statements execution
	 */
	public List<Integer> insertAux(List<T> insertObjects) throws DBException {

		try {
			// Gets the data repository in write mode
			SQLiteDatabase db = mDatabaseOpenHelper.getWritableDatabase();

			List<Integer> ids = new ArrayList<Integer>();
			if (db == null) return ids;

			for (T element : insertObjects) {
				// Insert the new row, returning the primary key value of the new row
				long newRowId;
				newRowId = db.insertOrThrow(tableName, null, fillMapValues(element));
				ids.add((int) newRowId);
			}
			db.close();

			return ids;
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
	 * Delete all the rows from the selected table in database
	 * @return the number of rows affected, 0 otherwise.
	 *
	 * @throws DBException
	 * 	if something goes wrong during SQL statements execution
	 */
	public Integer delete() throws DBException {
		try {
			// Gets the data repository in write mode
			SQLiteDatabase db = mDatabaseOpenHelper.getReadableDatabase();
			if (db == null) return 0;
			// Issue SQL statement.
			Integer res = db.delete(tableName, null, null);
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
	@Deprecated
	public Cursor getById(Integer id, String[] columns) throws DBException {
		return getById(id, columns, null);
	}

	/**
	 * Gets all the objects of the selected table with the selected id
	 *
	 * @param id
	 * 	Id that will be searched in the database
	 * @param columns
	 * 	The columns to include, if null then all are included
	 * @param projectionMap
	 * 	The projection map maps from column names that the caller passes into query to database column names. This is
	 * 	useful for renaming columns as well as disambiguating column names when doing joins. For example you could map
	 * 	"name" to "people.name". If a projection map is set it must contain all column names the user may request,
	 * 	even if
	 * 	the key and value are the same.
	 * @return Cursor positioned to matching word, or null if not found.
	 *
	 * @throws DBException
	 * 	if something goes wrong during SQL statements execution
	 */
	public Cursor getById(Integer id, String[] columns, Map<String, String> projectionMap) throws DBException {
		//Add table name to default filter id string in order to use full table name for join queries
		String selection = tableName + "." + DatabaseDictionary.DBBaseStructure.FILTER_ID;
		String[] selectionArgs = new String[]{String.valueOf(id)};
		try {
			return query(getDefaultTableJoin(), selection, selectionArgs, columns, projectionMap);
		} catch (SQLException e) {
			throw new DBException(e.getMessage(), e);
		}

		/*
		 * This builds a query that looks like: SELECT <columns> FROM <table>
		 * WHERE rowId = <id>
		 */
	}
}
