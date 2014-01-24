package com.cmovil.baseandroid.view;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;

import com.cmovil.baseandroid.R;
import com.cmovil.baseandroid.view.loader.ImageLoader;

public class SplashActivity extends BaseDrawerActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);

		ImageView imgTest = (ImageView) findViewById(R.id.imgTest);
		//String imageURL = "http://internetclaro.domainscm.com/resources/download.png";
		//String imageURL =
		//	"http://chamorrobible.org/images/photos/gpw-200905-UnitedStatesAirForce-090414-F-6911G-003-Pacific-Ocean-B" +
		//		"-2-Spirit-stealth-bomber-F-22A-Raptor-stealth-fighters-Guam-April-2009-huge.jpg";

		//String imageURL = "http://info.sortere.no/wp-content/filer/2009/07/earth-huge.png";
		//String imageURL ="http://www.nbbd.com/godo/ef/images/0707mapHuge.jpg";
		String imageURL ="http://www.digivill.net/~binary/wall-covering/(huge!)14850x8000%2520earth.jpg";
		ImageLoader<String> imageLoader = new ImageLoader<String>(this);
		imageLoader.displayImage("Test4", imageURL, imgTest, findViewById(R.id.progress), -1, -1, null);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return super.onCreateOptionsMenu(menu);
	}

}