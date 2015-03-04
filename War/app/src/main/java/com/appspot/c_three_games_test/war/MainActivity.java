package com.appspot.c_three_games_test.war;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;


public class MainActivity extends ActionBarActivity implements NameDialog.DialogListener, GameCodeDialog.DialogListener, CodeSelectionDialog.DialogListener, GameSelectionDialog.DialogListener {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = MainActivity.class.getName();
    War.CreateGame createGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createGame = new War.CreateGame(MainActivity.this);

        Button new_game_button = (Button) findViewById(R.id.new_game_button);
        new_game_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNameDialog();
            }
        });

        Button continue_game_button = (Button) findViewById(R.id.continue_game_button);
        continue_game_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ContinueGameActivity.class);
                startActivity(intent);
            }
        });

        // Check device for Play Services APK
        checkPlayServices();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogSetName(DialogFragment dialog, String name) {
        createGame.setName(name);
        Log.d(TAG, "name: " + name);
        showGameSelectionDialog();
    }

    @Override
    public void onDialogGameSelection(DialogFragment dialog, int selection) {
        Log.d(TAG, "new game: " + selection);
        switch (selection) {
            case 0: // new game
                createGame.execute();
                break;
            case 1: // join existing game
                showGameCodeSelectionDialog();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDialogSelectGameCode(DialogFragment dialog, int selection) {
        Log.d(TAG, "game code selection: " + selection);
        switch (selection) {
            case 0: //enter game code
                showGameCodeDialog();
                break;
            case 1: //scan qr code
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, 0);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String gameId = intent.getStringExtra("SCAN_RESULT");
                createGame.setGameId(Long.parseLong(gameId));
                createGame.execute();
            }
            //todo: handle canceled case
        }
    }

    @Override
    public void onDialogSetGameCode(DialogFragment dialog, final String gameCode) {
        createGame.setGameCode(gameCode);
        createGame.execute();
    }

    private void showNameDialog() {
        DialogFragment newFragment = new NameDialog();
        newFragment.show(getSupportFragmentManager(), "name");
    }

    private void showGameSelectionDialog() {
        DialogFragment newFragment = new GameSelectionDialog();
        newFragment.show(getSupportFragmentManager(), "new game selection");
    }

    private void showGameCodeSelectionDialog() {
        DialogFragment newFragment = new CodeSelectionDialog();
        newFragment.show(getSupportFragmentManager(), "code select");
    }

    private void showGameCodeDialog() {
        DialogFragment newFragment = new GameCodeDialog();
        newFragment.show(getSupportFragmentManager(), "game code");
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

}
