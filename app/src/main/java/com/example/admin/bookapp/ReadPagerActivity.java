package com.example.admin.bookapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ProgressBar;

import com.example.admin.bookapp.Fragments.ReadPageFragment;
import com.example.admin.bookapp.Fragments.WhatBookDialogFragment;
import com.example.admin.bookapp.data.BookListDbHelper;
import com.example.admin.bookapp.data.DatabaseAccess;

import static com.example.admin.bookapp.Fragments.WhatBookDialogFragment.NoticeDialogListener;


public class ReadPagerActivity extends DrawerActivity implements  NoticeDialogListener, LoaderManager.LoaderCallbacks<Cursor> {


    private static final int NUM_PAGES = 63;
    private ViewPager mPager;
    private ProgressBar mLoadingIndicator;
    private static int bookId;
    private static String bookName;
    private static String bookAuthor;
    private static String bookPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_read_pager, mContentFrame);

        setTitle("Книговорот");
        mPager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mLoadingIndicator.setVisibility(View.INVISIBLE);

        //CustomBookListDbHelper dbHelper = new CustomBookListDbHelper(this);
        //mDb = dbHelper.getWritableDatabase();
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        Cursor cursor = DatabaseAccess.getAllBooks();

        while (cursor.moveToNext()){
            DatabaseAccess.updatePageNotShown();
        }
        cursor.close();


    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

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
    public void onDialogPositiveClick(int id) {
        DatabaseAccess.updatePageInList(id);

    }

    public void btnClickWhatBook(int id, String book, String author) {
        String args[]={Integer.toString(id), book, author};
        Bundle bundle = new Bundle();
        bundle.putStringArray("book details", args);
        WhatBookDialogFragment dialogFragment = new WhatBookDialogFragment();
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getSupportFragmentManager(), "what book");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (args == null)return;
                mLoadingIndicator.setVisibility(View.VISIBLE);
            }

            @Override
            public Cursor loadInBackground() {
                loadData();
                return null;
            }
        };


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);


    }

    @Override
    public void onLoaderReset(Loader loader) {
    }

    public void loadData(){
        Cursor cursor = DatabaseAccess.getBookPage();
        cursor.moveToFirst();

         bookId = cursor.getInt(cursor.getColumnIndex(BookListDbHelper.COLUMN_BOOK_ID));
         bookAuthor = cursor.getString(cursor.getColumnIndex(BookListDbHelper.COLUMN_BOOK_NAME));
         bookName = cursor.getString(cursor.getColumnIndex(BookListDbHelper.COLUMN_BOOK_AUTHOR));
         bookPage = cursor.getString(cursor.getColumnIndex(BookListDbHelper.COLUMN_BOOK_PAGE));
        DatabaseAccess.updatePageShown(bookId);
    }
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter{

        ScreenSlidePagerAdapter(FragmentManager fm) {

            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return ReadPageFragment.newInstance(bookId, bookName, bookAuthor, bookPage);
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
