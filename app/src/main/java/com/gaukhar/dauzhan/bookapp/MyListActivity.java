package com.gaukhar.dauzhan.bookapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gaukhar.dauzhan.bookapp.data.MyBookListContract;
import com.gaukhar.dauzhan.bookapp.data.MyBookListDbHelper;


public class MyListActivity extends DrawerActivity {

    private static final String TAG = "MY_LIST_ACTIVITY_TAG";
    private SQLiteDatabase mDb;
    private BooksAdapter mAdapter;
    private TextView mNoBooksTextView;
    private long id;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_my_list_view, mContentFrame);
        setTitle(R.string.my_list);
        RecyclerView mListRecyclerView = (RecyclerView) findViewById(R.id.rv_books_list);

        mListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyBookListDbHelper dbHelper =  new MyBookListDbHelper(this);
        mDb = dbHelper.getReadableDatabase();
        mNoBooksTextView = (TextView) findViewById(R.id.tv_no_books_yet);
        Cursor cursor = getBooksInMyList();
        Log.e(TAG, "onCreate: got cursor");
        if (cursor.getCount() == 0) {
            mNoBooksTextView.setText(R.string.tv_no_books);
            mNoBooksTextView.setVisibility(View.VISIBLE);
        }
        mAdapter = new BooksAdapter(this, cursor, this);
        mListRecyclerView.setAdapter(mAdapter);
        registerForContextMenu(mListRecyclerView);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                long id = (long)viewHolder.itemView.getTag();
                removeBook(id);
                mAdapter.swapCursor(getBooksInMyList());
            }


        }).attachToRecyclerView(mListRecyclerView);


    }



    //    public Cursor getBooksInMyList(){
//        return getContentResolver().query(BookListContract.CONTENT_URI,
//                null,
//                BookListContract.COLUMN_IN_MY_LIST+" =?",
//                new String[]{"1"},
//                null);
//    }

    public Cursor getBooksInMyList(){
        return mDb.query(
                MyBookListContract.MyBookListItem.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                MyBookListContract.MyBookListItem.COLUMN_TIMESTAMP
        );
    }



//    private void addNewBook(String bookName, String authorName){
//        ContentValues cv = new ContentValues();
//        cv.put(MyBookListContract.MyBookListItem.MY_BOOK_NAME, bookName);
//        cv.put(MyBookListContract.MyBookListItem.MY_BOOK_AUTHOR, authorName);
//      //  Uri uri = getContentResolver().insert(BookListContract.CONTENT_URI, cv);
////        if (uri!=null){
////            Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_SHORT).show();
////        }
//    //    finish();
//       // return mDb.insert(MyBookListContract.CustomBookListItem.TABLE_NAME, null, cv);
//    }

    public  boolean removeBook(long id){
        return mDb.delete(MyBookListContract.MyBookListItem.TABLE_NAME, MyBookListContract.MyBookListItem._ID + " = " + id, null)>0;
    }


//    @Override
//    public boolean onItemLongClicked(long id) {
//        removeBook(id);
//        mAdapter.swapCursor(getBooksInMyList());
//        return true;
//    }
}
