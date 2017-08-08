package com.example.admin.bookapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutAppActivity extends DrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_about_app, mContentFrame);
        setTitle("О приложении");
        ImageView mImageView = (ImageView) findViewById(R.id.iv_app_icon);
        TextView mAboutAppTextView = (TextView) findViewById(R.id.tv_about_app);
        TextView mAboutAppIntroTextView = (TextView) findViewById(R.id.tv_about_app_intro);
        TextView mAboutAppNoteTextView = (TextView) findViewById(R.id.tv_about_app_note);
        TextView mAppNameTextView = (TextView) findViewById(R.id.tv_app_name);
        TextView mAppAuthorTextVew = (TextView) findViewById(R.id.app_author_name);
        mAppNameTextView.setText("Эврика!");
        mAboutAppIntroTextView.setText("Эврика - приложение, которое поможет Вам выбрать, что почитать. \n");
        mAboutAppTextView.setText("Вы начинаете читать случайно выпавший отрывок, не зная ни названия произведения, ни его автора, и основываете свой выбор только на прочитанном фрагменте. \n\n");
        mAboutAppNoteTextView.setText("Внимание! Это приложение не является электронной библиотекой и предназначено только для того, чтобы помочь Вам найти то, что Вы давно искали :)");
        mAppAuthorTextVew.setText("Gaukhar Dauzhan\ngaukhard96@gmail.com");

    }
}
