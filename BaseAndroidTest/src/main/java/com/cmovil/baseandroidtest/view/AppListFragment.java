/**
 * File: AppListFragment
 * CreationDate: 09/12/14
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description: 
 *
 */
package com.cmovil.baseandroidtest.view;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cmovil.baseandroid.view.util.BaseListLoader;
import com.cmovil.baseandroidtest.R;
import com.cmovil.baseandroidtest.model.db.AppEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author "Ing. Arturo Ayala Tello"
 * @version 1.0
 * @since 09/12/14
 */
public class AppListFragment extends ListFragment {

	private final BaseListLoader.BaseLoaderListener<AppEntry> mBaseLoaderListener =
		new BaseListLoader.BaseLoaderListener<AppEntry>() {
			@Override
			public List<? extends BaseListLoader.BaseLoaderObserver> createLoaderObservers(BaseListLoader baseListLoader) {
				List<InstalledAppsObserver> observers = new LinkedList<>();
				observers.add(new InstalledAppsObserver(baseListLoader));
				return observers;
			}

			@Override
			public List<AppEntry> onLoadInBackground() {
				// Retrieve all installed applications.
				List<ApplicationInfo> apps = getActivity().getPackageManager().getInstalledApplications(0);
				if (apps == null) {
					apps = new ArrayList<>();
				}
				// Create corresponding array of entries and load their labels.
				List<AppEntry> entries = new ArrayList<>(apps.size());
				for (int i = 0; i < apps.size(); i++) {
					entries.add(new AppEntry(getActivity(), apps.get(i)));
				}
				// Sort the list.
				Collections.sort(entries, new Comparator<AppEntry>() {
					@Override
					public int compare(AppEntry lhs, AppEntry rhs) {
						return lhs.getLabel().compareTo(rhs.getLabel());
					}
				});

				return entries;
			}
		};

	private final LoaderManager.LoaderCallbacks<List<AppEntry>> mLoaderCallbacks =
		new LoaderManager.LoaderCallbacks<List<AppEntry>>() {
			@Override
			public Loader<List<AppEntry>> onCreateLoader(int id, Bundle args) {
				return new BaseListLoader<>(getActivity(), mBaseLoaderListener);
			}

			@Override
			public void onLoadFinished(Loader<List<AppEntry>> loader, List<AppEntry> data) {
				((AppListAdapter) getListAdapter()).setData(data);
				if (isResumed()) {
					setListShown(true);
				} else {
					setListShownNoAnimation(true);
				}
			}

			@Override
			public void onLoaderReset(Loader<List<AppEntry>> loader) {
				((AppListAdapter) getListAdapter()).setData(null);
			}
		};

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setListAdapter(new AppListAdapter(getActivity()));
		getLoaderManager().initLoader(0, null, mLoaderCallbacks);
	}

	private class AppListAdapter extends ArrayAdapter<AppEntry> {

		public AppListAdapter(Context context) {
			super(context, 0);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.app_list_item, parent, false);
				holder.txtApp = (TextView) convertView.findViewById(R.id.txt_app);
				convertView.setTag(holder);
			} else {
				holder = ((ViewHolder) convertView.getTag());
			}
			holder.txtApp.setText(getItem(position).getLabel());
			holder.txtApp.setCompoundDrawablesWithIntrinsicBounds(getItem(position).getIcon(), null, null, null);
			return super.getView(position, convertView, parent);
		}

		public void setData(List<AppEntry> data) {
			clear();
			if (data != null) {
				for (int i = 0; i < data.size(); i++) {
					add(data.get(i));
				}
			}
		}
	}

	static class ViewHolder {
		TextView txtApp;
	}
}
