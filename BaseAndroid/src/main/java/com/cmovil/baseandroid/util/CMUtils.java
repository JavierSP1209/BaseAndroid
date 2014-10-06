/**
 * File: CMUtils.java
 * CreationDate: 20/03/2013
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description: 
 * 	Class that contains common functions across the application as well as static
 * 	values that will be used by other classes
 */
package com.cmovil.baseandroid.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.cmovil.baseandroid.R;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * Class that contains common functions across the application as well as static values that will be used by other
 * classes
 *
 * @author "M. en C. Javier Silva Perez (JSP)"
 * @version 1.0
 * @since 20/03/2013
 */
public class CMUtils {

	/**
	 * Regular expression to validate an email address
	 */
	private static final String EMAIL_VALID_REGEX =
		"[a-z0-9!#$%&'*+/=?^_'{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_'{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)" +
			"+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
	/**
	 * Pattern that validates an email address
	 */
	public static final Pattern PATTERN_EMAIL_ADDRESS = Pattern.compile(EMAIL_VALID_REGEX);

	/**
	 * Unique ID of the Android device
	 */
	public static String ANDROID_ID;
	/**
	 * Serial number of the SIM, null if unavailable
	 */
	public static String SIM_SERIAL_NUMBER;

	/**
	 * Sets a determined font on a text view element
	 *
	 * @param context
	 * 	Context in which the TextView can be found
	 * @param font
	 * 	Font to be set in the text view see available fonts as static attributes of this class
	 * @param style
	 * 	{@see Typeface}
	 * @param textViews
	 * 	TextViews to which the font will be applied
	 */
	public static void setTypeface(Context context, String font, int style, TextView... textViews) {
		Typeface tf = Typefaces.get(context, font);
		for (TextView txt : textViews) {
			txt.setTypeface(tf, style);
		}
	}

	/**
	 * Sets a determined font on a text view element
	 *
	 * @param context
	 * 	Context in which the TextView can be found
	 * @param font
	 * 	Font to be set in the text view see available fonts as static attributes of this class
	 * @param textViews
	 * 	TextViews to which the font will be applied
	 */
	public static void setTypeface(Context context, String font, TextView... textViews) {
		Typeface tf = Typefaces.get(context, font);
		for (TextView txt : textViews) {
			txt.setTypeface(tf);
		}
	}

	/**
	 * Sets a determined font on a button element view
	 *
	 * @param context
	 * 	Context in which the TextView can be found
	 * @param font
	 * 	Font to be set in the text view see available fonts as static attributes of this class
	 * @param buttons
	 * 	Buttons to which the font will be applied
	 */
	public static void setTypeface(Context context, String font, Button... buttons) {
		Typeface tf = Typefaces.get(context, font);
		for (Button txt : buttons) {
			txt.setTypeface(tf);
		}
	}

	/**
	 * Sets a determined font on a text view element
	 *
	 * @param context
	 * 	Context in which the TextView can be found
	 * @param font
	 * 	Font to be set in the text view see available fonts as static attributes of this class
	 * @param style
	 * 	{@see Typeface}
	 * @param buttons
	 * 	Buttons to which the font will be applied
	 */
	public static void setTypeface(Context context, String font, int style, Button... buttons) {
		Typeface tf = Typefaces.get(context, font);
		for (Button txt : buttons) {
			txt.setTypeface(tf, style);
		}
	}

	/**
	 * Get the MD5 hash of an input
	 *
	 * @param input
	 * @return The MD5 hash of the selected input
	 */
	public static String getMD5(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			String hashText = number.toString(16);
			while (hashText.length() < 32) {
				hashText = "0" + hashText;
			}
			return hashText;
		} catch (NoSuchAlgorithmException e) {
			Log.e(KeyDictionary.TAG, e.getMessage(), e);
			return "";
		}

	}

	/**
	 * Converts an hexadecimal string to a byte array
	 *
	 * @param s
	 * 	Hexadecimal string
	 * @return A byte array with the parsed hexadecimal values
	 */
	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	/**
	 * Sets a determined font on a text view element
	 *
	 * @param context
	 * 	Context in which the TextView can be found
	 * @param font
	 * 	Font to be set in the text view see available fonts as static attributes of this class
	 * @param style
	 * 	{@see Typeface}
	 * @param group
	 * 	Root layout in which TextView and Buttons will be searched to apply the font
	 */
	public static void setTypeface(Context context, String font, int style, ViewGroup group) {
		Typeface tf = Typefaces.get(context, font);
		int count = group.getChildCount();
		View v;
		for (int i = 0; i < count; i++) {
			v = group.getChildAt(i);
			if (v instanceof TextView) ((TextView) v).setTypeface(tf, style);
			else if (v instanceof ViewGroup) setTypeface(context, font, style, (ViewGroup) v);
		}
	}

	/**
	 * Sets a determined font on a text view element
	 *
	 * @param context
	 * 	Context in which the TextView can be found
	 * @param font
	 * 	Font to be set in the text view see available fonts as static attributes of this class
	 * @param group
	 * 	Root layout in which TextView and Buttons will be searched to apply the font
	 */
	public static void setTypeface(Context context, String font, ViewGroup group) {
		Typeface tf = Typefaces.get(context, font);
		int count = group.getChildCount();
		View v;
		for (int i = 0; i < count; i++) {
			v = group.getChildAt(i);
			if (v instanceof TextView) ((TextView) v).setTypeface(tf);
			else if (v instanceof ViewGroup) setTypeface(context, font, (ViewGroup) v);
		}
	}

	/**
	 * Set an error message to a text view
	 *
	 * @param color
	 * 	Set the color foreground for the span
	 * @param message
	 * 	Message to be shown
	 * @param txtView
	 * 	Text View to which the message will be added
	 */
	public static void setError(int color, String message, TextView txtView) {
		ForegroundColorSpan fgcspan = new ForegroundColorSpan(color);
		SpannableStringBuilder ssbuilder = new SpannableStringBuilder(message);
		ssbuilder.setSpan(fgcspan, 0, message.length(), 0);
		txtView.setError(ssbuilder);
	}

	/**
	 * Set an error message to a text view
	 *
	 * @param message
	 * 	Message string to be shown
	 * @param txtView
	 * 	Text View to which the message will be added
	 */
	public static void setError(String message, TextView txtView) {

		//Due to an SDK bug a custom error icon should be set, because on 4.2.x version the icon its not show
		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR1) {
			//Check if message is null
			if (message != null) {
				// only for gingerbread and newer versions
				txtView.setError(message, null);
				txtView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error_icon, 0);
			} else {
				txtView.setError(null);
				//Clean the compound drawable
				txtView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			}
		} else {
			txtView.setError(message);
		}
	}

	/**
	 * Reads the phone state in order to get SIM serial number and AndroidID, those values are saved in static
	 * attributes in this class
	 *
	 * @param context
	 */
	public static void readPhoneState(Context context) {

		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		//Get SIM serial number
		SIM_SERIAL_NUMBER = telephonyManager.getSimSerialNumber();
		if (SIM_SERIAL_NUMBER == null) {
			SIM_SERIAL_NUMBER = "";
		}

	  /*
	   * Settings.Secure.ANDROID_ID returns the unique DeviceID
	   * Works for Android 2.2 and above
	   */
		ANDROID_ID = getUniqueDeviceId(context);
	}

	/**
	 * Gets an unique device Id depending on the sdk version
	 *
	 * @param context
	 * @return
	 */
	public static String getUniqueDeviceId(Context context) {
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
			return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
		} else {
			final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			return tm.getDeviceId();
		}
	}

	/**
	 * Disable or enable all the children of the selected root view
	 *
	 * @param rootView
	 * 	View to iterate in order to disable all its children
	 * @param alpha
	 * 	Alpha to set to disabled elements
	 * @param enabled
	 * 	view enabled status
	 */
	public static void toggleViewEnabled(ViewGroup rootView, float alpha, Boolean enabled) {
		int count = rootView.getChildCount();
		View v;
		//Go over the child list of the view and disable all
		for (int i = 0; i < count; i++) {
			v = rootView.getChildAt(i);
			if (v != null) {
				if (v instanceof ViewGroup) toggleViewEnabled((ViewGroup) v, alpha, enabled);
				setAlphaForAllVersions(v, alpha);
				v.setEnabled(enabled);
			}
		}
	}

	/**
	 * Remove the time of a date value
	 *
	 * @param date
	 * 	Date to remove the time part
	 * @return A date with its time set to 00:00:00
	 */
	public static Date removeTime(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.clear(Calendar.HOUR_OF_DAY);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.clear(Calendar.MINUTE);
		gc.clear(Calendar.SECOND);
		gc.clear(Calendar.MILLISECOND);
		return gc.getTime();
	}

	/**
	 * Change the default locale of the application for this activity
	 *
	 * @param locale
	 * 	To set in the configuration
	 */
	public static void setDefaultLocale(Context context, String locale) {
		Locale locJa = new Locale(locale);
		Locale.setDefault(locJa);

		Configuration config = new Configuration();
		config.locale = locJa;

		if (context != null) {
			context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
		}
	}

	/**
	 * Method to know if an Id indicates an empty object
	 *
	 * @param id
	 * 	The searched id
	 * @return True if it exists. Otherwise, False.
	 */
	public static boolean existsInDB(int id) {
		return id > KeyDictionary.EMPTY_OBJECT_ID;
	}

	public static List<BasicNameValuePair> jsonStringToUrlEncodedEntity(String json) {
		JsonParser parser = new JsonParser();
		JsonElement jsonElem = parser.parse(json);
		JsonObject jsonObject = jsonElem.getAsJsonObject();
		List<BasicNameValuePair> entityParameters = new LinkedList<BasicNameValuePair>();
		Set<Map.Entry<String, JsonElement>> jsonSet = jsonObject.entrySet();

		for (Map.Entry<String, JsonElement> entry : jsonSet) {
			entityParameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue().getAsString()));

		}
		return entityParameters;
	}


	/**
	 * This method fetches data from a given url
	 *
	 * @param strUrl
	 * 	Url from which the data will be fetched
	 * @return A String representing the resource obtained in the connection
	 *
	 * @throws IOException
	 * 	If something went wrong with the connection
	 */
	public static String getDataFromUrl(String strUrl) throws IOException {
		InputStream iStream;
		HttpURLConnection urlConnection;
		URL url = new URL(strUrl);

		// Creating an http connection to communicate with url
		urlConnection = (HttpURLConnection) url.openConnection();

		// Connecting to url
		urlConnection.connect();

		// Reading data from url
		iStream = urlConnection.getInputStream();

		BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

		StringBuilder sb = new StringBuilder();

		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		br.close();
		iStream.close();
		urlConnection.disconnect();
		return sb.toString();
	}

	/**
	 * Change opacity of a given view, with the alpha level given as parameter.
	 * This method considers the environment version.
	 *
	 * @param view
	 * 	View that will change its opacity
	 * @param alphaLevel
	 * 	Alpha level representing the opacity, where 0.0 is completely transparent, and 1.0 is completely opaque
	 */
	public static void setAlphaForAllVersions(View view, float alphaLevel) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			view.setAlpha(alphaLevel);
		} else {
			AlphaAnimation alpha = new AlphaAnimation(alphaLevel, alphaLevel);
			alpha.setDuration(0); // Make animation instant
			alpha.setFillAfter(true); // Tell it to persist after the animation ends
			view.startAnimation(alpha);
		}
	}


	/**
	 * Returns the output file for images using a custom name
	 *
	 * @param directoryName
	 * 	Name of the directory that will be created on the external storage
	 * @param dateFormat
	 * 	Format to be used for setting the time stamp for the file name
	 * @param imageSuffix
	 * 	Suffix to be used on the file name
	 * @param imageFormat
	 * 	Format of the file
	 * @return A file with a custom name using a suffix and time stamp on the external storage
	 */
	public static File getOutputMediaFile(String directoryName, String dateFormat, String imageSuffix,
	                                      String imageFormat) {
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.
		//  String appName = context.getResources().getString(R.string.app_name);
		File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), directoryName);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d(KeyDictionary.TAG, "failed to create directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat(dateFormat).format(new Date());

		return new File(mediaStorageDir.getPath() + File.separator + imageSuffix + timeStamp + imageFormat);
	}

	/**
	 * Decodes a bitmap to its corresponding representation in bytes, in the format sent as parameter
	 *
	 * @param bitmap
	 * 	Bitmap to decode
	 * @param format
	 * 	Format with which this image is going to be decoded
	 * @return A byte array containing the bytes of the decoded bitmap
	 */
	public static byte[] bitmapToByteArray(Bitmap bitmap, Bitmap.CompressFormat format) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(format, 100, stream);
		return stream.toByteArray();
	}

	private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

	/**
	 * Generate a value suitable for use in {@link #(int)}.
	 * This value will not collide with ID values generated at build time by aapt for R.id.
	 *
	 * @return a generated ID value
	 */
	public static int generateViewId() {
		for (; ; ) {
			final int result = sNextGeneratedId.get();
			// aapt-generated IDs have the high byte nonzero; clamp to the range under that.
			int newValue = result + 1;
			if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
			if (sNextGeneratedId.compareAndSet(result, newValue)) {
				return result;
			}
		}
	}

	/**
	 * Method to remove accents from an uppercase string
	 *
	 * @param str
	 * 	String to remove accents from
	 * @return The new string without accents
	 */
	public static String removeAccentsUppercase(String str) {
		str = str.replaceAll("[ÁÀÄÂ]", "A");
		str = str.replaceAll("[ÉÈËÊ]", "E");
		str = str.replaceAll("[ÍÌÎÏ]", "I");
		str = str.replaceAll("[ÓÒÖÔ]", "O");
		str = str.replaceAll("[ÚÙÜÛ]", "U");
		return str;
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public static String cleanString(String value) {
		//Remove spaces and upper case input
		String res = value.trim();


		// The char 180 (´) is not replaced by the regex, so first replace it by '-'
		res = res.replace((char) 180, '-');

		//Normalize the string will separate all of the accent marks from the characters.
		res = Normalizer.normalize(res, Normalizer.Form.NFD);
		//Using a regular expression remove every non alphanumeric character (don't include lower Case characters
		// because the first line change them to upper case)
		res = res.replace(" ", "%20");
		res = res.replaceAll("([^a-zA-Z0-9%])", "");
		return res;
	}

	/**
	 * Check if the device screen is large
	 *
	 * @param context
	 * 	Context of the application
	 * @return TRUE if the screen is large
	 */
	public static boolean isLargeScreen(Context context) {
		int screenSize = context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
		return screenSize >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	/**
	 * Uses the {@link android.view.inputmethod.InputMethodManager} system service to hide the soft keyboard from
	 * the screen
	 *
	 * @param context
	 * 	The current context
	 * @param view
	 * 	The main context view to extract the window token from as of: ({@link android.view.View#getWindowToken()}
	 */
	public static void hideKeyboard(Context context, View view) {
		if (context == null || view == null) return;
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	/**
	 * Uses the {@link android.view.inputmethod.InputMethodManager} system service to hide the soft keyboard from
	 * the screen
	 *
	 * @param activity
	 * 	The current activity
	 */
	public static void hideKeyboard(Activity activity) {
		if (activity == null) return;
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
	}
}
