/**
 * File: DatePickerDialogFragment
 * CreationDate: 12/02/14
 * Author: "Luis Alberto Regalado Gomez"
 * Description: 
 *
 */
package com.cmovil.baseandroid.view.util;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import com.cmovil.baseandroid.R;
import com.cmovil.baseandroid.util.KeyDictionary;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;

/**
 * @author "Luis Alberto Regalado Gomez"
 * @author "M. en C. Javier Silva Perez (JSP)"
 * @version 1.0
 * @since 01/09/14
 */
public class DatePickerDialogFragment extends DialogFragment {
	private static final String TAG_SELECTION_DIVIDER = "mSelectionDivider";
	private static final int TAG_ZERO = 0;
	private static final String TAG_ATTRIBUTES = "attributes";
	private DatePickerAttributes mAttributes;

	/**
	 * Creates a new {@link com.cmovil.baseandroid.view.util.DatePickerDialogFragment} instance with the attributes
	 * sent as parameter
	 *
	 * @param attributes
	 * 	The {@link com.cmovil.baseandroid.view.util.DatePickerDialogFragment.DatePickerAttributes} that dictate
	 * 	the appearance and behaviour of the DatePicker
	 * @return A new {@link com.cmovil.baseandroid.view.util.DatePickerDialogFragment} instance
	 */
	public static DatePickerDialogFragment newInstance(DatePickerAttributes attributes) {
		DatePickerDialogFragment fragment = new DatePickerDialogFragment();
		Bundle args = new Bundle();
		args.putSerializable(TAG_ATTRIBUTES, attributes);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	@NonNull
	public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
		Bundle args = getArguments();
		mAttributes = ((DatePickerAttributes) args.getSerializable(TAG_ATTRIBUTES));
		if (mAttributes == null) mAttributes = new DatePickerAttributes();
		Calendar initial = Calendar.getInstance();
		initial.setTime(mAttributes.getInitialDate());
		DatePickerDialog datePickerDialog =
			new DatePickerDialog(getActivity(), mAttributes.getOnDateSetListener(), initial.get(Calendar.YEAR),
				initial.get(Calendar.MONTH), initial.get(Calendar.DAY_OF_MONTH));
		datePickerDialog
			.setButton(DialogInterface.BUTTON_POSITIVE, getText(mAttributes.getPositiveButtonText()),
				datePickerDialog);
		// removes the original top bar:
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (mAttributes.getMaxDate() != null) {
				datePickerDialog.getDatePicker().setMaxDate(mAttributes.getMaxDate().getTime());
			}
			setCustomPicker(datePickerDialog);
		}

		//If clean date listener is set, add the button
		if (mAttributes.getOnCleanDateListener() != null) {
			datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, getText(mAttributes.getClearButtonText()),
				mAttributes.getOnCleanDateListener());
		}
		return datePickerDialog;
	}

	/**
	 * Sets a custom header color and date picker divider style for the dialog fragment
	 *
	 * @param datePickerDialog
	 * 	Date picker to customize
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setCustomPicker(DatePickerDialog datePickerDialog) {
		datePickerDialog.setTitle("");
		DatePicker dpView = datePickerDialog.getDatePicker();
		if (dpView != null) {
			LinearLayout llFirst = (LinearLayout) dpView.getChildAt(TAG_ZERO);
			if (llFirst != null) {
				LinearLayout llSecond = (LinearLayout) llFirst.getChildAt(TAG_ZERO);
				if (llSecond != null) {
					for (int i = TAG_ZERO; i < llSecond.getChildCount(); i++) {
						NumberPicker picker = (NumberPicker) llSecond.getChildAt(i);
						Field[] pickerFields = NumberPicker.class.getDeclaredFields();
						for (Field pf : pickerFields) {
							if (pf.getName().equals(TAG_SELECTION_DIVIDER)) {
								pf.setAccessible(true);
								try {
									pf.set(picker, getResources().getDrawable(mAttributes.getDivider()));
								} catch (IllegalArgumentException | Resources.NotFoundException |
									IllegalAccessException e) {
									Log.e(KeyDictionary.TAG, e.getMessage());
								}
								break;
							}
						}
					}
				}
				Resources resources = getResources();
				if (mAttributes.getHeaderLayout() <= 0) return;
				LayoutInflater inflater = getActivity().getLayoutInflater();
				View v = inflater.inflate(mAttributes.getHeaderLayout(), dpView, false);
				// Default header height
				dpView.addView(v);
				if (mAttributes.getHeaderHeight() <= 0) return;
				int titleHeight = (int) resources.getDimension(mAttributes.getHeaderHeight());
				FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) llFirst.getLayoutParams();
				if (lp != null) {
					lp.setMargins(TAG_ZERO, titleHeight, TAG_ZERO, TAG_ZERO);
				}
			}
		}
	}

	/**
	 * Class that encloses a set of attributes that represent the configuration for a
	 * {@link com.cmovil.baseandroid.view.util.DatePickerDialogFragment}
	 */
	public static class DatePickerAttributes implements Serializable {

		/**
		 * Action taken when a date is selected
		 */
		private OnDateSetListener mOnDateSetListener;
		/**
		 * If defined, a Negative Button is added to the DatePicker and, when clicked, the action defined by the
		 * listener is executed.
		 */
		private DialogInterface.OnClickListener mOnCleanDateListener;
		/**
		 * The initial date that the picker will display. If null, {@link com.cmovil.baseandroid.util
		 * .KeyDictionary#ZERO_DATE}
		 */
		private Date mInitialDate;
		/**
		 * The maximum date that the picker will display. If null, no max date is specified
		 */
		private Date mMaxDate;
		/**
		 * The text to display in the positive button
		 */
		@StringRes
		private int mPositiveButtonText;
		/**
		 * The text to display in the negative button
		 */
		@StringRes
		private int mClearButtonText;
		/**
		 * A drawable for the picker dividers. Typically, a shape with a solid color.
		 */
		@DrawableRes
		private int mDivider;
		/**
		 * A layout that works as header for the date picker. If 0, no header is added
		 */
		@LayoutRes
		private int mHeaderLayout;
		/**
		 * The height of the header, if any
		 */
		@DimenRes
		private int mHeaderHeight;

		/**
		 * Constructor that initializes all the needed values with default values
		 */
		public DatePickerAttributes() {
			mInitialDate = KeyDictionary.ZERO_DATE;
			mPositiveButtonText = R.string.ok;
			mClearButtonText = R.string.cancel;
			mDivider = R.drawable.date_picker_default_divider;
			mHeaderLayout = R.layout.date_picker_default_header;
			mHeaderHeight = R.dimen.date_picker_default_height;
		}

		public OnDateSetListener getOnDateSetListener() {
			return mOnDateSetListener;
		}

		public DatePickerAttributes setOnDateSetListener(OnDateSetListener mOnDateSetListener) {
			this.mOnDateSetListener = mOnDateSetListener;
			return this;
		}

		public DialogInterface.OnClickListener getOnCleanDateListener() {
			return mOnCleanDateListener;
		}

		public DatePickerAttributes setOnCleanDateListener(DialogInterface.OnClickListener mOnCleanDateListener) {
			this.mOnCleanDateListener = mOnCleanDateListener;
			return this;
		}

		public Date getInitialDate() {
			return mInitialDate;
		}

		/**
		 * The initial date that the picker will display
		 *
		 * @param initialDate
		 * A {@link java.util.Date} object with the date to display initially. If null, it is replaced with
		 * {@link com.cmovil.baseandroid.util.KeyDictionary#ZERO_DATE}
		 * @return
		 * This object to chain the set call
		 */
		public DatePickerAttributes setInitialDate(@Nullable Date initialDate) {
			if (initialDate == null) initialDate = KeyDictionary.ZERO_DATE;
			this.mInitialDate = initialDate;
			return this;
		}

		@Nullable
		public Date getMaxDate() {
			return mMaxDate;
		}

		/**
		 * The maximum date that the picker will display.
		 *
		 * @param maxDate
		 * A {@link java.util.Date} object for the maximum date to display. The picker won't display any date
		 * beyond this one.
		 * @return
		 * This object to chain the set call
		 */
		public DatePickerAttributes setMaxDate(@Nullable Date maxDate) {
			this.mMaxDate = maxDate;
			return this;
		}

		@StringRes
		public int getPositiveButtonText() {
			return mPositiveButtonText;
		}

		/**
		 * The text for the positive button
		 *
		 * @param positiveButtonText
		 * A String resource with the text to show at the positive button. If this parameter is <= 0, it is replaced
		 * by a default text resource
		 * @return
		 * This object to chain the set call
		 */
		public DatePickerAttributes setPositiveButtonText(@StringRes int positiveButtonText) {
			if (positiveButtonText <= 0) positiveButtonText = R.string.ok;
			this.mPositiveButtonText = positiveButtonText;
			return this;
		}

		@StringRes
		public int getClearButtonText() {
			return mClearButtonText;
		}

		public DatePickerAttributes setClearButtonText(@StringRes int clearButtonText) {
			this.mClearButtonText = clearButtonText;
			return this;
		}

		@DrawableRes
		public int getDivider() {
			return mDivider;
		}

		/**
		 * Sets the divider for the date picker numbers. A Shape with a Solid color is recommended for the divider
		 *
		 * @param divider
		 * The divider to set. If this parameter is <= 0, it is replaced by the default divider
		 * @return
		 * This object to chain the set call
		 */
		public DatePickerAttributes setDivider(@DrawableRes int divider) {
			if (divider <= 0) divider = R.drawable.date_picker_default_divider;
			this.mDivider = divider;
			return this;
		}

		@LayoutRes
		public int getHeaderLayout() {
			return mHeaderLayout;
		}

		/**
		 * Sets the header for the date picker
		 *
		 * @param headerLayout
		 * The layout that represents the header that will be displayed over the DatePicker. If this parameter is
		 * <= 0, the header is not added.
		 * @return
		 * This object to chain the set call
		 */
		public DatePickerAttributes setHeaderLayout(@LayoutRes int headerLayout) {
			this.mHeaderLayout = headerLayout;
			return this;
		}

		@DimenRes
		public int getHeaderHeight() {
			return mHeaderHeight;
		}

		/**
		 * Sets the height for the header set by {@link #setHeaderLayout(int)}
		 *
		 * @param headerHeight
		 * A dimen resource for the height. If this parameter is <= 0, it is ignored when building the header.
		 * @return
		 * This object to chain the set call
		 */
		public DatePickerAttributes setHeaderHeight(@DimenRes int headerHeight) {
			this.mHeaderHeight = headerHeight;
			return this;
		}
	}
}