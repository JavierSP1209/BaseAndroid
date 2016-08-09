/**
 * File: Parser.java
 * CreationDate: 09/07/13
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description:
 * Parser class for AmaneceNET objects, this functions receives JSON string and parse them into
 * the corresponding model
 * objects
 */

package com.keysd.baseandroid.model.ws;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.keysd.baseandroid.dao.ws.InvalidResponseException;
import com.keysd.baseandroid.util.KeyDictionary;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser class for AmaneceNET objects, this functions receives JSON string and parse them into
 * the corresponding model
 * objects
 *
 * @param <T>
 * 	Generic type for parsing json lists
 * @author "M. en C. Javier Silva Perez (JSP)"
 * @version 1.0
 * @since 09/07/13
 */
public class Parser<T> {

  /**
   * Parse a JSON string to the corresponding list of objects.
   *
   * @param json
   * 	Corresponding JSON string
   * @return A list of objects if the JSON string was correct
   *
   * @throws com.google.gson.JsonSyntaxException
   * 	If the string is not valid JSON
   */
  public List<T> parseWSObjects(String json, Class<T> type) throws InvalidResponseException {
    Gson gson = new Gson();
    JsonParser jsonParser = new JsonParser();
    List<T> list = new ArrayList<T>();
    try {
      JsonElement jsonElem = jsonParser.parse(json);
      if (jsonElem.isJsonArray()) {
        JsonArray arr = jsonElem.getAsJsonArray();
        for (JsonElement je : arr) {
          list.add(gson.fromJson(je, type));
        }
      } else {
        throw new InvalidResponseException("Server response invalid or unsupported format");
      }
      return list;
    } catch (ClassCastException ex) {
      Log.e(KeyDictionary.TAG, ex.getMessage(), ex);
      throw new InvalidResponseException(ex.getMessage());
    } catch (JsonSyntaxException ex) {
      Log.e(KeyDictionary.TAG, ex.getMessage(), ex);
      throw new InvalidResponseException(ex.getMessage());
    }
  }
}