/**
 * File: BaseLoader
 * CreationDate: 09/12/14
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description:
 */

package com.keysd.baseandroid.view.util;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import java.util.LinkedList;
import java.util.List;

/**
 * Base, configurable AsyncTaskLoader that manages a List of objects of the type sent as parameter
 * This loader provides an interface in order to give the client class the ability to tell this
 * loader
 * what to load in the background.
 * This loader is based on managing lists, so it is simple but very flexible given its generic
 * nature.
 *
 * @author "Ing. Arturo Ayala Tello"
 * @version 1.0
 * @since 09/12/14
 */
public class BaseListLoader<D> extends AsyncTaskLoader<List<D>> {

  /**
   * Listener that tells the loader what to load in background
   */
  private BaseLoaderListener<D> mBaseLoaderListener;
  /**
   * List to preserve the results in the loader
   */
  private List<D> mList;
  /**
   * The observers that look over data sources and notify the loader
   */
  private List<? extends BaseLoaderObserver> mObserverList;

  public BaseListLoader(Context context, BaseLoaderListener<D> baseLoaderListener) {
    super(context);
    this.mBaseLoaderListener = baseLoaderListener;
  }

  @Override
  public List<D> loadInBackground() {
    if (mBaseLoaderListener != null) {
      mList = mBaseLoaderListener.onLoadInBackground();
    } else {
      mList = new LinkedList<>();
    }
    return mList;
  }

  @Override
  public void deliverResult(List<D> data) {
    if (isReset()) {
      data = new LinkedList<>();
    }
    mList = data;
    if (isStarted()) {
      super.deliverResult(mList);
    }
  }

  //--Loader states--

  @Override
  protected void onStartLoading() {
    if (mList != null) {
      deliverResult(mList);
    }

    if (mBaseLoaderListener != null) {
      mObserverList = mBaseLoaderListener.createLoaderObservers(this);
    }

    if (takeContentChanged() || mList == null) {
      forceLoad();
    }
  }

  @Override
  protected void onStopLoading() {
    cancelLoad();
  }

  @Override
  protected void onReset() {
    //Stop monitoring the data source
    onStopLoading();
    if (mList != null) {
      mList = null;
    }

    if (mObserverList != null) {
      for (BaseLoaderObserver observer : mObserverList) {
        observer.stopObserving();
      }
      mObserverList = null;
    }
  }

  /**
   * Interface that serves as a listener for the client user to be able to tell the Loader what
   * to load
   * in the background
   *
   * @param <D>
   * 	The type of object with which the loader is working
   */
  public interface BaseLoaderListener<D> {

    /**
     * Creates a list of objects that are subclasses of
     * {@link BaseListLoader.BaseLoaderObserver} that are going to listen for
     * changes in a data source and notify those changes to the loader
     *
     * @param baseListLoader
     * The base loader to which the observer will be associated
     * @return
     * A list of observers for the loader
     */
    public List<? extends BaseLoaderObserver> createLoaderObservers(
        BaseListLoader<D> baseListLoader);

    /**
     * Method that carries out the fetching of the data. This is for telling the loader how is
     * the data
     * fetched
     *
     * @return
     * A list of objects obtained from the data source
     */
    public List<D> onLoadInBackground();

  }

  /**
   * Base observer that contains the main observer functionality and lets the subclasses define
   * what and how they
   * are observing any data source. These objects may be added to {@link BaseListLoader}
   * class, where the main methods are used. When the client determines that a change needs to be
   * notified, the method
   * can be called, which will notify the loader.
   *
   */
  public interface BaseLoaderObserver {

    /**
     * Tells the observer to stop monitoring the data source that it observes
     */
    public void stopObserving();

  }
}
