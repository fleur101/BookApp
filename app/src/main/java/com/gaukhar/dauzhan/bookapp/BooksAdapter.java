package com.gaukhar.dauzhan.bookapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gaukhar.dauzhan.bookapp.data.MyBookListContract;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BooksAdapterViewHolder>  {

    private static final String TAG = "BOOKS_ADAPTER_TAG";
    private Context mContext;
    private Cursor mCursor;
    private MyListActivity myListActivity;


    public interface onItemLongClickListener{
        boolean onItemLongClicked(long id);
    }
    public BooksAdapter(Context context, Cursor cursor, MyListActivity listActivity){
        this.mContext = context;
        mCursor = cursor;
        myListActivity=listActivity;
    }

    @Override
    public BooksAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.activity_my_list_view_item, parent, false);
        BooksAdapterViewHolder viewHolder = new BooksAdapterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final BooksAdapterViewHolder holder, final int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }
        Log.e(TAG, "onBindViewHolder:not returned");
        String bookName = mCursor.getString(mCursor.getColumnIndex(MyBookListContract.MyBookListItem.COLUMN_BOOK_NAME));
        String bookAuthor = mCursor.getString(mCursor.getColumnIndex(MyBookListContract.MyBookListItem.COLUMN_BOOK_AUTHOR));
        long bookId = 0;
        try {
            bookId = mCursor.getLong(mCursor.getColumnIndex(MyBookListContract.MyBookListItem._ID));
        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder: for some dumb readon cannot return bookid");
        }
        holder.nameTextView.setText(bookName);
        holder.authorTextView.setText(bookAuthor);
        holder.itemView.setTag(bookId);
//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                Toast.makeText(mContext, "onlongclicked", Toast.LENGTH_SHORT).show();
//                long id = (long)holder.itemView.getTag();
//                myListActivity.onItemLongClicked(id);
//                return true;
//            }
//        });
    }




    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

//    public void onSwiped(final RecyclerView.ViewHolder viewHolder, RecyclerView recyclerView){
//        long id = (long)viewHolder.itemView.getTag();
//        Snackbar snackbar = Snackbar.make(recyclerView, " book removed", Snackbar.LENGTH_LONG)
//                .setAction("UNDO", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        int mAdapterPosition = viewHolder.getAdapterPosition();
//
//                    }
//                }
//    }

    public void swapCursor(Cursor newCursor) {
        // Always close the previous mCursor first
        if (mCursor != null) mCursor.close();
        mCursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }




    class BooksAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView authorTextView;

        private BooksAdapterViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.tv_book_name);
            authorTextView = (TextView) itemView.findViewById(R.id.tv_author_name);
//            itemView.setOnCreateContextMenuListener(this);
        }


//        @Override
//        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//            if (v.getId() == R.id.rv_books_list){
//                Log.e(TAG, "onCreateContextMenu: rv_book");
//                MenuInflater inflater = getMenuInflater();
//                inflater.inflate(R.menu.menu_list, menu);
//                id = (long)((AdapterView.AdapterContextMenuInfo)menuInfo).targetView.getTag();
//
//            } else if (v.getId() == R.id.ll_list_item){
//                Log.e(TAG, "onCreateContextMenu: ll_list_item");
//            } else{
//                Log.e(TAG, "onCreateContextMenu: no");
//            }
//        }
//
//        @Override
//        public boolean onContextItemSelected(MenuItem item) {
//            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//            switch (item.getItemId()){
//                case R.id.delete_menu_item:
//                    removeBook(id);
//                    mAdapter.swapCursor(getBooksInMyList());
//            }
//            return true;
//        }
    }

}
