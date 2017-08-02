package com.example.admin.bookapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Gaukhar on 7/27/2017.
 */

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private static SQLiteDatabase database;
    private static DatabaseAccess instance;

    private DatabaseAccess(Context context, String dbName, int dbVersion) {
        this.openHelper = new BookListDbHelper(context, dbName, dbVersion);
    }

    public static DatabaseAccess getInstance(Context context, String dbName, int dbVersion) {
        if (instance == null) {
            instance = new DatabaseAccess(context, dbName, dbVersion);
        }
        return instance;
    }

    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    public static Cursor getBookPage(){

        return database.query(
                BookListContract.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                " RANDOM() LIMIT 1"
        );
    }

    public Cursor getBooksInMyList(){
        String selection = BookListContract.COLUMN_IN_MY_LIST + " = ?";
        String selectionArgs [] = {"Да"};
        return database.query(
                BookListContract.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
    }
    public static Cursor getAllBooks(){
        return database.query(
                BookListContract.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
    public static void updatePageShown(int bookId){
        ContentValues cv = new ContentValues();
        cv.put(BookListContract.COLUMN_ALREADY_SHOWN, "Да");
        database.update(BookListContract.TABLE_NAME, cv, BookListContract.COLUMN_BOOK_ID+"=" + bookId, null);
    }

    public static void updatePageNotShown() {
        ContentValues cv = new ContentValues();
        cv.put(BookListContract.COLUMN_ALREADY_SHOWN, "Нет");
        database.update(BookListContract.TABLE_NAME, cv, null, null);
    }

    public static void updatePageInList(int bookId){
        ContentValues cv = new ContentValues();
        cv.put(BookListContract.COLUMN_IN_MY_LIST, "Да");
        database.update(BookListContract.TABLE_NAME, cv, BookListContract.COLUMN_BOOK_ID+"=" + bookId, null);
    }


}
