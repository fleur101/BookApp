package com.example.admin.bookapp.data;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Gaukhar on 7/27/2017.
 */

public class BookListDbHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "books.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME="Book";
    public static final String COLUMN_BOOK_ID="_id";
    public static final String COLUMN_BOOK_NAME="bookName";
    public static final String COLUMN_BOOK_AUTHOR="bookAuthor";
    public static final String COLUMN_BOOK_PAGE="bookPage";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_ALREADY_SHOWN="bookShown";
    public static final String COLUMN_IN_MY_LIST="bookInList";

    public BookListDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


}
