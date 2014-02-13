/**
 * File: BaseDatabaseOpenHelper
 * CreationDate: 29/07/13
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description: 
 *
 */
package com.cmovil.baseandroid.dao.db.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.cmovil.baseandroid.util.KeyDictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Base class which handles data base tables creation and upgrade and using SQLiteOpenHelper
 *
 * @author "M. en C. Javier Silva Perez (JSP)"
 * @version 1.0
 * @since 29/07/13
 */
public abstract class BaseDatabaseOpenHelper extends SQLiteOpenHelper {

	protected final Context dbHelperContext;

	public BaseDatabaseOpenHelper(Context context, String name, int version) {
		super(context, name, null, version);
		this.dbHelperContext = context;
	}

	/**
	 * Get a list of column base_dictionary for the selected table
	 *
	 * @param db
	 * 	Database that contains the table
	 * @param tableName
	 * 	Table name to be used
	 * @return A List of column name
	 */
	public static List<String> getColumns(SQLiteDatabase db, String tableName) {
		List<String> ar = null;
		Cursor c = null;
		try {
			c = db.rawQuery("select * from " + tableName + " limit 1", null);
			if (c != null) {
				ar = new ArrayList<String>(Arrays.asList(c.getColumnNames()));
			}
		} catch (Exception e) {
			Log.v(tableName, e.getMessage(), e);
			e.printStackTrace();
		} finally {
			if (c != null) c.close();
		}
		return ar;
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
	public abstract void create(SQLiteDatabase db, String createSQL);

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
	 */
	public void upgrade(String tableName, String sqlCreate, String sqlBackUp, SQLiteDatabase db) {
		Log.i(KeyDictionary.TAG, "Upgrading State");

		//Update State table
		db.beginTransaction();
		// run a table creation with if not exists (we are doing an upgrade, so the table might not exists yet,
		// it will fail alter and drop)
		db.execSQL(sqlCreate);
		//Get a list the existing columns
		List<String> columns = getColumns(db, tableName);
		//Backup table
		db.execSQL(sqlBackUp);
		//create new table (the newest table creation schema)
		db.execSQL(sqlCreate);
		//get the intersection with the new columns, this time columns taken from the upgraded table
		columns.retainAll(getColumns(db, tableName));
		//Restore data
		String cols = TextUtils.join(",", columns);
		db.execSQL(String.format("INSERT INTO %s (%s) SELECT %s from temp_%s", tableName, cols, cols, tableName));
		//remove backup table
		db.execSQL("DROP table 'temp_" + tableName + "'");
		//Close transaction
		db.setTransactionSuccessful();
		db.endTransaction();
	}
}
