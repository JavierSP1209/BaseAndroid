/**
 * File: ResponseErrorException
 * CreationDate: 11/07/13
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description:
 */

package com.keysd.baseandroid.dao.ws;

import com.keysd.baseandroid.model.ws.MessageErrorCode;
import java.util.List;

/**
 * Custom exception that should be thrown when the server response contains an
 * error message
 *
 * @author "M. en C. Javier Silva Perez (JSP)"
 * @version 1.0
 * @since 11/07/13
 */
public class ResponseErrorException extends Exception {

  private List<MessageErrorCode> errorMessages;

  /**
   * Constructs a new {@code Exception} with the list of errors
   *
   * @param errorMessages
   * 	List of error contained in the response message header
   */
  public ResponseErrorException(List<MessageErrorCode> errorMessages) {
    super("Server response with errors");
    this.errorMessages = errorMessages;
  }

  public List<MessageErrorCode> getErrorMessages() {
    return errorMessages;
  }
}
