/**
 * File: DatePickerBuilder
 * CreationDate: 05/11/14
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description: 
 *
 */
package com.cmovil.baseandroid.view.util;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.cmovil.baseandroid.R;
import com.cmovil.baseandroid.util.KeyDictionary;

import java.io.Serializable;
import java.util.Date;

/**
 * Builder class to set the configuration for creating a
 * {@link com.cmovil.baseandroid.view.util.DatePickerDialogFragment}. Several attributes can be set in order to
 * change the behaviour and appearance of the date picker
 *
 * @author "Ing. Arturo Ayala Tello"
 * @version 1.0
 * @since 05/11/14
 */

public class DatePickerBuilder implements Serializable {

	/**
	 * Action taken when a date is selected
	 */
	private DatePickerDialog.OnDateSetListener mOnDateSetListener;
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
	 * The minimum selectable date for the picker. If null, no min date is specified
	 */
	private Date mMinDate;
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

	@StringRes
	private int mHeaderTitle;

	private String mHeaderTitleStr;

	@DrawableRes
	private int mButtonBackground;

	/**
	 * Constructor that initializes all the needed values with default values
	 */
	public DatePickerBuilder() {
		mInitialDate = KeyDictionary.ZERO_DATE;
		mPositiveButtonText = R.string.ok;
		mClearButtonText = R.string.cancel;
		mDivider = R.drawable.date_picker_default_divider;
		mHeaderLayout = R.layout.date_picker_default_header;
		mHeaderHeight = R.dimen.date_picker_default_height;
		mHeaderTitle = R.string.select_date;
		mHeaderTitleStr = "";
		mButtonBackground = R.drawable.dialog_default_button_background;
	}

	public DatePickerDialog.OnDateSetListener getOnDateSetListener() {
		return mOnDateSetListener;
	}

	public DatePickerBuilder setOnDateSetListener(DatePickerDialog.OnDateSetListener mOnDateSetListener) {
		this.mOnDateSetListener = mOnDateSetListener;
		return this;
	}

	public DialogInterface.OnClickListener getOnCleanDateListener() {
		return mOnCleanDateListener;
	}

	public DatePickerBuilder setOnCleanDateListener(DialogInterface.OnClickListener mOnCleanDateListener) {
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
	 * 	A {@link java.util.Date} object with the date to display initially. If null, it is replaced with
	 * 	{@link com.cmovil.baseandroid.util.KeyDictionary#ZERO_DATE}
	 * @return This object to chain the set call
	 */
	public DatePickerBuilder setInitialDate(@Nullable Date initialDate) {
		if (initialDate == null) initialDate = KeyDictionary.ZERO_DATE;
		this.mInitialDate = initialDate;
		return this;
	}

	@Nullable
	public Date getMinDate() {
		return mMinDate;
	}

	/**
	 * The minimum date selectable for the picker.
	 *
	 * @param minDate
	 * 	A {@link java.util.Date} object for the maximum date to display. The picker won't display any date
	 * 	before this one.
	 * @return This object to chain the set call
	 */
	public DatePickerBuilder setMinDate(@Nullable Date minDate) {
		this.mMinDate = minDate;
		return this;
	}

	@Nullable
	public Date getMaxDate() {
		return mMaxDate;
	}

	/**
	 * The maximum date selectable for the picker.
	 *
	 * @param maxDate
	 * 	A {@link java.util.Date} object for the maximum date to display. The picker won't display any date
	 * 	beyond this one.
	 * @return This object to chain the set call
	 */
	public DatePickerBuilder setMaxDate(@Nullable Date maxDate) {
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
	 * 	A String resource with the text to show at the positive button. If this parameter is <= 0, it is replaced
	 * 	by a default text resource
	 * @return This object to chain the set call
	 */
	public DatePickerBuilder setPositiveButtonText(@StringRes int positiveButtonText) {
		if (positiveButtonText <= 0) positiveButtonText = R.string.ok;
		this.mPositiveButtonText = positiveButtonText;
		return this;
	}

	@StringRes
	public int getClearButtonText() {
		return mClearButtonText;
	}

	public DatePickerBuilder setClearButtonText(@StringRes int clearButtonText) {
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
	 * 	The divider to set. If this parameter is <= 0, it is replaced by the default divider
	 * @return This object to chain the set call
	 */
	public DatePickerBuilder setDivider(@DrawableRes int divider) {
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
	 * 	The layout that represents the header that will be displayed over the DatePicker. If this parameter is
	 * 	<= 0, the header is not added.
	 * @return This object to chain the set call
	 */
	public DatePickerBuilder setHeaderLayout(@LayoutRes int headerLayout) {
		this.mHeaderLayout = headerLayout;
		return this;
	}

	@DimenRes
	public int getHeaderHeight() {
		return mHeaderHeight;
	}

	@StringRes
	public int getHeaderTitle() {
		return mHeaderTitle;
	}

	/**
	 * Sets the title for the header. If a custom header layout is set by using {@link #setHeaderLayout(int)},
	 * this attribute is ignored
	 *
	 * @param headerTitle
	 * 	A string resource for the date picker title
	 * @return This object to chain the set call
	 */
	public DatePickerBuilder setHeaderTitle(@StringRes int headerTitle) {
		this.mHeaderTitle = headerTitle;
		return this;
	}

	/**
	 * Method to give the option to the user to give a String as the title for the header. If a custom header layout
	 * is set by using {@link #setHeaderLayout(int)}, this attribute is ignored.
	 * Note that calling this method will make that if a header was set previously with {@link #setHeaderTitle(int)},
	 * this previous call will be overwritten by this method.
	 *
	 * @param headerTitleStr
	 * 	The String to put as title for the date picker
	 * @return This object to chain the set call
	 */
	public DatePickerBuilder setHeaderTitleStr(String headerTitleStr) {
		this.mHeaderTitleStr = headerTitleStr;
		this.mHeaderTitle = 0;
		return this;
	}

	public String getHeaderTitleStr() {
		return mHeaderTitleStr;
	}

	/**
	 * Sets the height for the header set by {@link #setHeaderLayout(int)}
	 *
	 * @param headerHeight
	 * 	A dimen resource for the height. If this parameter is <= 0, it is ignored when building the header.
	 * @return This object to chain the set call
	 */
	public DatePickerBuilder setHeaderHeight(@DimenRes int headerHeight) {
		this.mHeaderHeight = headerHeight;
		return this;
	}

	@DrawableRes
	public int getButtonBackground() {
		return mButtonBackground;
	}

	/**
	 * Set a background drawable for the dialog buttons. This is recommended to be a selector.
	 *
	 * @param buttonBackground
	 * 	The button background drawable resource
	 * @return This object to chain the set call
	 */
	public DatePickerBuilder setButtonBackground(@DrawableRes int buttonBackground) {
		this.mButtonBackground = buttonBackground;
		return this;
	}

	public DatePickerDialogFragment create() {
		return DatePickerDialogFragment.newInstance(this);
	}
}
