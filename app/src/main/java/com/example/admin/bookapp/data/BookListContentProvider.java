package com.example.admin.bookapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.admin.bookapp.MainActivity;
import com.example.admin.bookapp.R;



public class BookListContentProvider extends ContentProvider {

    private static final String TAG = "CONTENT_PROVIDER_TAG";
    private BookListDbHelper mDbHelper;
    private String dbName;
    private int dbVersion;

    public static final int BOOKS=100;
    public static final int BOOK_WITH_ID=101;

    private static final UriMatcher sUriMatcher=buildUriMatcher();


    @Override
    public boolean onCreate() {
        SharedPreferences settings = getContext().getSharedPreferences(MainActivity.MY_LAN_PREFS, getContext().MODE_PRIVATE);
        String key = "lan";
        String lan = settings.getString(key, "");
        if (lan.compareTo(getContext().getString(R.string.lan_rus)) == 0) {
            dbName=BookListDbHelper.DATABASE_NAME_RUS;
            dbVersion=BookListDbHelper.DATABASE_VERSION_RUS;
        } else if (lan.compareTo(getContext().getString(R.string.lan_kaz)) == 0) {
            dbName=BookListDbHelper.DATABASE_NAME_KZ;
            dbVersion=BookListDbHelper.DATABASE_VERSION_KZ;
        } else {
            Log.e(TAG, "onCreate: error database access initializing");
        }
        mDbHelper = new BookListDbHelper(getContext(), dbName, dbVersion);
        return false;
    }

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(BookListContract.AUTHORITY, BookListContract.PATH_BOOK, BOOKS);
        uriMatcher.addURI(BookListContract.AUTHORITY, BookListContract.PATH_BOOK +"/#", BOOKS);
        return uriMatcher;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor returnCursor;

        switch(match){
            case BOOKS:
                returnCursor = db.query(BookListContract.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case BOOK_WITH_ID:
                returnCursor=db.query(BookListContract.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match){
            case BOOKS:
                long id = db.insert(BookListContract.TABLE_NAME, null, values);
                if (id>0){
                    returnUri = ContentUris.withAppendedId(BookListContract.CONTENT_URI, id);
                } else{
                    throw new SQLException("Failed to insert row into "+uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
