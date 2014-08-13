/**
 * File: SampleDAO
 * CreationDate: 31/07/13
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description: 
 * Data Access Object implementation for State table, will contain all the function that access to the data base
 * table in order to modify or retrieve information
 *
 */
package com.cmovil.baseandroidtest.dao.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.cmovil.baseandroid.dao.db.BaseDBDAO;
import com.cmovil.baseandroid.dao.db.DBException;
import com.cmovil.baseandroidtest.dao.db.helper.CustomDataBaseOpenHelper;
import com.cmovil.baseandroidtest.dao.db.helper.DatabaseDictionary;
import com.cmovil.baseandroidtest.dao.db.helper.SampleOpenHelper;
import com.cmovil.baseandroidtest.model.db.State;

import java.util.Map;

/**
 * Data Access Object implementation for State table, will contain all the function that access to the data base table
 * in order to modify or retrieve information
 *
 * @author "M. en C. Javier Silva Perez (JSP)"
 * @version 1.0
 * @since 31/07/13
 */
public class SampleDAO extends BaseDBDAO<State> {

	/**
	 * Constructor
	 *
	 * @param context
	 * 	The Context within which to work, used to create the DB
	 */
	public SampleDAO(Context context) {
		super(DatabaseDictionary.State.NAME, new CustomDataBaseOpenHelper(context));
	}

	/**
	 * Fill up a map for the values to be inserted or updated into the data base
	 *
	 * @param state
	 * 	Object to be get the table values
	 * @return A ContentValues object filled up with the corresponding Patient values
	 */
	@Override
	protected ContentValues fillMapValues(State state) {
		// Create a new map of values, where column base_dictionary are the keys
		ContentValues values = new ContentValues();
		values.put(DatabaseDictionary.State.COLUMN_NAME_NAME, state.getName());
		values.put(DatabaseDictionary.State.COLUMN_NAME_ID_SERVER, state.getIdServer());
		return values;
	}


	/**
	 * Gets all the states with the selected server id
	 *
	 * @param idServer
	 * 	Server Id that will be searched in the database
	 * @param columns
	 * 	The columns to include, if null then all are included
	 * @return Cursor positioned to matching word, or null if not found.
	 *
	 * @throws com.cmovil.baseandroid.dao.db.DBException
	 * 	if something goes wrong during SQL statements execution
	 */
	public Cursor getByServerId(Integer idServer, String[] columns) throws DBException {
		String selection = DatabaseDictionary.State.FILTER_ID_SERVER;
		String[] selectionArgs = new String[]{String.valueOf(idServer)};
		try {
			return query(DatabaseDictionary.State.NAME, selection, selectionArgs, columns, null);
		} catch (SQLException e) {
			throw new DBException(e.getMessage(), e);
		}
	}

	/**
	 * Gets all the states with the selected server id
	 *
	 * @param join
	 * 	Join string
	 * @param columns
	 * 	The columns to include, if null then all are included
	 * @return Cursor positioned to matching word, or null if not found.
	 *
	 * @throws com.cmovil.baseandroid.dao.db.DBException
	 * 	if something goes wrong during SQL statements execution
	 */
	public Cursor testJoin(String join, String[] columns, Map<String, String> projectionMap) throws DBException {
		//String selection = DatabaseDictionary.StateAux.FILTER_ID_SERVER;
		//String[] selectionArgs = new String[]{String.valueOf(id)};
		//String join = "State INNER JOIN StateAux ON State._id=StateAux.idState";
		try {
			return query(join, null, null, columns, projectionMap);
		} catch (SQLException e) {
			throw new DBException(e.getMessage(), e);
		}
	}
}
