package com.example.admin.bookapp;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.example.admin.bookapp.data.BookListKazContract;
import com.example.admin.bookapp.data.BookListRusContract;
import com.example.admin.bookapp.data.CustomBookListContract;
import com.example.admin.bookapp.data.DatabaseAccess;

/**
 * Created by Admin on 17.07.2017.
 */

public class MyListActivity extends DrawerActivity {

    private SQLiteDatabase mDb;
    private BooksAdapter mAdapter;

    private RecyclerView mListRecyclerView;
    private EditText customBookName;
    private EditText customAuthorName;
    private String lan;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_my_list_view, mContentFrame);
        setTitle(R.string.my_books);

//        customBookName = (EditText) findViewById(R.id.et_custom_book_name);
//        customAuthorName = (EditText) findViewById(R.id.et_custom_author_name);

        RecyclerView mListRecyclerView = (RecyclerView) findViewById(R.id.rv_books_list);
        mListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        CustomBookListDbHelper dbHelper =  new CustomBookListDbHelper(this);
//        mDb = dbHelper.getReadableDatabase();
        SharedPreferences settings = getSharedPreferences(MainActivity.MY_LAN_PREFS, MODE_PRIVATE);
        String key = "language";
        lan = settings.getString(key, "");
        DatabaseAccess databaseAccess;
        if (lan.compareTo("rus") == 0) {
            databaseAccess = DatabaseAccess.getInstance(this, BookListRusContract.DATABASE_NAME, BookListRusContract.DATABASE_VERSION);
        } else {
            databaseAccess=DatabaseAccess.getInstance(this, BookListKazContract.DATABASE_NAME, BookListKazContract.DATABASE_VERSION);
        }
        databaseAccess.open();
        Cursor cursor = databaseAccess.getBooksInMyList();



        mAdapter = new BooksAdapter(this, cursor);
        mListRecyclerView.setAdapter(mAdapter);
    }

    public void addToBookList(View view){
        if (customBookName.getText().length()==0 || customAuthorName.getText().length()==0)return;
        addNewBook(customBookName.getText().toString(), customAuthorName.getText().toString());
        DatabaseAccess databaseAccess;
        if (lan.compareTo("rus") == 0) {
            databaseAccess = DatabaseAccess.getInstance(this, BookListRusContract.DATABASE_NAME, BookListRusContract.DATABASE_VERSION);
        } else {
            databaseAccess=DatabaseAccess.getInstance(this, BookListKazContract.DATABASE_NAME, BookListKazContract.DATABASE_VERSION);
        }
        databaseAccess.open();
        Cursor newCursor = databaseAccess.getBooksInMyList();
        mAdapter.swapCursor(newCursor);

        customAuthorName.clearFocus();
        customBookName.getText().clear();
        customAuthorName.getText().clear();
    }

//    public Cursor getCustomBooksInMyList(){
//        return mDb.query(
//                CustomBookListContract.CustomBookListItem.TABLE_NAME,
//                null,
//                null,
//                null,
//                null,
//                null,
//                CustomBookListContract.CustomBookListItem.COLUMN_TIMESTAMP
//        );
//    }

//    public Cursor  getBooksInMyList(){
//        String selection = CustomBookListContract.BookListItem.COLUMN_IN_MY_LIST + " = ?";
//        String selectionArgs [] = {"Да"};
//        return mDb.query(
//                CustomBookListContract.BookListItem.TABLE_NAME,
//                null,
//                selection,
//                selectionArgs,
//                null,
//                null,
//                CustomBookListContract.BookListItem.COLUMN_TIMESTAMP
//        );
//    }
//    public Cursor getBooksInMyList(){
//
////        return mDb.rawQuery("SELECT bookName, bookAuthor FROM "+ BookListDbHelper.TABLE_NAME+
////                        " WHERE "+ BookListDbHelper.COLUMN_IN_MY_LIST+ " = 'Да'"+
////                        " UNION SELECT "+ CustomBookListContract.CustomBookListItem.COLUMN_CUSTOM_BOOK_NAME+
////                        ", "+ CustomBookListContract.CustomBookListItem.COLUMN_CUSTOM_BOOK_AUTHOR+" FROM " + CustomBookListContract.CustomBookListItem.TABLE_NAME
////                        , new String[]{"Да"});
//
//        return BookListDbHelper.TABLE_NAME.rawQuery("SELECT bookName, bookAuthor FROM "+ BookListDbHelper.TABLE_NAME+
//                        " WHERE "+ BookListDbHelper.COLUMN_IN_MY_LIST+ " = 'Да'"+
//                        " UNION SELECT "+ CustomBookListContract.CustomBookListItem.COLUMN_CUSTOM_BOOK_NAME+
//                        ", "+ CustomBookListContract.CustomBookListItem.COLUMN_CUSTOM_BOOK_AUTHOR+" FROM " + CustomBookListContract.CustomBookListItem.TABLE_NAME
//                , new String[]{"Да"});
//    }

    private long addNewBook(String bookName, String authorName){
        ContentValues cv = new ContentValues();
        cv.put(CustomBookListContract.CustomBookListItem.COLUMN_CUSTOM_BOOK_NAME, bookName);
        cv.put(CustomBookListContract.CustomBookListItem.COLUMN_CUSTOM_BOOK_AUTHOR, authorName);
        return mDb.insert(CustomBookListContract.CustomBookListItem.TABLE_NAME, null, cv);
    }

    private boolean removeBook(long id){
        return mDb.delete(CustomBookListContract.CustomBookListItem.TABLE_NAME, CustomBookListContract.CustomBookListItem._ID + " = " + id, null)>0;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
