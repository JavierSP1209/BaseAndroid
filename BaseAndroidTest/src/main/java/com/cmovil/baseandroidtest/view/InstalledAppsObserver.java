/**
 * File: InstalledAppsObserver
 * CreationDate: 09/12/14
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description: 
 *
 */
package com.cmovil.baseandroidtest.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.cmovil.baseandroid.view.util.BaseListLoader;

/**
 * @author "Ing. Arturo Ayala Tello"
 * @version 1.0
 * @since 09/12/14
 */
public class InstalledAppsObserver extends BroadcastReceiver implements BaseListLoader.BaseLoaderObserver {

	private static final String RECEIVER_DATA_SCHEME = "package";
	private BaseListLoader mBaseListLoader;

	public InstalledAppsObserver(BaseListLoader baseListLoader) {
		this.mBaseListLoader = baseListLoader;
		// Register for events related to application installs/removals/updates.
		IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
		filter.addDataScheme(RECEIVER_DATA_SCHEME);
		mBaseListLoader.getContext().registerReceiver(this, filter);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		mBaseListLoader.onContentChanged();
	}

	@Override
	public void stopObserving() {
		mBaseListLoader.getContext().unregisterReceiver(this);
	}
}
