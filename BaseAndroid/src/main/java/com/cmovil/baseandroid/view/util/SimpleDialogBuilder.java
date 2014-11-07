/**
 * File: SimpleDialogBuilder
 * CreationDate: 06/11/14
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description: 
 *
 */
package com.cmovil.baseandroid.view.util;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.View;

import com.cmovil.baseandroid.R;

import java.io.Serializable;

/**
 * Builder class to set the configuration for creating a
 * {@link com.cmovil.baseandroid.view.util.SimpleDialogFragment}. Several attributes can be set in order to
 * change the behaviour and appearance of the dialog fragment
 *
 * @author "Ing. Arturo Ayala Tello"
 * @version 1.0
 * @since 06/11/14
 */
public class SimpleDialogBuilder implements Serializable {

	private View mView;

	private DialogInterface.OnClickListener mPositiveClickListener;

	private DialogInterface.OnClickListener mNegativeClickListener;

	private DialogInterface.OnDismissListener mOnDismissListener;

	@StringRes
	private int mTitle;

	@StringRes
	private int mContent;

	@StringRes
	private int mPositiveText;

	@StringRes
	private int mNegativeText;

	@DrawableRes
	private int mPositiveBackground;

	@DrawableRes
	private int mNegativeBackground;

	public SimpleDialogBuilder() {
		mTitle = R.string.empty_string;
		mContent = R.string.empty_string;
		mPositiveText = R.string.empty_string;
		mNegativeText = R.string.empty_string;
		mPositiveBackground = R.drawable.dialog_default_button_background;
		mNegativeBackground = R.drawable.dialog_default_button_background;
	}

	public View getView() {
		return mView;
	}

	public SimpleDialogBuilder setView(View view) {
		this.mView = view;
		return this;
	}

	public DialogInterface.OnClickListener getPositiveClickListener() {
		return mPositiveClickListener;
	}

	public SimpleDialogBuilder setPositiveClickListener(DialogInterface.OnClickListener positiveClickListener) {
		this.mPositiveClickListener = positiveClickListener;
		return this;
	}

	public DialogInterface.OnClickListener getNegativeClickListener() {
		return mNegativeClickListener;
	}

	public SimpleDialogBuilder setNegativeClickListener(DialogInterface.OnClickListener negativeClickListener) {
		this.mNegativeClickListener = negativeClickListener;
		return this;
	}

	public DialogInterface.OnDismissListener getOnDismissListener() {
		return mOnDismissListener;
	}

	public SimpleDialogBuilder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
		this.mOnDismissListener = onDismissListener;
		return this;
	}

	@StringRes
	public int getTitle() {
		return mTitle;
	}

	/**
	 * Sets the text for the dialog header.
	 * Note: This title only appears if and only if a custom view is not defined using
	 * {@link #setView(android.view.View)}. In case of having a custom view, this attribute is ignored.
	 * @param title
	 * The string resource of the text that appears as the dialog header
	 * @return This object to chain the set call
	 */
	public SimpleDialogBuilder setTitle(@StringRes int title) {
		this.mTitle = title;
		return this;
	}

	@StringRes
	public int getContent() {
		return mContent;
	}

	/**
	 * Sets the text for the dialog content.
	 * Note: This content only appears if and only if a custom view is not defined using
	 * {@link #setView(android.view.View)}. In case of having a custom view, this attribute is ignored.
	 * @param content
	 * The string resource of the text that appears as the dialog content
	 * @return This object to chain the set call
	 */
	public SimpleDialogBuilder setContent(@StringRes int content) {
		this.mContent = content;
		return this;
	}

	@StringRes
	public int getPositiveText() {
		return mPositiveText;
	}

	/**
	 * Sets the text for the positive button.
	 * Note: This text and the button only appears if and only if a click listener is set for this matter using
	 * {@link #setPositiveClickListener(android.content.DialogInterface.OnClickListener)}. In case of not having
	 * a listener defined, this attribute is ignored
	 * @param positiveText
	 * The string resource of the text that appears in the positive button
	 * @return This object to chain the set call
	 */
	public SimpleDialogBuilder setPositiveText(@StringRes int positiveText) {
		this.mPositiveText = positiveText;
		return this;
	}

	@StringRes
	public int getNegativeText() {
		return mNegativeText;
	}

	/**
	 * Sets the text for the negative button.
	 * Note: This text and the button only appears if and only if a click listener is set for this matter using
	 * {@link #setNegativeClickListener(android.content.DialogInterface.OnClickListener)}. In case of not having
	 * a listener defined, this attribute is ignored
	 * @param negativeText
	 * The string resource of the text that appears in the negative button
	 * @return This object to chain the set call
	 */
	public SimpleDialogBuilder setNegativeText(@StringRes int negativeText) {
		this.mNegativeText = negativeText;
		return this;
	}

	@DrawableRes
	public int getPositiveBackground() {
		return mPositiveBackground;
	}

	/**
	 * Sets a background drawable for the positive button.
	 * Note: This background only appears if and only if a click listener is set for this matter using
	 * {@link #setNegativeClickListener(android.content.DialogInterface.OnClickListener)}. In case of not having
	 * a listener defined, this attribute is ignored
	 * @param positiveBackground
	 * The drawable for the button background
	 * @return This object to chain the set call
	 */
	public SimpleDialogBuilder setPositiveBackground(@DrawableRes int positiveBackground) {
		this.mPositiveBackground = positiveBackground;
		return this;
	}

	@DrawableRes
	public int getNegativeBackground() {
		return mNegativeBackground;
	}

	/**
	 * Sets a background drawable for the negative button.
	 * Note: This background only appears if and only if a click listener is set for this matter using
	 * {@link #setNegativeClickListener(android.content.DialogInterface.OnClickListener)}. In case of not having
	 * a listener defined, this attribute is ignored
	 * @param negativeBackground
	 * The drawable for the button background
	 * @return This object to chain the set call
	 */
	public SimpleDialogBuilder setNegativeBackground(@DrawableRes int negativeBackground) {
		this.mNegativeBackground = negativeBackground;
		return this;
	}

	public SimpleDialogFragment create() {
		SimpleDialogFragment fragment = new SimpleDialogFragment();
		Bundle args = new Bundle();
		args.putSerializable(SimpleDialogFragment.BUNDLE_TAG_BUILDER, this);
		fragment.setArguments(args);
		return fragment;
	}
}
