/**
 * File: PlaceHolderFragment
 * CreationDate: 05/11/14
 * Author: "Ing. Jesús Fernando Sierra Pastrana"
 * Description: 
 *
 */
package com.cmovil.baseandroidtest.view;

/**
 * @author "Ing. Jesús Fernando Sierra Pastrana"
 * @version 1.0
 * @since 05/11/14
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.cmovil.baseandroid.view.loader.ImageLoader;
import com.cmovil.baseandroid.view.util.FloatingHintControl;
import com.cmovil.baseandroidtest.R;
import com.cmovil.baseandroidtest.controller.SampleController;
import com.cmovil.baseandroidtest.util.KeyDictionary;

import java.util.LinkedList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceHolderFragment extends Fragment {
	public static final int INSERT_NUMBER = 1054;
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";

	public PlaceHolderFragment(int position) {
		super();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, position);
		setArguments(args);
	}

	public PlaceHolderFragment() {
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
		ImageLoader<String> imageLoader = new ImageLoader<String>(getActivity());
		//imageLoader.displayImage("Test", imageURL, imgTest, rootView.findViewById(R.id.progress), -1, -1, null);
		Button begin = (Button) rootView.findViewById(R.id.btnBegin);
		begin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SampleController sampleController = new SampleController(getActivity());

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
			new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, spinnerOptions);
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
