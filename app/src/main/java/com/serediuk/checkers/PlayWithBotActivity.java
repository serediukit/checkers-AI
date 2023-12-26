package com.serediuk.checkers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.serediuk.checkers.model.CheckersModel;
import com.serediuk.checkers.model.CheckersPiece;
import com.serediuk.checkers.util.CheckersDelegate;
import com.serediuk.checkers.view.CheckersView;

public class PlayWithBotActivity extends AppCompatActivity implements CheckersDelegate {
    private final String TAG = "PlayWithBot";
    private CheckersModel checkersModel = new CheckersModel();
    private CheckersView checkersView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_with_bot);

        Log.d(TAG, checkersModel.toString());

        checkersView = findViewById(R.id.checkers_deck);
        checkersView.setCheckersDelegate(this);
    }

    public void changeTurn(View view) {
        checkersModel.changeTurn();
        Log.d(TAG, checkersModel.toString());
    }

    @Override
    public CheckersPiece pieceAt(int row, int col) {
        return checkersModel.pieceAt(row, col);
    }
}