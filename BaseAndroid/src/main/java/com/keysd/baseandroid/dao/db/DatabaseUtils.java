/**
 * File: DatabaseUtils
 * CreationDate: 14/08/14
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description:
 */

package com.keysd.baseandroid.dao.db;

import android.support.annotation.NonNull;

/**
 * Class with common methods for data base management
 *
 * @author "M. en C. Javier Silva Perez (JSP)"
 * @version 1.0
 * @since 14/08/14
 */
public class DatabaseUtils {

  /**
   * Gets the full name for an specific column and table
   *
   * @param tableName
   * 	The table name
   * @param columnName
   * 	The column name
   * @return The SQL statement full column name
   */
  public static String getFullName(@NonNull final String tableName,
      @NonNull final String columnName) {
    return tableName + "." + columnName;
  }

  /**
   * Get the column name to be used on the 'AS' SQL Statement
   *
   * @param tableName
   * 	The original table name
   * @param columnName
   * 	The column name to use
   * @return The AS column name to be used on the SELECT SQL statement
   */
  public static String getAsName(@NonNull final String tableName,
      @NonNull final String columnName) {
    return tableName + columnName;
  }

  /**
   * Get the as sentence
   *
   * @param tableName
   * 	The original table name
   * @param columnName
   * 	The column name to use
   * 	String with the full name of the column.
   * @return The complete As Sentence.
   */
  public static String getAsSentence(@NonNull final String tableName,
      @NonNull final String columnName) {
    return getFullName(tableName, columnName) + " AS " + getAsName(tableName, columnName);
  }
}
