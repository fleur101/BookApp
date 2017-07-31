package com.example.admin.bookapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private static final String TAG_MAIN_ACTIVITY = "MAIN ACTIVITY";
    private TextView mWelcomeTextView;
    private TextView mBookLoverTextView;
    private TextView mChooseBookLanguage;
    private Button mRusButton;
    private Button mKazButton;
    public static final String MY_LAN_PREFS = "My language settings";
    private static String key="lan";
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
    }

    public void butLanClick(View view){
        SharedPreferences.Editor editor = getSharedPreferences(MY_LAN_PREFS, MODE_PRIVATE).edit();

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
        Log.e(TAG_MAIN_ACTIVITY, "butLanClick: "+ value);
        editor.putString(key, value);
        editor.apply();

        Intent intent = new Intent(this, ReadPagerActivity.class);
        startActivity(intent);
    }


}
