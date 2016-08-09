/**
 * File: BaseNavigationDrawerFragment
 * CreationDate: 27/01/14
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description:
 */

package com.keysd.baseandroidtest.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.keysd.baseandroid.view.BaseNavigationDrawerFragment;
import com.keysd.baseandroidtest.R;

/**
 * @author "M. en C. Javier Silva Perez (JSP)"
 * @version 1.0
 * @since 27/01/14
 */
public class NavigationDrawerFragment extends BaseNavigationDrawerFragment {

  private ListView mDrawerListView;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mDrawerListView = (ListView) inflater
        .inflate(R.layout.fragment_navigation_drawer, container, false);
    if (getActionBar() != null) {
      mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
          selectItem(position);
        }
      });
      mDrawerListView.setAdapter(
          new ArrayAdapter<String>(getActionBar().getThemedContext(),
                                   android.R.layout.simple_list_item_1,
                                   android.R.id.text1,
                                   new String[]{getString(R.string.title_section1),
                                       getString(R.string.title_section2),
                                       getString(R.string.title_section3),}));
      mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
    }
    return mDrawerListView;
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    // If the drawer is open, show the global app actions in the action bar. See also
    // showGlobalContextActionBar, which controls the top-left area of the action bar.
    if (isDrawerOpen() && !isLockDrawer()) {
      inflater.inflate(com.keysd.baseandroid.R.menu.global, menu);
      showGlobalContextActionBar();
    }
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  protected void selectItem(int position) {
    if (mDrawerListView != null) {
      mDrawerListView.setItemChecked(position, true);
    }
    super.selectItem(position);
  }
}
