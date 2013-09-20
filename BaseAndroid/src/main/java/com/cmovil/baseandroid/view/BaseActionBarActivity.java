package com.cmovil.baseandroid.view;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cmovil.baseandroid.R;

/**
 * Main class to handle Support Library ActionBar in all application views
 * Created by Ing. Arturo Ayala on 19/09/13.
 */
public class BaseActionBarActivity extends ActionBarActivity {

    private ActionBar baseActionBar;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseActionBar = getSupportActionBar();
        baseActionBar.setTitle(R.string.app_name);

        mTitle = getTitle();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            removePaddingFromBar();
    }

    private void removePaddingFromBar(){
        // Remove home icon padding and margins
        ImageView view = (ImageView) findViewById(android.R.id.home);
        view.setPadding(0, 0, 0, 0);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);
        view.setLayoutParams(lp);
    }

    public CharSequence getmTitle() {
        return mTitle;
    }

    public void setmTitle(CharSequence mTitle) {
        this.mTitle = mTitle;
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        baseActionBar.setTitle(title);
    }

    public ActionBar getBaseActionBar() {
        return baseActionBar;
    }
}
