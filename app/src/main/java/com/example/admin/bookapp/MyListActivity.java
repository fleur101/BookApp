package com.example.admin.bookapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.example.admin.bookapp.data.BookListContract;
import com.example.admin.bookapp.data.DatabaseAccess;

/**
 * Created by Admin on 17.07.2017.
 */

public class MyListActivity extends BaseActivity {

    private SQLiteDatabase mDb;
    private BooksAdapter mAdapter;

    private RecyclerView mListRecyclerView;
    private EditText customBookName;
    private EditText customAuthorName;

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
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        Cursor cursor = databaseAccess.getBooksInMyList();


        mAdapter = new BooksAdapter(this, cursor);
        mListRecyclerView.setAdapter(mAdapter);
    }

    public void addToBookList(View view){
        if (customBookName.getText().length()==0 || customAuthorName.getText().length()==0)return;
        addNewBook(customBookName.getText().toString(), customAuthorName.getText().toString());
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        Cursor newCursor = databaseAccess.getBooksInMyList();
        mAdapter.swapCursor(newCursor);

        customAuthorName.clearFocus();
        customBookName.getText().clear();
        customAuthorName.getText().clear();
    }

//    public Cursor getCustomBooksInMyList(){
//        return mDb.query(
//                BookListContract.CustomBookListItem.TABLE_NAME,
//                null,
//                null,
//                null,
//                null,
//                null,
//                BookListContract.CustomBookListItem.COLUMN_TIMESTAMP
//        );
//    }

//    public Cursor  getBooksInMyList(){
//        String selection = BookListContract.BookListItem.COLUMN_IN_MY_LIST + " = ?";
//        String selectionArgs [] = {"Да"};
//        return mDb.query(
//                BookListContract.BookListItem.TABLE_NAME,
//                null,
//                selection,
//                selectionArgs,
//                null,
//                null,
//                BookListContract.BookListItem.COLUMN_TIMESTAMP
//        );
//    }
//    public Cursor getBooksInMyList(){
//
////        return mDb.rawQuery("SELECT bookName, bookAuthor FROM "+ BookListDbHelper.TABLE_NAME+
////                        " WHERE "+ BookListDbHelper.COLUMN_IN_MY_LIST+ " = 'Да'"+
////                        " UNION SELECT "+ BookListContract.CustomBookListItem.COLUMN_CUSTOM_BOOK_NAME+
////                        ", "+ BookListContract.CustomBookListItem.COLUMN_CUSTOM_BOOK_AUTHOR+" FROM " + BookListContract.CustomBookListItem.TABLE_NAME
////                        , new String[]{"Да"});
//
//        return BookListDbHelper.TABLE_NAME.rawQuery("SELECT bookName, bookAuthor FROM "+ BookListDbHelper.TABLE_NAME+
//                        " WHERE "+ BookListDbHelper.COLUMN_IN_MY_LIST+ " = 'Да'"+
//                        " UNION SELECT "+ BookListContract.CustomBookListItem.COLUMN_CUSTOM_BOOK_NAME+
//                        ", "+ BookListContract.CustomBookListItem.COLUMN_CUSTOM_BOOK_AUTHOR+" FROM " + BookListContract.CustomBookListItem.TABLE_NAME
//                , new String[]{"Да"});
//    }

    private long addNewBook(String bookName, String authorName){
        ContentValues cv = new ContentValues();
        cv.put(BookListContract.CustomBookListItem.COLUMN_CUSTOM_BOOK_NAME, bookName);
        cv.put(BookListContract.CustomBookListItem.COLUMN_CUSTOM_BOOK_AUTHOR, authorName);
        return mDb.insert(BookListContract.CustomBookListItem.TABLE_NAME, null, cv);
    }

    private boolean removeBook(long id){
        return mDb.delete(BookListContract.CustomBookListItem.TABLE_NAME, BookListContract.CustomBookListItem._ID + " = " + id, null)>0;
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
