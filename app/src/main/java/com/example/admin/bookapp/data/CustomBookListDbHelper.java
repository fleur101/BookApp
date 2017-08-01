package com.example.admin.bookapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.admin.bookapp.data.CustomBookListContract.*;

/**
 * Created by Admin on 14.07.2017.
 */

public class CustomBookListDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "booklist.db";
    public static final int DATABASE_VERSION = 6;


    public CustomBookListDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        final String SQL_CREATE_CUSTOM_BOOKLIST_TABLE = "CREATE TABLE IF NOT EXISTS "+
                CustomBookListItem.TABLE_NAME + "(" +
                CustomBookListItem.COLUMN_CUSTOM_BOOK_NAME + " TEXT NOT NULL, " +
                CustomBookListItem.COLUMN_CUSTOM_BOOK_AUTHOR + " TEXT NOT NULL, " +
                CustomBookListItem.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP"+
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_CUSTOM_BOOKLIST_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ CustomBookListItem.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
