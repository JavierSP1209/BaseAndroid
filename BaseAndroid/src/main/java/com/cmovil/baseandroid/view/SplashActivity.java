package com.cmovil.baseandroid.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.cmovil.baseandroid.R;
import com.cmovil.baseandroid.controller.SampleController;
import com.cmovil.baseandroid.dao.db.DBException;
import com.cmovil.baseandroid.model.db.State;
import com.cmovil.baseandroid.util.KeyDictionary;

import java.util.List;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash, menu);
        return true;
    }
    
}
