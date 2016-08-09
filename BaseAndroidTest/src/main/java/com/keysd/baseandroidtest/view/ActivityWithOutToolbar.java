/**
 * File: ActivityWithOutToolbar
 * CreationDate: 05/11/14
 * Author: "Ing. Jesús Fernando Sierra Pastrana"
 * Description:
 */

package com.keysd.baseandroidtest.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import com.keysd.baseandroid.view.BaseActionBarActivity;
import com.keysd.baseandroidtest.R;

/**
 * @author "Ing. Jesús Fernando Sierra Pastrana"
 * @version 1.0
 * @since 05/11/14
 */
public class ActivityWithOutToolbar extends BaseActionBarActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.no_toolbar_activity);
  }

  @NonNull
  @Override
  public Toolbar getToolbar() {
    return new Toolbar(this);
  }
}
