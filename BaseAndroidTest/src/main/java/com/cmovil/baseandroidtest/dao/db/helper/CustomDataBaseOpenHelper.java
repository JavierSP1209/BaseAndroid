/**
 * File: CustomDataBaseOpenHelper
 * CreationDate: 27/01/14
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description: 
 *
 */
package com.cmovil.baseandroidtest.dao.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cmovil.baseandroid.dao.db.helper.BaseDatabaseOpenHelper;
import com.cmovil.baseandroid.util.KeyDictionary;

/**
 * @author "M. en C. Javier Silva Perez (JSP)"
 * @version 1.0
 * @since 27/01/14
 */
public class CustomDataBaseOpenHelper extends BaseDatabaseOpenHelper {

	public CustomDataBaseOpenHelper(Context context) {
		super(context, DatabaseDictionary.DATABASE_NAME, DatabaseDictionary.DATABASE_VERSION);
	}

	/**
	 * Called when the database is created for the first time. This is where the creation of tables and the initial
	 * population of the tables should happen.
	 *
	 * @param db
	 * 	The database
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {

		//TODO: Add each table open helper create function
		//Call create function for each table
		create(db, DatabaseDictionary.State.SQL_CREATE);
	}

	/**
	 * Called when the database needs to be upgraded. The implementation should use this method to drop tables,
	 * add tables,
	 * or do anything else it needs to upgrade to the new schema version. <p/> <p> The SQLite ALTER TABLE
	 * documentation can
	 * be found <a href="http://sqlite.org/lang_altertable.html">here</a> . If you add new columns you can use ALTER
	 * TABLE
	 * to insert them into a live table. If you rename or remove columns you can use ALTER TABLE to rename the old
	 * table,
	 * then create the new table and then populate the new table with the contents of the old table. </p><p> This
	 * method
	 * executes within a transaction.  If an exception is thrown, all changes will automatically be rolled back. </p>
	 *
	 * @param db
	 * 	The database.
	 * @param oldVersion
	 * 	The old database version.
	 * @param newVersion
	 * 	The new database version.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(KeyDictionary.TAG, "Upgrading DB from version " + oldVersion + " to " + newVersion);

		//TODO: Add each table open helper upgrade function
		//Call each table upgrade function
		upgrade(DatabaseDictionary.State.NAME, DatabaseDictionary.State.SQL_CREATE,
			DatabaseDictionary.State.SQL_BACKUP,
			db);
	}

	/**
	 * Called when the database is created for the first time. This is where the creation of tables and the initial
	 * population of the tables should happen.
	 *
	 * @param db
	 * 	The database.
	 * @param createSQL
	 * 	SQL create statement that will be executed
	 */
	@Override
	public void create(SQLiteDatabase db, String createSQL) {
		db.execSQL(createSQL);
	}
}
