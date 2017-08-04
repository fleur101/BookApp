package com.example.admin.bookapp.data;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;


public class BookListDbHelper extends SQLiteAssetHelper {

    public static final String DATABASE_NAME_KZ = "books_kz.db";
    public static final int DATABASE_VERSION_KZ = 1;
    public static final String DATABASE_NAME_RUS = "books.db";
    public static final int DATABASE_VERSION_RUS = 1;

    public BookListDbHelper(Context context, String DATABASE_NAME, int DATABASE_VERSION) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



}
