package com.keysd.baseandroid.view;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import com.keysd.baseandroid.R;

/**
 * Main class to handle Support Library ActionBar and navigation drawer in all application views
 * This class has methods to listen the navigation drawer events (open, close, slide, state change)
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

  /**
   * String resource id that will be shown when the drawer its open
   */
  private int appName;

  @Override
  public void setContentView(int layoutResID) {
    super.setContentView(layoutResID);
    checkDrawerLayout();
    checkDrawerContents();
  }

  /**
   * Method to verify if the DrawerLayout its correctly implemented.
   */
  private void checkDrawerLayout() {
    try {
      DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
      if (drawerLayout == null) {
        throw new IllegalStateException(
            "The layout needs have a DrawerLayout with the \"drawer_layout\" id.");
      }
    } catch (ClassCastException e) {
      throw new IllegalStateException(
          "The layout needs have a DrawerLayout with the \"drawer_layout\" id.");
    }
  }

  /**
   * Method to verify if the DrawerLayout contents its correctly implemented.
   */
  private void checkDrawerContents() {
    try {
      //Try to get by id the main frame an left drawer and cast it to FrameLayout
      FrameLayout mainFrame = (FrameLayout) findViewById(R.id.main_frame);
      FrameLayout leftDrawerFrame = (FrameLayout) findViewById(R.id.left_drawer);

      //If some of frames is null
      if (mainFrame == null || leftDrawerFrame == null) {
        throw new IllegalStateException(
            "The layout needs have a two FrameLayouts with \"main_frame\" and \"left_drawer\" "
                + "id's.");
      }
    } catch (ClassCastException e) {
      //If the cast fails
      throw new IllegalStateException(
          "The layout needs have a two FrameLayouts with \"main_frame\" and \"left_drawer\" id's.");
    }
  }

  /**
   * Sets up drawer content using a custom drawer icon
   *
   * @param appName    Resource id of the string to be shown when the drawer its open
   * @param drawerIcon Resource id of the drawer icon to be used
   */
  public void setDrawerContent(int appName, int drawerIcon) {
    this.appName = appName;
    FragmentManager fragmentManager = getSupportFragmentManager();

    mNavigationDrawerFragment = (BaseNavigationDrawerFragment) fragmentManager
        .findFragmentById(R.id.left_drawer);
    if (mNavigationDrawerFragment == null) {
      mNavigationDrawerFragment = createNewNavigationDrawerFragment();
    }
    fragmentManager.beginTransaction().replace(R.id.left_drawer, mNavigationDrawerFragment)
                   .commit();
    mTitle = getTitle();
  }

  /**
   * Sets up drawer content using a default drawer ico
   *
   * @param appName Resource id of the string to be shown when the drawer its open
   */
  public void setDrawerContent(int appName) {
    setDrawerContent(appName, R.drawable.ic_drawer);
  }

  protected abstract BaseNavigationDrawerFragment createNewNavigationDrawerFragment();

  @Override
  protected void onStart() {
    super.onStart();
    // Set up the drawer.
    mNavigationDrawerFragment
        .setUp(R.id.left_drawer, (DrawerLayout) findViewById(R.id.drawer_layout),
               GravityCompat.START,
               getToolbar(),
               appName);
  }

  /**
   * Updates section title
   */
  public void updateSectionTitle(int sectionNumber) {
    //If drawer is locked, update action bar title
    if (mNavigationDrawerFragment != null && mNavigationDrawerFragment.isLockDrawer()) {
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
    if (mNavigationDrawerFragment.isLockDrawer()) {
      getSupportActionBar().setTitle(mTitle);
    }

    return super.onCreateOptionsMenu(menu);
  }

  /**
   * Disable or enables toggle menu indicator
   *
   * @param enabled TRUE if the menu indicator should be shown, FALSE if default up caret must be
   *                 shown
   */
  public void setDrawerIndicatorEnabled(boolean enabled) {
    if (mNavigationDrawerFragment != null) {
      mNavigationDrawerFragment.setDrawerIndicatorEnabled(enabled);
    }
  }

  /**
   * Close the current drawer
   */
  protected void closeDrawer() {
    if (mNavigationDrawerFragment != null) {
      mNavigationDrawerFragment.closeDrawer();
    }
  }

  /**
   * Locks the drawer in order to prevent open it or close it
   *
   * @param lockDrawer TRUE if it should be locked, FALSE otherwise
   */
  protected void lockDrawer(Boolean lockDrawer) {
    if (mNavigationDrawerFragment != null) {
      mNavigationDrawerFragment.setLockDrawer(lockDrawer);
    }
  }

  @Override
  public void onDrawerClosed(View drawerView) {

  }

  @Override
  public void onDrawerOpened(View drawerView) {

  }

  @Override
  public void onDrawerSlide(View drawerView, float slideOffset) {

  }

  @Override
  public void onDrawerStateChanged(int newState) {

  }
}

