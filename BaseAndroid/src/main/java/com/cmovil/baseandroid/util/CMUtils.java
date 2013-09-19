/**
 * File: CMUtils.java
 * CreationDate: 20/03/2013
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description: 
 * 	Class that contains common functions across the application as well as static
 * 	values that will be used by other classes
 */
package com.cmovil.baseandroid.util;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cmovil.baseandroid.R;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Class that contains common functions across the application as well as static values that will be used by other
 * classes
 *
 * @author "M. en C. Javier Silva Perez (JSP)"
 * @version 1.0
 * @since 20/03/2013
 */
public class CMUtils {

	public static final String FONT_REFLEX_BLACK = "fonts/ReflexBlack.ttf";
	public static final String FONT_VERDANA = "fonts/Verdana.ttf";
	public static final String FONT_ARIAL_BLACK = "fonts/ArialBlack.ttf";
	public static final SecureRandom SECURE_RANDOM = new SecureRandom();
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
		Typeface tf = Typeface.createFromAsset(context.getAssets(), font);
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
		Typeface tf = Typeface.createFromAsset(context.getAssets(), font);
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
		Typeface tf = Typeface.createFromAsset(context.getAssets(), font);
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
		Typeface tf = Typeface.createFromAsset(context.getAssets(), font);
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
		Typeface tf = Typeface.createFromAsset(context.getAssets(), font);
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
		Typeface tf = Typeface.createFromAsset(context.getAssets(), font);
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
		ANDROID_ID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
	}

	/**
	 * Disable all the childs of the selected root view
	 *
	 * @param rootView
	 * 	View to iterate in order to disable all its childs
	 * @param alpha
	 * 	Alpha to set to disabled elements
	 */
	public static void disableView(ViewGroup rootView, float alpha) {
		int count = rootView.getChildCount();
		View v;
		//Go over the child list of the view and disable all
		for (int i = 0; i < count; i++) {
			v = rootView.getChildAt(i);
			if (v != null) {
				if (v instanceof ViewGroup) disableView((ViewGroup) v, alpha);
				if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) v.setAlpha(alpha);
				v.setEnabled(false);
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
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
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
}
