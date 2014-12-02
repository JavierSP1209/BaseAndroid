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
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.cmovil.baseandroid.R;
import com.cmovil.baseandroid.util.CMUtils;
import com.cmovil.baseandroid.util.KeyDictionary;

import java.lang.reflect.Field;
import java.util.Calendar;

/**
 * DatePicker Dialog that can be fully configured. See {@link com.cmovil.baseandroid.view.util.DatePickerBuilder} class
 *
 * @author "Luis Alberto Regalado Gomez"
 * @author "M. en C. Javier Silva Perez (JSP)"
 * @author "Ing. Arturo Ayala Tello"
 * @version 1.0
 * @since 01/09/14
 */
public class DatePickerDialogFragment extends DialogFragment {
	private static final String TAG_SELECTION_DIVIDER = "mSelectionDivider";
	private static final int TAG_ZERO = 0;
	private DatePickerBuilder mAttributes;

	public void setDatePickerBuilder(DatePickerBuilder builder) {
		this.mAttributes = builder;
	}

	public static DatePickerDialogFragment newInstance(DatePickerBuilder builder) {
		DatePickerDialogFragment fragment = new DatePickerDialogFragment();
		fragment.setDatePickerBuilder(builder);
		return fragment;
	}

	@Override
	@NonNull
	public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
		if (mAttributes == null) mAttributes = new DatePickerBuilder();
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
			if (mAttributes.getMinDate() != null) {
				datePickerDialog.getDatePicker().setMinDate(mAttributes.getMinDate().getTime());
			}
			//For version under lollipop custom date picker elements
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
				setCustomPicker(datePickerDialog);
			}
		}

		//If clean date listener is set, add the button
		if (mAttributes.getOnCleanDateListener() != null) {
			datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, getText(mAttributes.getClearButtonText()),
				mAttributes.getOnCleanDateListener());
		}
		return datePickerDialog;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mAttributes.getButtonBackground() > 0) {
			((DatePickerDialog) getDialog()).getButton(DialogInterface.BUTTON_POSITIVE)
				.setBackgroundResource(mAttributes.getButtonBackground());
			if (mAttributes.getOnCleanDateListener() != null) {
				((DatePickerDialog) getDialog()).getButton(DialogInterface.BUTTON_NEGATIVE)
					.setBackgroundResource(mAttributes.getButtonBackground());
			}
		}

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

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
				TextView txtTitle = (TextView) v.findViewById(R.id.txtTitle);
				if (txtTitle != null) {
					if (mAttributes.getHeaderTitle() > 0) {
						txtTitle.setText(mAttributes.getHeaderTitle());
					} else if (!CMUtils.isEmptyText(mAttributes.getHeaderTitleStr())) {
						txtTitle.setText(mAttributes.getHeaderTitleStr());
					}
				}
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
}