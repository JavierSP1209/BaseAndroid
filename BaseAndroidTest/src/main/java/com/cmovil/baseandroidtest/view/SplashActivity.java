package com.cmovil.baseandroidtest.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.cmovil.baseandroid.view.BaseDrawerActivity;
import com.cmovil.baseandroid.view.BaseNavigationDrawerFragment;
import com.cmovil.baseandroid.view.loader.ImageLoader;
import com.cmovil.baseandroid.view.util.FloatingHintControl;
import com.cmovil.baseandroidtest.R;
import com.cmovil.baseandroidtest.controller.SampleController;
import com.cmovil.baseandroidtest.util.KeyDictionary;

import java.util.LinkedList;
import java.util.List;

public class SplashActivity extends BaseDrawerActivity {

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.main_frame, new PlaceholderFragment(position)).commit();
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

		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.main_frame, new PlaceholderFragment(0));
		fragmentTransaction.commit();

		DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerLayout.setScrimColor(getResources().getColor(R.color.primaryColorDark));
	}

	@Nullable
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


	/**
	 * A placeholder fragment containing a simple view.
	 */
	public class PlaceholderFragment extends Fragment {
		public static final int INSERT_NUMBER = 1054;
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		public PlaceholderFragment(int position) {
			super();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, position);
			setArguments(args);
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.main_content, container, false);

			ImageView imgTest = (ImageView) rootView.findViewById(R.id.imgTest);
			String imageURL = "http://internetclaro.domainscm.com/resources/download.png";
			//String imageURL =
			//	"http://chamorrobible.org/images/photos/gpw-200905-UnitedStatesAirForce-090414-F-6911G-003-Pacific
			// -Ocean
			// -B" +
			//		"-2-Spirit-stealth-bomber-F-22A-Raptor-stealth-fighters-Guam-April-2009-huge.jpg";

			//String imageURL = "http://info.sortere.no/wp-content/filer/2009/07/earth-huge.png";
			//String imageURL ="http://www.nbbd.com/godo/ef/images/0707mapHuge.jpg";
			//String imageURL = "http://www.digivill.net/~binary/wall-covering/(huge!)14850x8000%2520earth.jpg";
			ImageLoader<String> imageLoader = new ImageLoader<String>(SplashActivity.this);
			//imageLoader.displayImage("Test", imageURL, imgTest, rootView.findViewById(R.id.progress), -1, -1, null);
			Button begin = (Button) rootView.findViewById(R.id.btnBegin);
			begin.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					SampleController sampleController = new SampleController(SplashActivity.this);

					//for(int i=0;i<10;i++) {
					sampleController.testBatchInsert(INSERT_NUMBER);
//				try {
//					List<State> states = sampleController.getAll();
//					for(State state:states){
//						Log.d(KeyDictionary.TAG, state.toString());
//					}
//				}catch (DBException db){
//					Log.e(KeyDictionary.TAG, db.getMessage(),db);
//				}
					//}
				}
			});

			Spinner spinnerTest = (Spinner) rootView.findViewById(R.id.spinnerTest);
			List<String> spinnerOptions = new LinkedList<String>();
			spinnerOptions.add("Default");
			spinnerOptions.add("Option 1");
			spinnerOptions.add("Option 2");
			spinnerOptions.add("Option 3");
			spinnerOptions.add("Option 4");
			ArrayAdapter<String> adapter =
				new ArrayAdapter<String>(SplashActivity.this, android.R.layout.simple_list_item_1, spinnerOptions);
			spinnerTest.setAdapter(adapter);
			FloatingHintControl floatingHintControl = (FloatingHintControl) rootView.findViewById(R.id.txtTest2);

			floatingHintControl.setSpinnerItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					Log.d(KeyDictionary.TAG, "ITem Selected - " + position);
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {

				}
			});

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