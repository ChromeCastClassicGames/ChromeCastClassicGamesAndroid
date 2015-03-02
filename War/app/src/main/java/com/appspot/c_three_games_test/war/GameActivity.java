package com.appspot.c_three_games_test.war;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class GameActivity extends ActionBarActivity implements PlayersDialog.DialogListener {

    War.PlayGame war;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_game);

        TextView gameIdText = (TextView) findViewById(R.id.gameId);
        TextView playerIdText = (TextView) findViewById(R.id.playerId);

        Long gameId = intent.getLongExtra("gameId", -1);
        Long playerId = intent.getLongExtra("playerId", -1);

        gameIdText.setText(gameId.toString());
        playerIdText.setText(playerId.toString());

        war = new War.PlayGame(GameActivity.this);
        war.setGameId(gameId);
        war.setPlayerId(playerId);
        war.loadGameState();
    }

    @Override
    public void onDialogOnCreate(DialogFragment dialog) {
        ListView playersListView = (ListView) dialog.getDialog().findViewById(R.id.players);
        String[] testStrings = {"Alan", "Deborah", "Lois"};
        ArrayAdapter<String> playersArrayAdapter = new ArrayAdapter<>(dialog.getActivity(), R.layout.players_dialog, R.id.textView, testStrings);
        playersListView.setAdapter(playersArrayAdapter);
    }

    @Override
    public void onDialogStartGameClicked(DialogFragment dialog) {
        Log.d("WAR", "start game clicked");
        war.startGame();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
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
}
