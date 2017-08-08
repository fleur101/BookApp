package com.example.admin.bookapp.data;

import android.net.Uri;

/**
 * Created by Miras on 8/2/2017.
 */

public class BookListContract {

    public static final String AUTHORITY="com.example.admin.bookapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY);
    public static final String PATH_BOOK="Book";
    public static final Uri CONTENT_URI=BASE_CONTENT_URI.buildUpon().appendPath(PATH_BOOK).build();
    public static final String TABLE_NAME="Book";
    public static final String COLUMN_BOOK_ID="_id";
    public static final String COLUMN_BOOK_NAME="bookName";
    public static final String COLUMN_BOOK_AUTHOR="bookAuthor";
    public static final String COLUMN_BOOK_PAGE="bookPage";
    public static final String COLUMN_IN_MY_LIST="bookInList";
}
