package com.example.admin.bookapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin.bookapp.data.BookListContract;
import com.example.admin.bookapp.data.CustomBookListContract;



public class MyListActivity extends DrawerActivity {

    private static final String TAG = "MY_LIST_ACTIVITY_TAG";
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
        setTitle(R.string.my_list);

//        customBookName = (EditText) findViewById(R.id.et_custom_book_name);
//        customAuthorName = (EditText) findViewById(R.id.et_custom_author_name);

        RecyclerView mListRecyclerView = (RecyclerView) findViewById(R.id.rv_books_list);
        mListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        CustomBookListDbHelper dbHelper =  new CustomBookListDbHelper(this);
//        mDb = dbHelper.getReadableDatabase();

        Cursor cursor = getBooksInMyList();
        mAdapter = new BooksAdapter(this, cursor);
        mListRecyclerView.setAdapter(mAdapter);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int id = (int)viewHolder.itemView.getTag();
                String stringId = Integer.toString(id);
                Uri uri = BookListContract.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();
                Log.e(TAG, "onSwiped: "+uri.toString() );

                ContentValues cv = new ContentValues();
                cv.put(BookListContract.COLUMN_IN_MY_LIST, "Нет");
                int updated = getContentResolver().update(uri, cv, null, null);
                Toast.makeText(MyListActivity.this, String.valueOf(updated), Toast.LENGTH_SHORT).show();
                mAdapter.swapCursor(getBooksInMyList());
            }
        }).attachToRecyclerView(mListRecyclerView);
    }



    public void addToBookList(View view){
        if (customBookName.getText().length()==0 || customAuthorName.getText().length()==0)return;
        addNewBook(customBookName.getText().toString(), customAuthorName.getText().toString());
        Cursor newCursor = getBooksInMyList();
        mAdapter.swapCursor(newCursor);

        customAuthorName.clearFocus();
        customBookName.getText().clear();
        customAuthorName.getText().clear();
    }

    public Cursor getBooksInMyList(){
        return getContentResolver().query(BookListContract.CONTENT_URI,
                null,
                BookListContract.COLUMN_IN_MY_LIST+" =?",
                new String[]{"Да"},
                null);
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



    private void addNewBook(String bookName, String authorName){
        ContentValues cv = new ContentValues();
        cv.put(CustomBookListContract.CustomBookListItem.COLUMN_CUSTOM_BOOK_NAME, bookName);
        cv.put(CustomBookListContract.CustomBookListItem.COLUMN_CUSTOM_BOOK_AUTHOR, authorName);
        Uri uri = getContentResolver().insert(BookListContract.CONTENT_URI, cv);
        if (uri!=null){
            Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_SHORT).show();
        }
        finish();
       // return mDb.insert(CustomBookListContract.CustomBookListItem.TABLE_NAME, null, cv);
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
