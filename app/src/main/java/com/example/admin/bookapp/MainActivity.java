package com.example.admin.bookapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private TextView mErrorMsgDisplayTextView;
    private TextView mWelcomeTextView;
    private TextView mBookLoverTextView;
    private TextView mChooseBookLanguage;
    private Button mRusButton;
    private Button mKazButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mErrorMsgDisplayTextView = (TextView) findViewById(R.id.tv_error_message_display);
        mWelcomeTextView = (TextView) findViewById(R.id.tv_welcome);
        mBookLoverTextView = (TextView) findViewById(R.id.tv_book_lover);
        mChooseBookLanguage = (TextView) findViewById(R.id.tv_choose_book_language);
        mRusButton = (Button) findViewById(R.id.btn_rus);
        mKazButton = (Button) findViewById(R.id.btn_kaz);
    }

    public void butLanClick(View view){
        Intent intent = new Intent(this, ReadPagerActivity.class);
        intent.putExtra("lan", view.getId());
        startActivity(intent);
    }

}
