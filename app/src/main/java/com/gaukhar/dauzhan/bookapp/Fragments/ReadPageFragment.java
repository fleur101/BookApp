package com.gaukhar.dauzhan.bookapp.Fragments;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gaukhar.dauzhan.bookapp.R;
import com.gaukhar.dauzhan.bookapp.ReadPagerActivity;
import com.gaukhar.dauzhan.bookapp.data.BookListContract;


public class ReadPageFragment extends Fragment {


    public static final String ARG_ITEM_ID = "item_id";
    private static final String TAG = "READ_PAGE_FRAGMENT_TAG";
    private int id;
    private int fontSize;

    public static final String READ_PAGE_FRAGMENT_TAG="READ_PAGE_FRAGMENT_TAG";
    private int bookId;
    private int positionVis;
    private String bookName;
    private String bookAuthor;
    private String bookPage;
    private ImageButton leftNav;
    private ImageButton rightNav;

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
                fontSize = getArguments().getInt("fontSize");
                positionVis = getArguments().getInt("positionVis");
                Log.e(READ_PAGE_FRAGMENT_TAG, "onCreate: position"+positionVis);
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
        TextView mBookPageTextView = (TextView) view.findViewById(R.id.tv_book_contents);
        mBookPageTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        Button mWhatBookButton = (Button) view.findViewById(R.id.btn_what_book);
        leftNav = (ImageButton) view.findViewById(R.id.left_nav);
        rightNav = (ImageButton) view.findViewById(R.id.right_nav);
        if (id >= 0){
            Uri contentUri = ContentUris.withAppendedId(BookListContract.CONTENT_URI, id);
            Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null, null, null);
            if (cursor.moveToNext()) {
                bookId = cursor.getInt(cursor.getColumnIndex(BookListContract.COLUMN_BOOK_ID));
                bookAuthor = cursor.getString(cursor.getColumnIndex(BookListContract.COLUMN_BOOK_AUTHOR));
                bookName = cursor.getString(cursor.getColumnIndex(BookListContract.COLUMN_BOOK_NAME));
                bookPage = cursor.getString(cursor.getColumnIndex(BookListContract.COLUMN_BOOK_PAGE));
                Log.e(READ_PAGE_FRAGMENT_TAG, "onCreateView: book details: "+ bookId+ bookName+bookAuthor);
                Log.e(TAG, "onCreateView: TEXT SIZE " + mBookPageTextView.getTextSize());
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

        setArrowVisibility(positionVis);
        ///// set the strelochka invisible if position = 0 or length-1
        leftNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReadPagerActivity)getActivity()).leftOnClick();
            }
        });

        rightNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReadPagerActivity)getActivity()).rightOnClick();
            }

        });
        return view;
    }


    public void setArrowVisibility(int positionVis) {
        if (positionVis == 0){
            rightNav.setVisibility(View.VISIBLE);
            leftNav.setVisibility(View.INVISIBLE);
            Log.e(TAG, "onPageSelected: first"+positionVis);
        } else if (positionVis==1){
            leftNav.setVisibility(View.VISIBLE);
            rightNav.setVisibility(View.INVISIBLE);
            Log.e(TAG, "onPageSelected: last"+positionVis);
        } else if (positionVis==2){
            leftNav.setVisibility(View.VISIBLE);
            rightNav.setVisibility(View.VISIBLE);
            Log.e(TAG, "onPageSelected: not first not last"+positionVis);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }




}
