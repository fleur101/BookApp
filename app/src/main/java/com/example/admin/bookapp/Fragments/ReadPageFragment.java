package com.example.admin.bookapp.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.bookapp.R;
import com.example.admin.bookapp.ReadPagerActivity;
import com.example.admin.bookapp.data.BookListDbHelper;
import com.example.admin.bookapp.data.DatabaseAccess;

import java.io.IOException;
import java.net.URL;


public class ReadPageFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {



    private ScrollView mBookPageScrollView;
    private TextView mBookPageTextView;
    private Button mWhatBookButton;
    private TextView mBookPageNumTextView;
    private ProgressBar mProgressBar;
    private Context mContext;

    public static final String READ_PAGE_FRAGMENT_TAG="read page fragment tag";
    public static int LOADER_ID = 123456;

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
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_read_pager, container, false);

        mContext = getContext();
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
//        mProgressBar.setVisibility(View.VISIBLE);
//        mBookPageScrollView.setVisibility(View.INVISIBLE);
        Log.e(READ_PAGE_FRAGMENT_TAG, "onStart: before get book page ");
//        Log.e(READ_PAGE_FRAGMENT_TAG, cursor.getColumnName(0) + " " +
//        cursor.getColumnName(1));

        View view = getView();
        if (view != null){

            mBookPageNumTextView = (TextView) view.findViewById(R.id.tv_book_page_num);
            mBookPageScrollView = (ScrollView) view.findViewById(R.id.sv_book_contents);
            mBookPageTextView =(TextView)  view.findViewById(R.id.tv_book_contents);
            mWhatBookButton = (Button) view.findViewById(R.id.btn_what_book);
            mProgressBar = (ProgressBar) view.findViewById(R.id.pb_loading_indicator);

        }

        LoaderManager loaderManager = getActivity().getSupportLoaderManager();
        Loader<String> mLoader = loaderManager.getLoader(LOADER_ID);
        loaderManager.initLoader(LOADER_ID, null, this);


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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(mContext) {

            // TODO (1) Create a String member variable called mGithubJson that will store the raw JSON
            public Cursor mData = null;


            @Override
            protected void onStartLoading() {

                /* If no arguments were passed, we don't have a query to perform. Simply return. */

                /*
                 * When we initially begin loading in the background, we want to display the
                 * loading indicator to the user
                 */
//                mProgressBar.setVisibility(View.VISIBLE);
//                mBookPageScrollView.setVisibility(View.INVISIBLE);

                // TODO (2) If mGithubJson is not null, deliver that result. Otherwise, force a load
                if (mData!=null){
                    deliverResult(mData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                return DatabaseAccess.getBookPage();
            }

            // TODO (3) Override deliverResult and store the data in mGithubJson

            @Override
            public void deliverResult(Cursor data) {
                mData = data;
                super.deliverResult(data);
            }

            // TODO (4) Call super.deliverResult after storing the data
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        final int bookId = data.getInt(data.getColumnIndex(BookListDbHelper.COLUMN_BOOK_ID));
        final String bookAuthor = data.getString(data.getColumnIndex(BookListDbHelper.COLUMN_BOOK_NAME));
        final String bookName = data.getString(data.getColumnIndex(BookListDbHelper.COLUMN_BOOK_AUTHOR));
        String bookPage = data.getString(data.getColumnIndex(BookListDbHelper.COLUMN_BOOK_PAGE));
        DatabaseAccess.updatePageShown(bookId);


        Toast.makeText(mContext, "Finished Loading", Toast.LENGTH_SHORT).show();
        mBookPageNumTextView.setText(getString(R.string.excerpt)+bookId);
        mBookPageTextView.setText(bookPage);
        mWhatBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ReadPagerActivity)getActivity()).btnClickWhatBook(bookId, bookName, bookAuthor);
            }
        });

        Toast.makeText(mContext, "Finished Loading 2", Toast.LENGTH_SHORT).show();

//        mProgressBar.setVisibility(View.INVISIBLE);
//        mBookPageScrollView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
