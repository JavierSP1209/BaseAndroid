package com.cmovil.baseandroid.view;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;

import com.cmovil.baseandroid.R;

/**
 * Main class to handle Support Library ActionBar and navigation drawer in all application views
 *
 * @author "Ing. Arturo Ayala"
 * @version 1.0
 * @since 19/09/13
 */
public abstract class BaseDrawerActivity extends BaseActionBarActivity
	implements BaseNavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
	 */
	protected BaseNavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in {@link #restoreActionBar()}.
	 */
	protected CharSequence mTitle;

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(R.layout.base_activity);

		FrameLayout mainFrame = (FrameLayout) findViewById(R.id.main_frame);
		View v = getLayoutInflater().inflate(layoutResID, null);
		mainFrame.addView(v);
	}

	public void setDrawerContent(){

		FragmentManager fragmentManager = getSupportFragmentManager();

		mNavigationDrawerFragment = (BaseNavigationDrawerFragment)fragmentManager.findFragmentById(R.id.left_drawer);
		if(mNavigationDrawerFragment==null){
			mNavigationDrawerFragment = createNewNavigationDrawerFragment();
		}
		fragmentManager.beginTransaction().replace(R.id.left_drawer, mNavigationDrawerFragment)
			.commit();
		mTitle = getTitle();
	}

	protected abstract BaseNavigationDrawerFragment createNewNavigationDrawerFragment();

	@Override
	protected void onStart() {
		super.onStart();
		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.left_drawer, (DrawerLayout) findViewById(R.id.drawer_layout),
			GravityCompat.START);
	}

	/**
	 * Updates section title
	 */
	public void onSectionAttached(int sectionNumber) {
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
}
