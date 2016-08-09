/**
 * File: KeyDictionary
 * CreationDate: $(DATE)
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description:
 */

package com.keysd.baseandroid.util;

import java.util.Date;
import java.util.Locale;

/**
 * Class that should contain all keys used in this application, in order to prevent misspelled
 * key values
 *
 * @author "M. en C. Javier Silva Perez (JSP)"
 * @version 1.0
 * @since 20/05/13
 */
public class KeyDictionary {

  /**
   * Tag to be used for log entries across the application
   */
  public static final String TAG = "BASE_TAG";

  /**
   * Key used by async task if every thing was ok
   */
  public static final int RESULT_OK = 1;
  /**
   * Key used by Async tasks if an error occurs
   */
  public static final int RESULT_ERROR = 0;
  /**
   * Default device language
   */
  public static final String LAN = Locale.getDefault().getLanguage();
  /**
   * Key used by AsyncTasks if Time out errors occurs
   */
  public static final int RESULT_ERROR_TIME_OUT = 1000;
  /**
   * Key used by AsyncTasks if Time out errors occurs
   */
  public static final int RESULT_ERROR_NO_MORE = 1001;

  /**
   * Key used by AsyncTasks if Network errors occurs
   */
  public static final int RESULT_ERROR_NETWORK = 1002;

  /**
   * Key used by AsyncTasks if Network errors occurs
   */
  public static final int RESULT_ERROR_INVALID_RESPONSE = 1003;

  /**
   * Key used by AsyncTasks if Network errors occurs
   */
  public static final int RESULT_ERROR_RESPONSE = 1004;

  /**
   * Static alpha value for disabled elements
   */
  @Deprecated
  public static final float DISABLE_ALPHA = 0.95f;

  /**
   * Static alpha value for disabled text elements
   */
  @Deprecated
  public static final float DISABLE_ALPHA_TEXT = 0.99f;
  /**
   * Static alpha value for disabled images
   */
  @Deprecated
  public static final float DISABLE_ALPHA_IMG = 0.5f;
  /**
   * Static alpha value for enabled values
   */
  @Deprecated
  public static final float ENABLED_ALPHA = 1.0f;
  /*
   * Define a request code to send to Google Play services
   * This code is returned in Activity.onActivityResult
   */
  public static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
  public static final int EMPTY_OBJECT_ID = 0;

  /**
   * Private default constructor, for prevent this class from been instantiated
   */
  protected KeyDictionary() {
  }

  /**
   * Empty date value used as the default
   */
  public static final Date ZERO_DATE = CMUtils.removeTime(new java.util.Date(-2208926549925L));
}

