package com.cmovil.baseandroid.view.loader;

import android.util.Log;

import com.cmovil.baseandroid.util.KeyDictionary;

import java.io.InputStream;
import java.io.OutputStream;

public class Utils {
    public static void CopyStream(InputStream is, OutputStream os)
    {
	    try
        {
	        byte[] buffer = new byte[1024];
	        int bytesRead;
	        while ((bytesRead = is.read(buffer)) != -1)
	        {
		        os.write(buffer, 0, bytesRead);
	        }
        }
        catch(Exception ex){
	        Log.e(KeyDictionary.TAG, ex.getMessage(), ex);
        }
    }
}