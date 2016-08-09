/**
 * File: MessageErrorCode
 * CreationDate: 09/07/13
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description:
 * Base structure of errors for the communication between PDA and SIVA with AmaneceNET, the
 * errors should have an error
 * code and error description
 */

package com.keysd.baseandroid.model.ws;

/**
 * Base structure of errors for the communication between an application and the server, the
 * errors should have an
 * error code and error description
 *
 * @author "M. en C. Javier Silva Perez (JSP)"
 * @version 1.0
 * @since 09/07/13
 */
public class MessageErrorCode {
  //TODO: Add @SerializedName annotation if the ws fields have a different name
  /**
   * Error code number
   */
  private Integer code;

  //TODO: Add @SerializedName annotation if the ws fields have a different name
  /**
   * Error description
   */
  private String description;

  /**
   * Base constructor initialize code and description with default values
   */
  public MessageErrorCode() {
    code = 0;
    description = "";
  }

  /**
   * Constructor with parameters
   *
   * @param code
   * 	Error code
   * @param description
   * 	Error description
   */
  public MessageErrorCode(Integer code, String description) {
    this.code = code;
    this.description = description;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return "MessageErrorCode{" +
        "code=" + code +
        ", description='" + description + '\'' +
        '}';
  }


}
