package com.gaukhar.dauzhan.bookapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gaukhar.dauzhan.bookapp.data.MyBookListContract;

class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BooksAdapterViewHolder>  {

    private static final String TAG = "BOOKS_ADAPTER_TAG";
    private Context mContext;
    private Cursor mCursor;
    private MyListActivity myListActivity;
    private String fontType;
    private SharedPreferences.OnSharedPreferenceChangeListener mListener;



    BooksAdapter(Context context, Cursor cursor, MyListActivity listActivity){
        this.mContext = context;
        mCursor = cursor;
        myListActivity=listActivity;
    }

    @Override
    public BooksAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        setupSharedPreferences();
        setListener();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.activity_my_list_view_item, parent, false);
        return new BooksAdapterViewHolder(view);
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
      //  holder.nameTextView.setTypeface(DrawerActivity.font_helvetica);
        switch (fontType){
            case "font_helvetica":
                holder.nameTextView.setTypeface(DrawerActivity.font_helvetica);
                break;

            case "font_roboto":
                holder.nameTextView.setTypeface(DrawerActivity.font_roboto);
                break;

            case "font_kurale":
                holder.nameTextView.setTypeface(DrawerActivity.font_kurale);
                break;

        }
        holder.itemView.setTag(bookId);
        holder.buttonViewOption.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(mContext, holder.buttonViewOption);
                //inflating menu from xml resource
                popup.inflate(R.menu.list_item_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                  @Override
                public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete_book:
                                AlertDialog.Builder builder = new AlertDialog.Builder(myListActivity);
                                    builder.setMessage("Удалить книгу из списка?");
                                    builder.setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            myListActivity.deleteBook(holder);
                                        }
                                })
                                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                        });
                                                    // Create the AlertDialog object and return it
                                    builder.create();
                                builder.show();
                        }
                        return true;
                  }
                });
                //displaying the popup
                popup.show();
            }
        });
    }
    private void setupSharedPreferences() {
        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            fontType = sharedPreferences.getString("font", "font_helvetica");
            Log.e(TAG, "setupprefs: tv font" + fontType);
        } catch (Exception ex) {
            Log.e(TAG, "setupprefs: tv font exception");
            ex.printStackTrace();
        }
    }

    private void setListener(){
        mListener = new SharedPreferences.OnSharedPreferenceChangeListener() {

            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                switch (key) {
                    case "font":
                        Log.e(TAG, "onSharedPreferenceChanged: key is font");
                        try {
                            fontType = sharedPreferences.getString(key, "font_helvetica");
                            Log.e(TAG, "onSharedPreferenceChanged: fontPref" + fontType);
                            break;
                        } catch (Exception ex) {
                            Log.e(TAG, "onSharedPreferenceChanged: font_size_pref exception");
                            ex.printStackTrace();
                        }
                        break;
                    default:
                        Log.e(TAG, "key is not recognized");
                        break;
                }
            }

        };
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

    void swapCursor(Cursor newCursor) {
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
        TextView buttonViewOption;

        private BooksAdapterViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.tv_book_name);
            authorTextView = (TextView) itemView.findViewById(R.id.tv_author_name);
            buttonViewOption = (TextView) itemView.findViewById(R.id.textViewOptions);
        }


    }

}
