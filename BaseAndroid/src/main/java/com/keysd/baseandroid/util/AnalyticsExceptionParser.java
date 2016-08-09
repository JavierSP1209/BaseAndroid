/**
 * File: AnalyticsExceptionParser
 * CreationDate: 26/07/13
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description:
 */

package com.keysd.baseandroid.util;


import com.google.android.gms.analytics.ExceptionParser;

/**
 * Custom exception parser for analytics exceptions,
 *
 * @author "M. en C. Javier Silva Perez (JSP)"
 * @version 1.0
 * @since 26/07/13
 */
public class AnalyticsExceptionParser implements ExceptionParser {

  /*
   * (non-Javadoc)
   * @see com.google.analytics.tracking.android.ExceptionParser#getDescription(java.lang.String,
   * java.lang.Throwable)
   */
  public String getDescription(String p_thread, Throwable p_throwable) {
    return "Thread: " + p_thread + ", Exception: " + getCombinedStackTrace(p_throwable);
  }

  /**
   * Parse the exception into an string
   *
   * @param aThrowable
   * 	Throwable to be parsed
   * @return A string line with the full stack trace
   */
  public static String getCombinedStackTrace(Throwable aThrowable) {

    final StringBuilder result = new StringBuilder();
    result.append(aThrowable.toString());
    result.append(',');

    String oneElement;

    for (StackTraceElement element : aThrowable.getStackTrace()) {
      // you can do some filtering here, selecting only the elements you need
      oneElement = element.toString();
      result.append(oneElement);
      result.append(",");
    }
    return result.toString();
  }
}
