package com.serediuk.checkers;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;

import com.serediuk.checkers.model.CheckersModel;
import com.serediuk.checkers.view.CheckersView;

public class PlayWithBot extends AppCompatActivity {
    private final String TAG = "PlayWithBot";
    private CheckersModel checkersModel = new CheckersModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_with_bot);

        Log.d(TAG, checkersModel.toString());

        CheckersView checkersView = findViewById(R.id.checkers_deck);
        checkersView.setMinimumHeight(800);
        checkersView.setMinimumWidth(800);
    }

    public void changeTurn(View view) {
        checkersModel.changeTurn();
        Log.d(TAG, checkersModel.toString());
    }
}