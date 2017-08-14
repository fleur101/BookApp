package com.gaukhar.dauzhan.bookapp;

import android.app.LoaderManager;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import com.gaukhar.dauzhan.bookapp.Fragments.ReadPageFragment;
import com.gaukhar.dauzhan.bookapp.Fragments.WhatBookDialogFragment;
import com.gaukhar.dauzhan.bookapp.data.BookListContentProvider;
import com.gaukhar.dauzhan.bookapp.data.BookListContract;
import com.gaukhar.dauzhan.bookapp.data.MyBookListContract;
import com.gaukhar.dauzhan.bookapp.data.MyBookListDbHelper;

import static com.gaukhar.dauzhan.bookapp.Fragments.WhatBookDialogFragment.NoticeDialogListener;


public class ReadPagerActivity extends DrawerActivity implements NoticeDialogListener, LoaderManager.LoaderCallbacks<Cursor>{


    private static final String TAG = "READ_PAGER_ACTIVITY_TAG";
    private CursorPagerAdapter adapter;
    private ProgressBar mLoadingIndicator;
    private int tvFontSize;
    private String stringFontSize;
    private ViewPager mPager;
    private SQLiteDatabase mDb;
    private int positionVis;
//    private ImageButton leftNav;
//    private ImageButton rightNav;

    SharedPreferences.OnSharedPreferenceChangeListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_read_pager, mContentFrame);
        setTitle(getString(R.string.bookcoaster));

        mPager = (ViewPager) findViewById(R.id.pager);
        adapter = new CursorPagerAdapter(getSupportFragmentManager(), null);
        mPager.setAdapter(adapter);
//        PageIndicatorView pageIndicatorView = (PageIndicatorView) findViewById(R.id.pageIndicatorView);
//        pageIndicatorView.setViewPager(mPager);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        MyBookListDbHelper mDbHelper = new MyBookListDbHelper(this);
        mDb = mDbHelper.getWritableDatabase();

//        leftNav = (ImageButton) findViewById(R.id.left_nav);
//        rightNav = (ImageButton) findViewById(R.id.right_nav);
//        leftNav.setVisibility(View.INVISIBLE);
//        final ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                if (position == adapter.mCursor.getCount()-1){
//                    rightNav.setVisibility(View.INVISIBLE);
//                    leftNav.setVisibility(View.VISIBLE);
//                    Log.e(TAG, "onPageSelected: last");
//                } else if (position==0 ){
//                    leftNav.setVisibility(View.INVISIBLE);
//                    rightNav.setVisibility(View.VISIBLE);
//                    Log.e(TAG, "onPageSelected: first");
//                } else {
//                    leftNav.setVisibility(View.VISIBLE);
//                    rightNav.setVisibility(View.VISIBLE);
//                    Log.e(TAG, "onPageSelected: not first not last");
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        };
//        mPager.addOnPageChangeListener(onPageChangeListener);
//        leftNav.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int tab = mPager.getCurrentItem();
//                if (tab > 0) {
//                    tab--;
//                    mPager.setCurrentItem(tab);
//                } else if (tab == 0) {
//                    mPager.setCurrentItem(tab);
//                }
//
//            }
//        });
//
//        rightNav.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int tab = mPager.getCurrentItem();
//                tab++;
//                mPager.setCurrentItem(tab);
//            }
//
//        });
//
        setupSharedPreferences();
        Log.e(TAG, "onCreate: created");
        setListener();
        getLoaderManager().initLoader(0, null, this);

    }


    public void rightOnClick(){
        int tab = mPager.getCurrentItem();
        tab++;
        mPager.setCurrentItem(tab);
    }

    public void leftOnClick(){
        int tab = mPager.getCurrentItem();
        if (tab > 0) {
            tab--;
            mPager.setCurrentItem(tab);
        } else if (tab == 0) {
            mPager.setCurrentItem(tab);
        }
    }
    private void setupSharedPreferences() {
        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            stringFontSize = sharedPreferences.getString("font_size", "18");
            tvFontSize=Integer.valueOf(stringFontSize);
            Log.e(TAG, "onCreate: tv font size" + tvFontSize);
        } catch (Exception ex) {
            Log.e(TAG, "onCreate: tv font size exception");
            ex.printStackTrace();
        }
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
                            stringFontSize = sharedPreferences.getString(key, "18");
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
        String bookName = null; String bookAuthor = null;
        Uri contentUri = ContentUris.withAppendedId(BookListContract.CONTENT_URI, id);
//        ContentValues cv = new ContentValues();
//        cv.put(BookListContract.COLUMN_IN_MY_LIST, "1");
//        getContentResolver().update(contentUri, cv, null, null);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor.moveToNext()) {
            bookAuthor = cursor.getString(cursor.getColumnIndex(BookListContract.COLUMN_BOOK_AUTHOR));
            bookName = cursor.getString(cursor.getColumnIndex(BookListContract.COLUMN_BOOK_NAME));
        }
        cursor.close();


        ContentValues cv = new ContentValues();
        cv.put(MyBookListContract.MyBookListItem.COLUMN_BOOK_NAME, bookName);
        cv.put(MyBookListContract.MyBookListItem.COLUMN_BOOK_AUTHOR, bookAuthor);
        mDb.insert(MyBookListContract.MyBookListItem.TABLE_NAME, null, cv);

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
                Log.e(TAG, "getItem: position"+position);
                if (position == 0){
                    positionVis=0;
                    Log.e(TAG, "getItem: positionVis 0");
                } else if (position == mCursor.getCount()-1){
                    positionVis=1;
                    Log.e(TAG, "getItem: positionVis 1");
                } else {
                    positionVis=2;
                    Log.e(TAG, "getItem: positionVis 2");
                }
                args.putInt("positionVis", positionVis);
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
