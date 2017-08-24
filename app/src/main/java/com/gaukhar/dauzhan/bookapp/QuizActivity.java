package com.gaukhar.dauzhan.bookapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.gaukhar.dauzhan.bookapp.Fragments.QuizFragment;
import com.gaukhar.dauzhan.bookapp.Fragments.QuizResultDialogFragment;

import java.util.ArrayList;
import java.util.Random;

public class QuizActivity extends DrawerActivity {
    private String fontType;
    SharedPreferences.OnSharedPreferenceChangeListener mListener;
    private static final String TAG = "QUIZ_ACTIVITY_TAG";
    private ArrayList<Integer> usedQuestionIdList;
    private int mScore;

    private int questionId;
    private static int questionNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_quiz, mContentFrame);
        setTitle(R.string.quiz);
        usedQuestionIdList = new ArrayList<>();
        setupSharedPreferences();
        setListener();
        mScore=0;
        questionNum=0;
       // if(savedInstanceState == null) {
            createNewFragment();
      //  }
    }

    public static int getQuestionNum(){
        return questionNum;
    }

    public void btnOnClick(boolean isRight){
        if (isRight && questionNum<=5)mScore++;
        Log.e(TAG, "btnOnClick: "+mScore);
        if (questionNum==5){
            Log.e(TAG, "btnOnClick: used answer id list = 5");
            Bundle bundle = new Bundle();
            bundle.putInt("mScore", mScore);
            QuizResultDialogFragment dialogFragment = new QuizResultDialogFragment();
            dialogFragment.setArguments(bundle);
            dialogFragment.show(getSupportFragmentManager(), "quiz_result");

        } else {
            replaceFragment();
        }
    }

    public void setupSharedPreferences() {
        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            fontType = sharedPreferences.getString("font", "font_helvetica");
            Log.e(TAG, "setupprefs: tv font" + fontType);
        } catch (Exception ex) {
            Log.e(TAG, "setupprefs: tv font exception");
            ex.printStackTrace();
        }
    }

    public void setListener() {
        mListener = new SharedPreferences.OnSharedPreferenceChangeListener() {

            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                switch (key) {
                    case "font":
                        Log.e(TAG, "onSharedPreferenceChanged: key is font");
                        try {
                            fontType = sharedPreferences.getString(key, "font_helvetica");
                            Log.e(TAG, "onSharedPreferenceChanged: fontPref" + fontType);
                            break;
                        } catch (Exception ex) {
                            Log.e(TAG, "onSharedPreferenceChanged: font_size_pref exception");
                            ex.printStackTrace();
                        }
                        break;
                    default:
                        Log.e(TAG, "key is not recognized");
                        break;
                }
            }

        };
    }


    public void createNewFragment(){
        questionId = generateRandom(0, 68, usedQuestionIdList);
        usedQuestionIdList.add(questionId);
        questionNum++;
        Bundle args = new Bundle();
        args.putString("font", fontType);
        args.putInt("questionId", questionId);
        args.putInt("questionNum", questionNum);
        //create fragment
        QuizFragment fragment = new QuizFragment();
        fragment.setArguments(args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fragment_quiz_holder, fragment)
                .commit();
    }
    public void replaceFragment(){
        if (questionNum<=5)questionNum++;
        questionId = generateRandom(0, 68, usedQuestionIdList);
        usedQuestionIdList.add(questionId);
        Bundle args = new Bundle();
        args.putString("font", fontType);
        args.putInt("questionId", questionId);
        args.putInt("questionNum", questionNum);
        //create fragment
        QuizFragment fragment = new QuizFragment();
        fragment.setArguments(args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_quiz_holder, fragment)
                .commit();
    }


    public static int generateRandom(int start, int end, ArrayList<Integer> excludeIds) {
        Random rand = new Random();
        int range = end - start + 1;

        int random = rand.nextInt(range) + 1;
        while(excludeIds.contains(random)) {
            random = rand.nextInt(range) + 1;
        }

        return random;
    }


}
