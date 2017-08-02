package com.example.admin.bookapp;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.admin.bookapp.Fragments.ReadPageFragment;
import com.example.admin.bookapp.Fragments.WhatBookDialogFragment;
import com.example.admin.bookapp.data.BookListContract;
import com.example.admin.bookapp.data.DatabaseAccess;

import static com.example.admin.bookapp.Fragments.WhatBookDialogFragment.NoticeDialogListener;


public class ReadPagerActivity extends DrawerActivity implements NoticeDialogListener, LoaderManager.LoaderCallbacks<Cursor> {


    private static final int NUM_PAGES = 63;
    private static final String TAG = "READ_PAGER_ACTIVITY";
    private ViewPager mPager;
    private CursorPagerAdapter adapter;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_read_pager, mContentFrame);
        setTitle(getString(R.string.bookcoaster));

        mPager = (ViewPager) findViewById(R.id.pager);
        adapter = new CursorPagerAdapter(getSupportFragmentManager(), null);
        mPager.setAdapter(adapter);
       // setUpDatabase();

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        //
        getLoaderManager().initLoader(0, null, this);

//        Cursor cursor = DatabaseAccess.getAllBooks();
//
//        while (cursor.moveToNext()){
//            DatabaseAccess.updatePageNotShown();
//        }
//        cursor.close();
    }

//    private void setUpDatabase() {
  //      SharedPreferences settings = getSharedPreferences(MainActivity.MY_LAN_PREFS, MODE_PRIVATE);
//        String key = "lan";
//        String lan = settings.getString(key, "");
//        DatabaseAccess databaseAccess = null;
//        if (lan.compareTo(getString(R.string.lan_rus)) == 0) {
//         //   databaseAccess = DatabaseAccess.getInstance(this, BookListDbHelper.DATABASE_NAME_RUS, BookListDbHelper.DATABASE_VERSION_RUS);
//            BookListContentProvider contentProvider = new BookListContentProvider();
//
//        } else if (lan.compareTo(getString(R.string.lan_kaz)) == 0) {
//          //  databaseAccess = DatabaseAccess.getInstance(this, BookListDbHelper.DATABASE_NAME_KZ, BookListDbHelper.DATABASE_VERSION_KZ);
//            BookListContentProvider contentProvider = new BookListContentProvider();
//
//        } else {
//            Log.e(TAG, "onCreate: error database access initializing");
//        }
//        if (databaseAccess != null) {
//            databaseAccess.open();
//        }
//    }


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
        getLoaderManager().restartLoader(0, null, this);
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

        mLoadingIndicator.setVisibility(View.VISIBLE);


//        CursorPagerAdapter mPagerAdapter = new CursorPagerAdapter(getSupportFragmentManager());
//        mPager.setAdapter(mPagerAdapter);
        //mPager.setOffscreenPageLimit(63);

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
    }


}
