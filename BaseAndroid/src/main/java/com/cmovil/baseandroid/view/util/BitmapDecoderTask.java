/**
 * File: BitmapDecoderTask.java
 * CreationDate: 06/05/2013
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description: 
 *
 */
package com.cmovil.baseandroid.view.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.cmovil.baseandroid.util.KeyDictionary;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Decodes a bitmap using an InputStream or a File as input, if the input could not be decoded as a
 * bitmap a null object will be passed as result to {@link #onPostExecute(android.graphics.Bitmap)}
 *
 * @param <T>
 * 	Type of the input that will be decoded could be: <ul>
 * 	<li>{@link java.io.BufferedInputStream}: This is commonly used for streams that comes from URL connections, this
 * 	streams could be used only once, so this class uses marks to reuse them but if the stream is too big and a mark
 * 	could no be used an exception will be raised.</li>
 * 	<li>{@link java.io.FileInputStream}: For file streams</li></ul>
 * 	<li>{@link java.io.File}: For file items</li></ul>
 * @author "M. en C. Javier Silva Perez (JSP)"
 * @version 1.0
 * @since 06/05/2013
 */
public class BitmapDecoderTask<T> extends AsyncTask<T, Void, Bitmap> {

	/**
	 * Requested width for the decoded bitmap
	 */
	private int reqWidth;

	/**
	 * Requested height for the decoded bitmap
	 */
	private int reqHeight;

	private boolean requestedMeasuresSet;
	private CustomAsyncTaskEventListener<Bitmap> customAsyncTaskEventListener;
	private Context context;

	private static final float SCALE_FACTOR = 0.75f;

	/**
	 * Constructor, using this constructor the requested image sizes will be established and the bitmap will only
	 * decode a
	 * sampled version of the original one
	 *
	 * @param reqWidth
	 * 	Requested width, ej the image view width
	 * @param reqHeight
	 * 	height width, ej the image view height
	 * @param customAsyncTaskEventListener
	 * 	Custom event listener that indicates what to do onPostExecute and onPreExecute function
	 */
	public BitmapDecoderTask(Context context, int reqWidth, int reqHeight,
	                         CustomAsyncTaskEventListener<Bitmap> customAsyncTaskEventListener) {
		this.customAsyncTaskEventListener = customAsyncTaskEventListener;
		this.reqHeight = reqHeight;
		this.reqWidth = reqWidth;
		this.context = context;

		requestedMeasuresSet = reqHeight > 0 || reqWidth > 0;
	}

	/**
	 * Constructor, using this constructor no requested height and width will be set, so the bitmap will decode the
	 * full
	 * original image, this could cause {@link OutOfMemoryError} exceptions
	 *
	 * @param customAsyncTaskEventListener
	 * 	Custom event listener that indicates what to do onPostExecute and onPreExecute function
	 */
	public BitmapDecoderTask(Context context, CustomAsyncTaskEventListener<Bitmap> customAsyncTaskEventListener) {
		this.customAsyncTaskEventListener = customAsyncTaskEventListener;
		this.reqHeight = 0;
		this.reqWidth = 0;
		requestedMeasuresSet = false;
		this.context = context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected void onPreExecute() {
		if (customAsyncTaskEventListener != null) {
			customAsyncTaskEventListener.onPreExecute();
		}

	}

	@Override
	protected Bitmap doInBackground(T... params) {
		Boolean complete;
		InputStream bis = null;

		//If not parameters to decode are set, return null
		if (params == null || params[0] == null) return null;
		try {
			do {
				complete = Boolean.TRUE;
				try {
					//If no request width or height are specified, set them to integer max value in order to decode the
					// image using its full size, this is because at the end the sampling value will always be 0
					if (reqWidth <= 0) reqWidth = Integer.MAX_VALUE;
					if (reqHeight <= 0) reqHeight = Integer.MAX_VALUE;

					Log.d(KeyDictionary.TAG, "Decoding: W: "+reqWidth+" - H: "+reqHeight);
					if (params[0] instanceof InputStream) {
						bis = (InputStream) params[0];
						return BitmapUtils.decodeSampledBitmapFromStream((InputStream) params[0], reqWidth, reqHeight);
					} else if (params[0] instanceof File) {
						return BitmapUtils
							.decodeSampledBitmapFromFile(((File) params[0]).getAbsolutePath(), reqWidth, reqHeight);
					} else {
						return BitmapUtils
							.decodeSampledBitmapFromResource(context.getResources(), (Integer) params[0], reqWidth,
								reqHeight);
					}

				} catch (OutOfMemoryError ex) {
					//If the image is too large, resize it using the half of the actual required height and width
					complete = Boolean.FALSE;

					Log.d(KeyDictionary.TAG, "OutOfMemory: W: "+reqWidth+" - H: "+reqHeight);

					//Reset bitmap
					if (bis != null) {
						bis.reset();
					}
					if (!requestedMeasuresSet) {

						//If no height or width are set, get the actual image sizes
						// First decode with inJustDecodeBounds=true to check dimensions
						final BitmapFactory.Options options = new BitmapFactory.Options();
						options.inJustDecodeBounds = true;
						if (params[0] instanceof InputStream) {
							//Mark the buffer for get the actual size
							if (bis != null) {
								bis.mark(bis.available());
							}
							BitmapFactory.decodeStream((InputStream) params[0], null, options);

							//Reset bitmap
							if (bis != null) {
								bis.reset();
							}
						} else if (params[0] instanceof File) {
							BitmapFactory.decodeFile(((File) params[0]).getAbsolutePath(), options);
						}
						reqHeight = (int) (options.outHeight * SCALE_FACTOR);
						reqWidth = (int) (options.outWidth * SCALE_FACTOR);

						requestedMeasuresSet = true;
					} else {
						reqHeight = (int) (reqHeight * SCALE_FACTOR);
						reqWidth = (int) (reqWidth * SCALE_FACTOR);
					}

					System.gc();

				}
			} while (!complete);
		} catch (IOException ex) {
			//If the image stream its to large and it could not be reset, it could not be decoded so return null
			// and set the error cause
			Log.e(KeyDictionary.TAG, ex + ": Stream too large to decode it, try using a cached file instead", ex);
			if (customAsyncTaskEventListener != null) {
				StreamTooLargeException aux = new StreamTooLargeException(
					ex + ": Stream too large to decode it, try using a cached file instead");
				aux.setStackTrace(ex.getStackTrace());
				customAsyncTaskEventListener.setErrorCause(aux);
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(Bitmap result) {
		if (customAsyncTaskEventListener != null) {
			Log.d(KeyDictionary.TAG, "Decoded: "+result);
			customAsyncTaskEventListener.onPostExecute(result);
		}
	}
}
