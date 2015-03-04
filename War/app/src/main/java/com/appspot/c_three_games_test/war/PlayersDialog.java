package com.appspot.c_three_games_test.war;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;


public class PlayersDialog extends DialogFragment {

    private static final String TAG = "TAG";
    DialogListener mListener;
    private BroadcastReceiver mMessageReceiver;
    private ProgressDialog progressDialog;

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
//        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    return true;
//                }
//                return false;
//            }
//        });
        progressDialog = new ProgressDialog(getActivity());
        mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String message = intent.getStringExtra("message");
                switch (message) {
                    case "game started":
                        progressDialog.dismiss();
                        dialog.dismiss();
                        break;
                    default:
                        Log.i(TAG, "invalid message received:" + message);
                        break;
                }
            }
        };
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter(getString(R.string.players_dialog_broadcast)));
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        final AlertDialog dialog = (AlertDialog) getDialog();

        if (dialog != null) {
            mListener.onDialogOnCreate(PlayersDialog.this);
            Button positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onDialogStartGameClicked(PlayersDialog.this);
                    progressDialog.setTitle(getString(R.string.please_wait));
                    progressDialog.setMessage(getString(R.string.loading_game));
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            });
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
        super.onDismiss(dialog);
    }

    public interface DialogListener {
        public void onDialogOnCreate(DialogFragment dialog);
        public void onDialogStartGameClicked(DialogFragment dialog);
    }
}
