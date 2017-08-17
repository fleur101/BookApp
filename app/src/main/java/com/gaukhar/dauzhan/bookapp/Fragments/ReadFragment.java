package com.gaukhar.dauzhan.bookapp.Fragments;

import android.animation.LayoutTransition;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    private ProgressBar mLoadingIndicator;

    public static final String READ_PAGE_FRAGMENT_TAG="READ_FRAGMENT_TAG";
    private int bookId;
    private String bookName;
    private String bookAuthor;
    private String bookPage;
    private int tvFontSize;
    private TextView mTransitionTextView;
    private TextView mBookPageTextView;
    private Button mWhatBookButton;
    private Button mNextBookButton;
    private ImageView mArrowImageview;
    private CardView mCardView;
    private LinearLayout mCardViewHolderLayout;
    private boolean mExpanded = false;
    private LinearLayout.LayoutParams mCompressedParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 500);
    private LinearLayout.LayoutParams mExpandedParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    private FrameLayout.LayoutParams MATCH_PARENT_PARAM = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    private FrameLayout.LayoutParams WRAP_CONTENT_PARAM = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

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
            }
        } else{
            Log.e(READ_PAGE_FRAGMENT_TAG, "onCreate: no args got in fragment" );
        }
        setRetainInstance(true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_read, container, false);

        mLoadingIndicator = (ProgressBar) view.findViewById(R.id.pb_loading_indicator);
        mTransitionTextView = (TextView) view.findViewById(R.id.tv_transition);
        mCardViewHolderLayout = (LinearLayout) view.findViewById(R.id.layout_scroller);
        mCardView = (CardView) view.findViewById(R.id.card_view);
        mBookPageTextView = (TextView) view.findViewById(R.id.tv_book_contents);
        mBookPageTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        mWhatBookButton = (Button) view.findViewById(R.id.btn_what_book);
        mNextBookButton = (Button) view.findViewById(R.id.btn_next);
        mArrowImageview = (ImageView) view.findViewById(R.id.iv_read_more);
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mTransitionTextView.setText("Ищем для Вас нечто особенное...");
        mTransitionTextView.setVisibility(View.VISIBLE);
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
                    Log.e(TAG, "onDataChange: " +bookName+ " "+bookAuthor+ " "+bookPage);
                    mLoadingIndicator.setVisibility(View.INVISIBLE);
                    mTransitionTextView.setVisibility(View.INVISIBLE);

                    mBookPageTextView.setText("     "+'"'+bookPage+'"');
                    DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
                    int px =(int) (Math.round(15*(metrics.densityDpi/160f))); // MAKE IT FINAL
                    mCompressedParams.setMargins(px, px, px, px);
                    mExpandedParams.setMargins(px, px, px, px);
                    mCardView.setLayoutParams(mCompressedParams);
                    mCardView.setVisibility(View.VISIBLE);
                    mWhatBookButton.setVisibility(View.VISIBLE);
                    mNextBookButton.setVisibility(View.VISIBLE);

                    mCardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                    // Size changes will cause a LayoutTransition animation if the CHANGING
                                    // transition is enabled
                            mCardView.setLayoutParams(mExpanded ? mCompressedParams : mExpandedParams);
                            mExpanded = !mExpanded;
                            if (mExpanded) {
                                Log.e(TAG, "onClick: not expanded");
                              //  mBookPageTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                mArrowImageview.setVisibility(View.INVISIBLE);
                            } else{
                                Log.e(TAG, "onClick: expanded");
                              //  mBookPageTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.arrow_small);
                                mArrowImageview.setVisibility(View.VISIBLE);
                            }
                            mCardView.requestLayout();
                                }


                    });
                      LayoutTransition transition = mCardView.getLayoutTransition();
                    transition.enableTransitionType(LayoutTransition.CHANGING);

                }

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
                ((ReadActivity) getActivity()).btnClickWhatBook(bookId, bookName, bookAuthor);
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
