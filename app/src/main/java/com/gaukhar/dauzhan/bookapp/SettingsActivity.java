package com.gaukhar.dauzhan.bookapp;

import android.os.Bundle;
import android.support.annotation.Nullable;


public class SettingsActivity extends DrawerActivity {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_settings, mContentFrame);
        setTitle(getString(R.string.settings));


    }
}
