package com.cmovil.baseandroid.view.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
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
	private Map<String, PendingImageView> pending = new HashMap<String, PendingImageView>();
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
		imageViews.put(imageView, key);
		//Get the bitmap from cache
		Bitmap bitmap = memoryCache.get(key);
		if (bitmap != null) {
			//If its not null, set it to the image view
			imageView.setImageBitmap(bitmap);
			//Change visibility values to image view and progress bar
			imageView.setVisibility(View.VISIBLE);
			if (progressBar != null) progressBar.setVisibility(View.GONE);
			if (onImageDecodedListener != null) onImageDecodedListener
				.processImageResult(key, new WeakReference<ImageView>(imageView), progressBar, bitmap);
		} else {
			//If the bitmap is not on the cache, add it to the queue for download and decode
			queuePhoto(key, imageResource, imageView, progressBar, requestedWidth, requestedHeight,
				onImageDecodedListener);
		}
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
	 */
	private void queuePhoto(String key, T imageResource, ImageView imageView, View progressBar, int requestedWidth,
	                        int requestedHeight, OnImageDecodedListener onImageDecodedListener) {
		//Check if the image view has already been displayed
		if (imageViewReused(imageView, key)) return;

		//Create a weak reference object for the image view, so the gb could collect it
		WeakReference<ImageView> imgReference = new WeakReference<ImageView>(imageView);
		File f = fileCache.getFile(key);
		if (f.exists()) {
			//Tries to load the bitmap from cache, if the bit map is not present download it
			decodeFile(key, imgReference, progressBar, requestedWidth, requestedHeight, f, onImageDecodedListener);
		} else {
			//If the bitmap has never been cached, download it
			if (!downloading.contains(key)) {
				//Add it to download map
				downloading.add(key);
				if (imageResource instanceof Integer) {
					decodeResource(key, (Integer) imageResource, imgReference, progressBar, requestedWidth,
						requestedHeight, onImageDecodedListener);
				} else if (imageResource instanceof String) {
					DownloadImageAsyncTask loadFirm = new DownloadImageAsyncTask(
						new CustomDownloadAsyncTaskEventListenerImp(progressBar, imageView, requestedWidth,
							requestedHeight, key, onImageDecodedListener), f.getAbsolutePath());
					loadFirm.execute((String) imageResource);
				} else if (imageResource instanceof Bitmap) {
					processBitmap(key, imgReference, progressBar, onImageDecodedListener, (Bitmap) imageResource);
				}
			} else {
				pending.put(key, new PendingImageView(imgReference, progressBar, onImageDecodedListener));
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
	 */
	private void decodeResource(final String key, int resourceId, final WeakReference<ImageView> imgViewReference,
	                            final View progressBar, int requestedWidth, int requestedHeight,
	                            final OnImageDecodedListener onImageDecodedListener) {
		// Decode de image using the original size
		BitmapDecoderTask<Integer> bitmapDecoderTask =
			new BitmapDecoderTask<Integer>(context, requestedWidth, requestedHeight,
				new CustomAsyncTaskEventListener<Bitmap>() {

					@Override
					public void onPostExecute(Bitmap result) {
						// Once complete, see if ImageView is still around and set bitmap.
						processBitmap(key, imgViewReference, progressBar, onImageDecodedListener, result);
					}

					@Override
					public void onPreExecute() {

					}
				});

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
	 */
	private void decodeFile(final String key, final WeakReference<ImageView> imgViewReference, final View progressBar,
	                        int requestedWidth, int requestedHeight, File f,
	                        final OnImageDecodedListener onImageDecodedListener) {
		// Decode de image using the original size
		BitmapDecoderTask<File> bitmapDecoderTask =
			new BitmapDecoderTask<File>(context, requestedWidth, requestedHeight,
				new BitmapDecodedEventListenerImp(progressBar, imgViewReference, requestedWidth, requestedHeight, key,
					onImageDecodedListener));

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
	 */
	private void decodeStream(final String key, final WeakReference<ImageView> imgViewReference,
	                          final View progressBar,
	                          int requestedWidth, int requestedHeight, InputStream is,
	                          final OnImageDecodedListener onImageDecodedListener) {
		// Decode de image using the original size
		BitmapDecoderTask<InputStream> bitmapDecoderTask =
			new BitmapDecoderTask<InputStream>(context, requestedWidth, requestedHeight,
				new BitmapDecodedEventListenerImp(progressBar, imgViewReference, requestedWidth, requestedHeight, key,
					onImageDecodedListener));

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
	 */
	private void processBitmap(String key, WeakReference<ImageView> imgViewReference, View progressBar,
	                           OnImageDecodedListener onImageDecodedListener, Bitmap result) {
		// Once complete, see if ImageView is still around
		// and set bitmap.
		if (imgViewReference != null) {
			final ImageView imgView = imgViewReference.get();
			if (imgView != null) {

				//Set resulting bitmap to image view
				imgView.setImageBitmap(result);
				imgView.setVisibility(View.VISIBLE);
				memoryCache.put(key, result);
				downloading.remove(key);

				PendingImageView pendingImageView = pending.get(key);
				//Check if there's pending image views to load for that key
				if (pendingImageView != null) {
					//Get the image view reference
					WeakReference<ImageView> pendingImageViewReference = pendingImageView.getImageView();
					if (pendingImageViewReference != null) {
						final ImageView imgViewP = pendingImageViewReference.get();
						//Update image view
						if (imgViewP != null) {
							imgViewP.setImageBitmap(result);
							imgViewP.setVisibility(View.VISIBLE);
						}
					}
					View pendingProgressBar = pendingImageView.progressBar;
					if (pendingProgressBar != null) pendingProgressBar.setVisibility(View.GONE);

					OnImageDecodedListener onImageDecodedListenerPending = pendingImageView.onImageDecodedListener;
					if (onImageDecodedListenerPending != null) onImageDecodedListenerPending
						.processImageResult(key, pendingImageViewReference, pendingProgressBar, result);
				}
				pending.remove(key);
			}
			if (onImageDecodedListener != null)
				onImageDecodedListener.processImageResult(key, imgViewReference, progressBar, result);
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
		 */
		public void processImageResult(String key, final WeakReference<ImageView> imgViewReference, View progressBar,
		                               Bitmap resultingBitmap);

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


		public CustomDownloadAsyncTaskEventListenerImp(View progressBar, ImageView imgOriginal, int requestedWidth,
		                                               int requestedHeight, String key,
		                                               OnImageDecodedListener onImageDecodedListener) {
			this.progressBar = progressBar;
			this.requestedHeight = requestedHeight;
			this.requestedWidth = requestedWidth;
			// Use a WeakReference to ensure the ImageView can be garbage
			// collected
			imgOriginalReference = new WeakReference<ImageView>(imgOriginal);
			this.key = key;
			this.onImageDecodedListener = onImageDecodedListener;
		}

		@Override
		public void onPreExecute() {
			if (progressBar != null) progressBar.setVisibility(View.VISIBLE);

		}

		@Override
		public void onPostExecute(InputStream result) {
			decodeStream(key, imgOriginalReference, progressBar, requestedWidth, requestedHeight, result,
				onImageDecodedListener);
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


		public BitmapDecodedEventListenerImp(View progressBar, WeakReference<ImageView> imgOriginalReference,
		                                     int requestedWidth, int requestedHeight, String key,
		                                     OnImageDecodedListener onImageDecodedListener) {
			this.progressBar = progressBar;
			this.requestedHeight = requestedHeight;
			this.requestedWidth = requestedWidth;
			// Use a WeakReference to ensure the ImageView can be garbage
			// collected
			this.imgOriginalReference = imgOriginalReference;
			this.key = key;
			this.onImageDecodedListener = onImageDecodedListener;
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
							onImageDecodedListener);
						return;
					}
				}
			}

			// Once complete, see if ImageView is still around and set bitmap.
			processBitmap(key, imgOriginalReference, progressBar, onImageDecodedListener, result);
		}

		@Override
		public void onPreExecute() {

		}
	}

}
