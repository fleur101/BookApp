package com.gaukhar.dauzhan.bookapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MAIN ACTIVITY_TAG";
    private TextView mWelcomeTextView;
    private TextView mBookLoverTextView;
    private TextView mChooseBookLanguage;
    private Button mGoToReadActivity;
    public static final String MY_LAN_PREFS = "My language settings";
    private static String value="";
    private PrefManager prefManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWelcomeTextView = (TextView) findViewById(R.id.tv_welcome);
        mBookLoverTextView = (TextView) findViewById(R.id.tv_book_lover);
       // mGoToReadActivity = (Button) findViewById(R.id.btn_go_to_read_activity);
        Log.e(TAG, "onCreate: created ");
       // prefManager = new PrefManager(this);

//        if (!prefManager.isFirstTimeLaunch()) {
//            launchHomeScreen();
//            finish();
//        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), ReadActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        }, 1000);
    }

    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(this, ReadActivity.class));
        finish();
    }

//    public void butLanClick(View view){
//        Intent intent = new Intent(this, ReadActivity.class);
//        startActivity(intent);
//    }


}
