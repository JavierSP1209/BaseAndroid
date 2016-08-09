/**
 * File: NavigationDrawerItem
 * CreationDate: 07/08/14
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description:
 */

package com.keysd.baseandroid.model.view;

/**
 * Model class that represents an item for the navigation drawer options menu
 * @author "Ing. Arturo Ayala Tello"
 * @version 1.0
 * @since 07/08/14
 */
public class NavigationDrawerItem {

  /**
   * Id of the resource to be shown as drawable of the menu item
   */
  private final int drawableIcon;

  /**
   * Id of the string resource to be used for the menu item
   */
  private final int textId;

  /**
   * Id of the menu item
   */
  private final int id;

  /**
   * Default constructor
   *
   * @param drawableIcon
   * 	Id of the resource to be shown as drawable of the menu item
   * @param textId
   * 	Id of the string resource to be used for the menu item
   * @param id
   * 	Id of the menu item
   */
  public NavigationDrawerItem(int drawableIcon, int textId, int id) {
    this.drawableIcon = drawableIcon;
    this.textId = textId;
    this.id = id;
  }

  public int getDrawableIcon() {
    return drawableIcon;
  }

  public int getTextId() {
    return textId;
  }

  public int getId() {
    return id;
  }
}
