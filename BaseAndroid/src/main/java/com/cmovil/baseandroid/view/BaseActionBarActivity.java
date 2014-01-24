package com.cmovil.baseandroid.view;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cmovil.baseandroid.R;

/**
 * Main class to handle Support Library ActionBar in all application views
 *
 * @author "Ing. Arturo Ayala"
 * @version 1.0
 * @since 19/09/13
 */
public class BaseActionBarActivity extends ActionBarActivity {

    private ActionBar baseActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	    supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        baseActionBar = getSupportActionBar();

	    baseActionBar.setTitle(R.string.app_name);

		// Set custom layout to action bar
	    if (null != baseActionBar) {
		    baseActionBar.setDisplayShowTitleEnabled(true);
		    baseActionBar.setDisplayHomeAsUpEnabled(false);

		    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			    baseActionBar.setHomeButtonEnabled(true);
		    }
	    }

	    //Remove padding from home icon
        //if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        //    removePaddingFromBar();
    }

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	/**
	 * Removes padding from home button
	 */
    private void removePaddingFromBar(){
        // Remove home icon padding and margins
        ImageView view = (ImageView) findViewById(android.R.id.home);
        view.setPadding(0, 0, 0, 0);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);
        view.setLayoutParams(lp);
    }

    @Override
    public void setTitle(CharSequence title) {
	    super.setTitle(title);
        baseActionBar.setTitle(title);
    }

    public ActionBar getBaseActionBar() {
        return baseActionBar;
    }
}
