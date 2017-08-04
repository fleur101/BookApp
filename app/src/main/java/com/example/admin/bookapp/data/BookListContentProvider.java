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
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.example.admin.bookapp.R;


public class BookListContentProvider extends ContentProvider {

    private static final String TAG = "CONTENT_PROVIDER_TAG";
    private BookListDbHelper mDbHelper;
    private static String dbName;
    private static int dbVersion;

    public static final int BOOKS=100;
    public static final int BOOK_WITH_ID=101;

    private static final UriMatcher sUriMatcher=buildUriMatcher();



    @Override
    public boolean onCreate() {
        Log.e(TAG, "onCreate: initial creation");
        setupSharedPreferences();
        mDbHelper = new BookListDbHelper(getContext(), dbName, dbVersion);
        return false;
    }

    private void setupSharedPreferences(){

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        String lan = settings.getString("lan", "");

        if (!lan.equals("")) {
            Log.e(TAG, "setupSharedPreferences: " + lan);
            String lanRus = getContext().getString(R.string.lan_rus);
            String lanKaz = getContext().getString(R.string.lan_kaz);
            if (lan.compareTo(lanRus) == 0) {
                Log.e(TAG, "onCreate: lan_prefs=lan_rus");
                dbName = BookListDbHelper.DATABASE_NAME_RUS;
                dbVersion = BookListDbHelper.DATABASE_VERSION_RUS;
            } else if (lan.compareTo(lanKaz) == 0) {
                Log.e(TAG, "onCreate: lan_prefs=lan_kaz");
                dbName = BookListDbHelper.DATABASE_NAME_KZ;
                dbVersion = BookListDbHelper.DATABASE_VERSION_KZ;
            } else {
                Log.e(TAG, "onCreate: error database access initializing");
            }
        } else {
            dbName=BookListDbHelper.DATABASE_NAME_RUS;
            dbVersion=BookListDbHelper.DATABASE_VERSION_RUS;
        }

    }



    public void resetDatabase(){
        Log.e(TAG, "resetDatabase: got here");
        mDbHelper.close();
        setupSharedPreferences();
        mDbHelper= new BookListDbHelper(getContext(), dbName, dbVersion);
    }

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(BookListContract.AUTHORITY, BookListContract.PATH_BOOK, BOOKS);
        uriMatcher.addURI(BookListContract.AUTHORITY, BookListContract.PATH_BOOK +"/#", BOOK_WITH_ID);
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
                Log.e(TAG, "query: cursor without id");
                returnCursor = db.query(BookListContract.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case BOOK_WITH_ID:
                Log.e(TAG, "query: cursor with id");
                String id = uri.getPathSegments().get(1);
                String mSelection = "_id=?";
                String [] mSelectionArgs = new String[]{id};
                returnCursor=db.query(BookListContract.TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
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
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int tasksDeleted;
        switch(match) {
            case BOOK_WITH_ID:
                Log.e(TAG, "delete: cursor with id");
                String id = uri.getPathSegments().get(1);
                tasksDeleted=db.delete(BookListContract.TABLE_NAME, " _id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (tasksDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return tasksDeleted;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int tasksUpdated;
        int match = sUriMatcher.match(uri);

        switch (match) {
            case BOOK_WITH_ID:
                Log.e(TAG, "update: update with id");
                String id = uri.getPathSegments().get(1);
                Log.e(TAG, "update: id num = "+id+" table name :"+ BookListContract.TABLE_NAME);
                tasksUpdated = mDbHelper.getWritableDatabase().update(BookListContract.TABLE_NAME, values, "_id=?", new String[]{id});
                Log.e(TAG, "update: "+tasksUpdated);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (tasksUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return tasksUpdated;

    }







}
