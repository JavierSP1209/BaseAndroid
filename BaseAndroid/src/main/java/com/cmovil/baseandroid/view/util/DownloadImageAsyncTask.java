/**
 * File: DownloadImageAsyncTask.java
 * CreationDate: 06/05/2013
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description: 
 * AsyncTask for download an image and prevent the UI freeze while trying this, it will contain two constructors and
 * depending on its parameters this task will save or not the downloaded image into a cache file. If cache its been
 * used, the result of the downloading task will be a {@link java.io.FileInputStream} object, on the other hand if no
 * cache its been used the downloading task will return the BufferedInputStream that comes from the HttpURLConnection,
 * be careful with this stream because it can only be used ones!
 */
package com.cmovil.baseandroid.view.util;

import android.os.AsyncTask;
import android.util.Log;

import com.cmovil.baseandroid.util.KeyDictionary;
import com.cmovil.baseandroid.view.loader.Utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * AsyncTask for download an image and prevent the UI freeze while trying this, it will contain two constructors and
 * depending on its parameters this task will save or not the downloaded image into a cache file. If cache its been
 * used, the result of the downloading task will be a {@link java.io.FileInputStream} object, on the other hand if no
 * cache its been used the downloading task will return the BufferedInputStream that comes from the HttpURLConnection,
 * be careful with this stream because it can only be used ones!
 *
 * @author "M. en C. Javier Silva Perez (JSP)"
 * @version 1.5
 * @since 20/01/2014
 */
public class DownloadImageAsyncTask extends AsyncTask<String, Void, InputStream> {
	CustomAsyncTaskEventListener<InputStream> customAsyncTaskEventListener;
	String cacheFile;

	/**
	 * Default constructor receives the event listener for this async task, using this constructor the task will
	 * return
	 * the InputStream directly from the HttpURLConnection so be careful with it because this can only be used once.
	 *
	 * @param customAsyncTaskEventListener
	 * 	Listener with the basic asyncTask methods that will be invoked in the different steps of this async task
	 */
	public DownloadImageAsyncTask(CustomAsyncTaskEventListener<InputStream> customAsyncTaskEventListener) {
		this.customAsyncTaskEventListener = customAsyncTaskEventListener;
	}

	/**
	 * Constructor receives the event listener for this async task and the cache file, using this constructor the task
	 * will save the downloaded stream to a cache file and return the input stream read from this file.
	 *
	 * @param customAsyncTaskEventListener
	 * 	Listener with the basic asyncTask methods that will be invoked in the different steps of this async task
	 * @param cacheFile
	 * 	Path to the cache file where the downloaded stream will be saved
	 */
	public DownloadImageAsyncTask(CustomAsyncTaskEventListener<InputStream> customAsyncTaskEventListener, String cacheFile) {
		this.customAsyncTaskEventListener = customAsyncTaskEventListener;
		this.cacheFile = cacheFile;
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
	protected InputStream doInBackground(String... params) {
		try {
			URL openPoster = new URL(params[0]);
			HttpURLConnection conn = (HttpURLConnection) openPoster.openConnection();
			conn.setConnectTimeout(1000);
			conn.setReadTimeout(5000);
			conn.setInstanceFollowRedirects(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);

			//If cache file is not null, the file should be saved on cache
			if (cacheFile != null) {
				try {
					//Save the downloaded stream to the file
					OutputStream os = new FileOutputStream(cacheFile);
					Utils.CopyStream(bis, os);
					os.close();
					//Load the file and return its stream
					return new FileInputStream(cacheFile);
				} catch (FileNotFoundException e) {
					Log.e(KeyDictionary.TAG, "Saving to cache error: " + e.getMessage(), e);
				} catch (IOException e) {
					Log.e(KeyDictionary.TAG, "Saving to cache error: " + e.getMessage(), e);
				}
			}

			return bis;
		} catch (MalformedURLException e) {
			Log.e(KeyDictionary.TAG, "Download error: " + e.getMessage());
			if(customAsyncTaskEventListener!=null) customAsyncTaskEventListener.setErrorCause(e);
		} catch (IOException e) {
			Log.e(KeyDictionary.TAG, "Download error: " + e + "");
			if(customAsyncTaskEventListener!=null) customAsyncTaskEventListener.setErrorCause(e);
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(InputStream result) {
		if (customAsyncTaskEventListener != null) {
			Log.d(KeyDictionary.TAG, "DownloadFinish!: "+result);
			customAsyncTaskEventListener.onPostExecute(result);
		}
	}
}
