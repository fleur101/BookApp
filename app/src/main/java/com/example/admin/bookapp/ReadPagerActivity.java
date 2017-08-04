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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.admin.bookapp.Fragments.ReadPageFragment;
import com.example.admin.bookapp.Fragments.WhatBookDialogFragment;
import com.example.admin.bookapp.data.BookListContentProvider;
import com.example.admin.bookapp.data.BookListContract;

import static com.example.admin.bookapp.Fragments.WhatBookDialogFragment.NoticeDialogListener;


public class ReadPagerActivity extends DrawerActivity implements NoticeDialogListener, LoaderManager.LoaderCallbacks<Cursor>{


    private static final String TAG = "READ_PAGER_ACTIVITY_TAG";
    private ViewPager mPager;
    private CursorPagerAdapter adapter;
    private ProgressBar mLoadingIndicator;
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
        getLoaderManager().initLoader(0, null, this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Log.e(TAG, "onStart: "+sharedPreferences.getString("lan", ""));

        mListener = new SharedPreferences.OnSharedPreferenceChangeListener() {

            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                Toast.makeText(ReadPagerActivity.this, "Preference Changed", Toast.LENGTH_SHORT).show();
                if (key.equals("lan")) {
                    Log.e(TAG, "onSharedPreferenceChanged: key is lan");
                    ContentResolver resolver = getApplicationContext().getContentResolver();
                    ContentProviderClient client = resolver.acquireContentProviderClient(BookListContract.CONTENT_URI);
                    BookListContentProvider contentProvider = (BookListContentProvider) client.getLocalContentProvider();
                    contentProvider.resetDatabase();
                } else {
                    Log.e(TAG, "key is not lan");
                }
            }
        };
        sharedPreferences.registerOnSharedPreferenceChangeListener(mListener);

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
        Log.e(TAG, "onResume: resumed");
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mListener = new SharedPreferences.OnSharedPreferenceChangeListener() {

            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                Toast.makeText(ReadPagerActivity.this, "Preference Changed", Toast.LENGTH_SHORT).show();
                if (key.equals("lan")) {
                    Log.e(TAG, "onSharedPreferenceChanged: key is lan");
                    ContentResolver resolver = getApplicationContext().getContentResolver();
                    ContentProviderClient client = resolver.acquireContentProviderClient(BookListContract.CONTENT_URI);
                    BookListContentProvider contentProvider = (BookListContentProvider) client.getLocalContentProvider();
                    contentProvider.resetDatabase();
                } else {
                    Log.e(TAG, "key is not lan");
                }
            }
        };
        sharedPreferences.registerOnSharedPreferenceChangeListener(mListener);
        Log.e(TAG, "onPause: paused");

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
        int added = getContentResolver().update(contentUri, cv, null, null);

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
                ReadPageFragment fragment = new ReadPageFragment();
                fragment.setArguments(args);
                Log.e(TAG, "getItem: return new nonempty page fragment");
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
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(mListener);


    }


}
