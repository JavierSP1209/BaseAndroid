package com.keysd.baseandroid.view.loader;

import android.content.Context;
import java.io.File;

public class FileCache {

  private File cacheDir;

  public FileCache(Context context) {
    //Find the dir to save cached images
    cacheDir = new File(
        context.getCacheDir() + "/ImagesTemp"); // context being the Activity pointer
    if (!cacheDir.exists()) {
      cacheDir.mkdirs();
    }
  }

  /**
   * Return the file that corresponds to the selected key
   *
   * @param key Key for look up the file
   * @return The file for the corresponding key
   */
  public File getFile(String key) {
    //I identify images by hashcode. Not a perfect solution, good for the demo.
    String filename = String.valueOf(key.hashCode());
    //Another possible solution (thanks to grantland)
    //String filename = URLEncoder.encode(url);
    return new File(cacheDir, filename);

  }

  /**
   * Clears the cache directory
   */
  public void clear() {
    File[] files = cacheDir.listFiles();
    if (files == null) {
      return;
    }
    for (File f : files) {
      f.delete();
    }
  }

}