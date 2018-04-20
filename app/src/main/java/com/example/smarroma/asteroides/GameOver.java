package com.example.smarroma.asteroides;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by smarroma on 17/04/2018.
 */

public class GameOver extends Fragment {

    private Button btBack;
    private Button btScore;
    private Button btTry;
    private int score = -1;
    private String name = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflater.inflate(R.layout.gameover, container, false);
        //View view = getView();
        this.score = getArguments().getInt("score");

        View view = super.onCreateView(inflater, container, savedInstanceState);
        // + o -
        view.inflate(getContext(), R.layout.gameover, container);
        //view.setBackgroundColor(Color.TRANSPARENT);
//null Object Reference
        btBack = (Button) getActivity().findViewById(R.id.btBack);
        btScore = (Button) getActivity().findViewById(R.id.btScore);
        btTry = (Button) getActivity().findViewById(R.id.btTry);

        this.name = MainActivity.getNombre();

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBack();
            }
        });

        btScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickScore();
            }
        });

        btTry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickTry();
            }
        });

        return view;
    }

    /**
     * Guardamos puntuación al darle a este botón
     */
    public void clickBack() {
        saveScore();
        Intent i = new Intent(getContext(), MainActivity.class);
        startActivity(i);
    }

    //Puntuacion
    public void clickScore() {
        Intent i = new Intent(getContext(), Puntuacions.class);
        startActivity(i);
    }


    public void clickTry() {
        Intent i = new Intent(getContext(), Juego.class);
        startActivity(i);
    }

    public void saveScore(){

        Puntuacions.guardarPuntuacio(getContext(), this.name, this.score);

    }

}
