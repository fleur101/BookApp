package com.gaukhar.dauzhan.bookapp.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.gaukhar.dauzhan.bookapp.DrawerActivity;
import com.gaukhar.dauzhan.bookapp.R;


public class WhatBookDialogFragment extends DialogFragment {

    public interface NoticeDialogListener{
         void onDialogPositiveClick(int id, String bookName, String bookAuthor);
    }

    NoticeDialogListener mListener ;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String[] args = getArguments().getStringArray("book details");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final int bookId = Integer.parseInt(args[0]);
        final String bookName = args[1];
        final String bookAuthor = args[2];
        final String fontType = args[3];
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View dialog = inflater.inflate(R.layout.what_book_dialog_fragment, null);
        builder.setView(dialog);
        TextView bookNameTextView = (TextView) dialog.findViewById(R.id.dialog_book_name);
        TextView bookAuthorTextView = (TextView) dialog.findViewById(R.id.dialog_book_author);
        switch (fontType){
            case "font_helvetica":
                bookNameTextView.setTypeface(DrawerActivity.font_helvetica);
                bookAuthorTextView.setTypeface(DrawerActivity.font_helvetica);
                break;

            case "font_roboto":
                bookNameTextView.setTypeface(DrawerActivity.font_roboto);
                bookAuthorTextView.setTypeface(DrawerActivity.font_roboto);
                break;

            case "font_kurale":
                bookNameTextView.setTypeface(DrawerActivity.font_kurale);
                bookAuthorTextView.setTypeface(DrawerActivity.font_kurale);
                break;

        }

//        bookNameTextView.setTypeface(DrawerActivity.font_helvetica);
//        bookAuthorTextView.setTypeface(DrawerActivity.font_helvetica);

        bookNameTextView.setText('"' + bookName + '"');
        bookAuthorTextView.setText(bookAuthor);


        builder.setPositiveButton(R.string.add_list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                mListener.onDialogPositiveClick(bookId, bookName, bookAuthor);
                dialog.dismiss();
            }
        })
                .setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                            }
                        };
                    }
                });
        final AlertDialog alert = builder.create();
        alert.setOnShowListener( new DialogInterface.OnShowListener() {
              @Override
              public void onShow(DialogInterface arg0) {
                  alert.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccentDark));
                  alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccentDark));
              }
          });

        return alert;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity ReadPagerActivity = null;

        if (context instanceof Activity) {
            ReadPagerActivity = (Activity) context;
        }
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) ReadPagerActivity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(ReadPagerActivity.toString() + " must implement NoticeDialogListener");
        }
    }



    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
