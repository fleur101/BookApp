package com.example.admin.bookapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.admin.bookapp.data.MyBookListContract.*;

/**
 * Created by Admin on 14.07.2017.
 */

public class MyBookListDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "booklist.db";
    public static final int DATABASE_VERSION = 1;


    public MyBookListDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        final String SQL_CREATE_CUSTOM_BOOKLIST_TABLE = "CREATE TABLE IF NOT EXISTS "+
                MyBookListItem.TABLE_NAME + "(" +
                MyBookListItem._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MyBookListItem.COLUMN_BOOK_NAME + " TEXT NOT NULL, " +
                MyBookListItem.COLUMN_BOOK_AUTHOR + " TEXT NOT NULL, " +
                MyBookListItem.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP"+
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_CUSTOM_BOOKLIST_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ MyBookListItem.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
