package com.example.admin.bookapp.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.admin.bookapp.R;
import com.example.admin.bookapp.ReadPagerActivity;
import com.example.admin.bookapp.data.DatabaseAccess;


public class ReadPageFragment extends Fragment {



    private ScrollView mBookPageScrollView;
    private TextView mBookPageTextView;
    private Button mWhatBookButton;
    private TextView mBookPageNumTextView;

    private static int bookId;
    private static String bookName;
    private static String bookAuthor;
    private static String bookPage;



    public static ReadPageFragment newInstance(int bookId, String bookName, String bookAuthor, String bookPage) {
        ReadPageFragment fragment = new ReadPageFragment();
        Bundle args = new Bundle();
        args.putInt("bookId", bookId);
        args.putString("bookName", bookName);
        args.putString("bookAuthor", bookAuthor);
        args.putString("bookPage", bookPage);
        fragment.setArguments(args);
        return fragment;
    }


    public ReadPageFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            bookId = getArguments().getInt("bookId");
            bookName = getArguments().getString("bookName");
            bookAuthor=getArguments().getString("bookAuthor");
            bookPage=getArguments().getString("bookPage");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_read_pager, container, false);

        mBookPageNumTextView = (TextView) view.findViewById(R.id.tv_book_page_num);
        mBookPageScrollView = (ScrollView) view.findViewById(R.id.sv_book_contents);
        mBookPageTextView =(TextView)  view.findViewById(R.id.tv_book_contents);
        mWhatBookButton = (Button) view.findViewById(R.id.btn_what_book);

//        Cursor cursor = DatabaseAccess.getBookPage();
//        cursor.moveToFirst();
//
//        final int bookId = cursor.getInt(cursor.getColumnIndex(BookListDbHelper.COLUMN_BOOK_ID));
//        final String bookAuthor = cursor.getString(cursor.getColumnIndex(BookListDbHelper.COLUMN_BOOK_NAME));
//        final String bookName = cursor.getString(cursor.getColumnIndex(BookListDbHelper.COLUMN_BOOK_AUTHOR));
//        String bookPage = cursor.getString(cursor.getColumnIndex(BookListDbHelper.COLUMN_BOOK_PAGE));
//        DatabaseAccess.updatePageShown(bookId);

        mBookPageNumTextView.setText("Отрывок №"+bookId);
        mBookPageTextView.setText(bookPage);
        mWhatBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ReadPagerActivity)getActivity()).btnClickWhatBook(bookId, bookName, bookAuthor);
            }
        });

        return view;

    }

    @Override
    public void onDestroy() {
        Cursor cursor = DatabaseAccess.getAllBooks();

        while (cursor.moveToNext()){
            DatabaseAccess.updatePageNotShown();
        }
        cursor.close();

        super.onDestroy();
    }
}
