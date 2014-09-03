/**
 * File: BasicSpinnerAdapter
 * CreationDate: 26/03/14
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description: 
 *
 */
package com.cmovil.baseandroid.view.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cmovil.baseandroid.model.db.BaseModel;

import java.util.List;

/**
 * Reusable spinner adapter used for simple spinners throughout the app
 *
 * @author "Ing. Arturo Ayala Tello"
 * @version 1.0
 * @since 26/03/14
 */
public class BaseModelSpinnerAdapter<T extends BaseModel> extends ArrayAdapter<T> {

	/**
	 * Constructor
	 *
	 * @param context
	 * 	The current context.
	 * @param resource
	 * 	The resource ID for a layout file containing a TextView to use when
	 * 	instantiating views.
	 * @param objects
	 * 	The objects to represent in the view.
	 */
	public BaseModelSpinnerAdapter(Context context, int resource, List<T> objects) {
		super(context, resource, objects);
		// Specify the layout to use when the list of choices appears
		setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	}

	/**
	 * Constructor with a default resource
	 *
	 * @param context
	 * 	The current context.
	 * @param objects
	 * 	The objects to represent in the view.
	 */
	public BaseModelSpinnerAdapter(Context context, List<T> objects) {
		this(context, android.R.layout.simple_spinner_item, objects);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param position
	 * @param convertView
	 * @param parent
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return buildConvertView(position, super.getView(position, convertView, parent));
	}

	/**
	 * Builds the common view using the item description and setting the proper font to the view
	 *
	 * @param position
	 * 	The item position
	 * @param view
	 * 	The view created
	 * @return The view with the modifications
	 */
	private View buildConvertView(int position, View view) {
		TextView txtView = (TextView) view.findViewById(android.R.id.text1);

		T item = getItem(position);
		txtView.setText(item.getShownDescription());
		//SearsUtils.setTypeface(getContext(), SearsUtils.FONT_OPEN_SANS_REGULAR, txtView);
		return view;
	}

	/**
	 * Method to return the DbId of the object in the position.
	 *
	 * @param position
	 * 	Position in the list.
	 * @return DbId.
	 */
	@Override
	public long getItemId(int position) {
		return getItem(position).getDbId();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param position
	 * @param convertView
	 * @param parent
	 */
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return buildConvertView(position, super.getDropDownView(position, convertView, parent));
	}

	@Override
	public T getItem(int position) {
		return super.getItem(position);
	}
}

