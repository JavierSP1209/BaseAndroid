/**
 * File: DatabaseDictionary
 * CreationDate: 28/05/13
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description: 
 *  Class which contains static values for all the Data base base_dictionary needed across the application,
 *  this values could
 * include database name, tables base_dictionary, attributes base_dictionary, etc.
 */
package com.cmovil.baseandroid.dao.db;

import android.app.SearchManager;
import android.provider.BaseColumns;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Class which contains static values for all the Data base base_dictionary needed across the application, this values
 * could include database name, tables base_dictionary, attributes base_dictionary, etc.
 *
 * @author "M. en C. Javier Silva Perez (JSP)"
 * @version 1.0
 * @since 28/05/13
 */
public class DatabaseDictionary {

	/**
	 * Database name
	 */
	public static final String DATABASE_NAME = "buenfin.db";
	/**
	 * Database version
	 */
	public static final int DATABASE_VERSION = 2;
	public static final DateFormat FORMATTER_VIEW = new SimpleDateFormat("dd/MM/yyyy");
	public static final DateFormat FORMATTER_DB = new SimpleDateFormat("yyyy-MM-dd");
	public static final DateFormat FORMATTER_SERVER = new SimpleDateFormat("yyyyMMdd");
	/**
	 * SQLLite Text data type
	 */
	private static final String TEXT_TYPE = " TEXT";
	/**
	 * SQLLite Integer data type
	 */
	private static final String INTEGER_TYPE = " INTEGER";
	private static final String BLOB_TYPE = " BLOB";
	/**
	 * SQLLite Real data type
	 */
	private static final String REAL_TYPE = " REAL";
	private static final String COMMA_SEP = ",";

	/**
	 * Prevents the DatabaseDictionary class from being instantiated.
	 */
	private DatabaseDictionary() {
	}

	/**
	 * Base db structure which contains filter id value
	 */
	public interface DBBaseStructure extends BaseColumns{
		public static final String FILTER_ID = _ID + " = ?";
	}

	/**
	 * Inner class that contains all SQL statements and column names related to State table in DB
	 *
	 * @author "M. en C. Javier Silva Perez (JSP)"
	 * @version 1.0
	 * @since 29/07/13
	 */
	public static final class State implements DBBaseStructure {

		/**
		 * Table name
		 */
		public static final String NAME = "State";
		/**
		 * Column names
		 */
		public static final String COLUMN_NAME_NAME = "name";
		public static final String COLUMN_NAME_ID_SERVER = "idServer";
		public static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS " + NAME + " (" +
			_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
			COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
			COLUMN_NAME_ID_SERVER + INTEGER_TYPE + " )";
		/**
		 * Backup table statement
		 */
		public static final String SQL_BACKUP = "ALTER table " + NAME + " RENAME TO 'temp_" + NAME + "'";
		/**
		 * BEGIN SEARCH FILTERS DEFINITION *
		 */

		public static final String FILTER_ID_SERVER = COLUMN_NAME_ID_SERVER + " = ?";

		/** END SEARCH FILTERS DEFINITION **/
	}

	/**
	 * Inner class that contains all SQL statements that a table could need, this contract class contains statements
	 * for
	 * create a table using primary keys with auto-increments, foreign keys with constrains and indexes (useful for
	 * quicker searches)
	 *
	 * @author "M. en C. Javier Silva Perez (JSP)"
	 * @version 1.0
	 * @since 29/07/13
	 */
	public static final class ExampleFullIndexedTable implements DBBaseStructure {

		/**
		 * Table name
		 */
		public static final String NAME = "TableName";
		/**
		 * Column names
		 */
		public static final String COLUMN_NAME_EXAMPLE = "example";
		public static final String COLUMN_NAME_ID_FOREIGN = "example";
		public static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS " + NAME + " (" +
			_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
			COLUMN_NAME_EXAMPLE + TEXT_TYPE + COMMA_SEP +
			COLUMN_NAME_ID_FOREIGN + INTEGER_TYPE + COMMA_SEP +
			"FOREIGN KEY(" + COLUMN_NAME_ID_FOREIGN + ") REFERENCES " + "OTHER_TABLE_NAME" + "(_id) ON DELETE CASCADE" +
			" ); ";

		/**
		 * Sample create index structure, to be called after create statement
		 */
		public static final String SQL_CREATE_INDEX_TOWN =
			"CREATE INDEX IF NOT EXISTS loc_id_town_index ON " + NAME + " (" + COLUMN_NAME_ID_FOREIGN + ");";
		/**
		 * Backup table statement
		 */
		public static final String SQL_BACKUP = "ALTER table " + NAME + " RENAME TO 'temp_" + NAME + "'";
		/**
		 * BEGIN SEARCH FILTERS DEFINITION *
		 */
		public static final String FILTER_ID_EXAMPLE = COLUMN_NAME_EXAMPLE + " = ?";

		/** END SEARCH FILTERS DEFINITION **/
	}

	/**
	 * Example contract class for FTS tables
	 *
	 * @author "M. en C. Javier Silva Perez (JSP)"
	 * @version 1.0
	 * @since 29/07/13
	 */
	public static abstract class ExampleFTS implements DBBaseStructure {

		/**
		 * Table name
		 */
		public static final String NAME = "ExampleFTS";
		/**
		 * Column base_dictionary *
		 */
		// This columns must be present in the data base, so the cursor could be
		// understood by the suggestion manager
		public static final String COLUMN_NAME_KEY = SearchManager.SUGGEST_COLUMN_TEXT_1;
		public static final String COLUMN_NAME_HELPER = SearchManager.SUGGEST_COLUMN_TEXT_2;
		//ID of the element to which the FTS row will be linked to
		public static final String COLUMN_NAME_ID_RELATIVE_TABLE = "idLinkRow";
		/*
		 * Note that FTS3 does not support column constraints and thus, you
		 * cannot declare a primary key. However, "rowid" is automatically used
		 * as a unique identifier, so when making requests, we will use "_id" as
		 * an alias for "rowid"
		 */
		public static final String SQL_CREATE =
			"CREATE VIRTUAL TABLE " + NAME + " USING fts3 (" + COLUMN_NAME_KEY + COMMA_SEP + COLUMN_NAME_HELPER +
				COMMA_SEP + COLUMN_NAME_ID_RELATIVE_TABLE + INTEGER_TYPE +
				");";
		/**
		 * Backup table statement
		 */
		public static final String SQL_BACKUP = "ALTER table " + NAME + " RENAME TO 'temp_" + NAME + "'";
		/**
		 * FILTERS
		 */
		public static final String FILTER_ID = COLUMN_NAME_ID_RELATIVE_TABLE + " = CAST(? AS INTEGER)";
		public static final String FILTER_KEY = COLUMN_NAME_KEY + " MATCH ?";
		public static final String FILTER_KEY_EXACT = COLUMN_NAME_KEY + " = ?";

	}
}
