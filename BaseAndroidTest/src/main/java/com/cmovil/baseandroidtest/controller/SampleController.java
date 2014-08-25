/**
 * File: BaseDBController
 * CreationDate: 31/07/13
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description: 
 *
 * Class that will be the connection between database and view classes, so it will include functions for inserting,
 * deleting, updating and getting rows in the data base, but will use DAO objects for getting and returning data
 */
package com.cmovil.baseandroidtest.controller;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.cmovil.baseandroid.controller.BaseDBController;
import com.cmovil.baseandroid.dao.db.DBException;
import com.cmovil.baseandroidtest.dao.db.SampleDAO;
import com.cmovil.baseandroidtest.dao.db.helper.DatabaseDictionary;
import com.cmovil.baseandroidtest.model.db.State;
import com.cmovil.baseandroidtest.util.KeyDictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class that will be the connection between database and view classes, so it will include generic functions for
 * inserting,
 * deleting, updating and getting rows in the data base, but will use DAO objects for getting and returning data
 *
 * @author "M. en C. Javier Silva Perez (JSP)"
 * @version 1.0
 * @since 19/09/13
 */
public class SampleController extends BaseDBController<State> {

	private static final String[] COLUMNS =
		new String[]{DatabaseDictionary.State._ID, DatabaseDictionary.State.COLUMN_NAME_NAME,
			DatabaseDictionary.State.COLUMN_NAME_ID_SERVER};

	/**
	 * Constructor
	 *
	 * @param context
	 * 	The Context within which to work, used to create the DB
	 */
	public SampleController(Context context) {
		super(new SampleDAO(context));
	}

	/**
	 * Columns that will be returned by the sql statements
	 *
	 * @return A string array with the columns that will be returned in all sql statements
	 */
	@Override
	protected String[] getColumns() {
		return COLUMNS;
	}

	/**
	 * Fill up an object with cursor values, the cursor must be valid or exceptions could be thrown
	 *
	 * @param cursor
	 * 	Valid cursor for extract object information
	 * @return A State object fill up with cursor information
	 */
	public State fillUpObject(Cursor cursor) {
		State state = new State();
		state.setDbId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseDictionary.State._ID)));
		state.setIdServer(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseDictionary.State.COLUMN_NAME_ID_SERVER)));
		state.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseDictionary.State.COLUMN_NAME_NAME)));
		return state;
	}

	/**
	 * Gets the states with the selected server id
	 *
	 * @param idServer
	 * 	Server Id that will be searched in the database
	 * @return A {@link com.cmovil.baseandroidtest.model.db.State} object with the values from the database or an empty
	 * object if no results where found
	 *
	 * @throws com.cmovil.baseandroid.dao.db.DBException
	 * 	if something goes wrong during SQL statements execution
	 */
	public State getByServerId(Integer idServer) throws DBException {

		Cursor res = ((SampleDAO) getBaseDBDAO()).getByServerId(idServer, COLUMNS);
		//If the cursor has at least one element, create the corresponding State object, if not,
		// return an empty object
		if (res != null && res.moveToFirst()) {
			State state = fillUpObject(res);
			res.close();
			return state;

		}
		if (res != null) {
			res.close();
		}
		return new State();
	}

	public void testBatchInsert(int insertNumber){

		List<State> stateList = new ArrayList<State>();
		for(int i=0;i<insertNumber;i++){
			State state = new State();
			state.setName("Test"+i);
			state.setIdServer(i);
			stateList.add(state);
		}


		/*try {
			long begin = System.nanoTime();
			//Test simple insert
			for (State state : stateList) {
				insert(state);
			}
			long finish = System.nanoTime();
			long total = finish-begin;
			Log.d(KeyDictionary.TAG, "SimpleInsert: "+total+" nS");
		}catch (DBException ex){
			Log.e(KeyDictionary.TAG, ex.getMessage(), ex);
		}*/

		/*try {
			long begin = System.nanoTime();
			//Test customBatch insert
			insertAux(stateList);
			long finish = System.nanoTime();
			long total = finish-begin;
			Log.d(KeyDictionary.TAG, "SimpleBatchInsert: "+total+" nS");
		}catch (DBException ex){
			Log.e(KeyDictionary.TAG, ex.getMessage(), ex);
		}*/

		try {
			long begin = System.nanoTime();
			//Test customBatch insert
			insert(stateList);
			long finish = System.nanoTime();
			long total = finish-begin;
			Log.d(KeyDictionary.TAG, "CustomBatchInsert: "+total+" nS");
		}catch (DBException ex){
			Log.e(KeyDictionary.TAG, ex.getMessage(), ex);
		}
	}

	/**
	 * Test inner join queries using projection maps
	 *
	 * @return Cursor positioned to matching word, or null if not found.
	 *
	 * @throws com.cmovil.baseandroid.dao.db.DBException
	 * 	if something goes wrong during SQL statements execution
	 */
	public void testJoin() throws DBException {
		//String selection = DatabaseDictionary.StateAux.FILTER_ID_SERVER;
		//String[] selectionArgs = new String[]{String.valueOf(id)};
		String join = "State INNER JOIN StateAux ON State._id=StateAux.idState";
		String[] columns = new String[]{DatabaseDictionary.State._ID, "stateName", "stateIdServer", "stateAux"};
		HashMap<String, String> projectionMap = new HashMap<String, String>();
		projectionMap
			.put(DatabaseDictionary.State._ID, DatabaseDictionary.State.NAME + "." + DatabaseDictionary.State._ID);
		projectionMap.put("stateName", DatabaseDictionary.State.COLUMN_FULL_NAME_NAME + " AS stateName");
		projectionMap.put("stateIdServer", DatabaseDictionary.State.COLUMN_FULL_NAME_ID_SERVER + " AS stateIdServer");
		projectionMap.put("stateAux",
			DatabaseDictionary.StateAux.NAME + "." + DatabaseDictionary.StateAux.COLUMN_NAME_NAME + " AS stateAux");

		Cursor cursor = ((SampleDAO) getBaseDBDAO()).testJoin(join, columns, projectionMap);
		//If the cursor has at least one element, create the corresponding State object, if not,
		// return an empty object
		//If the cursor has at least one element, create the corresponding State object, if not,
		// return an empty object
		if (cursor != null && cursor.moveToFirst()) {
			do {
				Integer aux = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseDictionary.State._ID));
				String aux2 = cursor.getString(cursor.getColumnIndexOrThrow("stateName"));
				Integer aux3 = cursor.getInt(cursor.getColumnIndexOrThrow("stateIdServer"));
				String aux4 = cursor.getString(cursor.getColumnIndexOrThrow("stateAux"));
			} while (cursor.moveToNext());
			cursor.close();
		}
		if (cursor != null) {
			cursor.close();
		}

	}
}
