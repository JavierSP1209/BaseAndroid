/**
 * File: BaseModelAdapter
 * CreationDate: 27/08/14
 * Author: "Ing. Jesús Fernando Sierra Pastrana"
 * Author: "Lic. Luis Alberto Regalado Gómez"
 * Description: Generic Adapter for BaseModels list.
 *
 */

package com.cmovil.baseandroid.view.util;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cmovil.baseandroid.model.db.BaseModel;

import java.util.List;

/**
 * Generic adapter for {@link com.cmovil.baseandroid.model.db.BaseModel} objects.
 *
 * @author "Ing. Jesús Fernando Sierra Pastrana"
 * @author "Lic. Luis Alberto Regalado Gómez"
 * @version 1.0
 * @since 27/08/14
 */

public class BaseModelAdapter<T extends BaseModel> extends BaseAdapter {
	private List<T> baseModels;
	private int itemLayout;
	private HolderInterface<T> holderInterface;

	/**
	 * Constructor
	 *
	 * @param baseModels
	 * 	List of {@link com.cmovil.baseandroid.model.db.BaseModel} objects to be displayed.
	 * @param itemLayout
	 * 	Resource Layout to draw in each item.
	 * @param holderInterface
	 * 	To define the behavior of the View Holder.
	 */
	public BaseModelAdapter(@NonNull List<T> baseModels, @LayoutRes int itemLayout,
	                        @NonNull HolderInterface<T> holderInterface) {
		this.baseModels = baseModels;
		this.itemLayout = itemLayout;
		this.holderInterface = holderInterface;
	}

	@Override
	public int getCount() {
		return baseModels.size();
	}

	@Override
	public BaseModel getItem(int i) {
		return baseModels.get(i);
	}

	@Override
	public long getItemId(int i) {
		return getItem(i).getDbId();
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		if (view == null) {
			view = LayoutInflater.from(viewGroup.getContext()).inflate(itemLayout, null);
			ViewHolder viewHolder = holderInterface.getViewHolder(view);
			view.setTag(viewHolder);
		}
		holderInterface.setViews(baseModels.get(position), (ViewHolder) view.getTag());
		return view;
	}

	/**
	 * Interface to implement the behavior of the View Holder inside the {@link com.cmovil.baseandroid.view.util
	 * .BaseModelAdapter}.
	 *
	 * @param <T>
	 * 	Class extended of {@link com.cmovil.baseandroid.model.db.BaseModel}.
	 */
	public interface HolderInterface<T extends BaseModel> {
		/**
		 * Method to return a {@link com.cmovil.baseandroid.view.util.BaseModelAdapter.ViewHolder} based on view.
		 *
		 * @param view
		 * 	Non Null view for each item in the {@link com.cmovil.baseandroid.view.util.BaseModelAdapter}.
		 * @return {@link com.cmovil.baseandroid.view.util.BaseModelAdapter.ViewHolder} child with all the widget
		 * references declared inside of {@link com.cmovil.baseandroid.view.util.BaseModelAdapter.ViewHolder} child.
		 */
		public ViewHolder getViewHolder(View view);

		/**
		 * Method to set the content for each item in the {@link com.cmovil.baseandroid.view.util.BaseModelAdapter}.
		 *
		 * @param baseModel
		 * 	{@link com.cmovil.baseandroid.model.db.BaseModel} child for each position in {@link
		 * 	com.cmovil.baseandroid.view.util.BaseModelAdapter}.
		 * @param viewHolder
		 * 	{@link com.cmovil.baseandroid.view.util.BaseModelAdapter.ViewHolder} child with the widget references of
		 * 	each
		 * 	view in {@link com.cmovil.baseandroid.view.util.BaseModelAdapter}.
		 */
		public void setViews(T baseModel, ViewHolder viewHolder);
	}

	/**
	 * Abstract Class to retain the references of Widgets in the view.
	 * The declarations of the widgets references needs be private final and initialized in the constructor.
	 */
	public static abstract class ViewHolder {
		/**
		 * Constructor
		 *
		 * @param itemView
		 * 	The inflated view for each item in the adapter, its used to get the Views founded by id.
		 */
		public ViewHolder(@NonNull View itemView) {
		}
	}
}
