package com.serediuk.checkers;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.serediuk.checkers.model.BoardCell;
import com.serediuk.checkers.model.CheckersModel;
import com.serediuk.checkers.model.CheckersPiece;
import com.serediuk.checkers.model.emuns.Player;
import com.serediuk.checkers.util.CheckersDelegate;
import com.serediuk.checkers.view.CheckersView;

import java.util.ArrayList;
import java.util.Set;

public class PlayWithBotActivity extends AppCompatActivity implements CheckersDelegate {
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
                .setTitle(R.string.text_warning)
                .setMessage(R.string.text_changing)
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
                .setTitle(R.string.text_warning)
                .setMessage(R.string.text_restarting)
                .setPositiveButton("Так", (dialog, which) -> {
                    checkersModel.restart();
                    checkersView.invalidate();
                })
                .setNegativeButton("Ні", (dialog, which) -> {})
                .show();
    }

    @Override
    public CheckersPiece pieceAt(BoardCell cell) {
        return checkersModel.pieceAt(cell);
    }

    @Override
    public boolean movePiece(BoardCell from, BoardCell to) {
        boolean res = checkersModel.movePiece(from, to);
        ((TextView) findViewById(R.id.turn_title)).setText(getTurn() == Player.WHITE ? R.string.white_move_title : R.string.black_move_title);
        findViewById(R.id.checkers_deck).invalidate();
        return res;
    }

    @Override
    public Player getTurn() {
        return checkersModel.getTurn();
    }

    @Override
    public ArrayList<BoardCell> getCorrectMovesForPiece(CheckersPiece piece) {
        return checkersModel.getCorrectMovesForPiece(piece);
    }

    @Override
    public ArrayList<BoardCell> getTakenPieces() {
        return checkersModel.getTakenPieces();
    }

    @Override
    public int getWhiteCount() {
        return checkersModel.getWhiteCount();
    }

    @Override
    public int getBlackCount() {
        return checkersModel.getBlackCount();
    }
}