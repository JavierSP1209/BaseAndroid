package com.cmovil.baseandroid.view;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;

import com.cmovil.baseandroid.R;
import com.cmovil.baseandroid.util.CMUtils;

/**
 * Base navigation drawer fragments using android
 * <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for more details about it. This base fragment will implement common functions like showing and
 * hiding the drawer, also define functions for managing interactions with this drawer.
 */
public class BaseNavigationDrawerFragment extends Fragment {

	/**
	 * Remember the position of the selected item.
	 */
	private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

	/**
	 * Remember the configuration of the drawer.
	 */
	private static final String STATE_DRAWER = "state_drawer";

	/**
	 * Per the design guidelines, you should show the drawer on launch until the user manually
	 * expands it. This shared preference tracks this.
	 */
	private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

	/**
	 * A pointer to the current callbacks instance (the Activity).
	 */
	private NavigationDrawerCallbacks mCallbacks;

	/**
	 * If the screen is at least large in order to modify drawer layout
	 */
	private boolean isLargeScreen;

	/**
	 * If the screen orientation is landscape
	 */
	private boolean isLandscape;

	private boolean lockDrawer;
	private boolean lastDrawerState;
	private int drawerPosition;

	/**
	 * Helper component that ties the action bar to the navigation drawer.
	 */
	private ActionBarDrawerToggle mDrawerToggle;

	private DrawerLayout mDrawerLayout;

	private View mFragmentContainerView;

	protected int mCurrentSelectedPosition = 0;
	private boolean mFromSavedInstanceState;
	private boolean mUserLearnedDrawer;

	/**
	 * String resource id that will be shown when the drawer its open
	 */
	private int appName;

	public void setLockDrawer(boolean lockDrawer) {
		this.lockDrawer = lockDrawer;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Read in the flag indicating whether or not the user has demonstrated awareness of the
		// drawer. See PREF_USER_LEARNED_DRAWER for details.
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
		mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

		//Initialize values
		isLargeScreen = false;
		isLandscape = false;
		lockDrawer = false;
		lastDrawerState = false;

		if (savedInstanceState != null) {
			mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
			mFromSavedInstanceState = true;
			lastDrawerState = savedInstanceState.getBoolean(STATE_DRAWER, false);

			// Select either the default item (0) or the last selected item.
			selectItem(mCurrentSelectedPosition);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Indicate that this fragment would like to influence the set of actions in the action bar.
		setHasOptionsMenu(true);
	}

	/**
	 * Gets if the drawer fragment is shown
	 *
	 * @return TRUE if drawer is opened, FALSE other wise
	 */
	public boolean isDrawerOpen() {
		return (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView));
	}

	/**
	 * Gets if the drawer is locked, so the user could not open or close it
	 *
	 * @return TRUE if the drawer is locked
	 */
	public boolean isLockDrawer() {
		return lockDrawer;
	}

	/**
	 * Users of this fragment must call this method to set up the navigation drawer interactions.
	 *
	 * @param fragmentId
	 * 	The android:id of this fragment in its activity's layout.
	 * @param drawerLayout
	 * 	The DrawerLayout containing this fragment's UI.
	 * @param drawerPosition
	 * 	Position of the drawer, {@link android.support.v4.view.GravityCompat#START},
	 * 	{@link android.support.v4.view.GravityCompat#END}
	 * @param drawerIcon
	 * 	Id of the resource to use as drawer icon on the action bar
	 * @param appName
	 * 	Resource id of the string to be shown when the drawer its open
	 */
	public void setUp(int fragmentId, DrawerLayout drawerLayout, int drawerPosition, int drawerIcon, int appName) {
		mFragmentContainerView = getActivity().findViewById(fragmentId);
		mDrawerLayout = drawerLayout;
		this.drawerPosition = drawerPosition;
		this.appName = appName;

		// set a custom shadow that overlays the main content when the drawer opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, drawerPosition);
		// set up the drawer's list view with items and click listener

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the navigation drawer and the action bar app icon.
		mDrawerToggle = new ActionBarDrawerToggle(getActivity(),                    /* host Activity */
			mDrawerLayout,                    /* DrawerLayout object */
			drawerIcon,             /* nav drawer image to replace 'Up' caret */
			R.string.drawer_open,  /* "open drawer" description for accessibility */
			R.string.drawer_close  /* "close drawer" description for accessibility */) {
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				if (!isAdded()) {
					return;
				}

				getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
				mCallbacks.onDrawerClosed(drawerView);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				if (!isAdded()) {
					return;
				}

				if (!mUserLearnedDrawer) {
					// The user manually opened the drawer; store this flag to prevent auto-showing
					// the navigation drawer automatically in the future.
					mUserLearnedDrawer = true;
					SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
					sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).commit();
				}

				getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
				mCallbacks.onDrawerOpened(drawerView);
			}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				super.onDrawerSlide(drawerView, slideOffset);
				mCallbacks.onDrawerSlide(drawerView, slideOffset);
			}

			@Override
			public void onDrawerStateChanged(int newState) {
				super.onDrawerStateChanged(newState);
				mCallbacks.onDrawerStateChanged(newState);
			}
		};

		// If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
		// per the navigation drawer design guidelines.
		if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
			mDrawerLayout.openDrawer(mFragmentContainerView);
		}

		// Defer code dependent on restoration of previous instance state.
		mDrawerLayout.post(new Runnable() {
			@Override
			public void run() {
				mDrawerToggle.syncState();
			}
		});

		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	/**
	 * Users of this fragment must call this method to set up the navigation drawer interactions.
	 *
	 * @param fragmentId
	 * 	The android:id of this fragment in its activity's layout.
	 * @param drawerLayout
	 * 	The DrawerLayout containing this fragment's UI.
	 * @param drawerPosition
	 * 	Position of the drawer, {@link android.support.v4.view.GravityCompat#START},
	 * 	{@link android.support.v4.view.GravityCompat#END}
	 * @param appName
	 * 	Resource id of the string to be shown when the drawer its open
	 */
	public void setUp(int fragmentId, DrawerLayout drawerLayout, int drawerPosition, int appName) {
		setUp(fragmentId, drawerLayout, drawerPosition, R.drawable.ic_drawer, appName);
	}

	protected void selectItem(int position) {
		mCurrentSelectedPosition = position;

		if (mDrawerLayout != null && !lockDrawer) {
			mDrawerLayout.closeDrawer(mFragmentContainerView);
		}
		if (mCallbacks != null) {
			mCallbacks.onNavigationDrawerItemSelected(position);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallbacks = (NavigationDrawerCallbacks) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
		outState.putBoolean(STATE_DRAWER, lockDrawer);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Forward the new configuration the drawer toggle component.
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onResume() {
		super.onResume();
		isLargeScreen = CMUtils.isLargeScreen(getActivity());
		isLandscape =
			getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

		//If the screen is large and its on landscape lock drawer
		if (isLargeScreen && isLandscape) {
			mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
			mDrawerLayout.setScrimColor(Color.TRANSPARENT);
			getActionBar().setDisplayHomeAsUpEnabled(false);
			getActionBar().setHomeButtonEnabled(false);
			lockDrawer = true;
		} else {
			mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
			if (lastDrawerState) mDrawerLayout.closeDrawer(mFragmentContainerView);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (!lockDrawer && mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Per the navigation drawer design guidelines, updates the action bar to show the global app
	 * 'context', rather than just what's in the current screen.
	 */
	protected void showGlobalContextActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setTitle(this.appName);
	}

	protected ActionBar getActionBar() {
		return ((ActionBarActivity) getActivity()).getSupportActionBar();
	}

	/**
	 * Callbacks interface that all activities using this fragment must implement.
	 */
	public static interface NavigationDrawerCallbacks {
		/**
		 * Called when an item in the navigation drawer is selected.
		 */
		void onNavigationDrawerItemSelected(int position);

		void onDrawerClosed(View drawerView);

		void onDrawerOpened(View drawerView);

		void onDrawerSlide(View drawerView, float slideOffset);

		void onDrawerStateChanged(int newState);
	}

	/**
	 * Close the current drawer
	 */
	protected void closeDrawer() {
		if (!lockDrawer) mDrawerLayout.closeDrawer(drawerPosition);
	}

	/**
	 * Disable or enables toggle menu indicator
	 *
	 * @param enabled
	 * 	TRUE if the menu indicator should be shown, FALSE if default up caret must be shown
	 */
	public void setDrawerIndicatorEnabled(boolean enabled) {
		if (mDrawerToggle != null) mDrawerToggle.setDrawerIndicatorEnabled(enabled);
	}

}
