package com.example.admin.bookapp;

import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.admin.bookapp.data.BookListContentProvider;
import com.example.admin.bookapp.data.BookListContract;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MAIN ACTIVITY_TAG";
    private TextView mWelcomeTextView;
    private TextView mBookLoverTextView;
    private TextView mChooseBookLanguage;
    private Button mRusButton;
    private Button mKazButton;
    public static final String MY_LAN_PREFS = "My language settings";
    private static String value="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWelcomeTextView = (TextView) findViewById(R.id.tv_welcome);
        mBookLoverTextView = (TextView) findViewById(R.id.tv_book_lover);
        mChooseBookLanguage = (TextView) findViewById(R.id.tv_choose_book_language);
        mRusButton = (Button) findViewById(R.id.btn_rus);
        mKazButton = (Button) findViewById(R.id.btn_kaz);
        Log.e(TAG, "onCreate: created ");
    }

    public void butLanClick(View view){

        switch (view.getId()) {
            case R.id.btn_rus:
                value=getString(R.string.lan_rus);
                break;
            case R.id.btn_kaz:
                value=getString(R.string.lan_kaz);
                break;
            default:
                break;
        }
        Log.e(TAG, "butLanClick: "+ value);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString("lan", value);
        editor.apply();
        ContentResolver resolver = getApplicationContext().getContentResolver();
        ContentProviderClient client = resolver.acquireContentProviderClient(BookListContract.CONTENT_URI);
        BookListContentProvider contentProvider = (BookListContentProvider) client.getLocalContentProvider();
        contentProvider.resetDatabase();

        Intent intent = new Intent(this, ReadPagerActivity.class);
        startActivity(intent);
    }


}
