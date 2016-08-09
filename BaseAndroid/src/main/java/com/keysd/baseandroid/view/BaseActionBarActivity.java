package com.keysd.baseandroid.view;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Main class to handle Support Library ActionBar in all application views
 * <p/>
 * Added the support for the Toolbar for API 21
 *
 * @author "Ing. Arturo Ayala"
 * @author "Ing. Jesús Fernando Sierra Pastrana"
 * @version 2.0
 * @since 19/09/13
 */
public abstract class BaseActionBarActivity extends ActionBarActivity {
  private ActionBar baseActionBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
  }

  @Override
  public void setContentView(int layoutResID) {
    super.setContentView(layoutResID);

    //Set of the Toolbar into the activity
    setSupportActionBar(getToolbar());
    baseActionBar = getSupportActionBar();

    // Set custom layout to action bar
    if (null != baseActionBar) {
      baseActionBar.setDisplayShowTitleEnabled(true);
      baseActionBar.setDisplayHomeAsUpEnabled(false);

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
        baseActionBar.setHomeButtonEnabled(true);
      }
    }
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  /**
   * Removes padding from home button
   */
  private void removePaddingFromBar() {
    // Remove home icon padding and margins
    ImageView view = (ImageView) findViewById(android.R.id.home);
    view.setPadding(0, 0, 0, 0);
    FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT);
    lp.setMargins(0, 0, 0, 0);
    view.setLayoutParams(lp);
  }

  @Override
  public void setTitle(@Nullable CharSequence title) {
    if (baseActionBar != null) {
      super.setTitle(title);
      baseActionBar.setTitle(title);
    }
  }

  public
  @Nullable
  ActionBar getBaseActionBar() {
    return baseActionBar;
  }

  /**
   * Method to indicate the generation of the Toolbar for the Activity.
   * If you want to use {@link com.keysd.baseandroid.view.BaseDrawerActivity} this method need to
   * return a non null
   * Toolbar.
   * <p/>
   * BE CAREFUL WITH THE IMPLEMENTATION OF THIS METHOD
   *
   * @return A instance to Toolbar, return a new Toolbar to indicate the activity doesn't has a
   * ActionBar and the
   * call
   * to {@link
   * #getBaseActionBar()} always return a no valid Toolbar.
   */
  public abstract
  @NonNull
  Toolbar getToolbar();
}
