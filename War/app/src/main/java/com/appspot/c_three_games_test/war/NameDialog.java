package com.appspot.c_three_games_test.war;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NameDialog extends DialogFragment {

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
        final AlertDialog dialog = builder.setTitle(R.string.name)
                .setView(inflater.inflate(R.layout.name_dialog, null))
                .setPositiveButton(R.string.next, null)
                .create();
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        final AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog != null) {
            SharedPreferences settings = getActivity().getSharedPreferences(getString(R.string.shared_pref), 0);
            String name = settings.getString("name", "");
            EditText editTextName = (EditText) dialog.findViewById(R.id.name);
            editTextName.setText(name);

            Button positiveButton = (Button) dialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText editTextName = (EditText) dialog.findViewById(R.id.name);
                    String name = editTextName.getText().toString();
                    if (name.length() > 0) {
                        mListener.onDialogSetName(NameDialog.this, name);
                        SharedPreferences settings = getActivity().getSharedPreferences(getString(R.string.shared_pref), 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("name", name);
                        editor.apply();
                        dismiss();
                    }
                }
            });
        }
    }

    public interface DialogListener {
        public void onDialogSetName(DialogFragment dialog, String name);
    }
}
