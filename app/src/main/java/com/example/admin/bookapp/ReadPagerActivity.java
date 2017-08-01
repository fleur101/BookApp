package com.example.admin.bookapp;

import android.app.LoaderManager;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.admin.bookapp.Fragments.ReadPageFragment;
import com.example.admin.bookapp.Fragments.WhatBookDialogFragment;
import com.example.admin.bookapp.data.BookListKazContract;
import com.example.admin.bookapp.data.BookListRusContract;
import com.example.admin.bookapp.data.DatabaseAccess;

import static com.example.admin.bookapp.Fragments.WhatBookDialogFragment.NoticeDialogListener;


public class ReadPagerActivity extends DrawerActivity implements NoticeDialogListener, LoaderManager.LoaderCallbacks<Cursor> {


    private static final int NUM_PAGES = 63;
    private static final String TAG = "READ_PAGER_ACTIVITY";
    private ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_read_pager, mContentFrame);
        setTitle(getString(R.string.bookcoaster));

        mPager = (ViewPager) findViewById(R.id.pager);

        setUpDatabase();

        //
        getLoaderManager().restartLoader(0, null, this);

//        Cursor cursor = DatabaseAccess.getAllBooks();
//
//        while (cursor.moveToNext()){
//            DatabaseAccess.updatePageNotShown();
//        }
//        cursor.close();


    }

    private void setUpDatabase() {
        SharedPreferences settings = getSharedPreferences(MainActivity.MY_LAN_PREFS, MODE_PRIVATE);
        String key = "lan";
        String lan = settings.getString(key, "");
        DatabaseAccess databaseAccess = null;
        if (lan.compareTo(getString(R.string.lan_rus)) == 0) {
            databaseAccess = DatabaseAccess.getInstance(this, BookListRusContract.DATABASE_NAME, BookListRusContract.DATABASE_VERSION);
        } else if (lan.compareTo(getString(R.string.lan_kaz)) == 0) {
            databaseAccess = DatabaseAccess.getInstance(this, BookListKazContract.DATABASE_NAME, BookListKazContract.DATABASE_VERSION);
        } else {
            Log.e(TAG, "onCreate: error database access initializing");
        }
        if (databaseAccess != null) {
            databaseAccess.open();
        }
    }


    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    public void onDialogPositiveClick(int id) {
        DatabaseAccess.updatePageInList(id);

    }

    public void btnClickWhatBook(int id, String book, String author) {
        String args[] = {Integer.toString(id), book, author};
        Bundle bundle = new Bundle();
        bundle.putStringArray("book details", args);
        WhatBookDialogFragment dialogFragment = new WhatBookDialogFragment();
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getSupportFragmentManager(), "what book");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        ProgressBar mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
//        mLoadingIndicator.setVisibility(View.VISIBLE);


        ScreenSlidePagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        //mPager.setOffscreenPageLimit(63);

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        ProgressBar mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
//        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new ReadPageFragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
