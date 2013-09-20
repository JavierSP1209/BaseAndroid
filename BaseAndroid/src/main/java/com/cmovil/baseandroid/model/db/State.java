/**
 * File: State
 * CreationDate: 29/07/13
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description: 
 *  Model Object that represents the States table in DB model
 */
package com.cmovil.baseandroid.model.db;


import com.cmovil.baseandroid.util.CustomCatalogComparator;

/**
 * Model Object that represents the States table in DB model
 *
 * @author "M. en C. Javier Silva Perez (JSP)"
 * @version 1.0
 * @since 29/07/13
 */
public class State extends BaseModel {
	/**
	 * Comparator for state objects, using its description as comparable attribute
	 */
	public static final CustomCatalogComparator<State> COMPARATOR = new CustomCatalogComparator<State>();

	/**
	 * State id on the server
	 */
	private Integer idServer;
	/**
	 * State name
	 */
	private String name;

	/**
	 * Default constructor, initialize ids with -1 and name as an empty string
	 */
	public State() {
		this.id = -1;
		this.idServer = -1;
		this.name = "";
	}

	/**
	 * Constructor with parameters which initialize the corresponding attribute element
	 *
	 * @param id
	 * 	Table row id
	 * @param idServer
	 * 	State id on the server
	 * @param name
	 * 	State name
	 */
	public State(Integer id, Integer idServer, String name) {
		this.id = id;
		this.idServer = idServer;
		this.name = name;
	}

	/**
	 * Return the description that will be shown in the view for these element
	 *
	 * @return A string that will be shown in the UI
	 */
	@Override
	public String getShownDescription() {
		return name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdServer() {
		return idServer;
	}

	public void setIdServer(Integer idServer) {
		this.idServer = idServer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "State{" +
			"id=" + id +
			", idServer=" + idServer +
			", name='" + name + '\'' +
			'}';
	}
}
