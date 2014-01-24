/**
 * File: SmartycUtils.java
 * CreationDate: 20/03/2013
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description: 
 * 	Class that contains common functions across the application as well as static
 * 	values that will be used by other classes
 */
package com.cmovil.baseandroid.view.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.Base64;
import android.util.Log;

import com.cmovil.baseandroid.util.KeyDictionary;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Class that contains common functions across the application as well as static
 * values that will be used by other classes
 *
 * @author "M. en C. Javier Silva Perez (JSP)"
 * @version 1.0
 * @since 20/03/2013
 */
public class BitmapUtils {
	private static final float PHOTO_BORDER_WIDTH = 0.0f;
	private static final Paint sPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
	private static final Paint sStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private static final Paint sNumPaint = new Paint(Paint.LINEAR_TEXT_FLAG);

	static {
		sNumPaint.setColor(Color.BLACK);
		sNumPaint.setTextSize(25);
		sNumPaint.setTextAlign(Align.RIGHT);
	}

	static {
		sStrokePaint.setStrokeWidth(PHOTO_BORDER_WIDTH);
		sStrokePaint.setStyle(Paint.Style.STROKE);
		sStrokePaint.setColor(Color.parseColor("#FFFFFFFF"));

	}

	/**
	 *
	 */

	/**
	 * Converts the number in dips to the number in pixels
	 *
	 * @param context
	 * 	Context to be used for getting application resources
	 * @param sizeInDips
	 * 	Dips to be converted
	 * @return The integer value of the corresponding pixel value for the
	 * desaired dips value
	 */
	public static int convertToPix(Context context, int sizeInDips) {
		float scale = context.getResources().getDisplayMetrics().density;
		float size = sizeInDips * scale;
		return (int) size;
	}

	/**
	 * Calculate InSampleSize for a requested image size and the original image
	 * sizes
	 *
	 * @param options
	 * 	Contains the original image size, loaded using option
	 * 	BitmapFactory.Options.inJustDecodeBounds flag
	 * @param reqWidth
	 * 	Requested image with
	 * @param reqHeight
	 * 	Requested image height
	 * @return the optimal sample size for loading the image
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	/**
	 * Gets a sampled bitmap from resources in order to optimize memory and performance
	 *
	 * @param res
	 * 	Resources list
	 * @param resId
	 * 	Resource id to be loaded
	 * @param reqWidth
	 * 	Requested with, ej. the with of the image view
	 * @param reqHeight
	 * 	Requeste height, ej. the height of the image view
	 * @return The sampled Bitmap
	 */
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	/**
	 * Gets a sampled bitmap from stream in order to optimize memory and performance,
	 * at least one reqWidth or reqHeight
	 * must be different from 0 or null bitmap will be returned
	 *
	 * @param reqWidth
	 * 	Requested with, ej. the with of the image view
	 * @param reqHeight
	 * 	Requested height, ej. the height of the image view
	 * @return The sampled Bitmap
	 *
	 * @throws java.io.IOException
	 * 	If the input stream could not be marked or reset for decoding purposes
	 */
	public static Bitmap decodeSampledBitmapFromStream(InputStream bis, int reqWidth, int reqHeight)
		throws IOException {
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();

		//As a InputStream from a HttpUrlConnection can only be used one time its necessary to rewind ir or the
		//actual decode will return a null bitmap
		int markSize = bis.available();
		bis.mark(markSize);

		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(bis, null, options);


		if (reqHeight <= 0 || reqHeight>=Integer.MAX_VALUE) {
			//If the requested height is zero calculate it in proportion to the requested width
			reqHeight = (int) ((reqWidth / (options.outWidth * 1.0)) * options.outHeight);
		} else{
			//If the requested width is zero calculate it in proportion to the requested height
			reqWidth = (int) ((reqHeight / (options.outHeight * 1.0)) * options.outWidth);
		}

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		//Reset bitmap
		bis.reset();

		bis.mark(bis.available());

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeStream(bis, null, options);
	}

	/**
	 * Gets a sampled bitmap from file in order to optimize memory and
	 * performance
	 *
	 * @param selectedImageFile
	 * 	Absolute image path
	 * @param reqWidth
	 * 	Requested with, ej. the with of the image view
	 * @param reqHeight
	 * 	Request height, ej. the height of the image view
	 * @return The sampled Bitmap
	 */
	public static Bitmap decodeSampledBitmapFromFile(String selectedImageFile, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(selectedImageFile, options);

		if (reqHeight <= 0 || reqHeight>=Integer.MAX_VALUE) {
			//If the requested height is zero calculate it in proportion to the requested width
			reqHeight = (int) ((reqWidth / (options.outWidth * 1.0)) * options.outHeight);
		} else {
			//If the requested width is zero calculate it in proportion to the requested height
			reqWidth = (int) ((reqHeight / (options.outHeight * 1.0)) * options.outWidth);
		}

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(selectedImageFile, options);

	}

	/**
	 * Rotates a bitmap in the desired angle (in degrees)
	 * <p/>
	 * TODO: Move to Utils class
	 *
	 * @param bitmap
	 * 	Bitmap to be rotated
	 * @param angle
	 * 	Angle for rotate the image, in degrees
	 * @return A new Bitmap that correspond to the rotated original bitmap
	 */
	public static Bitmap rotateAndFrame(Bitmap bitmap, float angle) {
		final double radAngle = Math.toRadians(angle);

		final int bitmapWidth = bitmap.getWidth();
		final int bitmapHeight = bitmap.getHeight();

		final double cosAngle = Math.abs(Math.cos(radAngle));
		final double sinAngle = Math.abs(Math.sin(radAngle));

		final int strokedWidth = (int) (bitmapWidth + 2 * PHOTO_BORDER_WIDTH);
		final int strokedHeight = (int) (bitmapHeight + 2 * PHOTO_BORDER_WIDTH);

		final int width = (int) (strokedHeight * sinAngle + strokedWidth * cosAngle);
		final int height = (int) (strokedWidth * sinAngle + strokedHeight * cosAngle);

		final float x = (width - bitmapWidth) / 2.0f;
		final float y = (height - bitmapHeight) / 2.0f;

		final Bitmap decored = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

		final Canvas canvas = new Canvas(decored);

		canvas.rotate(angle, width / 2.0f, height / 2.0f);
		canvas.drawBitmap(bitmap, x, y, sPaint);
		return decored;
	}

	/**
	 * Este método disminuye el bitmap
	 *
	 * @param file
	 * 	cadena con la ruta de la imagen
	 * @param width
	 * 	valor entero con el ancho esperado del bitmap
	 * @param height
	 * 	valor entero con el alto esperado del bitmap
	 * @return bitmap devuelve el bitmap disminuido
	 */
	public static Bitmap shrinkBitmap(String file, int width, int height) {
		BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
		bmpFactoryOptions.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

		int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) height);
		int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) width);

		if (heightRatio > 1 || widthRatio > 1) {
			if (heightRatio > widthRatio) {
				bmpFactoryOptions.inSampleSize = heightRatio;
			} else {
				bmpFactoryOptions.inSampleSize = widthRatio;
			}
		}

		bmpFactoryOptions.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

		return bitmap;
	}

	/**
	 * Este método convierte un Bitmap a una cadena Base64
	 *
	 * @param bitmap
	 * 	el bitmap a convertir
	 * @return encodedImage cadena base64
	 */
	public static String convertBitmapToStringBase64(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] byteArrayImage = baos.toByteArray();
		String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
		return encodedImage;
	}

	/**
	 * Convierte una cadena base64 a Bitmap
	 *
	 * @param encodedString
	 * 	cadena base 64
	 * @return bitmap devuelve el bitmap generado de la cadena base64
	 * si ocurre un error regresa nulo
	 */
	public static Bitmap convertStringToBitMap(String encodedString) {
		Bitmap bitmap = null;
		try {
			byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
		} catch (Exception e) {
			String error = (e.getMessage() == null) ? BitmapUtils.class.toString() : e.getMessage();
			Log.e(KeyDictionary.TAG, "StringToBitMap() error = " + error);
		}
		return bitmap;
	}

	/**
	 * Compress a bitmap using JPEG format, depending on the desired compression level
	 *
	 * @param bmap
	 * 	Bitmap to be compress
	 */
	public static String convertImage(Bitmap bmap) {

		ByteArrayOutputStream bao = new ByteArrayOutputStream();

		bmap.compress(Bitmap.CompressFormat.JPEG, 70, bao);
		// Decode the image into a Base64 string
		byte[] ba = bao.toByteArray();
		String image = "";
		try {
			image = Base64.encodeToString(ba, Base64.NO_WRAP);
		} catch (Exception e) {
			Log.e(KeyDictionary.TAG, e.getMessage(), e);
		}
		return image;
	}
}


