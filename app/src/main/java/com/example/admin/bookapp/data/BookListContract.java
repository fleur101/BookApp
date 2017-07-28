package com.example.admin.bookapp.data;

import android.provider.BaseColumns;

/**
 * Created by Admin on 14.07.2017.
 */

public class BookListContract {

//    public static final class BookListItem implements BaseColumns{
//        public static final String TABLE_NAME="bookList";
//        public static final String COLUMN_BOOK_NAME="bookName";
//        public static final String COLUMN_BOOK_AUTHOR="bookAuthor";
//        public static final String COLUMN_BOOK_PAGE="bookPage";
//        public static final String COLUMN_TIMESTAMP = "timestamp";
//        public static final String COLUMN_ALREADY_SHOWN="bookShown";
//        public static final String COLUMN_IN_MY_LIST="bookInList";
//    }

    public static final class CustomBookListItem implements BaseColumns{
        public static final String TABLE_NAME="customBookList";
        public static final String COLUMN_CUSTOM_BOOK_NAME="customBookName";
        public static final String COLUMN_CUSTOM_BOOK_AUTHOR="customBookAuthor";
        public static final String COLUMN_TIMESTAMP="timestamp";
    }
}
