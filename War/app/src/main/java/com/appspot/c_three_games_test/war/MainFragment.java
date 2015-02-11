package com.appspot.c_three_games_test.war;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainFragment extends Fragment {

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        Button create_new_game_button = (Button) rootView.findViewById(R.id.create_new_game_button);
        create_new_game_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainFragment.this.getActivity(), CreateNewGame.class);
                startActivity(intent);
            }
        });

        Button join_existing_game_button = (Button) rootView.findViewById(R.id.join_existing_game_button);
        join_existing_game_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainFragment.this.getActivity(), JoinExistingGame.class);
                startActivity(intent);
            }
        });

        Button continue_game_button = (Button) rootView.findViewById(R.id.continue_game_button);
        continue_game_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainFragment.this.getActivity(), ContinueGame.class);
                startActivity(intent);
            }
        });

        return rootView;
    }
}
