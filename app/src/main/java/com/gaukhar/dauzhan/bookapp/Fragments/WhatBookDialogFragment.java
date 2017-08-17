package com.gaukhar.dauzhan.bookapp.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.gaukhar.dauzhan.bookapp.R;


public class WhatBookDialogFragment extends DialogFragment {

    public interface NoticeDialogListener{
         void onDialogPositiveClick(int id, String bookName, String bookAuthor);
    }

    NoticeDialogListener mListener ;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final String[] args = getArguments().getStringArray("book details");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final int bookId = Integer.parseInt(args[0]);
        final String bookName = args[1];
        final String bookAuthor = args[2];


        builder.setMessage(bookAuthor+"\n"+'"'+bookName+'"')
                .setPositiveButton(R.string.add_list, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        mListener.onDialogPositiveClick(bookId, bookName, bookAuthor);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                            }
                        };
                    }
                });
        AlertDialog alert = builder.create();
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
