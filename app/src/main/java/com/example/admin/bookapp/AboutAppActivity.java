package com.example.admin.bookapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutAppActivity extends DrawerActivity {

    private ImageView mImageView;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_about_app, mContentFrame);
        setTitle("О приложении");
        mImageView = (ImageView)findViewById(R.id.iv_app_icon);
        mTextView = (TextView) findViewById(R.id.tv_about_app);
        mTextView.setText("Эврика - приложение, которое поможет Вам выбрать, что почитать. Приложение не является стандартным " +
                "рекомендательным сервисом и здесь не нужно заполнять многочисленные списки для того, чтобы выбрать интересную книгу для чтения. " +
                "Вы просто начинаете читать случайно выпавший отрывок, не зная ни названия произведения, ни его автора, и основывайте " +
                "свой выбор на прочитанном фрагменте. \n\n"+
                "Внимание! Это приложение не является электронной библиотекой и предназначено только для того, чтобы помочь Вам найти" +
                " то, что Вы давно искали :)");
    }
}
