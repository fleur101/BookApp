package com.gaukhar.dauzhan.bookapp.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gaukhar.dauzhan.bookapp.DrawerActivity;
import com.gaukhar.dauzhan.bookapp.QuizActivity;
import com.gaukhar.dauzhan.bookapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;


public class QuizFragment extends Fragment {
    private static final String TAG = "QUIZ_FRAGMENT_TAG";
    private int questionId, questionNum, i;
    private String fontType, question, rightAuthor, rightTitle, wrongTitle;
    private int rightAnswerId, rightAnswerBtnId,  wrongAnswerBtnId;
    private Button btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4, rightBtnAnswer;
    private TextView questionTextView, questionNumTextView;
    private ProgressBar mLoadingIndicator;
    private LinearLayout mQuizFragmentRelativeLayout;
    private AlphaAnimation buttonClick;
    private boolean isRight=false;
    private Handler handler;
    private ArrayList<Integer> usedAnswerIdList, usedBtnAnswerIdList;
    private int[] num ;
    private DatabaseReference mDatabaseReference;
    private boolean valueExists=false;
    public QuizFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null) {
            if (getArguments().containsKey("font")) {
                fontType = getArguments().getString("font");
            }
            if (getArguments().containsKey("questionId")){
                questionId = getArguments().getInt("questionId");
            }
            if (getArguments().containsKey("questionNum")){
                questionNum = getArguments().getInt("questionNum");
            }
        } else{
            Log.e(TAG, "onCreate: no args got in fragment" );
        }
        setRetainInstance(true);



    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
        handler = new Handler();

        mLoadingIndicator = (ProgressBar) view.findViewById(R.id.pb_loading_indicator);
        questionNumTextView = (TextView) view.findViewById(R.id.tv_question_num);
        btnAnswer1 = (Button) view.findViewById(R.id.btn_quiz_answer_1);
        btnAnswer2 = (Button) view.findViewById(R.id.btn_quiz_answer_2);
        btnAnswer3 = (Button) view.findViewById(R.id.btn_quiz_answer_3);
        btnAnswer4 = (Button) view.findViewById(R.id.btn_quiz_answer_4);
        questionTextView = (TextView) view.findViewById(R.id.tv_quiz_question);
        mQuizFragmentRelativeLayout = (LinearLayout) view.findViewById(R.id.ll_fragment_quiz);
        buttonClick = new AlphaAnimation(1F, 0.8F);

        switch (fontType){
            case "font_helvetica":
                questionTextView.setTypeface(DrawerActivity.font_helvetica);
                Log.e(TAG, "onCreateView: font helvetica");
                break;

            case "font_roboto":
                questionTextView.setTypeface(DrawerActivity.font_roboto);
                Log.e(TAG, "onCreateView: font roboto");
                break;

            case "font_kurale":
                questionTextView.setTypeface(DrawerActivity.font_kurale);
                Log.e(TAG, "onCreateView: font kurale");
                break;

        }
        setValues();
        btnOnClickListener(btnAnswer1);
        btnOnClickListener(btnAnswer2);
        btnOnClickListener(btnAnswer3);
        btnOnClickListener(btnAnswer4);

        return view;
    }

    public void setValues() {
        final Random rand = new Random();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        DatabaseReference myQuestionReference = mDatabaseReference.child("quiz/questions/" + String.valueOf(questionId));
        usedAnswerIdList = new ArrayList<>();
        usedAnswerIdList.add(16);
        usedAnswerIdList.add(44);
        usedAnswerIdList.add(51);
        usedAnswerIdList.add(68);
        Log.e(TAG, "setValues: question id "+questionId);
        // DatabaseReference myQuestionRef = mFirebaseDatabase.getReference("quiz/questions/"+String.valueOf(questionId));
        myQuestionReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "myQuestionRef onDataChange: " + dataSnapshot.toString());
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String key = child.getKey();
                    if (key.compareTo("quote") == 0) question = (String) child.getValue();
                    if (key.compareTo("answerId") == 0)
                        rightAnswerId = ((Long) child.getValue()).intValue();
                }
                Log.e(TAG, "onDataChange: " + question + " " + rightAnswerId);
                Log.e(TAG, "setValues: " + String.valueOf(rightAnswerId));
                DatabaseReference myRightAnswerRef = mDatabaseReference.child("quiz/answers/" + String.valueOf(rightAnswerId));

                //  DatabaseReference myRightAnswerRef = FirebaseDatabase.getInstance().getReference("quiz/answers/"+String.valueOf(rightAnswerId));
                // Attach a listener to read the data at our posts reference
                myRightAnswerRef.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.e(TAG, "myRightAnswerRef onDataChange: " + dataSnapshot.toString());
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            String key = child.getKey();
                            if (key.compareTo("author") == 0)
                                rightAuthor = (String) child.getValue();
                            if (key.compareTo("title") == 0) rightTitle = (String) child.getValue();

                        }
                        Log.e(TAG, "myRightAnswerRef onDataChange: " + rightAuthor + " " + rightTitle);
                        rightAnswerBtnId = rand.nextInt(4) + 1;
                        Log.e(TAG, "onDataChange: " + rightAnswerBtnId);

                        num = new int[3];

                        usedBtnAnswerIdList = new ArrayList<>();
                        usedBtnAnswerIdList.add(rightAnswerBtnId);
                        usedAnswerIdList.add(rightAnswerId);
                        Log.e(TAG, "onDataChange: usedBtnAnswerIdList" + usedBtnAnswerIdList);
                        Log.e(TAG, "onDataChange:rightAnswerBtnIdList " + usedAnswerIdList);
                        switch (rightAnswerBtnId) {
                            case 1:
                                btnAnswer1.setText(rightTitle);
                                rightBtnAnswer = btnAnswer1;
                                Log.e(TAG, "onDataChange: rightAnswerBtnId 1");
                                break;
                            case 2:
                                btnAnswer2.setText(rightTitle);
                                rightBtnAnswer = btnAnswer2;
                                Log.e(TAG, "onDataChange: rightAnswerBtnId 2");
                                break;

                            case 3:
                                btnAnswer3.setText(rightTitle);
                                rightBtnAnswer = btnAnswer3;
                                Log.e(TAG, "onDataChange: rightAnswerBtnId 3");
                                break;

                            case 4:
                                btnAnswer4.setText(rightTitle);
                                rightBtnAnswer = btnAnswer4;
                                Log.e(TAG, "onDataChange: rightAnswerBtnId 4");
                                break;


                        }
                        for (i = 0; i < 3; i++) {
                             Log.e(TAG, "onDataChange: got into loop "+i);
                            num[i] = ((QuizActivity)getActivity()).generateRandom(0, 68, usedAnswerIdList);
                            DatabaseReference myWrongAnswersReference = mDatabaseReference.child("quiz/answers/" + String.valueOf(num[i]));
                            usedAnswerIdList.add(num[i]);
                            myWrongAnswersReference.addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Log.e(TAG, " myWrongAnswersRef onDataChange: " + dataSnapshot.toString());


                                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                                        String key = child.getKey();
                                        if (key.compareTo("title") == 0)
                                            wrongTitle = (String) child.getValue();
                                    }
                                    Log.e(TAG, " myWrongAnswersRef onDataChange: " + wrongTitle);
                                    wrongAnswerBtnId = ((QuizActivity)getActivity()).generateRandom(1, 4, usedBtnAnswerIdList);
                                    switch (wrongAnswerBtnId) {
                                        case 1:
                                            btnAnswer1.setText(wrongTitle);
                                            Log.e(TAG, "onDataChange: wrongAnswerBtnId 1");
                                            break;

                                        case 2:
                                            btnAnswer2.setText(wrongTitle);
                                            Log.e(TAG, "onDataChange: wrongAnswerBtnId 2");
                                            break;

                                        case 3:
                                            btnAnswer3.setText(wrongTitle);
                                            Log.e(TAG, "onDataChange: wrongAnswerBtnId 3");
                                            break;

                                        case 4:
                                            btnAnswer4.setText(wrongTitle);
                                            Log.e(TAG, "onDataChange: wrongAnswerBtnId 4");
                                            break;

                                    }
                                    usedBtnAnswerIdList.add(wrongAnswerBtnId);
                                    Log.e(TAG, "onDataChange: " + usedBtnAnswerIdList + usedAnswerIdList);


                                    questionNumTextView.setText(questionNum+"/5");
                                    questionTextView.setText('"'+question+'"');
                                    mLoadingIndicator.setVisibility(View.INVISIBLE);
                                    mQuizFragmentRelativeLayout.setVisibility(View.VISIBLE);
                                }


                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.e(TAG, "onCancelled: cancelled :(");
                                }
                            });
                            //   DatabaseReference myWrongAnswersRef = FirebaseDatabase.getInstance().getReference("quiz/questions/"+String.valueOf(num[i]));

                        }

                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled: cancelled :(");
                    }
                });
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: cancelled :(");
            }
        });

    }


    public void btnOnClickListener (final Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                if (button.getText().equals(rightTitle)){
                    button.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));

                    buttonBlink(button);
                    isRight=true;
                }else{
                    buttonBlink(rightBtnAnswer);

                    //  btnAnswer2.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorGray));
                    button.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                }

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (QuizActivity.getQuestionNum()==5) {
                            Log.e(TAG, "run: question num = 5 , so clear animation");
                            button.clearAnimation();
                            rightBtnAnswer.clearAnimation();
                        }
                        ((QuizActivity)getActivity()).btnOnClick(isRight);
                    }

                }, 2000);

            }
        });
    }
    public void buttonBlink (Button button){
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(300); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        button.startAnimation(anim);
    }
}
