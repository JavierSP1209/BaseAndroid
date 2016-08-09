/**
 * File: InvalidResponseException
 * CreationDate: $(DATE)
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description:
 */

package com.keysd.baseandroid.dao.ws;

/**
 * Exception used when the server response does not correspond with the expected one
 * @author "M. en C. Javier Silva Perez (JSP)"
 * @version 1.0
 * @since 31/05/13
 */
public class InvalidResponseException extends Exception {

  /**
   * Constructs a new {@code Exception} that includes the current stack trace.
   */
  public InvalidResponseException() {
    super();
  }

  /**
   * Constructs a new {@code Exception} with the current stack trace and the specified detail
   * message.
   *
   * @param detailMessage
   * 	the detail message for this exception.
   */
  public InvalidResponseException(String detailMessage) {
    super(detailMessage);
  }

  /**
   * Constructs a new {@code Exception} with the current stack trace, the specified detail
   * message and the specified
   * cause.
   *
   * @param detailMessage
   * 	the detail message for this exception.
   * @param throwable
   * 	the cause of this exception.
   */
  public InvalidResponseException(String detailMessage, Throwable throwable) {
    super(detailMessage, throwable);
  }

}
