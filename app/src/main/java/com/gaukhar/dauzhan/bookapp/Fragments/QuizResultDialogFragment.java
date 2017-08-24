package com.gaukhar.dauzhan.bookapp.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.gaukhar.dauzhan.bookapp.QuizActivity;
import com.gaukhar.dauzhan.bookapp.R;
import com.gaukhar.dauzhan.bookapp.ReadActivity;


public class QuizResultDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int result = getArguments().getInt("mScore");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View dialog = inflater.inflate(R.layout.quiz_result_dialog_fragment, null);
        builder.setView(dialog);
        TextView quizResultTextView = (TextView) dialog.findViewById(R.id.dialog_quiz_result);
        quizResultTextView.setText(result +"/5");

        builder.setPositiveButton("Сыграть еще", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ((QuizActivity)getActivity()).finish();
                        Intent intent = new Intent(getContext(), QuizActivity.class);
                        startActivity(intent);

                    }
                })
                .setNegativeButton("Закончить", new DialogInterface.OnClickListener() {
                                        @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getContext(), ReadActivity.class);
                        startActivity(intent);
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
}
