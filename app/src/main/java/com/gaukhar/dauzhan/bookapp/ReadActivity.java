package com.gaukhar.dauzhan.bookapp;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.gaukhar.dauzhan.bookapp.Fragments.ReadFragment;
import com.gaukhar.dauzhan.bookapp.Fragments.WhatBookDialogFragment;
import com.gaukhar.dauzhan.bookapp.data.MyBookListContract;
import com.gaukhar.dauzhan.bookapp.data.MyBookListDbHelper;

import java.util.ArrayList;
import java.util.Random;


public class ReadActivity extends DrawerActivity  implements WhatBookDialogFragment.NoticeDialogListener{

    private static final String TAG = "READ_ACTIVITY_TAG";
    private int tvFontSize;
    private String stringFontSize;
    private String font;
    private int positionVis;
    private SQLiteDatabase mDb;
    private ArrayList<Integer> usedQuestionIdList;
    private int randomId;


    //    private ImageButton leftNav;
//    private ImageButton rightNav;
    SharedPreferences.OnSharedPreferenceChangeListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_read, mContentFrame);
        setTitle(getString(R.string.bookcoaster));
    //    FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        usedQuestionIdList = new ArrayList<>();
        usedQuestionIdList.add(7);
        //my list db
        MyBookListDbHelper mDbHelper = new MyBookListDbHelper(this);
        mDb = mDbHelper.getWritableDatabase();


        setupSharedPreferences();
        Log.e(TAG, "onCreate: created");
        setListener();
        if(savedInstanceState == null) {
            //put args
            Random rand = new Random();
          //  int randomId = rand.nextInt(96);
            randomId = QuizActivity.generateRandom(0, 95, usedQuestionIdList);
            usedQuestionIdList.add(randomId);
            Bundle args = new Bundle();
            args.putInt(ReadFragment.ARG_ITEM_ID, randomId);
            args.putInt("fontSize", tvFontSize);
            args.putString("font", font);
            //create fragment
            ReadFragment fragment = new ReadFragment();
            fragment.setArguments(args);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_holder, fragment)
                    .commit();
        }

    }

    public void replaceFragment(){
        //add args
        Random rand = new Random();
       // int randomId= rand.nextInt(96);
        randomId = QuizActivity.generateRandom(0, 95, usedQuestionIdList);
        usedQuestionIdList.add(randomId);
        Bundle args = new Bundle();
        args.putInt(ReadFragment.ARG_ITEM_ID, randomId);
        args.putInt("fontSize", tvFontSize);
        args.putString("font", font);

        //replace fragment
        ReadFragment fragment = new ReadFragment();
        fragment.setArguments(args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_holder, fragment)
                .commit();

    }

    public void setupSharedPreferences() {
        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            stringFontSize = sharedPreferences.getString("font_size", "18");
            tvFontSize=Integer.valueOf(stringFontSize);
            font = sharedPreferences.getString("font", "font_helvetica");

            Log.e(TAG, "onCreate: tv font size" + tvFontSize);
            Log.e(TAG, "onCreate: tv font" + font);
        } catch (Exception ex) {
            Log.e(TAG, "onCreate: tv font exception");
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
                    case "font":
                        Log.e(TAG, "onSharedPreferenceChanged: key is font");
                        try {
                            font = sharedPreferences.getString(key, "font_helvetica");
                            Log.e(TAG, "onSharedPreferenceChanged: fontPref" + font);
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
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Log.e(TAG, "onResume: "+sharedPreferences.getString("lan", ""));
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(mListener);
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
    public void onDialogPositiveClick(int id, String bookName, String bookAuthor) {
        ContentValues cv = new ContentValues();
        cv.put(MyBookListContract.MyBookListItem.COLUMN_BOOK_NAME, bookName);
        cv.put(MyBookListContract.MyBookListItem.COLUMN_BOOK_AUTHOR, bookAuthor);
        mDb.insert(MyBookListContract.MyBookListItem.TABLE_NAME, null, cv);

    }

    public void btnClickWhatBook(int id, String book, String author, String fontType) {
        String args[] = {Integer.toString(id), book, author, fontType};
        Bundle bundle = new Bundle();
        bundle.putStringArray("book details", args);
        WhatBookDialogFragment dialogFragment = new WhatBookDialogFragment();
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getSupportFragmentManager(), "what book");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}



