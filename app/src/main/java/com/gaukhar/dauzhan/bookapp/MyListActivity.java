package com.gaukhar.dauzhan.bookapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaukhar.dauzhan.bookapp.data.MyBookListContract;
import com.gaukhar.dauzhan.bookapp.data.MyBookListDbHelper;


public class MyListActivity extends DrawerActivity {

    private static final String TAG = "MY_LIST_ACTIVITY_TAG";
    private SQLiteDatabase mDb;
    private BooksAdapter mAdapter;
    private TextView mNoBooksTextView;
    private TextView mSearchBooksTextView;
    private LinearLayout mNoBooksLinearLayout;
    private long id;
    private int numMyBooks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_my_list_view, mContentFrame);
        setTitle(R.string.my_list);
        RecyclerView mListRecyclerView = (RecyclerView) findViewById(R.id.rv_books_list);
        mListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyBookListDbHelper dbHelper =  new MyBookListDbHelper(this);
        mDb = dbHelper.getReadableDatabase();
        mNoBooksLinearLayout = (LinearLayout) findViewById(R.id.ll_no_books);
        mNoBooksTextView = (TextView) findViewById(R.id.tv_no_books_yet);
        mSearchBooksTextView = (TextView) findViewById(R.id.tv_search_books);
        Cursor cursor = getBooksInMyList();
        numMyBooks = cursor.getCount();
        Log.e(TAG, "onCreate: got cursor");
        mNoBooksTextView.setText(R.string.tv_no_books);
        mSearchBooksTextView.setText(R.string.tab_here);

        if (cursor.getCount() == 0) {
            mNoBooksLinearLayout.setVisibility(View.VISIBLE);
            mSearchBooksTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ReadActivity.class);
                    startActivity(intent);
                }
            });
        }
        mAdapter = new BooksAdapter(this, cursor, this);
        mListRecyclerView.setAdapter(mAdapter);
        registerForContextMenu(mListRecyclerView);


    }






    public void deleteBook(RecyclerView.ViewHolder holder) {
        long id = (long)holder.itemView.getTag();
        removeBook(id);
        mAdapter.swapCursor(getBooksInMyList());
        numMyBooks--;
        if (numMyBooks==0){
            mNoBooksTextView.setVisibility(View.VISIBLE);
            mSearchBooksTextView.setVisibility(View.VISIBLE);
            mSearchBooksTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ReadActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
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




    public  boolean removeBook(long id){
        return mDb.delete(MyBookListContract.MyBookListItem.TABLE_NAME, MyBookListContract.MyBookListItem._ID + " = " + id, null)>0;
    }


}
