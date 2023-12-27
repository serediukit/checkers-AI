package com.serediuk.checkers;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.serediuk.checkers.model.CheckersModel;
import com.serediuk.checkers.model.CheckersPiece;
import com.serediuk.checkers.util.CheckersDelegate;
import com.serediuk.checkers.view.CheckersView;

public class PlayWithBotActivity extends AppCompatActivity implements CheckersDelegate {
    private final String TAG = "PlayWithBot";
    private CheckersModel checkersModel = CheckersModel.getInstance();
    private CheckersView checkersView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_with_bot);
        checkersView = findViewById(R.id.checkers_deck);
        checkersView.setCheckersDelegate(this);
    }

    public void changeTurn(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Попередження")
                .setMessage("Ви точно хочете змінити сторону?\nЦя гра буде врахована як програна.")
                .setPositiveButton("Так", (dialog, which) -> {
                    checkersModel.changeTurn();
                    checkersView.invalidate();
                })
                .setNegativeButton("Ні", (dialog, which) -> {})
                .show();
    }

    public void restartGame(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Попередження")
                .setMessage("Ви точно хочете почати спочатку?\nЦя гра буде врахована як програна.")
                .setPositiveButton("Так", (dialog, which) -> {
                    checkersModel.restart();
                    checkersView.invalidate();
                })
                .setNegativeButton("Ні", (dialog, which) -> {})
                .show();
    }

    @Override
    public CheckersPiece pieceAt(int row, int col) {
        return checkersModel.pieceAt(row, col);
    }

    @Override
    public boolean movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        boolean res = checkersModel.movePiece(fromRow, fromCol, toRow, toCol);
        findViewById(R.id.checkers_deck).invalidate();
        return res;
    }
}