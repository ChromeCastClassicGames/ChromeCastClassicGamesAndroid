package com.appspot.c_three_games_test.war;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.appspot.c_three_games_test.warAPI.WarAPI;
import com.appspot.c_three_games_test.warAPI.model.Game;
import com.appspot.c_three_games_test.warAPI.model.Player;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class War {
    private static WarAPI warAPI;
    private static WarState warState;

    public War() {
        warAPI = new WarAPI.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), null).build();
        warState = new WarState();
    }

    private static void createNewGame() throws IOException {
        Game game = warAPI.createGame().execute();
        warState.setGame(game);
    }

    private static void findGameByGameCode(String gameCode) throws IOException {
        Game game = warAPI.getGameByCode(gameCode).execute();
        warState.setGame(game);
    }

    private static void findGameByGameId(Long gameId) throws IOException {
        Game game = warAPI.getGame(gameId).execute();
        warState.setGame(game);
    }

    private static void joinGame(String name, String gcm) throws IOException {
        Player player = null;
        if (warState.getGame() != null) {
            player = warAPI.joinGame(warState.getGame().getId(), name).execute();
        }
        if ((warState.getGame() != null) && (player != null)) {
            player = warAPI.setPlayerRegId(warState.getGame().getId(), player.getId(), gcm).execute();
        }
        warState.setPlayer(player);
    }

    private static void findPlayerById(Long playerId) throws IOException {
        Player player = null;
        if (warState.getGame() != null) {
            player = warAPI.getPlayer(warState.getGame().getId(), playerId).execute();
        }
        warState.setPlayer(player);
    }

    private static void findPlayers() throws IOException {
        List<Player> players = new ArrayList<>();
        if (warState.getGame() != null) {
            players = warAPI.getPlayers(warState.getGame().getId()).execute().getItems();
            warState.setPlayers(players);
        }
    }

    private static boolean isGameStarted() {
        String gameState;
        if (warState.getGame() != null) {
            gameState = warState.getGame().getState();
            switch (gameState) {
                case "PLAYING":
                case "EVALUATING":
                case "ROUNDOVER":
                case "OVER":
                    return true;
                case "JOINING":
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    private static void startGame() throws IOException {
        if (warState.getGame() != null) {
            Game game = warAPI.startGame(warState.getGame().getId()).execute();
            warState.setGame(game);
        }
    }

    public static class CreateGame extends War {
        private String name;
        private String gameCode;
        private Long gameId;
        private Context context;
        private ProgressDialog progressDialog;

        public CreateGame(Context context) {
            super();
            this.context = context;
            progressDialog = new ProgressDialog(context);
            gameCode = "";
        }

        public void execute() {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    progressDialog.setTitle(context.getString(R.string.please_wait));
                    progressDialog.setMessage(context.getString(R.string.loading_game));
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    progressDialog.dismiss();
                    Log.d("WAR", "stop");
                    if ((warState != null) && (warState.getGame() != null) && (warState.getPlayer() != null)) {
                        Intent intent = new Intent(context, GameActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("gameId", warState.getGame().getId());
                        intent.putExtra("playerId", warState.getPlayer().getId());
                        intent.putExtra("warState", warState.toString());
                        context.startActivity(intent);
                    }
                }

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        //create a new game, or find an existing with id or game code
                        if (gameId != null) {
                            findGameByGameId(getGameId());
                        } else if (!gameCode.isEmpty()) {
                            findGameByGameCode(getGameCode());
                        } else {
                            createNewGame();
                        }
                        //join game
                        joinGame(getName(), "");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();

        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGameCode() {
            return gameCode;
        }

        public void setGameCode(String gameCode) {
            this.gameCode = gameCode;
        }

        public Long getGameId() {
            return gameId;
        }

        public void setGameId(Long gameId) {
            this.gameId = gameId;
        }

    }

    public static class PlayGame extends War {
        private Long gameId;
        private Long playerId;
        private Context context;
        private ProgressDialog progressDialog;
        private DialogFragment playersDialog;

        public PlayGame(Context context) {
            super();
            this.context = context;
            progressDialog = new ProgressDialog(context);
        }

        public void loadGameState() {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    progressDialog.setTitle(context.getString(R.string.please_wait));
                    progressDialog.setMessage(context.getString(R.string.loading_game));
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    progressDialog.dismiss();
                    if (!isGameStarted()) {
                        playersDialog = new PlayersDialog();
                        playersDialog.show(((FragmentActivity) context).getSupportFragmentManager(), "players");
                    }
                }

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        findGameByGameId(gameId);
                        findPlayerById(playerId);
                        findPlayers();
                        return null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();
        }

        public void startGame() {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        War.startGame();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();
        }

        public Long getPlayerId() {
            return playerId;
        }

        public void setPlayerId(Long playerId) {
            this.playerId = playerId;
        }

        public Long getGameId() {
            return gameId;
        }

        public void setGameId(Long gameId) {
            this.gameId = gameId;
        }
    }

    public static class WarState {
        private Game game;
        private Player player;
        private List<Player> players;

        public WarState() {
            players = new ArrayList<>();
        }

        public Game getGame() {
            return game;
        }

        public void setGame(Game game) {
            this.game = game;
        }

        public Player getPlayer() {
            return player;
        }

        public void setPlayer(Player player) {
            this.player = player;
        }

        public List<Player> getPlayers() {
            return players;
        }

        public void setPlayers(List<Player> players) {
            this.players = players;
        }
    }
}
