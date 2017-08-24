package com.gaukhar.dauzhan.bookapp.Fragments;

import android.animation.LayoutTransition;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gaukhar.dauzhan.bookapp.DrawerActivity;
import com.gaukhar.dauzhan.bookapp.R;
import com.gaukhar.dauzhan.bookapp.ReadActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ReadFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";
    private static final String TAG = "READ_FRAGMENT_TAG";
    private int fontSize;
    private String fontType;
    private ProgressBar mLoadingIndicator;

    public static final String READ_PAGE_FRAGMENT_TAG="READ_FRAGMENT_TAG";
    private int bookId;
    private String bookName;
    private String bookAuthor;
    private String bookPage;

    private TextView mTransitionTextView;
    private RelativeLayout mReadLoaderLayout;

    private TextView mBookPageTextView;
    private Button mWhatBookButton;
    private Button mNextBookButton;
    private ImageView mArrowImageview;
    private CardView mCardView;
    private ScrollView mReadScrollView;
    private boolean mExpanded = false;
    private int px;
    private LinearLayout.LayoutParams mCompressedParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,900);
    private LinearLayout.LayoutParams mExpandedParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    public ReadFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //firebase

        if (getArguments()!=null) {
            if (getArguments().containsKey(ARG_ITEM_ID)) {
                bookId = getArguments().getInt(ARG_ITEM_ID);
                fontSize = getArguments().getInt("fontSize");
                fontType = getArguments().getString("font");

            }
        } else{
            Log.e(READ_PAGE_FRAGMENT_TAG, "onCreate: no args got in fragment" );
        }
        setRetainInstance(true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_read, container, false);

        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        px =(int) (Math.round(15*(metrics.densityDpi/160f))); // MAKE IT FINAL
        mCompressedParams.setMargins(px, px, px, px);
//        mCompressedParams.getPercentLayoutInfo().bottomMarginPercent = 25*0.01f;
//        mCompressedParams.getPercentLayoutInfo().topMarginPercent = 5*0.01f;
//        mCompressedParams.getPercentLayoutInfo().leftMarginPercent = 5*0.01f;
//        mCompressedParams.getPercentLayoutInfo().rightMarginPercent = 5*0.01f;

        mExpandedParams.setMargins(px, px, px, px);

        mReadLoaderLayout = (RelativeLayout) view.findViewById(R.id.ll_read_load);
//        mLoadingIndicator = (ProgressBar) view.findViewById(R.id.pb_loading_indicator);
//        mTransitionTextView = (TextView) view.findViewById(R.id.tv_transition);

        mReadScrollView = (ScrollView) view.findViewById(R.id.sv_book_contents);
        mCardView = (CardView) view.findViewById(R.id.card_view);
        mBookPageTextView = (TextView) view.findViewById(R.id.tv_book_contents);
        mBookPageTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
       // mBookPageTextView.setTypeface(DrawerActivity.font_helvetica);
        switch (fontType){
            case "font_helvetica":
                 mBookPageTextView.setTypeface(DrawerActivity.font_helvetica);
                Log.e(TAG, "onCreateView: font helvetica");
                break;
            case "font_roboto":
                 mBookPageTextView.setTypeface(DrawerActivity.font_roboto);
                Log.e(TAG, "onCreateView: font roboto");
                break;
            case "font_kurale":
                 mBookPageTextView.setTypeface(DrawerActivity.font_kurale);
                Log.e(TAG, "onCreateView: font kurale");
                break;
        }

        mWhatBookButton = (Button) view.findViewById(R.id.btn_what_book);
        mNextBookButton = (Button) view.findViewById(R.id.btn_next);
        mArrowImageview = (ImageView) view.findViewById(R.id.iv_read_more);

       // mLoadingIndicator.setVisibility(View.VISIBLE);
   //     mTransitionTextView.setText("Ищем для Вас интересный отрывок...");
      //  mTransitionTextView.setVisibility(View.VISIBLE);
        mReadLoaderLayout.setVisibility(View.VISIBLE);
        //firebase
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("books/"+String.valueOf(bookId));
        // Attach a listener to read the data at our posts reference
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "onDataChange: "+dataSnapshot.toString());
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    Log.e(TAG, "onDataChange: got into looop");
                    String key = child.getKey();
                    Log.e(TAG, "onDataChange: inside loop: "+key);
                    if (key.compareTo("bookName") == 0)bookName=(String)child.getValue();
                    if (key.compareTo("bookAuthor") == 0)bookAuthor=(String)child.getValue();
                    if (key.compareTo("bookPage") == 0)bookPage=(String)child.getValue();

                }
                Log.e(TAG, "onDataChange: " +bookName+ " "+bookAuthor+ " "+bookPage);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mReadLoaderLayout.setVisibility(View.INVISIBLE);
                        mBookPageTextView.setText(bookPage);
                        mCardView.setLayoutParams(mCompressedParams);
                        mReadScrollView.setVisibility(View.VISIBLE);
                    }
                }, 1000);


                mCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Size changes will cause a LayoutTransition animation if the CHANGING
                        // transition is enabled
                        mCardView.setLayoutParams(mExpanded ? mCompressedParams : mExpandedParams);
                        mExpanded = !mExpanded;
                        if (mExpanded) {
                            Log.e(TAG, "onClick: not expanded");
                            mBookPageTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            mArrowImageview.setVisibility(View.INVISIBLE);
                        } else{
                            Log.e(TAG, "onClick: expanded");
                            mBookPageTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.arrow_small);
                            mArrowImageview.setVisibility(View.VISIBLE);
                        }
                        mCardView.requestLayout();
                    }


                });
                LayoutTransition transition = mCardView.getLayoutTransition();
                transition.enableTransitionType(LayoutTransition.CHANGING);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: cancelled :(");
            }
        });
        Log.e(TAG, "onCreateView: got here after set text");

        mWhatBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ReadActivity) getActivity()).btnClickWhatBook(bookId, bookName, bookAuthor, fontType);
            }
        });
        mNextBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: mnextbuttonclicklistener");

                ((ReadActivity)getActivity()).replaceFragment();
            }
        });

        return view;
    }



    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
