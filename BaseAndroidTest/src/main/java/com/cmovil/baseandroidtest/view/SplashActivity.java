package com.cmovil.baseandroidtest.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmovil.baseandroid.dao.db.DBException;
import com.cmovil.baseandroid.view.BaseDrawerActivity;
import com.cmovil.baseandroid.view.BaseNavigationDrawerFragment;
import com.cmovil.baseandroid.view.loader.ImageLoader;
import com.cmovil.baseandroidtest.R;
import com.cmovil.baseandroidtest.controller.SampleController;
import com.cmovil.baseandroidtest.util.KeyDictionary;

public class SplashActivity extends BaseDrawerActivity {

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.main_frame, PlaceholderFragment.newInstance(position))
			.commit();
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
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);
		setDrawerContent(R.string.app_name);

		ImageView imgTest = (ImageView) findViewById(R.id.imgTest);
		String imageURL = "http://internetclaro.domainscm.com/resources/download.png";
		//String imageURL =
		//	"http://chamorrobible.org/images/photos/gpw-200905-UnitedStatesAirForce-090414-F-6911G-003-Pacific-Ocean
		// -B" +
		//		"-2-Spirit-stealth-bomber-F-22A-Raptor-stealth-fighters-Guam-April-2009-huge.jpg";

		//String imageURL = "http://info.sortere.no/wp-content/filer/2009/07/earth-huge.png";
		//String imageURL ="http://www.nbbd.com/godo/ef/images/0707mapHuge.jpg";
		//String imageURL = "http://www.digivill.net/~binary/wall-covering/(huge!)14850x8000%2520earth.jpg";
		ImageLoader<String> imageLoader = new ImageLoader<String>(this);
		//imageLoader.displayImage("Test", imageURL, imgTest, findViewById(R.id.progress), -1, -1, null);
		SampleController sampleController= new SampleController(this);
		try {
			sampleController.testJoin();
		} catch (DBException e) {
			Log.e(KeyDictionary.TAG, e.getMessage());
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return super.onCreateOptionsMenu(menu);
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
			((SplashActivity) activity).updateSectionTitle(getArguments().getInt(ARG_SECTION_NUMBER));
		}
	}
}