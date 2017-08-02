package com.example.admin.bookapp.Fragments;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.admin.bookapp.R;
import com.example.admin.bookapp.ReadPagerActivity;
import com.example.admin.bookapp.data.BookListContract;


public class ReadPageFragment extends Fragment {


    public static final String ARG_ITEM_ID = "item_id";
    private int id;

    public static final String READ_PAGE_FRAGMENT_TAG="READ_PAGE_FRAGMENT_TAG";
    private int bookId;
    private String bookName;
    private String bookAuthor;
    private String bookPage;

    static ReadPageFragment newInstance(int num) {
        ReadPageFragment fragment = new ReadPageFragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        fragment.setArguments(args);
        return fragment;
    }


    public ReadPageFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null) {
            if (getArguments().containsKey(ARG_ITEM_ID)) {
                id = getArguments().getInt(ARG_ITEM_ID);
                Log.e(READ_PAGE_FRAGMENT_TAG, "onCreate: got id" +id);
            }
        } else{
            Log.e(READ_PAGE_FRAGMENT_TAG, "onCreate: no args got in fragment" );
        }
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_read_pager, container, false);
        TextView mBookPageNumTextView = (TextView) view.findViewById(R.id.tv_book_page_num);
        ScrollView mBookPageScrollView = (ScrollView) view.findViewById(R.id.sv_book_contents);
        TextView mBookPageTextView = (TextView) view.findViewById(R.id.tv_book_contents);
        Button mWhatBookButton = (Button) view.findViewById(R.id.btn_what_book);

        if (id >= 0){
            Log.e(READ_PAGE_FRAGMENT_TAG, "onCreateView: id>=0");
            Uri contentUri = ContentUris.withAppendedId(BookListContract.CONTENT_URI, id);
            Log.e(READ_PAGE_FRAGMENT_TAG, "onCreateView: "+contentUri.toString());
            String selection="_id = ?";
            String[] selectionArgs={String.valueOf(id)};
            Cursor cursor = getActivity().getContentResolver().query(contentUri, null, selection, selectionArgs, null);
            Log.e(READ_PAGE_FRAGMENT_TAG, "onCreateView: "+cursor.getCount()+cursor.toString());

            if (cursor.moveToNext()) {
                Log.e(READ_PAGE_FRAGMENT_TAG, "onCreateView: CURSOR MOVED TO FIRST");

                bookId = cursor.getInt(cursor.getColumnIndex(BookListContract.COLUMN_BOOK_ID));
                bookAuthor = cursor.getString(cursor.getColumnIndex(BookListContract.COLUMN_BOOK_NAME));
                bookName = cursor.getString(cursor.getColumnIndex(BookListContract.COLUMN_BOOK_AUTHOR));
                bookPage = cursor.getString(cursor.getColumnIndex(BookListContract.COLUMN_BOOK_PAGE));
               // DatabaseAccess.updatePageShown(bookId);
                Log.e(READ_PAGE_FRAGMENT_TAG, "onCreateView: book details: "+ bookId+ bookName+bookAuthor);

                mBookPageNumTextView.setText(getString(R.string.excerpt) + bookId);
                mBookPageTextView.setText(bookPage);
                mWhatBookButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((ReadPagerActivity) getActivity()).btnClickWhatBook(bookId, bookName, bookAuthor);
                    }
                });
            }
            cursor.close();


        }


        return view;

    }

    @Override
    public void onStart() {
        super.onStart();

      //  Log.e(READ_PAGE_FRAGMENT_TAG, "onStart: before get book page ");
//        Cursor cursor = DatabaseAccess.getBookPage();
//        cursor.moveToFirst();
//        Log.e(READ_PAGE_FRAGMENT_TAG, cursor.getColumnName(0) + " " +
//        cursor.getColumnName(1));

    }

    @Override
    public void onDestroy() {
//        Cursor cursor = DatabaseAccess.getAllBooks();
//
//        while (cursor.moveToNext()){
//            DatabaseAccess.updatePageNotShown();
//        }
//        cursor.close();

        super.onDestroy();
    }




}
