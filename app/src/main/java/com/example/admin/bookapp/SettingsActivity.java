package com.example.admin.bookapp;

import android.os.Bundle;
import android.support.annotation.Nullable;


public class SettingsActivity extends DrawerActivity {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_settings, mContentFrame);
        setTitle(getString(R.string.settings));
//
//        SharedPreferences shp = getSharedPreferences(getResources().getResourceName(R.xml.preferences), 0);
//        if (shp.getString("lan", null) == null) {
//            Log.e("MIRka_TAG", "null");
//        } else {
//            Log.e("MIRKA_TAG", shp.getString("lan", null));
//        }

    }
}
