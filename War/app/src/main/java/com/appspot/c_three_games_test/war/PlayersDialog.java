package com.appspot.c_three_games_test.war;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;


public class PlayersDialog extends DialogFragment {

    DialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (DialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString() + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final AlertDialog dialog = builder.setTitle(R.string.players)
                .setView(inflater.inflate(R.layout.players_dialog, null))
                .setPositiveButton(R.string.start_game, null)
                .create();
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        final AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog != null) {
            mListener.onDialogOnCreate(PlayersDialog.this);
            Button positiveButton = (Button) dialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //todo: start game
                    mListener.onDialogStartGameClicked(PlayersDialog.this);
                }
            });
        }
    }

    public interface DialogListener {
        public void onDialogOnCreate(DialogFragment dialog);

        public void onDialogStartGameClicked(DialogFragment dialog);
    }
}
