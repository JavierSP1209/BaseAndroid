/**
 * File: BaseNavigationDrawerFragment
 * CreationDate: 27/01/14
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description: 
 *
 */
package com.cmovil.baseandroidtest.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cmovil.baseandroid.view.BaseNavigationDrawerFragment;
import com.cmovil.baseandroidtest.R;

/**
 * @author "M. en C. Javier Silva Perez (JSP)"
 * @version 1.0
 * @since 27/01/14
 */
public class NavigationDrawerFragment extends BaseNavigationDrawerFragment {

	private ListView mDrawerListView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mDrawerListView = (ListView) inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
		mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				selectItem(position);
			}
		});
		mDrawerListView.setAdapter(
			new ArrayAdapter<String>(getActionBar().getThemedContext(), android.R.layout.simple_list_item_1,
				android.R.id.text1, new String[]{getString(R.string.title_section1),
				getString(R.string.title_section2),
				getString(R.string.title_section3),}));
		mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
		return mDrawerListView;
	}

	@Override
	protected void selectItem(int position) {
		if (mDrawerListView != null) {
			mDrawerListView.setItemChecked(position, true);
		}
		super.selectItem(position);
	}
}
