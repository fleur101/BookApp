package com.example.admin.bookapp.data;

import android.provider.BaseColumns;

/**
 * Created by Admin on 14.07.2017.
 */

public class CustomBookListContract {


    public static final class CustomBookListItem implements BaseColumns{
        public static final String TABLE_NAME="customBookList";
        public static final String COLUMN_CUSTOM_BOOK_NAME="customBookName";
        public static final String COLUMN_CUSTOM_BOOK_AUTHOR="customBookAuthor";
        public static final String COLUMN_TIMESTAMP="timestamp";
    }
}
