package com.gaukhar.dauzhan.bookapp.data;

import android.provider.BaseColumns;



public class MyBookListContract {


    public static final class MyBookListItem implements BaseColumns{
        public static final String TABLE_NAME="myBookList";
        public static final String COLUMN_BOOK_NAME="myBookName";
        public static final String COLUMN_BOOK_AUTHOR="myBookAuthor";
        public static final String COLUMN_TIMESTAMP="timestamp";
    }
}
