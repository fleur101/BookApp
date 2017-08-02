package com.example.admin.bookapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.bookapp.data.BookListContract;
import com.example.admin.bookapp.data.BookListDbHelper;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BooksAdapterViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    public BooksAdapter(Context context, Cursor cursor){
        this.mContext = context;
        mCursor = cursor;
    }

    @Override
    public BooksAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.activity_my_list_view_item, parent, false);
        BooksAdapterViewHolder viewHolder = new BooksAdapterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BooksAdapterViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)){
            return;
        }
        String bookName = mCursor.getString(mCursor.getColumnIndex(BookListContract.COLUMN_BOOK_NAME));
        String bookAuthor = mCursor.getString(mCursor.getColumnIndex(BookListContract.COLUMN_BOOK_AUTHOR));
        holder.nameTextView.setText(bookName);
        holder.authorTextView.setText(bookAuthor);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        // Always close the previous mCursor first
        if (mCursor != null) mCursor.close();
        mCursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    class BooksAdapterViewHolder extends RecyclerView.ViewHolder{

        TextView nameTextView;
        TextView authorTextView;

        private BooksAdapterViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.tv_book_name);
            authorTextView = (TextView) itemView.findViewById(R.id.tv_author_name);
        }


    }
}
