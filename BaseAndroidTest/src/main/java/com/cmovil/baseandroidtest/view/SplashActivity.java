package com.cmovil.baseandroidtest.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.cmovil.baseandroid.view.BaseDrawerActivity;
import com.cmovil.baseandroid.view.BaseNavigationDrawerFragment;
import com.cmovil.baseandroidtest.R;

public class SplashActivity extends BaseDrawerActivity {

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.main_frame, new PlaceHolderFragment(position)).commit();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content view
		//boolean drawerOpen = mDrawerLayout.isDrawerOpen(GravityCompat.END);
		//menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected BaseNavigationDrawerFragment createNewNavigationDrawerFragment() {
		return new NavigationDrawerFragment();
	}

	public void updateSectionTitle(int sectionNumber) {
		switch (sectionNumber) {
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
		super.updateSectionTitle(sectionNumber);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_no_toolbar) {
			Intent intent = new Intent(this, ActivityWithOutToolbar.class);
			startActivity(intent);
			return true;
		} else if (id == R.id.action_toolbar) {
			Intent intent = new Intent(this, ActivityWithActionBar.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);
		setDrawerContent(R.string.app_name);

		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.main_frame, new PlaceHolderFragment(0));
		fragmentTransaction.commit();

		DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerLayout.setScrimColor(getResources().getColor(R.color.primaryColorDark));
	}

	@NonNull
	@Override
	public Toolbar getToolbar() {
		return (Toolbar) findViewById(R.id.toolbar);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return super.onCreateOptionsMenu(menu);
	}
}