package com.cmovil.baseandroid.view.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.cmovil.baseandroid.view.util.BitmapDecoderTask;
import com.cmovil.baseandroid.view.util.CustomAsyncTaskEventListener;
import com.cmovil.baseandroid.view.util.DownloadImageAsyncTask;
import com.cmovil.baseandroid.view.util.StreamTooLargeException;

import java.io.File;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * This class is based on the library created by fedorvlasov (https://github.com/thest1/LazyList), but this class uses
 * AsyncTasks instead of executors for download and/or decode the images, also the decoded bitmap can change its size
 * using specific values, the implementation should implement its own file decoder which gets called after the image
 * has been downloaded in order to customize its behavior depending on its result
 *
 * @param <T>
 * 	Type of the resource to decode:
 * 	<ul><li>{@link java.lang.String}: for URLs that needs to be downloaded and decoded</li>
 * 	<li>{@link android.graphics.Bitmap} For decoded images, this will only added to cache</li>
 * 	<li>{@link java.lang.Integer} To decode an image resource and add it to cache</li></ul>
 * @author "M. en C. Javier Silva Perez (JSP)"
 * @version 1.0
 * @since 25/09/13
 */
public class ImageLoader<T> {

	MemoryCache memoryCache = new MemoryCache();
	FileCache fileCache;
	private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
	/**
	 * Saves the keys that are been downloaded, in order to prevent duplicated downloads
	 */
	private List<String> downloading = new LinkedList<String>();

	/**
	 * Saves the keys that have been download and fail, so it will not try it again
	 */
	private List<String> fails = new LinkedList<String>();
	private Map<String, List<PendingImageView>> pending = new HashMap<String, List<PendingImageView>>();
	private Context context;

	public ImageLoader(Context context) {
		fileCache = new FileCache(context);
		this.context = context;
	}

	/**
	 * Displays an image from an URL to an ImageView, the downloaded images are cached in order to prevent unnecessary
	 * downloads
	 *
	 * @param key
	 * 	Key to identify the images to download, this should be unique per download image
	 * @param imageResource
	 * 	Image resource to decode
	 * @param imageView
	 * 	ImageView where the image will be loaded
	 * @param progressBar
	 * 	View that shows a progress bar while the image its been downloaded and decoded
	 * @param requestedWidth
	 * 	If necessary, the desired width of the bitmap to be decoded, IMPORTANT for reduce memory consumption
	 * @param requestedHeight
	 * 	If necessary, the desired width of the bitmap to be decoded, IMPORTANT for reduce memory consumption
	 * @param onImageDecodedListener
	 * 	Gets called after the image its downloaded and decoded, so the implementing method could personalize its
	 * 	layout behavior depending on the result
	 */
	public void displayImage(String key, T imageResource, ImageView imageView, View progressBar, int requestedWidth,
	                         int requestedHeight, OnImageDecodedListener onImageDecodedListener) {
		displayImage(key, imageResource, imageView, progressBar, requestedWidth, requestedHeight,
			onImageDecodedListener, null, -1);
	}

	/**
	 * Displays an image from an URL to an ImageView, the downloaded images are cached in order to prevent unnecessary
	 * downloads
	 *
	 * @param key
	 * 	Key to identify the images to download, this should be unique per download image
	 * @param imageResource
	 * 	Image resource to decode
	 * @param imageView
	 * 	ImageView where the image will be loaded
	 * @param progressBar
	 * 	View that shows a progress bar while the image its been downloaded and decoded
	 * @param requestedWidth
	 * 	If necessary, the desired width of the bitmap to be decoded, IMPORTANT for reduce memory consumption
	 * @param requestedHeight
	 * 	If necessary, the desired width of the bitmap to be decoded, IMPORTANT for reduce memory consumption
	 * @param onImageDecodedListener
	 * 	Gets called after the image its downloaded and decoded, so the implementing method could personalize its
	 * 	layout behavior depending on the result
	 * @param absListView
	 * 	List view that its used for display the images of this image loader, this will be used for checking if the
	 * 	decoded image is on the screen at the end of the process, and if its not do not set the resulting bitmap into
	 * 	the image view
	 * @param position
	 * 	Position of the item that contains the image view on the list, this will used for check if the image view its
	 * 	shown on the list view if not do not set the bitmap to the image view
	 */
	public void displayImage(String key, T imageResource, ImageView imageView, View progressBar, int requestedWidth,
	                         int requestedHeight, OnImageDecodedListener onImageDecodedListener,
	                         AbsListView absListView, Integer position) {
		imageViews.put(imageView, key);
		//Get the bitmap from cache
		Bitmap bitmap = memoryCache.get(key);
		if (bitmap != null) {
			//If its not null, set it to the image view
			imageView.setImageBitmap(bitmap);
			//Change visibility values to image view and progress bar
			imageView.setVisibility(View.VISIBLE);
			if (progressBar != null) progressBar.setVisibility(View.GONE);
			callOnImageDecodedListener(key, new WeakReference<ImageView>(imageView), progressBar, bitmap, position,
				onImageDecodedListener);
		} else {
			//Check if the image has been downloaded and fail, in that case return null
			if (fails.contains(key)) {
				if (progressBar != null) progressBar.setVisibility(View.GONE);
				callOnImageDecodedListener(key, new WeakReference<ImageView>(imageView), progressBar, null, position,
					onImageDecodedListener);
				return;
			}
			//If the bitmap is not on the cache, add it to the queue for download and decode
			queuePhoto(key, imageResource, imageView, progressBar, requestedWidth, requestedHeight,
				onImageDecodedListener, absListView, position);
		}
	}

	/**
	 * Cleans up the fails list, so all the images that has fail on decoding process could be tried again
	 */
	public void cleanFails() {
		this.fails = new LinkedList<String>();
	}

	/**
	 * Calls on image decoded listener if its not null
	 *
	 * @param key
	 * 	Key to identify the images to download, this should be unique per download image
	 * @param imageViewWeakReference
	 * 	Weak reference object for the image view to show, so the gb could collect it
	 * @param progressBar
	 * 	View that shows a progress bar while the image its been downloaded and decoded
	 * @param bitmap
	 * 	Decoded bitmap
	 * @param onImageDecodedListener
	 * 	Gets called after the image its downloaded and decoded, so the implementing method could personalize its
	 * 	layout behavior depending on the result
	 * @param position
	 * 	Position of the item that contains the image view on the list, this will used for check if the image view its
	 * 	shown on the list view if not do not set the bitmap to the image view
	 */
	private void callOnImageDecodedListener(String key, WeakReference<ImageView> imageViewWeakReference,
	                                        View progressBar, Bitmap bitmap, Integer position,
	                                        OnImageDecodedListener onImageDecodedListener) {
		if (onImageDecodedListener != null) {
			onImageDecodedListener.processImageResult(key, imageViewWeakReference, progressBar, bitmap, position);
		}
	}

	/**
	 * Check if the selected position its visible on the list view
	 *
	 * @param absListView
	 * 	List view that its used for display the images of this image loader, this will be used for checking if the
	 * 	decoded image is on the screen at the end of the process, and if its not do not set the resulting bitmap into
	 * 	the image view
	 * @param position
	 * 	Position of the item that contains the image view on the list, this will used for check if the image view its
	 * 	shown on the list view if not do not set the bitmap to the image view
	 * @return TRUE if the position its visible on the list view, FALSE otherwise
	 */
	public static Boolean imageViewVisible(AbsListView absListView, Integer position) {
		if (absListView == null) {
			return Boolean.TRUE;
		}
		int firstVisible = absListView.getFirstVisiblePosition();
		int lastVisible = absListView.getLastVisiblePosition();
		return firstVisible <= position && lastVisible >= position;
	}

	/**
	 * Add the url and image view to the queue of images to load
	 *
	 * @param key
	 * 	Key to identify the images to download, this should be unique per download image
	 * @param imageResource
	 * 	Image resource to decode
	 * @param imageView
	 * 	ImageView where the image will be loaded
	 * @param progressBar
	 * 	View that shows a progress bar while the image its been downloaded and decoded
	 * @param requestedWidth
	 * 	If necessary, the desired width of the bitmap to be decoded, IMPORTANT for reduce memory consumption
	 * @param requestedHeight
	 * 	If necessary, the desired width of the bitmap to be decoded, IMPORTANT for reduce memory consumption
	 * @param onImageDecodedListener
	 * 	Gets called after the image its downloaded and decoded, so the implementing method could personalize its
	 * 	layout behavior depending on the result
	 * @param absListView
	 * 	List view that its used for display the images of this image loader, this will be used for checking if the
	 * 	decoded image is on the screen at the end of the process, and if its not do not set the resulting bitmap into
	 * 	the image view
	 * @param position
	 * 	Position of the item that contains the image view on the list, this will used for check if the image view its
	 * 	shown on the list view if not do not set the bitmap to the image view
	 */
	private void queuePhoto(String key, T imageResource, ImageView imageView, View progressBar, int requestedWidth,
	                        int requestedHeight, OnImageDecodedListener onImageDecodedListener,
	                        AbsListView absListView,
	                        Integer position) {
		//Check if the image view has already been displayed
		if (imageViewReused(imageView, key)) return;

		//Create a weak reference object for the image view, so the gb could collect it
		WeakReference<ImageView> imgReference = new WeakReference<ImageView>(imageView);
		File f = fileCache.getFile(key);
		if (f.exists()) {
			//Tries to load the bitmap from cache, if the bit map is not present download it
			decodeFile(key, imgReference, progressBar, requestedWidth, requestedHeight, f, onImageDecodedListener,
				absListView, position);
		} else {
			//If the bitmap has never been cached, download it
			if (!downloading.contains(key)) {
				//Add it to download map
				downloading.add(key);
				pending.put(key, new LinkedList<PendingImageView>());
				if (imageResource instanceof Integer) {
					decodeResource(key, (Integer) imageResource, imgReference, progressBar, requestedWidth,
						requestedHeight, onImageDecodedListener, absListView, position);
				} else if (imageResource instanceof String) {
					DownloadImageAsyncTask loadFirm = new DownloadImageAsyncTask(
						new CustomDownloadAsyncTaskEventListenerImp(progressBar, imageView, requestedWidth,
							requestedHeight, key, onImageDecodedListener, absListView, position), f.getAbsolutePath()
					);
					loadFirm.execute((String) imageResource);
				} else if (imageResource instanceof Bitmap) {
					processBitmap(key, imgReference, progressBar, onImageDecodedListener, (Bitmap) imageResource,
						absListView, position);
				}
			} else {
				pending.get(key).add(new PendingImageView(imgReference, progressBar, onImageDecodedListener));
			}
		}
	}

	/**
	 * Decodes the resource bitmap using an AsyncTask, sets the resulting bitmap into the selected image view
	 * reference and updates the progress bar visibility
	 *
	 * @param key
	 * 	Key to identify the images to download, this should be unique per download image
	 * @param resourceId
	 * 	Drawable resource to be loaded
	 * @param imgViewReference
	 * 	Weak reference object for the image view to show, so the gb could collect it
	 * @param progressBar
	 * 	View that shows a progress bar while the image its been downloaded and decoded
	 * @param requestedWidth
	 * 	If necessary, the desired width of the bitmap to be decoded, IMPORTANT for reduce memory consumption
	 * @param requestedHeight
	 * 	If necessary, the desired width of the bitmap to be decoded, IMPORTANT for reduce memory consumption
	 * @param onImageDecodedListener
	 * 	Gets called after the image its downloaded and decoded, so the implementing method could personalize its
	 * 	layout behavior depending on the result
	 * @param absListView
	 * 	List view that its used for display the images of this image loader, this will be used for checking if the
	 * 	decoded image is on the screen at the end of the process, and if its not do not set the resulting bitmap into
	 * 	the image view
	 * @param position
	 * 	Position of the item that contains the image view on the list, this will used for check if the image view its
	 * 	shown on the list view if not do not set the bitmap to the image view
	 */
	private void decodeResource(final String key, int resourceId, final WeakReference<ImageView> imgViewReference,
	                            final View progressBar, int requestedWidth, int requestedHeight,
	                            final OnImageDecodedListener onImageDecodedListener, final AbsListView absListView,
	                            final Integer position) {
		// Decode de image using the original size
		BitmapDecoderTask<Integer> bitmapDecoderTask =
			new BitmapDecoderTask<Integer>(context, requestedWidth, requestedHeight,
				new CustomAsyncTaskEventListener<Bitmap>() {

					@Override
					public void onPostExecute(Bitmap result) {
						// Once complete, see if ImageView is still around and set bitmap.
						processBitmap(key, imgViewReference, progressBar, onImageDecodedListener, result, absListView,
							position);
					}

					@Override
					public void onPreExecute() {

					}
				}
			);

		bitmapDecoderTask.execute(resourceId);
	}

	/**
	 * Decodes the requested file using an AsyncTask, sets the resulting bitmap into the selected image view
	 * reference and updates the progress bar visibility
	 *
	 * @param key
	 * 	Key to identify the images to download, this should be unique per download image
	 * @param imgViewReference
	 * 	Weak reference object for the image view to show, so the gb could collect it
	 * @param progressBar
	 * 	View that shows a progress bar while the image its been downloaded and decoded
	 * @param requestedWidth
	 * 	If necessary, the desired width of the bitmap to be decoded, IMPORTANT for reduce memory consumption
	 * @param requestedHeight
	 * 	If necessary, the desired width of the bitmap to be decoded, IMPORTANT for reduce memory consumption
	 * @param f
	 * 	File that will be decoded
	 * @param onImageDecodedListener
	 * 	Gets called after the image its downloaded and decoded, so the implementing method could personalize its
	 * 	layout behavior depending on the result
	 * @param absListView
	 * 	List view that its used for display the images of this image loader, this will be used for checking if the
	 * 	decoded image is on the screen at the end of the process, and if its not do not set the resulting bitmap into
	 * 	the image view
	 * @param position
	 * 	Position of the item that contains the image view on the list, this will used for check if the image view its
	 * 	shown on the list view if not do not set the bitmap to the image view
	 */
	private void decodeFile(final String key, final WeakReference<ImageView> imgViewReference, final View progressBar,
	                        int requestedWidth, int requestedHeight, File f,
	                        final OnImageDecodedListener onImageDecodedListener, final AbsListView absListView,
	                        final Integer position) {
		// Decode de image using the original size
		BitmapDecoderTask<File> bitmapDecoderTask =
			new BitmapDecoderTask<File>(context, requestedWidth, requestedHeight,
				new BitmapDecodedEventListenerImp(progressBar, imgViewReference, requestedWidth, requestedHeight, key,
					onImageDecodedListener, absListView, position)
			);

		bitmapDecoderTask.execute(f);
	}

	/**
	 * Decodes the requested input stream using an AsyncTask, sets the resulting bitmap into the selected image view
	 * reference and updates the progress bar visibility
	 *
	 * @param key
	 * 	Key to identify the images to download, this should be unique per download image
	 * @param imgViewReference
	 * 	Weak reference object for the image view to show, so the gb could collect it
	 * @param progressBar
	 * 	View that shows a progress bar while the image its been downloaded and decoded
	 * @param requestedWidth
	 * 	If necessary, the desired width of the bitmap to be decoded, IMPORTANT for reduce memory consumption
	 * @param requestedHeight
	 * 	If necessary, the desired width of the bitmap to be decoded, IMPORTANT for reduce memory consumption
	 * @param is
	 * 	InputStream that will be decoded
	 * @param onImageDecodedListener
	 * 	Gets called after the image its downloaded and decoded, so the implementing method could personalize its
	 * 	layout behavior depending on the result
	 * @param absListView
	 * 	List view that its used for display the images of this image loader, this will be used for checking if the
	 * 	decoded image is on the screen at the end of the process, and if its not do not set the resulting bitmap into
	 * 	the image view
	 * @param position
	 * 	Position of the item that contains the image view on the list, this will used for check if the image view its
	 * 	shown on the list view if not do not set the bitmap to the image view
	 */
	private void decodeStream(final String key, final WeakReference<ImageView> imgViewReference,
	                          final View progressBar,
	                          int requestedWidth, int requestedHeight, InputStream is,
	                          final OnImageDecodedListener onImageDecodedListener, AbsListView absListView,
	                          Integer position) {
		// Decode de image using the original size
		BitmapDecoderTask<InputStream> bitmapDecoderTask =
			new BitmapDecoderTask<InputStream>(context, requestedWidth, requestedHeight,
				new BitmapDecodedEventListenerImp(progressBar, imgViewReference, requestedWidth, requestedHeight, key,
					onImageDecodedListener, absListView, position)
			);

		bitmapDecoderTask.execute(is);
	}

	/**
	 * Ones the bitmap its decoded, display it to the image view, hide the progress bar and add the bitmap to the cache
	 *
	 * @param key
	 * 	Key to identify the images to download, this should be unique per download image
	 * @param imgViewReference
	 * 	Weak reference object for the image view to show, so the gb could collect it
	 * @param progressBar
	 * 	View that shows a progress bar while the image its been downloaded and decoded
	 * @param onImageDecodedListener
	 * 	Gets called after the image its downloaded and decoded, so the implementing method could personalize its
	 * 	layout behavior depending on the result
	 * @param result
	 * 	The decoded bitmap
	 * @param absListView
	 * 	List view that its used for display the images of this image loader, this will be used for checking if the
	 * 	decoded image is on the screen at the end of the process, and if its not do not set the resulting bitmap into
	 * 	the image view
	 * @param position
	 * 	Position of the item that contains the image view on the list, this will used for check if the image view its
	 * 	shown on the list view if not do not set the bitmap to the image view
	 */
	private void processBitmap(String key, WeakReference<ImageView> imgViewReference, View progressBar,
	                           OnImageDecodedListener onImageDecodedListener, Bitmap result, AbsListView absListView,
	                           Integer position) {
		// Once complete, see if ImageView is still around
		// and set bitmap.
		if (imgViewReference != null) {
			final ImageView imgView = imgViewReference.get();
			if (imgView != null) {

				//If the image view its visible on the list, set the image view and update progress bar status
				if (imageViewVisible(absListView, position)) {
					//Set resulting bitmap to image view
					imgView.setImageBitmap(result);
					imgView.setVisibility(View.VISIBLE);
				}
				memoryCache.put(key, result);
				downloading.remove(key);
				//If the resulting bitmap is null, add it to fails list
				if (result == null) {
					fails.add(key);
				}

				List<PendingImageView> pendingImageViews = pending.get(key);
				if (pendingImageViews != null) {
					for (PendingImageView pendingImageView : pendingImageViews) {
						//Check if there's pending image views to load for that key
						if (pendingImageView != null) {
							//Get the image view reference
							WeakReference<ImageView> pendingImageViewReference = pendingImageView.getImageView();
							if (pendingImageViewReference != null) {
								final ImageView imgViewP = pendingImageViewReference.get();
								//Update image view
								if (imgViewP != null && imageViewVisible(absListView, position)) {
									imgViewP.setImageBitmap(result);
									imgViewP.setVisibility(View.VISIBLE);
								}
							}
							View pendingProgressBar = pendingImageView.progressBar;
							if (pendingProgressBar != null) pendingProgressBar.setVisibility(View.GONE);

							OnImageDecodedListener onImageDecodedListenerPending =
								pendingImageView.onImageDecodedListener;
							callOnImageDecodedListener(key, pendingImageViewReference, pendingProgressBar, result,
								position, onImageDecodedListenerPending);
						}
					}
				}
				pending.remove(key);
			}
			callOnImageDecodedListener(key, imgViewReference, progressBar, result, position, onImageDecodedListener);
		}
		if (progressBar != null) progressBar.setVisibility(View.GONE);
	}

	/**
	 * Check if the image view has already been used, so do nothing
	 *
	 * @param imageView
	 * 	ImageView where the image will be loaded
	 * @param key
	 * 	Key to identify the images to download, this should be unique per download image
	 * @return TRUE if the image view has already been loaded with that image key
	 */
	boolean imageViewReused(ImageView imageView, String key) {
		String tag = imageViews.get(imageView);
		return (tag == null || !tag.equals(key));
	}

	/**
	 * Clears both memory and file cache bitmaps
	 */
	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}

	/**
	 * Interface that defines what to do after the image gets decoded
	 *
	 * @author "M. en C. Javier Silva Perez (JSP)"
	 * @version 1.0
	 * @since 09/04/2013
	 */
	public interface OnImageDecodedListener {
		/**
		 * Gets called after the image its downloaded and decoded, so the implementing method could personalize its
		 * layout behavior depending on the result
		 *
		 * @param key
		 * 	Key to identify the images to download, this should be unique per download image
		 * @param imgViewReference
		 * 	Weak reference object for the image view to show, so the gb could collect it
		 * @param progressBar
		 * 	View that shows a progress bar while the image its been downloaded and decoded
		 * @param resultingBitmap
		 * 	Bitmap that results from the image decoded
		 * @param position
		 * 	Position of the item that contains the image view on the list, this will used for check if the image view
		 * 	its shown on the list view if not do not set the bitmap to the image view, this should only be considered
		 * 	when the image loader involves list view items
		 */
		public void processImageResult(String key, final WeakReference<ImageView> imgViewReference, View progressBar,
		                               Bitmap resultingBitmap, Integer position);
	}

	/**
	 * Inner class that will save the references to the image view and progress bat that should be updated when the
	 * images are decoded
	 */
	private class PendingImageView {
		WeakReference<ImageView> imageView;
		View progressBar;
		OnImageDecodedListener onImageDecodedListener;


		private PendingImageView(WeakReference<ImageView> imageView, View progressBar,
		                         OnImageDecodedListener onImageDecodedListener) {
			this.imageView = imageView;
			this.progressBar = progressBar;
			this.onImageDecodedListener = onImageDecodedListener;
		}

		public WeakReference<ImageView> getImageView() {
			return imageView;
		}

		public View getProgressBar() {
			return progressBar;
		}

		public OnImageDecodedListener getOnImageDecodedListener() {
			return onImageDecodedListener;
		}
	}

	/**
	 * Custom implementation for download event listener, this implementation
	 * contains the specific implementations for onPreExecute and onPostExecute
	 * for this case
	 *
	 * @author "M. en C. Javier Silva Perez (JSP)"
	 * @version 1.0
	 * @since 06/05/2013
	 */
	protected class CustomDownloadAsyncTaskEventListenerImp extends CustomAsyncTaskEventListener<InputStream> {

		private final WeakReference<ImageView> imgOriginalReference;
		View progressBar;
		private Integer requestedWidth;
		private Integer requestedHeight;
		private String key;
		private OnImageDecodedListener onImageDecodedListener;
		private AbsListView absListView;
		private Integer position;


		public CustomDownloadAsyncTaskEventListenerImp(View progressBar, ImageView imgOriginal, int requestedWidth,
		                                               int requestedHeight, String key,
		                                               OnImageDecodedListener onImageDecodedListener,
		                                               AbsListView absListView, Integer position) {
			this.progressBar = progressBar;
			this.requestedHeight = requestedHeight;
			this.requestedWidth = requestedWidth;
			// Use a WeakReference to ensure the ImageView can be garbage
			// collected
			imgOriginalReference = new WeakReference<ImageView>(imgOriginal);
			this.key = key;
			this.onImageDecodedListener = onImageDecodedListener;
			this.absListView = absListView;
			this.position = position;
		}

		@Override
		public void onPreExecute() {
			if (progressBar != null) progressBar.setVisibility(View.VISIBLE);

		}

		@Override
		public void onPostExecute(InputStream result) {
			decodeStream(key, imgOriginalReference, progressBar, requestedWidth, requestedHeight, result,
				onImageDecodedListener, absListView, position);
		}
	}

	/**
	 * Custom implementation for async event listener, this implementation
	 * contains the specific implementations for onPreExecute and onPostExecute
	 * on this case, this implementation will receive the results of the decoded bitmap async task
	 *
	 * @author "M. en C. Javier Silva Perez (JSP)"
	 * @version 1.5
	 * @since 21/01/2014
	 */
	protected class BitmapDecodedEventListenerImp extends CustomAsyncTaskEventListener<Bitmap> {

		private final WeakReference<ImageView> imgOriginalReference;
		View progressBar;
		private String key;
		private Integer requestedWidth;
		private Integer requestedHeight;
		private OnImageDecodedListener onImageDecodedListener;
		private AbsListView absListView;
		private Integer position;


		public BitmapDecodedEventListenerImp(View progressBar, WeakReference<ImageView> imgOriginalReference,
		                                     int requestedWidth, int requestedHeight, String key,
		                                     OnImageDecodedListener onImageDecodedListener, AbsListView absListView,
		                                     Integer position) {
			this.progressBar = progressBar;
			this.requestedHeight = requestedHeight;
			this.requestedWidth = requestedWidth;
			// Use a WeakReference to ensure the ImageView can be garbage
			// collected
			this.imgOriginalReference = imgOriginalReference;
			this.key = key;
			this.onImageDecodedListener = onImageDecodedListener;
			this.absListView = absListView;
			this.position = position;
		}

		@Override
		public void onPostExecute(Bitmap result) {

			//If the result is null, check its error cause
			if (result == null) {
				Exception ex = getErrorCause();
				if (ex instanceof StreamTooLargeException) {
					File f = fileCache.getFile(key);
					if (f.exists()) {
						//Tries to load the bitmap from cache, if the bit map is not present download it
						decodeFile(key, imgOriginalReference, progressBar, requestedWidth, requestedHeight, f,
							onImageDecodedListener, absListView, position);
						return;
					}
				}
			}

			// Once complete, see if ImageView is still around and set bitmap.
			processBitmap(key, imgOriginalReference, progressBar, onImageDecodedListener, result, absListView,
				position);
		}

		@Override
		public void onPreExecute() {

		}
	}

}
