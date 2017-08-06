package com.example.admin.bookapp;

import android.app.LoaderManager;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.admin.bookapp.Fragments.ReadPageFragment;
import com.example.admin.bookapp.Fragments.WhatBookDialogFragment;
import com.example.admin.bookapp.data.BookListContentProvider;
import com.example.admin.bookapp.data.BookListContract;

import static com.example.admin.bookapp.Fragments.WhatBookDialogFragment.NoticeDialogListener;


public class ReadPagerActivity extends DrawerActivity implements NoticeDialogListener, LoaderManager.LoaderCallbacks<Cursor>{


    private static final String TAG = "READ_PAGER_ACTIVITY_TAG";
    private CursorPagerAdapter adapter;
    private ProgressBar mLoadingIndicator;
    private int tvFontSize;
    private String stringFontSize;
    private ViewPager mPager;

    SharedPreferences.OnSharedPreferenceChangeListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_read_pager, mContentFrame);
        setTitle(getString(R.string.bookcoaster));

        mPager = (ViewPager) findViewById(R.id.pager);
        adapter = new CursorPagerAdapter(getSupportFragmentManager(), null);
        mPager.setAdapter(adapter);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);


        Log.e(TAG, "onCreate: created");
        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            stringFontSize = sharedPreferences.getString("font_size", "20");
            tvFontSize=Integer.valueOf(stringFontSize);
            Log.e(TAG, "onCreate: tv font size" + tvFontSize);
        } catch (Exception ex) {
            Log.e(TAG, "onCreate: tv font size exception");
            ex.printStackTrace();
        }
        setListener();
        getLoaderManager().initLoader(0, null, this);

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    public void setListener(){
        mListener = new SharedPreferences.OnSharedPreferenceChangeListener() {

            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                switch (key) {
                    case "lan":
                        Log.e(TAG, "onSharedPreferenceChanged: key is lan");

                        ContentResolver resolver = getApplicationContext().getContentResolver();
                        ContentProviderClient client = resolver.acquireContentProviderClient(BookListContract.CONTENT_URI);
                        BookListContentProvider contentProvider = null;
                        if (client != null)
                            contentProvider = (BookListContentProvider) client.getLocalContentProvider();
                        if (contentProvider != null) contentProvider.resetDatabase();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            if (client != null) client.close();
                        }

                        break;
                    case "font_size":
                        Log.e(TAG, "onSharedPreferenceChanged: key is font_size");
                        try {
                            stringFontSize = sharedPreferences.getString(key, "20");
                            tvFontSize=Integer.valueOf(stringFontSize);
                            Log.e(TAG, "onSharedPreferenceChanged: fontSizePref" + tvFontSize);
                            break;
                        } catch (Exception ex) {
                            Log.e(TAG, "onSharedPreferenceChanged: font_size_pref exception");
                            ex.printStackTrace();
                        }
                        break;
                    default:
                        Log.e(TAG, "key is not recognized");
                        break;
                }
                }

        };
    }



    @Override
    public void onBackPressed() {
      //  if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
//        } else {
//            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
       // Log.e(TAG, "onResume: resumed");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Log.e(TAG, "onResume: "+sharedPreferences.getString("lan", ""));
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(mListener);
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
      //  setListener();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Log.e(TAG, "onPause: "+sharedPreferences.getString("lan", ""));
        sharedPreferences.registerOnSharedPreferenceChangeListener(mListener);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    public void onDialogPositiveClick(int id) {
        Uri contentUri = ContentUris.withAppendedId(BookListContract.CONTENT_URI, id);
        ContentValues cv = new ContentValues();
        cv.put(BookListContract.COLUMN_IN_MY_LIST, "Да");
        getContentResolver().update(contentUri, cv, null, null);

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

        mLoadingIndicator.setVisibility(View.VISIBLE);


        return new android.content.CursorLoader( this,
                BookListContract.CONTENT_URI,
                new String[]{BookListContract.COLUMN_BOOK_ID},
                null,
                null,
                "RANDOM()");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }



    private class CursorPagerAdapter extends FragmentStatePagerAdapter {
        private Cursor mCursor;

        CursorPagerAdapter(FragmentManager fm, Cursor c) {
            super(fm);
            mCursor=c;
        }

        @Override
        public Fragment getItem(int position) {
            if (mCursor!=null && mCursor.moveToPosition(position)){
                Bundle args = new Bundle();
                args.putInt(ReadPageFragment.ARG_ITEM_ID, mCursor.getInt(mCursor.getColumnIndex(BookListContract.COLUMN_BOOK_ID)));
                args.putInt("fontSize", tvFontSize);
                Log.e(TAG, "getItem: font size"+tvFontSize);
                ReadPageFragment fragment = new ReadPageFragment();
                fragment.setArguments(args);
             //   Log.e(TAG, "getItem: return new nonempty page fragment");
                return fragment;
            } else {
                Log.e(TAG, "getItem: return new empty page fragment");
                return new ReadPageFragment();
            }
        }

        @Override
        public int getCount() {
            if (mCursor!=null)return mCursor.getCount();
            return 0;
        }


        private void swapCursor(Cursor cursor){
            mCursor=cursor;
            notifyDataSetChanged();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        sharedPreferences.unregisterOnSharedPreferenceChangeListener(mListener);


    }


}
