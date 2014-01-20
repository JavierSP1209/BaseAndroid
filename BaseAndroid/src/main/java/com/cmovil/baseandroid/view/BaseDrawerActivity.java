package com.cmovil.baseandroid.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cmovil.baseandroid.R;

/**
 * Main class to handle Support Library ActionBar and navigation drawer in all application views
 *
 * @author "Ing. Arturo Ayala"
 * @version 1.0
 * @since 19/09/13
 */
public class BaseDrawerActivity extends BaseActionBarActivity
	implements NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
	 */
	protected NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(R.layout.base_activity);

		mNavigationDrawerFragment =
			(NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.left_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.left_drawer,   (DrawerLayout) findViewById(R.id.drawer_layout));

		FrameLayout mainFrame = (FrameLayout) findViewById(R.id.main_frame);
		View v = getLayoutInflater().inflate(layoutResID, null);
		mainFrame.addView(v);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content view
		//boolean drawerOpen = mDrawerLayout.isDrawerOpen(GravityCompat.END);
		//menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.main_frame, PlaceholderFragment.newInstance(position))
			.commit();
	}

	public void onSectionAttached(int number) {
		switch (number) {
			case 0:
				mTitle = getString(R.string.title_section1);
				break;
			case 1:
				mTitle = getString(R.string.title_section2);
				break;
			case 2:
				mTitle = getString(R.string.title_section3);
				break;
			default:
				mTitle = getString(R.string.app_name);
		}

		//If drawer is locked, update action bar title
		if(mNavigationDrawerFragment!=null && mNavigationDrawerFragment.isLockDrawer()){
			getSupportActionBar().setTitle(mTitle);
		}
	}

	/**
	 * Restores default action bar state like navigation mode and title
	 */
	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			//getMenuInflater().inflate(R.menu.home, menu);
			restoreActionBar();
			return true;
		}

		//If drawer is locked, update action bar title
		if(mNavigationDrawerFragment.isLockDrawer()){
			getSupportActionBar().setTitle(mTitle);
		}

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section
		 * number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.splash_activity, container, false);
			TextView textView = (TextView) rootView.findViewById(R.id.txtSectionNumber);
			textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((BaseDrawerActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
		}
	}

}
