/**
 * File: Typefaces
 * CreationDate: 10/06/14
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description: 
 *
 */
package com.cmovil.baseandroid.util;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

/**
 * Workaround for a memory leak caused by
 * {@link android.graphics.Typeface#createFromAsset(android.content.res.AssetManager, String)} in which each time the
 * font is loaded it is allocated to memory on a new instance instead of reuse the
 * previews one
 *
 * @author "M. en C. Javier Silva Perez (JSP)"
 * @version 1.0
 * @since 10/06/14
 */
public class Typefaces {
	private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

	/**
	 * Gets the requested font type face by name, the font should be on assets directory, the type face is stored on a
	 * hash table and reused each time its requested
	 *
	 * @param c
	 * 	Context in which the asset will be searched
	 * @param name
	 * 	Name of the font file on assets directory
	 * @return The loaded typeface
	 */
	public static Typeface get(Context c, String name) {
		synchronized (cache) {
			if (!cache.containsKey(name)) {
				Typeface t = Typeface.createFromAsset(c.getAssets(), name);
				cache.put(name, t);
			}
			return cache.get(name);
		}
	}
}
