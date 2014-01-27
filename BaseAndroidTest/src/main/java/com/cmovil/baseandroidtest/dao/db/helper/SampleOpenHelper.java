/**
 * File: SampleOpenHelper
 * CreationDate: 31/07/13
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description: 
 *
 */
package com.cmovil.baseandroidtest.dao.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.cmovil.baseandroidtest.dao.db.helper.DatabaseDictionary;
import com.cmovil.baseandroid.dao.db.helper.BaseDatabaseOpenHelper;

/**
 * State table data base open helper, this class will implements its own onCreate and onUpgrade functions depending on
 * the table needs
 *
 * @author "M. en C. Javier Silva Perez (JSP)"
 * @version 1.0
 * @since 31/07/13
 */
public class SampleOpenHelper extends CustomDataBaseOpenHelper {

	public SampleOpenHelper(Context context) {
		super(context);
	}




	/**
	 * Called when the database is created for the first time. This is where the creation of tables and the initial
	 * population of the tables should happen.
	 *
	 * @param db
	 * 	The database.
	 */
	@Override
	public void create(SQLiteDatabase db) {
		//Create tables
		db.execSQL(DatabaseDictionary.State.SQL_CREATE);
		//TODO: EXECUTE CREATE INDEX IF NECESSARY

	}
}
