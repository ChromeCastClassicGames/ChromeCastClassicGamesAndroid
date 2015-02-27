package com.appspot.c_three_games_test.war;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class GameCodeDialog extends DialogFragment {

    public static final String PREFS_NAME = "WarSharedPreferences";
    DialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (DialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString() + " must implement DialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final AlertDialog dialog = builder.setTitle(R.string.enter_code)
                .setView(inflater.inflate(R.layout.game_code_dialog, null))
                .setPositiveButton(R.string.next, null)
                .create();
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        final AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog != null) {
            Button positiveButton = (Button) dialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText editTextGameCode = (EditText) dialog.findViewById(R.id.game_code);
                    String gameCode = editTextGameCode.getText().toString();
                    if (gameCode.length() == 4) {
                        //TODO: evauluate game code
                        mListener.onDialogSetGameCode(GameCodeDialog.this, gameCode);
                        dismiss();
                    }
                }
            });
        }
    }

    public interface DialogListener {
        public void onDialogSetGameCode(DialogFragment dialog, String gameCode);
    }
}
