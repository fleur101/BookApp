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

    private DatabaseAccess(Context context) {
        this.openHelper = new BookListDbHelper(context);
    }

    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
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
       // String selection = BookListDbHelper.COLUMN_IN_MY_LIST + " = ?";
//        String selectionArgs [] = {"Да"};
        return database.query(
                BookListDbHelper.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                " RANDOM() LIMIT 1"
        );
  //      return database.rawQuery("SELECT * FROM Book ORDER BY RANDOM() LIMIT 1", null);
    }

    public Cursor getBooksInMyList(){
        String selection = BookListDbHelper.COLUMN_IN_MY_LIST + " = ?";
        String selectionArgs [] = {"Да"};
        return database.query(
                BookListDbHelper.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
      //  return database.rawQuery("SELECT * FROM Book WHERE bookInList = ?", new String[]{"Да"});
    }
    public static Cursor getAllBooks(){
        return database.query(
                BookListDbHelper.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
       // return database.rawQuery("SELECT * FROM Book", null);
    }
    public static void updatePageShown(int bookId){
        ContentValues cv = new ContentValues();
        cv.put(BookListDbHelper.COLUMN_ALREADY_SHOWN, "Да");
        database.update(BookListDbHelper.TABLE_NAME, cv, BookListDbHelper.COLUMN_BOOK_ID+"=" + bookId, null);
        // database.rawQuery("UPDATE Book SET bookShown=? WHERE _id = "+bookId, new String[]{"Да"});
    }

    public static void updatePageNotShown() {

        ContentValues cv = new ContentValues();
        cv.put(BookListDbHelper.COLUMN_ALREADY_SHOWN, "Нет");
        database.update(BookListDbHelper.TABLE_NAME, cv, null, null);
       // database.rawQuery("UPDATE Book SET bookShown=?", new String[]{"Нет"});
    }

    public static void updatePageInList(int bookId){
        //database.rawQuery("UPDATE Book SET bookInList=? WHERE _id ="+bookId, new String[]{"Да"});

        ContentValues cv = new ContentValues();
        cv.put(BookListDbHelper.COLUMN_IN_MY_LIST, "Да");
        database.update(BookListDbHelper.TABLE_NAME, cv, BookListDbHelper.COLUMN_BOOK_ID+"=" + bookId, null);
    }


}
