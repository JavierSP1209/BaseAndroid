/**
 * File: AppEntry
 * CreationDate: 09/12/14
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description: 
 *
 */
package com.cmovil.baseandroidtest.model.db;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

import java.io.File;

/**
 * @author "Ing. Arturo Ayala Tello"
 * @version 1.0
 * @since 09/12/14
 */
public class AppEntry {

	private final Context mContext;
	private final ApplicationInfo mInfo;
	private final File mApkFile;
	private String mLabel;
	private Drawable mIcon;
	private boolean mMounted;

	public AppEntry(Context context, ApplicationInfo info) {
		this.mContext = context;
		this.mInfo = info;
		this.mApkFile = new File(info.sourceDir);
	}

	public ApplicationInfo getApplicationInfo() {
		return mInfo;
	}

	@Override
	public String toString() {
		return mLabel;
	}

	public String getLabel() {
		if (mLabel == null || !mMounted) {
			if (mApkFile.exists()) {
				mMounted = true;
				CharSequence label = mInfo.loadLabel(mContext.getPackageManager());
				mLabel = label == null ? mInfo.packageName : label.toString();
			} else {
				mMounted = false;
				mLabel = mInfo.packageName;
			}
		}
		return mLabel;
	}

	public Drawable getIcon() {
		if (mIcon == null) {
			if (mApkFile.exists()) {
				mIcon = mInfo.loadIcon(mContext.getPackageManager());
				return mIcon;
			} else {
				mMounted = false;
			}
		} else if (!mMounted) {
			// If the app wasn't mounted but is now mounted, reload its icon.
			if (mApkFile.exists()) {
				mMounted = true;
				mIcon = mInfo.loadIcon(mContext.getPackageManager());
				return mIcon;
			}
		} else {
			return mIcon;
		}
		return mContext.getResources().getDrawable(android.R.drawable.sym_def_app_icon);
	}
}
