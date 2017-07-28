package com.example.admin.bookapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ProgressBar;

import com.example.admin.bookapp.Fragments.ReadPageFragment;
import com.example.admin.bookapp.Fragments.WhatBookDialogFragment;
import com.example.admin.bookapp.data.DatabaseAccess;

import static com.example.admin.bookapp.Fragments.WhatBookDialogFragment.NoticeDialogListener;


public class ReadPagerActivity extends BaseActivity implements  NoticeDialogListener{



    public static int bookId;

    public static int getBookId() {
        return bookId;
    }

    public static void setBookId(int bookId) {
        ReadPagerActivity.bookId = bookId;
    }

    public static String getBookName() {
        return bookName;
    }

    public static void setBookName(String bookName) {
        ReadPagerActivity.bookName = bookName;
    }

    public static String getBookAuthor() {
        return bookAuthor;
    }

    public static void setBookAuthor(String bookAuthor) {
        ReadPagerActivity.bookAuthor = bookAuthor;
    }


    public static void setBookPage(String bookPage) {
        ReadPagerActivity.bookPage = bookPage;
    }

    public static String getBookPage() {
        return bookPage;
    }

    public static String bookName;
    public static String bookAuthor;
    public static String bookPage;



    private static final int NUM_PAGES = 63;
    private ViewPager mPager;
    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_read_pager, mContentFrame);

        setTitle("Книговорот");
        mPager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        ProgressBar mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
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

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter{

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
        mDb.close();
        super.onDestroy();
    }











}
